package dh_kang.nozero.Fragment;

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

import dh_kang.nozero.Activity.ActiPerfResult;
import dh_kang.nozero.DataSet.FlavorValues;
import dh_kang.nozero.Adapter.Lv_MyBPAdapter;
import dh_kang.nozero.Adapter.FlavorListAdapter;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-10-14.
 */
public class SearchFragment extends Fragment {
    private static final String TAG = "NOZERO_FINAL";      /* LOG TEST */
    private Spinner spin_searchType;     /* Declare xml components */
    private ListView list_searchView;
    private Button btn_searchByList;

    /* Declare java parameters */
    FlavorListAdapter flavorAdapter = null;    // CustomListView components
    FlavorValues flavorValues;
    ArrayList<FlavorValues> flavorList = new ArrayList<FlavorValues>();;
    Lv_MyBPAdapter bpAdapter = null;


    /* JAVA 선언 */
    SharedPreferences userInfo;
    String[] selectedFlaArr;
    String selectedArr;

    /* 네트워크 통신 */
    sendSelected taskFla; //향료 찾는
    long type; // 1: 향료 2: 브랜드 및 가격
    public static String resultList; // 나온 결과 다음 액티비티에 보내주기

    public static String getResultList() {
        return resultList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_search, container, false);

        /* Init xml widgets */
        list_searchView = (ListView) view.findViewById(R.id.list_searchView);
        spin_searchType = (Spinner) view.findViewById(R.id.spin_searchType);
        btn_searchByList = (Button) view.findViewById(R.id.btn_searchByList);

