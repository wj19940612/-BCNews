package com.sbai.bcnews.http;

import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.sbai.bcnews.BuildConfig;
import com.sbai.httplib.CookieManger;
import com.sbai.httplib.GsonRequest;
import com.sbai.httplib.MultipartRequest;
import com.sbai.httplib.ReqCallback;
import com.sbai.httplib.ReqError;
import com.sbai.httplib.ReqHeaders;
import com.sbai.httplib.ReqIndeterminate;
import com.sbai.httplib.ReqLogger;
import com.sbai.httplib.ReqParams;
import com.sbai.httplib.RequestManager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Modified by john on 23/01/2018
 * <p>
 * Description: 请求 Api 基础类
 * <p>
 */
public class ReqApi extends RequestManager {

    private static Set<String> sCurrentUrls = new HashSet<>();

    private int mMethod;
    private String mId;
    private String mTag;
    private String mApi;
    private String mJsonBody;
    private ReqCallback<?> mCallback;
    private ReqParams mReqParams;
    private ReqIndeterminate mIndeterminate;
    private int mTimeout;

    private File mFile;
    private String mFileName;

    private ReqApi() {
        mMethod = GET;
    }

    public static ReqApi get(String api) {
        ReqApi reqApi = new ReqApi();
        reqApi.mApi = api;
        return reqApi;
    }

    public static ReqApi get(String api, ReqParams reqParams) {
        ReqApi reqApi = new ReqApi();
        reqApi.mApi = api;
        reqApi.mReqParams = reqParams;
        return reqApi;
    }

    public static ReqApi post(String api, ReqParams reqParams) {
        ReqApi reqApi = new ReqApi();
        reqApi.mMethod = POST;
        reqApi.mApi = api;
        reqApi.mReqParams = reqParams;
        return reqApi;
    }

    public static ReqApi post(String api, ReqParams reqParams, String jsonBody) {
        ReqApi reqApi = new ReqApi();
        reqApi.mMethod = POST;
        reqApi.mApi = api;
        reqApi.mReqParams = reqParams;
        reqApi.mJsonBody = jsonBody;
        return reqApi;
    }

    public ReqApi id(String id) {
        mId = id;
        return this;
    }

    public ReqApi tag(String tag) {
        mTag = tag;
        return this;
    }

    public ReqApi callback(ReqCallback<?> callback) {
        mCallback = callback;
        return this;
    }

    public ReqApi indeterminate(ReqIndeterminate indeterminate) {
        mIndeterminate = indeterminate;
        return this;
    }

    public ReqApi timeout(int timeout) {
        mTimeout = timeout;
        return this;
    }

    public void fire() {
        String url = getUrl();
        synchronized (sCurrentUrls) {
            if (sCurrentUrls.add(mTag + "#" + url)) {
                createReqThenEnqueue(url);
            }
        }
    }

    public void fireFreely() {
        String url = getUrl();
        createReqThenEnqueue(url);
    }

    private void createReqThenEnqueue(String url) {
        ReqHeaders headers = new ReqHeaders();
        setupHeaders(headers);

        // setup Callback
        if (mCallback != null) {
            mCallback.setUrl(url);
            mCallback.setOnFinishedListener(new RequestFinishedListener());
            mCallback.setId(mId);
            mCallback.setTag(mTag);
            mCallback.setIndeterminate(mIndeterminate);
        } else { // create a default callback for handle request finish event
            mCallback = new ReqCallback<Object>() {
                @Override
                public void onSuccess(Object o) {
                }

                @Override
                public void onFailure(ReqError reqError) {
                }
            };
            mCallback.setUrl(url);
            mCallback.setOnFinishedListener(new RequestFinishedListener());
        }

        // new request
        Request request;
        Type type = mCallback.getGenericType();
        if (mFile != null && !TextUtils.isEmpty(mFileName)) {
            request = new MultipartRequest(mMethod, url, headers, mFileName, mFile, mReqParams, type, mCallback);
        } else {
            request = new GsonRequest(mMethod, url, headers, mReqParams, mJsonBody, type, mCallback);
        }
        request.setTag(mTag);

        if (mTimeout != 0) {
            request.setRetryPolicy(new DefaultRetryPolicy(mTimeout, 1, 1));
        }

        // start and enqueue
        mCallback.onStart();
        enqueue(request);
    }

    protected void setupHeaders(ReqHeaders headers) {
        String cookies = CookieManger.getInstance().getCookies();
        if (!TextUtils.isEmpty(cookies)) {
            headers.put("Cookie", cookies);
        }
//        headers.put("lemi-version", AppInfo.getVersionName(App.getAppContext()))
//                .put("lemi-device", AppInfo.getDeviceHardwareId(App.getAppContext()))
//                .put("lemi-channel", "android:" + AppInfo.getMetaData(App.getAppContext(), "UMENG_CHANNEL"));
    }

    private static class RequestFinishedListener implements ReqCallback.onFinishedListener {

        public void onFinished(String tag, String url) {
            if (sCurrentUrls != null) {
                sCurrentUrls.remove(tag + "#" + url);
            }
        }
    }

    private String getUrl() {
        String url = new StringBuilder(getHost()).append(mApi).toString();
        if (mMethod == GET && mReqParams != null) {
            url += mReqParams.toString();
            mReqParams = null;
        }
        return url;
    }

    private static String getHost() {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("dev")) {
            return "http://" + BuildConfig.HOST;
        }
        return "https://" + BuildConfig.HOST;
    }

    public static void cancel(String tag) {
        RequestManager.cancel(tag);
        Iterator<String> iterator = sCurrentUrls.iterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            if (str.startsWith(tag + "#")) {
                iterator.remove();

                ReqLogger logger = getLogger();
                if (logger != null) {
                    logger.onTag("req of " + tag + " is not finish, cancel (" + str + ")");
                }
            }
        }
    }
}
