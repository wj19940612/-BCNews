package com.sbai.bcnews.model.mine;

/**
 * Modified by $nishuideyu$ on 2018/5/18
 * <p>
 * Description:
 * </p>
 */
public class QKC {

    /**
     * createTime : 1527043249000
     * id : 5
     * integral : 50
     * isGet : 0
     * type : 7
     * updateTime : 1527047164000
     * userId : 1787
     */

    private long createTime;
    private String id;
    private int integral;
    private int isGet;
    private int type;
    private long updateTime;
    private int userId;

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

    public int getIsGet() {
        return isGet;
    }

    public void setIsGet(int isGet) {
        this.isGet = isGet;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
