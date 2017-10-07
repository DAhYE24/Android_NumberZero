package dh_kang.nozero.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import dh_kang.nozero.DataSet.BoardValues;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-11-21.
 */
public class Lv_BoardBasicAdapter extends ArrayAdapter<BoardValues> {
    public ArrayList<BoardValues> basicList;

    public Lv_BoardBasicAdapter(Context context, int textViewResourceId, List<BoardValues> basicList) {
        super(context, textViewResourceId, basicList);
        this.basicList = new ArrayList<BoardValues>();
        this.basicList.addAll(basicList);
    }

    private class ViewHolder{
        TextView lb_txtId, lb_txtTitle, lb_txtDate;
        ImageView lb_imgIcon;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.lv_board, null); // 리스트뷰 칸 xml과 연결

            holder = new ViewHolder();
            holder.lb_txtId = (TextView)convertView.findViewById(R.id.txt_writerName);
            holder.lb_txtTitle = (TextView)convertView.findViewById(R.id.txt_writingTitle);
            holder.lb_txtDate = (TextView)convertView.findViewById(R.id.txt_writingTime);
            holder.lb_imgIcon = (ImageView)convertView.findViewById(R.id.img_writerImg);
            convertView.setTag(holder); // 칸 클릭시 뭐 나올지 인듯?

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        BoardValues basicSet = basicList.get(position);
        //텍스트 적용
        holder.lb_txtId.setText(basicSet.getBasicId());
        holder.lb_txtId.setTag(basicSet);
        holder.lb_txtTitle.setText(basicSet.getBasicTitle());
        holder.lb_txtTitle.setTag(basicSet);
        holder.lb_txtDate.setText(basicSet.getBasicDate());
        holder.lb_txtDate.setTag(basicSet);

        // Glide로 이미지 경로 & url 연결
        Glide.with(getContext()).load(basicSet.getBasicIcon()).override(40, 40).into(holder.lb_imgIcon);

        return  convertView;
    }
}
