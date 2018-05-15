package com.sbai.bcnews.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.sbai.bcnews.model.mine.ReplyNews;

import java.util.ArrayList;

/**
 * Modified by $nishuideyu$ on 2018/5/7
 * <p>
 * Description: 写评论或者回复返回的内容
 * </p>
 */
public class WriteCommentResponse implements Parcelable {
    private String content;
    private long createTime;
    private String dataId;
    private String id;
    private int module;
    private int praiseCount;
    private int receive;
    private long replayTime;
    private int userId;
    private String firstId;
    private String username;
    private String userPortrait;

    //3级评论返回的多余数据
    private int replayUserId;
    private String replayId;
    private int type;
    private String replayUsername;
    private String secondId;

    private String replayContent;


    public NewsViewpoint getNewsViewpoint() {
        NewsViewpoint newsViewpoint = new NewsViewpoint();
        newsViewpoint.setUsername(getUsername());
        newsViewpoint.setUserPortrait(getUserPortrait());
        newsViewpoint.setUserId(getUserId());
        newsViewpoint.setDataId(getDataId());
        newsViewpoint.setContent(getContent());
        newsViewpoint.setId(getId());
        newsViewpoint.setType(newsViewpoint.getType());
        newsViewpoint.setUserId(newsViewpoint.getUserId());
        return newsViewpoint;
    }


    public NewViewPointAndReview getNewViewPointAndReview() {
        NewViewPointAndReview newViewPointAndReview = new NewViewPointAndReview();
        newViewPointAndReview.setUserPortrait(getUserPortrait());
        newViewPointAndReview.setUsername(getUsername());
        newViewPointAndReview.setDataId(getDataId());
        newViewPointAndReview.setContent(getContent());
        newViewPointAndReview.setId(getId());
        newViewPointAndReview.setUserId(getUserId());
        newViewPointAndReview.setVos(new ArrayList<NewsViewpoint>());
        newViewPointAndReview.setReplayTime(System.currentTimeMillis());
        return newViewPointAndReview;
    }

    public ViewPointComment getViewPointComment() {
        ViewPointComment viewPointComment = new ViewPointComment();
        viewPointComment.setDataId(getDataId());
        viewPointComment.setVos(new ArrayList<ViewPointCommentReview>());
        viewPointComment.setUserPortrait(getUserPortrait());
        viewPointComment.setContent(getContent());
        viewPointComment.setCreateTime(getCreateTime());
        viewPointComment.setFirstId(getFirstId());
        viewPointComment.setModule(getModule());
        viewPointComment.setUsername(getUsername());
        viewPointComment.setUserId(getUserId());
        viewPointComment.setReplayId(getReplayId());
        viewPointComment.setReplayUserId(getReplayUserId());
        viewPointComment.setReplayUsername(getReplayUsername());
        viewPointComment.setReplayTime(getReplayTime());
        viewPointComment.setType(getType());
        viewPointComment.setId(getId());
        viewPointComment.setSecondId(getSecondId());
        return viewPointComment;
    }

    public ViewPointCommentReview getViewPointCommentReview() {
        ViewPointCommentReview viewPointComment = new ViewPointCommentReview();
        viewPointComment.setDataId(getDataId());
        viewPointComment.setId(getId());
        viewPointComment.setUserPortrait(getUserPortrait());
        viewPointComment.setContent(getContent());
        viewPointComment.setCreateTime(getCreateTime());
        viewPointComment.setFirstId(getFirstId());
        viewPointComment.setModule(getModule());
        viewPointComment.setUsername(getUsername());
        viewPointComment.setUserId(getUserId());
        viewPointComment.setReplayId(getReplayId());
        viewPointComment.setReplayUserId(getReplayUserId());
        viewPointComment.setReplayUsername(getReplayUsername());
        viewPointComment.setReplayTime(getReplayTime());
        viewPointComment.setType(getType());
        viewPointComment.setSecondId(getSecondId());
        return viewPointComment;
    }


