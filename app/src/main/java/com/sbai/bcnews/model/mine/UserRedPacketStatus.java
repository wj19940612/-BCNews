package com.sbai.bcnews.model.mine;

/**
 * @author yangguangda
 * @date 2018/5/24
 */
public class UserRedPacketStatus {
    public static final int ROBED = 1;
    public static final int HAVE_REDPACKET = 1;
    /**
     * redPacket : 1
     * isRob : 0
     */

    private int redPacket;
    private int isRob;

    public int getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(int redPacket) {
        this.redPacket = redPacket;
    }

    public int getIsRob() {
        return isRob;
    }

    public void setIsRob(int isRob) {
        this.isRob = isRob;
    }
}
