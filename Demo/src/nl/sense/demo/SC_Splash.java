package nl.sense.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SC_Splash extends Activity {
    
    /**
     * The thread to process splashscreen screen events
     */
    private Thread mSplashThread;    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Splash screen view
        setContentView(R.layout.sc_splash);
        
        final SC_Splash sPlashScreen = this;   
        
        // The thread to wait for splashscreen screen events
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        // Wait given period of time or exit on touch
                        wait(5000);
                    }
                }
                catch(InterruptedException ex){                    
                }

                finish();
                
                // Run next activity
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, MainActivity.class);
                startActivity(intent);
                stop();                    
            }
        };
        
        mSplashThread.start();        
    }
        
    /**
     * Processes splashscreen screen touch events
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread){
                mSplashThread.notifyAll();
            }
        }
        return true;
    }    
} 