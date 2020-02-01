package com.example.english4kids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.e4kids.dao.WordInfoDB;
import com.e4kids.helper.AppPreferences;
import com.e4kids.helper.MyCountDownTimer;
import com.e4kids.helper.Utility;
import com.e4kids.model.LetterInfo;
import com.e4kids.model.WordInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LetterGameActivity extends Activity {

	private AppPreferences _appPrefs;

	// Screen resources
	private ImageView ivBack;
	private ImageView ivLogo;
	private TextView tvTitle;
	private TextView tvCoins;
	private ImageView ivLeaderBoard;
	private GridView gvLetters;
	private GridView gvTargetWord;
	private ImageView ivWordImage;
	private ImageView ivWordImageZoom;
	private ImageView ivSuccess;
	

	private TextView tvCategories;
	private LinearLayout llSuccessImage;
	private RelativeLayout rlSuccessSummary;
	private LinearLayout llLetters;
	private RelativeLayout rlTargetWord;
	private RelativeLayout llTargetImg;
	private TextView tvNext;
	private TextView tvBack;
	private TextView tvSummaryScore;
	private TextView tvLevelNums;
	private LinearLayout llSummaryScore;
	private TextView tvSuccessMessage;
	private TextView tvSuccessSubMessage;	
	private static boolean mDisplayQuote = false;	/*Switch between success message and Quote*/


	private RelativeLayout rlPopupImageZoom;
	private int mTotalWords = 0;
	// Members and Parameters
	private WordInfo mWordInfo; // Target Word information
	private LettersAdapter mLettersAdapter;
	private TargetWordAdapter mTargetWordAdapter;
	private ArrayList<LetterInfo> mLetters; // Full list of Letters
	private ArrayList<LetterInfo> mTargetWord; // Target list of letters
	private long mElapsedTime;
	private int mBaseScore;
	private int mPenalties; // Penalty is for incorrect try (10,9,8...(		// consecutive tries.
	private boolean mCorrectWord; // Flag used for handling penalties
	private boolean mSolvedWord; // Flag used for handling compelte word
	private Typeface face;
	private MyCountDownTimer mTimerRemind;
	private int mCountdownTime;
	private Animation mAnimGrow;
	private Animation mScaleUp;
	private Animation mScaleDown;
	private int mScreenWidth;
	private boolean isScaledUp;
	private String[] mSuccessMsg;
	private String[] mSuccessSubMsg;
	private MediaPlayer mpButtonClick;
	private MediaPlayer mpGameWin;
	private int mScore = 0;
	private int mCoins = 0;
	// TAPJOY
	int point_total;
	String currency_name;

	String displayText = "";
	boolean update_text = false;
	boolean earnedPoints = false;

	// Display Ads.
	boolean update_display_ad = false;
	View adView;
	RelativeLayout relativeLayout;
	LinearLayout adLinearLayout;

	// Need handler for callbacks to the UI thread
	final Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	//Remove notification bar
	//	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_letter_game);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mScreenWidth = metrics.widthPixels;

		_appPrefs = new AppPreferences(getApplicationContext());		
		
		LoadTotalWords();		
		LoadResources();
		
		
		if (_appPrefs.getWordCompleted() - 1 >= mTotalWords) {
			ProcessCompletedAllLevels();
		} else {
			LoadWord(_appPrefs.getWordCompleted());
								
			// initialize like the title and score
			InitializeValues();
					
			LoadListeners();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.letter_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

private void InitializeValues() {
		

		isScaledUp = false;		
		mBaseScore = this.getResources().getInteger(R.integer.base_score);
		mCountdownTime = this.getResources().getInteger(
				R.integer.help_icon_shake);
		mCorrectWord = true;
		
		//mLetters = Utility.RandomLetters(mWordInfo.Letters, mAlphabet,
		//mMaxNumberOfLetters);
		mLetters = Utility.ParseString(mWordInfo.Letters, true);
		
		tvCategories.setText(mWordInfo.Suggestion);
		
		mTargetWord = Utility.GenerateBlankArray("", mWordInfo.Word, true);
		// Mark letters as target word letters
		String letter = "";
		boolean isFound = false;
		for (int i = 0; i < mWordInfo.Word.length(); i++) {
			letter = mWordInfo.Word.substring(i, i + 1);
			isFound = false;
			for (int j = 0; j < mLetters.size(); j++) {
				if (!mLetters.get(j).isButton
						&& !mLetters.get(j).isTargetLetter) {
					if (mLetters.get(j).Letter.equals(letter)) {
						mLetters.get(j).isTargetLetter = true;
						isFound = true;
					}
				}
				if (isFound)
					break;
			}
		}

		// Load Grid Views - Target Word
		mTargetWordAdapter = new TargetWordAdapter(this, mTargetWord);
		gvTargetWord.setNumColumns(mTargetWord.size());		
		gvTargetWord.setAdapter(mTargetWordAdapter);
		gvTargetWord.setClickable(true);
		// Set target Word width
		ViewGroup.LayoutParams layoutParams = gvTargetWord.getLayoutParams();
		layoutParams.width = layoutParams.width * mTargetWord.size(); // this is
																		// in
																		// pixels
		gvTargetWord.setLayoutParams(layoutParams);

		// Load Grid Views - Proposed Letters
		mLettersAdapter = new LettersAdapter(this, mLetters);
		gvLetters.setAdapter(mLettersAdapter);
		gvLetters.setClickable(true);

		// Load Images
		ivWordImage.setImageDrawable(Utility.DrawableFromAsset(this,
				mWordInfo.Image, DisplayMetrics.DENSITY_DEFAULT, 1));		
		
		// Set the coins
		tvCoins.setText(String.valueOf(_appPrefs.getCoins()));

		// ***********************************************************

		// Start the countdown timer
		//mTimerRemind.start();
	}
	
	
	private void LoadResources() {
		
		mpGameWin = MediaPlayer.create(this, R.raw.win);
		face = Typeface.createFromAsset(getAssets(), "Chuck.ttf");
		ivBack = (ImageView) findViewById(R.id.button_back);
		ivLogo = (ImageView) findViewById(R.id.button_logo);
		tvTitle = (TextView) findViewById(R.id.tvTopBarTitle);		
		tvTitle.setTypeface(face);
		
		tvLevelNums = (TextView) findViewById(R.id.tvLevelNums);
		tvLevelNums.setText(String.valueOf(_appPrefs.getWordCompleted()));
		tvLevelNums.setTypeface(face);
		
		tvCoins = (TextView) findViewById(R.id.tvTopBarCoins);
		tvCoins.setTypeface(face);
		ivLeaderBoard = (ImageView) findViewById(R.id.icon_leaderboard);

		// Categories
		
		tvCategories = (TextView) findViewById(R.id.tvCategories);
		tvCategories.setTypeface(face);
		
		gvLetters = (GridView) findViewById(R.id.gvLetters);
		gvTargetWord = (GridView) findViewById(R.id.gvTargetWord);
		// gvTargetWord.setEnabled(false);
		ivWordImage = (ImageView) findViewById(R.id.ivWordImage);
		ivWordImageZoom = (ImageView) findViewById(R.id.ivWordImageZoom);
		
		rlPopupImageZoom = (RelativeLayout) findViewById(R.id.popup_imagezoom);
		
		// Summary layout
		llSummaryScore = (LinearLayout) findViewById(R.id.layout_success_summary_score);
		//llSummaryCoins = (LinearLayout) findViewById(R.id.layout_success_summary_coins);

		llSuccessImage = (LinearLayout) findViewById(R.id.layout_images_success);
		llSuccessImage.setVisibility(View.INVISIBLE);
		ivSuccess = (ImageView) findViewById(R.id.ivWordImageSuccess);

		rlSuccessSummary = (RelativeLayout) findViewById(R.id.layout_success_summary);
		rlSuccessSummary.setVisibility(View.INVISIBLE);
		/*Switch between success message and Quote*/
		if(mDisplayQuote)
		{
			tvSuccessMessage = (TextView) findViewById(R.id.tvSuccessQuoteMessage);
			tvSuccessMessage.setTypeface(face);
			tvSuccessSubMessage = (TextView) findViewById(R.id.tvSuccessQuotePerson);
		}
		else
		{
			tvSuccessMessage = (TextView) findViewById(R.id.tvSuccessMessage);
			tvSuccessMessage.setTypeface(face);
			tvSuccessSubMessage = (TextView) findViewById(R.id.tvSuccessSubMessage);
		}

		llLetters = (LinearLayout) findViewById(R.id.layout_bottom_letters);
		rlTargetWord = (RelativeLayout) findViewById(R.id.layout_target_word);
		llTargetImg = (RelativeLayout) findViewById(R.id.layout_target_images);
		Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.push_left_in);
		llLetters.startAnimation(anim);
		rlTargetWord.startAnimation(anim);
		llTargetImg.startAnimation(anim);

		tvNext = (TextView) findViewById(R.id.tvNext);
		tvNext.setTypeface(face);
		tvBack = (TextView) findViewById(R.id.tvBack);
		tvBack.setTypeface(face);
		tvSummaryScore = (TextView) findViewById(R.id.tvSummaryScoreVal);
		// Helper layout
		if(mDisplayQuote)
		{
			//mSuccessMsg = this.getResources().getStringArray(R.array.quote_message);
			//mSuccessSubMsg = this.getResources().getStringArray(R.array.quote_person);
		}
		else
		{
			mSuccessMsg = this.getResources().getStringArray(R.array.success_message);
			mSuccessSubMsg = this.getResources().getStringArray(R.array.success_submessage);
		}
		
		mAnimGrow = AnimationUtils.loadAnimation(this, R.anim.grow_from_middle);
		mScaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
		mScaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

		// Initialize the Time
		mElapsedTime = System.currentTimeMillis();

		mTimerRemind = new MyCountDownTimer(mCountdownTime * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				if (!mSolvedWord) {
					//mHelperView = ivHint;
					//mHelperView.startAnimation(mAnimShake);
					mCountdownTime = LetterGameActivity.this.getResources()
							.getInteger(R.integer.help_icon_shake_repeat);
					this.setMillisInFuture(mCountdownTime * 1000); // here we
																	// change
																	// the
																	// millisInFuture
																	// of our
																	// timer
					this.start();

				}
			}
		};
	}

	
	private void LoadTotalWords() {
		WordInfoDB db = new WordInfoDB(this);
		db.open();
		Cursor cur = db.getRecords();
		mTotalWords = cur.getCount();
		cur.close();
		db.close();
	}
	
	private void showLeaderBoard() {
		mTimerRemind.cancel();
		// TODO dcm
		ArrayList<WordInfo> mWordList = LoadWords();		
		int totalScore = 0;
		for(int i=0;i<mWordList.size();i++)
			totalScore = totalScore + mWordList.get(i).Score;
	}
		

	/**
	 * Load all the levels for the specified Category
	 * 
	 * @param catId
	 */
	private ArrayList<WordInfo> LoadWords() {
		ArrayList<WordInfo> mWordList = new ArrayList<WordInfo>();
		WordInfoDB db = new WordInfoDB(this);
		db.open();
		Cursor cur = db.getRecords();
		if (cur != null) {
			cur.moveToFirst();
			do {
								
				WordInfo mWordInfo = new WordInfo();			
				mWordInfo.ID = cur.getInt(cur.getColumnIndex(WordInfoDB.ROW_ID));
				mWordInfo.Unlocked = cur.getInt(cur
						.getColumnIndex(WordInfoDB.UNLOCKED)) > 0;
				mWordInfo.Solved = cur
						.getInt(cur.getColumnIndex(WordInfoDB.SOLVED)) > 0;
				mWordInfo.Score = cur.getInt(cur.getColumnIndex(WordInfoDB.SCORE));
				mWordInfo.Word = cur.getString(cur.getColumnIndex(WordInfoDB.WORD));
				mWordInfo.Letters = cur.getString(cur
						.getColumnIndex(WordInfoDB.LETTERS));
				mWordInfo.Image = cur.getString(cur
						.getColumnIndex(WordInfoDB.IMAGE));
				mWordInfo.Suggestion = cur.getString(cur
						.getColumnIndex(WordInfoDB.SUGGESTION));
				
				mWordList.add(mWordInfo);
			} while (cur.moveToNext());
			cur.close();
		}
		db.close();
		return mWordList;
	}
	
	/**
	 * Load all the levels for the specified Category
	 * 
	 * @param catId
	 */
	private void LoadWord(int id) {
		mWordInfo = new WordInfo();
		mWordInfo.ID = id;

		WordInfoDB db = new WordInfoDB(this);
		db.open();
		Cursor cur = db.getRecord(id);
		if (cur != null) {
			cur.moveToFirst();
			mWordInfo.ID = cur.getInt(cur.getColumnIndex(WordInfoDB.ROW_ID));
			mWordInfo.Unlocked = cur.getInt(cur
					.getColumnIndex(WordInfoDB.UNLOCKED)) > 0;
			mWordInfo.Solved = cur
					.getInt(cur.getColumnIndex(WordInfoDB.SOLVED)) > 0;
			mWordInfo.Score = cur.getInt(cur.getColumnIndex(WordInfoDB.SCORE));
			mWordInfo.Word = cur.getString(cur.getColumnIndex(WordInfoDB.WORD));
			mWordInfo.Letters = cur.getString(cur
					.getColumnIndex(WordInfoDB.LETTERS));
			mWordInfo.Image = cur.getString(cur
					.getColumnIndex(WordInfoDB.IMAGE));
			mWordInfo.Suggestion = cur.getString(cur
					.getColumnIndex(WordInfoDB.SUGGESTION));
			cur.close();
		}
		db.close();
	}

	
	
	/**
	 * Remove a single letter from the list of invalid letters. This is a
	 * version 2.
	 * 
	 */
	private boolean RemoveLetters() {
		boolean isRemoved = false;
		boolean hasLetters = false;
		Random rnd = new Random();

		// Check if there are letters to be hidden.
		for (int i = 0; i < mLetters.size(); i++) {
			if (mLetters.get(i).Visible && !mLetters.get(i).isTargetLetter
					&& !mLetters.get(i).isButton)
				hasLetters = true;
		}

		// Select randomly a letter and hide it.
		// Check if letters are offered or already selected as mistake in target
		// word
		if (hasLetters) {
			do {
				int pos = rnd.nextInt(mLetters.size());
				if (!mLetters.get(pos).isButton && !mLetters.get(pos).isHint
						&& !mLetters.get(pos).isTargetLetter) {
					mLetters.get(pos).Visible = false;
					mLetters.get(pos).isHint = true;
					gvLetters.getChildAt(pos).setVisibility(View.INVISIBLE);
					isRemoved = true;
				}
			} while (!isRemoved);
		}

		if (isRemoved) {
			// Mark the level has used a hint.
			// TODO dcm
//			mLevelInfo.HintUsed = true;
//			LevelInfoDB level = new LevelInfoDB(this);
//			level.open();
//			level.updateHintUsed(mLevelInfo.ID, 1);
//			level.close();
		}
		return isRemoved;
	}

	/**
	 * Show the next letter as hint. Make it not clickable.
	 */
	private void ShowHintLetter() {
		String letter = "";
		int position = 0;
		Random rnd = new Random();
		boolean isNext = false;

		do {
			int i = rnd.nextInt(mTargetWord.size());
			if (mTargetWord.get(i).Letter.equals("")) {
				letter = mWordInfo.Word.substring(i, i + 1);
				isNext = true;
			}
		} while (!isNext);

		// Find the position in the source
		for (int i = 0; i < mLetters.size(); i++) {
			if (mLetters.get(i).Visible
					&& mLetters.get(i).Letter.equals(letter)) {
				mLetters.get(i).isHint = true;
				position = i;
				break;
			}
		}
		// Mark the level has used a hint.
		// TODO dcm
//		mLevelInfo.HintUsed = true;
//		LevelInfoDB level = new LevelInfoDB(this);
//		level.open();
//		level.updateHintUsed(mLevelInfo.ID, 1);
//		level.close();

		// Play the game
		PlayGame(position, true);
	}
	
	
	private void PlayGame(int position, boolean isHint) {
		mTimerRemind.cancel();
		String letter = mLetters.get(position).Letter;
		mLetters.get(position).Visible = false;

		// If its hint, than it can be random, otherwise not.
		if (isHint) {
			// Place the letter in the target Word
			for (int i = 0; i < mTargetWord.size(); i++) {
				if (mTargetWord.get(i).Letter.equals("")
						&& mWordInfo.Word.substring(i, i + 1).equals(letter)) {
					mTargetWord.get(i).Letter = letter;
					mTargetWord.get(i).Visible = true;
					mTargetWord.get(i).isHint = mLetters.get(position).isHint;
					mTargetWord.get(i).isTargetLetter = mLetters.get(position).isTargetLetter;
					break;
				}
			}
		} else {
			// Place the letter in the target Word
			for (int i = 0; i < mTargetWord.size(); i++) {
				if (mTargetWord.get(i).Letter.equals("")) {
					mTargetWord.get(i).Letter = letter;
					mTargetWord.get(i).Visible = true;
					mTargetWord.get(i).isHint = mLetters.get(position).isHint;
					mTargetWord.get(i).isTargetLetter = mLetters.get(position).isTargetLetter;
					break;
				}
			}
		}
		// Hide the button from the list of letters
		gvLetters.getChildAt(position).setVisibility(View.INVISIBLE);

		// Refresh the target word Adapter, to show the letter
		mTargetWordAdapter.notifyDataSetChanged();
		gvTargetWord.setAdapter(mTargetWordAdapter);
		gvTargetWord.refreshDrawableState();

		// Check if full word is entered and solved
		mSolvedWord = WordSolved();
		if (mSolvedWord)
			mCorrectWord = WordCorrect();

		if (mSolvedWord && !mCorrectWord) {
			// Mark mistake fro Achievements counter
			_appPrefs.setCorrectAnswerCount(0);
			//AddPenalty();
		}

		// Refresh the target word Adapter, to show the letter
		/*
		 * mTargetWordAdapter.notifyDataSetChanged();
		 * gvTargetWord.setAdapter(mTargetWordAdapter);
		 * gvTargetWord.refreshDrawableState();
		 */

		if (mSolvedWord && mCorrectWord)
			ProcessSuccess();
		else
			mTimerRemind.start();
	}

	/**
	 * Check if the Target word is Correct
	 * 
	 * @return
	 */
	private boolean WordCorrect() {
		for (int i = 0; i < mTargetWord.size(); i++) {
			if (!mTargetWord.get(i).Visible
					|| !mTargetWord.get(i).Letter.equals(mWordInfo.Word
							.substring(i, i + 1)))
				return false;
		}
		return true;
	}

	/**
	 * Check if all the letters have been entered
	 * 
	 * @return
	 */
	private boolean WordSolved() {
		for (int i = 0; i < mTargetWord.size(); i++) {
			if (!mTargetWord.get(i).Visible
					|| mTargetWord.get(i).Letter.equals(""))
				return false;
		}
		return true;
	}

	