        /* Set spinner to load each lists */
        spin_searchType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (position == 0) loadFlavorList();
                else if (position == 1) searchByBrand();
            }

            /* Load flavors list */
            private void loadFlavorList() {
//                btn_searchByList.setVisibility(View.VISIBLE); // 검색버튼 활성화
//                fid_btnCheck.setVisibility(View.VISIBLE);
                // TODO : 어댑터 flavorAdapter / 값 flavorValues / 어레이리스트 flavorList
                /* Connect checkbox list to StringArray */
                String[] flavorFirstLine = getResources().getStringArray(R.array.flavorFirstLine);
                String[] flavorSecondLine = getResources().getStringArray(R.array.flavorSecondLine);
                String[] flavorThirdLine = getResources().getStringArray(R.array.flavorThirdLine);
                for (int i = 0; i < flavorFirstLine.length; i++) {  // Input data-set into ArrayList
                    flavorValues = new FlavorValues(flavorFirstLine[i], false, flavorSecondLine[i], false, flavorThirdLine[i], false);
                    flavorList.add(flavorValues);
                }
                flavorAdapter = new FlavorListAdapter(getContext(), R.layout.lv_search_flavor, flavorList);
                list_searchView.setAdapter(flavorAdapter); // Connect ListView widget with Adapter

//                fid_btnCheck.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        /* 해당 화면 새로고침으로 우선 해두기 */
//                        /* Toast로 띄우는 StringBuffer 때문에 우선은 이렇게 하는게 좋을 거 같음 */
//                        /* 나중에 수정할 때는 해당 StringBuffer의 생성 방식부터 수정을 하는게 좋을 거 같음 */
//                        ((MainActivity) MainActivity.mContext).ChangeFragment(((MainActivity) MainActivity.mContext).findViewById(R.id.am_btnSearch));
//                    }
//                });


//                btn_searchByList.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        /* 리스트어댑터에서 선택된 값들이 저장된 버퍼스트링 값과 선택 수 받아오기 */
//                        StringBuffer selectedFlas = new StringBuffer();
//                        selectedFlas = flavorAdapter.getSelectedFlavors();
//                        long selectCnt = flavorAdapter.getSelectedCount();
//
//                        /* 향료 선택에 따른 출력창 */
//                        if (selectCnt > 5)
//                            Toast.makeText(getContext(), "향료는 최대 5개까지 선택할 수 있습니다", Toast.LENGTH_LONG).show();
//                        else if (selectCnt == 0)
//                            Toast.makeText(getContext(), "향료를 선택하세요", Toast.LENGTH_LONG).show();
//                        else {
//                            /* 선택한 향료 배열 String에 넣어두기 */
//                            selectedFlaArr = String.valueOf(selectedFlas).split(" ");
//                            type = 1; // 향료 선택
//                            taskFla = new sendSelected();
//                            taskFla.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Fs_FlaSearch.php");
//                        }
//                    }
//                });
            }

            /* 스피너 : 브랜드별 선택 */
            private void searchByBrand() {
//                btn_searchByList.setVisibility(View.GONE); // 검색버튼 비활성화
//                fid_btnCheck.setVisibility(View.GONE);
//
//                /* 리스트뷰 요소 선언 및 초기화 */
//                ArrayList<Lv_BPValues> brandList = new ArrayList<Lv_BPValues>();
//                String[] brand = getResources().getStringArray(R.array.brandList);
//                Lv_BPValues inputBrand;
//
//                /* 리스트뷰에 목록 적용 */
//                for (int i = 0; i < brand.length; i++) {
//                    inputBrand = new Lv_BPValues(brand[i]);
//                    brandList.add(inputBrand);
//                }
//
//                /* 실제 리스트뷰에 값 적용 */
//                bpAdapter = new Lv_MyBPAdapter(getContext(), R.layout.lv_search_value, brandList);
//                list_searchView.setAdapter(bpAdapter);
//
//                /* 브랜드 선택에 따른 출력창 */
//                list_searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//                        Lv_BPValues lvBrand = (Lv_BPValues) parent.getItemAtPosition(position);
//                        selectedArr = lvBrand.getBpTitle();
//                        type = 2; // 브랜드 선택
//                        taskFla = new sendSelected();
//                        taskFla.execute("http://pridena1030.cafe24.com/NumberZero/NoZ_Fs_BrandSearch.php");
//                    }
//                });
            }

            /* 필수선언 : 아무 선택을 하지 않았을 때 */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        flavorAdapter.setSelectedCount(0); // 검색 기능 벗어나면 어댑터에서 향료 선택한 갯수 초기화
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
                    if (type == 1) { // 향료조합
                        /* 배열에 저장된 값 가져오기 */
                        for (int i = 0; i < selectedFlaArr.length; i++)
                            selectedFlaArr[i] = URLEncoder.encode("fla" + i, "UTF-8") + "=" + URLEncoder.encode(selectedFlaArr[i], "UTF-8");
                        for (int j = 0; j < selectedFlaArr.length; j++) {
                            if (j != 0) {
                                dataStr.append("&");
                                dataStr.append(selectedFlaArr[j]);
                            } else {
                                dataStr.append(selectedFlaArr[j]);
                            }
                        }
                        Log.i(TAG, String.valueOf(dataStr));
                        wr.write(String.valueOf(dataStr));//onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                    } else if (type == 2) { // 브랜드별, 가격대별
                        data = URLEncoder.encode("selectedArr", "UTF-8") + "=" + URLEncoder.encode(selectedArr, "UTF-8");
                        wr.write(data);//onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                    }

                    wr.flush(); //OutputStreamWriter 버퍼 메모리 비우기

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

        @Override
        protected void onPostExecute(String str) {
            asyncDialog.dismiss();
            userInfo = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = userInfo.edit();
            editor.putString("searchPerf", "search"); // 닉네임 저장
            editor.commit();

            /* 조합별 저장하는 값 */
            if (type == 1) { // 향료조합
                if (str.trim().equals("200")) {
                    Toast.makeText(getContext(), "해당하는 향수가 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    resultList = str;
                    Intent i = new Intent(getContext(), ActiPerfResult.class);
                    startActivity(i);
                }
            } else if (type == 2) { //브랜드별, 가격대별
                resultList = str;
                Intent i = new Intent(getContext(), ActiPerfResult.class);
                startActivity(i);
            }
            super.onPostExecute(str);
        }
    }
}