package com.sbai.bcnews.model.mine;

import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/5/17
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class RedPacketListInfo {

    /**
     * data : [{"createTime":1527148864000,"id":"356","integral":20,"updateTime":1527148864000,"userId":1673,"userName":"用户m","userPortrait":"https://esongtest.oss-cn-shanghai.aliyuncs.com/upload/20180305151441544/1673i1520234081545.png"},{"createTime":1527150161000,"id":"363","integral":13,"updateTime":1527150161000,"userId":1787,"userName":"用户34472","userPortrait":"https://esongtest.oss-cn-shanghai.aliyuncs.com/upload/20180228104021899.png"}]
     * integral : 13.0
     * page : 0
     * total : 2
     * userName : 用户34472
     * userPortrait : https://esongtest.oss-cn-shanghai.aliyuncs.com/upload/20180228104021899.png
     */

    private double integral;
    private int page;
    private int total;
    private String userName;
    private String userPortrait;
    private List<RedPacketInfo> data;

    public double getIntegral() {
        return integral;
    }

    public void setIntegral(double integral) {
        this.integral = integral;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    public List<RedPacketInfo> getData() {
        return data;
    }

    public void setData(List<RedPacketInfo> data) {
        this.data = data;
    }
}