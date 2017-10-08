package dh_kang.nozero.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dh_kang.nozero.Adapter.Lv_CommentAdapter;
import dh_kang.nozero.R;

public class ActiBoardContent extends AppCompatActivity {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    ListView abc_lvComment;
    TextView abc_txtTitle, abc_txtId, abc_txtCnt, abc_txtContent, abc_txtDate;
    Button abc_btnComment, abc_btnDelete, abc_btnModify;
    EditText abc_editComment;
    ImageView abc_imgIcon;

    /* 네트워크 통신 */
    connectServer task;
    String myJSON;
    JSONArray vc = null;
    int chkPhp; // 0:댓글쓰기, 1:댓글불러오기, 2:글삭제

    /* JAVA 선언 */
    String receiveNum; // 게시글 번호
    String tempCmtId, tempCmtContent, tempCmtDate, tempCmtImg; // 댓글 내용들
    Lv_CommentAdapter commentAdapter = null; // 코멘트 리스트뷰 적용
    String userWritingCmt, dateString, userIdCmt; // 유저가 쓴 댓글내용, 댓글 쓴 시간, 댓글 쓴 유저 닉네임
    SharedPreferences userInfo;
    String temp[];
    public static Context abcContext;
    public static String tempWrite; // 글 수정할 때 내용 보내기

    public static String getTempWrite() {
        return tempWrite;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_board_content);

        /* 초기화 */
        abc_lvComment = (ListView)findViewById(R.id.abc_lvComment);
        abc_txtContent = (TextView)findViewById(R.id.abc_txtContent);
        abc_txtCnt = (TextView)findViewById(R.id.abc_txtCnt);
        abc_txtId = (TextView)findViewById(R.id.abc_txtId);
        abc_txtTitle = (TextView)findViewById(R.id.abc_txtTitle);
        abc_txtDate = (TextView)findViewById(R.id.abc_txtDate);
        abc_btnComment = (Button)findViewById(R.id.abc_btnComment);
        abc_editComment = (EditText)findViewById(R.id.abc_editComment);
        abc_btnDelete = (Button)findViewById(R.id.abc_btnDelete);
        abc_btnModify = (Button)findViewById(R.id.abc_btnModify);
        abc_imgIcon = (ImageView)findViewById(R.id.abc_imgIcon);
        abcContext = this;

        /* 화면 초기화 */
        abc_btnDelete.setVisibility(View.GONE);
        abc_btnModify.setVisibility(View.GONE);

        /* 게시글 내용 받아오기 */
        acti_ActiBoard ab = null;
//        temp = ab.getSendSelectedInfo().split(",SP,");

        /* 게시글 내용 보여주기 */
        Glide.with(this).load(temp[0]).override(30, 30).into(abc_imgIcon); // Glide로 이미지 경로 & url 연결
        abc_txtId.setText(temp[1]);
        abc_txtTitle.setText("[제목] " + temp[2]);
        abc_txtContent.setText(temp[3]);
        abc_txtDate.setText(temp[4]);
        receiveNum = temp[5].toString(); // 게시글 번호

        /* 유저 정보 */
        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userIdCmt = userInfo.getString("userId", "");

        /* 게시글의 유저와 해당 유저의 아이디가 동일한 경우 수정&삭제 기능 활성화 */
        if(temp[1].equals(userInfo.getString("userId", ""))){
            abc_btnDelete.setVisibility(View.VISIBLE);
            abc_btnModify.setVisibility(View.VISIBLE);
        }

        /* 댓글 받아오기 */
        loadCommentList();

