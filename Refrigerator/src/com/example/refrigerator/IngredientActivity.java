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
import android.widget.Toast;

public class IngredientActivity extends Activity implements OnItemSelectedListener, OnClickListener {

	private Spinner sp;
	private Button addBtn;
	private Button removeBtn;
	private Button finishBtn;
	private TextView text;
	private ArrayList selectedItem;
	private int i = -1; //�������ε���
	private String currentItem;
	private StringBuffer searchMessage; //���� ��� ������ ��Ʈ�� 
	SQLiteDatabase database;
    String dbName = "MyDB";
    ArrayList<String> arr_id_list = null;
    
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient); //// �Ϲ����� UI����(activity class)�� java �ڵ带 ��������ִ� �κ�
        
        selectedItem = new ArrayList<String>();
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
		removeBtn = (Button) findViewById(R.id.removeBtn);
		finishBtn = (Button) findViewById(R.id.finishBtn);
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
                searchMessage.append(currentItem + " ");
                i++;
                text.setText(searchMessage.toString());
                break;
                
            case R.id.removeBtn:
            	if(i == -1)
            	{
            		return;
            	}
            	int end = searchMessage.toString().length() + 1;
            	
            	String removed = searchMessage.toString().replace(selectedItem.get(i).toString() + " ", ""); //�ǵ� �����۸� ����
            	selectedItem.remove(i);
            	i--;
            	searchMessage.delete(0, end);
            	searchMessage.append(removed);
            	
            	text.setText(searchMessage.toString());
            	break;
            	
            case R.id.finishBtn:
            	if(text.getText().toString().compareTo("")==0)
            	{
            		Toast.makeText(IngredientActivity.this, "��Ḧ �߰����ּ���", Toast.LENGTH_SHORT).show();
            		return;
            	}
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
		currentItem = arr_id_list.get(arg2);

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
