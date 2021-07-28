package cn.edu.guet.popular_blog.pojo;

import lombok.Data;

/**
 * @author pangjian
 * @ClassName AccessToken
 * @Description TODO
 * @date 2021/7/21 17:08
 */
@Data
public class AccessToken {

    private Long expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;
    private String errcode;

}
