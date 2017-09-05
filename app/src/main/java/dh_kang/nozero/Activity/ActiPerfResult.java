package dh_kang.nozero.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

import dh_kang.nozero.Fragment.FragMain;
import dh_kang.nozero.Fragment.SearchFragment;
import dh_kang.nozero.Adapter.Lv_MyPerfumeAdapter;
import dh_kang.nozero.DataSet.Lv_PerfumeValues;
import dh_kang.nozero.R;

public class ActiPerfResult extends AppCompatActivity {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    ListView ap_lvResult; //리스트뷰
    String rJson;

    /* JAVA 선언*/
    SharedPreferences userInfo;
    JSONArray vc = null;
    String perfName, perfPrice, perfBrand, perfCapacity, perfEngName, perfImage;
    String tempName, tempEngName;
    String sendName, sendCapacity;
    String tempId;

    /* 리스트뷰 */
    Lv_MyPerfumeAdapter perfAdapter = null;

    /* 네트워크 통신 */
    getPerfData task;
    SearchFragment fs = null;
    FragMain fm = null;
    public static String perfumeJSON;

    public String getPerfumeJSON() {
        return perfumeJSON;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_perfresult);

        /* 일반검색인지 메인검색인지 구별 */
        userInfo = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String tempType = userInfo.getString("searchPerf", "");

        /* 값 받아오기 */
        if(tempType.equals("main")){
            rJson = fm.getSpecificJSON();
        }else if(tempType.equals("search")){
            rJson = fs.getResultList();
        }

        /* 초기화 */
        ap_lvResult = (ListView)findViewById(R.id.apr_lvResult);

        /* 검색결과 향수 보여주기 */
        showPerfumeInfo();

        /* 사용자의 조합ID 받아오기 */
        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        tempId = userInfo.getString("userId", "");
        Log.i(TAG, tempId);

        /* 클릭하는 경우 */
        ap_lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lv_PerfumeValues lvPerf = (Lv_PerfumeValues) parent.getItemAtPosition(position);
                sendName = lvPerf.getSearchName();
                sendCapacity = lvPerf.getSearchCapacity();
                task = new getPerfData();
                task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Apr_PerfDetail.php");
            }
        });
    }

    /* 검색결과 향수 보여주기 */
    private void showPerfumeInfo() {
        try{
            JSONObject jsonObj = new JSONObject(rJson);
            vc = jsonObj.getJSONArray("results");

            ArrayList<Lv_PerfumeValues> perfList = new ArrayList<Lv_PerfumeValues>();
            Lv_PerfumeValues inputPerf;

            for(int i=0;i<vc.length();i++) {
                JSONObject jo = vc.getJSONObject(i);
                perfName = jo.getString("perfName");
                perfPrice = jo.getString("perfPrice");
                perfImage = jo.getString("perfImage");
                perfEngName = jo.getString("perfEngName");
                perfBrand = jo.getString("perfBrand");
                perfCapacity = jo.getString("perfCapacity");

//                /* 해당 길이에 따른 출력 변화*/
//                if(perfName.length() > 15){
//                    tempName =  perfName.substring(0, 13) + "..";
//                    inputPerf = new Lv_PerfumeValues(perfName, perfCapacity, perfImage, tempName, perfEngName, perfBrand, perfCapacity + " / " + perfPrice + "원");
//                }else if(perfEngName.length() > 25){
//                    tempEngName = perfEngName.substring(0, 23) + "..";
//                    inputPerf = new Lv_PerfumeValues(perfName, perfCapacity, perfImage, perfName, tempEngName, perfBrand, perfCapacity + " / " + perfPrice + "원");
//                }else if(perfName.length() > 15 && perfEngName.length() > 25){
//                    tempName =  perfName.substring(0, 13) + "..";
//                    tempEngName = perfEngName.substring(0, 23) + "..";
//                    inputPerf = new Lv_PerfumeValues(perfName, perfCapacity, perfImage, tempName, tempEngName, perfBrand, perfCapacity + " / " + perfPrice + "원");
//                }else{
                    inputPerf = new Lv_PerfumeValues(perfName, perfCapacity, perfImage, perfName, perfEngName, perfBrand, perfCapacity + " / " + perfPrice + "원");
//                }
                perfList.add(inputPerf);
            }

            perfAdapter = new Lv_MyPerfumeAdapter(this, R.layout.lv_perf_result, perfList); // 리스트 형태 연결
            ap_lvResult.setAdapter(perfAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* 향수명과 용량을 통해 값 불러오기 */
    public class getPerfData extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(ActiPerfResult.this);
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

                    String data = URLEncoder.encode("perfName", "UTF-8") + "=" + URLEncoder.encode(sendName, "UTF-8")
                            + "&" + URLEncoder.encode("tempId", "UTF-8") + "=" + URLEncoder.encode(tempId, "UTF-8")
                            + "&" + URLEncoder.encode("perfCapacity", "UTF-8") + "=" + URLEncoder.encode(sendCapacity, "UTF-8");

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush(); //OutputStreamWriter 버퍼 메모리 비우기

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
            SharedPreferences.Editor editor = userInfo.edit();
            editor.putString("searchBox", "search");
            editor.commit();

            Log.i(TAG, str);
            perfumeJSON = str;
            Intent i = new Intent(ActiPerfResult.this, ActiPerfDetail.class);
            startActivity(i);
            asyncDialog.dismiss();
        }
    }

//    /* 뒤로가기 클릭하는 경우 */
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(ActiPerfResult.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        finish();
//        startActivity(intent);
//    }
}
