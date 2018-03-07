package com.example.smartm0012;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

public static Handler mainHandler;
private Button setting;
protected ClientThread rxListenerThread;
private Button exit;
private String tem ;
private TextView modelview;
private boolean flag =true;
private Button light_on;
private Button light_off;
private TextView textTips;
private TextView temp;
private TextView shuiwei;

private TextView hum;
private TextView light;
private TextView smoke;
private TextView rain;
private Button light2_on;
private Button light2_off;
private String s_tem;
private String s_hum;
private String s_light;
private String s_smoke;




@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_main);
	init();
	initMainHandler();
		 
}



/**
 * 发送消息
 */
void sendMessage(String str){
	
	//通知客户端线程 发送消息
	Message message = ClientThread.childHandler.obtainMessage(0, str);  //0为子线程开启输出流
	ClientThread.childHandler.sendMessage(message);
	
	
	
}


private void initMainHandler() {
	 mainHandler = new Handler() {
			@Override
            /**
             * 主线程消息处理中心
             */
            public void handleMessage(Message msg) {
            	switch (msg.what) {
				case 0:
					
					Toast.makeText(MainActivity.this, "链接成功", 2000).show();
					break;
					
              case 2://处理消息并显示
            	     String data = (String)msg.obj;
            	     if (!(data=="")) {
            	     handledata(data);
					}
					break;
                case 1:
				
				Toast.makeText(MainActivity.this, "无法连接到服务器", 2000).show();
				break;
     	
	             case 3:
			
			Toast.makeText(MainActivity.this, "与服务器断开链接", 2000).show();
			break;            	
            	}	
                
            }
 
        };
	
	
}
	
protected void handledata(String data) {
	System.out.println(data);
	
	
	//温
	s_tem=data.substring(1,3);
	//湿度
	s_hum=data.substring(3,5);
	//光
	s_light=data.substring(6,8);
	//烟雾
	s_smoke=data.substring(9,11);
	
	
	temp.setText(s_tem+"℃");
	hum.setText(s_hum+"%");
	light.setText(s_light+"%");
	smoke.setText(s_smoke+"%");
	if ("Y".equals((data.substring(11, 12)))) {
		rain.setText("下雨");
	
	}
	else{
		rain.setText("无雨");
	}
	
	}
	
	
	

private void init() {
	//温度
	temp=(TextView) findViewById(R.id.temp);

	//湿度
	hum=(TextView) findViewById(R.id.hum);
	//光
	light=(TextView) findViewById(R.id.light);
	//烟雾
	smoke=(TextView) findViewById(R.id.smoke);
	
	//rain
	rain=(TextView) findViewById(R.id.rain);
	//开1
	light_on=(Button)findViewById(R.id.light1_on);
	light_on.setOnClickListener(this);
	
	
	
	//关1
	light_off=(Button)findViewById(R.id.light1_off);
	light_off.setOnClickListener(this);

	//开2
	light2_on=(Button)findViewById(R.id.light2_on);
	light2_on.setOnClickListener(this);
	
		
	//关2
	light2_off=(Button)findViewById(R.id.light2_off);
	light2_off.setOnClickListener(this);
	
	
	
	//连接网络
	setting =(Button) findViewById(R.id.btn_network);
	setting.setOnClickListener(this);
	
	//退出
	exit =(Button) findViewById(R.id.btn_exit);
	exit.setOnClickListener(this);
    

	
	//提示
	textTips=(TextView) findViewById(R.id.tips);
}




@Override
public void onClick(View v) {
	if (rxListenerThread == null
			&& (v.getId() != R.id.btn_exit) && (v.getId() != R.id.btn_network) 
			) {
		textTips.setText("提示信息：请先连接网络");
		return;
	}
	switch (v.getId()) {
	
	case R.id.btn_network://网络连接
		
		showDialog(MainActivity.this);
		break;
    case R.id.btn_exit://退出
    	Message message = ClientThread.childHandler.obtainMessage(1);  //1为子线程开退出
    	ClientThread.childHandler.sendMessage(message);
    	finish();
    	
		break;
  
    case R.id.light1_on:
    	 
    	sendMessage("S@"); //开灯
    	
    	break;
    	
    case R.id.light1_off:
   	 
    	sendMessage("S#"); //关灯
    	
    	break;
    case R.id.light2_on:
   	 
    	sendMessage("Z@"); //开灯
    	
    	break;
    	
    case R.id.light2_off:
   	 
    	sendMessage("Z#"); //关灯
    	
    	break;	
   
	default:
		break;
	}
	
}






//显示连接对话框，以前链接wifi需要。
	private void showDialog(Context context) {
		LayoutInflater mInflater = (LayoutInflater) context  
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		View view = mInflater.inflate(R.layout.recordlayout, null);  
		LinearLayout layout = (LinearLayout) view  
		    .findViewById(R.id.id_recordlayout);


	    TextView tv1 = new TextView(context);  
	    tv1.setText("IP:");  
	    final EditText editIP = new EditText(context); 
	    editIP.setText("123.207.63.194");  
	    layout.addView(tv1);  
	    layout.addView(editIP);  

	    TextView tv2 = new TextView(context);  
	    tv2.setText("端口:");
	    final EditText editPort = new EditText(context);  
		editPort.setText("8081");  
	    layout.addView(tv2);  
	    layout.addView(editPort);  	


		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("服务器设置");
		builder.setView(view);
		builder.setPositiveButton("连接", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String strIpAddr = editIP.getText().toString();
				int iPort=Integer.parseInt(editPort.getText().toString());
				boolean ret = isIPAddress(strIpAddr);

				if (ret) {
					//textTips.setText("IP地址:" + strIpAddr + ",端口:"+iPort);
				} else {
					Toast.makeText(MainActivity.this, "地址不合法，请重新设置", 2000).show();
					//textTips.setText("地址不合法，请重新设置");
					return;
				}
				rxListenerThread = new ClientThread(strIpAddr, iPort);//建立客户端线程
				rxListenerThread.start();
				//clientThread = new ClientThread(strIpAddr, iPort);//建立客户端线程
				//clientThread.start();
								
//				if(clientThread != null && clientThread.socketConnect()){
//					textTips.setText("start timer");
//					mainTimer = new Timer();//定时查询所有终端信息
//					setTimerTask();
//				}
			}
		});
		builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				/*if (clientThread != null) {
					MainMsg = ClientThread.childHandler
							.obtainMessage(ClientThread.RX_EXIT);
					ClientThread.childHandler.sendMessage(MainMsg);
					//textTips.setText("与服务器断开连接");
				}*/
			}
		});

		builder.show();
	}
	//判断输入IP是否合法
	private boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		Pattern pattern = Pattern
				.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
	}



	
}