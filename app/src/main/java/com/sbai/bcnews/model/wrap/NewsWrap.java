package com.sbai.bcnews.model.wrap;

import com.sbai.bcnews.model.NewsDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\2\13 0013.
 */

public class NewsWrap {

    public static final int DISPLAY_TYPE_NO_IMAGE = 1;
    public static final int DISPLAY_TYPE_SINGLE_IMAGE = 2;
    public static final int DISPLAY_TYPE_THREE_IMAGE = 3;

    private NewsDetail mNewsDetail;
    private int imgType;

    public NewsDetail getNewsDetail() {
        return mNewsDetail;
    }

    public void setNewsDetail(NewsDetail newsDetail) {
        mNewsDetail = newsDetail;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    //判断资讯列表中每项的图片显示模式  单张模式  3张模式
    public static List<NewsWrap> updateImgType(List<NewsDetail> newsDetails) {
        if (newsDetails == null || newsDetails.size() == 0) {
            return new ArrayList<>();
        }
        List<NewsWrap> newsWraps = new ArrayList<>();
        for (int i = 0; i < newsDetails.size(); i++) {
            NewsWrap newsWrap = new NewsWrap();
            newsWrap.setNewsDetail(newsDetails.get(i));
            newsWrap.setImgType(judgeImageType(newsDetails, i));
            newsWraps.add(newsWrap);
        }
        return newsWraps;
    }

    private static int judgeImageType(List<NewsDetail> newsDetails, int position) {
        int thePicNum = newsDetails.get(position).getImgs().size();
        //图片数量少于3张，一定是单张或者0张模式
        if (thePicNum == 0) {
            return DISPLAY_TYPE_NO_IMAGE;
        } else if (thePicNum < 3) {
            return DISPLAY_TYPE_SINGLE_IMAGE;
        } else {
            //三张图片，不一定是三张模式
            //这是前五张，那么此项一定是单张模式
            if (position <= 4) {
                return DISPLAY_TYPE_SINGLE_IMAGE;
            }
            //前面五张全是单张模式，这里才显示3张模式
            if (judgeFiveSingleMode(newsDetails, position)) {
                return DISPLAY_TYPE_THREE_IMAGE;
            } else {
                return DISPLAY_TYPE_SINGLE_IMAGE;
            }
        }
    }

    //前面五item是否全为单张模式
    private static boolean judgeFiveSingleMode(List<NewsDetail> newsDetails, int position) {
        if (position <= 4) {
            return true;
        }
        for (int i = position - 1; i > position - 5; i--) {
            //前面五项中的每个item，如果图片超过2张并且这个item的前面五项也都是单张模式，那么这个item必定是3张模式
            if (newsDetails.get(i).getImgs().size() > 2 && judgeFiveSingleMode(newsDetails, i)) {
                return false;
            }
        }
        return true;
    }
}
