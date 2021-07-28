package cn.edu.guet.popular_blog.service;

import cn.edu.guet.popular_blog.pojo.AccessToken;
import cn.edu.guet.popular_blog.pojo.WxUserInfo;

/**
 * @author pangjian
 * @ClassName WxLoginService
 * @Description TODO
 * @date 2021/7/21 16:28
 */

public interface WxLoginService {

    String getWxLoginUrl(String redirectUrl);

    AccessToken getWxAccessToken(String code);

    WxUserInfo getWxUserInfo(String access_token, String openId);

}
