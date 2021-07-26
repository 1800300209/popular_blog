package cn.edu.guet.popular_blog.controller;

import cn.edu.guet.popular_blog.dto.LoginDTO;
import cn.edu.guet.popular_blog.dto.MailRegisterDTO;
import cn.edu.guet.popular_blog.dto.RegisterDTO;
import cn.edu.guet.popular_blog.pojo.AccessToken;
import cn.edu.guet.popular_blog.pojo.WxUserInfo;
import cn.edu.guet.popular_blog.respbean.RespBean;
import cn.edu.guet.popular_blog.service.AdminService;
import cn.edu.guet.popular_blog.service.WxLoginService;
import cn.edu.guet.popular_blog.util.PropConfig;
import cn.edu.guet.popular_blog.util.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author pangjian
 * @ClassName LoginController
 * @Description 登录注册控制器
 * @date 2021/7/17 15:35
 */
@Controller
@Slf4j
@CrossOrigin //允许跨域
public class LoginController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private WxLoginService wxLoginService;

    /**
     * @Description: 登录控制器
     * @Param loginDTO: 登录传输对象
     * @return cn.edu.guet.popular_blog.respbean.RespBean
     * @date 2021/7/17 15:51
    */
    @ResponseBody
    @PostMapping("/login")
    public RespBean login(@RequestBody LoginDTO loginDTO){
        return adminService.login(loginDTO.getUsername(),loginDTO.getPassword());
    }


    /**
     * @Description: 注册控制器
     * @Param registerDTO: 注册传输对象
     * @return cn.edu.guet.popular_blog.respbean.RespBean
     * @date 2021/7/17 16:00
    */
    @ResponseBody
    @PostMapping("/register")
    public RespBean register(@RequestBody RegisterDTO registerDTO, HttpServletRequest httpServletRequest){
        return adminService.register(registerDTO,httpServletRequest);
    }


    /**
     * @Description: 获取4位验证码的图片控制器
     * @Param request:
     * @return java.lang.String
     * @date 2021/7/17 16:00
    */
    @ResponseBody
    @GetMapping("/getImage")
    public String getImageCode(HttpServletRequest request) throws IOException {
        String code = VerifyCodeUtils.generateVerifyCode(4);
        request.getServletContext().setAttribute("code", code);
        log.info("验证码" + code);
        //将图片转为base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(120, 30, byteArrayOutputStream, code);
        return "data:image/png;base64," + Base64Utils.encodeToString(byteArrayOutputStream.toByteArray());
    }

    @GetMapping("/wxLogin")
    public String wxLoginUrl(){
        String wxLoginUrl = wxLoginService.getWxLoginUrl(PropConfig.getProperty("redirectUrl"));
        return "redirect:" + wxLoginUrl;
    }

    @GetMapping("/callBack")
    @ResponseBody
    public RespBean wxLogin(String code, String state, RedirectAttributes attr, HttpServletRequest httpServletRequest){
        try {
            AccessToken accessToken = wxLoginService.getWxAccessToken(code);
            if(!StringUtils.isBlank(accessToken.getErrcode())){
                return RespBean.error("获取access_token失败");
            }
            WxUserInfo wxUserInfo = wxLoginService.getWxUserInfo(accessToken.getRefresh_token(), accessToken.getOpenid());
            return RespBean.success("获取用户信息成功",wxUserInfo);
        } catch (Exception e){
            e.printStackTrace();
            return RespBean.error("系统异常");
        }
    }

    /**
     * @Description: 激活账号
     * @Param confirmCode: 雪花算法得到唯一的验证码
     * @return cn.edu.guet.popular_blog.respbean.RespBean
     * @date 2021/7/22 11:55
    */
    @GetMapping("/activation")
    @ResponseBody
    public RespBean activation(String confirmCode){
        return adminService.activationRegister(confirmCode);
    }


    /**
     * @Description: 邮箱注册
     * @Param mailRegisterDTO:
     * @return cn.edu.guet.popular_blog.respbean.RespBean
     * @date 2021/7/22 12:30
    */
    @PostMapping("/registerByMail")
    @ResponseBody
    public RespBean register(@RequestBody MailRegisterDTO mailRegisterDTO){
        return adminService.register(mailRegisterDTO);
    }


}
