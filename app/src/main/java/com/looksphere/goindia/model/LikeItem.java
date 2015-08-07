package com.looksphere.goindia.model;

/**
 * Created by SPARK on 16/10/14.
 */
public class LikeItem {

    /*
    dirtId: "543a8a8de4b0211ab23c43e0",
    timestamp: "1413308401292",
    userId: "anonymous"
*/
    private String dirtId;
    private String timestamp;
    private String userId;

    public String getDirtId() {
        return dirtId;
    }

    public void setDirtId(String dirtId) {
        this.dirtId = dirtId;
    }

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
}
