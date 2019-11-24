package com.example.csis3175project.Model;

public class PostMedia {
    public String Url;
    public int Width;
    public int Height;
    public String Type;
    public String ID;

    public PostMedia(String id, String url, int width, int height, String type) {
        ID = id;
        Url = url;
        Width = width;
        Height = height;
        Type = type;
    }
}
