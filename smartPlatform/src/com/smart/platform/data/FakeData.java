package com.smart.platform.data;

import java.util.HashMap;

import com.smart.platform.model.Device;
import com.smart.platform.model.User;

public class FakeData {

	private static HashMap<Long,User> users;
	private static HashMap<Long, Device> devices;

	public static HashMap<Long,User> getFakeUsers(){
		if(users!=null) {
			return users;
		}
		users = new HashMap<Long,User>();
		users.put(10000l, new User(10000, "admin", "admin"));
		users.put(10001l, new User(10001, "admin1", "admin1"));
		users.put(10002l, new User(10002, "admin2", "admin2"));
		users.put(10003l, new User(10003, "admin3", "admin3"));
		return users;
	}
	
	public static HashMap<Long, Device> getFakeDevices(){
		if(devices!=null) {
			return devices;
		}
		devices = new HashMap<Long,Device>();
		devices.put(10000l, new Device(10000, "洗衣机", "xiyiji"));
		devices.put(10001l, new Device(10001, "冰箱", "bingxiang"));
		devices.put(10002l, new Device(10002, "热水器", "reshuiqi"));
		devices.put(10003l, new Device(10003, "空调", "kongtiao"));
		return devices;
	}
}
