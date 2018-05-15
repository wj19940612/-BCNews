package com.sbai.bcnews.model;

public class ConversionContent {
    private String content;
    private String introduce;
    private int count;

    public ConversionContent() {
    }

    public ConversionContent(String content, String introduce, int count) {
        this.content = content;
        this.introduce = introduce;
        this.count = count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
