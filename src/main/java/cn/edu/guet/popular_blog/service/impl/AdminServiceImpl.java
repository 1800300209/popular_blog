package cn.edu.guet.popular_blog.service.impl;

import cn.edu.guet.popular_blog.dto.MailRegisterDTO;
import cn.edu.guet.popular_blog.dto.RegisterDTO;
import cn.edu.guet.popular_blog.dto.UpdateAdminDTO;
import cn.edu.guet.popular_blog.dto.UpdatePwdDTO;
import cn.edu.guet.popular_blog.mapper.AdminMapper;
import cn.edu.guet.popular_blog.pojo.Admin;
import cn.edu.guet.popular_blog.respbean.RespBean;
import cn.edu.guet.popular_blog.service.AdminService;
import cn.edu.guet.popular_blog.service.MailService;
import cn.edu.guet.popular_blog.util.*;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pangjian
 * @ClassName AdminServiceImpl
 * @Description 用户业务实现类
 * @date 2021/7/16 17:57
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper,Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MailService mailService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${fileRootPath}")
    private String fileRootPath;

    @Override
    public void test(){
        adminMapper.test();
    }


    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).eq("enabled",true));
    }

    @Override
    public Admin getAdminByMail(String mail){
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("mail",mail).eq("enabled", true));
    }

    /**
     * @Description: 登录业务
     * @Param username: 用户名
     * @Param password: 密码
     * @return cn.edu.guet.popular_blog.respbean.RespBean
     * @date 2021/7/17 15:40
    */
    @Override
    public RespBean login(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails == null || !passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error("用户名或密码错误");
        }
        if(!userDetails.isEnabled()){
            return RespBean.error("该用户名被锁定,请联系管理员");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,Object> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenhead",tokenHead);
        return RespBean.success("登录成功",tokenMap);
    }


    @Override
    public RespBean register(RegisterDTO registerDTO, HttpServletRequest httpServletRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(registerDTO.getUsername());
        String code = (String) httpServletRequest.getServletContext().getAttribute("code");
        if(!code.equalsIgnoreCase(registerDTO.getCode())){
            return RespBean.error("请输入正确的验证码");
        }
        if(userDetails != null){
            return RespBean.error("请修改用户名，该用户名已经被使用");
        }
        if(registerDTO.getPassword() == null || registerDTO.getRepwd() == null){
            return RespBean.error("密码和确认密码都不能为空");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getRepwd())){
            return RespBean.error("确认密码错误，请重新确认密码");
        }
        String password = passwordEncoder.encode(registerDTO.getPassword());
        adminMapper.insertOneAdmin(registerDTO.getUsername(),password,TimeUtil.getCurrentTime());
        return RespBean.success("注册成功");
    }

    @Transactional()
    @Override
    public RespBean updateAdmin(UpdateAdminDTO updateAdminDTO) {
        MultipartFile[] files = updateAdminDTO.getFiles();
        if(files != null){
            String imgPath = UploadUtil.fileUpload(files,fileRootPath);
            updateAdminDTO.setImgPath(imgPath);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        updateAdminDTO.setUsername(username);
        adminMapper.updateAdmin(updateAdminDTO);
        return RespBean.success("更新成功");
    }

    @Transactional
    @Override
    public RespBean updatePwd(UpdatePwdDTO updatePwdDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(!passwordEncoder.matches(updatePwdDTO.getPassword(),userDetails.getPassword())){
            return RespBean.error("旧密码错误");
        }
        if(!updatePwdDTO.getNewPwd().equals(updatePwdDTO.getRePwd())){
            return RespBean.error("新密码和确认密码不一致，请重新确认");
        }
        String password = passwordEncoder.encode(updatePwdDTO.getNewPwd());
        adminMapper.updatePwd(password, username);
        return RespBean.success("修改成功");
    }


    @Override
    public RespBean activationRegister(String confirmCode) {
        String obj = redisUtil.getValue(confirmCode);
        if ("".equals(obj)){
            return RespBean.error("激活失败,链接已超24小时，请重新注册");
        }
        MailRegisterDTO mailRegisterDTO = JSON.parseObject(obj, MailRegisterDTO.class);
        adminMapper.insertByMail(mailRegisterDTO.getMail(), mailRegisterDTO.getPassword(), mailRegisterDTO.getRegisterTime());
        return RespBean.success("激活成功");
    }


    @Transactional
    @Override
    public RespBean register(MailRegisterDTO mailRegisterDTO) {
        if(!mailRegisterDTO.getPassword().equals(mailRegisterDTO.getRePwd())){
            return RespBean.error("确认密码不一致，请重新查看");
        }
        // 生成确认码
        String confirmCode = IdUtil.getSnowflake(1,1).nextIdStr();
        log.info("===========" + confirmCode);
        mailRegisterDTO.setRegisterTime(TimeUtil.getCurrentTime());
        // 密码加密
        mailRegisterDTO.setPassword(passwordEncoder.encode(mailRegisterDTO.getPassword()));
        // 存入redis缓存
        log.info("+++++++++++++++++++++++" + JSONUtil.toJsonStr(mailRegisterDTO));
        redisUtil.creatByTimeout(confirmCode, JSON.toJSONString(mailRegisterDTO));
        String registerMailUrl = "http://localhost:8989/activation?confirmCode=" + confirmCode;
        mailService.sendMailForRegister(registerMailUrl, mailRegisterDTO.getMail());
        return RespBean.success("注册成功，请前往你的邮箱激活账号");
    }
}
