package dh_kang.nozero.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import dh_kang.nozero.IntegratedClass.CheckNetworkState;
import dh_kang.nozero.R;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "NOZERO_FINAL";   /* LOG TEST */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_splash);

        /* Check network state */
        CheckNetworkState chkNetwork = new CheckNetworkState();
        boolean networkState = chkNetwork.returnNetworkState(getApplicationContext());

        Handler handler = new Handler();
        if(!networkState){
            Toast.makeText(this, "네트워크 연결을 확인해주세요", Toast.LENGTH_LONG).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        }else{
            /* Go to main page */
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();   // To block 'back button'
                }
            }, 2000);}
        }
}
