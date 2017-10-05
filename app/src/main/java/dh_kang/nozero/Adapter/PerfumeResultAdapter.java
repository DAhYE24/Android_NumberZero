package dh_kang.nozero.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import dh_kang.nozero.DataSet.PerfumeValues;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-11-17.
 */
public class PerfumeResultAdapter extends RecyclerView.Adapter<PerfumeResultAdapter.ViewHolder> {
    private PerfumeValues[] perfumeValues;
    private Context context;

    public PerfumeResultAdapter(Context context, PerfumeValues[] perfumeValues) {
        this.context = context;
        this.perfumeValues = perfumeValues;
    }

// public class PerfumeResultAdapter extends ArrayAdapter<PerfumeValues> {
//    public ArrayList<PerfumeValues> perfumeList;
//
//    public PerfumeResultAdapter(Context context, int textViewResourceId, List<PerfumeValues> perfumeList) {
//        super(context, textViewResourceId, perfumeList);
//        this.perfumeList = new ArrayList<PerfumeValues>();
//        this.perfumeList.addAll(perfumeList);
//    }

    /* Create View */
    @Override
    public PerfumeResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_perfume_result, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    /* Get Data from dataset and replace it*/
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.img_resultPerfImg.setImageResource(perfumeValues[position].getImageUrl());
        Glide.with(context).load(perfumeValues[position].getPerfumeImage()).override(200, 200).into(holder.img_resultPerfImg);
        holder.txt_resultPerfBrand.setText(perfumeValues[position].getPerfumeBrand());
        holder.txt_resultPerfName.setText(perfumeValues[position].getPerfumeName());
        holder.txt_resultPerfEngName.setText(perfumeValues[position].getPerfumeEngName());
        holder.txt_resultPerfAddition.setText(perfumeValues[position].getPerfumeCapacity() + " / " + perfumeValues[position].getPerfumePrice());
    }

//        PerfumeValues pfSet = perfumeList.get(position);
//        //텍스트 적용
//        holder.txt_resultPerfName.setText(pfSet.getPerfName());
//        holder.txt_resultPerfName.setTag(pfSet);
//        holder.txt_resultPerfEngName.setText(pfSet.getPerfNameEng());
//        holder.txt_resultPerfEngName.setTag(pfSet);
//        holder.txt_resultPerfBrand.setText(pfSet.getPerfBrand());
//        holder.txt_resultPerfBrand.setTag(pfSet);
//        holder.txt_resultPerfAddition.setText(pfSet.getPerfCnP());
//        holder.txt_resultPerfAddition.setTag(pfSet);
//
//        // Glide로 이미지 경로 & url 연결
//        Glide.with(getContext()).load(pfSet.getPerfImg()).override(200, 200).into(holder.img_resultPerfImg);
//        return convertView;
//    }

    /* Return the size of item data*/
    @Override
    public int getItemCount() {
        return perfumeValues.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_resultPerfImg;
        public TextView txt_resultPerfBrand, txt_resultPerfName, txt_resultPerfEngName, txt_resultPerfAddition;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            img_resultPerfImg = (ImageView) itemLayoutView.findViewById(R.id.img_resultPerfImg);
            txt_resultPerfBrand = (TextView) itemLayoutView.findViewById(R.id.txt_resultPerfBrand);
            txt_resultPerfName = (TextView) itemLayoutView.findViewById(R.id.txt_resultPerfName);
            txt_resultPerfEngName = (TextView) itemLayoutView.findViewById(R.id.txt_resultPerfEngName);
            txt_resultPerfAddition = (TextView) itemLayoutView.findViewById(R.id.txt_resultPerfAddition);
        }
    }

//    private class ViewHolder {
//        ImageView img_resultPerfImg;
//        TextView txt_resultPerfBrand, txt_resultPerfName, txt_resultPerfEngName, txt_resultPerfAddition;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if (convertView == null) {
//            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = vi.inflate(R.layout.lv_perfume_result, null); // 리스트뷰 칸 xml과 연결
//
//            holder = new ViewHolder();
//            holder.img_resultPerfImg = (ImageView) convertView.findViewById(R.id.img_resultPerfImg);
//            holder.txt_resultPerfBrand = (TextView) convertView.findViewById(R.id.txt_resultPerfBrand);
//            holder.txt_resultPerfName = (TextView) convertView.findViewById(R.id.txt_resultPerfName);
//            holder.txt_resultPerfEngName = (TextView) convertView.findViewById(R.id.txt_resultPerfEngName);
//            holder.txt_resultPerfAddition = (TextView) convertView.findViewById(R.id.txt_resultPerfAddition);
//            convertView.setTag(holder); // 칸 클릭시 뭐 나올지 인듯
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
}
