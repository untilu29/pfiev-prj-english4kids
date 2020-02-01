package com.e4kids.adapter;

import java.util.List;

import com.e4kids.adapter.ListWordAdapter.Holder;
import com.e4kids.helper.ImageManager;
import com.e4kids.model.Topic;
import com.e4kids.model.Word;
import com.example.english4kids.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


public class ListTopicAdapter extends BaseAdapter {

	private List<Topic>mItems;
	private Context mContext;
	private static LayoutInflater inflater=null;
	public ListTopicAdapter(Context context, List<Topic> listTopics) {
		// TODO Auto-generated constructor stub
		this.mItems = listTopics;
		this.mContext = context;
		inflater = ( LayoutInflater )mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (getItems()!=null&&!getItems().isEmpty())?getItems().size():0;
	}

	@Override
	public Topic getItem(int position) {
		// TODO Auto-generated method stub
		return (getItems()!=null&&!getItems().isEmpty())?getItems().get(position):null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return (getItems()!=null&&!getItems().isEmpty())?getItems().get(position).getId():position;
	}
	
	public List<Topic> getItems(){
		return mItems;
	}
	
	public void setItems(List<Topic> items){
		this.mItems = items;
	}

	public class Holder{
		TextView tv;
		ImageView img;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		 ImageView imageView;
//	      
//	      if (convertView == null) {
//	         imageView = new ImageView(mContext);
//	         imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
//	         imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//	         imageView.setPadding(8, 8, 8, 8);
//	      } 
//	      else 
//	      {
//	         imageView = (ImageView) convertView;
//	      }
//	   
//	      long id = mItems.get(position).getId();
//	      imageView.setImageResource(ImageManager.getDrawableId("topic"+id, mContext));
//	      return imageView;
//	      
	        
	        Holder holder=new Holder();
		    View rowView;       
		    rowView = inflater.inflate(R.layout.worditem, null);
		    holder.tv=(TextView) rowView.findViewById(R.id.wordtxtitem);
		    holder.img=(ImageView) rowView.findViewById(R.id.wordimgitem);      
		    
		    long id = mItems.get(position).getId();
		    Topic topic = getItem(position);
	        holder.tv.setText(topic.getName());
	        holder.img.setImageResource(ImageManager.getDrawableId("topic"+id, mContext));
	        return rowView;
	}
   
}
