package com.example.zcapi;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        ViewHolder holder; 
        Toast toast=null;
        Context toastContext=null;
        ArrayList<String> myList;

        /**构造函数*/
        public MyAdapter(Context context, ArrayList<String> list) {
            this.mInflater = LayoutInflater.from(context);
            myList=list;
            toastContext=context; 
        }  
   			
        @Override
        public int getCount() {  
            return myList.size();//��������ĳ���          
        }  
   
        @Override
        public Object getItem(int position) {
            return position;  
        }  
   
        @Override
        public long getItemId(int position) {  
            return 0;  
        }

        //返回数组的长度
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
              

             Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {  
            				
                     convertView = mInflater.inflate(R.layout.listview,null);  
                     holder = new ViewHolder();
                    holder.text = (TextView) convertView.findViewById(R.id.functionTextView);
                    convertView.setTag(holder);
            }  
            else{  
                    holder = (ViewHolder)convertView.getTag();
            }  

            holder.text.setText(myList.get(position).toString());  
            
            return convertView;  
        }  
        
   	public final class ViewHolder{  
        		public TextView text;
		}  
  
    public void makeToast(String str){
       	toast.makeText(toastContext,str, Toast.LENGTH_SHORT).show();
       	
    }
    
    
}  
       
