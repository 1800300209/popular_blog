package cn.edu.guet.popular_blog.service.impl;

import cn.edu.guet.popular_blog.mapper.LabelMapper;
import cn.edu.guet.popular_blog.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelMapper labelMapper;

    @Override
    public List<String> labelList() {
        return labelMapper.getLabelList();
    }

    @Override
    public List<String> subLabelList(String pname){
        return labelMapper.getSubLabelList(pname);
    }

    @Override
    public String getLabel(String article) {
        return labelMapper.getLabel(article);
    }

    @Override
    public void insertDate(String article, String date) {
        labelMapper.insertDate(article,date);
    }

    @Override
    public void updateDate(String article, String date) {
        labelMapper.updateDate(article, date);
    }
}
