package com.example.refrigerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by DeokR on 2015-02-25.
 */
public class ColdAddActivity extends Activity {

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
    CheckBox notifyBtn;
    
    int alarmCode = 100;
    int notifyCheck = 0;
    PendingIntent pendingIntent;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.activity_cold_add_activity);
        super.onCreate(savedInstanceState);
        btninsert = (ImageButton) findViewById(R.id.coldAddBtn);
        etname = (EditText) findViewById(R.id.coldName);
        etbuyyear = (EditText) findViewById(R.id.coldBuyYear);
        etbuymonth = (EditText) findViewById(R.id.coldBuyMonth);
        etbuyday = (EditText) findViewById(R.id.coldBuyDay);
        etlimityear = (EditText) findViewById(R.id.coldLimitYear);
        etlimitmonth = (EditText) findViewById(R.id.coldLimitMonth);
        etlimitday = (EditText) findViewById(R.id.coldLimitDay);
        
        btnScan = (Button)findViewById(R.id.btnScan);
        notifyBtn = (CheckBox)findViewById(R.id.notifyBtn);
        
        createDatabase();
        
        btnScan.setOnClickListener(new View.OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A");
				startActivityForResult(intent,0);
			}
		});
        
        notifyBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	  
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                    if (isChecked) {
                        notifyCheck = 1;
                    } else {
                        notifyCheck = 0;
                    }
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
                insertData(etname.getText().toString(), etbuyyear.getText().toString(), etbuymonth.getText().toString(), etbuyday.getText().toString(), etlimityear.getText().toString(), etlimitmonth.getText().toString(), etlimitday.getText().toString(), notifyCheck);
                
                setNotification();
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }


   
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
 

    private void insertData(String name, String buyyear, String buymonth, String buyday, String limityear, String limitmonth, String limitday, int notifyCheck){
        database.beginTransaction();
 
        try{
            String sql = "insert into coldTable (name, buyyear, buymonth, buyday, limityear, limitmonth, limitday, notifyCheck)"
            		+ " values ('"+name+ "','"+buyyear+ "','"+buymonth+ "','"+buyday+ "','"+limityear+ "','"+limitmonth+ "','" + limitday+ "'," + notifyCheck + ");";
            database.execSQL(sql);
            database.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            database.endTransaction();
        }
        finish();
        Intent intent = new Intent(this, ColdActivity.class);
        startActivity(intent); //여기서 실행되는 이벤트는 버튼클릭시(onclick) mainActvity2로 이동하는것이다.
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
               // Handle successful scan
                                         
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
    
    private void setNotification() {
    	if(notifyCheck == 1) {
    		Calendar calendar = Calendar.getInstance();
        	
        	//시연용 코드
        	calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

            //실제로 쓰일 코드
            /*calendar.set(Calendar.MONTH, Integer.parseInt(etlimitmonth.getText().toString())-1);
            calendar.set(Calendar.YEAR, Integer.parseInt(etlimityear.getText().toString()));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(etlimitday.getText().toString()));*/

            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.AM_PM, calendar.get(Calendar.AM_PM));
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+1);
            calendar.set(Calendar.SECOND, 0);
            
            Intent myIntent = new Intent(ColdAddActivity.this, MyReceiver.class);
            myIntent.putExtra("foodName", etname.getText().toString());
    		myIntent.putExtra("buyYear", etbuyyear.getText().toString());
    		myIntent.putExtra("buyMonth", etbuymonth.getText().toString());
    		myIntent.putExtra("buyDay", etbuyday.getText().toString());
    		myIntent.putExtra("alarmCode", alarmCode);
            pendingIntent = PendingIntent.getBroadcast(ColdAddActivity.this, alarmCode, myIntent,0);

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            alarmCode++;
    	}
    }
}