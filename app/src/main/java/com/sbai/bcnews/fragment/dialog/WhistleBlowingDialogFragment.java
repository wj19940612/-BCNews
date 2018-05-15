package com.sbai.bcnews.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.fragment.BottomDialogFragment;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Modified by $nishuideyu$ on 2018/4/26
 * <p>
 * Description:
 * </p>
 */
public class WhistleBlowingDialogFragment extends BottomDialogFragment {

    public static final int WHISTLE_BLOWING_TYPE_ARTICLE = 0; //文章
    public static final int WHISTLE_BLOWING_TYPE_COMMENT = 1; //评论

    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.cancel)
    TextView mCancel;

    private WhistleBlowingReasonAdapter mWhistleBlowingReasonAdapter;

    @OnClick(R.id.cancel)
    public void onViewClicked() {
        dismiss();
    }


    public interface OnWhistleBlowingReasonListener {
        void onChooseWhistleBlowingReason(String reason, int type, String id);
    }

    Unbinder unbinder;

    public OnWhistleBlowingReasonListener mOnWhistleBlowingReasonListener;

    private int mWhistleBlowingType;
    private String mDataId;
    private LinkedHashMap<String, String> mWhistleBlowingReason;


    public WhistleBlowingDialogFragment setOnWhistleBlowingReasonListener(OnWhistleBlowingReasonListener onWhistleBlowingReasonListener) {
        mOnWhistleBlowingReasonListener = onWhistleBlowingReasonListener;
        return this;
    }

    public WhistleBlowingDialogFragment() {

    }


    public static WhistleBlowingDialogFragment newInstance(int type, String id, LinkedHashMap<String, String> hashMap) {
        Bundle args = new Bundle();
        args.putInt(ExtraKeys.TAG, type);
        args.putString(ExtraKeys.ID, id);
        args.putSerializable(ExtraKeys.DATA, hashMap);
        WhistleBlowingDialogFragment fragment = new WhistleBlowingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWhistleBlowingType = getArguments().getInt(ExtraKeys.TAG);
            mDataId = getArguments().getString(ExtraKeys.ID);
            mWhistleBlowingReason = (LinkedHashMap<String, String>) getArguments().getSerializable(ExtraKeys.DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_whistle_blowing, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWhistleBlowingReasonListener) {
            mOnWhistleBlowingReasonListener = (OnWhistleBlowingReasonListener) context;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //前面写死数据，后期请求，直接在窗口里面请求了。有时间可以把请求提出去


        mWhistleBlowingReasonAdapter = new WhistleBlowingReasonAdapter(getActivity());


        mListView.setAdapter(mWhistleBlowingReasonAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WhistleBlowingReason whistleBlowingReason = (WhistleBlowingReason) parent.getAdapter().getItem(position);
                if (mOnWhistleBlowingReasonListener != null && whistleBlowingReason != null) {
                    mOnWhistleBlowingReasonListener.onChooseWhistleBlowingReason(whistleBlowingReason.getKey(), mWhistleBlowingType, mDataId);
                    dismiss();
                }
            }
        });

        for (Map.Entry<String, String> entry : mWhistleBlowingReason.entrySet()) {
            WhistleBlowingDialogFragment.WhistleBlowingReason whistleBlowingReason = new WhistleBlowingDialogFragment.WhistleBlowingReason();
            whistleBlowingReason.setKey(entry.getKey());
            whistleBlowingReason.setValues(entry.getValue());
            mWhistleBlowingReasonAdapter.add(whistleBlowingReason);
        }

//        requestWhistleBlowingList(mWhistleBlowingType);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    static class WhistleBlowingReasonAdapter extends ArrayAdapter<WhistleBlowingReason> {
        private Context mContext;

        public WhistleBlowingReasonAdapter(@NonNull Context context) {
            super(context, 0);
            mContext = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_whistle_blowing_reason, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            WhistleBlowingReason item = getItem(position);
            if (item != null)
                viewHolder.mContent.setText(item.getValues());
            return convertView;

        }

        static class ViewHolder {
            @BindView(R.id.content)
            TextView mContent;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    private class WhistleBlowingReason {

        private String key;
        private String values;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValues() {
            return values;
        }

        public void setValues(String values) {
            this.values = values;
        }
    }

}
