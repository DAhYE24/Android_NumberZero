package dh_kang.nozero.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import dh_kang.nozero.Adapter.Lv_BoardBasicAdapter;
import dh_kang.nozero.DataSet.Lv_BoardBasicValues;
import dh_kang.nozero.R;

public class acti_ActiBoard extends AppCompatActivity {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    ImageButton ab_btnWrite; // 글쓰기 버튼
    ListView ab_lvBoard; // 게시판 리스트뷰
    ImageView ab_imgNothing; // 이미지뷰 이미지 없을 때 보여줄 레이아웃

    /* JAVA 선언 */
    public static Context bContext;
    SharedPreferences userInfo;

    /* 네트워크 통신 */
    boardServer task;
    String myJSON;
    JSONArray vc = null;
    String tempId, tempTitle, tempDate, tempNum, tempContent, tempIcon;
    Lv_BoardBasicAdapter basicAdapter = null;

    /* 게시글 정보 보내기 */
    public static String sendSelectedInfo;

    public static String getSendSelectedInfo() {
        return sendSelectedInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_board);

        /* 초기화 */
        ab_btnWrite = (ImageButton)findViewById(R.id.ab_btnWrite);
        ab_lvBoard = (ListView)findViewById(R.id.ab_lvBoard);
        ab_imgNothing = (ImageView) findViewById(R.id.ab_imgNothing);

        /* 화면 보여주기 */
        ab_lvBoard.setVisibility(View.GONE);
        ab_imgNothing.setVisibility(View.GONE);
        bContext = this;

        /* 게시판 읽어오기 */
        loadBoardList();

        /* 게시글 선택하는 경우 */
        ab_lvBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lv_BoardBasicValues lvBba = (Lv_BoardBasicValues)parent.getItemAtPosition(position);
                /* 게시글의 정보 저장하기 */
                sendSelectedInfo = lvBba.getBasicIcon() + ",SP," + lvBba.getBasicId() + ",SP,"
                        + lvBba.getBasicTitle() + ",SP," + lvBba.getBasicContent() + ",SP,"
                        + lvBba.getBasicDate() + ",SP," + lvBba.getBasicNumber();

                /* 게시글 보기로 이동*/
                Intent i = new Intent(acti_ActiBoard.this, ActiBoardContent.class);
                startActivity(i);
            }
        });

        /* 글쓰기 버튼 */
        ab_btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 값 저장 */
                userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();
                editor.putBoolean("writeCheck", true); // 일반 글쓰기일 때는 true
                editor.commit();

                /* 글쓰기 창으로 이동 */
                Intent i = new Intent(acti_ActiBoard.this, ActiWrite.class);
                startActivity(i);
            }
        });
    }

    /* 게시판 읽어오기 */
    public void loadBoardList() {
        task = new boardServer();
        task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Ab_BoardLoad.php");
    }

    /* 게시판 읽어오기 */
    public class boardServer extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(acti_ActiBoard.this);

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
            if (str.trim().equals("200")) { // 게시글이 없는 경우
                ab_imgNothing.setVisibility(View.VISIBLE);
                ab_lvBoard.setVisibility(View.GONE);
            } else {
                ab_imgNothing.setVisibility(View.GONE);
                ab_lvBoard.setVisibility(View.VISIBLE);
                myJSON = str;
                showList();
            }
            asyncDialog.dismiss();
        }
    }

    protected void showList(){
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            vc = jsonObj.getJSONArray("results");

            ArrayList<Lv_BoardBasicValues> basicList = new ArrayList<Lv_BoardBasicValues>();
            Lv_BoardBasicValues inputBasic;

            for(int i=0;i<vc.length();i++) {
                JSONObject jo = vc.getJSONObject(i);
                tempIcon = jo.getString("userImage");
                tempNum = jo.getString("boardNum");
                tempId = jo.getString("boardId");
                tempTitle = jo.getString("boardTitle");
                tempContent = jo.getString("boardContent");
                tempDate = jo.getString("boardTime");

                inputBasic = new Lv_BoardBasicValues(tempIcon, tempId, tempTitle, tempDate, tempNum, tempContent);
                basicList.add(inputBasic);
            }

            basicAdapter = new Lv_BoardBasicAdapter(this, R.layout.lv_board, basicList); // 리스트 형태 연결
            ab_lvBoard.setAdapter(basicAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
