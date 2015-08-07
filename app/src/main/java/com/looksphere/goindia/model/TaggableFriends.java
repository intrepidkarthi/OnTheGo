package com.looksphere.goindia.model;

/**
 * Created by karthikeyan on 10/10/14.
 */
public class TaggableFriends {

    private String id;
    private String name;
    private Picture picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public class Picture {

        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public class Data {

        private String url;
        private boolean is_sillhouette;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isIs_sillhouette() {
            return is_sillhouette;
        }

        public void setIs_sillhouette(boolean is_sillhouette) {
            this.is_sillhouette = is_sillhouette;
        }
    }
}