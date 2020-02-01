package com.e4kids.adapter;

import java.util.List;

import com.e4kids.adapter.ListDialogAdapter.Holder;
import com.e4kids.helper.ImageManager;
import com.e4kids.model.Link;
import com.e4kids.model.Word;
import com.example.english4kids.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ListWordAdapter extends BaseAdapter {
	private List<Word>mItems;
	private Context mContext;
	private long topicId;
	private static LayoutInflater inflater=null;
	public ListWordAdapter(Context context, List<Word>listWords,long topicId) {
		this.mItems = listWords;
		this.mContext = context;
		this.topicId = topicId;
		inflater = ( LayoutInflater )mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return (getItems()!=null&&!getItems().isEmpty())?getItems().size():0;
	}

	@Override
	public Word getItem(int position) {
		return (getItems()!=null&&!getItems().isEmpty())?getItems().get(position):null;
	}

	@Override
	public long getItemId(int position) {
		return (getItems()!=null&&!getItems().isEmpty())?getItems().get(position).getId():position;
	}
	
	public List<Word> getItems(){
		return mItems;
	}
	
	public void setItems(List<Word> listWords){
		this.mItems = listWords;
	}

	public class Holder{
		TextView tv;
		ImageView img;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		  //  ImageView imageView;
//	      
			Holder holder=new Holder();
		    View rowView;       
		    rowView = inflater.inflate(R.layout.worditem, null);
		    holder.tv=(TextView) rowView.findViewById(R.id.wordtxtitem);
		    holder.img=(ImageView) rowView.findViewById(R.id.wordimgitem);      
		    
		    long id = mItems.get(position).getId();
		    Word word = getItem(position);
	        holder.tv.setText(word.getContent());
	        holder.img.setImageResource(ImageManager.getDrawableId("word"+topicId+"_"+id,mContext)); 
//	      if (convertView == null) {
//	         imageView = new ImageView(mContext);
//	         imageView.setLayoutParams(new GridView.LayoutParams(130, 130));
//	         imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//	         imageView.setPadding(8, 8, 8, 8);
//	      } 
//	      else 
//	      {
//	         imageView = (ImageView) convertView;
//	      }
//	   
//	      long id = mItems.get(position).getId();
//	      imageView.setImageResource(ImageManager.getDrawableId("word"+topicId+"_"+id,mContext));
//	      return imageView;
	        return rowView;
	}

}


