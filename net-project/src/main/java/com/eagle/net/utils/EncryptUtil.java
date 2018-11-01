package com.eagle.net.utils;


import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class EncryptUtil {

    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final Random RANDOM = new Random();
    /**
     * 针对URL进行签名，关于这几个参数的作用，详细请看
     * http://www.cnblogs.com/bestzrz/archive/2011/09/03/2164620.html
     */
    public static Map<String, String> sign(Map<String, String> params) {

        params.put("nonce", getRndStr(6 + RANDOM.nextInt(8)));
        params.put("timestamp", "" + (System.currentTimeMillis() / 1000L));
        StringBuilder sb = new StringBuilder();
//        Map<String, String> map = new HashMap<>();
//        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
//            map.put(entry.getKey(), entry.getValue().get(0));
//        }
        for (Map.Entry<String, String> entry : getSortedMapByKey(params).entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.delete(sb.length() - 1, sb.length());
//        String sign = MD5Utils.encode(sb.toString());
        String sign = "1111111111111";
        params.put("sign", sign);
        return params;
    }

    /** 获取随机数 */
    private static String getRndStr(int length) {
        StringBuilder sb = new StringBuilder();
        char ch;
        for (int i = 0; i < length; i++) {
            ch = CHARS.charAt(RANDOM.nextInt(CHARS.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    /** 按照key的自然顺序进行排序，并返回 */
    private static Map<String, String> getSortedMapByKey(Map<String, String> map) {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        };
        Map<String, String> treeMap = new TreeMap<>(comparator);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            treeMap.put(entry.getKey(), entry.getValue());
        }
        return treeMap;
    }

}
