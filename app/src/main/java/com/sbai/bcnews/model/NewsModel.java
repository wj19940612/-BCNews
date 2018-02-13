package com.sbai.bcnews.model;

import java.util.ArrayList;
import java.util.List;

import static com.sbai.bcnews.fragment.NewsFragment.NewsAdapter.TYPE_NONE;
import static com.sbai.bcnews.fragment.NewsFragment.NewsAdapter.TYPE_SINGLE;
import static com.sbai.bcnews.fragment.NewsFragment.NewsAdapter.TYPE_THREE;

/**
 * Created by Administrator on 2018\2\13 0013.
 */

public class NewsModel {

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
    public static List<NewsModel> updateImgType(List<NewsDetail> newsDetails) {
        if (newsDetails == null || newsDetails.size() == 0) {
            return new ArrayList<>();
        }
        List<NewsModel> newsModels = new ArrayList<>();
        for (int i = 0; i < newsDetails.size(); i++) {
            NewsModel newsModel = new NewsModel();
            newsModel.setNewsDetail(newsDetails.get(i));
            newsModel.setImgType(judgeImageType(newsDetails, i));
            newsModels.add(newsModel);
        }
        return newsModels;
    }

    private static int judgeImageType(List<NewsDetail> newsDetails, int position) {
        int thePicNum = newsDetails.get(position).getImgs().size();
        //图片数量少于3张，一定是单张或者0张模式
        if (thePicNum == 0) {
            return TYPE_NONE;
        } else if (thePicNum < 3) {
            return TYPE_SINGLE;
        } else {
            //三张图片，不一定是三张模式
            //这是前五张，那么此项一定是单张模式
            if (position <= 4) {
                return TYPE_SINGLE;
            }
            //前面五张全是单张模式，这里才显示3张模式
            if (judgeFiveSingleMode(newsDetails, position)) {
                return TYPE_THREE;
            } else {
                return TYPE_SINGLE;
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
