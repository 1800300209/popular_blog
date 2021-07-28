package cn.edu.guet.popular_blog.controller;

import cn.edu.guet.popular_blog.dto.LabelDTO;
import cn.edu.guet.popular_blog.service.impl.LabelServiceImpl;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/html")
public class LabelController {

    @Autowired
    private LabelServiceImpl labelService;

    @RequestMapping("/label")
    public String labelList(HttpServletRequest request){
        String s_label=labelService.getLabel("5");
        if(s_label!=null){
            String[] s_l=s_label.split("@@");
            request.setAttribute("s_label",s_l);
        }
        List<String> plist=labelService.labelList();
        List<LabelDTO> labels=new ArrayList<LabelDTO>();
        for(String plabel:plist){
            LabelDTO label=new LabelDTO();
            List<String> slist=labelService.subLabelList(plabel);
            label.setPname(plabel);
            label.setLabels(slist);
            labels.add(label);
        }
        request.setAttribute("labelList",labels);
        return "label";
    }

    @RequestMapping("insertLabel")
    public String insertLabel(@RequestBody String str) {
        JSONObject jsonArray=JSONObject.fromObject(str);
        String article=jsonArray.get("article").toString();
        String date=jsonArray.get("date").toString();
        labelService.insertDate(article,date);
        return "redirect:label";
    }

    @PostMapping("updateLabel")
    public String updateLabel(@RequestBody String str) {
        JSONObject jsonArray=JSONObject.fromObject(str);
        String article=jsonArray.get("article").toString();
        String date=jsonArray.get("date").toString();
        labelService.updateDate(article,date);
        return "redirect:label";
    }
}
