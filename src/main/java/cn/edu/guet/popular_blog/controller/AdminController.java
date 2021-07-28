package cn.edu.guet.popular_blog.controller;

import cn.edu.guet.popular_blog.dto.UpdateAdminDTO;
import cn.edu.guet.popular_blog.dto.UpdatePwdDTO;
import cn.edu.guet.popular_blog.pojo.Admin;
import cn.edu.guet.popular_blog.respbean.RespBean;
import cn.edu.guet.popular_blog.service.AdminService;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author pangjian
 * @ClassName TestController
 * @Description 用户控制器
 * @date 2021/7/16 17:55
 */
@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;


    // @PreAuthorize("hasAuthority('/employee/basic1/**')")
    @ResponseBody
    @GetMapping("/test")
    public String test(){
        adminService.test();
        return "hello world";
    }

    @PostMapping("/updateAdmin")
    @ResponseBody
    public RespBean updateAdmin(UpdateAdminDTO updateAdminDTO){
        return adminService.updateAdmin(updateAdminDTO);
    }

    @PostMapping("/updatePwd")
    @ResponseBody
    public RespBean updatePwd(@RequestBody UpdatePwdDTO updatePwdDTO){
        return adminService.updatePwd(updatePwdDTO);
    }

    @PostMapping("/findAdmin")
    @ResponseBody
    public RespBean findAdmin(){
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(admin == null){
            return RespBean.error("您还未登录");
        }
        admin.setPassword(null);
        return RespBean.success("成功查询",admin);
    }

    @GetMapping("/getPerson")
    public String person(){
        return "person_msg";
    }

}
