package com.sbai.bcnews.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.WebActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.model.Banner;
import com.sbai.bcnews.model.Pop;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sbai.bcnews.view.HomeBanner.AdvertisementAdapter.IMAGE_CENTER_CROP;


public class StartBanner extends HomeBanner {

    @BindView(R.id.buttonImg)
    ImageView mButtonImg;
    @BindView(R.id.buttonText)
    TextView mButtonText;
    @BindView(R.id.buttonWrapper)
    RelativeLayout mButtonWrapper;
    private List<Pop> mPops;
    private int mCurrentPosition;

    public interface OnButtonClickListener {
        void onButtonClick();
    }


    private OnButtonClickListener mOnButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        mOnButtonClickListener = onButtonClickListener;
    }

    public StartBanner(@NonNull Context context) {
        super(context);
    }

    public StartBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.start_banner, this, true);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPageSwitched(int position) {
        if (mPops != null) {
            mCurrentPosition = position;
            Pop pop = mPops.get(position);
            setButtonView(pop);
        }
    }

    private void setButtonView(Pop pop) {
        GlideApp.with(getContext())
                .load(pop.getButtonPicUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .encodeQuality(50)
                .priority(Priority.IMMEDIATE)
                .into(mButtonImg);
        mButtonText.setText(pop.getButtonMsg());
    }

    public void setPopList(List<Pop> pops) {
        mPops = pops;
        ArrayList<Banner> banners = new ArrayList<>();
        for (Pop pop : pops) {
            Banner banner = new Banner();
            banner.setCover(pop.getPopImg());
            banners.add(banner);
        }
        super.setHomeAdvertisement(banners, IMAGE_CENTER_CROP);
        if (pops != null) {
            Pop pop = pops.get(0);
            setButtonView(pop);
        }
    }

    @OnClick(R.id.buttonWrapper)
    public void onViewClicked() {
        if (mPops != null) {
            Pop pop = mPops.get(mCurrentPosition);
            Launcher.with(getContext(), WebActivity.class)
                    .putExtra(WebActivity.EX_URL,pop.getLinkUrl())
                    .execute();

            if (mOnButtonClickListener != null) {
                mOnButtonClickListener.onButtonClick();
            }
        }
    }
}
