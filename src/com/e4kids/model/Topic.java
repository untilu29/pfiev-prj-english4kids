package com.e4kids.model;

public class Topic {
	private long id; //primary key
	private String topic;	
	public Topic(){
	}

	public String getName(){
		return topic;
	}
	
	public void setName(String topic){
		this.topic = topic;
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
}
