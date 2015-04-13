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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
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
	Table contactTable = new Table();
	Table messageTable = new Table();
	final TextField searchField = new TextField();
	Button sendButton = new Button("发送");
	@Override
	protected void init(VaadinRequest request) {
		service.setInclientCallback(this);

		
		searchField.setInputPrompt("Input your message");
		sendButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				service.sendMessage(searchField.getValue());
				messageTable.addItem(new Object[]{"","",searchField.getValue(),"My"}, ++i);
			}
		});
		
        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        setContent(splitPanel);

        VerticalLayout leftLayout = new VerticalLayout();
        splitPanel.addComponent(leftLayout);
        splitPanel.addComponent(contactTable);
        leftLayout.addComponent(messageTable);
        HorizontalLayout bottomLeftLayout = new HorizontalLayout();
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(searchField);
        bottomLeftLayout.addComponent(sendButton);


        leftLayout.setSizeFull();
        leftLayout.setMargin(true);

        leftLayout.setExpandRatio(messageTable, 1);
        messageTable.setSizeFull();
//        messageTable.setStyleName("messageTable");

        bottomLeftLayout.setWidth("100%");
        searchField.setWidth("100%");
        bottomLeftLayout.setExpandRatio(searchField, 1);


       
		messageTable.addContainerProperty("Device", String.class, null);
		messageTable.addContainerProperty("Message1", String.class, null);
		messageTable.addContainerProperty("Message2", String.class, null);
		messageTable.addContainerProperty("Me", String.class, null);
		messageTable.setPageLength(messageTable.size());
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
		messageTable.addItem(new Object[]{"App10086", response.toString(),"",""}, ++i);
	}

}