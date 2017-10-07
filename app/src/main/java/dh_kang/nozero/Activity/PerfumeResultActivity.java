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
    private String txt_resultPerfName, perfPrice, txt_resultPerfBrand, perfCapacity, txt_resultPerfEngName, img_resultPerfImg;

    String rJson;
    SharedPreferences userInfo;
    JSONArray vc = null;
    String sendName, sendCapacity;
    String tempId;
//    getPerfData task;
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

        /* 검색결과 받아와서 적용하기 */
        getPerfumesInfoAndShow();

//        PerfumeValues perfumeValues = null;// TODO : 데이터 연결시키기
//        ItemData itemsData[] = { new ItemData("Help",R.drawable.help),
//                new ItemData("Delete",R.drawable.content_discard),
//                new ItemData("Cloud",R.drawable.collections_cloud),
//                new ItemData("Favorite",R.drawable.rating_favorite),
//                new ItemData("Like",R.drawable.rating_good),
//                new ItemData("Rating",R.drawable.rating_important)};
//        list_resultBox.setLayoutManager(new LinearLayoutManager(this));
//        PerfumeResultAdapter perfumeResultAdapter = new PerfumeResultAdapter(perfumeValues);
//        list_resultBox.setAdapter(perfumeResultAdapter);
//        list_resultBox.setItemAnimator(new DefaultItemAnimator());

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
    private void getPerfumesInfoAndShow() {
//        try {
//            JSONObject jsonObj = new JSONObject(rJson);
//            vc = jsonObj.getJSONArray("results");

//            ArrayList<PerfumeValues> perfList = new ArrayList<PerfumeValues>();

//            for (int i = 0; i < vc.length(); i++) {
//                JSONObject jo = vc.getJSONObject(i);
//                txt_resultPerfName = jo.getString("txt_resultPerfName");
//                perfPrice = jo.getString("perfPrice");
//                img_resultPerfImg = jo.getString("img_resultPerfImg");
//                txt_resultPerfEngName = jo.getString("txt_resultPerfEngName");
//                txt_resultPerfBrand = jo.getString("txt_resultPerfBrand");
//                perfCapacity = jo.getString("perfCapacity");

        /* 테스트용 => 나중에 지우기 */
        txt_resultPerfName = "향수명";
        perfPrice = "36000";
        txt_resultPerfBrand = "브랜드";
        perfCapacity = "30ml";
        txt_resultPerfEngName = "Perfume name";
        img_resultPerfImg = "http://mblogthumb1.phinf.naver.net/MjAxNzAzMDFfMjkx/MDAxNDg4Mzc2NTAyNjYw.Fp9maEC8x6AiOPRtYsZRJhTZPZyq0aAarlK_Y1j927Ig.WIDo6AODa_rXmpbcnX1OJPnBJhqxhpiFOQW4120Y51Qg.PNG.hydroin303/%ED%94%BC%EC%B9%B4%EC%B8%84.png?type=w800";

        /* 출력하기 + 길이에 따라 다르게 표현하기*/
        String tempPerfName = (txt_resultPerfName.length() > 15)? txt_resultPerfName.substring(0, 13) + "..." : txt_resultPerfName;
        String tempPerfEngName = (txt_resultPerfEngName.length() > 25)? txt_resultPerfEngName.substring(0, 23) + "..." : txt_resultPerfEngName;

        /* 받아온 데이터 적용하기 */
        /* TODO : 실제 데이터 적용할 때는 for문으로 돌려서 배열에 데이터 적용하기*/
        PerfumeValues[] perfumeValues = {new PerfumeValues(tempPerfName, perfCapacity, img_resultPerfImg, tempPerfEngName, txt_resultPerfBrand, perfPrice)};
        list_resultBox.setLayoutManager(new LinearLayoutManager(this));
        PerfumeResultAdapter perfumeResultAdapter = new PerfumeResultAdapter(this, perfumeValues);
        list_resultBox.setAdapter(perfumeResultAdapter);
        list_resultBox.setItemAnimator(new DefaultItemAnimator());

//        perfumeResultAdapter = new PerfumeResultAdapter(this, R.layout.lv_perfume_result, perfList); // 리스트 형태 연결
//        list_resultBox.setAdapter(perfumeResultAdapter);

//        if (txt_resultPerfName.length() > 15) {
//            tempPerfName = txt_resultPerfName.substring(0, 13) + "...";
//            perfumeValues = new PerfumeValues(tempPerfName, perfCapacity, img_resultPerfImg, txt_resultPerfEngName, txt_resultPerfBrand, perfPrice);
//        } else if (txt_resultPerfEngName.length() > 25) {
//            tempPerfEngName = txt_resultPerfEngName.substring(0, 23) + "...";
//            perfumeValues = new PerfumeValues(txt_resultPerfName, perfCapacity, img_resultPerfImg, txt_resultPerfName, tempPerfEngName, txt_resultPerfBrand, perfCapacity + " / " + perfPrice + "원");
//        } else if (txt_resultPerfName.length() > 15 && txt_resultPerfEngName.length() > 25) {
//            tempPerfName = txt_resultPerfName.substring(0, 13) + "..";
//            tempPerfEngName = txt_resultPerfEngName.substring(0, 23) + "...";
//            perfumeValues = new PerfumeValues(txt_resultPerfName, perfCapacity, img_resultPerfImg, tempPerfName, tempPerfEngName, txt_resultPerfBrand, perfCapacity + " / " + perfPrice + "원");
//        } else {
//            perfumeValues = new PerfumeValues(txt_resultPerfName, perfCapacity, img_resultPerfImg, txt_resultPerfName, txt_resultPerfEngName, txt_resultPerfBrand, perfCapacity + " / " + perfPrice + "원");
//        }
//                perfList.add(perfumeValues);
//}
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

//    /* 향수명과 용량을 통해 값 불러오기 */
//    public class getPerfData extends AsyncTask<String, Integer, String> {
//        ProgressDialog asyncDialog = new ProgressDialog(PerfumeResultActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            asyncDialog.setMessage("..로딩중..");
//            asyncDialog.show();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... urls) {
//            StringBuilder jsonHtml = new StringBuilder();
//            try {
//                URL url = new URL(urls[0]); // 연결 url 설정
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 커넥션 객체 생성
//                if (conn != null) { // 연결된 경우
//                    conn.setConnectTimeout(3000);
//                    conn.setUseCaches(false);
//                    conn.setDoOutput(true);
//
//                    String data = URLEncoder.encode("txt_resultPerfName", "UTF-8") + "=" + URLEncoder.encode(sendName, "UTF-8")
//                            + "&" + URLEncoder.encode("tempId", "UTF-8") + "=" + URLEncoder.encode(tempId, "UTF-8")
//                            + "&" + URLEncoder.encode("perfCapacity", "UTF-8") + "=" + URLEncoder.encode(sendCapacity, "UTF-8");
//
//                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//                    wr.write(data);
//                    wr.flush(); //OutputStreamWriter 버퍼 메모리 비우기
//
//                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) { // 연결 후 코드가 리턴
//                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                        for (; ; ) {  // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
//                            String line = br.readLine();
//                            if (line == null) break;
//                            jsonHtml.append(line + "\n"); // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
//                        }
//                        br.close();
//                    }
//                    conn.disconnect();
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            return jsonHtml.toString();
//        }
//
//        protected void onPostExecute(String str) {
//            SharedPreferences.Editor editor = userInfo.edit();
//            editor.putString("searchBox", "search");
//            editor.commit();
//
//            Log.i(TAG, str);
//            perfumeJSON = str;
//            Intent i = new Intent(PerfumeResultActivity.this, ActiPerfDetail.class);
//            startActivity(i);
//            asyncDialog.dismiss();
//        }
//    }

    /* Clicking back button case : To reload .Activity to make unchecked states */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PerfumeResultActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
