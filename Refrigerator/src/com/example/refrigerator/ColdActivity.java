package com.example.refrigerator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by DeokR on 2015-02-15.
 */
public class ColdActivity extends Activity {


    TextView et;
    int index;
    ArrayList<ListItem> listItems = new ArrayList<ListItem>();
    ListItem list;
    android.os.Handler hanler = new android.os.Handler();
    private final String SERVER_ADDRESS = "http://wonyoungdb.esy.es/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cold);
        et = (TextView)findViewById(R.id.textView4);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }

    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.imageButton4: //case문으로 나누어 이미지버튼을 눌렀을때 행해지는 이벤트를 구성하였다.
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent); //여기서 실행되는 이벤트는 버튼클릭시(onclick) mainActvity2로 이동하는것이다.

                finish();
                break;

            case R.id.imageButton3:
                Intent intent1 = new Intent(this, ColdAddActivity.class);
                startActivity(intent1);
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //버튼클릭시 운영체제에서 핸드폰에게 진동이벤트를 부여한다.
                vibrator.vibrate(400); //여기서는 400ms 동안 진동이 울리도록 하였다.
                finish(); // finish를 써주게 되면 뒤로가기를 눌렀을때 바로전 activity로 화면전환이 실행된다.
                break;


        }

    }
    private void get() {
        String url = SERVER_ADDRESS + "search.php";
        HttpPost post = new HttpPost(url);
        HttpClient client = new DefaultHttpClient();
        String json = "";
        JSONObject jObj = null;

        String name;
        String buyyear;
        String buymonth;
        String buyday;
        String limityear;
        String limitmonth;
        String limitday;

        ResponseHandler reshandler = new BasicResponseHandler();
        try {
            HttpResponse response = client.execute(post);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            // hier habe ich das JSON-File als String
            json = sb.toString();
            JSONObject root = new JSONObject(json);
            JSONArray ja = root.getJSONArray("results");
            index = ja.length();
            final StringBuilder history = new StringBuilder("");
            for(int i = 0; i < ja.length()-1; i++){
                JSONObject jo = ja.getJSONObject(i);
                name = jo.getString("name");
                buyyear = jo.getString("buyyear ");
                buymonth =  jo.getString("buymonth");
                buyday = jo.getString("buyday ");
                limityear = jo.getString("limityear");
                limitmonth = jo.getString("limitmonth");
                limitday =  jo.getString("limitday");
                listItems.add(new ListItem(name,buyyear,buymonth,buyday,limityear,limitmonth,limitday));
                history.append("이름 : " + listItems.get(i).getData(0) + " (구매일 " + listItems.get(i).getData(1) + " - " +listItems.get(i).getData(2) + " - " + listItems.get(i).getData(3) +")\n" + "유통기한 : " + listItems.get(i).getData(4) + " - " + listItems.get(i).getData(5) + " - " + listItems.get(i).getData(6) + "\n\n");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            et.setText(history.toString());
                        }
                    });
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
