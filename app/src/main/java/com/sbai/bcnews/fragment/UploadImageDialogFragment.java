package com.sbai.bcnews.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.ClipImageActivity;
import com.sbai.bcnews.utils.FileUtils;
import com.sbai.bcnews.utils.PermissionUtil;
import com.sbai.bcnews.utils.ToastUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 选择图片方式
 */

public class UploadImageDialogFragment extends BottomDialogFragment {
    private static final String TAG = "UploadUserImageDialogFr";


    private static final String KEY_IMAGE_URL = "KEY_IMAGE_URL";
    private static final String KEY_TYPE = "type";
    private static final String KEY_IMAGE_URL_INDEX = "key_image_url_index";

    //不做任何处理，只返回图片地址
    public static final int IMAGE_TYPE_NOT_DEAL = 1750;
    //上传头像类似裁剪 图片可移动，缩放
    public static final int IMAGE_TYPE_CLIPPING_IMAGE_SCALE_OR_MOVE = 3750;
    // 实名认证 型裁剪  拍照后，获取固定区域的图片
    public static final int IMAGE_TYPE_CLIPPING_IMMOBILIZATION_AREA = 2750;
    //打开自定义画廊
    public static final int IMAGE_TYPE_OPEN_CUSTOM_GALLERY = 4750;


    /**
     * 打开相机的请求码
     */
    private static final int REQ_CODE_TAKE_PHONE_FROM_CAMERA = 379;
    /**
     * 打开图册的请求码
     */
    private static final int REQ_CODE_TAKE_PHONE_FROM_PHONES = 600;
    /**
     * 打开自定义裁剪页面的请求码
     */
    public static final int REQ_CLIP_HEAD_IMAGE_PAGE = 144;
    //打开区域拍照页面的请求吗
    private static final int REQ_CODE_AREA_TAKE_PHONE = 46605;
    //打开自定义画廊页面请求码
    private static final int REQ_CODE_TAKE_PHONE_FROM_GALLERY = 46606;

    @BindView(R.id.takePhoneFromCamera)
    TextView mTakePhoneFromCamera;
    @BindView(R.id.takePhoneFromGallery)
    TextView mTakePhoneFromGallery;
    @BindView(R.id.takePhoneCancel)
    TextView mTakePhoneCancel;

    private Unbinder mBind;
    private File mFile;

    private OnImagePathListener mOnImagePathListener;

    private String HDPictureUrl;
    private int mImageDealType;
    private int mImageUrlIndex;

    public interface OnImagePathListener {
        void onImagePath(int index, String imagePath);
    }

    public UploadImageDialogFragment() {

    }

    public static UploadImageDialogFragment newInstance(int type) {
        return newInstance(type, "");
    }

    public static UploadImageDialogFragment newInstance(int type, String url) {
        return newInstance(type, url, -1);
    }

    public static UploadImageDialogFragment newInstance(int type, int imageIndex) {
        return newInstance(type, "", imageIndex);
    }

