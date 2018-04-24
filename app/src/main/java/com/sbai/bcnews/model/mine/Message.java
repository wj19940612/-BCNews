package com.sbai.bcnews.model.mine;

import com.sbai.bcnews.model.MessageStatus;

/**
 * Modified by $nishuideyu$ on 2018/4/16
 * <p>
 * Description:
 * </p>
 * APIS:{@link /api/news-msg/msg/history}
 */
public class Message implements MessageStatus {

    /**
     * createTime : 1521098828000
     * dataId : 234
     * id : 8290
     * msg : aadasdad
     * sourceUserId : 1656
     * sourceUserName : 蓝色星痕
     * sourceUserPortrait : http://thirdwx.qlogo.cn/mmopen/vi_32/EspJPrNZCIOtapXEQKuMYvmU50iaemoKXd0ia3IKGWLVNayPE1RwDL6kCLUlAwmfZRgVqAjcyFeEzZJT4xE20QqA/132
     * status : 0
     * title : bushi
     * type : 1
     * updateTime : 1521187174000
     * userId : 1664
     */

    private long createTime;
    private int dataId;
    private String id;
    private String msg;
    private int sourceUserId;
    private String sourceUserName;
    private String sourceUserPortrait;
    private int status;
    private String title;
    private int type;
    private long updateTime;
    private int userId;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(int sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public String getSourceUserName() {
        return sourceUserName;
    }

    public void setSourceUserName(String sourceUserName) {
        this.sourceUserName = sourceUserName;
    }

    public String getSourceUserPortrait() {
        return sourceUserPortrait;
    }

    public void setSourceUserPortrait(String sourceUserPortrait) {
        this.sourceUserPortrait = sourceUserPortrait;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
