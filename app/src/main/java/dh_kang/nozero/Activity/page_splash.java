package dh_kang.nozero.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import dh_kang.nozero.IntegratedClass.CheckNetworkState;
import dh_kang.nozero.R;

public class page_splash extends AppCompatActivity {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_splash);

        /* Check network state */
        CheckNetworkState chkNetwork = new CheckNetworkState();
        boolean networkState = chkNetwork.ReturnNetworkState(getApplicationContext());

        Handler handler = new Handler();
        if(!networkState){
            Toast.makeText(this, "네트워크 연결이 필요합니다", Toast.LENGTH_LONG).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        }else{
            handler.postDelayed(new Runnable() {
                public void run() { /* Go to main page */
                    Intent intent = new Intent(page_splash.this, ActiMain.class);
                    startActivity(intent);
                    finish();   // To block 'back button'
                }
            }, 2000);}
        }
}
