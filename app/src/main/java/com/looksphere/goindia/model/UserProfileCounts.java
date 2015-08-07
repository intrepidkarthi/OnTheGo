package com.looksphere.goindia.model;

/**
 * Created by SPARK on 19/10/14.
 */
public class UserProfileCounts {


    /**
     * {
     postedDirtCnt: "0",
     completedDirtCnt: "0",
     assignedDirtCnt: "0",
     likesCnt: "0",
     commentsCnt: "0"
     }
     */

    private String postedDirtCnt;
    private String completedDirtCnt;
    private String assignedDirtCnt;
    private String likesCnt;
    private String commentsCnt;

    public String getPostedDirtCnt() {
        return postedDirtCnt;
    }

    public void setPostedDirtCnt(String postedDirtCnt) {
        this.postedDirtCnt = postedDirtCnt;
    }

    public String getCompletedDirtCnt() {
        return completedDirtCnt;
    }

    public void setCompletedDirtCnt(String completedDirtCnt) {
        this.completedDirtCnt = completedDirtCnt;
    }

    public String getAssignedDirtCnt() {
        return assignedDirtCnt;
    }

    public void setAssignedDirtCnt(String assignedDirtCnt) {
        this.assignedDirtCnt = assignedDirtCnt;
    }

    public String getLikesCnt() {
        return likesCnt;
    }

    public void setLikesCnt(String likesCnt) {
        this.likesCnt = likesCnt;
    }

    public String getCommentsCnt() {
        return commentsCnt;
    }

    public void setCommentsCnt(String commentsCnt) {
        this.commentsCnt = commentsCnt;
    }
}
