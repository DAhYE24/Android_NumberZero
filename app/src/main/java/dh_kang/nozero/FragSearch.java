package dh_kang.nozero;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by dh93 on 2016-10-14.
 */
public class FragSearch extends Fragment {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    Spinner fs_spinType;
    ListView fs_lvSearch;
    ImageButton fs_btnSearch; // 검색기능
    Button fid_btnCheck;

    /* JAVA 선언 */
    SharedPreferences userInfo;
    String[] selectedFlaArr;
    String selectedArr;

    /* 리스트뷰 */
    Lv_MyFlaAdapter flaAdapter = null;
    Lv_MyBPAdapter bpAdapter = null;
    Lv_FlaValues inputFla;
    ArrayList<Lv_FlaValues> scentList;

    /* 네트워크 통신 */
    sendSelected taskFla; //향료 찾는
    long type; // 1: 향료 2: 브랜드 및 가격
    public static String resultList; // 나온 결과 다음 액티비티에 보내주기

    public static String getResultList() {
        return resultList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.frag_search, container, false);

        /* XML 초기화 */
        fs_lvSearch = (ListView)v.findViewById(R.id.fs_lvSearch);
        fs_spinType = (Spinner)v.findViewById(R.id.fs_spinType);
        fs_btnSearch = (ImageButton)v.findViewById(R.id.fs_btnSearch);
        fid_btnCheck = (Button)v.findViewById(R.id.fid_btnCheck);

