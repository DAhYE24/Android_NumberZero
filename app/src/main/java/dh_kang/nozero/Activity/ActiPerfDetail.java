package dh_kang.nozero.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import dh_kang.nozero.Dialog.DialBox;
import dh_kang.nozero.R;
import dh_kang.nozero.IntegratedClass.TextViewHelper;

public class ActiPerfDetail extends AppCompatActivity {
    private static final String TAG = "NOZERO_FINAL";   /* LOG TEST */
    TextView txt_likeCount;   /* Declare xml components */
    ImageView img_detailPerfImg;
    TextView txt_detailPerfBrand, txt_detailPerfName, txt_detailPerfEngName, txt_detailPerfAddition;
    RelativeLayout btn_storeThis, btn_priceCompare, btn_likeThis, btn_askRevision;
    TextView txt_perfType, txt_perfCountry, txt_perfIntro, txt_flavors_info, txt_components;

    String perfName, perfEngName, perfPrice, perfBrand, perfFla, perfCountry;    /* Declare java parameters */
    String perfCapacity, perfType, perfImage, perfIntro, perfLike; // 향수 데이터 임시 저장
    String rJson;
    Boolean userCheck = false; // 유저 체크, false일 때는 로그인 화면
    int tempLike;
    JSONArray vc = null;
    SharedPreferences userInfo;
    String tempId; // 유저아이디
    String vLike, vBox;

    /* 네트워크 통신 */
    perfServer task;
    int phpChk; // 0: 향수추천, 1: 향수보관, 2: 추천취소, 3: 보관취소

    @Override
    protected void onPause() {
        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putBoolean("pauseCheck", true);// 뒤로 갔다 온건지 확인
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_perfume_details);

        /* Init xml widgets */
        txt_likeCount = (TextView) findViewById(R.id.txt_likeCount);
        img_detailPerfImg = (ImageView) findViewById(R.id.img_detailPerfImg);
        txt_detailPerfBrand = (TextView) findViewById(R.id.txt_detailPerfBrand);
        txt_detailPerfName = (TextView) findViewById(R.id.txt_detailPerfName);
        txt_detailPerfEngName = (TextView) findViewById(R.id.txt_detailPerfEngName);
        txt_detailPerfAddition = (TextView) findViewById(R.id.txt_detailPerfAddition);
        btn_priceCompare = (RelativeLayout) findViewById(R.id.btn_priceCompare);
        btn_likeThis = (RelativeLayout) findViewById(R.id.btn_likeThis);
        btn_storeThis = (RelativeLayout) findViewById(R.id.btn_storeThis);
        btn_askRevision = (RelativeLayout) findViewById(R.id.btn_askRevision);
        txt_perfType = (TextView) findViewById(R.id.txt_perfType);
        txt_perfCountry = (TextView) findViewById(R.id.txt_perfCountry);
        txt_perfIntro = (TextView) findViewById(R.id.txt_perfIntro);
        txt_flavors_info = (TextView) findViewById(R.id.txt_flavors_info);
        txt_components = (TextView) findViewById(R.id.txt_components);

        /* 지난 액티비티 창에서 php 전달 값 불러오기 */
        userInfo = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String tempType = userInfo.getString("searchBox", "search");

        /* 유저 여부 체크 */
        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userCheck = userInfo.getBoolean("userCheck", false);

        if (tempType.equals("search")) {
            PerfumeResultActivity asr = new PerfumeResultActivity();
            rJson = asr.getPerfumeJSON();
        } else {
            DialBox db = null;
            rJson = db.getBoxJSON();
        }

        /* 화면에 데이터 적용하기 */
        putPerfumeInfo();

        /* 가격비교 버튼 클릭 */
        btn_priceCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://m.shopping.naver.com/search/all.nhn?query=" + perfName + " " + perfCapacity);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        /* 유저 조합아이디 받아오기 */
        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        tempId = userInfo.getString("userId", "");

