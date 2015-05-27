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
	String key1 = "eb7cc5af375f2efc3c4b6194bf8457bc"; //네이버에서 발급 받은 인증키
	String Data;
	int count = 6;
	//더보기 기능으로 1개가 지워져 보인다.
	
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
		//네이버 인증 키 
		
		Intent intent = getIntent();
		Bundle myBundle = intent.getExtras();
		Data = myBundle.getString("key");
		//검색값 가져오기

		getNewList(Data,count);
		//처음 5개 화면

		myListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				int Last = adapter.getCount();
				//adapter 전체 갯수 카운터

				Log.i("NET", Last + "Parsing...");

				if ((Last - 1) == arg2) {
					count = count + 5;					
					getNewList(Data,count);							
					//더보기를 클릭했을 때, 카운터를 5 증가시키고 GetNewList()
					
				} else
				{							
					String[] StringArrayData = StringArrayData(arg2);						
					String webUrl = urlExtract(StringArrayData[2]); //api링크 넘겨서 url추출해서 반환
					
					Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
					startActivity(webIntent); //인터넷 실행					
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

		  callUrl = apiLink; // api링크 넣음
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

		  if (resultCode != 200) { // 서비스 호출 결과가 성공이 아닌 경우

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
		//나온 결과가 실제 인터넷 주소값 
		return xmlRead.toString();
	}
}
