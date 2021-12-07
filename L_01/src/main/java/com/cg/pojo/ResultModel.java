package com.cg.pojo;

import java.util.List;


//封装结果，自定义分类结果
public class ResultModel {
     private List<Sku> skuList;
     private Long recordCount;
     private Long pageCount;
     private long currentPage;

     private String news_link;

    public String getNews_link() {
        return news_link;
    }
    public void setNews_link(String news_link) {
        this.news_link = news_link;
    }

    public List<Sku> getSkuList() {
        return skuList;
    }
    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }

    public Long getRecordCount() {
        return recordCount;
    }
    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public Long getPageCount() {
        return pageCount;
    }
    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public long getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

}
