package com.sbai.bcnews.model.mine;

/**
 * Modified by $nishuideyu$ on 2018/5/18
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class QKCDetails {

    /**
     * changeType : 1
     * createTime : 1527039927000
     * id : 3
     * integral : 1
     * type : 2
     * typeStr : 分成收益
     * updateTime : 1527039927000
     * userId : 1665
     */

    private int changeType;
    private long createTime;
    private String id;
    private int integral;
    private int type;
    private String typeStr;
    private long updateTime;
    private int userId;

    public int getChangeType() {
        return changeType;
    }

    public void setChangeType(int changeType) {
        this.changeType = changeType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
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
}
