package com.sbai.bcnews.activity.comment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;

import com.sbai.bcnews.Preference;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsViewPointListActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.fragment.dialog.WhistleBlowingDialogFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.mine.ReadHistoryOrMyCollect;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.ClipboardUtils;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.view.NewsShareDialog;
import com.sbai.bcnews.view.ShareDialog;
import com.sbai.httplib.ReqError;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Modified by $nishuideyu$ on 2018/4/28
 * <p>
 * Description:  资讯详情  评论 评论详情都有底部分享 收藏等
 * {@link com.sbai.bcnews.activity.NewsDetailActivity}和{@link NewsViewPointListActivity}
 * {@link com.sbai.bcnews.activity.CommentDetailActivity}
 * </p>
 */
public abstract class NewsShareOrCommentBaseActivity extends RecycleViewSwipeLoadActivity implements WhistleBlowingDialogFragment.OnWhistleBlowingReasonListener {

    protected NewsDetail mNewsDetail;

    public static final int PAGE_TYPE_NEWS_DETAIL = 0; //资讯详情页
    public static final int PAGE_TYPE_NEWS_VIEWPOINT = 1; //资讯观点页
    public static final int PAGE_TYPE_VIEWPOINT_DETAIL = 2; //观点详情页

    protected static final String ACTION_WEB_TEXT_SIZE_HAS_CHANGE = "action_web_text_size_has_change";


    protected int mWebTextSize;

    protected abstract int getPageType();

