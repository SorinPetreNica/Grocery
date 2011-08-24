package com.amazing;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Directions1 extends Activity{
	public static Boolean exit;
	Button home_button, next_button;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateFullscreenStatus(true);
        setContentView(R.layout.dir1);
        
        home_button = (Button) findViewById(R.id.button2);
        
        home_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        
        next_button = (Button) findViewById(R.id.button3);
        
        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextScene();
            }
        });
	}
	
	private void nextScene()
	{
		startActivity(new Intent(getBaseContext(), Directions2.class));
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Directions1.exit) finish();
	}
	
	private void updateFullscreenStatus(Boolean bUseFullscreen)
	{   
	   if(bUseFullscreen)
	   {
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	    }
	    else
	    {
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    }

	    //m_contentView.requestLayout();
	}
	
}

