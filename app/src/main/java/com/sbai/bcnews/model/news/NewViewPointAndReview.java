package com.sbai.bcnews.model.news;

import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/5/2
 * <p>
 * Description: 观点列表页面 包含评论的model
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class NewViewPointAndReview extends NewsViewpoint {

    public static final int TAG_HOT = 1;
    public static final int TAG_NORMAL = 2;

    private List<NewsViewpoint> vos;

    private int tag;

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
}
