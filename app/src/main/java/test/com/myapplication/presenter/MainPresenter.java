package test.com.myapplication.presenter;

import android.os.Handler;
import android.util.Log;

import com.eagle.common.mvp.BasePresenter;
import com.eagle.common.util.net.NetObserver;


import test.com.myapplication.Data;
import test.com.myapplication.model.MainModel;
import test.com.myapplication.view.MainView;


public class MainPresenter extends BasePresenter<MainModel, MainView> {

    public void initData(){
        getView().showLoadingDialog("");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                model.initData(new NetObserver<Data>(MainPresenter.this){

                    @Override
                    public void onSuccess(Data data) {
                        getView().hideLoadingDialog();
                        Log.e("aa", "s-->" + data.takedCount);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        getView().hideLoadingDialog();
                        Log.e("aa", "err-->" + e.getMessage());
                    }

                });
            }
        }, 1000);

//        NetUtil.download(url, "", "", headers, params, new Observer<Progress>(){
//
//
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Progress progress) {
//                String downloadLength = Formatter.formatFileSize(getApplicationContext(), progress.currentSize);
//                String totalLength = Formatter.formatFileSize(getApplicationContext(), progress.totalSize);
//                Log.e("aa", "s-->" + downloadLength + "/" + totalLength);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e("aa", "err-->" + e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
    }
}
