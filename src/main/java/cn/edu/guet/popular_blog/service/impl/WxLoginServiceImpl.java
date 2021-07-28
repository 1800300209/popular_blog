package cn.edu.guet.popular_blog.service.impl;

import cn.edu.guet.popular_blog.pojo.AccessToken;
import cn.edu.guet.popular_blog.pojo.WxUserInfo;
import cn.edu.guet.popular_blog.service.WxLoginService;
import cn.edu.guet.popular_blog.util.PropConfig;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.net.URLEncoder;

/**
 * @author pangjian
 * @ClassName WxLoginServiceImpl
 * @Description TODO
 * @date 2021/7/21 16:29
 */
@Service
public class WxLoginServiceImpl implements WxLoginService {


    /**
     * @Description: 获取微信登录二维码
     * @Param redirectUrl:
     * @return java.lang.String
     * @date 2021/7/21 16:42
    */
    @Override
    public String getWxLoginUrl(String redirectUrl){
        String context = "共享博文博文系统";
        String appId = String.valueOf(PropConfig.getProperty("appid"));
        String codeUrl = String.valueOf(PropConfig.getProperty("getCode"));
        codeUrl = codeUrl.replace("APPID" , appId);
        try {
            codeUrl = codeUrl.replace("REDIRECT_URI", URLEncoder.encode(redirectUrl, "GBK"));
        } catch (Exception e){
            e.printStackTrace();
        }
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        codeUrl = codeUrl.replace("STATE", aes.decryptStr(context));
        return codeUrl;
    }

    /**
     * @Description: 根据code获取accessToken
     * @Param code:
     * @return cn.edu.guet.popular_blog.pojo.AccessToken
     * @date 2021/7/21 17:33
    */
    @Override
    public AccessToken getWxAccessToken(String code){
        AccessToken accessToken = new AccessToken();
        String getAccessTokenUrl = String.valueOf(PropConfig.getProperty("getAccessToken"));
        String appId = String.valueOf(PropConfig.getProperty("appid"));
        String secret = String.valueOf(PropConfig.getProperty("secret"));
        getAccessTokenUrl = getAccessTokenUrl.replace("APPID",appId);
        getAccessTokenUrl = getAccessTokenUrl.replace("SECRET",secret);
        getAccessTokenUrl = getAccessTokenUrl.replace("CODE", code);
        // get请求获取AccessToken
        String accessTokenStr = HttpUtil.get(getAccessTokenUrl);
        if(!StringUtils.isEmpty(accessTokenStr)){
            accessToken = JSONUtil.toBean(accessTokenStr , AccessToken.class);
        }
        return accessToken;
    }


    /**
     * @Description: 根据access_token和openId获取用户信息
     * @Param access_token:
     * @Param openId:
     * @return cn.edu.guet.popular_blog.pojo.WxUserInfo
     * @date 2021/7/21 17:26
    */
    @Override
    public WxUserInfo getWxUserInfo(String access_token, String openId){
        WxUserInfo wxUserInfo = new WxUserInfo();
        String wxUserInfoUrl = String.valueOf(PropConfig.getProperty("getUserInfo"));
        wxUserInfoUrl = wxUserInfoUrl.replaceAll("ACCESS_TOKEN" , access_token);
        wxUserInfoUrl = wxUserInfoUrl.replaceAll("OPENID" , openId);
        String wxUserInfoStr = HttpUtil.get(wxUserInfoUrl);
        if(!StringUtils.isEmpty(wxUserInfoStr)){
            wxUserInfo = JSONUtil.toBean(wxUserInfoStr , WxUserInfo.class);
        }
        return wxUserInfo;
    }




}
