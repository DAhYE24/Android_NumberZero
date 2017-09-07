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

import dh_kang.nozero.DataSet.Lv_PerfumeValues;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-11-17.
 */
public class Lv_MyPerfumeAdapter extends ArrayAdapter<Lv_PerfumeValues> {
    public ArrayList<Lv_PerfumeValues> perfumeList;

    public Lv_MyPerfumeAdapter(Context context, int textViewResourceId, List<Lv_PerfumeValues> perfumeList) {
        super(context, textViewResourceId, perfumeList);
        this.perfumeList = new ArrayList<Lv_PerfumeValues>();
        this.perfumeList.addAll(perfumeList);
    }

    private class ViewHolder {
        ImageView img_resultPerfImg;
        TextView txt_resultPerfBrand, txt_resultPerfName, txt_resultPerfEngName, txt_resultPerfAddition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.lv_perfume_result, null); // 리스트뷰 칸 xml과 연결

            holder = new ViewHolder();
            holder.img_resultPerfImg = (ImageView) convertView.findViewById(R.id.img_resultPerfImg);
            holder.txt_resultPerfBrand = (TextView) convertView.findViewById(R.id.txt_resultPerfBrand);
            holder.txt_resultPerfName = (TextView) convertView.findViewById(R.id.txt_resultPerfName);
            holder.txt_resultPerfEngName = (TextView) convertView.findViewById(R.id.txt_resultPerfEngName);
            holder.txt_resultPerfAddition = (TextView) convertView.findViewById(R.id.txt_resultPerfAddition);
            convertView.setTag(holder); // 칸 클릭시 뭐 나올지 인듯
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Lv_PerfumeValues pfSet = perfumeList.get(position);
        //텍스트 적용
        holder.txt_resultPerfName.setText(pfSet.getPerfName());
        holder.txt_resultPerfName.setTag(pfSet);
        holder.txt_resultPerfEngName.setText(pfSet.getPerfNameEng());
        holder.txt_resultPerfEngName.setTag(pfSet);
        holder.txt_resultPerfBrand.setText(pfSet.getPerfBrand());
        holder.txt_resultPerfBrand.setTag(pfSet);
        holder.txt_resultPerfAddition.setText(pfSet.getPerfCnP());
        holder.txt_resultPerfAddition.setTag(pfSet);

        // Glide로 이미지 경로 & url 연결W
        Glide.with(getContext()).load(pfSet.getPerfImg()).override(200, 200).into(holder.img_resultPerfImg);

        return convertView;
    }
}
