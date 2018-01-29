package com.sbai.bcnews.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.glide.GlideApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Modified by john on 24/01/2018
 * <p>
 * Description: 资讯
 * <p>
 * APIs:
 */
public class NewsFragment extends BaseFragment {

    private Unbinder mBind;
    @BindView(R.id.news)
    TextView mNewsView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    

    @OnClick({R.id.news})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    public static class NewsAdapter extends BaseAdapter {

        private int continuesPic;
        private Context mContext;

        public static final int TYPE_NONE = 1;
        public static final int TYPE_SINGLE = 2;
        public static final int TYPE_THREE = 3;

        private List<NewsDetail> items;

        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public NewsDetail getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            NewsDetail news = items.get(position);
            int thePicNum = news.getImgs().size();
            if (continuesPic <= 4) {
                continuesPic++;
                if (thePicNum == 0)
                    return TYPE_NONE;
                else
                    return TYPE_SINGLE;
            } else {
                if (thePicNum == 3) {
                    continuesPic = 0;
                    return TYPE_THREE;
                } else if (thePicNum == 1) {
                    continuesPic++;
                    return TYPE_SINGLE;
                } else {
                    continuesPic++;
                    return TYPE_NONE;
                }
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NoneHolder noneHolder;
            SingleHolder singleHolder;
            ThreeHolder threeHolder;
            switch (getItemViewType(position)) {
                case TYPE_NONE:
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news_none, null);
                        noneHolder = new NoneHolder(convertView);
                        convertView.setTag(noneHolder);
                    } else {
                        noneHolder = (NoneHolder) convertView.getTag();
                    }

                    noneHolder.bindingData(mContext, getItem(position), position, getCount());
                    return convertView;
                case TYPE_SINGLE:
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news_single, null);
                        singleHolder = new SingleHolder(convertView);
                        convertView.setTag(singleHolder);
                    } else {
                        singleHolder = (SingleHolder) convertView.getTag();
                    }

                    singleHolder.bindingData(mContext, getItem(position), position, getCount());
                    return convertView;
                case TYPE_THREE:
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news_three, null);
                        threeHolder = new ThreeHolder(convertView);
                        convertView.setTag(threeHolder);
                    } else {
                        threeHolder = (ThreeHolder) convertView.getTag();
                    }

                    threeHolder.bindingData(mContext, getItem(position), position, getCount());
                    return convertView;
            }
            return null;
        }

        static class NoneHolder {
            @BindView(R.id.title)
            TextView mTitle;
            @BindView(R.id.original)
            TextView mOriginal;
            @BindView(R.id.source)
            TextView mSource;
            @BindView(R.id.time)
            TextView mTime;
            @BindView(R.id.contentRL)
            RelativeLayout mContentRL;
            @BindView(R.id.line)
            View mLine;

            NoneHolder(View view) {
                ButterKnife.bind(this, view);
            }

            public void bindingData(Context context, NewsDetail item, int position, int count) {
                mTitle.setText(item.getTitle());
                mSource.setText(item.getSource());
                mTime.setText(DateUtil.formatDefaultStyleTime(item.getReleaseTime()));
                if (count - 1 == position) {
                    mLine.setVisibility(View.GONE);
                }
            }
        }

        static class SingleHolder {
            @BindView(R.id.img)
            ImageView mImg;
            @BindView(R.id.title)
            TextView mTitle;
            @BindView(R.id.original)
            TextView mOriginal;
            @BindView(R.id.source)
            TextView mSource;
            @BindView(R.id.time)
            TextView mTime;
            @BindView(R.id.contentRL)
            RelativeLayout mContentRL;
            @BindView(R.id.line)
            View mLine;

            SingleHolder(View view) {
                ButterKnife.bind(this, view);
            }

            public void bindingData(Context context, NewsDetail item, int position, int count) {
                mTitle.setText(item.getTitle());
                mSource.setText(item.getSource());
                mTime.setText(DateUtil.formatDefaultStyleTime(item.getReleaseTime()));
                if (item.getImgs() != null && item.getImgs().size() > 0) {
                    GlideApp.with(context).load(item.getImgs().get(0))
                            .placeholder(R.drawable.ic_default_image)
                            .centerCrop()
                            .into(mImg);
                }
                if (count - 1 == position) {
                    mLine.setVisibility(View.GONE);
                }
            }
        }

        static class ThreeHolder {
            @BindView(R.id.title)
            TextView mTitle;
            @BindView(R.id.img1)
            ImageView mImg1;
            @BindView(R.id.img2)
            ImageView mImg2;
            @BindView(R.id.img3)
            ImageView mImg3;
            @BindView(R.id.original)
            TextView mOriginal;
            @BindView(R.id.source)
            TextView mSource;
            @BindView(R.id.time)
            TextView mTime;
            @BindView(R.id.contentRL)
            RelativeLayout mContentRL;
            @BindView(R.id.line)
            View mLine;

            ThreeHolder(View view) {
                ButterKnife.bind(this, view);
            }

            public void bindingData(Context context, NewsDetail item, int position, int count) {
                mTitle.setText(item.getTitle());
                mSource.setText(item.getSource());
                mTime.setText(DateUtil.formatDefaultStyleTime(item.getReleaseTime()));
                if (item.getImgs() != null && item.getImgs().size() > 0) {
                    GlideApp.with(context).load(item.getImgs().get(0))
                            .placeholder(R.drawable.ic_default_image)
                            .centerCrop()
                            .into(mImg1);
                }

                if (item.getImgs() != null && item.getImgs().size() > 1) {
                    GlideApp.with(context).load(item.getImgs().get(1))
                            .placeholder(R.drawable.ic_default_image)
                            .centerCrop()
                            .into(mImg2);
                }

                if (item.getImgs() != null && item.getImgs().size() > 2) {
                    GlideApp.with(context).load(item.getImgs().get(2))
                            .placeholder(R.drawable.ic_default_image)
                            .centerCrop()
                            .into(mImg3);
                }
                if (count - 1 == position) {
                    mLine.setVisibility(View.GONE);
                }
            }
        }
    }
}
