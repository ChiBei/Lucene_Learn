package com.cg.service;


import com.cg.pojo.ResultModel;

public interface SearchService {

    //modle没用到
    public ResultModel query(String news_name, String news_time,String news_link, Integer page) throws Exception;

}
