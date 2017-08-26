package dh_kang.nozero;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dh93 on 2016-10-16.
 */
public class FragInfoDetail extends Fragment {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* XML 선언 */
    TextView fid_txtDetail, fid_txtTitle; // 내용, 제목
    ImageView fid_imgInfo; // 상세 이미지

    /* JAVA 선언 */
    String myJSON;
    JSONArray vc = null;
    String tempTitle, tempNumber, tempImg, tempDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.frag_info_detail, container, false );

        /* 초기화 */
        fid_txtDetail = (TextView)v.findViewById(R.id.fid_txtDetail);
        fid_txtTitle = (TextView)v.findViewById(R.id.fid_txtTitle);
        fid_imgInfo = (ImageView)v.findViewById(R.id.fid_imgInfo);

        /* 서버에서 받아온 내용 적용시키기 */
        FragInfo info = new FragInfo();
        myJSON = info.getMyJSON();

        showList();

        return v;
    }

    private void showList() {
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            vc = jsonObj.getJSONArray("results");

            for(int i=0;i<vc.length();i++) {
                JSONObject jo = vc.getJSONObject(i);
                tempTitle = jo.getString("contentTitle");
                tempNumber = jo.getString("listNumber");
                tempImg = jo.getString("contentImg");
                tempDetail = jo.getString("contentDetail");
            }

            /* 디바이스의 가로 크기 불러오기 */
            DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
            int width = dm.widthPixels;

            /* 레이아웃에 불러온 정보 적용하기 */
            //fid_txtDetail.setText(tempDetail);
            fid_txtDetail.setText(TextViewHelper.shrinkWithWordUnit(fid_txtDetail, tempDetail, width - 350)); // 양쪽 20dp씩해서 40dp한 경우
            tempTitle = tempTitle.replace("◆", " ");
            fid_txtTitle.setText(tempNumber + tempTitle);
            Glide.with(getContext()).load(tempImg).into(fid_imgInfo); //그림불러오기

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}