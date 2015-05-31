package com.example.refrigerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RecipeActivity extends Activity {
	ListView myListview;
	
	String Data;
	int count = 6;
	
	CustomListAdapter adapter;
	ArrayList<XmlData> m_xmlData;
	NaverParserTask parserTask;
	UrlExtractAsyncTask urlTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recipe);
		
		myListview = (ListView) findViewById(R.id.myListview);
	
		
		Intent intent = getIntent();
		Bundle myBundle = intent.getExtras();
		Data = myBundle.getString("key");
		//검색값 가져오기

		parserTask = new NaverParserTask(); //doInBack메소드에 무한루프 아니기때문에 이거 한번 실행되면 끝, 그래서 파싱할때마다 생성해줘야됨
		parserTask.execute(Data, String.valueOf(count));

		myListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				int Last = adapter.getCount();
				//adapter 전체 갯수 카운터

				Log.i("NET", Last + "Parsing...");

				if ((Last - 1) == arg2) {
					count = count + 10;	//리스트 10개 추가
					
					parserTask = new NaverParserTask();
					parserTask.execute(Data, String.valueOf(count));					
					
					
				} else
				{							
					String[] StringArrayData = StringArrayData(arg2);	
					urlTask = new UrlExtractAsyncTask();
					String webUrl= "";
					try {
						webUrl = urlTask.execute(StringArrayData[2]).get(); //asyncTask의 get메소드가 반환하는값은 doInbackground메소드의 반환값이다
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}//api링크 넘겨서 url추출해서 반환
					
					Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
					startActivity(webIntent); //인터넷 실행					
				}
			}
		});
	}	
	
	public String[] StringArrayData(int arg2) {
				
		XmlData xmlData = m_xmlData.get(arg2);
		
		String[] StringArrayData = new String[3];
		
		StringArrayData[0]=xmlData.d_title;
		StringArrayData[1]=xmlData.d_description;
		StringArrayData[2]=xmlData.d_link;
		
		return StringArrayData ;
	}	
	
	
	
	public class UrlExtractAsyncTask extends AsyncTask<String, String, String>
	{
		//나온 결과가 실제 인터넷 주소값 
		@Override
		protected String doInBackground(String... params) {
			StringBuffer xmlRead = null; 
			HttpURLConnection httpUrlConn = null;
			InputStreamReader inputReader = null;
			BufferedReader buffReader = null;
			String callUrl = "";
			
			try {

				  callUrl = params[0]; // api링크 넣음
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
				  }

				  try {
				    if (inputReader != null) {
				      inputReader.close();
				    }

				  } catch (IOException e) {
				  }

				}
			return xmlRead.toString();
		}	
	}
	public class NaverParserTask extends AsyncTask <String, String, ArrayList<XmlData> > 
	//첫번째파라미터는 백그라운드메소드에 넘겨질 인자, 두번째것은 진행중데이터반환값(?), 세번째는 백그라운드메소드 반환값
	{
		
		@Override
		protected ArrayList<XmlData> doInBackground(String... params) {
			String key1 = "eb7cc5af375f2efc3c4b6194bf8457bc"; //네이버에서 발급 받은 인증키
			m_xmlData = new ArrayList<XmlData>();
			XmlData xmlData = null;
			String m_searchTxt = "";
			int count = Integer.parseInt(params[1]);
			try {
				m_searchTxt = URLEncoder.encode(params[0], "UTF8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// XML 결과물 파싱하기
			try {

				URL text = new URL(
						"http://openapi.naver.com/search?key="+key1+"&query="
								+ m_searchTxt + "&display=" + count
								+ "&start=1&target=blog"); //블로그 검색 - target = blog //sort = 유사도순(기본값) - sim, 날짜순 - date

				XmlPullParserFactory parserCreator = XmlPullParserFactory
						.newInstance();
				XmlPullParser parser = parserCreator.newPullParser();

				parser.setInput(text.openStream(), null);

				Log.i("NET", "Parsing...");

				// status.setText("Parsing....");

				int parseEvent = parser.getEventType();
				while (parseEvent != XmlPullParser.END_DOCUMENT) {

					switch (parseEvent) {
					case XmlPullParser.START_TAG:
						String tag = parser.getName();				

						if (tag.compareTo("title") == 0) {
							xmlData = new XmlData();
							//자료 구조 1개 생성
							
							String titleSrc = parser.nextText().replace("<b>", " ").replace("</b>", " ").replace("&lt;", "").replace("&gt;", "").replace("&amp;","").replace("&quot;", "");
							
							xmlData.d_title = titleSrc;
						}
						
						
						if (tag.compareTo("description") == 0) {
							String descriptionSrc = parser.nextText().replace("<b>", " ").replace("</b>", " ").replace("&lt;", "").replace("&gt;", "").replace("&amp;","").replace("&quot;", "");
							xmlData.d_description = descriptionSrc;
							
						}if (tag.compareTo("link") == 0) {
							String linkSrc = parser.nextText().replace("<b>", " ").replace("</b>", " ").replace("&lt;", "").replace("&gt;", "").replace("&amp;","").replace("&quot;", "");
							xmlData.d_link = linkSrc;
							m_xmlData.add(xmlData);			
							//자료 구조 1개 넣기
						}
						
						break;
					}
					parseEvent = parser.next();

					// xmlData = null;
					// While next 다음으로 넘긴다.
				}
				Log.i("NET", "End...");

			} catch (Exception e) {
				Log.i("NET", "Parsing Failed!");
			}	
			return m_xmlData;
		}
		protected void onPostExecute(ArrayList<XmlData> data)
		{
			if(data !=null)
			{
				adapter = new CustomListAdapter(RecipeActivity.this, R.layout.listitem, data);
				myListview.setAdapter(adapter);	
				
			}
		}	
	}	
	
}