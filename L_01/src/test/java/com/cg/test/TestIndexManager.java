package com.cg.test;

import com.cg.dao.SkuDao;
import com.cg.dao.SkuDaoImpl;
import com.cg.pojo.Sku;
import org.apache.lucene.store.*;
import org.junit.Test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestIndexManager {

    @Test   //创建索引库
    public void createIndexTest() throws Exception {
        //【1】采集数据，Dao到数据库读取
        SkuDao skuDao = new SkuDaoImpl();
        List<Sku> skuList = skuDao.querySkuList();
        //【2】创建文档对象--域
        List<Document> docList = new ArrayList<>(); //文档集合
        for (Sku sku : skuList) {
            Document document = new Document();     //文档对象
            //域对象--》文档对象
            //id:索引，不分词，存储--》StringField
            //name：索引，分词，存储--》TextField
            //price：索引（范围查找，必须索引），【分词】（范围查找，规定必须分词），存储
            // --》先用数字IntPoint（存不了），再加一个域用StoredField存储
            //image：不索引，不分词，存储（影响document，在文档中展示）--》StoredField
            //category：索引，不分词（分词也行），存储--》StringField
            //brandName：索引，不分词，存储--》StringField
            document.add(new StringField("id", sku.getId(), Field.Store.YES));
            document.add(new TextField("news_name", sku.getNews_name(), Field.Store.YES));
            document.add(new StringField("news_time", sku.getNews_time(), Field.Store.YES));
            document.add(new StringField("news_link", sku.getNews_link(), Field.Store.YES));

            //文档对象--》文档集合
            docList.add(document);
        }
        //【3】创建分词器：对被搜索对象进行分词
        Analyzer analyzer = new IKAnalyzer();   //英文分词器,改用ik
        //【4】创建目录对象directory，表示索引库位置
        Directory dir = FSDirectory.open(Paths.get("E:\\File_Lucene\\index_dir_3"));
        //【5】创建输出流初始化对象IndexWriterConfig对象，用来指定输出到分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //【6】创建IndexWriter输出流对象，指定输出位置和config输出对象
        IndexWriter indexWriter = new IndexWriter(dir, config);
        //【7】把文档集合写到索引
        for (Document doc : docList) {
            indexWriter.addDocument(doc);
        }
        //【8】释放流的资源
        indexWriter.close();
    }

    @Test   //修改索引库
    public void updateIndexTest() throws Exception {
        Document document = new Document();     //文档对象

        document.add(new StringField("id", "100000003145", Field.Store.YES));
        document.add(new TextField("name", "ggg", Field.Store.YES));
        document.add(new IntPoint("price", 123333));
        document.add(new StoredField("price", 12333));
        document.add(new StoredField("image", "xxxx.jpg"));
        document.add(new StringField("category", "hhhh", Field.Store.YES));
        document.add(new StringField("brandName", "hjhjhjhj", Field.Store.YES));


        //【3】创建分词器：对被搜索对象进行分词
        Analyzer analyzer = new StandardAnalyzer();   //英文分词器
        //【4】创建目录对象directory，表示索引库位置
        Directory dir = FSDirectory.open(Paths.get("E:\\File_Lucene\\index_dir_3"));
        //【5】创建输出流初始化对象IndexWriterConfig对象，用来指定输出到分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //【6】创建IndexWriter输出流对象，指定输出位置和config输出对象
        IndexWriter indexWriter = new IndexWriter(dir, config);

        //【！！！！！！】【7】把文档集合修改到索引，第一个参数：修改条件；第二个参数：修改的内容
        indexWriter.updateDocument(new Term("id","100000003145"),document);
        //修改是删掉所选，再在索引结尾添上修改后的

        //【8】释放流的资源
            indexWriter.close();
    }

    @Test   //根据条件删除索引库
    public void deleteIndexTest() throws Exception {
        //【3】创建分词器：对被搜索对象进行分词
        Analyzer analyzer = new StandardAnalyzer();   //英文分词器
        //【4】创建目录对象directory，表示索引库位置
        Directory dir = FSDirectory.open(Paths.get("E:\\File_Lucene\\index_dir_3"));
        //【5】创建输出流初始化对象IndexWriterConfig对象，用来指定输出到分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //【6】创建IndexWriter输出流对象，指定输出位置和config输出对象
        IndexWriter indexWriter = new IndexWriter(dir, config);

        //【！！！！！！】【7】把文档集合删除到索引，第一个参数：删除条件
        indexWriter.deleteDocuments(new Term("id","100000003145"));

        //【8】释放流的资源
        indexWriter.close();
    }

    @Test   //IK分词器
    public void IKAnalyzerTest() throws Exception {
        //【3】创建分词器：对被搜索对象进行分词
        Analyzer analyzer = new IKAnalyzer();   //英文分词器
        //【4】创建目录对象directory，表示索引库位置
        Directory dir = FSDirectory.open(Paths.get("E:\\File_Lucene\\index_dir_3"));
        //【5】创建输出流初始化对象IndexWriterConfig对象，用来指定输出到分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //【6】创建IndexWriter输出流对象，指定输出位置和config输出对象
        IndexWriter indexWriter = new IndexWriter(dir, config);

        //【！！！！！！】【7】把文档集合添加到索引
        Document doc=new Document();
        doc.add(new TextField("name","黑马程序员Lucene全文检索技术，从底层到实战应用Lucene全套教程",Field.Store.YES));
        indexWriter.addDocument(doc);

        //【8】释放流的资源
        indexWriter.close();
    }
}

