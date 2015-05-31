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
		//�˻��� ��������

		parserTask = new NaverParserTask(); //doInBack�޼ҵ忡 ���ѷ��� �ƴϱ⶧���� �̰� �ѹ� ����Ǹ� ��, �׷��� �Ľ��Ҷ����� ��������ߵ�
		parserTask.execute(Data, String.valueOf(count));

		myListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				int Last = adapter.getCount();
				//adapter ��ü ���� ī����

				Log.i("NET", Last + "Parsing...");

				if ((Last - 1) == arg2) {
					count = count + 10;	//����Ʈ 10�� �߰�
					
					parserTask = new NaverParserTask();
					parserTask.execute(Data, String.valueOf(count));					
					
					
				} else
				{							
					String[] StringArrayData = StringArrayData(arg2);	
					urlTask = new UrlExtractAsyncTask();
					String webUrl= "";
					try {
						webUrl = urlTask.execute(StringArrayData[2]).get(); //asyncTask�� get�޼ҵ尡 ��ȯ�ϴ°��� doInbackground�޼ҵ��� ��ȯ���̴�
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}//api��ũ �Ѱܼ� url�����ؼ� ��ȯ
					
					Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
					startActivity(webIntent); //���ͳ� ����					
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
		//���� ����� ���� ���ͳ� �ּҰ� 
		@Override
		protected String doInBackground(String... params) {
			StringBuffer xmlRead = null; 
			HttpURLConnection httpUrlConn = null;
			InputStreamReader inputReader = null;
			BufferedReader buffReader = null;
			String callUrl = "";
			
			try {

				  callUrl = params[0]; // api��ũ ����
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
	//ù��°�Ķ���ʹ� ��׶���޼ҵ忡 �Ѱ��� ����, �ι�°���� �����ߵ����͹�ȯ��(?), ����°�� ��׶���޼ҵ� ��ȯ��
	{
		
		@Override
		protected ArrayList<XmlData> doInBackground(String... params) {
			String key1 = "eb7cc5af375f2efc3c4b6194bf8457bc"; //���̹����� �߱� ���� ����Ű
			m_xmlData = new ArrayList<XmlData>();
			XmlData xmlData = null;
			String m_searchTxt = "";
			int count = Integer.parseInt(params[1]);
			try {
				m_searchTxt = URLEncoder.encode(params[0], "UTF8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// XML ����� �Ľ��ϱ�
			try {

				URL text = new URL(
						"http://openapi.naver.com/search?key="+key1+"&query="
								+ m_searchTxt + "&display=" + count
								+ "&start=1&target=blog"); //��α� �˻� - target = blog //sort = ���絵��(�⺻��) - sim, ��¥�� - date

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
							//�ڷ� ���� 1�� ����
							
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
							//�ڷ� ���� 1�� �ֱ�
						}
						
						break;
					}
					parseEvent = parser.next();

					// xmlData = null;
					// While next �������� �ѱ��.
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