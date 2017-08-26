package dh_kang.nozero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by dh93 on 2016-11-27.
 */
public class Lv_BoxAdapter extends BaseAdapter {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    /* JAVA 선언 */
    public Context lvContext;
    ViewHolder holder = null;

    /* 리스트뷰 */
    public ArrayList<Lv_BoxValues> boxList;

    /* 리스트뷰 Adapter와 Value 연결 */
    public Lv_BoxAdapter(Context context, int textViewResourceId, ArrayList<Lv_BoxValues> boxList) {
        this.boxList = new ArrayList<Lv_BoxValues>();
        this.boxList.addAll(boxList);
    }

    /* 리스트뷰 Value 선언 */
    public static class Lv_BoxValues{
        String boxImg = null;
        String boxTitle = null;
        String boxCapacity = null;

        public Lv_BoxValues(String boxImg, String boxTitle, String boxCapacity) {
            this.boxImg = boxImg;
            this.boxTitle = boxTitle;
            this.boxCapacity = boxCapacity;
        }

        public String getBoxImg() {
            return boxImg;
        }

        public String getBoxTitle() {
            return boxTitle;
        }

        public String getBoxCapacity() {
            return boxCapacity;
        }
    }

    /* 필수 선언 */
    @Override
    public int getCount() {
        return boxList.size();
    }

    @Override
    public Object getItem(int position) {
        return boxList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /* 어댑터 */
    public class ViewHolder{
        TextView lv_txtInfo;
        ImageView lv_imgPerf;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        lvContext = parent.getContext();

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) lvContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.lv_box, null); // 리스트뷰 칸 xml과 연결

             /* 레이아웃과 연결 */
            holder = new ViewHolder();
            holder.lv_txtInfo = (TextView)convertView.findViewById(R.id.lv_txtInfo);
            holder.lv_imgPerf = (ImageView)convertView.findViewById(R.id.lv_imgPerf);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        /* 실제 받아온 텍스트 적용하는 부분 */
        Lv_BoxValues lbSet = boxList.get(position);
        holder.lv_txtInfo.setText(lbSet.getBoxTitle() + " / " + lbSet.getBoxCapacity());
        holder.lv_txtInfo.setTag(lbSet);
        Glide.with(lvContext).load(lbSet.getBoxImg()).override(30, 30).into(holder.lv_imgPerf); // 이미지 설정

        return  convertView;
    }
}
