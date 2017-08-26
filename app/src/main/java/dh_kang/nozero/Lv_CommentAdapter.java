package dh_kang.nozero;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by dh93 on 2016-11-22.
 */
public class Lv_CommentAdapter extends BaseAdapter{
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* JAVA 선언 */
    SharedPreferences userInfo;
    String userId, cmtUserDate;
    public Context lcContext;
    ViewHolder holder = null;

    /* 리스트뷰 */
    public ArrayList<Lv_CommentValues> commentList;

    /* 댓글 삭제 네트워크 통신 */
    commentChange task;

    /* 리스트뷰 Adapter와 Value 연결 */
    public Lv_CommentAdapter(ActiBoardContent actiBoardContent, int textViewResourceId, ArrayList<Lv_CommentValues> commentList) {
        this.commentList = new ArrayList<Lv_CommentValues>();
        this.commentList.addAll(commentList);
    }

    /* 리스트뷰 Value 선언 */
    public static class Lv_CommentValues{
        String commentId = null;
        String commentContent = null;
        String commentDate = null;
        String commentImg = null;

        public Lv_CommentValues(String commentId, String commentContent, String commentDate, String commentImg) {
            this.commentId = commentId;
            this.commentContent = commentContent;
            this.commentDate = commentDate;
            this.commentImg = commentImg;
        }

        public String getCommentImg() {
            return commentImg;
        }

        public String getCommentId() {
            return commentId;
        }

        public String getCommentContent() {
            return commentContent;
        }

        public String getCommentDate() {
            return commentDate;
        }
    }

    /* 필수 선언 */
    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /* 어댑터 */
    public class ViewHolder{
        TextView lc_txtId, lc_txtComment, lc_txtDate; // 코멘트 이름, 내용, 시간
        Button lc_btnDelete; // 댓글 삭제 버튼
        ImageView lc_imgIcon;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        lcContext = parent.getContext();

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) lcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.lv_comment, null); // 리스트뷰 칸 xml과 연결

            /* 레이아웃과 연결 */
            holder = new ViewHolder();
            holder.lc_txtId = (TextView)convertView.findViewById(R.id.lc_txtId);
            holder.lc_txtComment = (TextView)convertView.findViewById(R.id.lc_txtComment);
            holder.lc_txtDate = (TextView)convertView.findViewById(R.id.lc_txtDate);
            holder.lc_btnDelete = (Button)convertView.findViewById(R.id.lc_btnDelete);
            holder.lc_imgIcon = (ImageView)convertView.findViewById(R.id.lc_imgIcon);
            //holder.btnCmtModify = (Button)convertView.findViewById(R.id.lc_btnModify);

            /* 댓글 속 버튼 숨기기 */
            holder.lc_btnDelete.setVisibility(View.GONE);
//            holder.btnCmtModify.setVisibility(View.GONE);

            /* 댓글 삭제 버튼 */
            holder.lc_btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Lv_CommentValues toGetDate = commentList.get(position);
                    cmtUserDate = toGetDate.getCommentDate();
                    AlertDialog dialog = removeComment();
                    dialog.show();
                }

                private AlertDialog removeComment() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(lcContext);
                    builder.setTitle("댓글삭제");
                    builder.setMessage("해당 댓글을 삭제하시겠습니까?");

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            task = new commentChange();
                            task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Lc_DeleteComment.php");
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
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        /* 실제 받아온 텍스트 적용하는 부분 */
        Lv_CommentValues cmtSet = commentList.get(position);
        holder.lc_txtId.setText(cmtSet.getCommentId());
        holder.lc_txtId.setTag(cmtSet);
        holder.lc_txtComment.setText(cmtSet.getCommentContent());
        holder.lc_txtComment.setTag(cmtSet);
        holder.lc_txtDate.setText(cmtSet.getCommentDate());
        holder.lc_txtDate.setTag(cmtSet);
        Glide.with(lcContext).load(cmtSet.getCommentImg()).override(200, 200).into(holder.lc_imgIcon); // 이미지 설정

        /* 현재 유저 아이디 받아와서 같으면 버튼 출력 */
        userInfo = lcContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = userInfo.getString("userId", "");
        if(cmtSet.getCommentId().equals(userId)){
            holder.lc_btnDelete.setVisibility(View.VISIBLE);
//            holder.btnCmtModify.setVisibility(View.VISIBLE);
        }

        return  convertView;
    }

    public class commentChange extends AsyncTask<String, Integer, String> {
        ProgressDialog asyncDialog = new ProgressDialog(lcContext);

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

                    /* 댓글 쓴 아이디와 해당 시간을 바탕으로 댓글 삭제 */
                    String data = URLEncoder.encode("cmtId", "UTF-8") + "=" + URLEncoder.encode(userId, "UTF-8")
                                + "&" + URLEncoder.encode("cmtDate", "UTF-8") + "=" + URLEncoder.encode(cmtUserDate, "UTF-8");

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
            if(str.trim().equals("100")){
                Toast.makeText(lcContext, "댓글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                /* 댓글 새로고침 */
                ((ActiBoardContent) ActiBoardContent.abcContext).loadCommentList();
                asyncDialog.dismiss();
            }
        }
    }
}
