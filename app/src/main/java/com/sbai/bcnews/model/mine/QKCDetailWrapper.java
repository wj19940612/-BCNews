package com.sbai.bcnews.model.mine;

import java.util.List;

/**
 * @author yangguangda
 * @date 2018/5/23
 */
public class QKCDetailWrapper {

    /**
     * content : [{"changeType":1,"createTime":1527039927000,"id":"3","integral":1,"type":2,"typeStr":"分成收益","updateTime":1527039927000,"userId":1665},{"changeType":0,"createTime":1526999479000,"id":"1","integral":50,"type":6,"typeStr":"整点红包","updateTime":1526999479000,"userId":1665}]
     * first : true
     * last : true
     * number : 0
     * numberOfElements : 2
     * size : 20
     * totalElements : 2
     * totalPages : 1
     */

    private boolean first;
    private boolean last;
    private int number;
    private int numberOfElements;
    private int size;
    private int totalElements;
    private int totalPages;
    private List<QKCDetails> content;

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<QKCDetails> getContent() {
        return content;
    }

    public void setContent(List<QKCDetails> content) {
        this.content = content;
    }
}
