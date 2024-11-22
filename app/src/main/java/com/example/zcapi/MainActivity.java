package com.example.zcapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcapi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    zcapi zcApi;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private static final int REQUEST_CODE = 1024;
    private Context mContext;


//    private static final int REQUEST_PHONE_STATE=1;
//    private static String[] PERMISSIONS_PHONE={
//            "android.permission.READ_PHONE_STATE",
//            "android.permission.READ_PRIVILEGED_PHONE_STATE"
//    };

    TextView messageTextView;
    boolean statusBarEnable=true;
    boolean gestureStatusBarEnable=true;
    boolean bwatchDogEnable=true;

    String shutDown="关机:shutDown()";
    String reboot="重启:reboot()";
    String factoryReset="恢复出厂设置:factoryReset()";
    String updateOta="OTA升级:updateOta()";
    String setStatusBar="设置状态栏:setStatusBar()";
    String setGestureStatusBar="是否允许呼出状态栏:setGestureStatusBar()";
    String setPowetOnOffTime="设置定时开关机:setPowetOnOffTime()";
    String getBuildModel="获取设备型号:getBuildModel()";
    String getBuildSerial="获取序列号:getBuildSerial()";
    String getEthMacAddress="获取以太网MAC地址:getEthMacAddress()";
    String getWifiMacAddress="获取wifi MAC地址:getWifiMacAddress()";
    String setLcdOnOff="设置背光亮灭:setLcdOnOff()";
    String gpioControl="GPIO引脚控制:gpioControl()";
    String execShellCmd="执行命令:execShellCmd()";
    String setSystemTime="设置系统时间:setSystemTime()";
    String watchDogEnable="看门狗开关:watchDogEnable()";
    String screenshot="屏幕截屏:screenshot()";
    String InstallApk="普通安装应用:InstallApk()";
    String InstallApkRoot="root安装应用:InstallApk()";
    String HDMIControl="HDMI控制:setLcdOnOff()";
    String SettingEthernetStaticIP="设置以太网静态IP:SetStaticIP()";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();//android11动态获取权限
        verifyStoragePermissions(this);//获取权限
        zcApi=new zcapi();
        zcApi.getContext(getApplicationContext());
        initView();
    }

    private void initView(){
        messageTextView=(TextView)findViewById(R.id.messageTextView);
        ListView listView;
        listView=(ListView) findViewById(R.id.APIListView);
        final MyAdapter mAdapter;

        mAdapter=new MyAdapter(this,getFunctionName());
        listView.setAdapter(mAdapter);//show the list

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this,String.valueOf(i)+":"+getFunctionName().get(i),Toast.LENGTH_SHORT).show();
                //the number of functionName is defined in getFunctionName()
                String function=getFunctionName().get(i);
                {
                    if(function.equals(shutDown)){//shutDown
                        showMessage("关机中");
                        zcApi.shutDown();
                    }
                    else if(function.equals(reboot)){//reboot
                        showMessage("重启中");
                        zcApi.reboot();
                    }
                    else if(function.equals(factoryReset)){//factoryReset
                        showMessage("恢复出厂设置中");
                        zcApi.factoryReset();
                    }
                    else if(function.equals(updateOta)){//updateOta
                        showMessage("OTA升级中");
                        zcApi.updateOta();
                    }
                    else if(function.equals(setStatusBar)){//setStatusBar
                        showMessage("状态栏:"+(statusBarEnable?"允许":"禁止"));
                        zcApi.setStatusBar(statusBarEnable);
                        statusBarEnable=!statusBarEnable;
                    }
                    else if(function.equals(setGestureStatusBar)){//setGestureStatusBar
                        showMessage("滑动呼出状态栏:"+(gestureStatusBarEnable?"允许":"禁止"));
                        zcApi.setGestureStatusBar(gestureStatusBarEnable);
                        gestureStatusBarEnable=!gestureStatusBarEnable;
                    }

                    else if(function.equals(setPowetOnOffTime)){//setPowetOnOffTime
                        showMessage("1 分钟内关机 ,3 分钟内启动");
                        Calendar calendar = Calendar.getInstance();
                        //获取系统的日期
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        boolean enable=true;//取消或者使能定时开关机
                        //当前1分钟后关机，3分钟后开机
                        int []onTime={year,month,day,hour,minute+3};
                        int []offTime={year,month,day,hour,minute+1};
                        zcApi.setPowetOnOffTime(enable,onTime,offTime);
                    }
                    else if(function.equals(getBuildModel)){//getBuildModel
                        String model=zcApi.getBuildModel();
                        showMessage("设备型号"+model);
                    }
                    else if(function.equals(getBuildSerial)){//getBuildSerial

                        String serial=zcApi.getBuildSerial();
                        //String serial = Build.getSerial();
                        showMessage("设备序列号:"+serial);
                    }
                    else if(function.equals(getEthMacAddress)){//getEthMacAddress
                        String EthMacAddress;
                        EthMacAddress=zcApi.getEthMacAddress();
                        showMessage("以太网MAC地址:"+EthMacAddress);
                    }
                    else if(function.equals(getWifiMacAddress)){//getWifiMacAddress
                        String WifiMacAddress;
                        WifiMacAddress=zcApi.getWifiMacAddress();
                        showMessage("wifi MAC地址:"+WifiMacAddress);
                    }
                    else if(function.equals(setLcdOnOff)){//setLcdOnOff
                        showMessage("背光1秒后关闭 2秒后开启:");
                        handler.postDelayed(CloseBackLightRunnable,1000);
                        handler.postDelayed(OpenBackLightRunnable,2000);
                        //zcApi.setLcdOnOff(false);
                    }
                    else if(function.equals(gpioControl)){//gpioControl
                        //goto the gpio ControlActivity,it is too complex
                        //Log.e("szl",getPropStr("ro.pinhe.board"));
                        if(getPropStr("ro.pinhe.board").equals("ZC-328E")){
                            Toast.makeText(getApplicationContext(),"328E旧主板GPIO丝印 1234顺序反过来：4321",Toast.LENGTH_LONG).show();
                        }
                        Intent intent = new Intent();
                        Bundle data = new Bundle();
                        data.putString("myinfo",getPropStr("ro.pinhe.board"));
                        intent.putExtra("data",data);        //发送选择到的主板名字到controlActivity
                        intent.setClass(MainActivity.this, ControlActivity.class);
                        startActivity(intent);
                    }
                    else if(function.equals(execShellCmd)){//execShellCmd
                        //create a file /sdcard/execShellCmd,we can see the file in /sdcard/
                        String cmd="touch /sdcard/execShellCmd";
                        showMessage("sd卡根目录中创建了新文件 /sdcard/execShellCmd");
                        zcApi.execShellCmd(cmd);
                    }
                    else if(function.equals(setSystemTime)){//setSystemTime
                        //year,month,day,hour,minute,the format must right!
                        int year=2020,month=11,day=24,hour=12,minute=12;
                        int time[]={year,month,day,hour,minute};
                        showMessage("设置系统时间:"+year+" "+month+" "+day+" "+hour+" "+minute);
                        zcApi.
                                setSystemTime(time);
                    }
                    else if(function.equals(watchDogEnable)){//watchDogEnable
                        showMessage("看门狗:"+(bwatchDogEnable?"开启":"关闭")+" 两分钟内不关闭看门狗或者不再次喂狗 系统将重启");
                        zcApi.watchDogEnable(bwatchDogEnable);
                        bwatchDogEnable=!bwatchDogEnable;
                    }
                    else if(function.equals(screenshot)){//screenshot
                        String path="/sdcard";
                        String fileName="screenshot.png";

                        showMessage("截屏成功!文件在: /sdcard/screenshot.png");
                        zcApi.screenshot(path,fileName);

                    }
                    else if(function.equals(InstallApk)){//InstallApk
                        //put the apk then install the apk in /sdcard/test.apk
                        boolean isFileExist = copyAssetsSingleFile("/sdcard","hello.apk");
                        if(isFileExist){
                            String path="/sdcard/hello.apk";
                            showMessage("普通安装应用");
                            zcApi.InstallApk(path,false);
                        }else{
                            showMessage("请给apk读写存储权限");
                        }

                    }else if(function.equals(InstallApkRoot)){//InstallApk
                        //put the apk then install the apk in /sdcard/test.apk
                        boolean isFileExist = copyAssetsSingleFile("/sdcard","hello.apk");
                        if(isFileExist){
                            String path="/sdcard/hello.apk";
                            showMessage("root安装应用中");
                            zcApi.InstallApk(path,true);
                        }else{
                            showMessage("请给apk读写存储权限");
                        }
                    }else if(function.equals(HDMIControl)){//InstallApk
                        showMessage("HDMI 1秒后关闭 2秒后开启(仅支持328、339系列)");
                        handler.postDelayed(HDMIOffRunnable,1000);
                        handler.postDelayed(HDMIOnRunnable,2000);
                    }else if (function.equals(SettingEthernetStaticIP)){
                        showMessage("开始设置以太网静态IP");
                        showDialog();
                    }
                }

            }
        });

    }



    Runnable OpenBackLightRunnable=new Runnable() {
        @Override
        public void run() {
            zcApi.setLcdOnOff(true);
            //开启背光就这一条指令
        }
    };

    Runnable CloseBackLightRunnable=new Runnable() {
        @Override
        public void run() {
            zcApi.setLcdOnOff(false);
            //关闭背光就这一条指令
        }
    };

    Runnable HDMIOnRunnable=new Runnable() {
        @Override
        public void run() {
            zcApi.setLcdOnOff(true,-1);
            //打开hdmi
        }
    };

    Runnable HDMIOffRunnable=new Runnable() {
        @Override
        public void run() {
            zcApi.setLcdOnOff(false,-1);
            //关闭hdmi
        }
    };

    private Handler handler = new Handler();               //开启一个handler任务


    private String getPropStr(String key) {
        Class<?> mClassType = null;
        Method mGetMethod = null;
        String value = "";
        try {
            if (mClassType == null) {
                mClassType = Class.forName("android.os.SystemProperties");
                mGetMethod = mClassType.getDeclaredMethod("get", String.class);
                value = (String) mGetMethod.invoke(mClassType, key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            value = "";
        }
        return value;
    }

    private boolean copyAssetsSingleFile(String outPath, String fileName) {
        File file = new File(outPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("--Method--", "copyAssetsSingleFile: cannot create directory.");
                return false;
            }
        }
        try {
            InputStream inputStream = getAssets().open(fileName);
            File outFile = new File(file, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            // Transfer bytes from inputStream to fileOutputStream
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = inputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            inputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showMessage(String message){
        messageTextView.setText("提示信息:"+message);
    }



    private  ArrayList<java.lang.String> getFunctionName(){
        ArrayList<String> functionNameList=new ArrayList<String>();
        functionNameList.add(shutDown);//
        functionNameList.add(reboot);
        functionNameList.add(factoryReset);
        functionNameList.add(updateOta);
        functionNameList.add(setStatusBar);
        functionNameList.add(setGestureStatusBar);//
        functionNameList.add(setPowetOnOffTime);
        functionNameList.add(getBuildModel);
        functionNameList.add(getBuildSerial);
        functionNameList.add(getEthMacAddress);
        functionNameList.add(getWifiMacAddress);//
        functionNameList.add(setLcdOnOff);
        functionNameList.add(gpioControl);
        functionNameList.add(execShellCmd);//
        functionNameList.add(setSystemTime);
        functionNameList.add(watchDogEnable);
        functionNameList.add(screenshot);
        functionNameList.add(InstallApk);
        functionNameList.add(InstallApkRoot);
        functionNameList.add(HDMIControl);
        functionNameList.add(SettingEthernetStaticIP);
        return functionNameList;
    }
    private void showDialog(){
        View view= LayoutInflater.from(this).inflate(R.layout.ethernet_dialog_layout,null,false);
        final AlertDialog dialog=new AlertDialog.Builder(this).setView(view).create();

        final EditText EdIp = view.findViewById(R.id.Ed_ip);
        final EditText EdGateway = view.findViewById(R.id.Ed_gateway);
        final EditText EdNetMask = view.findViewById(R.id.Ed_netMask);
        final EditText EdDns1 = view.findViewById(R.id.Ed_dns1);
        final EditText EdDns2 = view.findViewById(R.id.Ed_dns2);
        Button BuCancel=view.findViewById(R.id.Bu_cancel);
        Button BuConnect=view.findViewById(R.id.Bu_connect);
        //设定初始值（可在对话框中更改）
        EdIp.setText("192.168.3.12");
        EdGateway.setText("192.168.3.1");
        EdNetMask.setText("255.255.255.0");
        EdDns1.setText("8.8.8.8");
        EdDns2.setText("8.8.4.4");

        BuCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//关闭对话框
            }
        });
        BuConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddr=EdIp.getText().toString();
                String gateway=EdGateway.getText().toString();
                String netMask=EdNetMask.getText().toString();
                String dns1=EdDns1.getText().toString();
                String dns2=EdDns2.getText().toString();
                System.out.println("设置的IP"+ipAddr+"\n"+gateway+"\n"+netMask+"\n"+dns1+"\n"+dns2);
                Log.i("hsf111","设置的IP"+ipAddr+"\n"+gateway+"\n"+netMask+"\n"+dns1+"\n"+dns2);
                zcApi.SetStaticIP(ipAddr,gateway,netMask,dns1,dns2);
                dialog.dismiss();//关闭对话框
            }
        });
        dialog.show();

    }
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限

            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
//            int phonePermission=ActivityCompat.checkSelfPermission(activity,"android.permission.READ_PHONE_STATE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
//            if (phonePermission!=PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(activity, PERMISSIONS_PHONE,REQUEST_PHONE_STATE);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                //writeFile();
            } else {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION), REQUEST_CODE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //writeFile();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
            //writeFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //writeFile();
            } else {
                //ToastUtils.show("存储权限获取失败");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                //writeFile();
            } else {
                //ToastUtils.show("存储权限获取失败");
            }
        }
    }



}