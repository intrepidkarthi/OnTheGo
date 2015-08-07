package com.looksphere.goindia.model;

import java.util.ArrayList;

/**
 * Created by karthikeyan on 10/10/14.
 */
public class TaggableFriendsWrapper {

    private ArrayList<TaggableFriends> data;
    private Paging paging;

    public ArrayList<TaggableFriends> getData() {
        return data;
    }

    public void setData(ArrayList<TaggableFriends> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public class Paging {

        private Cursors cursors;

        public Cursors getCursors() {
            return cursors;
        }

        public void setCursors(Cursors cursors) {
            this.cursors = cursors;
        }
    }

    public class Cursors {
        private String after;
        private String before;

        public String getAfter() {
            return after;
        }

        public void setAfter(String after) {
            this.after = after;
        }

        public String getBefore() {
            return before;
        }

        public void setBefore(String before) {
            this.before = before;
        }

    }
}