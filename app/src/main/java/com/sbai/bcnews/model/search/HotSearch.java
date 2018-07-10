package com.sbai.bcnews.model.search;

import com.sbai.bcnews.http.Apic;

/**
 * Modified by $nishuideyu$ on 2018/7/10
 * <p>
 * Description:
 * </p>
 * APIS:{@link Apic#requestHotSearchContent() }
 */
public class HotSearch {
    /**
     * createTime : 1531102727000
     * hotNumber : 10
     * id : 1
     * remark : 说明
     * searchCount : 10
     * statTime : 1531098000000
     * status : 1
     * type : 1
     * updateTime : 1531102948000
     * word : 比特币
     */

    private long createTime;
    private int hotNumber;
    private int id;
    private String remark;
    private int searchCount;
    private long statTime;
    private int status;
    private int type;
    private long updateTime;
    private String word;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getHotNumber() {
        return hotNumber;
    }

    public void setHotNumber(int hotNumber) {
        this.hotNumber = hotNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public long getStatTime() {
        return statTime;
    }

    public void setStatTime(long statTime) {
        this.statTime = statTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "HotSearch{" +
                "createTime=" + createTime +
                ", hotNumber=" + hotNumber +
                ", id=" + id +
                ", remark='" + remark + '\'' +
                ", searchCount=" + searchCount +
                ", statTime=" + statTime +
                ", status=" + status +
                ", type=" + type +
                ", updateTime=" + updateTime +
                ", word='" + word + '\'' +
                '}';
    }
}
