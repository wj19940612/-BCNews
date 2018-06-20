package com.sbai.bcnews.model.author;

/**
 * Modified by $nishuideyu$ on 2018/6/8
 * <p>
 * Description:
 * </p>
 */
public class Author {

    public static final int AUTHOR_STATUS_ORDINARY = 0; //普通人
    public static final int AUTHOR_STATUS_SPECIAL = 1; //专栏作家
    public static final int AUTHOR_STATUS_OFFICIAL = 2; //官方认证


    public static final int AUTHOR_IS_ALREADY_ATTENTION = 1;
    public static final int AUTHOR_IS_NOT_ATTENTION = 0;
    /**
     * articleCount : 0
     * authInfo : 修改了认证信息
     * collectCount : 0
     * createTime : 1528276697000
     * fansCount : 0
     * id : 1
     * isAuthor : 1
     * isConcern : 0
     * isRecommend : 1
     * praiseCount : 0
     * rankType : 1
     * rankTypeStr : 专栏作家
     * readCount : 0
     * recommendTime : 1528872267000
     * remark : 备注修改
     * setReadCount : 0
     * showReadCount : 0
     * updateTime : 1528872201000
     * userId : 1829
     * userName : 用户111631
     */

    private int articleCount;
    private String authInfo;
    private int collectCount;
    private long createTime;
    private int fansCount;
    private int id;
    private int isAuthor;
    private int isConcern;    //是否关注:0未关注,1关注
    private int isRecommend;
    private int praiseCount;
    private int rankType;   // 角色:0普通用户,1专栏作家,2官方媒体
    private String rankTypeStr; //角色名
    private int readCount;
    private long recommendTime;
    private String remark;
    private int setReadCount;
    private int showReadCount;
    private long updateTime;
    private int userId;
    private String userName;
    private String userPortrait;

    private int yesterdayReadCount;
    private int yesterdayPraiseCount;
    private int yesterdayFansCount;


    public boolean isAuthor() {
        return getRankType() != AUTHOR_STATUS_ORDINARY;
    }

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public String getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(String authInfo) {
        this.authInfo = authInfo;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsAuthor() {
        return isAuthor;
    }

    public void setIsAuthor(int isAuthor) {
        this.isAuthor = isAuthor;
    }

    public int getIsConcern() {
        return isConcern;
    }

    public void setIsConcern(int isConcern) {
        this.isConcern = isConcern;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getRankType() {
        return rankType;
    }

    public void setRankType(int rankType) {
        this.rankType = rankType;
    }

    public String getRankTypeStr() {
        return rankTypeStr;
    }

    public void setRankTypeStr(String rankTypeStr) {
        this.rankTypeStr = rankTypeStr;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public long getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(long recommendTime) {
        this.recommendTime = recommendTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSetReadCount() {
        return setReadCount;
    }

    public void setSetReadCount(int setReadCount) {
        this.setReadCount = setReadCount;
    }

    public int getShowReadCount() {
        return showReadCount;
    }

    public void setShowReadCount(int showReadCount) {
        this.showReadCount = showReadCount;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getYesterdayReadCount() {
        return yesterdayReadCount;
    }

    public void setYesterdayReadCount(int yesterdayReadCount) {
        this.yesterdayReadCount = yesterdayReadCount;
    }

    public int getYesterdayPraiseCount() {
        return yesterdayPraiseCount;
    }

    public void setYesterdayPraiseCount(int yesterdayPraiseCount) {
        this.yesterdayPraiseCount = yesterdayPraiseCount;
    }

    public int getYesterdayFansCount() {
        return yesterdayFansCount;
    }

    public void setYesterdayFansCount(int yesterdayFansCount) {
        this.yesterdayFansCount = yesterdayFansCount;
    }
}
