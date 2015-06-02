package com.example.refrigerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by DeokR on 2015-02-15.
 */
public class ColdActivity extends Activity implements AdapterView.OnItemLongClickListener {//�õ��� ȭ�� �����ִ� Activity


	ArrayList<String> arrlist = null;
	ArrayList<ListItem> arrlist2 = null;
    ArrayList<String> arr_id_list = null;
    CustomAdapter Adapter = null;
    SQLiteDatabase database;
    String dbName = "MyDB";
    String createTable = "create table coldTable (id integer primary key ,name text , buyyear text , buymonth text , buyday text , limityear text ,limitmonth text , limitday text, notifyCheck int default 1);";
    ListView listview = null;
    ArrayList<ListItem> listItems = new ArrayList<ListItem>();//ListItem ������ �迭�� �޾ƿ�. 
    ListItem list;
    android.os.Handler hanler = new android.os.Handler();
    private final String SERVER_ADDRESS = "http://wonyoungdb.esy.es/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cold);
        listview = (ListView) findViewById(R.id.l_view_cold);
        database = openOrCreateDatabase(dbName, MODE_MULTI_PROCESS, null);
        arrlist = new ArrayList<String>();
        arr_id_list = new ArrayList<String>();
    	arrlist2 = new ArrayList<ListItem>();
    	
        createTable();

        selectData();
        
        Adapter = new CustomAdapter(this, R.layout.listviewitem, arrlist2);
        ListView list = (ListView)findViewById(R.id.l_view_cold);
 
        list.setAdapter(Adapter);
        list.setOnItemLongClickListener(this);


    }

    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.coldUperBtn: //case������ ������ �̹�����ư�� �������� �������� �̺�Ʈ�� �����Ͽ���.
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent); //���⼭ ����Ǵ� �̺�Ʈ�� ��ưŬ����(onclick) mainActvity2�� �̵��ϴ°��̴�.

                finish();
                break;

            case R.id.coldAddPageBtn:
                Intent intent1 = new Intent(this, ColdAddActivity.class);
                startActivity(intent1);
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //��ưŬ���� �ü������ �ڵ������� �����̺�Ʈ�� �ο��Ѵ�.
                vibrator.vibrate(400); //���⼭�� 400ms ���� ������ �︮���� �Ͽ���.
                finish(); // finish�� ���ְ� �Ǹ� �ڷΰ��⸦ �������� �ٷ��� activity�� ȭ����ȯ�� ����ȴ�.
                break;


        }

    }
    public void selectData(){
        String sql = "select * from coldTable";
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        while(!result.isAfterLast()){
            arr_id_list.add(result.getString(0));
            //arrlist.add(result.getString(1));
            ListItem item = new ListItem(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7));
            arrlist2.add(item);
            result.moveToNext();
        }
        result.close();
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	arrlist2.clear();
        selectData();
        Adapter.notifyDataSetChanged();
    }

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Integer selectedPos = i;
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
        alertDlg.setTitle(R.string.alert_title_question);
        alertDlg.setPositiveButton( R.string.button_yes, new DialogInterface.OnClickListener(){
 
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String position = arr_id_list.get(selectedPos);
                final String sql = "delete from coldTable where id = "+ position;
                dialog.dismiss();
                Log.i("test", "onclick");
                database.execSQL(sql);
                arrlist2.clear();
                selectData();
                Adapter.notifyDataSetChanged();

            }
        });
 
        alertDlg.setNegativeButton( R.string.button_no, new DialogInterface.OnClickListener(){
 
            @Override
            public void onClick( DialogInterface dialog, int which ) {
                String position = arr_id_list.get(selectedPos);
                dialog.dismiss();
                Intent intent = new Intent(ColdActivity.this, UpdateActivity.class);
                intent.putExtra("p_id", position);
                intent.putExtra("tablename", "coldTable");
                startActivity(intent);
            }
        });
 
        alertDlg.setMessage(R.string.alert_msg_delete);
        alertDlg.show();
        return false;
 
	}
    public void createTable(){
        try{
            database.execSQL(createTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}
