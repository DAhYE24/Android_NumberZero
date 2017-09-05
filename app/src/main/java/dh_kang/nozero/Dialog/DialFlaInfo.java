package dh_kang.nozero.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import dh_kang.nozero.Adapter.FlavorListAdapter;
import dh_kang.nozero.R;
import dh_kang.nozero.IntegratedClass.TextViewHelper;

/**
 * Created by dh93 on 2016-11-21.
 */
public class DialFlaInfo extends Dialog {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    TextView dfi_txtTitle, dfi_txtDetail, dfi_txtType;
    Button dfi_btnOk;

    /* 필수 선언 */
    public DialFlaInfo(Context context) {
        super(context);
    }

    /* JAVA */
    String flaFindName, myJSON;
    String flaName, flaType, flaDetail; //받아오는값
    JSONArray vc = null;

    /* 네트워크 통신 */
    receiveFlaInfo task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dial_flainfo);

        /* 초기화 */
        dfi_txtDetail = (TextView)findViewById(R.id.dfi_txtDetail);
        dfi_txtTitle = (TextView)findViewById(R.id.dfi_txtTitle);
        dfi_txtType = (TextView)findViewById(R.id.dfi_txtType);
        dfi_btnOk = (Button)findViewById(R.id.dfi_btnOk);

        /* 해당 향료명 받아오기*/
        FlavorListAdapter lvFla = null;
        flaFindName = lvFla.getFlavorName();

        Log.i(TAG, flaFindName);

        /* 향료 정보 받아오기 */
        task = new receiveFlaInfo();
        task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Dfi_FlaInfo.php");

        /* OK 버튼 */
        dfi_btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /* 서버에서 향료 정보 받아오기 */
    public class receiveFlaInfo extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(getContext());

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

                    String data  = URLEncoder.encode("flaFindName", "UTF-8") + "=" + URLEncoder.encode(flaFindName, "UTF-8");

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
            myJSON = str;
            showList();
            asyncDialog.dismiss();
        }
    }

    protected void showList(){
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            vc = jsonObj.getJSONArray("results");

            for(int i=0;i<vc.length();i++) {
                JSONObject jo = vc.getJSONObject(i);
                flaName = jo.getString("flaName");
                flaType = jo.getString("flaType");
                flaDetail = jo.getString("flaDetail");
            }

            /* 향료 정보 보여주기 */
            dfi_txtTitle.setText("[ " + flaName + " ]");
            dfi_txtType.setText(flaType);

            /* 디바이스의 가로 크기 불러오기 */
            DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
            int width = dm.widthPixels;

            dfi_txtDetail.setText(TextViewHelper.shrinkWithWordUnit(dfi_txtDetail, flaDetail, width / 2));
            //dfi_txtDetail.setText(flaDetail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
