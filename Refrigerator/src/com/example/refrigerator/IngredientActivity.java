package com.example.refrigerator;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class IngredientActivity extends Activity implements OnItemSelectedListener, OnClickListener {

	private ImageButton addBtn;
	private ImageButton removeBtn;
	private ImageButton finishBtn;
	private Spinner sp;
	private TextView text;
	private ArrayList selectedItem;
	private int i = -1; //ArrayList의 인덱스
	private String currentItem;
	private StringBuffer searchMessage; //재료들 모두 종합한 스트링 
	private	SQLiteDatabase database;
    private String dbName = "MyDB";
    private ArrayList<String> arr_id_list = null;
    
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient); //// 일반적인 UI형식(activity class)와 java 코드를 연결시켜주는 부분
        
        selectedItem = new ArrayList<String>();
        database = openOrCreateDatabase(dbName, MODE_MULTI_PROCESS, null);
        arr_id_list = new ArrayList<String>();
        selectData();
        
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 

				android.R.layout.simple_dropdown_item_1line, arr_id_list); 

                               //스피너 속성

		sp = (Spinner) findViewById(R.id.spinner1);
		sp.setPrompt("재료1"); // 스피너 제목?? 어디서 쓰는거지?
		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(this);
		
		searchMessage = new StringBuffer();
		addBtn = (ImageButton) findViewById(R.id.addBtn);
		removeBtn = (ImageButton) findViewById(R.id.removeBtn);
		finishBtn = (ImageButton) findViewById(R.id.finishBtn);
		text = (TextView) findViewById(R.id.itemText);
		addBtn.setOnClickListener(this);
		removeBtn.setOnClickListener(this);
		finishBtn.setOnClickListener(this);
		
		
	}
	
	public void onClick(View view) { 
        switch (view.getId())
        {
            case R.id.addBtn:
            	selectedItem.add(currentItem);
            	i++;
            	
                searchMessage.append(currentItem + " ");
                text.setText(searchMessage.toString());
                break;
                
            case R.id.removeBtn:
            	if(i == -1)
            	{
            		return;
            	}
            	else
            	{
            		int end = searchMessage.toString().length() + 1;
            	
            		String removed = searchMessage.toString().replace(selectedItem.get(i).toString() + " ", ""); //맨뒤 아이템만 제거
            		selectedItem.remove(i);
            		i--;
            		
            		searchMessage.delete(0, end);
            		searchMessage.append(removed);
            		text.setText(searchMessage.toString());
            	}
            	break;
            	
            case R.id.finishBtn:
            	if(text.getText().toString().compareTo("")==0)
            	{
            		Toast.makeText(IngredientActivity.this, "재료를 추가해주세요", Toast.LENGTH_SHORT).show();
            		return;
            	}
            	Intent intent = new Intent (this, RecipeActivity.class);						
				Bundle myData = new Bundle();				
	            myData.putString("key", text.getText() + " 요리" + " 레시피");	           
	            intent.putExtras(myData);							
				startActivity(intent);
                break;
        }

    }
	
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                                           //arg2 - 인덱스
		currentItem = arr_id_list.get(arg2);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}
	
	public void selectData(){
	        String sql = "select * from coolTable";
	        String sql1 = "select * from coldTable";
	        
	        Cursor result = database.rawQuery(sql, null);
	        result.moveToFirst();
	        while(!result.isAfterLast()){
	            arr_id_list.add(result.getString(1));
	            result.moveToNext();
	        }
	        
	        result = database.rawQuery(sql1, null);
	        result.moveToFirst();
	        while(!result.isAfterLast()){
	            arr_id_list.add(result.getString(1));
	            result.moveToNext();
	        }
	        result.close();
	        
	}
}
