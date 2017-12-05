package com.login.a54liyuanchao.blog;

/**
 * Created by 54liyuanchao on 2017/11/16.
 */

public class Blog {

    private String Title;
    private String Desc;
    private String Image;
    private String username;

    public Blog(){

    }
//名称要对应
    public Blog(String title, String desc, String image, String Username) {
        Title = title;
        Desc = desc;
        Image = image;
        username = Username;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
