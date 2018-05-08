package com.sbai.bcnews.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.sbai.bcnews.model.NewsDetail;

/**
 * Modified by $nishuideyu$ on 2018/5/2
 * <p>
 * Description: 提交观点model
 * </p>
 */
public class WriteComment implements Parcelable, ViewpointType {


    //必传
    private String dataId;  //新闻id
    private int type;
    private String content;
    private int module;

    private Integer replayUserId; //回复的哪个用户
    private String replayId;  //评论/回复哪条id
    private Integer status;


    public static WriteComment getWriteComment(NewsDetail newsDetail) {
        WriteComment writeComment = new WriteComment();
        writeComment.setDataId(newsDetail.getId());
        writeComment.setModule(0);
        writeComment.setType(WriteComment.FIRST_COMMENT);
        return writeComment;
    }


    public static WriteComment getSecondWriteComment(NewsViewpoint newsViewpoint) {
        WriteComment writeComment = new WriteComment();
        writeComment.setDataId(newsViewpoint.getDataId());
        writeComment.setType(WriteComment.SECOND_COMMENT);
        writeComment.setModule(0);
        writeComment.setReplayId(newsViewpoint.getId());
        writeComment.setReplayUserId(newsViewpoint.getUserId());
        return writeComment;
    }

    public static WriteComment getThirdWriteComment(ViewPointComment viewPointComment) {
        WriteComment writeComment = new WriteComment();
        writeComment.setDataId(viewPointComment.getDataId());
        writeComment.setType(WriteComment.THIRD_COMMENT);
        writeComment.setModule(0);
        writeComment.setReplayId(viewPointComment.getId());
        writeComment.setReplayUserId(viewPointComment.getUserId());
        return writeComment;
    }

    public static WriteComment getThirdReplyComment(ViewPointCommentReview viewPointComment) {
        WriteComment writeComment = new WriteComment();
        writeComment.setDataId(viewPointComment.getDataId());
        writeComment.setType(WriteComment.THIRD_REPLY);
        writeComment.setModule(0);
        writeComment.setReplayId(viewPointComment.getId());
        writeComment.setReplayUserId(viewPointComment.getUserId());
        return writeComment;
    }


    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public String getDataId() {
        return dataId;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public int getModule() {
        return module;
    }

    public Integer getReplayUserId() {
        return replayUserId;
    }

    public void setReplayUserId(Integer replayUserId) {
        this.replayUserId = replayUserId;
    }

    public String getReplayId() {
        return replayId;
    }

    public void setReplayId(String replayId) {
        this.replayId = replayId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dataId);
        dest.writeInt(this.type);
        dest.writeString(this.content);
        dest.writeInt(this.module);
        dest.writeValue(this.replayUserId);
        dest.writeValue(this.replayId);
        dest.writeValue(this.status);
    }

    public WriteComment() {
    }

    protected WriteComment(Parcel in) {
        this.dataId = in.readString();
        this.type = in.readInt();
        this.content = in.readString();
        this.module = in.readInt();
        this.replayUserId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.replayId = (String) in.readValue(String.class.getClassLoader());
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<WriteComment> CREATOR = new Creator<WriteComment>() {
        @Override
        public WriteComment createFromParcel(Parcel source) {
            return new WriteComment(source);
        }

        @Override
        public WriteComment[] newArray(int size) {
            return new WriteComment[size];
        }
    };
}
