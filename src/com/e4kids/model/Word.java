package com.e4kids.model;

public class Word {
	private long id; //primary key;
	private long topicId; // foreign key
	private String word;
	
	public Word() {
		// TODO Auto-generated constructor stub
	}

	public long getTopicId(){
		return topicId;
	}

	public void setTopicId(long topicId){
		this.topicId = topicId;
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public String getContent(){
		return word;
	}
	
	public void setContent(String word){
		this.word = word;
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		Word s=(Word)o;
		return (this.id==s.id);
	}

}
