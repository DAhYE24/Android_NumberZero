package dh_kang.nozero.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-11-07.
 */
public class Dialjoin extends Dialog {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    Button dj_btnJoin; // 버튼 : 가입
    EditText dj_editId, dj_editEmail, dj_editPw, dj_editPwChk; // 에디트텍스트 : 닉네임, 이메일, 비밀번호, 비밀번호체크
    TextView dj_txtTitle;

    /* JAVA 선언 */;
    public Dialjoin(Context context) {
        super(context);
    } // 다이얼로그
    SharedPreferences userInfo;

    /* 네트워크 통신 */
    joinPhp task;
    String tempId, tempPass, tempEmail, tempDevice; // php를 통해서 보낼값
    String oldId, oldEmail;
    String tempImg = "http://i.imgur.com/HKmzkfx.png";
    Boolean tempType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dial_join);

        /* 초기화 */
        dj_btnJoin = (Button)findViewById(R.id.dj_btnJoin);
        dj_editEmail = (EditText)findViewById(R.id.dj_editEmail);
        dj_editId = (EditText)findViewById(R.id.dj_editId);
        dj_editPw = (EditText)findViewById(R.id.dj_editPw);
        dj_editPwChk = (EditText)findViewById(R.id.dj_editPwChk);
        dj_txtTitle = (TextView)findViewById(R.id.dj_txtTitle);

        /* 에디트텍스트 설정 */
        dj_editPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        dj_editPwChk.setTransformationMethod(PasswordTransformationMethod.getInstance());

        /* 디바이스 정보 */
        tempDevice = "MODEL: " + Build.MODEL;

        /* 수정하는 경우 */
        userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        tempType = userInfo.getBoolean("joinCheck", true);

        if(tempType == false){ //수정하는 경우
            /* 문구 바꾸기 */
            dj_txtTitle.setText("정보수정");
            dj_btnJoin.setText("수정하기");

            dj_editId.setText(userInfo.getString("userId", "").toString()); // 닉네임
            dj_editPw.setText(userInfo.getString("userPassword", "").toString()); // 비번
            dj_editEmail.setText(userInfo.getString("userEmail", "").toString()); // 이메일

            oldId = userInfo.getString("userId", "").toString();
            oldEmail = userInfo.getString("userEmail", "").toString();
        }


        /* 가입 버튼 */
        dj_btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dj_editId.length() == 0){ // ID 입력하지 않은 경우
                    Toast.makeText(getContext(), "닉네임 입력해주세요", Toast.LENGTH_SHORT).show();
                    dj_editId.requestFocus();
                }else if(dj_editPw.length() == 0){ // 비번 입력하지 않은 경우
                    Toast.makeText(getContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    dj_editPw.requestFocus();
                }else if(dj_editPwChk.length() == 0){ // 비번 체크 입력하지 않은 경우
                    Toast.makeText(getContext(), "비밀번호 확인을 입력해주세요", Toast.LENGTH_SHORT).show();
                    dj_editPwChk.requestFocus();
                }else if(dj_editEmail.length() == 0){ // 이메일 입력하지 않은 경우
                    Toast.makeText(getContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    dj_editEmail.requestFocus();
                }else if(dj_editPw.length() > 20 || dj_editPw.length() < 5){
                    Toast.makeText(getContext(), "비밀번호를 5-20자 사이로 입력해주세요", Toast.LENGTH_SHORT).show();
                    dj_editPw.requestFocus();
                }else{
                    if(!dj_editPw.getText().toString().equals(dj_editPwChk.getText().toString())){ // 비밀번호 확인
                        Toast.makeText(getContext(), "비밀번호가 동일하지않습니다", Toast.LENGTH_SHORT).show();
                    }else{
                        /* 입력값 저장 */
                        tempId = dj_editId.getText().toString();
                        tempPass = dj_editPw.getText().toString();
                        tempEmail = dj_editEmail.getText().toString();


                        if(tempType == false) { // 수정할 때
                            /* 네트워크 통신*/
                            task = new joinPhp();
                            task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Dj_Modify.php");
                        }else{ // 가입할 때
                            /* 네트워크 통신*/
                            task = new joinPhp();
                            task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Dj_Join.php");
                        }
                    }
                }
            }
        });
    }

    public class joinPhp extends AsyncTask<String, Integer, String> {
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
                    if(tempType == false) { // 수정할 때
                        data  = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(tempId, "UTF-8")
                                + "&" + URLEncoder.encode("userPassword", "UTF-8") + "=" + URLEncoder.encode(tempPass, "UTF-8")
                                + "&" + URLEncoder.encode("userEmail", "UTF-8") + "=" + URLEncoder.encode(tempEmail, "UTF-8")
                                + "&" + URLEncoder.encode("oldId", "UTF-8") + "=" + URLEncoder.encode(oldId, "UTF-8")
                                + "&" + URLEncoder.encode("oldEmail", "UTF-8") + "=" + URLEncoder.encode(oldEmail, "UTF-8");
                    }else { // 가입할 때
                        data  = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(tempId, "UTF-8")
                                + "&" + URLEncoder.encode("userPassword", "UTF-8") + "=" + URLEncoder.encode(tempPass, "UTF-8")
                                + "&" + URLEncoder.encode("userEmail", "UTF-8") + "=" + URLEncoder.encode(tempEmail, "UTF-8")
                                + "&" + URLEncoder.encode("userImage", "UTF-8") + "=" + URLEncoder.encode(tempImg, "UTF-8")
                                + "&" + URLEncoder.encode("userDevice", "UTF-8") + "=" + URLEncoder.encode(tempDevice, "UTF-8");
                    }

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
            asyncDialog.dismiss();

            if(str.trim().equals("201")) { // 가입 실패시(닉네임)
                Toast.makeText(getContext(), "이미 존재하는 닉네임입니다", Toast.LENGTH_SHORT).show();
                dj_editId.requestFocus();
            }else if(str.trim().equals("202")) { // 가입 실패시(이메일)
                Toast.makeText(getContext(), "이미 존재하는 이메일입니다", Toast.LENGTH_SHORT).show();
                dj_editEmail.requestFocus();
            }else {// 가입이 되면
                userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();

                /* 에디트텍스트 값 받아오기 */
                tempId = dj_editId.getText().toString();
                tempPass = dj_editPw.getText().toString();
                tempEmail = dj_editEmail.getText().toString();

                /* 캐시에 저장해두기 */
                editor.putString("userName", tempId); // 닉네임 저장
                editor.putString("userPassword", tempPass); // 비번 저장
                editor.putString("userEmail", tempEmail); // 이메일저장
                editor.putString("userId", str.trim()); // 아이디 저장, str에는 자동생성된 아이디가 반환됨

                /* 처음 가입한 경우면 이미지 주소를 기본으로 저장시키켜둠 */
                if(tempType == true)    editor.putString("userPicPath", tempImg); // 이미지 주소 저장
                editor.putBoolean("userCheck", true);// 유저 가입 체크
                editor.commit();
//
//                if(tempType == false) { // 수정할 때
//                    Toast.makeText(getContext(), "정보가 수정되었습니다", Toast.LENGTH_SHORT).show();
//                    ((MainActivity) MainActivity.mContext).ChangeFragment(((MainActivity) MainActivity.mContext).findViewById(R.id.am_btnMy));
//                }else{ // 가입할 때
//                    Toast.makeText(getContext(), "넘버제로에 가입되었습니다", Toast.LENGTH_LONG).show();
//                    ((MainActivity) MainActivity.mContext).finish();
//                    Intent intent = new Intent(getContext(), MainActivity.class);
//                    ((MainActivity) MainActivity.mContext).startActivity(intent);
//                }

                /* 다이얼로그 dismiss */
                dismiss();
            }
        }
    }
}
