package com.sbai.bcnews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 登录后的用户信息数据
 */
public class UserInfo implements Parcelable {


    public static final int WECHAT_BIND_STATUS_NOT_BIND = 0;
    public static final int WECHAT_BIND_STATUS_BIND = 1;
    public static final int WECHAT_BIND_STATUS_UNBIND = 2;

    public static final int USER_SEX_GIRL = 1;
    public static final int USER_SEX_BOY = 2;

    /**
     * createTime : 1518141650000
     * id : 1658
     * lastLoginTime : 1518141650000
     * loginErrorNum : 0
     * loginIp : 192.168.0.90
     * loginNum : 0
     * registrationIp : 192.168.0.90
     * status : 0
     * userPhone : 18182568093
     * userSex : 0
     * userType : 0
     */

    private String userPortrait;
    private String userName;
    private int collectCount;
    private int readCount;
    private long createTime;
    private int id;
    private long lastLoginTime;
    private int loginErrorNum;
    private String loginIp;
    private int loginNum;
    private String registrationIp;
    private int status;
    private String userPhone;
    private int userSex;
    private int userType;
    private String userProvince;
    private String city;
    private String userAddress;
    private String introduction;
    /**
     * age : 25
     * birthday : 93.12.12
     * deviceId : asadas
     * platform : 0
     * source : ios
     * userCity : huainan
     * userEmail : 1031214092@cc.com
     * userPass :
     */

    private int age;
    private String birthday;
    private String deviceId;
    private int platform;
    private String source;
    private String userCity;
    private String userEmail;
    private String userPass;


    private String wxOpenId;
    private int wxBound; //微信是否绑定:0未绑定微信,1绑定,2解绑
    private String wxName;

    public UserInfo() {
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public int getWxBound() {
        return wxBound;
    }

    public void setWxBound(int wxBound) {
        this.wxBound = wxBound;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getUserProvince() {
        return userProvince;
    }

    public void setUserProvince(String userProvince) {
        this.userProvince = userProvince;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
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


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getLoginErrorNum() {
        return loginErrorNum;
    }

    public void setLoginErrorNum(int loginErrorNum) {
        this.loginErrorNum = loginErrorNum;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public int getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }

    public String getRegistrationIp() {
        return registrationIp;
    }

    public void setRegistrationIp(String registrationIp) {
        this.registrationIp = registrationIp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userPortrait='" + userPortrait + '\'' +
                ", userName='" + userName + '\'' +
                ", collectCount=" + collectCount +
                ", readCount=" + readCount +
                ", createTime=" + createTime +
                ", id=" + id +
                ", lastLoginTime=" + lastLoginTime +
                ", loginErrorNum=" + loginErrorNum +
                ", loginIp='" + loginIp + '\'' +
                ", loginNum=" + loginNum +
                ", registrationIp='" + registrationIp + '\'' +
                ", status=" + status +
                ", userPhone='" + userPhone + '\'' +
                ", userSex=" + userSex +
                ", userType=" + userType +
                ", userProvince='" + userProvince + '\'' +
                ", city='" + city + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", introduction='" + introduction + '\'' +
                ", age=" + age +
                ", birthday='" + birthday + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", platform=" + platform +
                ", source='" + source + '\'' +
                ", userCity='" + userCity + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPass='" + userPass + '\'' +
                ", wxOpenId='" + wxOpenId + '\'' +
                ", wxBound=" + wxBound +
                ", wxName='" + wxName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userPortrait);
        dest.writeString(this.userName);
        dest.writeInt(this.collectCount);
        dest.writeInt(this.readCount);
        dest.writeLong(this.createTime);
        dest.writeInt(this.id);
        dest.writeLong(this.lastLoginTime);
        dest.writeInt(this.loginErrorNum);
        dest.writeString(this.loginIp);
        dest.writeInt(this.loginNum);
        dest.writeString(this.registrationIp);
        dest.writeInt(this.status);
        dest.writeString(this.userPhone);
        dest.writeInt(this.userSex);
        dest.writeInt(this.userType);
        dest.writeString(this.userProvince);
        dest.writeString(this.city);
        dest.writeString(this.userAddress);
        dest.writeString(this.introduction);
        dest.writeInt(this.age);
        dest.writeString(this.birthday);
        dest.writeString(this.deviceId);
        dest.writeInt(this.platform);
        dest.writeString(this.source);
        dest.writeString(this.userCity);
        dest.writeString(this.userEmail);
        dest.writeString(this.userPass);
        dest.writeString(this.wxOpenId);
        dest.writeInt(this.wxBound);
        dest.writeString(this.wxName);
    }

    protected UserInfo(Parcel in) {
        this.userPortrait = in.readString();
        this.userName = in.readString();
        this.collectCount = in.readInt();
        this.readCount = in.readInt();
        this.createTime = in.readLong();
        this.id = in.readInt();
        this.lastLoginTime = in.readLong();
        this.loginErrorNum = in.readInt();
        this.loginIp = in.readString();
        this.loginNum = in.readInt();
        this.registrationIp = in.readString();
        this.status = in.readInt();
        this.userPhone = in.readString();
        this.userSex = in.readInt();
        this.userType = in.readInt();
        this.userProvince = in.readString();
        this.city = in.readString();
        this.userAddress = in.readString();
        this.introduction = in.readString();
        this.age = in.readInt();
        this.birthday = in.readString();
        this.deviceId = in.readString();
        this.platform = in.readInt();
        this.source = in.readString();
        this.userCity = in.readString();
        this.userEmail = in.readString();
        this.userPass = in.readString();
        this.wxOpenId = in.readString();
        this.wxBound = in.readInt();
        this.wxName = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}


