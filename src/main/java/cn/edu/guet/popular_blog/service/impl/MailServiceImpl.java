package cn.edu.guet.popular_blog.service.impl;

import cn.edu.guet.popular_blog.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @author pangjian
 * @ClassName MailServiceImpl
 * @Description TODO
 * @date 2021/7/22 11:14
 */
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * @Description: 发送注入邮件
     * @Param registerMailUrl:
     * @Param email:
     * @return void
     * @date 2021/7/22 11:22
    */
    @Override
    public void sendMailForRegister(String registerMailUrl, String email){

        // 创建邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            // 创建邮件主题
            messageHelper.setSubject("欢迎来到共享博客 - 个人账号激活");
            // 创建邮件发送者
            messageHelper.setFrom(mailUsername);
            // 创建邮件接受者
            messageHelper.setTo(email);
            // 创建隐秘抄送者
            // 设置邮件发送日期
            messageHelper.setSentDate(new Date());
            // 创建上下文对象
            Context context = new Context();
            context.setVariable("registerMailUrl", registerMailUrl);
            // 加载模板
            String text = templateEngine.process("active-mail.html", context);
            // 邮件发送html的模板邮件
            messageHelper.setText(text, true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }



}
