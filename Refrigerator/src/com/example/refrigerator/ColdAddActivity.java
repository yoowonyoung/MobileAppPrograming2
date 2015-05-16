package com.example.refrigerator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by DeokR on 2015-02-25.
 */
public class ColdAddActivity extends Activity {

    private final String SERVER_ADDRESS = "http://wonyoungdb.esy.es/";
    Spinner spinner;
    ImageButton btninsert;
    EditText etname;
    EditText etbuyyear;
    EditText etbuymonth;
    EditText etbuyday;
    EditText etlimityear;
    EditText etlimitmonth;
    EditText etlimitday;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.activity_cool_add_acitvity);
        super.onCreate(savedInstanceState);
        btninsert = (ImageButton) findViewById(R.id.AddBtn);
        etname = (EditText) findViewById(R.id.coolName);
        etbuyyear = (EditText) findViewById(R.id.coldBuyYear);
        etbuymonth = (EditText) findViewById(R.id.coldBuyMonth);
        etbuyday = (EditText) findViewById(R.id.coldBuyDay);
        etlimityear = (EditText) findViewById(R.id.coldLimitYear);
        etlimitmonth = (EditText) findViewById(R.id.coldLimitMonth);
        etlimitday = (EditText) findViewById(R.id.coldLimitDay);
        spinner = (Spinner) findViewById(R.id.categorySpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "고르신 항목은 " + parent.getItemAtPosition(position).toString() + " 입니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etname.getText().toString().equals("")) {
                    Toast.makeText(ColdAddActivity.this,
                            "이름을 입력 하세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etlimityear.getText().toString().equals("") || etlimitmonth.getText().toString().equals("") || etlimitday.getText().toString().equals("")) {
                    Toast.makeText(ColdAddActivity.this,
                            "유통기한 정보를 입력 하세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etbuyyear.getText().toString().equals("") || etbuymonth.getText().toString().equals("") || etbuyday.getText().toString().equals("")) {
                    Toast.makeText(ColdAddActivity.this,
                            "구매일 정보를 입력 하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            post();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }


    public void onClick(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent); //여기서 실행되는 이벤트는 버튼클릭시(onclick) mainActvity2로 이동하는것이다.

        finish();

    }
    private void post() throws UnsupportedEncodingException {
        HttpClient client = new DefaultHttpClient();
        String url = SERVER_ADDRESS + "/insert.php";
        HttpPost post = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>(7);
        params.add(new BasicNameValuePair("name", etname.getText().toString().trim()));
        params.add(new BasicNameValuePair("buyyear", etbuyyear.getText().toString().trim()));
        params.add(new BasicNameValuePair("buymonth", etbuymonth.getText().toString().trim()));
        params.add(new BasicNameValuePair("buyday", etbuyday.getText().toString().trim()));
        params.add(new BasicNameValuePair("limityear", etlimityear.getText().toString().trim()));
        params.add(new BasicNameValuePair("limitmonth", etlimitmonth.getText().toString().trim()));
        params.add(new BasicNameValuePair("limitday", etlimitday.getText().toString().trim()));

        HttpEntity enty = new UrlEncodedFormEntity(params, HTTP.UTF_8);
        //UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
        post.setEntity(enty);

        HttpResponse responsePost = null;
        try {
            responsePost = client.execute(post);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Intent intent = new Intent(this, ColdActivity.class);
        startActivity(intent); //여기서 실행되는 이벤트는 버튼클릭시(onclick) mainActvity2로 이동하는것이다.

        finish();



    }
}