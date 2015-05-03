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
}
