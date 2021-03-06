package com.sbai.bcnews.model.news;

import com.sbai.bcnews.activity.comment.NewsShareOrCommentBaseActivity;

import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/5/4
 * <p>
 * Description: 评论详情
 * /api/news-info/discuss/second/{id}
 * GET
 * 评论详情----齐慕伟
 * Apic.requestCommentList()
 * </p>
 */
public class ViewPointComment extends NewsViewpoint implements NewsShareOrCommentBaseActivity.PraiseContent {

    /**
     * createTime : 1524801803800
     * firstContent : 发表第二个一级评论-----
     * firstId : 977023029349253121
     * id : 989716615342026753
     * module : 0
     * newsTitle : 挖矿从入门到精通：想要成为矿工，你需要了解的一些事
     * parentContent : 发表第二个一级评论-----
     * receive : 1
     * replayId : 977023029349253121
     * replayUserId : 1665
     * reportCount : 0
     * status : 1
     * suggest : 0
     */

    private long createTime;
    private String firstContent;
    private String firstId;
    private int module;
    private String newsTitle;
    private String parentContent;
    private int receive;
    private String replayId;
    private int replayUserId;
    private String replayUsername;
    private int reportCount;
    private int status;
    private int suggest;
    private String secondId;
    private List<ViewPointCommentReview> vos;

    public String getReplayUsername() {
        return replayUsername;
    }

    public void setReplayUsername(String replayUsername) {
        this.replayUsername = replayUsername;
    }

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    public String getReplayId() {
        return replayId;
    }

    public void setReplayId(String replayId) {
        this.replayId = replayId;
    }


    public String getSecondId() {
        return secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
    }


    public List<ViewPointCommentReview> getVos() {
        return vos;
    }

    public void setVos(List<ViewPointCommentReview> vos) {
        this.vos = vos;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFirstContent() {
        return firstContent;
    }

    public void setFirstContent(String firstContent) {
        this.firstContent = firstContent;
    }


    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getParentContent() {
        return parentContent;
    }

    public void setParentContent(String parentContent) {
        this.parentContent = parentContent;
    }

    public int getReceive() {
        return receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }



    public int getReplayUserId() {
        return replayUserId;
    }

    public void setReplayUserId(int replayUserId) {
        this.replayUserId = replayUserId;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSuggest() {
        return suggest;
    }

    public void setSuggest(int suggest) {
        this.suggest = suggest;
    }

    @Override
    public String getViewpointId() {
        return getId();
    }

    @Override
    public String getNewsDataId() {
        return getDataId();
    }

    @Override
    public Integer getPraisedUserId() {
        return getUserId();
    }

    @Override
    public int getPraiseType() {
        return getType();
    }
}
