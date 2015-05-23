package com.example.refrigerator;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	private ArrayList<ListItem> data;
	private LayoutInflater inflater;
	private int layout;
	
	public CustomAdapter(Context context, int layout, ArrayList<ListItem> data){
		this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data=data;
		this.layout=layout;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			 convertView=inflater.inflate(layout,parent,false);
		}
		ListItem listviewitem=data.get(position);
		TextView name=(TextView)convertView.findViewById(R.id.txtName);
		name.setText(listviewitem.getName());
		TextView buyInfo=(TextView)convertView.findViewById(R.id.txtBuyInfo);
		buyInfo.setText(listviewitem.getBuyInfo());
		TextView limitInfo=(TextView)convertView.findViewById(R.id.txtLimitInfo);
		limitInfo.setText(listviewitem.getLimitInfo());
		return convertView;
	}
	public void refresh(Context context, int layout, ArrayList<ListItem> data){
		this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data=data;
		this.layout=layout;
	}
}
