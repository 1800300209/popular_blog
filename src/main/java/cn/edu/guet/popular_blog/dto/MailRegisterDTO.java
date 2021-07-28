package cn.edu.guet.popular_blog.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pangjian
 * @ClassName MailRegisterDTO
 * @Description 邮箱注册传输类
 * @date 2021/7/22 12:24
 */
@Data
public class MailRegisterDTO implements Serializable {

    private String mail;
    private String password;
    private transient String rePwd;
    private String RegisterTime;

}
