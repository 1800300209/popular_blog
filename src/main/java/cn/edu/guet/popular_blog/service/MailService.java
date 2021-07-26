package cn.edu.guet.popular_blog.service;

/**
 * @author pangjian
 * @ClassName MailService
 * @Description TODO
 * @date 2021/7/22 11:14
 */

public interface MailService {

    void sendMailForRegister(String registerMailUrl, String email);

}