    protected abstract void onReceiveBroadcast(Context context, Intent intent);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebTextSize = Preference.get().getLocalWebTextSize();
        registersBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegistersBroadcastReceiver();
    }

    protected void registersBroadcastReceiver() {
        registerReceiver(sBroadcastReceiver, getIntentFilter());
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_WEB_TEXT_SIZE_HAS_CHANGE);
        return intentFilter;
    }

    protected void unRegistersBroadcastReceiver() {
        unregisterReceiver(sBroadcastReceiver);
    }

    private UMShareListener mUMShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            ToastUtil.show(R.string.share_succeed);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            //   ToastUtil.show(R.string.share_failed);
            ToastUtil.show(throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            ToastUtil.show(R.string.share_cancel);
        }
    };

    protected void shareToPlatform(SHARE_MEDIA platform) {
        umengEventCount(UmengCountEventId.NEWS05);
        if (mNewsDetail == null) return;
        UMWeb mWeb = new UMWeb(String.format(Apic.url.SHARE_NEWS, mNewsDetail.getId()));
        mWeb.setTitle(mNewsDetail.getTitle());
        mWeb.setDescription(getSummaryData());
        UMImage thumb;
        if (mNewsDetail.getImgs() == null || mNewsDetail.getImgs().isEmpty()) {
            thumb = new UMImage(getActivity(), R.mipmap.ic_launcher);
        } else {
            thumb = new UMImage(getActivity(), mNewsDetail.getImgs().get(0));
        }
        mWeb.setThumb(thumb);
        new ShareAction(getActivity())
                .withMedia(mWeb)
                .setPlatform(platform)
                .setCallback(mUMShareListener)
                .share();
        Apic.share(mNewsDetail.getId(), 0).tag(TAG).fireFreely();

    }

    protected void showShareDialog() {
        if (mNewsDetail == null) return;
        String shareThumbUrl = null;
        if (mNewsDetail.getImgs() != null && !mNewsDetail.getImgs().isEmpty()) {
            shareThumbUrl = mNewsDetail.getImgs().get(0);
        }
        ShareDialog.with(getActivity())
                .setTitleVisible(false)
                .setShareTitle(mNewsDetail.getTitle())
                .setShareDescription(getSummaryData())
                .setListener(new ShareDialog.OnShareDialogCallback() {
                    @Override
                    public void onSharePlatformClick(ShareDialog.SHARE_PLATFORM platform) {
                        Apic.share(mNewsDetail.getId(), 0).tag(TAG).fireFreely();
                    }
                })
                .setShareUrl(String.format(Apic.url.SHARE_NEWS, mNewsDetail.getId()))
                .setShareThumbUrl(shareThumbUrl)
                .show();

    }


    protected String getSummaryData() {
        if (TextUtils.isEmpty(mNewsDetail.getSummary())) {
            String content = new String(mNewsDetail.getContent());
            String imgTag = "<img\\s[^>]+>";
            Pattern pattern = Pattern.compile(imgTag);
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll("");
            content = Html.fromHtml(content).toString();
            if (content.length() > 150) {
                return content.substring(0, 150);
            } else {
                return content;
            }
        }
        return mNewsDetail.getSummary();
    }

    protected void showNewsShareAndSettingDialog() {
        if (mNewsDetail != null) {
            String shareThumbUrl = null;
            if (mNewsDetail.getImgs() != null && !mNewsDetail.getImgs().isEmpty()) {
                shareThumbUrl = mNewsDetail.getImgs().get(0);
            }
            showNewsShareAndSettingDialog(String.format(Apic.url.SHARE_NEWS, mNewsDetail.getId()), shareThumbUrl, mNewsDetail.getTitle(), getSummaryData(), mNewsDetail.getId());
        }
    }


    protected void showNewsShareAndSettingDialog(final String shareUrl, String shareThumbUrl, String shareTitle, String shareDescription, final Object id) {
        NewsShareDialog.with(getActivity())
                .setTextSize(mWebTextSize)
                .setOnNewsSettingClickListener(new NewsShareDialog.OnNewsSettingClickListener() {
                    @Override
                    public void onWhistleBlowing() {
                        if (mNewsDetail != null) {
                            WhistleBlowingDialogFragment.newInstance(WhistleBlowingDialogFragment.WHISTLE_BLOWING_TYPE_ARTICLE, mNewsDetail.getId()).show(getSupportFragmentManager());
                        }
                    }

                    @Override
                    public void subTextSize(int textSize) {
                        mWebTextSize = textSize;
                        NewsShareOrCommentBaseActivity.this.subTextSize(textSize);
                    }

                    @Override
                    public void addTextSize(int textSize) {
                        mWebTextSize = textSize;
                        NewsShareOrCommentBaseActivity.this.addTextSize(textSize);
                    }

                    @Override
                    public void copyLink() {
                        ClipboardUtils.clipboardText(getActivity(), shareUrl, R.string.copy_success);
                    }

                })
                .setTitleVisible(false)
                .setShareTitle(shareTitle)
                .setShareDescription(shareDescription)

                .setListener(new ShareDialog.OnShareDialogCallback() {
                    @Override
                    public void onSharePlatformClick(ShareDialog.SHARE_PLATFORM platform) {
                        Apic.share(id, 0).tag(TAG).fireFreely();
                    }
                })
                .setShareUrl(shareUrl)
                .setShareThumbUrl(shareThumbUrl)
                .show();
    }


    protected void collect() {
        collect(mNewsDetail);
    }

    protected void collect(final NewsDetail newsDetail) {
        if (newsDetail != null && LocalUser.getUser().isLogin()) {
            Apic.collectOrCancelCollect(newsDetail.getId(), newsDetail.getCollect(), ReadHistoryOrMyCollect.MESSAGE_TYPE_COLLECT)
                    .tag(TAG)
                    .callback(new Callback<Resp>() {
                        @Override
                        protected void onRespSuccess(Resp resp) {
                            if (newsDetail.getCollect() == 0) {
                                newsDetail.setCollect(1);
                                ToastUtil.show(R.string.collect_success);
                                umengEventCount(UmengCountEventId.NEWS04);
                            } else {
                                ToastUtil.show(R.string.cancel_collect_success);
                                newsDetail.setCollect(0);
                            }
                            onCollectSuccess(newsDetail);
                            updateCollect(newsDetail);
                        }

                        @Override
                        public void onFailure(ReqError reqError) {
                            super.onFailure(reqError);
                            ToastUtil.show(R.string.collect_fail);
                        }
                    }).fireFreely();
        } else if (!LocalUser.getUser().isLogin()) {
            Launcher.with(this, LoginActivity.class).executeForResult(LoginActivity.REQ_CODE_LOGIN);
        }
    }

    protected void onCollectSuccess(NewsDetail newsDetail) {

    }

    protected void updateCollect(NewsDetail newsDetail) {

    }


    protected void subTextSize(int textSize) {
        // 缩小webView字体大小
    }

    protected void addTextSize(int textSize) {
        // 扩大webView字体大小
    }


    @Override
    public void onChooseReason(int position, String content, int type, String id) {
        submitWhistleBlowing(content, type, id);
    }

    private void submitWhistleBlowing(String content, int type, String id) {
        Apic.submitWhistleBlowing(id, type, content)
                .tag(TAG)
                .callback(new Callback<Object>() {
                    @Override
                    protected void onRespSuccess(Object resp) {

                    }
                })
                .fire();
    }

    protected void requestData(String id) {
        Apic.getNewsDetail(id).tag(TAG).callback(new Callback2D<Resp<NewsDetail>, NewsDetail>() {
            @Override
            protected void onRespSuccessData(NewsDetail data) {
                updateNewsDetail(data);
            }

            @Override
            protected void onRespFailure(Resp failedResp) {
                super.onRespFailure(failedResp);
                //收藏文章没有查到
                if (failedResp.getCode() == Resp.CODE_MSG_NOT_FIND) {
//                    finish();
                }
            }
        }).fireFreely();
    }

    protected void updateNewsDetail(NewsDetail data) {

    }


    protected BroadcastReceiver sBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null)
                onReceiveBroadcast(context, intent);
        }

    };
}
