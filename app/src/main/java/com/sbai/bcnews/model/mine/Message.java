package com.sbai.bcnews.model.mine;

/**
 * Modified by $nishuideyu$ on 2018/4/16
 * <p>
 * Description:
 * </p>
 * APIS:{@link /api/news-msg/msg/history}
 */
public class Message implements MessageStatus {

    public static final int MESSAGE_IS_READ = 1;

    //类型：0 系统 1 评论 2 评论的回复 3 反馈回复 4 评论点赞 5 回复点赞 6 回复的回复
    public static final int MESSAGE_TYPE_SYSTEM = 0;
    public static final int MESSAGE_TYPE_COMMENT = 1;
    public static final int MESSAGE_TYPE_COMMENT_REVIEW = 2;
    public static final int MESSAGE_TYPE_FEED_BACK_REVIEW = 3;
    public static final int MESSAGE_TYPE_COMMENT_PRAISE = 4;
    public static final int MESSAGE_TYPE_REVIEW_PRAISE = 5;
    public static final int MESSAGE_TYPE_REVIEW_REVIEW = 6;

    public static final int MESSAGE_TYPE_ARTICLE = 7; //对文章的点赞和评论

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
    private int dataId;      //对象的id:暂时为评论的id
    private String id;       //该消息的id(阅读的时候使用)
    private String msg;
    private int sourceUserId;
    private String sourceUserName;  //给你点赞或者评论的那个人的名字
    private String sourceUserPortrait; //给你点赞或者评论的那个人的头像
    private int status;     //状态：0未读,1已读
    private String title;
    private int type;       //类型：0 系统 1 评论 2 评论的回复 3 反馈回复 4 评论点赞 5 回复点赞 6 回复的回复
    private long updateTime;
    private int userId;

    public boolean isReview() {
        return getType() == MESSAGE_TYPE_COMMENT ||
                getType() == MESSAGE_TYPE_COMMENT_REVIEW ||
                getType() == MESSAGE_TYPE_REVIEW_REVIEW;
    }

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
