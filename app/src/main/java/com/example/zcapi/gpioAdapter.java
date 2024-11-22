package com.example.zcapi;


import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zcapi;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class gpioAdapter extends BaseAdapter {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        Toast toast=null;
        Context toastContext=null;
        ArrayList<String> myList;
        Boolean[] setEnable=null;
        int[] gpioStatus=null;
        ListView clv;
        ArrayList<String> boardSilkName=null;
        Handler mHandler;
    	Runnable updateTextRunnable;
    	Timer timer =null;
    	zcapi zcApi;

        //Gpio gpio;
        public gpioAdapter(Context context, String BoradName) {
            this.mInflater = LayoutInflater.from(context);
            zcApi=new zcapi();
            zcApi.getContext(context);
            BoardGpioName boardGpioName=new BoardGpioName();
            boardSilkName=boardGpioName.getBoardGpioSilkName(BoradName);//主板GPIO丝印
            myList=boardGpioName.getBoradGpioRealName(BoradName);//主板实际操作的GPIO名称
            toastContext=context; 
            setEnable=new Boolean[myList.size()];
            gpioStatus=new int[myList.size()];
            
            for(int i=0;i<setEnable.length;i++){
            	setEnable[i]=false;
            	gpioStatus[i]= zcApi.readGpio(getPoint(i), getNum(i));
            	zcApi.setMulSelGpio(getPoint(i), getNum(i), 0);            //初始化引脚为输入模式
            }
            
        	if(timer==null) {           //线程只有一个任务，实时读取引脚状态
        		timer=new Timer();
            	timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                    	 
                    	for(int i=0;i<gpioStatus.length;i++) {
                    		int j= zcApi.readGpio(getPoint(i), getNum(i));
                    		if(j!=gpioStatus[i]) {
                    			gpioStatus[i]=j;
                    			mHandler.post(updateTextRunnable);
                    		}
                    		if(j==-1) {
                    			timer.cancel();
                    		}
                    	}
                    	
                    }
                },100,100);//0.1s后执行。并每0.1s执行一次
        	}
            
            
            mHandler = new Handler();
    		updateTextRunnable = new Runnable() {
    			@Override
    			public void run() {
//    				如果不想设置监听也可让其不断地运行，保证内容的刷新，不过很浪费性能；
  //  				mHandler.post(this);	
    				notifyDataSetChanged();
    			}
    		};
            
        }  
        	
        @Override
        public int getCount() {  
            return myList.size();//返回数组的长度
        }  
   
        @Override
        public Object getItem(int position) {
            return position;  
        }  
   
        @Override
        public long getItemId(int position) {  
            return 0;  
        }  
           

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
        	ViewHolder holder;

             //Log.v("MyListViewBase", "getView " + position + " " + convertView);  
            if (convertView == null) {  
                     convertView = mInflater.inflate(R.layout.controlview,null);  
                     holder = new ViewHolder();
                    /**得到各个控件的对象*/
                    holder.textName = (TextView) convertView.findViewById(R.id.boardName);
                    holder.textPoint = (TextView) convertView.findViewById(R.id.point);
                    holder.textNum = (TextView) convertView.findViewById(R.id.num);
                    holder.textStatus = (TextView) convertView.findViewById(R.id.textStatus);
                    holder.setStatusBt = (ToggleButton) convertView.findViewById(R.id.bt2);
                    holder.setModeBt = (ToggleButton) convertView.findViewById(R.id.bt3);
                    convertView.setTag(holder);//绑定ViewHolder对象
                    
            }  
            else{  
                    holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }

            //"设置引脚"按钮的状态 当引脚选择输入模式时，不可直接设置引脚高低、
            holder.setStatusBt.setEnabled(setEnable[position]);
            if(!setEnable[position])     //当设置引脚为高，又点击了设置模式为输入，电平自动拉低，因此设置引脚
            	if(holder.setStatusBt.isChecked())//按钮应该返回到“低”状态
            		holder.setStatusBt.setChecked(false);
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            
            holder.textName.setText(boardSilkName.get(position).toString());
            holder.textPoint.setText(String.valueOf(getPoint(position)));
            holder.textNum.setText(String.valueOf(getNum(position)));
            holder.textStatus.setText(String.valueOf(zcApi.readGpio(getPoint(position), getNum(position))));
            holder.setStatusBt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                	if(isChecked){
                        zcApi.writeGpio(getPoint(position), getNum(position), 1);//设置引脚高低(引脚组，引脚号，高)
                		//Log.e("szl","writeGpio "+ getPoint(position)+ " "+getNum(position)+1);
                		//Toast.makeText(toastContext,String.valueOf(i), 0).show();
                	}else {
                        zcApi.writeGpio(getPoint(position), getNum(position), 0);
                	}
                	notifyDataSetChanged();
                }
            });
            holder.setModeBt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                	
                	if(isChecked){
                        zcApi.setMulSelGpio(getPoint(position), getNum(position), 1);//设为输出模式
                		setEnable[position]=true;
                	}else {
                		setEnable[position]=false;
                        zcApi.setMulSelGpio(getPoint(position), getNum(position), 0);//设为输入  (引脚组，引脚号,输入)
                	}
                	notifyDataSetChanged();
                }
            });
            
            return convertView;  
        }
    
    public void cancelTimer() {
    	timer.cancel();
    }
    
    public char getPoint(int position) {
        //假设是A40，获取“PB14”里的引脚组 'B'.
    	String string=myList.get(position).toString().substring(1,2);
    	char group=string.toCharArray()[0];
    	return group;
    }
    
    public int getNum(int position) {
        //假设是A40，获取“PB14”里的引脚号 "14".
    	String st=myList.get(position).toString().substring(2);
    	int num= Integer.parseInt(st);
    	return num;
    }
    
   	public final class ViewHolder{  
        	public TextView textName;
        	public TextView textPoint;
        	public TextView textNum;
        	public TextView textStatus;
        	public ToggleButton setStatusBt;
        	public ToggleButton setModeBt;
	}  
  
    public void makeToast(String str){
       	toast.makeText(toastContext,str, Toast.LENGTH_SHORT).show();
       	
    }
    
    
}  
       
