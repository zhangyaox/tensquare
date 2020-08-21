package com.zhangyaoxing.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
@Document(indexName = "tensquare_article",type = "article")//搜索引擎 的 第一个是我搜索引擎中的表名字  一个是 我的这个表的实体类类型(小写)
public class Article implements Serializable {
    @Id
    private String id;
    //是否索引 就是看改域是否可以被搜索
    //是否分词 就是表示搜索时是 分词搜索 还是全部搜索
    //是否存储 就是是否要在页面上面显示
    //analyzer = "ik_max_word"  存储用这个分词 来存储
    //searchAnalyzer = "ik_max_word"  查找用这个分词来查找
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String title;
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String content;

    private String state;//状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
