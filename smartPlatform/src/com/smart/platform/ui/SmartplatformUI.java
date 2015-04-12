package com.smart.platform.ui;

import javax.servlet.annotation.WebServlet;

import com.smart.platform.service.MessageService;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("smartplatform")
@Push
public class SmartplatformUI extends UI{

	String inTopic = "APP10086/CMCCUser_P";
	String outTopic = "APP10086/CMCCUser_R";
	MessageService service = new MessageService(outTopic,inTopic);
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = SmartplatformUI.class)
	public static class Servlet extends VaadinServlet {
	}
	
	VerticalLayout layout;
	@Override
	protected void init(VaadinRequest request) {
		layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSizeFull();
		setContent(layout);

		
		final TextField tf = new TextField("Send your message");
		FormLayout form = new FormLayout();
		Button button = new Button("Send");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				service.sendMessage(tf.getValue());;
			}
		});
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

}