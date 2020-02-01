package com.example.english4kids;

import com.e4kids.helper.RoundedImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class AtractActivity extends Activity {
	  private Typeface face;
	 protected void onCreate(Bundle savedInstanceState) {
		 
		  super.onCreate(savedInstanceState);
		  //check if customTitlebar is supported.
		  final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		        setContentView(R.layout.main_menu);
		        if (customTitleSupported) 
		        {
		            // if customTitlebar is supports, set the titlebar layout for it.
		            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
		         }
		        
		      //  Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                     //   R.drawable.backbtn);
		        final ImageButton backButton = (ImageButton)findViewById(R.id.backButton);
		      //  Drawable drawable = this.getResources().getDrawable(R.drawable.backbtn);
		       // backButton.setImageBitmap(RoundedImageView.getCroppedBitmap(icon, 40));
		        
//		        Animation fadeIn = new AlphaAnimation(1, 2);
//				fadeIn.setDuration(1000);
//				fadeIn.setRepeatCount(Animation.INFINITE);
//				backButton.setAnimation(fadeIn);
//				backButton.startAnimation(fadeIn);
		        
		        if(backButton !=null){
		        	backButton.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							 onClickBackButton();
							 
						}
		        		
		        	});
		        }
		        
		        final ImageView logoButton = (ImageView)findViewById(R.id.button_logo_title);
		        if(logoButton !=null){
		        	logoButton.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							 onClickBackButton();
							 
						}
		        		
		        	});
		        }
		        
		        
		       // RotateAnimation ranim = (RotateAnimation)AnimationUtils.loadAnimation(this, R.anim.rotate);
		        //backButton.setAnimation(ranim);
		        
		        
		        face = 	Typeface.createFromAsset(getAssets(), "Chuck.ttf");;
		        // get a reference to myTitle TextView.
		        final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		        myTitleText.setTypeface(face);
		        if ( myTitleText != null ) {
		         //get the title text for the current screen, and set it in the myTitle TextView.
		         // getText() returns a localized string resource, and accepts a resourceId
		         // getTitleText must be implemented to return a string resource id.
		            myTitleText.setText((getTitleText()));
		            // user can also set color using "Color" and then "Color value constant"
		           // myTitleText.setBackgroundColor(Color.GREEN);
		        }
		        
		        
		 }
		 
		 /**
		  * Implement this method to return a string resource id from the strings.xml file
		  * 
		  * @return
		  */
		 protected abstract String getTitleText();
		 protected abstract void onClickBackButton();
		 
}


