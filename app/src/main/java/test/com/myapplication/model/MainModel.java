package test.com.myapplication.model;

import com.eagle.common.mvp.IModel;
import com.eagle.common.util.net.NetUtil;
import com.eagle.net.model.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;

public class MainModel implements IModel {

    public <T> void initData(Observer<T> observer){
        HttpHeaders headers = new HttpHeaders();
        headers.put("token", "7dce9cf1-4e47-4633-8b74-bc86b0831b22");
        headers.put("SSCOMMONS", "6BD7BA96B52AC44C25EDD6B001CDEB92D6F61302E80B51454330B67ADB21A4B71CABC1A6E0B75AF35042A83C906ECBB076DC40EDE63691536181128FB1ED01ADE16CD63EC0EB73041B7AEB476083CFA2F8F4A925C371502DA3AFC6DC6D8D59F60A9C34924F4724A7DFF4282A8A8427457F8F4F6C37269039");
//        String url = "http://eaglesite.bingex.com/eagle-v1.0.2.apk";
//                String url = "http://eagleapi.bingex.com/app/users/changePassword";
                String url = "http://eagleapi.bingex.com/app/courier/checkUpdate";
        Map<String, String> params = new HashMap<>();
        params.put("versionName", "23");
        NetUtil.getRequest(url,  headers, params, observer);
    }
}
