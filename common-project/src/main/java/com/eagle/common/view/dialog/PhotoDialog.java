package com.eagle.common.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.eagle.common.base.R;
import com.eagle.photo.app.TakePhoto;
import com.eagle.photo.compress.CompressConfig;
import com.eagle.photo.model.CropOptions;
import com.eagle.photo.model.TakePhotoOptions;

import java.io.File;


public class PhotoDialog extends Dialog implements View.OnClickListener {

    private Activity activity;
    private TakePhoto takePhoto;
    private boolean enableCrop = true;//默认裁剪
    private int width = 800;//裁剪宽度
    private int height = 800;//裁剪高度
    private boolean enableCompress = true;//默认压缩
    private int maxSize = 600 * 1024;//压缩到的最大大小，单位B
    private int limit = 1;//选择图片数量
    private boolean withOwnGallery = false;//是否自定义相册，默认使用系统
    private boolean withOwnCrop = true;//是否使用自定义裁剪，默认使用

    public PhotoDialog(@NonNull Context context, TakePhoto takePhoto) {
        super(context, R.style.PhotoDialog);
        this.activity = (Activity) context;
        this.takePhoto = takePhoto;
        View view = View.inflate(context, R.layout.dialog_select_photo, null);
        setContentView(view);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            layoutParams.width = dm.widthPixels;
            layoutParams.alpha = 1.0f;
            getWindow().setAttributes(layoutParams);
            getWindow().setWindowAnimations(R.style.PhotoDialogAnim);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//让该window后所有的东西都成暗淡
        }
        view.findViewById(R.id.take_photo).setOnClickListener(this);
        view.findViewById(R.id.photo_album).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
    }

    private TakePhotoOptions configTakePhotoOption() {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(withOwnGallery);
        builder.setCorrectImage(true);
        return builder.create();
    }

    private void configCompress(boolean enableCompress, TakePhoto takePhoto) {
        if (!enableCompress) {
            takePhoto.onEnableCompress(null, false);
            return;
        }
        CompressConfig config = new CompressConfig.Builder().setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, true);
    }

    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withOwnCrop);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel) {
            cancel();
        } else if (id == R.id.take_photo) {//拍照
            cancel();
            takePhoto(takePhoto);
        } else if (id == R.id.photo_album) {//相册
            cancel();
            photoAlbum(takePhoto);
        }
    }

    /**
     * 拍照
     */
    private void takePhoto(TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);
        configCompress(enableCompress, takePhoto);
        takePhoto.setTakePhotoOptions(configTakePhotoOption());
        if (enableCrop) {
            takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
        } else {
            takePhoto.onPickFromCapture(imageUri);
        }
    }

    /**
     * 相册
     */
    private void photoAlbum(TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);
        configCompress(enableCompress, takePhoto);
        takePhoto.setTakePhotoOptions(configTakePhotoOption());
        if (limit > 1) {
            if (enableCrop) {
                takePhoto.onPickMultipleWithCrop(limit, getCropOptions());
            } else {
                takePhoto.onPickMultiple(limit);
            }
            return;
        }
        if (enableCrop) {
            takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
        } else {
            takePhoto.onPickFromGallery();
        }
    }

    public PhotoDialog limit(int limit) {
        this.limit = limit;
        return this;
    }

    public PhotoDialog enableCrop(boolean enableCrop) {
        this.enableCrop = enableCrop;
        return this;
    }

    public PhotoDialog cropWidth(int width) {
        this.width = width;
        return this;
    }

    public PhotoDialog cropHeight(int height) {
        this.height = height;
        return this;
    }

    public PhotoDialog enableCompress(boolean enableCompress) {
        this.enableCompress = enableCompress;
        return this;
    }

    public PhotoDialog compressMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public PhotoDialog withOwnGallery(boolean withOwnGallery) {
        this.withOwnGallery = withOwnGallery;
        return this;
    }

    public PhotoDialog withOwnCrop(boolean withOwnCrop) {
        this.withOwnCrop = withOwnCrop;
        return this;
    }

    @Override
    public void show() {
        if (isFinishing()) {
            return;
        }
        super.show();
    }

    @Override
    public void cancel() {
        if (isFinishing()) {
            return;
        }
        super.cancel();
    }

    private boolean isFinishing() {
        return activity.isFinishing();
    }

}
