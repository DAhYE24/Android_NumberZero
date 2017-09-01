package dh_kang.nozero.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import dh_kang.nozero.Adapter.Lv_InfoAdapter;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-10-14.
 */
public class FragInfo extends Fragment {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    ExpandableListView fi_lvInfo;

    /* 리스트뷰 선언 */
    private ArrayList<String> infoGroup = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> infoChild = new HashMap<String, ArrayList<String>>();
    ArrayList<String> infoType = new ArrayList<String>();
    ArrayList<String> infoScent = new ArrayList<String>();
    ArrayList<String> infoPrecaution = new ArrayList<String>();

    /* 네트워크 통신 */
    infoPhp task;
    String tempTitle;
    public static String myJSON;

    public static String getMyJSON() {
        return myJSON;
    } //FragInfo_Detail과 연결

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.frag_info, container, false );

        /* 초기화 */
        fi_lvInfo = (ExpandableListView)v.findViewById(R.id.fi_lvInfo);

        /* 항목텍스트를 리스트뷰에 적용 */
        setDataIntoArray();
        fi_lvInfo.setAdapter(new Lv_InfoAdapter(getContext(), infoGroup, infoChild));

        /* 리스트뷰 클릭한 경우 */
        fi_lvInfo.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                infoGroup.get(groupPosition);
                switch (groupPosition){
                    case 0:
                        tempTitle = String.valueOf(infoType.get(childPosition));
                        break;
                    case 1:
                        tempTitle = String.valueOf(infoScent.get(childPosition));
                        break;
                    case 2:
                        tempTitle = String.valueOf(infoPrecaution.get(childPosition));
                        break;
                    default:
                        break;
                }

                /* 서버와 연결 */
                task = new infoPhp();
                task.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Fi_BasicSearch.php");
                return false;
            }
        });

        return v;
    }

    /* 항목텍스트를 리스트뷰에 적용 */
    private void setDataIntoArray() {
        /* array에서 가져오기 */
        String[] infoMain = getResources().getStringArray(R.array.info_main);
        String[] infoSub1 = getResources().getStringArray(R.array.info_sub1);
        String[] infoSub2 = getResources().getStringArray(R.array.info_sub2);
        String[] infoSub3 = getResources().getStringArray(R.array.info_sub3);

        /* 각 항목에 추가하기 */
        for(int i = 0; i < infoMain.length; i++)    infoGroup.add(infoMain[i]);
        for(int i = 0; i < infoSub1.length; i++)    infoType.add(infoSub1[i]);
        for(int i = 0; i < infoSub2.length; i++)    infoScent.add(infoSub2[i]);
        for(int i = 0; i < infoSub3.length; i++)    infoPrecaution.add(infoSub3[i]);

        /* Main리스트와 Sub리스트 연결하기 */
        infoChild.put(infoGroup.get(0), infoType);
        infoChild.put(infoGroup.get(1), infoScent);
        infoChild.put(infoGroup.get(2), infoPrecaution);
    }

    /* 네트워크 연결*/
    public class infoPhp extends AsyncTask<String, Integer, String> {
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

                    String data  = URLEncoder.encode("contentTitle", "UTF-8") + "=" + URLEncoder.encode(tempTitle, "UTF-8");

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
            myJSON = str;
            Log.i(TAG, myJSON);
            goToNextFragment(); // 설명 화면으로 이동
            asyncDialog.dismiss();
        }
    }

    private void goToNextFragment() {
        Fragment fg = new FragInfoDetail(); //임시 프레그먼트
        FragmentTransaction transaction = getFragmentManager().beginTransaction(); // 전환
        transaction.add(R.id.am_lyFrag, fg); // replace 해결 안되는지 다시 체크하기
        transaction.addToBackStack(null); //이전의 프레그먼트로 백스택에 추가해서, 돌아갈 수 있는 코드
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); // 애니메이션
        transaction.commit();
    }
}