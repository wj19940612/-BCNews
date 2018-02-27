package com.sbai.bcnews.model.mine;

import com.sbai.bcnews.http.Apic;

/**
 * Created by ${wangJie} on 2018/2/27.
 * {@link Apic#/api/news-user/operate/read/coolect/count}
 */

public class MsgNumber {

    /**
     * collect : 5
     * read : 12
     */

    private int collect;
    private int read;

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
