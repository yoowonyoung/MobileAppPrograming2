package org.dayyoung.naverapi;

import java.io.UnsupportedEncodingException;



import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class NaverParser {	
	
	String key1;
	
	ArrayList<XmlData> m_xmlData;
	

	public NaverParser(String key) {
		// TODO Auto-generated constructor stub
		this.key1 = key;				
	}		


	public ArrayList<XmlData> GetXmlData(String searchTxt,int count) {

		m_xmlData = new ArrayList<XmlData>();
		
		XmlData xmlData = null;

		String m_searchTxt = "";

		try {
			m_searchTxt = URLEncoder.encode(searchTxt, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
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
						
						String titleSrc = (parser.nextText().replace("<b>", " ")).replace("</b>", " "); 
						
						xmlData.d_title = titleSrc;
					}
					
					
					if (tag.compareTo("description") == 0) {
						String descriptionSrc = (parser.nextText().replace("<b>", " ")).replace("</b>", " ");
						xmlData.d_description = descriptionSrc;
						
					}if (tag.compareTo("link") == 0) {
						String linkSrc = (parser.nextText().replace("<b>", " ")).replace("</b>", " ");
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
			// TODO: handle exception
			Log.i("NET", "Parsing Failed!");
		}	
		return m_xmlData;
	}
	
}
