package com.sbai.bcnews.model.news;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/5/2
 * <p>
 * Description:  评论详情页面
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class NewsViewpointAndComment  {


    /**
     * normal : [{"content":"第四个一级评论,,,第四个一级评论,,,第四个一级评论,,,第四个一级评论,,,","dataId":960799530167795700,"id":975555655874334700,"isPraise":0,"praiseCount":0,"replayCount":0,"replayTime":1521425567967,"type":0,"userId":1660,"userPortrait":"一级评论的用户头像","username":"一级评论的用户昵称"},{"content":"第三个一级评论,,,第三个一级评论,,,第三个一级评论,,,第三个一级评论,,,","dataId":960799530167795700,"id":974564139726553100,"isPraise":0,"praiseCount":5,"replayCount":0,"replayTime":1521189172093,"type":0,"userId":1660,"userPortrait":"一级评论的用户头像","username":"一级评论的用户昵称"}]
     * pageable : {"offset":0,"pageNumber":0,"pageSize":2}
     * hot : [{"content":"第四个一级评论,,,第四个一级评论,,,第四个一级评论,,,第四个一级评论,,,","dataId":960799530167795700,"id":975560274243440600,"isPraise":0,"praiseCount":0,"replayCount":4,"replayTime":1521426669071,"type":0,"userId":1660,"userPortrait":"一级评论的用户头像","username":"一级评论的用户昵称","vos":[{"content":"这是测试三级评论,去评论二级","id":975621185318268900,"isPraise":0,"replayCount":0,"replayTime":1521441191401,"userPortrait":"二级热门评论的用户头像","username":"二级热门评论的用户昵称"},{"content":"普通的二级评论......普通的二级评论......普通的二级评论......普通的二级评论......","id":975620993953148900,"isPraise":0,"replayCount":0,"replayTime":1521441145779,"userPortrait":"二级热门评论的用户头像","username":"二级热门评论的用户昵称"},{"content":"普通的二级评论......普通的二级评论......普通的二级评论......普通的二级评论......","id":975616466759798800,"isPraise":0,"replayCount":0,"replayTime":1521440066413,"userPortrait":"二级热门评论的用户头像","username":"二级热门评论的用户昵称"}]},{"content":"一级评论一级评论一级评论一级评论一级评论一级评论一级评论一级评论","dataId":960799530167795700,"id":974563788906577900,"isPraise":0,"praiseCount":15,"replayCount":2,"replayTime":1521189088454,"type":0,"userId":1660,"userPortrait":"一级评论的用户头像","username":"一级评论的用户昵称","vos":[{"content":"二级评论,,,二级评论,,,二级评论,,,二级评论,,,","id":974564951727030300,"isPraise":0,"replayCount":0,"replayTime":1521189365690,"userPortrait":"二级热门评论的用户头像","username":"二级热门评论的用户昵称"},{"content":"第二个二级评论,,,第二个二级评论,,,第二个二级评论,,,","id":974565235421364200,"isPraise":0,"replayCount":0,"replayTime":1521189433328,"userPortrait":"二级热门评论的用户头像","username":"二级热门评论的用户昵称"}]},{"content":"第二个一级评论,,,第二个一级评论,,,第二个一级评论,,,","dataId":960799530167795700,"id":974564055593009200,"isPraise":0,"praiseCount":7,"replayCount":4,"replayTime":1521189152035,"type":0,"userId":1660,"userPortrait":"一级评论的用户头像","username":"一级评论的用户昵称","vos":[{"content":"223333第二个二级评论,,,第二个二级评论,,,第二个二级评论,,,","id":974568481833631700,"isPraise":0,"replayCount":0,"replayTime":1521190207333,"userPortrait":"二级热门评论的用户头像","username":"二级热门评论的用户昵称"},{"content":"三级评论....三级评论....三级评论....","id":974568965424300000,"isPraise":0,"replayCount":0,"replayTime":1521190322629,"userPortrait":"二级热门评论的用户头像","username":"二级热门评论的用户昵称"},{"content":"324324第二个二级评论,,,第二个二级评论,,,第二个二级评论,,,","id":974568523877335000,"isPraise":0,"replayCount":0,"replayTime":1521190217356,"userPortrait":"二级热门评论的用户头像","username":"二级热门评论的用户昵称"}]}]
     */

    private PageableBean pageable;
    private List<NewViewPointAndReview> normal;
    private List<NewViewPointAndReview> hot;

    public PageableBean getPageable() {
        return pageable;
    }

    public void setPageable(PageableBean pageable) {
        this.pageable = pageable;
    }

    public List<NewViewPointAndReview> getNormal() {
        return normal;
    }

    public void setNormal(List<NewViewPointAndReview> normal) {
        this.normal = normal;
    }

    public List<NewViewPointAndReview> getHot() {
        return hot;
    }

    public void setHot(List<NewViewPointAndReview> hot) {
        this.hot = hot;
    }

    public static class PageableBean {
        /**
         * offset : 0
         * pageNumber : 0
         * pageSize : 2
         */

        private int offset;
        private int pageNumber;
        private int pageSize;

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

}
