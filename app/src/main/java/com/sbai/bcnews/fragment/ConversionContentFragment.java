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
import com.sbai.bcnews.model.mine.MyIntegral;
import com.sbai.bcnews.utils.FinanceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sbai.bcnews.fragment.ConversionGoodsActivity.PAGE_ALIPAY;
import static com.sbai.bcnews.fragment.ConversionGoodsActivity.PAGE_DIGITAL_COIN;
import static com.sbai.bcnews.fragment.ConversionGoodsActivity.PAGE_TELEPHONE_CHARGE;

public class ConversionContentFragment extends BaseFragment {

    public static final String PAGE_TYPE = "page_type";
    public static final int SPAN_COUNT = 2;
    public static final int DEFAULT_SELECT = -1;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyView)
    TextView mEmptyView;

    private Unbinder mBind;

    private int mPageType;

    private List<ConversionContent> mContentList;
    private List<ConversionContent> mNetList;
    private ContentAdapter mContentAdapter;
    private MyIntegral mHashRateIntegral;
    private SelectListener mSelectListener;

    public static ConversionContentFragment newsInstance(int pageType) {
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_TYPE, pageType);
        ConversionContentFragment conversionContentFragment = new ConversionContentFragment();
        conversionContentFragment.setArguments(bundle);
        return conversionContentFragment;
    }

    public interface SelectListener {
        public void onSelect(int page, int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SelectListener){
            mSelectListener = (SelectListener) context;
        }
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
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded()) {
            loadData();
        }
    }

    private void initView() {
        mContentList = new ArrayList<>();
        mContentAdapter = new ContentAdapter(-1, mPageType, mContentList, getActivity(), new ContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int clickPosition) {
                mContentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSelect(int position) {
                mContentAdapter.setClickPosition(position);
                if (mSelectListener != null) {
                    mSelectListener.onSelect(mPageType, position);
                }
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        mRecyclerView.setAdapter(mContentAdapter);
    }

    private void loadData() {
        Apic.requestMyIntegral().tag(TAG).callback(new Callback2D<Resp<MyIntegral>, MyIntegral>() {
            @Override
            protected void onRespSuccessData(MyIntegral data) {
                mHashRateIntegral = data;
                if (mNetList != null) {
                    updateData(mNetList, mHashRateIntegral);
                }
            }
        }).fireFreely();

        Apic.requestConversionGoods(mPageType).tag(TAG).callback(new Callback2D<Resp<List<ConversionContent>>, List<ConversionContent>>() {
            @Override
            protected void onRespSuccessData(List<ConversionContent> data) {
                mNetList = data;
                if (mHashRateIntegral != null) {
                    updateData(mNetList, mHashRateIntegral);
                }
            }
        }).fireFreely();
    }

    private void updateData(List<ConversionContent> conversionContents, MyIntegral hashRateIntegral) {
        if (conversionContents != null && conversionContents.size() > 0) {
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mNetList = null;
        mHashRateIntegral = null;
        mContentList.clear();
        mContentList.addAll(conversionContents);
        mContentAdapter.setHashRateIntegral(hashRateIntegral);
        mContentAdapter.notifyDataSetChanged();
    }

    public ConversionContent getSelectConversionGood() {
        if (mContentAdapter.getClickPosition() != -1) {
            return mContentList.get(mContentAdapter.getClickPosition());
        }
        return null;
    }

    public static class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public interface OnItemClickListener {
            public void onItemClick(int position);

            public void onSelect(int position);
        }

        private Context mContext;
        private List<ConversionContent> mContentList;
        private OnItemClickListener mOnItemClickListener;
        private MyIntegral mHashRateIntegral;
        private int mPageType;
        private int mClickPosition;

        public ContentAdapter(int clickPosition, int pageType, List contentList, Context context, OnItemClickListener onItemClickListener) {
            mContentList = contentList;
            mContext = context;
            mOnItemClickListener = onItemClickListener;
            mPageType = pageType;
            mClickPosition = clickPosition;
        }

        public void setHashRateIntegral(MyIntegral hashRateIntegral) {
            mHashRateIntegral = hashRateIntegral;
        }

        public void setClickPosition(int position) {
            mClickPosition = position;
        }

        public int getClickPosition() {
            return mClickPosition;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_conversion, parent, false);
            return new ContentHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ContentHolder) holder).bindingData(mContext, mContentList.get(position), mOnItemClickListener, mPageType, mHashRateIntegral, position, mClickPosition);
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

            public void bindingData(Context context, final ConversionContent conversionContent, final OnItemClickListener onItemClickListener, int pageType, MyIntegral hashRateIntegral, final int position, final int clickPosition) {
                Drawable contentDrawable = getContentDrawable(context, pageType);
                Drawable labelDrawable = getLabelDrawable(context, pageType);
                mLabel.setBackgroundDrawable(labelDrawable);
                mContent.setBackgroundDrawable(contentDrawable);
                if (hashRateIntegral.getIntegral() < conversionContent.getPrice()) {
                    mRootView.setEnabled(false);
                    mLabel.setEnabled(false);
                    mContent.setEnabled(false);
                    mSelectBtn.setVisibility(View.GONE);
                } else {
                    mSelectBtn.setVisibility(View.VISIBLE);
                    mRootView.setEnabled(true);
                    mLabel.setEnabled(true);
                    mContent.setEnabled(true);
                    if (position == clickPosition || (clickPosition == DEFAULT_SELECT && position == 0)) {
                        mSelectBtn.setSelected(true);
                        if (onItemClickListener != null) {
                            onItemClickListener.onSelect(position);
                        }
                    } else {
                        mSelectBtn.setSelected(false);
                    }
                }

                mContent.setText(conversionContent.getName());
                mIntroduce.setText(context.getString(R.string.conversion_price_x, FinanceUtil.trimTrailingZero(conversionContent.getPrice())));
                mRemainingQty.setText(context.getString(R.string.conversion_margin_x, conversionContent.getMargin()));

                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int callBackPosition = DEFAULT_SELECT;
                        if (position != clickPosition) {
                            callBackPosition = position;
                        }
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(callBackPosition);
                            onItemClickListener.onSelect(callBackPosition);
                        }
                    }
                });
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
                        return ContextCompat.getDrawable(context, R.drawable.bg_conversion_ali);
                    case PAGE_TELEPHONE_CHARGE:
                        return ContextCompat.getDrawable(context, R.drawable.bg_conversion_telephone);
                    default:
                        return ContextCompat.getDrawable(context, R.drawable.bg_conversion_digital);

                }
            }
        }
    }
}
