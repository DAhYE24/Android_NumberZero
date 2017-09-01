package dh_kang.nozero.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import dh_kang.nozero.Activity.ActiPerfResult;
import dh_kang.nozero.Activity.ActiStory;
import dh_kang.nozero.Activity.acti_ActiBoard;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-10-14.
 */
public class FragMain extends Fragment {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    ViewFlipper fm_viewFlipper;
    ImageView fm_imgAd1, fm_imgAd2, fm_imgAd3, fm_imgStory1, fm_imgStory2, fm_imgStory3;
    EditText fm_editPerfume;
    ImageButton fm_btnSearchByName;

    /* JAVA 선언 */
    SharedPreferences userInfo;

    /* 네트워크 통신 */
    getSpecificData task;
    String tempName;
    public static String specificJSON;

    public static String getSpecificJSON() {
        return specificJSON;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View v = inflater.inflate(R.layout.frag_main, container, false );

        /* 초기화 */
        fm_viewFlipper = (ViewFlipper)v.findViewById(R.id.fm_viewAd);
        fm_imgAd1 = (ImageView)v.findViewById(R.id.fm_imgAd1);
        fm_imgAd2 = (ImageView)v.findViewById(R.id.fm_imgAd2);
        fm_imgAd3 = (ImageView)v.findViewById(R.id.fm_imgAd3);
        fm_imgStory1 = (ImageView)v.findViewById(R.id.fm_imgStory1);
        fm_imgStory2 = (ImageView)v.findViewById(R.id.fm_imgStory2);
        fm_imgStory3 = (ImageView)v.findViewById(R.id.fm_imgStory3);
        fm_editPerfume = (EditText)v.findViewById(R.id.fm_editPerfume);
        fm_btnSearchByName = (ImageButton)v.findViewById(R.id.fm_btnSearchByName);

        /* 광고 뷰플리퍼 */
        fm_viewFlipper.setFlipInterval(2000);
        fm_viewFlipper.startFlipping();

        fm_imgAd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://department.ssg.com/item/itemView.ssg?itemId=1000019660018");
                Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
            }
        });

        fm_imgAd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.oliveyoungshop.com/prd/detail_cate.jsp?item_cd=38699642");
                Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
            }
        });

        fm_imgAd1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://innisfree.co.kr/event/greenchristmas2016/gatePc.jsp#p2");
                Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
            }
        });

        /* 스토리 */
        fm_imgStory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ActiStory.class);
                startActivity(i);
            }
        });

        fm_imgStory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), acti_ActiBoard.class);
                startActivity(i);
            }
        });

        /* SharedPrferences */
        userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        /* 검색 기능 */
        fm_btnSearchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = userInfo.edit();
                editor.putString("searchPerf", "main"); // 닉네임 저장
                editor.commit();

                tempName = String.valueOf(fm_editPerfume.getText());
                task = new getSpecificData();
                task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Fm_SpecificSearch.php");
            }
        });

        return v;
    }

    /* 향수명과 용량을 통해 값 불러오기 */
    public class getSpecificData extends AsyncTask<String, Integer, String> {
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

                    String data = URLEncoder.encode("perfName", "UTF-8") + "=" + URLEncoder.encode(tempName, "UTF-8");

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
            Log.i(TAG, str);
            specificJSON = str;
            Intent i = new Intent(getContext(), ActiPerfResult.class);
            startActivity(i);
            asyncDialog.dismiss();
        }
    }
}