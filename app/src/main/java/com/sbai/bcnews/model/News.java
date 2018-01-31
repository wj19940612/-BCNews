package com.sbai.bcnews.model;

import java.util.List;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class News {
    private List<NewsDetail> content;

    public List<NewsDetail> getContent() {
        return content;
    }

    public void setContent(List<NewsDetail> content) {
        this.content = content;
    }
}
