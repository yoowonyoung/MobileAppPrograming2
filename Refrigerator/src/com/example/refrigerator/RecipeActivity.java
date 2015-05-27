package com.example.refrigerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RecipeActivity extends Activity {

	ListView myListview;
	String key1 = "eb7cc5af375f2efc3c4b6194bf8457bc"; //���̹����� �߱� ���� ����Ű
	String Data;
	int count = 6;
	//������ ������� 1���� ������ ���δ�.
	
	CustomListAdapter adapter;
	
	NaverParser naverPaser;
	
	ArrayList<XmlData> m_xmlData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recipe);
		
		myListview = (ListView) findViewById(R.id.myListview);
	
		naverPaser = new NaverParser(key1);
		//���̹� ���� Ű 
		
		Intent intent = getIntent();
		Bundle myBundle = intent.getExtras();
		Data = myBundle.getString("key");
		//�˻��� ��������

		getNewList(Data,count);
		//ó�� 5�� ȭ��

		myListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				int Last = adapter.getCount();
				//adapter ��ü ���� ī����

				Log.i("NET", Last + "Parsing...");

				if ((Last - 1) == arg2) {
					count = count + 5;					
					getNewList(Data,count);							
					//�����⸦ Ŭ������ ��, ī���͸� 5 ������Ű�� GetNewList()
					
				} else
				{							
					String[] StringArrayData = StringArrayData(arg2);						
					String webUrl = urlExtract(StringArrayData[2]); //api��ũ �Ѱܼ� url�����ؼ� ��ȯ
					
					Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
					startActivity(webIntent); //���ͳ� ����					
				}
			}
		});
	}

	public void getNewList(String searchTxt , int NumberOfList) {		
		m_xmlData = naverPaser.GetXmlData(searchTxt, count);		
		adapter = new CustomListAdapter(this,R.layout.listitem,m_xmlData);
		myListview.setAdapter(adapter);		
	}
	
	public String[] StringArrayData(int arg2) {
		// TODO Auto-generated method stub
				
		XmlData xmlData = m_xmlData.get(arg2);
		
		String[] StringArrayData = new String[3];
		
		StringArrayData[0]=xmlData.d_title;
		StringArrayData[1]=xmlData.d_description;
		StringArrayData[2]=xmlData.d_link;
		
		return StringArrayData ;
	}	
	
	public String urlExtract(String apiLink)
	{
		StringBuffer xmlRead = null; 
		HttpURLConnection httpUrlConn = null;
		InputStreamReader inputReader = null;
		BufferedReader buffReader = null;
		String callUrl = "";

		try {

		  callUrl = apiLink; // api��ũ ����
		  xmlRead = new StringBuffer("");
		  
		  httpUrlConn = (HttpURLConnection) new URL(callUrl).openConnection();
		  httpUrlConn.setRequestMethod("POST");
		  httpUrlConn.setRequestProperty("Content-Type", "apllication/xml; charset=UTF-8");
		  httpUrlConn.setUseCaches(false);
		  httpUrlConn.setDoOutput(true);
		  httpUrlConn.setDoInput(true);
		  PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(httpUrlConn.getOutputStream(), "UTF-8"));
		  printWriter.write("Param1=AAA&Param2=BBB");
		  printWriter.flush();

		  int resultCode = httpUrlConn.getResponseCode();

		  if (resultCode != 200) { // ���� ȣ�� ����� ������ �ƴ� ���

		    throw new IOException("Service responded with code[" + resultCode + "] in httpUrlService");

		  } else {

		    inputReader = new InputStreamReader(httpUrlConn.getInputStream(), "UTF-8");

		    buffReader = new BufferedReader(inputReader);

		    String strDmy = "";

		    while ((strDmy = buffReader.readLine()) != null){

		      if (!strDmy.equals("")){
		    	  StringTokenizer st = new StringTokenizer(strDmy,"\"");
		    	  while(st.hasMoreTokens())
		    	  {
		    		  String temp = st.nextToken();
		    		  if(temp.matches(".*http.*"))    
	    			  {	
	    		  			xmlRead.append(temp);
	    			  } 
		    	  }

		      }

		    }

		  }

		} catch(IOException e){
			
		}
		finally {

		  try {

		    if (buffReader != null) {

		      buffReader.close();

		     }

		  } catch (IOException e) {

		    // TODO: handle exception

		  }

		  try {

		    if (inputReader != null) {

		      inputReader.close();

		    }

		  } catch (IOException e) {


		  }

		}
		//���� ����� ���� ���ͳ� �ּҰ� 
		return xmlRead.toString();
	}
}
