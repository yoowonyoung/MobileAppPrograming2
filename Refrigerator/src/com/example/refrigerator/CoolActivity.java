package com.example.refrigerator;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
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
public class CoolActivity extends Activity implements AdapterView.OnItemLongClickListener {
	private ArrayList<ListItem> itemList = null;
	private ArrayList<String> id_list = null;
    private CustomAdapter Adapter = null;
    private SQLiteDatabase database;
	private ListView listview = null;
	private String dbName = "MyDB";
	private String createTable = "create table coolTable (id integer primary key ,name text , buyyear text , buymonth text , buyday text , limityear text ,limitmonth text , limitday text, notifyCheck int default 1);";
    private final String SERVER_ADDRESS = "http://wonyoungdb.esy.es/";
	android.os.Handler hanler = new android.os.Handler();

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool);
        database = openOrCreateDatabase(dbName, MODE_MULTI_PROCESS, null);
        
        id_list = new ArrayList<String>();
    	itemList = new ArrayList<ListItem>();
    	createTable();

        selectData();
        Adapter = new CustomAdapter(this, R.layout.listviewitem, itemList);
        listview = (ListView)findViewById(R.id.l_view_cool);
 
        listview.setAdapter(Adapter);
        listview.setOnItemLongClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imageButton2:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.coolAddPageBtn:
                Intent intent1 = new Intent(this, CoolAddActivity.class);
                startActivity(intent1);
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(400);
                finish();
                break;

        }

    }

    public void selectData(){
        String sql = "select * from coolTable";
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        while(!result.isAfterLast()){
            id_list.add(result.getString(0));
            //arrlist.add(result.getString(1));
            ListItem item = new ListItem(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7));
            itemList.add(item);
            result.moveToNext();
        }
        result.close();
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	itemList.clear();
        selectData();
        Adapter.notifyDataSetChanged();
    }

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Integer selectedPos = i;
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
        alertDlg.setTitle(R.string.alert_title_question);
        Log.i("test", "1");
        alertDlg.setPositiveButton( R.string.button_yes, new DialogInterface.OnClickListener(){
 
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String position = id_list.get(selectedPos);
                final String sql = "delete from coolTable where id = "+ position;
                dialog.dismiss();
                Log.i("test", "onclick");
                database.execSQL(sql);
                itemList.clear();
                selectData();
                Adapter.notifyDataSetChanged();
            }
        });
 
        alertDlg.setNegativeButton( R.string.button_no, new DialogInterface.OnClickListener(){
        	
        	@Override
            public void onClick( DialogInterface dialog, int which ) {
            	String position = id_list.get(selectedPos);
                dialog.dismiss();
                Intent intent = new Intent(CoolActivity.this, UpdateActivity.class);
                intent.putExtra("p_id", position);
                intent.putExtra("tablename", "coolTable");
                startActivity(intent);
            }
        });
 
        alertDlg.setMessage(R.string.alert_msg_delete);
        alertDlg.show();
        return false;
 
	}
    public void createTable(){
        try{
        	//database.execSQL("drop table coolTable;");
            database.execSQL(createTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
