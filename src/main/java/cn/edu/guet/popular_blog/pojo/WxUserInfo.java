package cn.edu.guet.popular_blog.pojo;

import lombok.Data;

/**
 * @author pangjian
 * @ClassName WxUserInfo
 * @Description TODO
 * @date 2021/7/21 17:12
 */
@Data
public class WxUserInfo {

    /*
     "openid": "OPENID",
  "nickname": "NICKNAME",
  "sex": 1,
  "province": "PROVINCE",
  "city": "CITY",
  "country": "COUNTRY",
  "headimgurl": "https://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
  "privilege": ["PRIVILEGE1", "PRIVILEGE2"],
  "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
    */

    private String openId;
    private String nickname;
    private Integer sex;
    private String province;
    private String city;
    private String headimgurl;
    private String unionid;
    private String country;



}
