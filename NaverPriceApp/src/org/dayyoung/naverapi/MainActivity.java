package org.dayyoung.naverapi;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView textView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);        
        
        textView = (TextView)findViewById(R.id.EditText01);       
        Button btn3 = (Button)findViewById(R.id.Button03);
        
        btn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent intent = new Intent (MainActivity.this,ResultActivity.class);						
				Bundle myData = new Bundle();				
	            myData.putString("key", textView.getText().toString() + " 요리" + " 레시피");	           
	            intent.putExtras(myData);							
				startActivity(intent);
						
			}
		});
        
        
    }
}