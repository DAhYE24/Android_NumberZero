package dh_kang.nozero.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import dh_kang.nozero.Activity.ActiMain;
import dh_kang.nozero.Activity.ActiPerfDetail;
import dh_kang.nozero.Adapter.Lv_BoxAdapter;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-11-27.
 */
public class DialBox extends Dialog {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    ListView db_lvBox;
    Button db_btnOk;

    /* 리스트뷰 선언 */
    Lv_BoxAdapter boxAdapter = null;

    /* JAVA 선언 */
    SharedPreferences userInfo;
    String userId, sName, sCapacity;

    /* 네트워크 통신 */
    String myJSON;
    JSONArray vc = null;
    getBoxInfo task;
    String tempBoxImg, tempBoxTitle, tempBoxCapacity;
    boolean checkPhp; // true : 전체 보관 향수 불러오기, false : 선택 향수 상세 정보 연결하기
    public static String boxJSON;

    public static String getBoxJSON() {
        return boxJSON;
    }

    /* 필수 선언 */
    public DialBox(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dial_box);

        /* 초기화 */
        db_lvBox = (ListView)findViewById(R.id.db_lvBox);
        db_btnOk = (Button)findViewById(R.id.db_btnOk);

        /* 유저아이디 */
        userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = userInfo.getString("userId", "");

        /* 보관함 정보 불러오기 */
        loadBoxPerfumes();

        /* 확인 창 */
        db_btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void loadBoxPerfumes() {
        checkPhp = true;
        task = new getBoxInfo();
        task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Db_BoxInfo.php");
    }

    public class getBoxInfo extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("..로딩중..");
            asyncDialog.show();
            super.onPreExecute();
        }

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

                    String data = null;

                    if(checkPhp == true){ // 모든 향수 불러오기
                        data = URLEncoder.encode("boxId", "UTF-8") + "=" + URLEncoder.encode(userId, "UTF-8");
                    }else{ // 특정 향수 정보 연결해주기
                        data = URLEncoder.encode("perfName", "UTF-8") + "=" + URLEncoder.encode(sName, "UTF-8")
                                + "&" + URLEncoder.encode("perfCapacity", "UTF-8") + "=" + URLEncoder.encode(sCapacity, "UTF-8");
                    }

                    Log.i(TAG, data);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);// onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                    wr.flush(); // OutputStreamWriter 버퍼 메모리 비우기

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) { // 연결 후 코드가 리턴
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {  // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
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

        protected void onPostExecute(String str) {
            if(checkPhp == true){
                if (str.trim().equals("200")) {
                    Toast.makeText(getContext(), "보관한 향수가 없습니다", Toast.LENGTH_SHORT).show();
                    asyncDialog.dismiss();
                } else {
                    myJSON = str;
                    showList();
                    asyncDialog.dismiss();
                }
            }else{ // 해당 아이템 클릭한 경우
                SharedPreferences.Editor editor = userInfo.edit();
                editor.putString("searchBox", "box"); // 닉네임 저장
                editor.commit();
                boxJSON = str;
                asyncDialog.dismiss();

                /* 결과창으로 이동 */
                Intent i = new Intent(getContext(), ActiPerfDetail.class);
                ((ActiMain)ActiMain.mContext).startActivity(i);
            }
        }
    }

    /* 보관함 내용 */
    protected void showList(){
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            vc = jsonObj.getJSONArray("results");

            ArrayList<Lv_BoxAdapter.Lv_BoxValues> boxList = new ArrayList<Lv_BoxAdapter.Lv_BoxValues>();
            Lv_BoxAdapter.Lv_BoxValues inputBox;

            for(int i=0;i<vc.length();i++) {
                JSONObject jo = vc.getJSONObject(i);
                tempBoxTitle = jo.getString("boxTitle");
                tempBoxCapacity = jo.getString("boxCapacity");
                tempBoxImg = jo.getString("boxImage");

                inputBox = new Lv_BoxAdapter.Lv_BoxValues(tempBoxImg, tempBoxTitle, tempBoxCapacity);
                boxList.add(inputBox);
            }
            boxAdapter = new Lv_BoxAdapter(getContext(), R.layout.lv_comment, boxList); // 리스트 형태 연결
            db_lvBox.setAdapter(boxAdapter);

            db_lvBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Lv_BoxAdapter.Lv_BoxValues bvalue = (Lv_BoxAdapter.Lv_BoxValues)parent.getItemAtPosition(position);
                    sName = bvalue.getBoxTitle();
                    sCapacity = bvalue.getBoxCapacity();

                    /* 선택한 향수로 연결해주기 */
                    checkPhp = false;
                    task = new getBoxInfo();
                    task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Apr_PerfDetail.php");
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
