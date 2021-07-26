package cn.edu.guet.popular_blog.config;

import cn.edu.guet.popular_blog.filter.JwtFilter;
import cn.edu.guet.popular_blog.mapper.MenuMapper;
import cn.edu.guet.popular_blog.mapper.RoleMapper;
import cn.edu.guet.popular_blog.pojo.Admin;
import cn.edu.guet.popular_blog.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * @author pangjian
 * @ClassName SecurityConfig
 * @Description SpringSecurity配置
 * @date 2021/7/17 10:36
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/css/**",
                "/js/**",
                "/index.html",
                "/static/html/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers("/login","/logout","/getImage","/register","/registerByMail","/activation").permitAll()
                    .antMatchers("test1").authenticated()
                    .anyRequest().permitAll()
                .and()
                .headers()
                .cacheControl();

        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());

    }


    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        return username-> {
            Admin admin = adminService.getAdminByUserName(username);

            if(admin == null){
                admin = adminService.getAdminByMail(username);
            }

            if(null!=admin){
                List<String> roleList = roleMapper.selectRoleByUsername(admin.getUsername());

                List<String> authorities = menuMapper.selectMenuByRole(admin.getUsername());

                authorities.addAll(roleList);

                admin.setAuthorities(
                        AuthorityUtils.commaSeparatedStringToAuthorityList(
                                String.join(",",authorities)
                        )
                );
                // 该自定义的用户类已经实现了UserDetails接口，刚好符合了该方法的返回类型
                return admin;
            }
            return null;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtFilter jwtFilter(){
        return new JwtFilter();
    }

}
