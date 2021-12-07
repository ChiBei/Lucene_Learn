package com.cg.controller;

import com.cg.pojo.ResultModel;
import com.cg.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/list")    //类上访问
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping     //查询关键字，价格范围（是个范围，字符串），当前页
    public String list(String news_name, String news_time,String news_link, Integer page, Model model) throws Exception{

        //处理当前页面,用了新方法
        if (ObjectUtils.isEmpty(page)) {
            page = 1;
        }
        if (page <= 0) {
            page = 1;
        }

        //调用service查询
        ResultModel resultModel = searchService.query(news_name, news_time,news_link, page);
        model.addAttribute("result",resultModel);

        //查询条件回显
        model.addAttribute("news_name",news_name);
        model.addAttribute("news_time",news_time);
        model.addAttribute("news_link",news_link);

        model.addAttribute("page",page);

        return "search";
    }
}
