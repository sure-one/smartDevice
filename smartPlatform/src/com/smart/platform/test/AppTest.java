package com.smart.platform.test;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * Hello world!
 *
 */
public class AppTest{

	private final String topTopic        = "APP10086";
	private final String pubTopic = "APP10086/CMCCUser_P";
	private final String subTopic        = "APP10086/CMCCUser_R";
	private final int qos             = 2;
	private final String broker       = "tcp://localhost:61613";
	private final String clientId     = "app10086";
	private final String userName = "admin";
	private final String passWord = "password";
	private MqttClient inClient;
	private MqttClient outClient;
	
	public void init(){
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			inClient = new MqttClient(broker, clientId, persistence);
			outClient = new MqttClient(broker, clientId+"_w", persistence);
	        final MqttConnectOptions connOpts = new MqttConnectOptions();
	        connOpts.setCleanSession(true);
	        connOpts.setUserName(userName);  
	        connOpts.setPassword(passWord.toCharArray());
	        connOpts.setConnectionTimeout(10);  
	        connOpts.setKeepAliveInterval(10);  
	        inClient.setCallback(new MqttCallback() {  
                public void connectionLost(Throwable cause) {  
                    System.out.println("connectionLost-----------reconnecting....");
                    try {
                    	inClient.connect(connOpts);
						System.out.println("------------Reconnected");
					} catch(MqttException me) {
			            System.out.println("reason "+me.getReasonCode());
			            System.out.println("msg "+me.getMessage());
			            System.out.println("loc "+me.getLocalizedMessage());
			            System.out.println("cause "+me.getCause());
			            System.out.println("excep "+me);
			            me.printStackTrace();
			            System.out.println("Reconnect failed!");
			        }
                }  
                public void deliveryComplete(IMqttDeliveryToken arg0) {  
                    System.out.println("deliveryComplete---------"+arg0.isComplete());  
                }  
                public void messageArrived(String topic, MqttMessage message)  
                        throws Exception {  
                    System.out.println("message:"+message+", from: "+topic);  
                    receiveCommand(message.toString());
                }
            });
	        inClient.connect(connOpts);
	        outClient.connect(connOpts);
	        System.out.println("Connected");
            MqttMessage message = new MqttMessage("欢迎使用 APP 10086!请发送‘菜单’获取可运行命令".getBytes());
            message.setQos(qos);
            message.setRetained(true); 
            System.out.println("Send to topic:"+topTopic+" with message:"+message);
            outClient.publish(topTopic, message);
            inClient.subscribe(subTopic);
            System.out.println("App startup successfully!");
	        
		} catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
	}
	
	public String receiveCommand(String command){
		switch (command){
		case "菜单": getMenu();
		case "开机": startup();
		case "关机": shutdown();
		case "加热": heat();
		default: return "对不起，没有找到相应指令！";
		}
	}
	
	private void sendMessage(String message){

        MqttMessage MQTTResponse = new MqttMessage(message.getBytes());
        MQTTResponse.setQos(2);
		System.out.println("message:"+message+", to: "+pubTopic);  
		try {
			outClient.publish(pubTopic, MQTTResponse );
		} catch (MqttException e) {
			System.out.println("failed of message:"+message+", to: "+pubTopic);  
		}
	}
	
	private void getMenu(){
		String response = "欢迎使用：/n";
		sendMessage(response);
	}
	
	private void startup(){
		//TODO startup
		String response =  "开机操作已成功";
		sendMessage(response);
	}
	
	private void shutdown(){
		//TODO shutdown
		String response =  "关机失败，请重试！";
		sendMessage(response);
	}
	
	private void heat(){
		String response =  "开始加热，预计十分钟后加热完毕！";
		sendMessage(response);
	}
	
	public static void main(String... args){
		AppTest app = new AppTest();
		app.init();
	}
	
}
