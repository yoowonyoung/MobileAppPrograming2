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
			// TODO: handle exception
			Log.i("NET", "Parsing Failed!");
		}	
		return m_xmlData;
	}
	
}
