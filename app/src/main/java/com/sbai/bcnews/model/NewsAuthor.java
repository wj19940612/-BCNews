package com.sbai.bcnews.model;

public class NewsAuthor {
    private boolean isAttention;

    public NewsAuthor(boolean isAttention) {
        this.isAttention = isAttention;
    }

    public boolean isAttention() {
        return isAttention;
    }

    public void setAttention(boolean attention) {
        isAttention = attention;
    }
}
