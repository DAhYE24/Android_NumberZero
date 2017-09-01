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

    private class ViewHolder{
        TextView lpr_txtName, lpr_txtNameEng, lpr_txtCnP, lpr_txtBrand;
        ImageView lpr_imgPerf;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.lv_perf_result, null); // 리스트뷰 칸 xml과 연결

            holder = new ViewHolder();
            holder.lpr_txtName = (TextView)convertView.findViewById(R.id.lpr_txtName);
            holder.lpr_txtNameEng = (TextView)convertView.findViewById(R.id.lpr_txtNameEng);
            holder.lpr_txtCnP = (TextView)convertView.findViewById(R.id.lpr_txtCnP);
            holder.lpr_txtBrand = (TextView)convertView.findViewById(R.id.lpr_txtBrand);
            holder.lpr_imgPerf = (ImageView)convertView.findViewById(R.id.lpr_imgPerf);
            convertView.setTag(holder); // 칸 클릭시 뭐 나올지 인듯
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Lv_PerfumeValues pfSet = perfumeList.get(position);
        //텍스트 적용
        holder.lpr_txtName.setText(pfSet.getPerfName());
        holder.lpr_txtName.setTag(pfSet);
        holder.lpr_txtNameEng.setText(pfSet.getPerfNameEng());
        holder.lpr_txtNameEng.setTag(pfSet);
        holder.lpr_txtBrand.setText(pfSet.getPerfBrand());
        holder.lpr_txtBrand.setTag(pfSet);
        holder.lpr_txtCnP.setText(pfSet.getPerfCnP());
        holder.lpr_txtCnP.setTag(pfSet);

        // Glide로 이미지 경로 & url 연결W
        Glide.with(getContext()).load(pfSet.getPerfImg()).override(200, 200).into(holder.lpr_imgPerf);

        return  convertView;
    }
}
