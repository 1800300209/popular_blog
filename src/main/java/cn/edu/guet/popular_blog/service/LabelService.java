package cn.edu.guet.popular_blog.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LabelService {

    List<String> labelList();

    List<String> subLabelList(String pname);

    String getLabel(String article);

    void insertDate(String article,String date);

    void updateDate(String article,String date);
}
