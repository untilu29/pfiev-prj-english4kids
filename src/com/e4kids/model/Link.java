package com.e4kids.model;

public class Link {

	private long id;
	private String link;
	private String content;
	private long type;
	public Link() {
		// TODO Auto-generated constructor stub
	}
	
	public String getLink(){
		return link;
	}
	
	public void setLink(String link){
		this.link = link;
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public long getType(){
		return type;
	}
	public void setType(int type){
		this.type = type;
	}
	

}
