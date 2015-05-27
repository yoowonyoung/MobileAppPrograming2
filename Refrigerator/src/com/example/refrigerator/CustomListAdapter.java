package com.example.refrigerator;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<Object> {

	private ArrayList<XmlData> items;

	View v;
		
	public CustomListAdapter(Context context, int textViewResourceId,
			ArrayList items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.listitem, null);
		}

		int Last = this.getCount();
		// adapter ��ü ���� ī����

		if ((Last - 1) == position) {
			// getNewList(Data);
			TextView tv1 = (TextView) v.findViewById(R.id.dataItem01);
			TextView tv2 = (TextView) v.findViewById(R.id.dataItem02);
			tv1.setText("10�� ������");
			tv2.setText("");
		} else

		{

			XmlData xmlData = (XmlData) items.get(position);
			if (xmlData != null) {
				TextView tv1 = (TextView) v.findViewById(R.id.dataItem01);
				TextView tv2 = (TextView) v.findViewById(R.id.dataItem02);
			
				tv1.setText("����:"+ xmlData.d_title);
				tv2.setText("���:" + xmlData.d_description);						
			}
		}

		return v;
	}
}