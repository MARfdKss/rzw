package test.com.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String a = "key=dsdsdsd";
        JSONObject o = null;
        try {
            o = new JSONObject(a);
            String val = (String) o.get("key");
            System.out.println(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}