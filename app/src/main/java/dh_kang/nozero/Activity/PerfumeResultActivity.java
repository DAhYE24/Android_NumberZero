package dh_kang.nozero.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;

import org.json.JSONArray;

import dh_kang.nozero.DataSet.PerfumeValues;
import dh_kang.nozero.Fragment.FragMain;
import dh_kang.nozero.Fragment.SearchFragment;
import dh_kang.nozero.Adapter.PerfumeResultAdapter;
import dh_kang.nozero.R;

public class PerfumeResultActivity extends AppCompatActivity {
    private static final String TAG = "NOZERO_FINAL";   /* LOG TEST */
    private RecyclerView list_resultBox;     /* Declare xml components */
    /* Declare java components */
    private int perfumeIdx;
    private String perfumeName, perfumeEnglishName, perfumeImageUrl, perfumeBrand, perfumePrice, perfumeCapacity;

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
        setContentView(R.layout.activity_perfume_result);

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

        /* Get data from server and put into recyclerview */
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

    /* Get data from server and put into recyclerview */
    private void getPerfumesInfoAndShow() {
//        try {
//            JSONObject jsonObj = new JSONObject(rJson);
//            vc = jsonObj.getJSONArray("results");

//            ArrayList<PerfumeValues> perfList = new ArrayList<PerfumeValues>();

//            for (int i = 0; i < vc.length(); i++) {
//                JSONObject jo = vc.getJSONObject(i);
//                perfumeName = jo.getString("perfumeName");
//                perfumePrice = jo.getString("perfumePrice");
//                perfumeImageUrl = jo.getString("perfumeImageUrl");
//                perfumeEnglishName = jo.getString("perfumeEnglishName");
//                perfumeBrand = jo.getString("perfumeBrand");
//                perfumeCapacity = jo.getString("perfumeCapacity");

        /* 테스트용 => 나중에 지우기 */
        perfumeIdx = 1;
        perfumeName = "향수명";
        perfumePrice = "36000";
        perfumeBrand = "브랜드";
        perfumeCapacity = "30";
        perfumeEnglishName = "Perfume name";
        perfumeImageUrl = "http://mblogthumb1.phinf.naver.net/MjAxNzAzMDFfMjkx/MDAxNDg4Mzc2NTAyNjYw.Fp9maEC8x6AiOPRtYsZRJhTZPZyq0aAarlK_Y1j927Ig.WIDo6AODa_rXmpbcnX1OJPnBJhqxhpiFOQW4120Y51Qg.PNG.hydroin303/%ED%94%BC%EC%B9%B4%EC%B8%84.png?type=w800";

        /* 출력하기 + 길이에 따라 다르게 표현하기*/
        String tempPerfumeName = (perfumeName.length() > 15) ? perfumeName.substring(0, 13) + "..." : perfumeName;
        String tempPerfumeEngName = (perfumeEnglishName.length() > 25) ? perfumeEnglishName.substring(0, 23) + "..." : perfumeEnglishName;

        /* 받아온 데이터 적용하기 */
        /* TODO : 실제 데이터 적용할 때는 for문으로 돌려서 배열에 데이터 적용하기*/
        PerfumeValues[] perfumeValues = {new PerfumeValues(perfumeIdx, tempPerfumeName, tempPerfumeEngName, perfumeImageUrl, perfumeBrand, perfumePrice, perfumeCapacity),
                new PerfumeValues(perfumeIdx, tempPerfumeName, tempPerfumeEngName, perfumeImageUrl, perfumeBrand, perfumePrice, perfumeCapacity)};
        list_resultBox.setLayoutManager(new LinearLayoutManager(this));
        PerfumeResultAdapter perfumeResultAdapter = new PerfumeResultAdapter(this, perfumeValues);
        list_resultBox.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.shape_recyclerview_divider), true));
        list_resultBox.setAdapter(perfumeResultAdapter);
        list_resultBox.setItemAnimator(new DefaultItemAnimator());
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
//                    String data = URLEncoder.encode("perfumeName", "UTF-8") + "=" + URLEncoder.encode(sendName, "UTF-8")
//                            + "&" + URLEncoder.encode("tempId", "UTF-8") + "=" + URLEncoder.encode(tempId, "UTF-8")
//                            + "&" + URLEncoder.encode("perfumeCapacity", "UTF-8") + "=" + URLEncoder.encode(sendCapacity, "UTF-8");
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
