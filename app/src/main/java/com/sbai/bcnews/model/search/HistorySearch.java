package com.sbai.bcnews.model.search;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbai.bcnews.Preference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/7/5
 * <p>
 * Description:
 * </p>
 */
public class HistorySearch {

    public static final int HISTORY_SEARCH_LABEL_MAX_SIZE = 9;

    private static Gson sGson = new Gson();
    private static ArrayList<String> sHistorySearchList;

    public static List<String> getHistorySearchList() {
        if (sHistorySearchList == null) {
            String historySearch = Preference.get().getHistorySearch();
            if (!TextUtils.isEmpty(historySearch)) {
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                sHistorySearchList = sGson.fromJson(historySearch, type);
                return sHistorySearchList;
            }
        }
        return sHistorySearchList;
    }

    public static void updateHistorySearch(String searchContent) {
        if (getHistorySearchList() == null) {
            sHistorySearchList = new ArrayList<>();
        }
        if (sHistorySearchList.size() > HISTORY_SEARCH_LABEL_MAX_SIZE - 1) {
            sHistorySearchList.remove(sHistorySearchList.size() - 1);
        }
        sHistorySearchList.add(0, searchContent);
        String s = sGson.toJson(sHistorySearchList);
        Preference.get().setHistoryRearch(s);
    }

}
