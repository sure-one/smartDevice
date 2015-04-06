package com.smart.platform.service;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.smart.platform.constant.MqttConfig;
import com.sun.istack.internal.logging.Logger;


public class MessageService {
	private static Logger logger = Logger.getLogger(MessageService.class);
	private static MqttClient client;
	private static MqttConnectOptions connOpts;
	
	public MessageService(MqttConfig config){
		logger.info("启动MessageService");
		try {
			client = new MqttClient(config.url, config.clientId, new MemoryPersistence());
	        connOpts = new MqttConnectOptions();
	        connOpts.setCleanSession(false);
	        connOpts.setUserName(config.userName);  
	        connOpts.setPassword(config.passWord.toCharArray());  
	        client.connect(connOpts);
	        client.setCallback(new MqttCallback(){

				@Override
				public void connectionLost(Throwable arg0) {
					try {
						client.connect(connOpts);
					} catch (MqttException e) {
						logger.severe("Failed to connect Apollo!");
						logger.logSevereException(e);
					}
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					logger.fine("DeliveryComplete---------"  + token.isComplete());
				}

				@Override
				public void messageArrived(String arg0, MqttMessage arg1)
						throws Exception {
					// TODO Auto-generated method stub
					
				}
	        	
	        });
		} catch (MqttException e) {
			logger.logSevereException(e);
		}
	}
	
}
