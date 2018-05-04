package com.sbai.bcnews.model.news;

/**
 * Modified by $nishuideyu$ on 2018/5/3
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public interface ViewpointType {
    int FIRST_COMMENT = 0;
    int SECOND_COMMENT = 1;
    int THIRD_COMMENT = 2;
    int THIRD_REPLY = 3;

    int NEWS_PAGE_SIZE = 50;
    int NEWS_LOAD_MORE_PAGE_SIZE = 30;
}
