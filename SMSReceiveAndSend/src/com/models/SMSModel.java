package com.models;

public class SMSModel {

	public SMSModel() {
		// TODO Auto-generated constructor stub
	}

	
	public SMSModel(String senderNumber, String date, String message, String id) {
		super();
		this.senderNumber = senderNumber;
		this.date = date;
		this.message = message;
		this.id = id;
	}


	private String senderNumber;
	private String date;
	private String message;
	private String id;

	public String getSenderNumber() {
		return senderNumber;
	}

	public void setSenderNumber(String senderNumber) {
		this.senderNumber = senderNumber;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public String getId() {
		return id;
	}


	public void setId(String string) {
		this.id = string;
	}
}