        /* 추천하기 버튼 클릭 */
        btn_likeThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userCheck == false) {
                    Toast.makeText(ActiPerfDetail.this, "해당 기능은 넘버제로 회원만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (vLike.equals("1")) { // 이미 추천이 되어있는 상태
                        AlertDialog dialog = deleteLike();
                        dialog.show();
                    } else {
                        /* 추천수 증가시키기 */
                        tempLike = Integer.parseInt(perfLike);
                        tempLike += 1;

                        /* 서버 연결 */
                        phpChk = 0; //향수 추천
                        task = new perfServer();
                        task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Apd_Like.php");
                    }
                }
            }

            private AlertDialog deleteLike() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActiPerfDetail.this);
                builder.setTitle("추천취소");
                builder.setMessage("추천을 취소하시겠습니까?");

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        if (userInfo.getBoolean("pauseCheck", true) == true) { // 뒤로 갔다가 온 상태
                            tempLike = Integer.parseInt(perfLike);
                        }
                        tempLike -= 1;
                        phpChk = 2;
                        task = new perfServer();
                        task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Apd_DeleteLike.php");
                    }
                });

                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                return dialog;
            }
        });

        /* 보관하기 버튼 클릭 */
        btn_storeThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userCheck == false) {
                    Toast.makeText(ActiPerfDetail.this, "해당 기능은 넘버제로 회원만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (vBox.equals("1")) {
                        AlertDialog dialog = deleteBox();
                        dialog.show();
                    } else {
                        /* 서버 연결 */
                        phpChk = 1;
                        task = new perfServer();
                        task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Apd_Box.php");
                    }
                }
            }

            private AlertDialog deleteBox() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActiPerfDetail.this);
                builder.setTitle("보관취소");
                builder.setMessage("보관을 취소하시겠습니까?");

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phpChk = 3;
                        task = new perfServer();
                        task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Apd_DeleteBox.php");
                    }
                });

                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                return dialog;
            }
        });
    }

    private void putPerfumeInfo() {
        try {
            JSONObject jsonObj = new JSONObject(rJson);
            vc = jsonObj.getJSONArray("results");

            for (int i = 0; i < vc.length(); i++) {
                JSONObject jo = vc.getJSONObject(i);
                vLike = jo.getString("vLike");
                vBox = jo.getString("vBox");
                perfLike = jo.getString("perfLike");
                perfName = jo.getString("perfName");
                perfEngName = jo.getString("perfEngName");
                perfPrice = jo.getString("perfPrice");
                perfBrand = jo.getString("perfBrand");
                perfFla = jo.getString("perfFla");
                perfCountry = jo.getString("perfCountry");
                perfCapacity = jo.getString("perfCapacity");
                perfImage = jo.getString("perfImage");
                perfType = jo.getString("perfType");
                perfIntro = jo.getString("perfIntro");
            }

            Glide.with(ActiPerfDetail.this).load(perfImage).override(100, 100).into(img_detailPerfImg);

            /* 이미 등록된 경우에는 선택된 버튼으로 변경 */
            if (vLike.equals("1"))
                btn_likeThis.setBackgroundResource(R.drawable.sbtn_samplechecked);
            if (vBox.equals("1"))
                btn_storeThis.setBackgroundResource(R.drawable.sbtn_samplechecked);

            txt_detailPerfName.setText(perfName);
            txt_detailPerfEngName.setText(perfEngName);
            txt_detailPerfBrand.setText(perfBrand);
            txt_detailPerfAddition.setText(perfCapacity + " / " + perfPrice + "원");
            txt_likeCount.setText("추천 " + perfLike);
            txt_perfType.setText(perfType);
            txt_perfCountry.setText(perfCountry);

            /* 레이아웃 가로 크기 */
            DisplayMetrics dm = this.getResources().getDisplayMetrics();
            int width = dm.widthPixels;

            /* 레이아웃에 불러온 정보 적용하기 */
            //fid_txtDetail.setText(tempDetail);
            txt_flavors_info.setText(TextViewHelper.shrinkWithWordUnit(txt_flavors_info, perfFla, width - 150));
            //txt_flavors_info.setText(perfFla);
            //txt_perfIntro.setText(perfIntro);
            txt_perfIntro.setText(TextViewHelper.shrinkWithWordUnit(txt_perfIntro, perfIntro, width - 150));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class perfServer extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(ActiPerfDetail.this);

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

                    String data = null;

                    if (phpChk == 0 || phpChk == 2) { // 향수 추천
                        data = URLEncoder.encode("perfLike", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(tempLike), "UTF-8")
                                + "&" + URLEncoder.encode("perfName", "UTF-8") + "=" + URLEncoder.encode(perfName, "UTF-8")
                                + "&" + URLEncoder.encode("likeId", "UTF-8") + "=" + URLEncoder.encode(tempId, "UTF-8")
                                + "&" + URLEncoder.encode("perfCapacity", "UTF-8") + "=" + URLEncoder.encode(perfCapacity, "UTF-8");
                    } else if (phpChk == 1 || phpChk == 3) { // 향수 보관
                        data = URLEncoder.encode("boxTitle", "UTF-8") + "=" + URLEncoder.encode(perfName, "UTF-8")
                                + "&" + URLEncoder.encode("boxCapacity", "UTF-8") + "=" + URLEncoder.encode(perfCapacity, "UTF-8")
                                + "&" + URLEncoder.encode("boxId", "UTF-8") + "=" + URLEncoder.encode(tempId, "UTF-8")
                                + "&" + URLEncoder.encode("boxImage", "UTF-8") + "=" + URLEncoder.encode(perfImage, "UTF-8");
                    }

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
            asyncDialog.dismiss();

            if (phpChk == 0) { // 향수 추천
                if (str.trim().equals("100")) {
                    Toast.makeText(ActiPerfDetail.this, "추천되었습니다", Toast.LENGTH_SHORT).show();
                    txt_likeCount.setText("추천 " + String.valueOf(tempLike));
                    btn_likeThis.setBackgroundResource(R.drawable.sbtn_samplechecked);
                    vLike = "1";

                    SharedPreferences.Editor editor = userInfo.edit();
                    editor.putBoolean("pauseCheck", false);// 뒤로 갔다 온건지 확인
                    editor.commit();
                } else {
                    Toast.makeText(ActiPerfDetail.this, "실패하였습니다", Toast.LENGTH_SHORT).show();
                }
            } else if (phpChk == 1) { // 향수 보관
                if (str.trim().equals("100")) {
                    Toast.makeText(ActiPerfDetail.this, "향수보관함에 저장되었습니다", Toast.LENGTH_SHORT).show();
                    btn_storeThis.setBackgroundResource(R.drawable.sbtn_samplechecked);
                    vBox = "1";
                } else {
                    Toast.makeText(ActiPerfDetail.this, "실패하였습니다", Toast.LENGTH_SHORT).show();
                }
            } else if (phpChk == 2) {
                if (str.trim().equals("100")) {
                    Toast.makeText(ActiPerfDetail.this, "추천을 취소하였습니다", Toast.LENGTH_SHORT).show();
                    txt_likeCount.setText("추천 " + String.valueOf(tempLike));
                    btn_likeThis.setBackgroundResource(R.drawable.sbtn_sample);
                    vLike = "0";
                } else {
                    Toast.makeText(ActiPerfDetail.this, "실패하였습니다", Toast.LENGTH_SHORT).show();
                }
            } else if (phpChk == 3) {
                if (str.trim().equals("100")) {
                    Toast.makeText(ActiPerfDetail.this, "보관을 취소하였습니다", Toast.LENGTH_SHORT).show();
                    btn_storeThis.setBackgroundResource(R.drawable.sbtn_sample);
                    vBox = "0";
                } else {
                    Toast.makeText(ActiPerfDetail.this, "실패하였습니다", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
