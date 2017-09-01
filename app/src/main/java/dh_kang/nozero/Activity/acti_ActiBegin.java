package dh_kang.nozero.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import dh_kang.nozero.R;

public class acti_ActiBegin extends AppCompatActivity {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_begin);

        /* 네트워크 변수 */
        Handler handler = new Handler();
        ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        boolean isMobileConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifiConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean isMobileAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
        boolean isWifiAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();

        /* 네트워크 연결체크 */
        if(!(isMobileConnect && isMobileAvailable) && !(isWifiConnect && isWifiAvailable)){
            Toast.makeText(this, "네트워크 연결이 필요합니다", Toast.LENGTH_LONG).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        }else{
            handler.postDelayed(new Runnable() {
                public void run() {
                    /* Go to ActiMain */
                    Intent i = new Intent(acti_ActiBegin.this, ActiMain.class);
                    startActivity(i);
                    finish(); // 뒤로 가기 버튼을 누른 경우 해당 화면이 나오지 않도록하기 위해서 설정
                }
            }, 2000);}
        }
}
