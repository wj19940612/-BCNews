package com.sbai.bcnews.utils;

/**
 * Modified by $nishuideyu$ on 2018/6/8
 * <p>
 * Description: 作者工作台字段格式化
 * </p>
 */
public class NumberUtils {

    private static final int RANG_NUMBER = 99999;
    private static final int TEN_THOUSAND = 10000;
    public static final String UNIT_WANG = "万";
    public static final String UNIT_YI = "亿";


    /**
     * a.小于等于99999的数据，全部展示出来
     * <p>
     * b.大于99999的数据，展示为10w+/11w+/100w/1000w+，以万为单位加上去
     * <p>
     * c.后续若出现大于9999w的数据，再另做处理
     *
     * @param number
     * @return
     */

    public static String formatNumber(int number) {

        if (number <= RANG_NUMBER) {
            return String.valueOf(number);
        } else if (number < TEN_THOUSAND * TEN_THOUSAND) {
            int i = number / TEN_THOUSAND;
            return i + UNIT_WANG + "+";
        }
        return String.valueOf(number);
    }

}
