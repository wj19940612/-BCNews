package com.sbai.bcnews.model.system;

/**
 * Modified by $nishuideyu$ on 2018/5/25
 * <p>
 * Description: 我的页面 qkc 和邀请有礼tab
 * </p>
 */
public class MintTabStatus {

    public static final int MINE_QKC_TAB_SHOW = 1;
    public static final int MINE_INVITE_HAS_GIFT_TAB_SHOW = 1;

    /**
     * promoterShow : 0
     * integralShow : 0
     */

    private int promoterShow; //promoterShow 邀请开关:0关,1开
    private int integralShow; // 积分开关：0关,1开


    public int getPromoterShow() {
        return promoterShow;
    }

    public void setPromoterShow(int promoterShow) {
        this.promoterShow = promoterShow;
    }

    public int getIntegralShow() {
        return integralShow;
    }

    public void setIntegralShow(int integralShow) {
        this.integralShow = integralShow;
    }

    @Override
    public String toString() {
        return "MintTabStatus{" +
                "promoterShow=" + promoterShow +
                ", integralShow=" + integralShow +
                '}';
    }
}
