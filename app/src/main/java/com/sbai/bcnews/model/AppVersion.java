package com.sbai.bcnews.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 版本升级相关类
 */
public class AppVersion implements Parcelable {
    private static final String TAG = "AppVersionModel";

    /**
     * createTime : 1499085818000
     * downloadUrl : www.baidu.com
     * forceUpdateAllPreVersions : 0
     * forceUpdatePreVersions : 1.0，1.1，1.2
     * id : 1
     * lastVersion : 1.5
     * modifyTime : 1499085818000
     * modifyUser : 0
     * platform : 0
     * remark : 备注
     * updateAllPreVersions : 0
     * updateLog : 这是一次非常重要的更新
     */

    private long createTime;
    private String downloadUrl;
    //是否强制更新之前版本  0 false   1 true
    private int forceUpdateAllPreVersions;
    //强制更新版本
    private String forceUpdatePreVersions;
    private int id;
    //最新版本
    private String lastVersion;
    private long modifyTime;
    private int modifyUser;
    private int platform;
    private String remark;
    //更新所有之前版本   0 false   1 true
    private int updateAllPreVersions;
    private String updateLog;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getForceUpdateAllPreVersions() {
        return forceUpdateAllPreVersions;
    }

    public void setForceUpdateAllPreVersions(int forceUpdateAllPreVersions) {
        this.forceUpdateAllPreVersions = forceUpdateAllPreVersions;
    }

    public String getForceUpdatePreVersions() {
        return forceUpdatePreVersions;
    }

    public void setForceUpdatePreVersions(String forceUpdatePreVersions) {
        this.forceUpdatePreVersions = forceUpdatePreVersions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(int modifyUser) {
        this.modifyUser = modifyUser;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getUpdateAllPreVersions() {
        return updateAllPreVersions;
    }

    public void setUpdateAllPreVersions(int updateAllPreVersions) {
        this.updateAllPreVersions = updateAllPreVersions;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    //是否强制更新
    public boolean isForceUpdate() {
        // TODO: 2018\1\22 0022 判断版本
        return false;
//        String versionName = AppInfo.getVersionName(App.getAppContext());
//        if (getForceUpdateAllPreVersions() == 1 && isLastVersionLawful()) {
//            return true;
//        } else if (isLastVersionLawful()) {
//            if (TextUtils.isEmpty(getForceUpdatePreVersions())) {
//                return false;
//            }
//            String[] split = getForceUpdatePreVersions().split(",");
//            for (String data : split) {
//                if (data.equalsIgnoreCase(versionName)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//        return false;
    }

    public boolean isNeedUpdate() {
        return updateAllPreVersions == 1 && isLastVersionLawful();
    }

    public boolean isLastVersionLawful() {
        return false;
        //TODO 判断版本
//        if (TextUtils.isEmpty(getLastVersion())) {
//            return false;
//        }
//        return AppInfo.getVersionName(App.getAppContext()).compareToIgnoreCase(getLastVersion()) < 0;
    }

    @Override
    public String toString() {
        return "AppVersionModel{" +
                "createTime=" + createTime +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", forceUpdateAllPreVersions=" + forceUpdateAllPreVersions +
                ", forceUpdatePreVersions='" + forceUpdatePreVersions + '\'' +
                ", id=" + id +
                ", lastVersion='" + lastVersion + '\'' +
                ", modifyTime=" + modifyTime +
                ", modifyUser=" + modifyUser +
                ", platform=" + platform +
                ", remark='" + remark + '\'' +
                ", updateAllPreVersions=" + updateAllPreVersions +
                ", updateLog='" + updateLog + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeString(this.downloadUrl);
        dest.writeInt(this.forceUpdateAllPreVersions);
        dest.writeString(this.forceUpdatePreVersions);
        dest.writeInt(this.id);
        dest.writeString(this.lastVersion);
        dest.writeLong(this.modifyTime);
        dest.writeInt(this.modifyUser);
        dest.writeInt(this.platform);
        dest.writeString(this.remark);
        dest.writeInt(this.updateAllPreVersions);
        dest.writeString(this.updateLog);
    }

    public AppVersion() {
    }

    protected AppVersion(Parcel in) {
        this.createTime = in.readLong();
        this.downloadUrl = in.readString();
        this.forceUpdateAllPreVersions = in.readInt();
        this.forceUpdatePreVersions = in.readString();
        this.id = in.readInt();
        this.lastVersion = in.readString();
        this.modifyTime = in.readLong();
        this.modifyUser = in.readInt();
        this.platform = in.readInt();
        this.remark = in.readString();
        this.updateAllPreVersions = in.readInt();
        this.updateLog = in.readString();
    }

    public static final Creator<AppVersion> CREATOR = new Creator<AppVersion>() {
        @Override
        public AppVersion createFromParcel(Parcel source) {
            return new AppVersion(source);
        }

        @Override
        public AppVersion[] newArray(int size) {
            return new AppVersion[size];
        }
    };
}
