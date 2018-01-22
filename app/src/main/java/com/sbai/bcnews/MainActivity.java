package com.sbai.bcnews;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void checkVersion() {
//        Client.updateVersion()
//                .setTag(TAG)
//                .setCallback(new Callback2D<Resp<AppVersion>, AppVersion>() {
//                    @Override
//                    protected void onRespSuccessData(AppVersion data) {
//                        if (data.isForceUpdate() || data.isNeedUpdate()) {
//                            UpdateVersionDialogFragment.newInstance(data, data.isForceUpdate())
//                                    .setOnDismissListener(new UpdateVersionDialogFragment.OnDismissListener() {
//                                        @Override
//                                        public void onDismiss() {
//                                            requestStartActivities();
//                                        }
//                                    })
//                                    .show(getSupportFragmentManager());
//                        } else {
//                            requestStartActivities();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(ApiError apiError) {
//                        super.onFailure(apiError);
//                        requestStartActivities();
//                    }
//                })
//                .fireFree();
    }
}