//	private void AddPenalty() {
//		// Add the points to penalty
//		mPenalties = mPenalties + mBasePenalty;
//		// Decrease the base penalty
//		if (mBasePenalty > 0)
//			mBasePenalty = mBasePenalty - 1;
//	}

	/**
	 * At the end of the game, show the success screen, calcucate and save
	 * scores.
	 * 
	 */
	private void ProcessCompletedAllLevels() {
				
		PlaySuccessSound();
		// Show the success message
		showRandomSuccessMsg();
		
		// Show the success layout
		llSuccessImage.setVisibility(View.VISIBLE);
		llSuccessImage.setBackgroundResource(R.color.play_success_shade);
		ivSuccess.startAnimation(mAnimGrow);

		Animation animL = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.push_left_in);
		Animation animR = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.push_right_in);

		llSummaryScore.startAnimation(animL);
	//	llSummaryCoins.startAnimation(animR);

	}
	
	/**
	 * At the end of the game, show the success screen, calcucate and save
	 * scores.
	 * 
	 */
	private void ProcessSuccess() {
		tvNext.setEnabled(false);
		
//		LogFlurryTimed("Play_Word", "Play Activity", "Start", false);
		PlaySuccessSound();
		// Show the success message
		showRandomSuccessMsg();
		// Prevent scale up when finished
		isScaledUp = true;
		// Stop the timer and calculate Score+Coins
		mElapsedTime = System.currentTimeMillis() - mElapsedTime;

		// Calculate the score
		mScore = CalculateScore();
		mCoins = CalculateCoins(mScore);

		// Save the score on category level
		
		_appPrefs.setWordCompleted(_appPrefs.getWordCompleted()+1);
		
		// Save the Coins, and handle synch for offline mode
		updateAppCoins(mCoins);					
				
		// Set the values
		//tvSummaryCoins.setText(String.valueOf(mCoins));
		tvSummaryScore.setText(String.valueOf(mScore));

		// Show the summary
		llLetters.setVisibility(View.INVISIBLE);
		rlSuccessSummary.setVisibility(View.VISIBLE);

		// Disable Letters
		gvLetters.setEnabled(false);
		
		// Show the success layout
		llSuccessImage.setVisibility(View.VISIBLE);
		llSuccessImage.setBackgroundResource(R.color.play_success_shade);
		ivSuccess.startAnimation(mAnimGrow);

		Animation animL = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.push_left_in);
		Animation animR = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.push_right_in);

		llSummaryScore.startAnimation(animL);
		//llSummaryCoins.startAnimation(animR);

		/*
		 * _appPrefs.saveCoins(_appPrefs.getCoins() + coins);
		 * _appPrefs.saveCoinsDiff(_appPrefs.getCoinsDiff()+ coins); //award
		 * points if plus, remove otherwise if(_appPrefs.getCoinsDiff() > 0)
		 * TapjoyConnect
		 * .getTapjoyConnectInstance().awardTapPoints(_appPrefs.getCoinsDiff(),
		 * this); else
		 * TapjoyConnect.getTapjoyConnectInstance().spendTapPoints(_appPrefs
		 * .getCoinsDiff(), this);
		 */
		
		ProcessPostSuccess();
	}
	
	private void ProcessPostSuccess() {
		new AsyncTask<Void, Void, Boolean>() {
			
			@Override
			protected void onPreExecute() {
				/*
				 * This is executed on UI thread before doInBackground(). It is
				 * the perfect place to show the progress dialog.
				 */				
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				if (params == null) {
					return false;
				}
				try {
					/*
					 * This is run on a background thread, so we can sleep here
					 * or do whatever we want without blocking UI thread. A more
					 * advanced use would download chunks of fixed size and call
					 * publishProgress();
					 */
					// Thread.sleep(params[0]);
					// HERE I'VE PUT ALL THE FUNCTIONS THAT WORK FOR ME			
										
					// Save score word to DB
					WordInfoDB db = new WordInfoDB(LetterGameActivity.this);
					db.open();
					db.update(mWordInfo.ID, 1, 1, mScore);
					db.close();

				} catch (Exception e) {
					/*
					 * The task failed
					 */
					return false;
				}

				/*
				 * The task succeeded
				 */
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {				
				/*
				 * Update here your view objects with content from download. It
				 * is save to dismiss dialogs, update views, etc., since we are
				 * working on UI thread.
				 */
				tvNext.setEnabled(true);
			}

		}.execute();
	}
	
	
	/**
	 * Play button click if sound is enabled
	 */
	private void PlaySuccessSound() {
		if (_appPrefs.getSound())
			mpGameWin.start();
	}

	/**
	 * Show the success message from random success messages
	 * 
	 * @return
	 */
	private void showRandomSuccessMsg() {
		Random rnd = new Random();
		if(mDisplayQuote)
		{
			int position = rnd.nextInt(mSuccessMsg.length);
			tvSuccessMessage.setText(mSuccessMsg[position]);
			tvSuccessSubMessage.setText(mSuccessSubMsg[position]);
			tvSuccessMessage.setVisibility(View.VISIBLE);
			tvSuccessSubMessage.setVisibility(View.VISIBLE);
		}
		else
		{
			tvSuccessMessage.setText(mSuccessMsg[rnd.nextInt(mSuccessMsg.length)]);
			tvSuccessSubMessage.setText(mSuccessSubMsg[rnd
					.nextInt(mSuccessSubMsg.length)]);
			tvSuccessMessage.setVisibility(View.VISIBLE);
			tvSuccessSubMessage.setVisibility(View.VISIBLE);
			// tvSuccessMessage.setAnimation(m)
		}
	}

	/**
	 * Calculate the score after game completed
	 * 
	 * @return
	 */
	private int CalculateScore() {
		int score = (int) (mBaseScore + (250 / Math
				.sqrt((mElapsedTime / 1000) + 4)));
		score = score - mPenalties;
		return score;
	}

	/**
	 * Calculate the coins based on the number of coins
	 * 
	 * @param score
	 * @return
	 */
	private int CalculateCoins(int score) {
		if (score >= 100)
			return 5;
		else if (score >= 90)
			return 4;
		else if (score >= 80)
			return 3;
		else if (score >= 70)
			return 2;
		else
			return 1;
	}
	

	/**
	 * Rollback the letter, pressed on the Target word. Leave the space as
	 * empty, and show the hidden letter.
	 * 
	 * @param position
	 */
	private void ReleaseLetter(int position) {
		mTimerRemind.cancel();
		String letter = mTargetWord.get(position).Letter;

		mTargetWord.get(position).Letter = "";
		mTargetWord.get(position).Visible = false;

		// Check if full word is entered and solved
		mSolvedWord = false;
		mCorrectWord = false;

		// Refresh the target word Adapter
		mTargetWordAdapter.notifyDataSetChanged();
		gvTargetWord.setAdapter(mTargetWordAdapter);
		gvTargetWord.refreshDrawableState();

		for (int i = 0; i < mLetters.size(); i++) {
			if (mLetters.get(i).Letter.equals(letter)
					&& !mLetters.get(i).Visible) {
				mLetters.get(i).Visible = true;
				gvLetters.getChildAt(i).setVisibility(View.VISIBLE);
				break;
			}
		}
		mTimerRemind.start();
	}

	/**
	 * Play button click if sound is enabled
	 */
	private void PlayButtonClick() {
		if (_appPrefs.getSound())
		{
			if (mpButtonClick!=null) {
				mpButtonClick.stop();
				mpButtonClick.release();
	          }
			mpButtonClick= MediaPlayer.create(LetterGameActivity.this,R.raw.button);
			mpButtonClick.start();
		}
	}
	/**
	 * Load all the Listeners. Resources need to be loaded previously.
	 */
	private void LoadListeners() {
		// Go back to previous screen
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				PlayButtonClick();
				finish();
			}
		});
		ivLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				PlayButtonClick();
				finish();
			}
		});

		// Show the leaderboard
		ivLeaderBoard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LogFlurry("High_Scores", "Show Leaderboard", "Play Activity");
				PlayButtonClick();
				showLeaderBoard();
			}
		});

		
		//myItem = myAdapter.getItem(myGridView.pointToPosition((int)e.getX(), (int)e.getY()));
		gvLetters.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{					
					float currentXPosition = event.getX();
		            float currentYPosition = event.getY();
		            int position = gvLetters.pointToPosition((int) currentXPosition, (int) currentYPosition);
		            if (position > -1){
						PlayButtonClick();
						if (!mSolvedWord) {
							PlayGame(position, false);
						}
					}
				}
				return false;
			}
		});
		
		
		gvTargetWord.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					PlayButtonClick();
					 
					float currentXPosition = event.getX();
		            float currentYPosition = event.getY();
		            int position = gvTargetWord.pointToPosition((int) currentXPosition, (int) currentYPosition);

		            if (position > -1){
			            if (!mTargetWord.get(position).isHint) {
							ReleaseLetter(position);
						}
		            }
				}
				return false;
			}
		});		

		// GoBack After game completed
		tvBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PlayButtonClick();
				finish();
			}
		});

		// Go to The Next game, after game completed
		tvNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PlayButtonClick();
				finish();
				// Get the next (first game)
				if (_appPrefs.getWordCompleted() - 1 < mTotalWords) {
					WordInfoDB db = new WordInfoDB(getApplicationContext());
					db.open();
					int id = db.getNextID(mWordInfo.ID);
					db.close();

					Intent intent = getIntent();
					_appPrefs.setWordCompleted(id);
					startActivity(intent);
					
					if ( _appPrefs.getShowAds() && (_appPrefs.getWordCompleted() - 1) % getResources().getInteger(R.integer.interstitial_level) == 0) {
				        // Create ad request
				        //AdRequest adRequest = new AdRequest();

				        // Begin loading your interstitial
				        //interstitialAd.loadAd(adRequest);
					}
				}
			}
		});


		// Show the big image picture
		ivWordImageZoom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PlayButtonClick();
				if (isScaledUp) {
					ivWordImageZoom.setVisibility(View.INVISIBLE);
					ivWordImageZoom.startAnimation(mScaleDown);
					rlPopupImageZoom.setVisibility(View.INVISIBLE);
					isScaledUp = false;
				}
			}
		});

		// Show the big image picture
		ivWordImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PlayButtonClick();
				if (!isScaledUp) {
					isScaledUp = true;
					ivWordImageZoom.setImageDrawable(Utility.DrawableFromAsset(
							getApplicationContext(), mWordInfo.Image,
							DisplayMetrics.DENSITY_MEDIUM, 1));
					ivWordImageZoom.setVisibility(View.VISIBLE);					
					ivWordImageZoom.startAnimation(mScaleUp);
					rlPopupImageZoom.setVisibility(View.VISIBLE);
				}

			}
		});

	}
	
	private void LogFlurry(String event, String Activity, String action) {
		Map<String, String> params = new HashMap<String, String>();
		/*params.put("Category", mLevelInfo.CatName);
		params.put("Level", mLevelInfo.LvlName);
		params.put("Word", mWordInfo.Word);*/
		params.put(Activity, action);
		//FlurryAgent.logEvent(event, params);
	}

	
	/**
	 * Update the points status.
	 * 
	 * @param points
	 */
	private void updateAppCoins(int points) {
		_appPrefs.saveCoins(_appPrefs.getCoins() + points);
		tvCoins.setText(String.valueOf(_appPrefs.getCoins()));
	}

	private class TargetWordAdapter extends BaseAdapter {
		ArrayList<LetterInfo> list;
		LayoutInflater inflater = null;
		TextView mLetter;
		ImageView mImage;

		public TargetWordAdapter(Context context, ArrayList<LetterInfo> list) {
			this.list = list;
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		// @Override
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;

			if (v == null)
				v = inflater.inflate(R.layout.target_letter_list_item, parent,
						false);

			v.setMinimumWidth(mScreenWidth / list.size());

			mLetter = (TextView) v.findViewById(R.id.tvTargetLetter);
			mImage = (ImageView) v.findViewById(R.id.ivTargetLetter);
			mLetter.setText(list.get(position).Letter);			
			mLetter.setTypeface(face);

			if (mSolvedWord) {
				if (mCorrectWord)
					mImage.setImageResource(R.drawable.button_letters_correct);					
				else
				{
					mImage.setImageResource(R.drawable.button_letters_error);
					Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
				    animation.setDuration(80); // duration - half a second
				    animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
				    animation.setRepeatCount(4); // Repeat animation 
				    animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in				  
				    v.startAnimation(animation);
				}
			} 
			else if (list.get(position).Letter.equals(""))
				mImage.setImageResource(R.drawable.button_letters_normal);
			else if (list.get(position).isHint)
				mImage.setImageResource(R.drawable.button_letters_hint);
			else
				mImage.setImageResource(R.drawable.button_letters_normal);
			
			return v;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public LetterInfo getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	/**
	 * Used for Displaying Possible Letters
	 * 
	 */
	private class LettersAdapter extends BaseAdapter {
		ArrayList<LetterInfo> list;
		LayoutInflater inflater = null;
		TextView mLetter;
		ImageView mImage;

		public LettersAdapter(Context context, ArrayList<LetterInfo> list) {
			this.list = list;
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		// @Override
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;

			if (v == null)
				v = inflater.inflate(R.layout.letter_list_item, parent, false);
			v.setId(position);
			mLetter = (TextView) v.findViewById(R.id.tvLetter);
			mLetter.setTypeface(face);
			mImage = (ImageView) v.findViewById(R.id.ivButtonLetter);

			mLetter.setText(list.get(position).Letter);

			return v;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public LetterInfo getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}	
	
}
