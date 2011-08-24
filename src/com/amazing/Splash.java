package com.amazing;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Splash extends Activity{
	
	private Button list_button, directions_button;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateFullscreenStatus(true);
        setContentView(R.layout.splash);
        list_button = (Button) findViewById(R.id.list_button);
        
        list_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                groceryScene();
            }
        });
        
        directions_button = (Button) findViewById(R.id.directions_button);
        
        directions_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                directionsScene();
            }
        });
	}
	
	private void groceryScene()
	{
		startActivity(new Intent(getBaseContext(), Grocery.class));
	}
	
	private void directionsScene()
	{
		Directions1.exit = false;
		startActivity(new Intent(getBaseContext(), Directions1.class));
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
