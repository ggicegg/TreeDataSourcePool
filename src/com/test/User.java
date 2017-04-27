package com.test;

public class User {
	private String id;
	private String name;
	private String money;
	
	
	
	public User() {
		
	}
	public User(String id, String name, String money) {
		super();
		this.id = id;
		this.name = name;
		this.money = money;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String message = getId()+":"+getName()+":"+getMoney();
		return message;
	}
	
	

}
