package com.sbai.bcnews.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.ConversionContent;
import com.sbai.bcnews.model.HashRateIntegral;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sbai.bcnews.fragment.ConversionGoodsFragment.PAGE_ALIPAY;
import static com.sbai.bcnews.fragment.ConversionGoodsFragment.PAGE_DIGITAL_COIN;
import static com.sbai.bcnews.fragment.ConversionGoodsFragment.PAGE_TELEPHONE_CHARGE;

public class ConversionContentFragment extends BaseFragment {

    public static final String PAGE_TYPE = "page_type";
    public static final int SPAN_COUNT = 2;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Unbinder mBind;

    private int mPageType;

    private List<ConversionContent> mContentList;
    private List<ConversionContent> mNetList;
    private ContentAdapter mContentAdapter;
    private HashRateIntegral mHashRateIntegral;

    public static ConversionContentFragment newsInstance(int pageType) {
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_TYPE, pageType);
        ConversionContentFragment conversionContentFragment = new ConversionContentFragment();
        conversionContentFragment.setArguments(bundle);
        return conversionContentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageType = getArguments().getInt(PAGE_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversion_content, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadData();
    }

    private void initView() {
        mContentList = new ArrayList<>();
        mContentAdapter = new ContentAdapter(-1,mPageType, mContentList, getActivity(), new ContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ConversionContent conversionContent) {

            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        mRecyclerView.setAdapter(mContentAdapter);
    }

    private void loadData() {
        Apic.requestMyIntegral().tag(TAG).callback(new Callback2D<Resp<HashRateIntegral>, HashRateIntegral>() {
            @Override
            protected void onRespSuccessData(HashRateIntegral data) {
                mHashRateIntegral = data;
                if (mNetList != null) {
                    updateData(mNetList);
                }
            }
        }).fireFreely();

        Apic.requestConversionGoods(mPageType).tag(TAG).callback(new Callback2D<Resp<List<ConversionContent>>, List<ConversionContent>>() {
            @Override
            protected void onRespSuccessData(List<ConversionContent> data) {
                mNetList = data;
                if (mHashRateIntegral != null) {
                    updateData(mNetList);
                }
            }
        }).fireFreely();
    }

    private void updateData(List<ConversionContent> conversionContents) {
        mContentList.clear();
        mContentList.addAll(conversionContents);
        mContentAdapter.setHashRateIntegral(mHashRateIntegral);
        mContentAdapter.notifyDataSetChanged();
    }

    public static class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public interface OnItemClickListener {
            public void onItemClick(ConversionContent conversionContent);
        }

        private Context mContext;
        private List<ConversionContent> mContentList;
        private OnItemClickListener mOnItemClickListener;
        private HashRateIntegral mHashRateIntegral;
        private int mPageType;
        private int mClickPosition;

        public ContentAdapter(int clickPosition, int pageType, List contentList, Context context, OnItemClickListener onItemClickListener) {
            mContentList = contentList;
            mContext = context;
            mOnItemClickListener = onItemClickListener;
            mPageType = pageType;
            mClickPosition = clickPosition;
        }

        public void setHashRateIntegral(HashRateIntegral hashRateIntegral) {
            mHashRateIntegral = hashRateIntegral;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_conversion, parent, false);
            return new ContentHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ContentHolder) holder).bindingData(mContext, mContentList.get(position), mOnItemClickListener, mPageType, mHashRateIntegral,position,mClickPosition);
        }

        @Override
        public int getItemCount() {
            return mContentList.size();
        }

        static class ContentHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.rootView)
            RelativeLayout mRootView;
            @BindView(R.id.contentLayout)
            LinearLayout mContentLayout;
            @BindView(R.id.content)
            TextView mContent;
            @BindView(R.id.introduce)
            TextView mIntroduce;
            @BindView(R.id.remainingQty)
            TextView mRemainingQty;
            @BindView(R.id.selectBtn)
            ImageView mSelectBtn;
            @BindView(R.id.label)
            ImageView mLabel;

            ContentHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindingData(Context context, final ConversionContent conversionContent, final OnItemClickListener onItemClickListener, int pageType, HashRateIntegral hashRateIntegral,int position,int clickPosition) {
                Drawable contentDrawable = getContentDrawable(context, pageType);
                Drawable labelDrawable = getLabelDrawable(context, pageType);

                if (hashRateIntegral.getIntegral() < conversionContent.getPrice()) {
                    mRootView.setEnabled(false);
                    mLabel.setEnabled(false);
                    mContent.setEnabled(false);
                    mSelectBtn.setEnabled(false);
                } else {

                }
//                Drawable drawable1 = getLabelDrawable(pageType);
//
//                if (hashRateIntegral.getRate() >= conversionContent.getPrice()) {
//
//                }
//                mIntroduce.setText(conversionContent.getName());
//                mContent.setText(conversionContent.getContent());
//                mRemainingQty.setText(String.valueOf(conversionContent.getCount()));
//
//                mRootView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mSelectBtn.setSelected(!mSelectBtn.isSelected());
//                        if (onItemClickListener != null) {
//                            onItemClickListener.onItemClick(conversionContent);
//                        }
//                    }
//                });
            }

            private Drawable getContentDrawable(Context context, int pageType) {
                switch (pageType) {
                    case PAGE_DIGITAL_COIN:
                        return ContextCompat.getDrawable(context, R.drawable.bg_conversion_content);
                    case PAGE_ALIPAY:
                        return ContextCompat.getDrawable(context, R.drawable.bg_conversion_ali_content);
                    case PAGE_TELEPHONE_CHARGE:
                        return ContextCompat.getDrawable(context, R.drawable.bg_conversion_telephone_content);
                    default:
                        return ContextCompat.getDrawable(context, R.drawable.bg_conversion_content);

                }
            }

            private Drawable getLabelDrawable(Context context, int pageType) {
                switch (pageType) {
                    case PAGE_DIGITAL_COIN:
                        return ContextCompat.getDrawable(context, R.drawable.bg_conversion_digital);
                    case PAGE_ALIPAY:
                        return ContextCompat.getDrawable(context, R.drawable.bg_conversion_digital);
                    case PAGE_TELEPHONE_CHARGE:
                        return ContextCompat.getDrawable(context, R.drawable.bg_conversion_digital);
                    default:
                        return ContextCompat.getDrawable(context, R.drawable.bg_conversion_digital);

                }
            }
        }
    }
}
