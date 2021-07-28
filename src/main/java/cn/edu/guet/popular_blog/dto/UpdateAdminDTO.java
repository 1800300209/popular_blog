package cn.edu.guet.popular_blog.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author pangjian
 * @ClassName UpdateAdminDTO
 * @Description 修改用户传输类
 * @date 2021/7/20 17:00
 */
@Data
public class UpdateAdminDTO {

    private String username;
    private String realName;
    private String phone;
    private Integer age;
    private String imgPath;
    private String mail;
    private MultipartFile[] files;

}
