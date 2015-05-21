package com.example.refrigerator;

public class ListItem {
    private String[] mData;

    public ListItem(String[] data){
        mData = data;
    }

    public ListItem(String name, String buyyear, String buymonth, String buyday, String limityear, String limitmonth, String limitday){
        mData = new String[7];
        mData[0] = name;
        mData[1] = buyyear;
        mData[2] = buymonth;
        mData[3] = buyday;
        mData[4] = limityear;
        mData[5] = limitmonth;
        mData[6] = limitday;
    }

    public String[] getData(){
        return mData;
    }

    public String getData(int index){
        return mData[index];
    }

    public void setData(String[] data){
        mData = data;
    }

	public String getName() {
		// TODO Auto-generated method stub
		return mData[0];
	}
	
	public String getBuyInfo() {
		// TODO Auto-generated method stub
		return "구매일 : " + mData[1] + " - "+ mData[2] + " - "+ mData[3];
	}
	public String getLimitInfo() {
		// TODO Auto-generated method stub
		return "유통기한 : " + mData[4] + " - "+ mData[5] + " - "+ mData[6];
	}
}
