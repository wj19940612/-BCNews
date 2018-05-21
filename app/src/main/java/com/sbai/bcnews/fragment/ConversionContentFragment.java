package com.sbai.bcnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.sbai.bcnews.model.ConversionContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ConversionContentFragment extends BaseFragment {

    public static final String PAGE_TYPE = "page_type";
    public static final int SPAN_COUNT = 2;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Unbinder mBind;

    private int mPageType;

    private List<ConversionContent> mContentList;
    private ContentAdapter mContentAdapter;

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
        mContentAdapter = new ContentAdapter(mContentList, getActivity(), new ContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ConversionContent conversionContent) {

            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        mRecyclerView.setAdapter(mContentAdapter);
    }

    private void loadData() {
        List<ConversionContent> conversionContents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ConversionContent conversionContent = new ConversionContent("100 EOS", "兑换: 1000QKC", 999990);
            conversionContents.add(conversionContent);
        }
        updateData(conversionContents);
    }

    private void updateData(List<ConversionContent> conversionContents) {
        mContentList.clear();
        mContentList.addAll(conversionContents);
        mContentAdapter.notifyDataSetChanged();
    }

    public static class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public interface OnItemClickListener {
            public void onItemClick(ConversionContent conversionContent);
        }

        private Context mContext;
        private List<ConversionContent> mContentList;
        private OnItemClickListener mOnItemClickListener;

        public ContentAdapter(List contentList, Context context, OnItemClickListener onItemClickListener) {
            mContentList = contentList;
            mContext = context;
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_conversion, parent, false);
            return new ContentHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ContentHolder) holder).bindingData(mContext, mContentList.get(position), mOnItemClickListener);
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

            ContentHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindingData(Context context, final ConversionContent conversionContent, final OnItemClickListener onItemClickListener) {
                mIntroduce.setText(conversionContent.getIntroduce());
                mContent.setText(conversionContent.getContent());
                mRemainingQty.setText(String.valueOf(conversionContent.getCount()));

                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSelectBtn.setSelected(!mSelectBtn.isSelected());
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(conversionContent);
                        }
                    }
                });
            }
        }
    }
}
