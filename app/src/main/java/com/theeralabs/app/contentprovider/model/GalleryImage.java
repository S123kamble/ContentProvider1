package com.theeralabs.app.contentprovider.model;

/**
 * Created by Kuldeep on 16-Nov-17.
 */

public class GalleryImage {
    private String name, image;

    public GalleryImage(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
