package com.sbai.bcnews.model.author;

/**
 * Modified by $nishuideyu$ on 2018/6/8
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class Author {

    public static final int AUTHOR_STATUS_ORDINARY = 0; //普通人
    public static final int AUTHOR_STATUS_SPECIAL = 1; //专栏作家
    public static final int AUTHOR_STATUS_OFFICIAL = 2; //官方认证

    private String userPortrait;
    private String userName;
    private String id;

    private int yesterdayRedNumber;
    private int totalRedNumber;

    private int yesterdayPraiseNumber;
    private int totalPraiseNumber;

    private int yesterdayFansNumber;
    private int totalFansNumber;
    private int authorType;
    private String introduce;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getAuthorType() {
        return authorType;
    }

    public void setAuthorType(int authorType) {
        this.authorType = authorType;
    }

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getYesterdayRedNumber() {
        return yesterdayRedNumber;
    }

    public void setYesterdayRedNumber(int yesterdayRedNumber) {
        this.yesterdayRedNumber = yesterdayRedNumber;
    }

    public int getTotalRedNumber() {
        return totalRedNumber;
    }

    public void setTotalRedNumber(int totalRedNumber) {
        this.totalRedNumber = totalRedNumber;
    }

    public int getYesterdayPraiseNumber() {
        return yesterdayPraiseNumber;
    }

    public void setYesterdayPraiseNumber(int yesterdayPraiseNumber) {
        this.yesterdayPraiseNumber = yesterdayPraiseNumber;
    }

    public int getTotalPraiseNumber() {
        return totalPraiseNumber;
    }

    public void setTotalPraiseNumber(int totalPraiseNumber) {
        this.totalPraiseNumber = totalPraiseNumber;
    }

    public int getYesterdayFansNumber() {
        return yesterdayFansNumber;
    }

    public void setYesterdayFansNumber(int yesterdayFansNumber) {
        this.yesterdayFansNumber = yesterdayFansNumber;
    }

    public int getTotalFansNumber() {
        return totalFansNumber;
    }

    public void setTotalFansNumber(int totalFansNumber) {
        this.totalFansNumber = totalFansNumber;
    }
}
