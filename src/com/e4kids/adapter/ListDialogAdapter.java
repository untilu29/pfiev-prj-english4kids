package com.e4kids.adapter;

import java.util.List;

import com.e4kids.helper.ImageManager;
import com.e4kids.model.Link;
import com.example.english4kids.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListDialogAdapter extends BaseAdapter {

	private Context mContext;
	private List<Link>mItems;
	private static LayoutInflater inflater=null;
	public ListDialogAdapter(Context context, List<Link> items) {
		this.mContext = context;
		this.mItems = items;
		inflater = ( LayoutInflater )mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return (getItems()!=null&&!getItems().isEmpty()?getItems().size():0);
	}

	@Override
	public Link getItem(int position) {
		return (getItems()!=null&&!getItems().isEmpty()?getItems().get(position):null);
	}

	@Override
	public long getItemId(int position) {
		return (getItems()!=null&&!getItems().isEmpty())?getItems().get(position).getId():position;
	}

	public List<Link> getItems(){
		return mItems;
	}
	
	public void setItems(List<Link> items){
		this.mItems = items;
	}
	
	
	public class Holder{
		TextView tv;
		ImageView img;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder=new Holder();
	    View rowView;       
	    rowView = inflater.inflate(R.layout.dialogitem, null);
	    holder.tv=(TextView) rowView.findViewById(R.id.txtitem);
	    holder.img=(ImageView) rowView.findViewById(R.id.imgitem);      
	    
	    Link link = getItem(position);
        holder.tv.setText(link.getContent());
        holder.img.setImageResource(ImageManager.getDrawableId("dialog1", mContext));  //+link.getId();
        
//	         rowView.setOnClickListener(new OnClickListener() {            
//	            @Override
//	            public void onClick(View v) {
//	                // TODO Auto-generated method stub
//	                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
//	            }
//	        });   
	        return rowView;
	}

}
