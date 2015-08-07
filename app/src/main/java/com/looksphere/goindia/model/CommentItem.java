package com.looksphere.goindia.model;

import java.io.Serializable;

/**
 * Created by SPARK on 16/10/14.
 */
public class CommentItem implements Serializable {

    /*
    comment: "Testing Comment 1",
    dirtId: "543a8a8de4b0211ab23c43e0",
    timestamp: "1413308443099",
    userId: "anonymous"
    */

    private String dirtId;
    private String timestamp;
    private String userId;
    private String comment;

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    private String userHandle;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDirtId() {
        return dirtId;
    }

    public void setDirtId(String dirtId) {
        this.dirtId = dirtId;
    }
}
