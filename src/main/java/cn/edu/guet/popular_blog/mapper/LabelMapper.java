package cn.edu.guet.popular_blog.mapper;

import cn.edu.guet.popular_blog.dto.LabelDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LabelMapper {
    String getLabel(String article);
    List<String> getLabelList();
    List<String> getSubLabelList(String pname);
    void insertDate(@Param("article") String article,@Param("date") String date);
    void updateDate(@Param("article") String article,@Param("date") String date);
}
