package com.example.refrigerator;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    String dbName = "MyDB";
    String createTable = "create table If Not Exist UserIDTable (id text ,pw text);";
    SQLiteDatabase database;
    EditText id;
    EditText pw;
    Button btnLogIn;
    Button btnSignUp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
        database = openOrCreateDatabase(dbName, MODE_MULTI_PROCESS, null);
        id = (EditText) findViewById(R.id.editID);
        pw = (EditText) findViewById(R.id.editPW);
        btnLogIn = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignup);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String getId = id.getText().toString();
            	String getPw = pw.getText().toString();
            	String sql = "select * from UserIDTable where id =" + "'"+getId+"'" + "and pw = " +"'"+getPw+"'";
            	Cursor result = database.rawQuery(sql, null);
            	if(result.moveToFirst()){
            		 Toast.makeText(LoginActivity.this,"환영합니다!", Toast.LENGTH_SHORT).show();
            		 finish();
            	}else {
            		Toast.makeText(LoginActivity.this,
                            "ID 혹은 PW를 확인 해주세요", Toast.LENGTH_SHORT).show();
            	}
			}
		});
        btnSignUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String getId = id.getText().toString();
            	String getPw = pw.getText().toString();
            	if(getId.equals("") || getPw.equals("")){
            		Toast.makeText(LoginActivity.this,"ID혹은 PW를 입력 해주세요", Toast.LENGTH_SHORT).show();
            	}else {
            		String sql = "select * from UserIDTable where id =" + "'"+getId+"'" + "and pw = " +"'"+getPw+"'";
            		Cursor result = database.rawQuery(sql, null);
            		if(result.moveToFirst()){
            			Toast.makeText(LoginActivity.this,"이미 존재하는 ID 입니다", Toast.LENGTH_SHORT).show();
            			id.setText("");
            			pw.setText("");
            		}else {
            			String sql2 = "insert into UserIDTable  (id, pw)  values (" + "'"+getId+"'" + "," +"'"+getPw+"')";
            			database.execSQL(sql2);
            			Toast.makeText(LoginActivity.this,"가입이 완료 되었습니다!\n환영합니다!", Toast.LENGTH_SHORT).show();
            			id.setText("");
            			pw.setText("");
	            		finish();
            		}
            	}
            	
			}
		});
        
        
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.

	}
	 public void onClick(View view) {
		 	Log.i("here", view.toString());
	        switch (view.getId())
	        {
	            case R.id.btnLogin:
	            	String getId = id.getText().toString();
	            	String getPw = pw.getText().toString();
	            	String sql = "select * from UserIDTable where id =" + getId + "and pw = " +getPw;
	            	Cursor result = database.rawQuery(sql, null);
	            	if(result.moveToFirst()){
	            		 Toast.makeText(LoginActivity.this,
	                             "환영합니다!", Toast.LENGTH_SHORT).show();
	            		 finish();
	            	}else {
	            		Toast.makeText(LoginActivity.this,
	                             "ID 혹은 PW를 확인 해주세요", Toast.LENGTH_SHORT).show();
	            	}
	            	break;

	            case R.id.btnSignup:

	                break;

	        }
	 }
	public void createTable(){
        try{
            database.execSQL(createTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public void onBackPressed() {
        //super.onBackPressed();
		moveTaskToBack(true);
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
    }

}
