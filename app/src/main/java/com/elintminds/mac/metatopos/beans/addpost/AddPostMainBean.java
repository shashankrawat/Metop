package com.elintminds.mac.metatopos.beans.addpost;

public class AddPostMainBean {
    private String name;
    private int image;
    private int postType;

    public AddPostMainBean(String name, int image)
    {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }
}
