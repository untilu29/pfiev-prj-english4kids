package com.e4kids.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.RadioGroup.OnCheckedChangeListener;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.e4kids.dao.WordDAO;
import com.e4kids.helper.ImageManager;
import com.e4kids.model.Word;
import com.example.english4kids.R;
import com.example.english4kids.TrueFalseGameActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;


public class TrueFalseGameAdapter extends BaseAdapter {

	private TextToSpeech tts;
	private Context mContext;
	private List<Word> mItems;
	private boolean[] correct=new boolean[100];
	private static List<Boolean> checkCorrect= new ArrayList<Boolean>();
	private static LayoutInflater inflater = null;
    private Holder holder; 
    private int num;
    private boolean flagresume = true;
    private Set setCheck;
    private Map<Integer,Boolean>mapCheck = new HashMap();
    public List<RadioGroup> radioList;
	public TrueFalseGameAdapter(Context context, List<Word> items) {
		this.mContext = context;
		this.mItems = items;
		this.radioList = new ArrayList<RadioGroup>();
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		tts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status != TextToSpeech.ERROR) {
					tts.setLanguage(Locale.US);
				}
			}
		});
	}

	public boolean[] getCorrect() {
		return correct;
	}
	
	public List<Boolean> getCheckCorrect() {
		return checkCorrect;
	}
	
	@Override
	public int getCount() {
		return (getItems() != null && !getItems().isEmpty() ? getItems().size() : 0);
	}

	@Override
	public Word getItem(int position) {
		return (getItems() != null && !getItems().isEmpty() ? getItems().get(position) : null);
	}

	@Override
	public long getItemId(int position) {
		return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
	}

	public List<Word> getItems() {
		return mItems;
	}

	public void setItems(List<Word> items) {
		this.mItems = items;
	}

	public class Holder {
		ImageView speaker;
		ImageView pic;
		RadioGroup radioGroup;
	}

	private int dpToPx(int dp) {
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	    holder = new Holder();
		View rowView;
		rowView = inflater.inflate(R.layout.truefalse_item, null);

		// Canh chỉnh view cho đúng 3 hàng và sát với lề
		int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
		rowView.setMinimumHeight((screenHeight - dpToPx(55)) / 3);
		rowView.canScrollVertically(0);
		// -----------------------------------------

		holder.speaker = (ImageView) rowView.findViewById(R.id.speakitem);
		holder.pic = (ImageView) rowView.findViewById(R.id.contentimg);

		// Đưa nội dung vào mỗi adapter
		holder.speaker.setImageResource(R.drawable.sound_icon);
		final Word word = getItem(position);
		holder.pic.setImageResource(ImageManager.getDrawableId("word" + 3 + "_" + word.getId(), mContext));

		holder.radioGroup = (RadioGroup) rowView.findViewById(R.id.radioGroup);

		if (getRandomBoolean()) {
			correct[(int) word.getId()] = true;
			holder.speaker.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tts.speak(word.getContent(), TextToSpeech.QUEUE_FLUSH, null);
				}
			});
		} else {
			correct[(int) word.getId()] = false;
			WordDAO wordDao = new WordDAO(mContext);
			List<Word> listwords = new ArrayList<Word>();
			listwords = wordDao.getAllWords();
			final Word randomWord = generateRandomExclude(word, listwords);
			holder.speaker.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tts.speak(randomWord.getContent(), TextToSpeech.QUEUE_FLUSH, null);
				}
			});
		}
		
		radioList.add(holder.radioGroup);
		holder.radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
			
				if(flagresume){
				if (checkedId==R.id.game_btn_true){
					//checkCorrect.add(correct[(int)word.getId()]==true);
					mapCheck.put(position, correct[(int)word.getId()]==true);
				}
				if (checkedId==R.id.game_btn_false){
					//checkCorrect.add(correct[(int)word.getId()]==false);
					mapCheck.put(position, correct[(int)word.getId()]==false);
				}
				
				if(mapCheck.size()==3 ){
					new ResultOperation().execute();
					
				}
				}
			}
		});
	
		// holder.radioGroup.setOn

		// rowView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Toast.makeText(context, "You Clicked "+result[position],
		// Toast.LENGTH_LONG).show();
		// }
		// });
		return rowView;
	}

	public Word generateRandomExclude(Word exclude, List<Word> listword) {
		Word random = listword.get(new Random().nextInt(listword.size()));
		while (exclude.equals((random))) {
			random = listword.get(new Random().nextInt(listword.size()));
		}
		return random;
	}

	public boolean getRandomBoolean() {
		Random random = new Random();
		return random.nextBoolean();
	}
	
	private class ResultOperation extends AsyncTask<String, Void, String> {

	    private int numRight = 0;
	 
        @Override
        protected String doInBackground(String... params) {
		
        	flagresume = false;
				for (int i = 0; i < mapCheck.size(); i++) {
					if (mapCheck.get(i) == true) {
						numRight++;
					}
				}
			return "";
        }

        @Override
        protected void onPostExecute(String result) {
        	synchronized(mapCheck){
    		new AlertDialog.Builder(mContext)
		    .setTitle("Message")
		    .setMessage("Number right answer: "+numRight)
		    .setPositiveButton("Next", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
//		        	int pid = android.os.Process.myPid();
//			        android.os.Process.killProcess(pid);
			        
			     //   System.exit(0);
		      		
		    		Intent newscreen = new Intent(mContext,TrueFalseGameActivity.class);
		    		mContext.startActivity(newscreen);
			       
		        }
		     })
		    .setNegativeButton("Resume", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        	for(RadioGroup rg : radioList){
		        	     rg.clearCheck();
		        	}
		        	mapCheck.clear();
		        	flagresume = true;
		        }  
		     })
		    //.setIcon(android.R.drawable.)
		     .show();
        	}
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
