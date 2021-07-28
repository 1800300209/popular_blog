package cn.edu.guet.popular_blog.dto;

import lombok.Data;

import java.util.List;

@Data
public class LabelDTO {
    private String pname;
    private List<String> labels;
}
