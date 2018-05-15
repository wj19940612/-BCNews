package com.sbai.bcnews.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.sbai.bcnews.activity.comment.NewsShareOrCommentBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/5/2
 * <p>
 * Description: 观点列表页面 包含评论的model
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class NewViewPointAndReview extends NewsViewpoint implements NewsShareOrCommentBaseActivity.PraiseContent {


    public static NewViewPointAndReview getNewViewPointAndReview(NewsViewpoint newsViewpoint) {
        NewViewPointAndReview newViewPointAndReview = new NewViewPointAndReview();
        newViewPointAndReview.setUsername(newsViewpoint.getUsername());
        newViewPointAndReview.setUserPortrait(newsViewpoint.getUserPortrait());
        newViewPointAndReview.setUserId(newsViewpoint.getUserId());
        newViewPointAndReview.setPraiseCount(newsViewpoint.getPraiseCount());
        newViewPointAndReview.setIsPraise(newsViewpoint.getIsPraise());
        newViewPointAndReview.setReplayTime(newsViewpoint.getReplayTime());
        newViewPointAndReview.setVos(new ArrayList<NewsViewpoint>());
        newViewPointAndReview.setId(newsViewpoint.getId());
        newViewPointAndReview.setContent(newsViewpoint.getContent());
        newViewPointAndReview.setDataId(newsViewpoint.getDataId());
        newViewPointAndReview.setType(newsViewpoint.getType());
        newViewPointAndReview.setReplayCount(newsViewpoint.getReplayCount());
        return newViewPointAndReview;
    }

    @Override
    public String getViewpointId() {
        return getId();
    }

    @Override
    public String getNewsDataId() {
        return getDataId();
    }

    @Override
    public Integer getPraisedUserId() {
        return getUserId();
    }

    @Override
    public int getPraiseType() {
        return getType();
    }

    public static final int TAG_HOT = 1;
    public static final int TAG_NORMAL = 2;

    private List<NewsViewpoint> vos;

    private int tag;

    //记录是否展开
    public boolean isSpread;

    public boolean isSpread() {
        return isSpread;
    }

    public void setSpread(boolean spread) {
        isSpread = spread;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public List<NewsViewpoint> getVos() {
        return vos;
    }

    public void setVos(List<NewsViewpoint> vos) {
        this.vos = vos;
    }

    public NewViewPointAndReview() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
//        dest.writeParcelable(this.mViewPointComment, flags);
        dest.writeList(this.vos);
        dest.writeInt(this.tag);
    }

    protected NewViewPointAndReview(Parcel in) {
        super(in);
//        this.mViewPointComment = in.readParcelable(ViewPointComment.class.getClassLoader());
        this.vos = new ArrayList<NewsViewpoint>();
        in.readList(this.vos, NewsViewpoint.class.getClassLoader());
        this.tag = in.readInt();
    }

    public static final Creator<NewViewPointAndReview> CREATOR = new Creator<NewViewPointAndReview>() {
        @Override
        public NewViewPointAndReview createFromParcel(Parcel source) {
            return new NewViewPointAndReview(source);
        }

        @Override
        public NewViewPointAndReview[] newArray(int size) {
            return new NewViewPointAndReview[size];
        }
    };

}
