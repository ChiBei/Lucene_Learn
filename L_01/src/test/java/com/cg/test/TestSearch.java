package com.cg.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;

public class TestSearch {

    @Test
    public void testSearch01() throws Exception{
        //【1】创建分词器:对搜索输入的关键词分词
        // （分词器要和创建索引的分词器一样，保证前后切分词效果一样，不然搜索对不上）
        Analyzer analyzer=new StandardAnalyzer();
        //【2】创建查询对象
        QueryParser queryParser=new QueryParser("name",analyzer);  //默认查询域、分词器
        //【3】设置搜索关键词
        Query query=queryParser.parse("brandName:华为");  //指定域brandName:是可选的，可以和上面不一样
        //【4】创建Directory目录对象，指定索引库位置
        Directory dir= FSDirectory.open(Paths.get("E:\\File_Lucene\\index_dir_3"));   //抽象类型不能new
        //【5】创建输入流对象，打开索引库
        IndexReader indexReader= DirectoryReader.open(dir);
        //【6】创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //【7】搜索，返回结果
        TopDocs topDocs = indexSearcher.search(query, 5);//分页展示
//        TotalHits totalHits = topDocs.totalHits;    //结果集总数
//        System.out.println(totalHits);
        //【8】获取结果集,String数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //【9】遍历结果集
        if(scoreDocs !=null){
            for (ScoreDoc scoreDoc:scoreDocs) {
                int docID = scoreDoc.doc;   //lucene自动给查询到的文档分配的id
                Document doc = indexSearcher.doc(docID);    //通过id读取文档
                System.out.println("-------------------------------------------");
                System.out.println("id是"+doc.get("id"));    //通过域名，从文档中获取域值
                System.out.println("name是"+doc.get("name"));
                System.out.println("price是"+doc.get("price"));
            }
        }
        //【10】关闭流
        indexReader.close();
    }

    @Test   //数值范围查询
    public void testSearch02() throws Exception{
        //【1】创建分词器:对搜索输入的关键词分词
        // （分词器要和创建索引的分词器一样，保证前后切分词效果一样，不然搜索对不上）
        Analyzer analyzer=new StandardAnalyzer();
        //【2】创建查询对象
        Query query = IntPoint.newRangeQuery("price", 100, 15000);

//        QueryParser queryParser=new QueryParser("name",analyzer);  //默认查询域、分词器
//        //【3】设置搜索关键词
//        Query query=queryParser.parse("brandName:华为");  //指定域brandName:是可选的，可以和上面不一样

        //【4】创建Directory目录对象，指定索引库位置
        Directory dir= FSDirectory.open(Paths.get("E:\\File_Lucene\\index_dir_3"));   //抽象类型不能new
        //【5】创建输入流对象，打开索引库
        IndexReader indexReader= DirectoryReader.open(dir);
        //【6】创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //【7】搜索，返回结果
        TopDocs topDocs = indexSearcher.search(query, 5);//分页展示
//        TotalHits totalHits = topDocs.totalHits;    //结果集总数
//        System.out.println(totalHits);
        //【8】获取结果集,String数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //【9】遍历结果集
        if(scoreDocs !=null){
            for (ScoreDoc scoreDoc:scoreDocs) {
                int docID = scoreDoc.doc;   //lucene自动给查询到的文档分配的id
                Document doc = indexSearcher.doc(docID);    //通过id读取文档
                System.out.println("-------------------------------------------");
                System.out.println("id是"+doc.get("id"));    //通过域名，从文档中获取域值
                System.out.println("name是"+doc.get("name"));
                System.out.println("price是"+doc.get("price"));
            }
        }
        //【10】关闭流
        indexReader.close();
    }

    @Test   //组合查询
    public void testSearch03() throws Exception{
        //【1】创建分词器:对搜索输入的关键词分词
        // （分词器要和创建索引的分词器一样，保证前后切分词效果一样，不然搜索对不上）
        Analyzer analyzer=new StandardAnalyzer();
        //【2】创建查询对象
        Query query1 = IntPoint.newRangeQuery("price", 1, 15000);

        QueryParser queryParser=new QueryParser("name",analyzer);  //默认查询域、分词器
        //【3】设置搜索关键词
        Query query2=queryParser.parse("华为");  //指定域brandName:是可选的，可以和上面不一样

        //【组合查询对象】
        BooleanQuery.Builder query=new BooleanQuery.Builder();
        query.add(query1, BooleanClause.Occur.MUST);    //occur对象，相当于must的 都要满足，should的 或者满足，Must_not的为 非
        query.add(query2, BooleanClause.Occur.MUST);    //如果只有MUST_NOT，为了保护搜索，不返回内容


        //【4】创建Directory目录对象，指定索引库位置
        Directory dir= FSDirectory.open(Paths.get("E:\\File_Lucene\\index_dir_3"));   //抽象类型不能new
        //【5】创建输入流对象，打开索引库
        IndexReader indexReader= DirectoryReader.open(dir);
        //【6】创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //【7】搜索，返回结果
        TopDocs topDocs = indexSearcher.search(query.build(), 5);//分页展示
//        TotalHits totalHits = topDocs.totalHits;    //结果集总数
//        System.out.println(totalHits);
        //【8】获取结果集,String数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //【9】遍历结果集
        if(scoreDocs !=null){
            for (ScoreDoc scoreDoc:scoreDocs) {
                int docID = scoreDoc.doc;   //lucene自动给查询到的文档分配的id
                Document doc = indexSearcher.doc(docID);    //通过id读取文档
                System.out.println("-------------------------------------------");
                System.out.println("id是"+doc.get("id"));    //通过域名，从文档中获取域值
                System.out.println("name是"+doc.get("name"));
                System.out.println("price是"+doc.get("price"));
            }
        }
        //【10】关闭流
        indexReader.close();
    }

}