    public static UploadImageDialogFragment newInstance(int type, String url, int imageIndex) {
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        args.putString(KEY_IMAGE_URL, url);
        args.putInt(KEY_IMAGE_URL_INDEX, imageIndex);
        UploadImageDialogFragment fragment = new UploadImageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UploadImageDialogFragment setOnImagePathListener(OnImagePathListener onImagePathListener) {
        mOnImagePathListener = onImagePathListener;
        return this;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImagePathListener) {
            mOnImagePathListener = (OnImagePathListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageDealType = getArguments().getInt(KEY_TYPE, IMAGE_TYPE_NOT_DEAL);
            HDPictureUrl = getArguments().getString(KEY_IMAGE_URL);
            mImageUrlIndex = getArguments().getInt(KEY_IMAGE_URL_INDEX, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_upload_image, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @OnClick({R.id.takePhoneFromCamera, R.id.takePhoneFromGallery, R.id.takePhoneCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.takePhoneFromCamera:
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && PermissionUtil.cameraIsCanUse()) {
                    openSystemCameraPage();
                } else {
                    ToastUtil.show(getString(R.string.please_open_camera_permission));
                }

                break;
            case R.id.takePhoneFromGallery:
                if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
                    openGalleryPage();
                } else {
                    ToastUtil.show(R.string.sd_is_not_useful);
                }
                break;
            case R.id.takePhoneCancel:
                this.dismissAllowingStateLoss();
                break;
        }
    }

    private void openGalleryPage() {
        if (mImageDealType == IMAGE_TYPE_OPEN_CUSTOM_GALLERY) {
            Intent openGalleryIntent = new Intent(getContext(), ImageSelectActivity.class);
            openGalleryIntent.putExtra(ExtraKeys.IMAGE, 0);
            startActivityForResult(openGalleryIntent, REQ_CODE_TAKE_PHONE_FROM_GALLERY);
        } else {
            Intent openAlbumIntent = new Intent(
                    Intent.ACTION_PICK);
            openAlbumIntent.setType("image/*");
            if (openAlbumIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(openAlbumIntent, REQ_CODE_TAKE_PHONE_FROM_PHONES);
            }
        }
    }

    private void openSystemCameraPage() {
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        mFile = FileUtils.createFile(getString(R.string.app_name) + System.currentTimeMillis() + "image.jpg");
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，防止拿到
        Uri mMBitmapUri = Uri.fromFile(mFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMBitmapUri);
        startActivityForResult(openCameraIntent, REQ_CODE_TAKE_PHONE_FROM_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FragmentActivity.RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_TAKE_PHONE_FROM_CAMERA:
                    if (mFile != null) {
                        Uri mMBitmapUri = Uri.fromFile(mFile);
                        if (mMBitmapUri != null) {
                            if (!TextUtils.isEmpty(mMBitmapUri.getPath())) {
                                dealImagePath(mMBitmapUri.getPath());
                            }
                        }
                    }
                    break;
                case REQ_CODE_TAKE_PHONE_FROM_PHONES:
                    String galleryBitmapPath = getGalleryBitmapPath(data);
                    if (!TextUtils.isEmpty(galleryBitmapPath)) {
                        dealImagePath(galleryBitmapPath);
                    }
                    break;
                case REQ_CODE_AREA_TAKE_PHONE:
                    String imageUrl = data.getStringExtra(ExtraKeys.IMAGE);
                    if (!TextUtils.isEmpty(imageUrl)) {
                        dealImagePath(imageUrl);
                    }
                    break;
                case REQ_CODE_TAKE_PHONE_FROM_GALLERY:
                    String imagePath = data.getStringExtra(ExtraKeys.IMAGE);
                    if (!TextUtils.isEmpty(imagePath)) {
                        dealImagePath(imagePath);
                    }
                    break;
            }
        } else {
            dismissAllowingStateLoss();
        }

    }

    private String getGalleryBitmapPath(Intent data) {
        if (data != null && data.getData() != null) {
            Uri photosUri = data.getData();
            if (photosUri != null) {
                ContentResolver contentResolver = getActivity().getContentResolver();
                Cursor cursor = contentResolver.query(photosUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    //最后根据索引值获取图片路径
                    String path = cursor.getString(column_index);
                    if (!TextUtils.isEmpty(path)) {
                        return path;
                    }
                    cursor.close();
                } else {
                    if (!TextUtils.isEmpty(photosUri.getPath())) {
                        return photosUri.getPath();
                    } else {
                        return photosUri.toString();
                    }
                }
            }
        }
        return null;
    }

    private void dealImagePath(String imaUri) {
        if (mImageDealType == IMAGE_TYPE_CLIPPING_IMAGE_SCALE_OR_MOVE) {
            Intent intent = new Intent(getActivity(), ClipImageActivity.class);
            intent.putExtra(ClipImageActivity.KEY_CLIP_USER_IMAGE, imaUri);
            getActivity().startActivityForResult(intent, REQ_CLIP_HEAD_IMAGE_PAGE);
        } else {
            if (mOnImagePathListener != null) {
                mOnImagePathListener.onImagePath(mImageUrlIndex, imaUri.replace("/raw//", ""));
            }
        }
        dismissAllowingStateLoss();
    }
}
