package com.example.zcapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;


public class ControlActivity extends Activity {
	String BoardName=null;
	gpioAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    			//WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Intent intent = getIntent();
        Bundle data = intent.getBundleExtra("data");
        BoardName = data.getString("myinfo");                 //接收主activity发送过来的主板名字
		System.out.println("11111111111111111>>"+BoardName);
		System.out.println("22222222222222222>>"+data);
		final ListView clv;
		clv=(ListView) findViewById(R.id.clv);  //listview 来显示某主板的所有引脚以及可执行操作
		
		View header = LayoutInflater.from(this).inflate(R.layout.head, null);
 		clv.addHeaderView(header);       //为listview增加标题：主板丝印 引脚组 引脚号 引脚状态。。。
		BoardGpioName boardGpioName=new BoardGpioName();
		mAdapter=new gpioAdapter(this,BoardName);    //把某主板的丝印引脚信息的 ArrayList传给FAdapter
		clv.setAdapter(mAdapter);//show the list
		
	}
	
	@Override
	public void onBackPressed() {
		mAdapter.cancelTimer();      //由于FAdapter里使用了线程，出来要销毁
		finish();
	}

	
	
	
}
