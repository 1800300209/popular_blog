package cn.edu.guet.popular_blog.mapper;

import cn.edu.guet.popular_blog.dto.LoginDTO;
import cn.edu.guet.popular_blog.dto.UpdateAdminDTO;
import cn.edu.guet.popular_blog.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author pangjian
 * @ClassName AdminMapper
 * @Description 用户数据库访问接口
 * @date 2021/7/16 17:55
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    LoginDTO test();

    void insertOneAdmin(String username, String password,String time);

    void updateAdmin(UpdateAdminDTO updateAdminDTO);

    void updatePwd(String newPwd, String username);

    void insertByMail(String mail, String password, String time);
}
