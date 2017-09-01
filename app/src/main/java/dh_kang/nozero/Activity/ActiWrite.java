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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import dh_kang.nozero.R;

public class ActiWrite extends AppCompatActivity {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    EditText aw_editContent, aw_editTitle; // 글 내용, 글 제목
    Button aw_btnWrite; // 글 쓰기 버튼
    String writeId, writeTitle, writeContent, writeDate, writeNum;

    /* JAVA 선언 */
    SharedPreferences userInfo;
    Boolean writeChk;

    /* 데이터 통신 */
    saveWriting task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_write);

        /* 초기화 */
        aw_btnWrite = (Button)findViewById(R.id.aw_btnWrite);
        aw_editContent = (EditText)findViewById(R.id.aw_editContent);
        aw_editTitle = (EditText)findViewById(R.id.aw_editTitle);

        //아이디 가져오기
        userInfo = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        writeId = userInfo.getString("userId", "");
        writeChk = userInfo.getBoolean("writeCheck", false); //true : 일반글쓰기, false:글수정하기

        Log.i(TAG, String.valueOf(writeChk));

        /* 글 수정할 때 내용 가져오기 */
        if(writeChk == false){
            ActiBoardContent abc = null;
            String [] tempWrite = abc.getTempWrite().split(",SP,");
            aw_editTitle.setText(tempWrite[0]);
            aw_editContent.setText(tempWrite[1]);
            writeNum = tempWrite[2];
            aw_btnWrite.setText("수정하기");
        }

        /* 글 등록 */
        aw_btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 글 쓴 시간 가져오기 */
                SimpleDateFormat forDatetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date getTime = new Date();
                writeDate = forDatetime.format(getTime);

                /* 에디트텍스트에서 데이터 가져오기 */
                writeTitle = aw_editTitle.getText().toString();
                writeContent = aw_editContent.getText().toString();

                /* 내용의 길이에 따른 설정 */
                if(writeTitle.length() == 0){
                    Toast.makeText(ActiWrite.this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else if(writeContent.length() == 0){
                    Toast.makeText(ActiWrite.this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    if(writeChk == true){ /* 일반 글쓰기 */
                        task = new saveWriting();
                        task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Aw_Write.php");
                    }else{ /* 글 수정하기 */
                        task = new saveWriting();
                        task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Aw_Rewrite.php");
                    }

                }
            }
        });
    }

    public class saveWriting extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(ActiWrite.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("..로딩중..");

            // show dialog
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
                    if(writeChk == true) { /* 일반 글쓰기 */
                        data  = URLEncoder.encode("boardId", "UTF-8") + "=" + URLEncoder.encode(writeId, "UTF-8")
                                + "&" + URLEncoder.encode("boardTitle", "UTF-8") + "=" + URLEncoder.encode(writeTitle, "UTF-8")
                                + "&" + URLEncoder.encode("boardContent", "UTF-8") + "=" + URLEncoder.encode(writeContent, "UTF-8")
                                + "&" + URLEncoder.encode("boardTime", "UTF-8") + "=" + URLEncoder.encode(writeDate, "UTF-8");
                    }else{ /* 글 수정 */
                        data  = URLEncoder.encode("boardId", "UTF-8") + "=" + URLEncoder.encode(writeId, "UTF-8")
                                + "&" + URLEncoder.encode("boardTitle", "UTF-8") + "=" + URLEncoder.encode(writeTitle, "UTF-8")
                                + "&" + URLEncoder.encode("boardContent", "UTF-8") + "=" + URLEncoder.encode(writeContent, "UTF-8")
                                + "&" + URLEncoder.encode("boardTime", "UTF-8") + "=" + URLEncoder.encode(writeDate, "UTF-8")
                                + "&" + URLEncoder.encode("boardNum", "UTF-8") + "=" + URLEncoder.encode(writeNum, "UTF-8");
                    }

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);//onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
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
            asyncDialog.dismiss();
            if(str.trim().equals("100")){
                Toast.makeText(ActiWrite.this, "글이 등록되었습니다", Toast.LENGTH_SHORT).show();

                if(writeChk == true) { /* 일반 글쓰기 */
                    ((acti_ActiBoard) acti_ActiBoard.bContext).loadBoardList(); // 게시글 새로 고침
                }else{ /* 글 수정 */
                    Intent i = new Intent(ActiWrite.this, acti_ActiBoard.class);
                    startActivity(i);
                }
                finish();
            }else{
                Toast.makeText(ActiWrite.this, "글쓰기가 실패되었습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