    public ReplyNews getReplyComment() {
        ReplyNews replyNews = new ReplyNews();
        replyNews.setUserPortrait(getUserPortrait());
        replyNews.setUserId(getUserId());
        replyNews.setContent(getContent());
        replyNews.setDataId(getDataId());
        replyNews.setFirstId(getFirstId());
        replyNews.setPraiseCount(getPraiseCount());
        replyNews.setId(getId());
        replyNews.setReplayId(getReplayId());
        replyNews.setReplayUserId(getReplayUserId());
        replyNews.setReplayUsername(getReplayUsername());
        replyNews.setReplayTime(getReplayTime());
        replyNews.setReplayContent(getReplayContent());
        return replyNews;
    }

    public String getReplayContent() {
        return replayContent;
    }

    public void setReplayContent(String replayContent) {
        this.replayContent = replayContent;
    }

    public String getSecondId() {
        return secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
    }

    public String getReplayUsername() {
        return replayUsername;
    }

    public void setReplayUsername(String replayUsername) {
        this.replayUsername = replayUsername;
    }

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReplayUserId() {
        return replayUserId;
    }

    public void setReplayUserId(int replayUserId) {
        this.replayUserId = replayUserId;
    }

    public String getReplayId() {
        return replayId;
    }

    public void setReplayId(String replayId) {
        this.replayId = replayId;
    }

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getReceive() {
        return receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public long getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(long replayTime) {
        this.replayTime = replayTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public WriteCommentResponse() {
    }

    @Override
    public String toString() {
        return "WriteCommentResponse{" +
                "content='" + content + '\'' +
                ", createTime=" + createTime +
                ", dataId='" + dataId + '\'' +
                ", id='" + id + '\'' +
                ", module=" + module +
                ", praiseCount=" + praiseCount +
                ", receive=" + receive +
                ", replayTime=" + replayTime +
                ", userId=" + userId +
                ", firstId='" + firstId + '\'' +
                ", username='" + username + '\'' +
                ", userPortrait='" + userPortrait + '\'' +
                ", replayUserId=" + replayUserId +
                ", replayId='" + replayId + '\'' +
                ", type=" + type +
                ", replayUsername='" + replayUsername + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeLong(this.createTime);
        dest.writeString(this.dataId);
        dest.writeString(this.id);
        dest.writeInt(this.module);
        dest.writeInt(this.praiseCount);
        dest.writeInt(this.receive);
        dest.writeLong(this.replayTime);
        dest.writeInt(this.userId);
        dest.writeString(this.firstId);
        dest.writeString(this.username);
        dest.writeString(this.userPortrait);
        dest.writeInt(this.replayUserId);
        dest.writeString(this.replayId);
        dest.writeInt(this.type);
        dest.writeString(this.replayUsername);
        dest.writeString(this.secondId);
    }

    protected WriteCommentResponse(Parcel in) {
        this.content = in.readString();
        this.createTime = in.readLong();
        this.dataId = in.readString();
        this.id = in.readString();
        this.module = in.readInt();
        this.praiseCount = in.readInt();
        this.receive = in.readInt();
        this.replayTime = in.readLong();
        this.userId = in.readInt();
        this.firstId = in.readString();
        this.username = in.readString();
        this.userPortrait = in.readString();
        this.replayUserId = in.readInt();
        this.replayId = in.readString();
        this.type = in.readInt();
        this.replayUsername = in.readString();
        this.secondId = in.readString();
    }

    public static final Creator<WriteCommentResponse> CREATOR = new Creator<WriteCommentResponse>() {
        @Override
        public WriteCommentResponse createFromParcel(Parcel source) {
            return new WriteCommentResponse(source);
        }

        @Override
        public WriteCommentResponse[] newArray(int size) {
            return new WriteCommentResponse[size];
        }
    };
}
