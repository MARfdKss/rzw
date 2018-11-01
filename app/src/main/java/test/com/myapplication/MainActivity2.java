package test.com.myapplication;

import android.Manifest;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eagle.common.base.BasePhotoActivity;
import com.eagle.photo.model.TResult;
import com.eagle.common.util.permission.Permission;
import com.eagle.common.util.permission.RxPermissions;
import com.eagle.common.view.dialog.PhotoDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import test.com.myapplication.model.MainModel;
import test.com.myapplication.presenter.MainPresenter;
import test.com.myapplication.view.MainView;

/**
 * @author: renzhiwen
 * @description:
 * @createdate: 2018/9/20 下午7:11
 */
public class MainActivity2 extends BasePhotoActivity<MainModel, MainView, MainPresenter> implements MainView {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "件榜单", "件榜单", "件榜单"

    };//"热门", "iOS", "Android", "前端", "后端", "设计", "工具资源"
    private MyPagerAdapter mAdapter;
    private ImageView iv_pic;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp);
//        STabLayout mSTabLayout = (STabLayout) findViewById(R.id.tab_layout);
//        for (String mTitle : mTitles) {
//            mSTabLayout.addTab(mTitle);
//            mFragments.add(SimpleCardFragment.getInstance(mTitle));
//        }
//
//        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
//        mViewPager.setAdapter(adapter);
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mSTabLayout.getTabLayout()));
//        mSTabLayout.setupWithViewPager(mViewPager);
//
//    }

    private Button test;
    @Override
    protected void findById() {
        test = findViewById(R.id.test);
        iv_pic = findViewById(R.id.iv_pic);
    }

    @Override
    protected void setListener() {
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                requestPermissions();
//                presenter.initData();
                showPhotoDialog();
            }
        });
    }

    private void showPhotoDialog(){
        PhotoDialog photoDialog = new PhotoDialog(this, getTakePhoto());
        photoDialog.show();
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        String pic = result.getImages().get(0).getCompressPath();
        Glide.with(this).load(new File(pic)).into(iv_pic);
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.e("aa", permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                            Log.e("aa", permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，而且选中『不再询问』
                            Log.e("aa", permission.name + " is denied.");
                        }
                    }
                });


    }
    @Override
    protected void initView() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main2;
    }

    @Override
    public MainModel createModel() {
        return new MainModel();
    }

    @Override
    public MainView createView() {
        return this;
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


    public static Map<String, Object> transStringToMap(String mapString, String separator, String pairSeparator) {
        Map<String, Object> map = new HashMap<>();
        String[] fSplit = mapString.split(separator);
        for (String aFSplit : fSplit) {
            if (aFSplit == null || aFSplit.length() == 0) {
                continue;
            }
            String[] sSplit = aFSplit.split(pairSeparator);
            String value = aFSplit.substring(aFSplit.indexOf('=') + 1, aFSplit.length());
            map.put(sSplit[0], value);
        }

        return map;
    }

}

