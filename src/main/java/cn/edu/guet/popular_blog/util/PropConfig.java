package cn.edu.guet.popular_blog.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

/**
 * @author pangjian
 * @ClassName ProConfig
 * @Description TODO
 * @date 2021/7/21 16:20
 */
@Slf4j
public class PropConfig {

    private static Properties props = new Properties();

    static {
        init();
    }

    public static void init(){
        try {
            String[] paths = new String[]{"wxlogin.properties"};

            for (String path : paths){
                ClassPathResource resource = new ClassPathResource(path);
                Properties properties = new Properties();
                properties.load(resource.getInputStream());
                props.putAll(properties);
            }
            log.info("读取微信登录配置文件成功");
        } catch (IOException e) {
            log.error("无法读取配置文件");
            e.printStackTrace();
        }
    }


    public static Properties getProps(){
        return props;
    }

    public static String getProperty(String key){
        return props.getProperty(key);
    }

}
