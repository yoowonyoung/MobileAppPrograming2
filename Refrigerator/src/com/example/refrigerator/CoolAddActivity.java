package com.example.refrigerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by DeokR on 2015-02-25.
 */

public class CoolAddActivity extends Activity {

    private final String SERVER_ADDRESS = "http://wonyoungdb.esy.es/";
    ImageButton btninsert;
    EditText etname;
    EditText etbuyyear;
    EditText etbuymonth;
    EditText etbuyday;
    EditText etlimityear;
    EditText etlimitmonth;
    EditText etlimitday;
     
    SQLiteDatabase database;
    String dbName = "MyDB";
    
    Button btnScan;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cool_add_acitvity);
        super.onCreate(savedInstanceState);
        btninsert = (ImageButton) findViewById(R.id.coolAddBtn);
        etname = (EditText) findViewById(R.id.coolName);
        etbuyyear = (EditText) findViewById(R.id.coolBuyYear);
        etbuymonth = (EditText) findViewById(R.id.coolBuyMonth);
        etbuyday = (EditText) findViewById(R.id.coolBuyDay);
        etlimityear = (EditText) findViewById(R.id.coolLimitYear);
        etlimitmonth = (EditText) findViewById(R.id.coolLimitMonth);
        etlimitday = (EditText) findViewById(R.id.coolLimitDay);
        createDatabase();
        
        btnScan = (Button)findViewById(R.id.btnScan);
        
        btnScan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");
				startActivityForResult(intent,0);
			}
		});

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etname.getText().toString().equals("")) {
                    Toast.makeText(CoolAddActivity.this,
                            "이름을 입력 하세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etlimityear.getText().toString().equals("") || etlimitmonth.getText().toString().equals("") || etlimitday.getText().toString().equals("")) {
                    Toast.makeText(CoolAddActivity.this,
                            "유통기한 정보를 입력 하세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etbuyyear.getText().toString().equals("") || etbuymonth.getText().toString().equals("") || etbuyday.getText().toString().equals("")) {
                    Toast.makeText(CoolAddActivity.this,
                            "구매일 정보를 입력 하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                insertData(etname.getText().toString(), etbuyyear.getText().toString(), etbuymonth.getText().toString(), etbuyday.getText().toString(), etlimityear.getText().toString(), etlimitmonth.getText().toString(), etlimitday.getText().toString());
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            post();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();*/
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
    /*
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
        Intent intent = new Intent(this, CoolActivity.class);
        startActivity(intent); //여기서 실행되는 이벤트는 버튼클릭시(onclick) mainActvity2로 이동하는것이다.

        finish();


    }*/
    private String get(String type, String code) {
    	String url = SERVER_ADDRESS + "BarcodeSearch.php"+"?type="+type+"&code="+code;
    	HttpPost post = new HttpPost(url);
    	HttpClient client = new DefaultHttpClient();
    	String json = "";

    	String name = null;
    	String expridate = null;
    	
    
    	ResponseHandler reshandler = new BasicResponseHandler();
    	try {
    		HttpResponse response = client.execute(post);
    		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
    		StringBuilder sb = new StringBuilder();
    		String line = null;
    		while ((line = reader.readLine()) != null) {
    			sb.append(line + "\n");
    		}
    		json = sb.toString();
    		JSONObject root = new JSONObject(json);
    		name = root.getString("name");
    		Log.i("name : ", name);
    		expridate = root.getString("expridate");
    		Log.i("date : ", expridate);	
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (JSONException e) {
    		e.printStackTrace();
    	}
    	return name + " " + expridate;
    	
	}
    public void createDatabase(){
        database = openOrCreateDatabase(dbName, MODE_MULTI_PROCESS, null);
    }
    private void insertData(String name, String buyyear, String buymonth, String buyday, String limityear, String limitmonth, String limitday){
   	 
        database.beginTransaction();
 
        try{
            String sql = "insert into coolTable (name, buyyear, buymonth, buyday, limityear, limitmonth, limitday)"
            		+ " values ('"+name+ "','"+buyyear+ "','"+buymonth+ "','"+buyday+ "','"+limityear+ "','"+limitmonth+ "','"+limitday+ "');";
            database.execSQL(sql);
            database.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            database.endTransaction();
        }
        finish();
        Intent intent = new Intent(this, CoolActivity.class);
        startActivity(intent);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
            	final String code = intent.getStringExtra("SCAN_RESULT");
                final String type = intent.getStringExtra("SCAN_RESULT_FORMAT");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                            String result = get(type, code);
                            String resultset[] = result.split(" ");
                            Bundle bundle = new Bundle();
                            bundle.putString("name", resultset[0]);
                            bundle.putInt("date", Integer.parseInt(resultset[1]));
                            Message msg = new Message();
                            msg.setData(bundle);
                            messageHandler.sendMessage(msg);
                    }
                }).start();
            } else if (resultCode == RESULT_CANCELED) {
               // Handle cancel
               Log.i("App","Scan unsuccessful");
            }
       } 
    }
    private Handler messageHandler = new Handler() {
    	public void handleMessage(Message msg){
    		String name = msg.getData().getString("name");
    		int expdate = msg.getData().getInt("date");
    		long now = System.currentTimeMillis();
        	Date date = new Date(now);
        	SimpleDateFormat curYear = new SimpleDateFormat("yyyy");
        	SimpleDateFormat curMonth = new SimpleDateFormat("MM");
        	SimpleDateFormat curDay = new SimpleDateFormat("dd");
        	etname.setText(name);
    		etbuyyear.setText(curYear.format(date));
    		etbuymonth.setText(curMonth.format(date));
    		etbuyday.setText(curDay.format(date));
    		date.setDate(date.getDate()+expdate);
    		etlimityear.setText(curYear.format(date));
    		etlimitmonth.setText(curMonth.format(date));
    		etlimitday.setText(curDay.format(date));
    	}
    };
}







