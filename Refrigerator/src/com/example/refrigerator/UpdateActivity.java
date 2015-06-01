package com.example.refrigerator;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class UpdateActivity extends Activity {
	SQLiteDatabase database;
	String tablename;
    String id;
    ImageButton btninsert;
    EditText etname;
    EditText etbuyyear;
    EditText etbuymonth;
    EditText etbuyday;
    EditText etlimityear;
    EditText etlimitmonth;
    EditText etlimitday;
    ImageButton Updatebtn;
    Button btnScan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
        database = openOrCreateDatabase("MyDB", MODE_MULTI_PROCESS, null);
        btninsert = (ImageButton) findViewById(R.id.UpdateBtn);
        etname = (EditText) findViewById(R.id.UpdateName);
        etbuyyear = (EditText) findViewById(R.id.UpdateBuyYear);
        etbuymonth = (EditText) findViewById(R.id.UpdateBuyMonth);
        etbuyday = (EditText) findViewById(R.id.UpdateBuyDay);
        etlimityear = (EditText) findViewById(R.id.UpdateLimitYear);
        etlimitmonth = (EditText) findViewById(R.id.UpdateLimitMonth);
        etlimitday = (EditText) findViewById(R.id.UpdateLimitDay);
        
        Updatebtn = (ImageButton)findViewById(R.id.UpdateBtn);
        
        Bundle bundle = getIntent().getExtras();
        id =  bundle.getString("p_id");
        tablename = bundle.getString("tablename");
        getData(id,tablename);
        
        Updatebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        String sql = "update " + tablename + " set name = '"+ etname.getText().toString() +"' , buyyear = '"
		        		+etbuyyear.getText().toString() + "' , buymonth = '" + etbuymonth.getText().toString()  + "' , buyday = '"
		        		+etbuyday.getText().toString() + "' , limityear = '" + etlimityear.getText().toString() + "' , limitmonth = '"
		        		+etlimitmonth.getText().toString()+ "' , limitday = '" + etlimitday.getText().toString() +"' where id = "+id;
		        database.execSQL(sql);
		        finish();
			}
		});

	}
	
	public void getData(String g_id, String tablename){
		 
        String sql = "select * from " + tablename + " where id = " + g_id;
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        etname.setText(result.getString(1));
        etbuyyear.setText(result.getString(2));
        etbuymonth.setText(result.getString(3));
        etbuyday.setText(result.getString(4));
        etlimityear.setText(result.getString(5));
        etlimitmonth.setText(result.getString(6));
        etlimitday.setText(result.getString(7));
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