        /* 댓글 등록하기 */
        abc_btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempLength = abc_editComment.getText().toString();
                if(tempLength.length() == 0){
                    Toast.makeText(ActiBoardContent.this, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    chkPhp = 0;

                    /* 댓글로 저장할 정보 */
                    userWritingCmt = tempLength; // 댓글 받아오기
                    SimpleDateFormat forDatetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // 등록시간
                    Date getTime = new Date();
                    dateString = forDatetime.format(getTime);

                    /* 댓글 등록하기*/
                    task = new connectServer();
                    task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Abc_WriteComment.php");
                }
            }
        });

        /* 글 수정하기 */
        abc_btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 값 저장 */
                tempWrite = temp[2] + ",SP," + temp[3] + ",SP," + temp[5];
                userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();
                editor.putBoolean("writeCheck", false); // 일반 글쓰기일 때는 true
                editor.commit();

                /* 글쓰기 창으로 이동 */
                Intent i = new Intent(ActiBoardContent.this, ActiWrite.class);
                startActivity(i);
            }
        });

        /* 글 삭제하기 */
        abc_btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = deleteWriting();
                dialog.show();
            }

            private AlertDialog deleteWriting() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActiBoardContent.this);
                builder.setTitle("댓글삭제");
                builder.setMessage("해당 글을 삭제하시겠습니까?");

                builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chkPhp = 2;
                        task = new connectServer();
                        task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Abc_DeleteWriting.php");
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

    /* 리스트뷰에 맞춰서 스크롤바의 길이 조절 */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)    return;

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /* 댓글 받아오기 */
    public void loadCommentList() {
        chkPhp = 1;
        task = new connectServer();
        task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Abc_LoadComment.php");
    }

    /* 서버통신하기 */
    public class connectServer extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(ActiBoardContent.this);

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

                    /* 보낼 데이터 선정하기 */
                    String data = null;
                    if(chkPhp == 0){ // 댓글 쓰기
                        data = URLEncoder.encode("receiveNum", "UTF-8") + "=" + URLEncoder.encode(receiveNum, "UTF-8")
                                + "&" + URLEncoder.encode("cmtContent", "UTF-8") + "=" + URLEncoder.encode(userWritingCmt, "UTF-8")
                                + "&" + URLEncoder.encode("cmtDate", "UTF-8") + "=" + URLEncoder.encode(dateString, "UTF-8")
                                + "&" + URLEncoder.encode("cmtId", "UTF-8") + "=" + URLEncoder.encode(userIdCmt, "UTF-8");
                    }else if(chkPhp == 1){ // 해당 글의 댓글 불러오기
                        data = URLEncoder.encode("receiveNum", "UTF-8") + "=" + URLEncoder.encode(receiveNum, "UTF-8");
                    }else if(chkPhp == 2){ // 글 삭제
                        data = URLEncoder.encode("receiveNum", "UTF-8") + "=" + URLEncoder.encode(receiveNum, "UTF-8")
                                + "&" + URLEncoder.encode("boardTime", "UTF-8") + "=" + URLEncoder.encode(temp[4], "UTF-8")
                                + "&" + URLEncoder.encode("boardId", "UTF-8") + "=" + URLEncoder.encode(userIdCmt, "UTF-8");
                    }

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
            switch(chkPhp){
                case 0:// 댓글 쓰기
                    if(str.trim().equals("100")) {
                        Toast.makeText(ActiBoardContent.this, "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show();
                        abc_editComment.setText(null);

                        /* 댓글 단 후에 키보드 내려오기 */
                        InputMethodManager imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(abc_editComment.getWindowToken(), 0);

                        loadCommentList(); // 댓글 새로고침
                    }else{
                        Toast.makeText(ActiBoardContent.this, "댓글등록을 실패하였습니다", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1: // 댓글 불러오기
                    if (str.trim().equals("200")) {
                        abc_lvComment.setVisibility(View.GONE);
                    } else {
                        abc_lvComment.setVisibility(View.VISIBLE);
                        myJSON = str;
                        showList();
                    }
                    break;
                case 2: // 글 삭제
                    if (str.trim().equals("100")) {
                        Intent intent = new Intent(ActiBoardContent.this, acti_ActiBoard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }else{
                        Toast.makeText(ActiBoardContent.this, "글 삭제가 실패하였습니다", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            asyncDialog.dismiss();
        }
    }

    /* 댓글 내용 */
    protected void showList(){
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            vc = jsonObj.getJSONArray("results");

            ArrayList<Lv_CommentAdapter.Lv_CommentValues> commentList = new ArrayList<Lv_CommentAdapter.Lv_CommentValues>();
            Lv_CommentAdapter.Lv_CommentValues inputComment;

            for(int i=0;i<vc.length();i++) {
                JSONObject jo = vc.getJSONObject(i);
                tempCmtId = jo.getString("cmtId");
                tempCmtContent = jo.getString("cmtContent");
                tempCmtDate = jo.getString("cmtDate");
                tempCmtImg = jo.getString("userImage");

                inputComment = new Lv_CommentAdapter.Lv_CommentValues(tempCmtId, tempCmtContent, tempCmtDate, tempCmtImg);
                commentList.add(inputComment);
            }
            commentAdapter = new Lv_CommentAdapter(this, R.layout.lv_comment, commentList); // 리스트 형태 연결
            abc_lvComment.setAdapter(commentAdapter);

            /* 리스트뷰에 맞춰서 스크롤바의 길이 조절 */
            setListViewHeightBasedOnChildren(abc_lvComment);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
