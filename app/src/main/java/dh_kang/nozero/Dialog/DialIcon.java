package dh_kang.nozero.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import dh_kang.nozero.Activity.ActiMain;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-11-28.
 */
public class DialIcon extends Dialog {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    ImageView di_imgIcon1, di_imgIcon2, di_imgIcon3, di_imgIcon4;
    Button di_btnOk;
    String tempImg, tempId; // 아이콘 주소

    /* JAVA 선언 */
    SharedPreferences userInfo;

    /* 네트워크 통신 */
    iconServer task;

    /* 필수 선언 */
    public DialIcon(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dial_icon);

        /* 초기화 */
        di_imgIcon1 = (ImageView)findViewById(R.id.di_imgIcon1);
        di_imgIcon2 = (ImageView)findViewById(R.id.di_imgIcon2);
        di_imgIcon3 = (ImageView)findViewById(R.id.di_imgIcon3);
        di_imgIcon4 = (ImageView)findViewById(R.id.di_imgIcon4);
        di_btnOk = (Button)findViewById(R.id.di_btnOk);

        /* 아이콘을 선택하면 선택선 만들어지게 하기*/
        di_imgIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                di_imgIcon1.setBackgroundResource(R.drawable.btn_clicked);
                di_imgIcon2.setBackgroundResource(R.drawable.btn_white_border);
                di_imgIcon3.setBackgroundResource(R.drawable.btn_white_border);
                di_imgIcon4.setBackgroundResource(R.drawable.btn_white_border);
                tempImg = "http://i.imgur.com/DtlZIUr.png";
            }
        });

        di_imgIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                di_imgIcon1.setBackgroundResource(R.drawable.btn_white_border);
                di_imgIcon2.setBackgroundResource(R.drawable.btn_clicked);
                di_imgIcon3.setBackgroundResource(R.drawable.btn_white_border);
                di_imgIcon4.setBackgroundResource(R.drawable.btn_white_border);
                tempImg = "http://i.imgur.com/2QOgFPX.png";
            }
        });

        di_imgIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                di_imgIcon1.setBackgroundResource(R.drawable.btn_white_border);
                di_imgIcon2.setBackgroundResource(R.drawable.btn_white_border);
                di_imgIcon3.setBackgroundResource(R.drawable.btn_clicked);
                di_imgIcon4.setBackgroundResource(R.drawable.btn_white_border);
                tempImg = "http://i.imgur.com/FR3sD3d.png";
            }
        });

        di_imgIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                di_imgIcon1.setBackgroundResource(R.drawable.btn_white_border);
                di_imgIcon2.setBackgroundResource(R.drawable.btn_white_border);
                di_imgIcon3.setBackgroundResource(R.drawable.btn_white_border);
                di_imgIcon4.setBackgroundResource(R.drawable.btn_clicked);
                tempImg = "http://i.imgur.com/mQcjyMw.png";
            }
        });


        /* Ok 누르면 서버에 있는 그림주소 바꾸기, shared 바꾸기, dismiss, glide 재실행하기 */
        di_btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 아이디와 주소 보내기 */
                userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                tempId = userInfo.getString("userId", "");
                Log.i(TAG, tempId);
                task = new iconServer();
                task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Di_ChangeIcon.php");
            }
        });
    }

    private class iconServer extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(getContext());

        /* 연결 전 */
        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("..로딩중..");
            asyncDialog.show();
            super.onPreExecute();
        }

        /* 연결 시 */
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL url = new URL(urls[0]); // 연결 url 설정
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 커넥션 객체 생성
                if (conn != null) { // 연결된 경우
                    conn.setConnectTimeout(3000);
                    conn.setUseCaches(false);
                    conn.setDoOutput(true);

                    String data  = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(tempId, "UTF-8")
                            + "&" + URLEncoder.encode("userImage", "UTF-8") + "=" + URLEncoder.encode(tempImg, "UTF-8");

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);// onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                    wr.flush(); // OutputStreamWriter 버퍼 메모리 비우기

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) { // 연결 후 코드가 리턴
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (;;) {  // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            jsonHtml.append(line + "\n"); // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str){
            if(str.trim().equals("100")){ // 아이콘 변경시
                Toast.makeText(getContext(), "아이콘이 변경되었습니다", Toast.LENGTH_SHORT).show();
                userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();
                editor.putString("userPicPath", tempImg); // 이미지 저장
                editor.commit();

                ((ActiMain)ActiMain.mContext).ChangeFragment(((ActiMain)ActiMain.mContext).findViewById(R.id.am_btnMy));

                asyncDialog.dismiss();
                dismiss();
            }else{
                asyncDialog.dismiss();
                Toast.makeText(getContext(), "아이콘 변경이 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
