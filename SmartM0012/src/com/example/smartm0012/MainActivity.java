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
 * ������Ϣ
 */
void sendMessage(String str){
	
	//֪ͨ�ͻ����߳� ������Ϣ
	Message message = ClientThread.childHandler.obtainMessage(0, str);  //0Ϊ���߳̿��������
	ClientThread.childHandler.sendMessage(message);
	
	
	
}


private void initMainHandler() {
	 mainHandler = new Handler() {
			@Override
            /**
             * ���߳���Ϣ��������
             */
            public void handleMessage(Message msg) {
            	switch (msg.what) {
				case 0:
					
					Toast.makeText(MainActivity.this, "���ӳɹ�", 2000).show();
					break;
					
              case 2://������Ϣ����ʾ
            	     String data = (String)msg.obj;
            	     if (!(data=="")) {
            	     handledata(data);
					}
					break;
                case 1:
				
				Toast.makeText(MainActivity.this, "�޷����ӵ�������", 2000).show();
				break;
     	
	             case 3:
			
			Toast.makeText(MainActivity.this, "��������Ͽ�����", 2000).show();
			break;            	
            	}	
                
            }
 
        };
	
	
}
	
protected void handledata(String data) {
	System.out.println(data);
	
	
	//��
	s_tem=data.substring(1,3);
	//ʪ��
	s_hum=data.substring(3,5);
	//��
	s_light=data.substring(6,8);
	//����
	s_smoke=data.substring(9,11);
	
	
	temp.setText(s_tem+"��");
	hum.setText(s_hum+"%");
	light.setText(s_light+"%");
	smoke.setText(s_smoke+"%");
	if ("Y".equals((data.substring(11, 12)))) {
		rain.setText("����");
	
	}
	else{
		rain.setText("����");
	}
	
	}
	
	
	

private void init() {
	//�¶�
	temp=(TextView) findViewById(R.id.temp);

	//ʪ��
	hum=(TextView) findViewById(R.id.hum);
	//��
	light=(TextView) findViewById(R.id.light);
	//����
	smoke=(TextView) findViewById(R.id.smoke);
	
	//rain
	rain=(TextView) findViewById(R.id.rain);
	//��1
	light_on=(Button)findViewById(R.id.light1_on);
	light_on.setOnClickListener(this);
	
	
	
	//��1
	light_off=(Button)findViewById(R.id.light1_off);
	light_off.setOnClickListener(this);

	//��2
	light2_on=(Button)findViewById(R.id.light2_on);
	light2_on.setOnClickListener(this);
	
		
	//��2
	light2_off=(Button)findViewById(R.id.light2_off);
	light2_off.setOnClickListener(this);
	
	
	
	//��������
	setting =(Button) findViewById(R.id.btn_network);
	setting.setOnClickListener(this);
	
	//�˳�
	exit =(Button) findViewById(R.id.btn_exit);
	exit.setOnClickListener(this);
    

	
	//��ʾ
	textTips=(TextView) findViewById(R.id.tips);
}




@Override
public void onClick(View v) {
	if (rxListenerThread == null
			&& (v.getId() != R.id.btn_exit) && (v.getId() != R.id.btn_network) 
			) {
		textTips.setText("��ʾ��Ϣ��������������");
		return;
	}
	switch (v.getId()) {
	
	case R.id.btn_network://��������
		
		showDialog(MainActivity.this);
		break;
    case R.id.btn_exit://�˳�
    	Message message = ClientThread.childHandler.obtainMessage(1);  //1Ϊ���߳̿��˳�
    	ClientThread.childHandler.sendMessage(message);
    	finish();
    	
		break;
  
    case R.id.light1_on:
    	 
    	sendMessage("S@"); //����
    	
    	break;
    	
    case R.id.light1_off:
   	 
    	sendMessage("S#"); //�ص�
    	
    	break;
    case R.id.light2_on:
   	 
    	sendMessage("Z@"); //����
    	
    	break;
    	
    case R.id.light2_off:
   	 
    	sendMessage("Z#"); //�ص�
    	
    	break;	
   
	default:
		break;
	}
	
}






//��ʾ���ӶԻ�����ǰ����wifi��Ҫ��
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
	    tv2.setText("�˿�:");
	    final EditText editPort = new EditText(context);  
		editPort.setText("8081");  
	    layout.addView(tv2);  
	    layout.addView(editPort);  	


		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("����������");
		builder.setView(view);
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String strIpAddr = editIP.getText().toString();
				int iPort=Integer.parseInt(editPort.getText().toString());
				boolean ret = isIPAddress(strIpAddr);

				if (ret) {
					//textTips.setText("IP��ַ:" + strIpAddr + ",�˿�:"+iPort);
				} else {
					Toast.makeText(MainActivity.this, "��ַ���Ϸ�������������", 2000).show();
					//textTips.setText("��ַ���Ϸ�������������");
					return;
				}
				rxListenerThread = new ClientThread(strIpAddr, iPort);//�����ͻ����߳�
				rxListenerThread.start();
				//clientThread = new ClientThread(strIpAddr, iPort);//�����ͻ����߳�
				//clientThread.start();
								
//				if(clientThread != null && clientThread.socketConnect()){
//					textTips.setText("start timer");
//					mainTimer = new Timer();//��ʱ��ѯ�����ն���Ϣ
//					setTimerTask();
//				}
			}
		});
		builder.setNeutralButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				/*if (clientThread != null) {
					MainMsg = ClientThread.childHandler
							.obtainMessage(ClientThread.RX_EXIT);
					ClientThread.childHandler.sendMessage(MainMsg);
					//textTips.setText("��������Ͽ�����");
				}*/
			}
		});

		builder.show();
	}
	//�ж�����IP�Ƿ�Ϸ�
	private boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		Pattern pattern = Pattern
				.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
	}



	
}