package com.sbai.bcnews.model.author;

/**
 * Modified by $nishuideyu$ on 2018/6/8
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class Author {

    private String userPortrait;
    private String userName;

    private int yesterdayRedNumber;
    private int totalRedNumber;

    private int yesterdayPraiseNumber;
    private int totalPraiseNumber;

    private int yesterdayFansNumber;
    private int totalFansNumber;

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
