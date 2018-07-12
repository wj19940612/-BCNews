package com.sbai.bcnews.fragment.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.HourWelfareActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.mine.RedPacketInfo;
import com.sbai.bcnews.model.mine.UserRedPacketStatus;
import com.sbai.bcnews.model.system.RedPacketActivityStatus;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Modified by $nishuideyu$ on 2018/5/17
 * <p>
 * Description: 抢红包弹窗
 * </p>
 */
public class StartRobRedPacketDialogFragment extends BaseDialogFragment {

    @BindView(R.id.rob)
    TextView mRob;
    @BindView(R.id.distanceNexRedPacketTime)
    TextView mDistanceNexRedPacketTime;
    @BindView(R.id.closeDialog)
    ImageView mCloseDialog;
    Unbinder unbinder;

    private RedPacketActivityStatus mRedPacketActivityStatus;

    private long mWaitTime;
    private CountDownTimer mCountDownTimer;
    private boolean mStartRob;
    private boolean mHaveRedPacket;
    private boolean isDismiss = false;

    private OnDialogDismissListener mOnDialogDismissListener;

    public interface OnDialogDismissListener {
        void onDismiss();
    }

    public StartRobRedPacketDialogFragment setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        mOnDialogDismissListener = onDialogDismissListener;
        return this;
    }

    public static StartRobRedPacketDialogFragment newInstance(RedPacketActivityStatus redPacketActivityStatus) {
        Bundle args = new Bundle();
        args.putParcelable(ExtraKeys.DATA, redPacketActivityStatus);
        StartRobRedPacketDialogFragment fragment = new StartRobRedPacketDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRedPacketActivityStatus = getArguments().getParcelable(ExtraKeys.DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_start_rob_red_packet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mRedPacketActivityStatus != null) {
            updateRedPacketActivityStatus(mRedPacketActivityStatus);
        }
        requestRedPacketStatus();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        stopScheduleJob();
        resetCountDownTimer();
        isDismiss = true;
        if (mOnDialogDismissListener != null) {
            mOnDialogDismissListener.onDismiss();
        }
    }

    private void requestRedPacketStatus() {
        mHaveRedPacket = true;
        Apic.requestRedPacketStatus()
                .tag(TAG)
                .callback(new Callback2D<Resp<RedPacketActivityStatus>, RedPacketActivityStatus>() {
                    @Override
                    protected void onRespSuccessData(RedPacketActivityStatus data) {
                        updateRedPacketActivityStatus(data);
                        if (mRedPacketActivityStatus.getRobStatus() == RedPacketActivityStatus.RED_PACKET_CAN_ROB) {
                            requestUserRedPacketStatus();
                        }
                    }
                })
                .fire();

    }

    private void requestUserRedPacketStatus() {
        Apic.requestUserRedPacketStatus()
                .tag(TAG)
                .callback(new Callback2D<Resp<UserRedPacketStatus>, UserRedPacketStatus>() {
                    @Override
                    protected void onRespSuccessData(UserRedPacketStatus data) {
                        if (data.getIsRob() == UserRedPacketStatus.ROBED) {
                            Launcher.with(getContext(), HourWelfareActivity.class)
                                    .execute();
                            dismiss();
                        } else {
                            if (isDismiss) return;
                            mHaveRedPacket = data.getRedPacket() == UserRedPacketStatus.HAVE_REDPACKET;
                            updateRobText(mHaveRedPacket);
                            updateRobStatus(mRedPacketActivityStatus);
                        }
                    }

                })
                .fire();
    }

    private void updateRedPacketActivityStatus(RedPacketActivityStatus data) {
        if (isDismiss) return;
        mRedPacketActivityStatus = data;
        if (mRedPacketActivityStatus.getRedPacketStatus() == RedPacketActivityStatus.RED_PACKET_ACTIVITY_IS_OPEN) {
            updateRobText(mRedPacketActivityStatus.getRobStatus() == RedPacketActivityStatus.RED_PACKET_CAN_ROB);
            updateRobStatus(mRedPacketActivityStatus);
        }
    }

    private void updateRobStatus(RedPacketActivityStatus redPacketActivityStatus) {
        mWaitTime = getNexGetRedPacketTime(redPacketActivityStatus);
        if (redPacketActivityStatus.getRobStatus() == RedPacketActivityStatus.RED_PACKET_CAN_ROB && mHaveRedPacket) {
            mDistanceNexRedPacketTime.setVisibility(View.GONE);
            resetCountDownTimer();
            startScheduleJob(1000);
        } else if (mWaitTime > 0) {
            mDistanceNexRedPacketTime.setVisibility(View.VISIBLE);
            startCountDownTimer();
        }
    }

    private void startCountDownTimer() {
        mCountDownTimer = new CountDownTimer(mWaitTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isDismiss) return;
                String format = DateUtil.format(millisUntilFinished, DateUtil.FORMAT_MINUTE_SECOND);
                mDistanceNexRedPacketTime.setText(getString(R.string.distance_nex_red_packet_time, " " + format));
            }

            @Override
            public void onFinish() {
                requestRedPacketStatus();
            }
        }.start();
    }

    private void resetCountDownTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    private void updateRobText(boolean isRobRedPacketTime) {
        if (isRobRedPacketTime) {
            mRob.setText(R.string.rob);
            mRob.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        } else {
            mRob.setText(R.string.already_rob_empty);
            mRob.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
        mRob.setEnabled(isRobRedPacketTime);
    }

    private long getNexGetRedPacketTime(RedPacketActivityStatus data) {
        return data.getStartTime() - data.getTime();
    }

    @Override
    public void onTimeUp(int count) {
        super.onTimeUp(count);
        if (count == mWaitTime / 1000) {
            stopScheduleJob();
            if (!mStartRob)
                requestRedPacketStatus();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopScheduleJob();
        resetCountDownTimer();
        unbinder.unbind();
    }

    @OnClick({R.id.rob, R.id.closeDialog})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rob:
                startRobRedPacket();
                break;
            case R.id.closeDialog:
                dismiss();
                break;
        }
    }

    private void startRobRedPacket() {
        if (!LocalUser.getUser().isLogin()) {
            Launcher.with(getContext(), LoginActivity.class).execute();
            return;
        }
        mStartRob = true;
        Apic.robRedPacket()
                .tag(TAG)
                .callback(new Callback2D<Resp<RedPacketInfo>, RedPacketInfo>() {
                    @Override
                    protected void onRespSuccessData(RedPacketInfo data) {
                        Launcher.with(getContext(), HourWelfareActivity.class)
                                .putExtra(ExtraKeys.DATA, data)
                                .execute();
                        dismiss();
                    }
                })
                .fire();

    }
}
