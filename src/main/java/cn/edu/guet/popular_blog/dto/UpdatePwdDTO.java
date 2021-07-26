package cn.edu.guet.popular_blog.dto;

import lombok.Data;

/**
 * @author pangjian
 * @ClassName UpdatePwdDTO
 * @Description 修改密码传输对象
 * @date 2021/7/20 18:00
 */
@Data
public class UpdatePwdDTO {

    private String password;
    private String newPwd;
    private String rePwd;

}
