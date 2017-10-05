package dh_kang.nozero.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

import dh_kang.nozero.DataSet.PerfumeValues;
import dh_kang.nozero.Fragment.FragMain;
import dh_kang.nozero.Fragment.SearchFragment;
import dh_kang.nozero.Adapter.PerfumeResultAdapter;
import dh_kang.nozero.R;

public class PerfumeResultActivity extends AppCompatActivity {
    private static final String TAG = "NOZERO_FINAL";   /* LOG TEST */
    private RecyclerView list_resultBox;     /* Declare xml components */
    /* Declare java components */
    
    private String perfName, perfPrice, perfBrand, perfCapacity, perfEngName, perfImage;
    String rJson;
    SharedPreferences userInfo;
    JSONArray vc = null;
    String tempName, tempEngName;
    String sendName, sendCapacity;
    String tempId;

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
        setContentView(R.layout.page_perfume_result);

//        /* 일반검색인지 메인검색인지 구별 */
//        userInfo = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        String tempType = userInfo.getString("searchPerf", "");
//
//        /* 값 받아오기 */
//        if (tempType.equals("main")) {
//            rJson = fm.getSpecificJSON();
//        } else if (tempType.equals("search")) {
//            rJson = fs.getResultList();
//        }

        /* Init RecyclerView */
        list_resultBox = (RecyclerView) findViewById(R.id.list_resultBox);
        PerfumeValues perfumeValues = null;// TODO : 데이터 연결시키기
//        ItemData itemsData[] = { new ItemData("Help",R.drawable.help),
//                new ItemData("Delete",R.drawable.content_discard),
//                new ItemData("Cloud",R.drawable.collections_cloud),
//                new ItemData("Favorite",R.drawable.rating_favorite),
//                new ItemData("Like",R.drawable.rating_good),
//                new ItemData("Rating",R.drawable.rating_important)};
        list_resultBox.setLayoutManager(new LinearLayoutManager(this));
        PerfumeResultAdapter perfumeResultAdapter = new PerfumeResultAdapter(perfumeValues);
        list_resultBox.setAdapter(perfumeResultAdapter);
        list_resultBox.setItemAnimator(new DefaultItemAnimator());

//        /* 검색결과 향수 보여주기 */
//        showPerfumeInfo();
//
//        /* 사용자의 조합ID 받아오기 */
//        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        tempId = userInfo.getString("userId", "");
//        Log.i(TAG, tempId);
//
//        /* 클릭하는 경우 */
//        list_resultBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PerfumeValues lvPerf = (PerfumeValues) parent.getItemAtPosition(position);
//                sendName = lvPerf.getSearchName();
//                sendCapacity = lvPerf.getSearchCapacity();
//                task = new getPerfData();
//                task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Apr_PerfDetail.php");
//            }
//        });
    }

    /* 검색결과 향수 보여주기 */
    private void showPerfumeInfo() {
        try {
            JSONObject jsonObj = new JSONObject(rJson);
            vc = jsonObj.getJSONArray("results");

            ArrayList<PerfumeValues> perfList = new ArrayList<PerfumeValues>();
            PerfumeValues inputPerf;

            for (int i = 0; i < vc.length(); i++) {
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
//                    inputPerf = new PerfumeValues(perfName, perfCapacity, perfImage, tempName, perfEngName, perfBrand, perfCapacity + " / " + perfPrice + "원");
//                }else if(perfEngName.length() > 25){
//                    tempEngName = perfEngName.substring(0, 23) + "..";
//                    inputPerf = new PerfumeValues(perfName, perfCapacity, perfImage, perfName, tempEngName, perfBrand, perfCapacity + " / " + perfPrice + "원");
//                }else if(perfName.length() > 15 && perfEngName.length() > 25){
//                    tempName =  perfName.substring(0, 13) + "..";
//                    tempEngName = perfEngName.substring(0, 23) + "..";
//                    inputPerf = new PerfumeValues(perfName, perfCapacity, perfImage, tempName, tempEngName, perfBrand, perfCapacity + " / " + perfPrice + "원");
//                }else{
//                    inputPerf = new PerfumeValues(perfName, perfCapacity, perfImage, perfName, perfEngName, perfBrand, perfCapacity + " / " + perfPrice + "원");
//                }
//                perfList.add(inputPerf);
            }

//            perfumeResultAdapter = new PerfumeResultAdapter(this, R.layout.lv_perfume_result, perfList); // 리스트 형태 연결
//            list_resultBox.setAdapter(perfumeResultAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* 향수명과 용량을 통해 값 불러오기 */
    public class getPerfData extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(PerfumeResultActivity.this);

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
            SharedPreferences.Editor editor = userInfo.edit();
            editor.putString("searchBox", "search");
            editor.commit();

            Log.i(TAG, str);
            perfumeJSON = str;
            Intent i = new Intent(PerfumeResultActivity.this, ActiPerfDetail.class);
            startActivity(i);
            asyncDialog.dismiss();
        }
    }

//    /* 뒤로가기 클릭하는 경우 */
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(PerfumeResultActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        finish();
//        startActivity(intent);
//    }
}
