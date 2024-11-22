package com.example.zcapi;

import java.util.ArrayList;

public class BoardGpioName {

	//主板的丝印名称是主板上看得到的GPIO的名字。
	//主板的实际操作GPIO口名字，是系统内部处理GPIO口时实际要操作的引脚名称。

	//所有主板系列名称
	public ArrayList<String> getAllBoardName(){        //这是主板名称。点击后进入相应的页面
		ArrayList<String> str=new ArrayList<String>();
		str.add("ZC-20A");
		str.add("ZC-40A");
		str.add("ZC-40M");
		str.add("ZC-64A");
		str.add("ZC-83A");
		str.add("ZC-83E");
		str.add("ZC-328");
		str.add("ZC-328E");
		str.add("ZC-328S");
		str.add("ZC-339");
		str.add("ZC-339E");
		str.add("ZC-312");
		str.add("ZC-358");
		str.add("ZC-356");
		str.add("ZC-972");
		return str;
	}

	//主板内部实际操作GPIO名称
	public ArrayList getBoradGpioRealName(String BoradName){
		ArrayList<String> list=new ArrayList<String>();
		if(BoradName.equals("ZC-20A")) {
			list.add("PI11");
			list.add("PI10");
			list.add("PB18");
			list.add("PB19");
		}else if(BoradName.equals("ZC-339")||BoradName.equals("ZC-339E")) {
			list.add("IO1");
			list.add("IO2");
			list.add("IO3");
			list.add("IO4");
		//}else if(BoradName.equals("ZC-339E")) {
		//	list.add("PO4");
		//	list.add("PO3");
		//	list.add("PO2");
		//	list.add("PO1");
		}else if(BoradName.equals("ZC-328E")) {
			list.add("PO4");    //注意看清楚！是英文的I+英文的O+阿拉伯数字1(大写)
			list.add("PO3");
			list.add("PO14");  //16
			list.add("PO15");//17
			list.add("PO2");//gpio-in
			list.add("PO1"); //
			list.add("PO12");//moto
			list.add("PO13");
		}else if(BoradName.equals("ZC-40A")){
			list.add("PB14");
			list.add("PB15");
			list.add("PB16");
			list.add("PB17");
		}else if(BoradName.equals("ZC-40M")){
			list.add("PB19");
			list.add("PB18");  
			list.add("PH0");
			list.add("PH1");
		}
		else if(BoradName.equals("ZC-83A")){
			list.add("PE14");
			list.add("PE15");
			list.add("PE16");
		}else if (BoradName.equals("ZC-358")){
			list.add("IO1");
			list.add("IO2");
			list.add("IO3");
			list.add("IO4");
		}else if (BoradName.equals("ZC-972")){
			list.add("IO1");
			list.add("IO2");
			list.add("IO3");
			list.add("IO4");
		}else if (BoradName.equals("ZC-356")){
			list.add("IO1");
			list.add("IO2");
			list.add("IO3");
			list.add("IO4");
		}
		else {
			list=getBoardGpioSilkName(BoradName); //其他主板，丝印名称跟主板实际操作GPIO名称一样
		}
		
		return list;
	}

	//主板GPIO丝印名称
	public ArrayList<String> getBoardGpioSilkName(String board){
		ArrayList<String> list=new ArrayList<String>();
		if(board.equals("ZC-20A")||board.equals("ZC-20M")) {             //不同主板 引脚数量，丝印，引脚组引脚号都不同
			list.add("IO1/PI11");
			list.add("IO2/PI10");
			list.add("IO3/PB18");
			list.add("IO4/PB19");
		}else if(board.equals("ZC-40A")) {
			list.add("IO1/PB14");
			list.add("IO2/PB15");
			list.add("IO3/PB16");
			list.add("IO4/PB17");
		}else if(board.equals("ZC-40M")) {
			list.add("GPIO1");
			list.add("GPIO2");
			list.add("GPIO3");
			list.add("GPIO4");
		}else if(board.equals("ZC-64A")) {
			list.add("PE0");
			list.add("PE1");
			list.add("PE2");
			list.add("PE3");
			list.add("PE12");
			list.add("PE13");
			list.add("PE14");
			list.add("PE15");
		}else if(board.equals("ZC-83A")) {
			list.add("IO3/PE14");
			list.add("IO2/PE15");
			list.add("IO1/PE16");
		}else if(board.equals("ZC-83E")) {
			list.add("PE16");
			list.add("PH4");
			list.add("PE14");
			list.add("PE15");
		}else if(board.equals("ZC-328")||board.equals("ZC-328D")) {
			list.add("IO1");    //注意看清楚！是英文的I+英文的O+阿拉伯数字1(大写)
			list.add("IO2");
			list.add("IO3");
			list.add("IO4");
		}else if(board.equals("ZC-328E")) {
			list.add("4");
			list.add("3");
			list.add("2");
			list.add("1");
			list.add("IO2");
			list.add("IO1");
			list.add("继电器1");
			list.add("继电器2");
		}else if(board.equals("ZC-328S")) {
			list.add("IO1");      //注意看清楚！是英文的I+英文的O+阿拉伯数字1(大写)
			list.add("IO2");
			list.add("IO3");
			list.add("IO4");
		//}else if(board.equals("ZC-328D")){
		//	list.add("IO1");      //注意看清楚！是英文的I+英文的O+阿拉伯数字1(大写)
		//	list.add("IO2");
		//	list.add("IO3");
		//	list.add("IO4");
		}else if(board.equals("ZC-339")||board.equals("ZC-339E")) {
			list.add("IO4/G4-D2");
			list.add("IO3/G4-D3");
			list.add("IO2/G4-D4");
			list.add("IO1/G4-D5");
		//}else if(board.equals("ZC-339E")) {
		//	list.add("IO1");      //注意看清楚！是英文的I+英文的O+阿拉伯数字1(大写)
		//	list.add("IO2");
		//	list.add("IO3");
		//	list.add("IO4");
		}else if(board.equals("ZC-312")){
			list.add("IO1");      //注意看清楚！是英文的I+英文的O+阿拉伯数字1(大写)
			list.add("IO2");
			list.add("IO3");
			list.add("IO4");
		}else if(board.equals("ZC-358")) {
			list.add("IO1");
			list.add("IO2");
			list.add("IO3");
			list.add("IO4");
		}else if(board.equals("ZC-972")) {
			list.add("IO1");
			list.add("IO2");
			list.add("IO3");
			list.add("IO4");
		}else if(board.equals("ZC-356")) {
			list.add("IO1");
			list.add("IO2");
			list.add("IO3");
			list.add("IO4");
		}
			
		return list;
	}
	
	
}
