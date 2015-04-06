package com.smart.platform.model;

public class Device {
	
	protected long id;
	protected String name;
	protected String passwd;

	public Device(long id, String name, String passwd){
		this.id = id;
		this.name = name;
		this.passwd = passwd;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
}
