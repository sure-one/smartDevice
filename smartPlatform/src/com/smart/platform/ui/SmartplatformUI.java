package com.smart.platform.ui;

import javax.servlet.annotation.WebServlet;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.smart.platform.service.MessageService;
import com.sun.istack.internal.logging.Logger;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("smartplatform")
@Push
public class SmartplatformUI extends UI implements MqttCallback{
	private static Logger logger = Logger.getLogger(SmartplatformUI.class);
	String inTopic = "APP10086/CMCCUser_P";
	String outTopic = "APP10086/CMCCUser_R";
	MessageService service = new MessageService(outTopic,inTopic);
	private static int i = 0;
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = SmartplatformUI.class)
	public static class Servlet extends VaadinServlet {
	}
	
	VerticalLayout layout;
//	VerticalLayout panel;
	Table table;
	@Override
	protected void init(VaadinRequest request) {
		service.setInclientCallback(this);
		layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSizeFull();
		setContent(layout);

		table = new Table();
		table.addContainerProperty("Name", String.class, null);
		table.addContainerProperty("Mag", String.class, null);
		table.setPageLength(table.size());
		table.setHeight("300px");
//		panel = new VerticalLayout();
//		panel.setMargin(false);
//		panel.setSizeFull();;
//		panel.setSpacing(false);
		final TextField tf = new TextField("Send your message");
		Button button = new Button("Send");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				service.sendMessage(tf.getValue());
//				panel.addComponent(new Label(tf.getValue()),i++);
//				Object newItemId = table.addItem();
//				Item row1 = table.getItem(newItemId);
//				row1.getItemProperty("Name").setValue("App10086");
//				row1.getItemProperty("Mag").setValue(tf.getValue());
				table.addItem(new Object[]{"My", tf.getValue()}, ++i);
				
			}
		});
		layout.addComponent(table);
		layout.addComponent(tf);
		layout.addComponent(button);
//		final MessageService service = new MessageService("sample", layout);
	}
	
	class FeederThread extends Thread {
		
		@Override
		public void run(){
			System.out.println("------------------------Running!");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void connectionLost(Throwable arg0) {
		System.out.println("connectionLost-----------reconnecting....");
//        service.reconnectInclient();
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage response) throws Exception {
		logger.info("收到消息:"+response.toString());
//		this.panel.addComponent(new Label(response.toString()));
		table.addItem(new Object[]{"App10086", response.toString()}, ++i);
	}

}