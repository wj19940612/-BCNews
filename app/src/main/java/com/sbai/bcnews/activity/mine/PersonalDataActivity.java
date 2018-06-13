package com.sbai.bcnews.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.fragment.dialog.UploadUserImageDialogFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.view.IconTextRow;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.glide.GlideApp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;

public class PersonalDataActivity extends BaseActivity {

    private static final String SEX_BOY = "男";
    private static final String SEX_GIRL = "女";

    public static final int REQ_CODE_PERSONAL_INTRODUCE = 872;
    public static final int REQ_CODE_NICK_NAME = 873;

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.userHeadImage)
    AppCompatImageView mUserHeadImage;
    @BindView(R.id.helpArrow)
    AppCompatImageView mHelpArrow;
    @BindView(R.id.headImageLayout)
    RelativeLayout mHeadImageLayout;
    @BindView(R.id.nickName)
    IconTextRow mNickName;
    @BindView(R.id.sex)
    IconTextRow mSex;
    @BindView(R.id.birthday)
    IconTextRow mBirthday;

    @BindView(R.id.personalIntroduce)
    LinearLayout mPersonalIntroduce;
    @BindView(R.id.introduce)
    TextView mIntroduce;
    @BindView(R.id.location)
    TextView mLocation;
    @BindView(R.id.locationLL)
    LinearLayout mLocationLL;
    @BindView(R.id.locationHint)
    TextView mLocationHint;
    @BindView(R.id.connectServiceHint)
    TextView mConnectServiceHint;

    private UserInfo mUserInfo;
    private OptionPicker mSexPicker;


    private DatePicker mBirthdayDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        ButterKnife.bind(this);

        mUserInfo = LocalUser.getUser().getUserInfo();
        if (mUserInfo != null)
            updateUserInfo(mUserInfo);
        requestUserInfo();
        requestService();
    }

    private void requestService() {
        // TODO: 2018/6/7 请求客服的邮箱和联系方式
    }

    @Override
    public void onBackPressed() {
        submitUserInfo();
        super.onBackPressed();
    }

    private void submitUserInfo() {

        String birthdy = null;
        Integer sex = null;
        birthdy = mUserInfo.getBirthday();
        if (mSex.getSubText().equalsIgnoreCase(SEX_BOY) || mSex.getSubText().equalsIgnoreCase(SEX_GIRL)) {
            sex = mSex.getSubText().equalsIgnoreCase(SEX_BOY) ? UserInfo.USER_SEX_BOY : UserInfo.USER_SEX_GIRL;
        }
        if (TextUtils.isEmpty(birthdy)
                && sex == null
                && TextUtils.isEmpty(mUserInfo.getUserProvince())
                && TextUtils.isEmpty(mUserInfo.getUserCity())) {
            return;
        }

        Apic.updateUserInfo(mUserInfo.getUserProvince(), mUserInfo.getUserCity(), birthdy, sex)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                    }
                })
                .fireFreely();
    }

    private void requestUserInfo() {
        Apic.requestUserInfo()
                .tag(TAG)
                .callback(new Callback2D<Resp<UserInfo>, UserInfo>() {
                    @Override
                    protected void onRespSuccessData(UserInfo data) {
                        mUserInfo = data;
                        LocalUser.getUser().setUserInfo(data);
                        updateUserInfo(data);
                    }
                })
                .fire();
    }

    private void updateUserInfo(UserInfo data) {
        updateUserPortrait(data.getUserPortrait());
        mNickName.setSubText(data.getUserName());

        int userSex = data.getUserSex();
        if (userSex != 0) {
            boolean isBoy = userSex == UserInfo.USER_SEX_BOY;
            if (mUserInfo != null && mUserInfo.getUserSex() != 0) {
                mSex.setSubText(isBoy ? SEX_BOY : SEX_GIRL);
            }
        }

        mIntroduce.setText(data.getIntroduction());
        if (!TextUtils.isEmpty(data.getBirthday())) {
            mBirthday.setSubText(data.getBirthday());
        }
        if (!TextUtils.isEmpty(data.getUserProvince()) && !TextUtils.isEmpty(data.getUserCity())) {
            mLocation.setText(data.getUserProvince() + " " + data.getUserCity());
        }
        updateUserInfoModifyEnable(!data.isAuthorIsCheck());
    }

    private void updateUserInfoModifyEnable(boolean viewEnable) {
        mHeadImageLayout.setEnabled(viewEnable);
        mNickName.setEnabled(viewEnable);
        mSex.setEnabled(viewEnable);
        mBirthday.setEnabled(viewEnable);
        mLocationLL.setEnabled(viewEnable);
        mPersonalIntroduce.setEnabled(viewEnable);
        if (!viewEnable) {
            mConnectServiceHint.setVisibility(View.VISIBLE);
        }
    }

    private void updateUserPortrait(String portrait) {
        GlideApp.with(getActivity())
                .load(portrait)
                .placeholder(R.drawable.ic_default_head_portrait)
                .circleCrop()
                .into(mUserHeadImage);
    }

    @OnClick({R.id.headImageLayout, R.id.nickName, R.id.sex, R.id.birthday, R.id.locationLL, R.id.personalIntroduce})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.headImageLayout:
                UploadUserImageDialogFragment uploadUserImageDialogFragment = UploadUserImageDialogFragment.newInstance(
                        UploadUserImageDialogFragment.IMAGE_TYPE_CLIPPING_IMAGE_SCALE_OR_MOVE);
                uploadUserImageDialogFragment.show(getSupportFragmentManager());
                break;
            case R.id.nickName:
                Launcher.with(getActivity(), ModifyNickNameActivity.class)
                        .putExtra(ExtraKeys.NICK_NAME, LocalUser.getUser().getUserInfo().getUserName())
                        .executeForResult(REQ_CODE_NICK_NAME);
                break;
            case R.id.sex:
                showSexWheel();
                break;
            case R.id.birthday:
                showTimeSelectWheel();
                break;
            case R.id.locationLL:
                showLocationWheel();
                break;
            case R.id.personalIntroduce:
                String personalIntroduce = "";
                if (mUserInfo != null) {
                    personalIntroduce = mUserInfo.getIntroduction();
                }
                Launcher.with(getActivity(), PersonalIntroduceActivity.class)
                        .putExtra(ExtraKeys.PERSONAL_INTRODUCE, personalIntroduce)
                        .executeForResult(REQ_CODE_PERSONAL_INTRODUCE);
                break;
        }
    }

    private void showLocationWheel() {
        String province = "浙江";
        String city = "杭州";
        if (mUserInfo != null) {
            province = !TextUtils.isEmpty(mUserInfo.getUserProvince()) ? mUserInfo.getUserProvince() : "浙江";
            city = !TextUtils.isEmpty(mUserInfo.getUserCity()) ? mUserInfo.getUserCity() : "杭州";
        }
        AddressAsyncTask addressAsyncTask = new AddressAsyncTask(getActivity(), true);
        addressAsyncTask.execute(province, city);
    }

    private void showTimeSelectWheel() {
        mBirthdayDatePicker = new DatePicker(getActivity(), DateTimePicker.YEAR_MONTH_DAY);
        mBirthdayDatePicker.setCancelTextColor(ContextCompat.getColor(getActivity(), R.color.text_4949));
        mBirthdayDatePicker.setSubmitTextColor(ContextCompat.getColor(getActivity(), R.color.textColorPrimary));
        mBirthdayDatePicker.setAnimationStyle(R.style.BottomDialogAnimation);
        mBirthdayDatePicker.setLineConfig(new WheelView.LineConfig(0));//使用最长的线
        mBirthdayDatePicker.setRangeStart(Calendar.getInstance().get(Calendar.YEAR) - 100, 1, 1);
        mBirthdayDatePicker.setRangeEnd(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        mBirthdayDatePicker.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_222),
                ContextCompat.getColor(getActivity(), R.color.text_999));

        if (mUserInfo != null) {
            String birthday = mUserInfo.getBirthday();
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            Log.d(TAG, "showTimeSelectWheel: " + year + " " + month + " " + day);
            if (!TextUtils.isEmpty(birthday)) {
                String[] split = birthday.split("-");
                if (split.length == 3) {
                    year = Integer.valueOf(split[0]);
                    month = Integer.valueOf(split[1]);
                    day = Integer.valueOf(split[2]);
                }
            }
            mBirthdayDatePicker.setSelectedItem(year, month, day);
        }

        mBirthdayDatePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                mUserInfo.setBirthday(year + "-" + month + "-" + day);
                mBirthday.setSubText(mUserInfo.getBirthday());
            }
        });

        mBirthdayDatePicker.show();
    }

    private void showSexWheel() {
        mSexPicker = new OptionPicker(this, new String[]{SEX_BOY, SEX_GIRL,});
        mSexPicker.setCancelTextColor(ContextCompat.getColor(getActivity(), R.color.text_4949));
        mSexPicker.setSubmitTextColor(ContextCompat.getColor(getActivity(), R.color.textColorPrimary));
        mSexPicker.setAnimationStyle(R.style.BottomDialogAnimation);
        mSexPicker.setOffset(1);
        if (mUserInfo != null && mUserInfo.getUserSex() != 0) {
            mSexPicker.setSelectedItem(mUserInfo.getUserSex() == UserInfo.USER_SEX_BOY ? SEX_BOY : SEX_GIRL);
        }
//        picker.setTopPadding(toDp(10));
//                picker.setTextSize(11);
        mSexPicker.setLineConfig(new WheelView.LineConfig(0));//使用最长的线
        mSexPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                if (!TextUtils.isEmpty(item)) {
                    mSex.setSubText(item);
//                    LocalUser.getUser().getUserInfo().setChinaSex(item);
//                    updateUserHeadImage(LocalUser.getUser().getUserInfo());
                }
            }
        });
        mSexPicker.show();
    }

    private class AddressAsyncTask extends AsyncTask<String, Void, ArrayList<Province>> {
        private static final String TAG = "AddressInitTask";

        private Activity mActivity;
        private String mSelectedProvince = "",
                mSelectedCity = "",
                mSelectedCounty = "";
        private boolean mHideCounty;

        public AddressAsyncTask(Activity activity) {
            this.mActivity = activity;
        }

        /**
         * 初始化为不显示区县的模式
         */
        public AddressAsyncTask(Activity activity, boolean hideCounty) {
            this.mActivity = activity;
            this.mHideCounty = hideCounty;
        }


        @Override
        protected ArrayList<Province> doInBackground(String... params) {
            if (params != null) {
                switch (params.length) {
                    case 1:
                        mSelectedProvince = params[0];
                        break;
                    case 2:
                        mSelectedProvince = params[0];
                        mSelectedCity = params[1];
                        break;
                    case 3:
                        mSelectedProvince = params[0];
                        mSelectedCity = params[1];
                        mSelectedCounty = params[2];
                        break;
                    default:
                        break;
                }
            }
            ArrayList<Province> data = new ArrayList<Province>();
            try {
                String json = ConvertUtils.toString(mActivity.getAssets().open("city.json"));

                Type listType = new TypeToken<ArrayList<Province>>() {
                }.getType();

                ArrayList<Province> provinces = new Gson().fromJson(json, listType);
                data.addAll(provinces);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Province> result) {
            if (result.size() > 0) {
                AddressPicker picker = new AddressPicker(mActivity, result);
                picker.setHideCounty(mHideCounty);
                if (mHideCounty) {
                    picker.setColumnWeight(1 / 3.0, 2 / 3.0);//将屏幕分为3份，省级和地级的比例为1:2
                } else {
                    picker.setColumnWeight(2 / 8.0, 3 / 8.0, 3 / 8.0);//省级、地级和县级的比例为2:3:3
                }
                picker.setCancelTextColor(ContextCompat.getColor(mActivity, R.color.text_999));
                picker.setSubmitTextColor(ContextCompat.getColor(mActivity, R.color.textColorPrimary));
                picker.setTopBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_F5F5));
                picker.setPressedTextColor(ContextCompat.getColor(mActivity, R.color.text_999));
                picker.setAnimationStyle(R.style.BottomDialogAnimation);
                picker.setSelectedItem(mSelectedProvince, mSelectedCity, mSelectedCounty);
                picker.setTextColor(ContextCompat.getColor(mActivity, R.color.text_222), ContextCompat.getColor(mActivity, R.color.text_999));
                WheelView.LineConfig lineConfig = new WheelView.LineConfig(0);//使用最长的分割线
                lineConfig.setColor(ContextCompat.getColor(mActivity, R.color.bg_F5F5));
                picker.setLineConfig(lineConfig);
                picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        UserInfo userInfo = LocalUser.getUser().getUserInfo();
                        userInfo.setUserProvince(province.getAreaName());
                        userInfo.setUserCity(city.getAreaName());
                        mLocation.setText(province.getAreaName() + " " + city.getAreaName());
                    }
                });
                picker.show();
            } else {
                Toast.makeText(mActivity, "数据初始化失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_PERSONAL_INTRODUCE:
                    mIntroduce.setText(LocalUser.getUser().getUserInfo().getIntroduction());
                    break;
                case REQ_CODE_NICK_NAME:
                    mNickName.setSubText(LocalUser.getUser().getUserInfo().getUserName());
                    break;
                case UploadUserImageDialogFragment.REQ_CLIP_HEAD_IMAGE_PAGE:
                    updateUserPortrait(LocalUser.getUser().getUserInfo().getUserPortrait());
                    setResult(RESULT_OK);
                    break;
            }
        }
    }
}
