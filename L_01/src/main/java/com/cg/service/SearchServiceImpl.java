package com.cg.service;

import com.cg.pojo.ResultModel;
import com.cg.pojo.Sku;
import com.cg.service.SearchService;

import org.springframework.util.ObjectUtils;
import org.springframework.stereotype.Service;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{

    public final static Integer PAGE_SIZE = 20;   //每页，常量

    @Override
    public ResultModel query(String news_name, String news_time,String news_link, Integer page) throws Exception {

        //【1】需要使用的对象封装
        ResultModel resultModel=new ResultModel();

        int start = (page - 1) * PAGE_SIZE;
        int end = page * PAGE_SIZE;
        //创建分词器
        Analyzer analyzer = new IKAnalyzer();

        //创建组合查询对象
        BooleanQuery.Builder builder=new BooleanQuery.Builder();

        //【2】根据 查询关键字 封装查询对象
        QueryParser queryParser =new QueryParser("news_name",analyzer);
        //判断输入的 查询关键字
        Query query1=null;
        if(ObjectUtils.isEmpty(news_name)){
            query1 = queryParser.parse("*:*");//查询所有
        }else {
            query1 = queryParser.parse(news_name);//查询关键字
        }

        //封装到组合查询中
        builder.add(new BooleanClause(query1, BooleanClause.Occur.MUST));

        //【3】根据 价格范围 封装查询对象
        //价格不为空时，按照范围（like 0-100）查询，要进行切割
//        if(!ObjectUtils.isEmpty(news_time)){
//            String[] split = news_time.split("-");
//            Query query2 = IntPoint.newRangeQuery("news_time", Integer.parseInt(split[0]),Integer.parseInt(split[1]));
//
//            builder.add(new BooleanClause(query2, BooleanClause.Occur.MUST));
//        }

        //【4】创建 Directory目录对象，指定索引库位置
        Directory directory = FSDirectory.open(Paths.get("E:\\File_Lucene\\index_dir_3"));
        //【5】创建 输入流对象
        IndexReader reader= DirectoryReader.open(directory);
        //【6】创建 搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        //【7】搜索，获取结果
        TopDocs topDocs = indexSearcher.search(builder.build(), end);
        //【8】获取 查询总条数,封装到返回结果resultmodle中
//        resultModel.setRecordCount(topDocs.totalHits);
        //【9】获取 查询结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //【10】遍历结果集，封装返回数据
        List<Sku> skuList = new ArrayList<>();  //存sku
        if (scoreDocs!=null) {
            for (int i = start; i < end; i++) {
                //注意翻页，根据编号找到文档对象
                Document document = reader.document(scoreDocs[i].doc);
                //封装sku对象
                Sku sku = new Sku();
                sku.setId(document.get("id"));
                sku.setNews_name(document.get("news_name"));
                sku.setNews_time(document.get("news_time"));
                sku.setNews_link(document.get("news_link"));
                skuList.add(sku);
            }
        }
        resultModel.setSkuList(skuList);
        resultModel.setCurrentPage(page);
        resultModel.setNews_link(news_link);


        reader.close();
        return resultModel;
    }
}
