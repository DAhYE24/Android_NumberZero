package dh_kang.nozero.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import dh_kang.nozero.Activity.ActiMain;
import dh_kang.nozero.Dialog.Dialjoin;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-11-06.
 */
public class FragLogin extends Fragment{
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    Button fj_btnJoin, fj_btnLogin; // 버튼 : 가입, 로그인
    EditText fj_editId, fj_editPw; // 에디트텍스트 : 아이디, 비밀번호

    /* JAVA 선언 */
    Dialjoin joinDialog;
    SharedPreferences userInfo;

    /* 네트워크 통신 */
    loginPhp task;
    String tempId, tempPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.frag_login, container, false );

        /* 초기화 */
        fj_btnJoin = (Button)v.findViewById(R.id.fj_btnJoin);
        fj_btnLogin = (Button)v.findViewById(R.id.fj_btnLogin);
        fj_editId = (EditText)v.findViewById(R.id.fj_editId);
        fj_editPw = (EditText)v.findViewById(R.id.fj_editPw);

        /* 대화상자 초기화 */
        joinDialog = new Dialjoin(getActivity());
        joinDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 대화상자의 타이틀바 제거

        /* 에디트텍스트 설정 */
        fj_editPw.setTransformationMethod(PasswordTransformationMethod.getInstance());

        /* 가입 버튼 */
        fj_btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();
                editor.putBoolean("joinCheck", true);// 가입하는 경우
                editor.commit();

                joinDialog.show(); // 다이얼로그 박스 연결
            }
        });

        /* 로그인 버튼 */
        fj_btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 각 에디트텍스트의 값 받아오기 */
                tempId = fj_editId.getText().toString();
                tempPass = fj_editPw.getText().toString();

                if(tempId.length() == 0){ // ID 입력하지 않은 경우
                    Toast.makeText(getContext(), "닉네임 입력해주세요", Toast.LENGTH_SHORT).show();
                    fj_editId.requestFocus();
                }else if(tempPass.length() == 0){ // 비번 입력하지 않은 경우
                    Toast.makeText(getContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    fj_editPw.requestFocus();
                }else{ // 제대로 입력하면 서버와 통신
                    task = new loginPhp();
                    task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Fj_Login.php");
                }
            }
        });

        return v;
    }

    private class loginPhp extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(getContext());

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

                    String data  = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(tempId, "UTF-8")
                            + "&" + URLEncoder.encode("userPassword", "UTF-8") + "=" + URLEncoder.encode(tempPass, "UTF-8");

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

            if(str.trim().equals("200")){ // 로그인 실패
                Toast.makeText(getContext(), "아이디 또는 패스워드를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
            }else{ // 로그인이 되면
                /* 정보 저장해두기 */
                userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();
                editor.putString("userName", tempId); // 닉네임 저장
                editor.putString("userPassword", tempPass); // 비번 저장
                editor.putBoolean("userCheck", true);// 이미 가입했다는 것을 체크
                String [] tempStr = str.trim().split(",");
                editor.putString("userId", tempStr[0]); // 고유 ID 저장
                editor.putString("userEmail", tempStr[1]); // 이메일 저장
                editor.putString("userPicPath", tempStr[2]); // 이미지 저장

                editor.commit();

                /* 화면 재시작 */
                ((ActiMain) ActiMain.mContext).finish();
                Intent intent = new Intent(getContext(), ActiMain.class);
                ((ActiMain) ActiMain.mContext).startActivity(intent);
            }
        }
    }
}