        /* 스피너 선택시 값에 맞춰서 리스트뷰 불러오기 */
        fs_spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);

                if(position == 0)   searchByScent(); // 향료 검색
                else if(position == 1)  searchByBrand(); // 브랜드 검색
                else    searchByPrice(); // 가격 검색
            }

            /* 스피너 : 향료조합 선택 */
            private void searchByScent() {
                fs_btnSearch.setVisibility(View.VISIBLE); // 검색버튼 활성화
                fid_btnCheck.setVisibility(View.VISIBLE);

                /* 리스트뷰 요소 선언 및 초기화 */
                scentList = new ArrayList<Lv_FlaValues>();
                String[] flaListO = getResources().getStringArray(R.array.flaListOne);
                String[] flaListT = getResources().getStringArray(R.array.flaListTwo);
                String[] flaListE = getResources().getStringArray(R.array.flaListThree);

                /* 리스트뷰에 목록 적용 */
                for(int i = 0; i < flaListO.length; i++){
                    inputFla = new Lv_FlaValues(flaListO[i], false, flaListT[i], false, flaListE[i], false); // 각 세 줄의 리스트에 향료 입력
                    scentList.add(inputFla); // 실제 리스트뷰에 값 추가
                }

                fid_btnCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /* 해당 화면 새로고침으로 우선 해두기 */
                        /* Toast로 띄우는 StringBuffer 때문에 우선은 이렇게 하는게 좋을 거 같음 */
                        /* 나중에 수정할 때는 해당 StringBuffer의 생성 방식부터 수정을 하는게 좋을 거 같음 */
                        ((ActiMain)ActiMain.mContext).ChangeFragment(((ActiMain)ActiMain.mContext).findViewById(R.id.am_btnSearch));
                    }
                });

                /* 실제 리스트뷰에 값 적용 */
                flaAdapter = new Lv_MyFlaAdapter(getContext(), R.layout.lv_search_fla, scentList);
                fs_lvSearch.setAdapter(flaAdapter);

                fs_btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /* 리스트어댑터에서 선택된 값들이 저장된 버퍼스트링 값과 선택 수 받아오기 */
                        StringBuffer selectedFlas = new StringBuffer();
                        selectedFlas = flaAdapter.getResponseText();
                        long selectCnt  = flaAdapter.getCnt();

                        /* 향료 선택에 따른 출력창 */
                        if(selectCnt > 5)   Toast.makeText(getContext(), "향료는 최대 5개까지 선택할 수 있습니다", Toast.LENGTH_LONG).show();
                        else if(selectCnt == 0) Toast.makeText(getContext(), "향료를 선택하세요", Toast.LENGTH_LONG).show();
                        else{
                            /* 선택한 향료 배열 String에 넣어두기 */
                            selectedFlaArr = String.valueOf(selectedFlas).split(" ");
                            type = 1; // 향료 선택
                            taskFla = new sendSelected();
                            taskFla.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Fs_FlaSearch.php");
                        }
                    }
                });
            }

            /* 스피너 : 브랜드별 선택 */
            private void searchByBrand() {
                fs_btnSearch.setVisibility(View.GONE); // 검색버튼 비활성화
                fid_btnCheck.setVisibility(View.GONE);

                /* 리스트뷰 요소 선언 및 초기화 */
                ArrayList<Lv_BPValues> brandList = new ArrayList<Lv_BPValues>();
                String[] brand = getResources().getStringArray(R.array.brandList);
                Lv_BPValues inputBrand;

                /* 리스트뷰에 목록 적용 */
                for(int i = 0; i < brand.length; i++){
                    inputBrand = new Lv_BPValues(brand[i]);
                    brandList.add(inputBrand);
                }

                /* 실제 리스트뷰에 값 적용 */
                bpAdapter = new Lv_MyBPAdapter(getContext(), R.layout.lv_search_value, brandList);
                fs_lvSearch.setAdapter(bpAdapter);

                /* 브랜드 선택에 따른 출력창 */
                fs_lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Lv_BPValues lvBrand = (Lv_BPValues) parent.getItemAtPosition(position);
                        selectedArr = lvBrand.getBpTitle();
                        type = 2; // 브랜드 선택
                        taskFla = new sendSelected();
                        taskFla.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Fs_BrandSearch.php");
                    }
                });
            }

            /* 스피너 : 가격대별 선택 */
            private void searchByPrice(){
                fs_btnSearch.setVisibility(View.GONE); // 검색버튼 비활성화
                fid_btnCheck.setVisibility(View.GONE);

                /* 리스트뷰 요소 선언 및 초기화 */
                ArrayList<Lv_BPValues> priceList = new ArrayList<Lv_BPValues>();
                String[] price = getResources().getStringArray(R.array.priceList);
                Lv_BPValues inputPrice;

                /* 리스트뷰에 목록 적용 */
                for(int i = 0; i < price.length; i++){
                    inputPrice = new Lv_BPValues(price[i]);
                    priceList.add(inputPrice);
                }

                /* 실제 리스트뷰에 값 적용 */
                bpAdapter = new Lv_MyBPAdapter(getContext(), R.layout.lv_search_value, priceList);
                fs_lvSearch.setAdapter(bpAdapter);

                /* 가격 선택에 따른 출력창 */
                fs_lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Lv_BPValues lvBrand = (Lv_BPValues) parent.getItemAtPosition(position);
                        selectedArr = lvBrand.getBpTitle();
                        selectedArr = selectedArr.substring(0, 5);
                        type = 2; // 가격대 선택
                        taskFla = new sendSelected();
                        taskFla.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Fs_PriceSearch.php");
                    }
                });
            }

            /* 필수선언 : 아무 선택을 하지 않았을 때 */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return v;
    }

    @Override
    public void onPause() {
        flaAdapter.setCnt(0); // 검색 기능 벗어나면 어댑터에서 향료 선택한 갯수 초기화
        super.onPause();
    }

    public class sendSelected extends AsyncTask<String, Integer, String> {
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

                    StringBuffer dataStr = new StringBuffer();
                    String data;
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    /* 선택한 타입에 맞춰서 데이터 전송하기 */
                    if(type == 1){ // 향료조합
                        /* 배열에 저장된 값 가져오기 */
                        for(int i = 0; i < selectedFlaArr.length; i++)  selectedFlaArr[i] = URLEncoder.encode("fla" + i, "UTF-8") + "=" + URLEncoder.encode(selectedFlaArr[i], "UTF-8");
                        for(int j = 0; j < selectedFlaArr.length; j++){
                            if(j != 0){
                                dataStr.append("&");
                                dataStr.append(selectedFlaArr[j]);
                            }else{
                                dataStr.append(selectedFlaArr[j]);
                            }
                        }
                        Log.i(TAG, String.valueOf(dataStr));
                        wr.write(String.valueOf(dataStr));//onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                    }else if(type == 2){ // 브랜드별, 가격대별
                        data = URLEncoder.encode("selectedArr", "UTF-8") + "=" + URLEncoder.encode(selectedArr, "UTF-8");
                        wr.write(data);//onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                    }

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

        @Override
        protected void onPostExecute(String str) {
            asyncDialog.dismiss();
            userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = userInfo.edit();
            editor.putString("searchPerf", "search"); // 닉네임 저장
            editor.commit();

            /* 조합별 저장하는 값 */
            if(type == 1){ // 향료조합
                if(str.trim().equals("200")){
                    Toast.makeText(getContext(), "해당하는 향수가 없습니다", Toast.LENGTH_SHORT).show();
                }else {
                    resultList = str;
                    Intent i = new Intent(getContext(), ActiPerfResult.class);
                    startActivity(i);
                }
            }else if(type == 2){ //브랜드별, 가격대별
                resultList = str;
                Intent i = new Intent(getContext(), ActiPerfResult.class);
                startActivity(i);
            }
            super.onPostExecute(str);
        }
    }
}