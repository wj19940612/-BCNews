package com.sbai.bcnews.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Banner implements Parcelable {

    private static final long serialVersionUID = -7266947195942724446L;

    public static final String FUNC_SHARE = "share";//分享
    public static final String FUNC_REGANDLOGIN = "regAndLogin";//注册登录
    public static final String FUNC_EVALUATE = "evaluate";//金融测评
    public static final String FUNC_CERTIFICATION = "certification";//实名认证

    public static final String STYLE_HTML = "html";
    public static final String STYLE_H5 = "h5";
    public static final String STYLE_FUNCTIONMODULE = "functionModule";//功能
    public static final String STYLE_ORIGINALPAGE = "originalPage";//源生页面


    public static final String QUESTION_INFO = "question";//问答详情页
    public static final String EXPLAIN_IDNEX = "explain";//姐说主页
    public static final String NEWS_INFO = "news";//要闻详情页
    public static final String TOPIC = "topic";//一日一题
    public static final String STOCK = "stock";//股票基础概念
    public static final String K_TRAIN = "k_train";//单K基础训练
    public static final String AVERAGE_TRAIN = "average_train";//均线买入信号训练
    public static final String ANNUAL = "annual";//上市公司年报
    public static final String MARKET_INFO = "market_info";//行情详情页
    public static final String GAME_AWARD = "game_award";//赏金场
    public static final String MARKET_NORMAL = "game_normal";//普通场
    public static final String USER_INFO = "user_info";//用户信息页
    public static final String USER_PURSE = "user_purse";//钱包
    public static final String USER_FEEDBACK = "user_feedback";//意见反馈
    public static final String APPRAISE = "appraise";//乐米学分页

    public static final int JUMP_TYPE_RICH_TEXT = 0;
    public static final int JUMP_TYPE_HTML = 1;
    public static final int JUMP_TYPE_NEWS = 2;


    /**
     * 首页banner
     */
    public static final int TYPE_BANNER = 0;
    public static final int STATUS_HIDE = 0;
    public static final int STATUS_SHOW = 1;
    /**
     * summary : 测试内容q550
     * content : N多美图
     * cover : 好图
     * createTime : 1492590335553
     * id : 58f71effa7670c2852bf2e29
     * index : 1
     * operator : admin
     * status : 1
     * style : html
     * title : 轮播标ssss
     * type : 1
     * updateTime : 1492590335553
     */
    private String summary;
    private String content;
    private String cover;   //封面
    private long createTime;
    private String id;
    private int index;
    private String operator;
    private int status;
    private String style;//html,h5, functionModule功能模块,originalPage原生页面
    private String title;
    private int type;
    private long updateTime;
    private int recommend;//是否前台推荐
    private int scope;//适用范围
    private int showType;//banner展示类型
    private int showcase;//banner展示位
    private String subTitle;//副标题
    private int userCount;//参与人数
    private String montageData;//活动关键字
    private int jumpType;//原生页面要跳到哪个原生页面
    private String jumpContent;
    private String jumpId;//原生页面跳过去带Id传过去

    //gift 独有的
    private int clicks; //点击次数
    private String smallPic;  //小图


    public boolean isH5Style() {
        return this.style.equalsIgnoreCase("h5");
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }


    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getShowcase() {
        return showcase;
    }

    public void setShowcase(int showcase) {
        this.showcase = showcase;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getMontageData() {
        return montageData;
    }

    public void setMontageData(String montageData) {
        this.montageData = montageData;
    }

    public int getJumpType() {
        return jumpType;
    }

    public void setJumpType(int jumpType) {
        this.jumpType = jumpType;
    }

    public String getJumpId() {
        return jumpId;
    }

    public void setJumpId(String jumpId) {
        this.jumpId = jumpId;
    }

    public String getJumpContent() {
        return jumpContent;
    }

    public void setJumpContent(String jumpContent) {
        this.jumpContent = jumpContent;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", cover='" + cover + '\'' +
                ", createTime=" + createTime +
                ", id='" + id + '\'' +
                ", index=" + index +
                ", operator='" + operator + '\'' +
                ", status=" + status +
                ", style='" + style + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", updateTime=" + updateTime +
                ", clicks=" + clicks +
                ", smallPic='" + smallPic + '\'' +
                '}';
    }

    public Banner() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.summary);
        dest.writeString(this.content);
        dest.writeString(this.cover);
        dest.writeLong(this.createTime);
        dest.writeString(this.id);
        dest.writeInt(this.index);
        dest.writeString(this.operator);
        dest.writeInt(this.status);
        dest.writeString(this.style);
        dest.writeString(this.title);
        dest.writeInt(this.type);
        dest.writeLong(this.updateTime);
        dest.writeInt(this.recommend);
        dest.writeInt(this.scope);
        dest.writeInt(this.showType);
        dest.writeInt(this.showcase);
        dest.writeString(this.subTitle);
        dest.writeInt(this.userCount);
        dest.writeString(this.montageData);
        dest.writeInt(this.clicks);
        dest.writeString(this.smallPic);
        dest.writeInt(this.jumpType);
        dest.writeString(this.jumpId);
        dest.writeString(this.jumpContent);
    }

    protected Banner(Parcel in) {
        this.summary = in.readString();
        this.content = in.readString();
        this.cover = in.readString();
        this.createTime = in.readLong();
        this.id = in.readString();
        this.index = in.readInt();
        this.operator = in.readString();
        this.status = in.readInt();
        this.style = in.readString();
        this.title = in.readString();
        this.type = in.readInt();
        this.updateTime = in.readLong();
        this.recommend = in.readInt();
        this.scope = in.readInt();
        this.showType = in.readInt();
        this.showcase = in.readInt();
        this.subTitle = in.readString();
        this.userCount = in.readInt();
        this.montageData = in.readString();
        this.clicks = in.readInt();
        this.smallPic = in.readString();
        this.jumpId = in.readString();
        this.jumpType = in.readInt();
        this.jumpContent = in.readString();
    }

    public static final Creator<Banner> CREATOR = new Creator<Banner>() {
        @Override
        public Banner createFromParcel(Parcel source) {
            return new Banner(source);
        }

        @Override
        public Banner[] newArray(int size) {
            return new Banner[size];
        }
    };
}
