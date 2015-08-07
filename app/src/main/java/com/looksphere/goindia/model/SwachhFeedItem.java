package com.looksphere.goindia.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by prasanna on 5/10/14.
 */
public class SwachhFeedItem implements Serializable {


    /**
     * sample json
     * landmark: "nearby ipad",
     severity: "HIGH",
     location: [
     12.9433481,
     77.6295012
     ],
     imageUrl: "http://res.cloudinary.com/bigo/image/upload/v1413570835/jfrfh2rgmt6zwdf0rymy.png",
     description: "dirty mac",
     timestamp: "1413570874988",
     userId: "10204049299689489",
     status: "OPEN",
     firstname: "Karthikeyan",
     gender: "male",
     link: "https://www.facebook.com/app_scoped_user_id/10204049299689489/",
     name: "Karthikeyan Ng",
     email: "intrepidkarthi@gmail.com",
     lastname: "Ng",
     userHandle: "Karthikeyan Ng",
     comments: [ ],
     commentsCount: 0,
     isLiked: false,
     likesCount: 0
     */

    private String id;
    private String name;
    private String status;
    private String image;
    private String profilePic;
    private String timeStamp;
    private String url;
    private String description;
    private String severity;
    private int commentsCount;
    private int likesCount;
    private String userHandle;
    private String email;
    private String fbUserId;

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    private String landmark;

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    private String locationAddress;

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbUserId() {
        return fbUserId;
    }

    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    private boolean isLiked;

    public ArrayList<CommentItem> getCommentItems() {
        return commentItems;
    }

    public void setCommentItems(ArrayList<CommentItem> commentItems) {
        this.commentItems = commentItems;
    }

    private ArrayList<CommentItem> commentItems;

    public ArrayList<LikeItem> getLikeItems() {
        return likeItems;
    }

    public void setLikeItems(ArrayList<LikeItem> likeItems) {
        this.likeItems = likeItems;
    }

    private ArrayList<LikeItem> likeItems;

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    private double[] location;
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private ArrayList<String> comments;
    private ArrayList<String> likes;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentCount) {
        this.commentsCount = commentCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }


    public SwachhFeedItem(String id, String name, String image, String status,
                          String profilePic, String timeStamp, String url) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.profilePic = profilePic;
        this.timeStamp = timeStamp;
        this.url = url;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SwachhFeedItem() {

    }

}
