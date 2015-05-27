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
import android.widget.Spinner;
import android.widget.TextView;

public class IngredientActivity extends Activity implements OnItemSelectedListener, OnClickListener {

	private Spinner sp;
	private Button addBtn;
	private Button finishBtn;
	private TextView text;
	private String selectedItem;
	private StringBuffer searchMessage; //���� ��� ������ ��Ʈ�� 
	SQLiteDatabase database;
    String dbName = "MyDB";
    ArrayList<String> arr_id_list = null;
    
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient); //// �Ϲ����� UI����(activity class)�� java �ڵ带 ��������ִ� �κ�
        database = openOrCreateDatabase(dbName, MODE_MULTI_PROCESS, null);
        arr_id_list = new ArrayList<String>();
        selectData();
        
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 

				android.R.layout.simple_dropdown_item_1line, arr_id_list); 

                               //���ǳ� �Ӽ�

		sp = (Spinner) findViewById(R.id.spinner1);
		sp.setPrompt("���1"); // ���ǳ� ����?? ��� ���°���?
		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(this);
		
		searchMessage = new StringBuffer();
		addBtn = (Button) findViewById(R.id.addBtn);
		finishBtn = (Button) findViewById(R.id.finishBtn);
		text = (TextView) findViewById(R.id.itemText);	
		
		addBtn.setOnClickListener(this);
		finishBtn.setOnClickListener(this);
		
		
	}
	
	public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.addBtn:
                searchMessage.append(selectedItem + " ");
                
                text.setText(searchMessage.toString());
                
                break;

            case R.id.finishBtn:
            	Intent intent = new Intent (this, RecipeActivity.class);						
				Bundle myData = new Bundle();				
	            myData.putString("key", text.getText() + " �丮" + " ������");	           
	            intent.putExtras(myData);							
				startActivity(intent);
                break;

        }

    }
	
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                                           //arg2 - �ε���
			long arg3) {

		// TODO Auto-generated method stub
		selectedItem = arr_id_list.get(arg2);

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
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
