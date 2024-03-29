package com.example.csis3175project.Model;

public class Post {
    public String ID;
    public String URL;
    public String Title;
    public Integer CommentCounts;
    public PostMedia PostMedia;

    public Post() {}

    public Post(String url, String title) {
        URL = url;
        Title = title;
    }

    public Post(String id, String url, String title, Integer commentCounts) {
        ID = id;
        URL = url;
        Title = title;
        CommentCounts = commentCounts;
    }
}
