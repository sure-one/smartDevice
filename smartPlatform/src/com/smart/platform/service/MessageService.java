package com.smart.platform.service;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.smart.platform.constant.MqttConfig;
import com.sun.istack.internal.logging.Logger;


public class MessageService {
	private static Logger logger = Logger.getLogger(MessageService.class);
	private static MqttClient inClient;
	private static MqttClient outClient;
	private static MqttConnectOptions connOpts;
	private final String inTopic;
	private final String outTopic;
	
	public MessageService(String sendTopic, String receiveTopic){
		logger.info("Start up MessageService");
		inTopic = receiveTopic;
		outTopic = sendTopic;
		try {
			inClient = new MqttClient(MqttConfig.url, MqttConfig.clientId+"_in", new MemoryPersistence());
			outClient = new MqttClient(MqttConfig.url, MqttConfig.clientId, new MemoryPersistence());
	        connOpts = new MqttConnectOptions();
	        connOpts.setCleanSession(false);
	        connOpts.setUserName(MqttConfig.userName);  
	        connOpts.setPassword(MqttConfig.passWord.toCharArray());  
	        inClient.connect(connOpts);
	        outClient.connect(connOpts);
	        logger.info("连接成功");
	        inClient.subscribe(inTopic);
	        inClient.setCallback(new MqttCallback(){

				@Override
				public void connectionLost(Throwable arg0) {
					logger.info("Connection lost, reconnecting...");
					try {
						inClient.connect(connOpts);
						logger.info("Reconnected!");
					} catch (Exception e) {
						logger.severe("Failed to connect Apollo!");
						logger.logSevereException(e);
					}
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					String status = token.isComplete()?"成功":"失败";
					logger.info("发送成功");
//					layout.addComponent(new Label("发送"+status));
				}

				@Override
				public void messageArrived(String topic, MqttMessage message)
						throws Exception {
					logger.info("收到消息:"+message.toString());
//					layout.addComponent(new Label(message.toString()));
				}
	        	
	        });
		} catch (Exception e) {
			logger.logSevereException(e);
		}
	}
	
	public void sendMessage(String message){
		MqttMessage m = new MqttMessage(message.getBytes());
		m.setQos(2);
		m.setRetained(false);
		try {
			outClient.publish(outTopic, m);
		} catch (Exception e) {
			logger.severe("Failed to send message: "+message);
			e.printStackTrace();
		}
	}
}
