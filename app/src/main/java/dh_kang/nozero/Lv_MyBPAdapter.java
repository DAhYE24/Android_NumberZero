package dh_kang.nozero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dh93 on 2016-11-16.
 */
public class Lv_MyBPAdapter extends ArrayAdapter<Lv_BPValues> {
    public ArrayList<Lv_BPValues> bpList; // 향료 리스트뷰

    public Lv_MyBPAdapter(Context context, int textViewResourceId, List<Lv_BPValues> bpList) {
        super(context, textViewResourceId, bpList);
        this.bpList = new ArrayList<Lv_BPValues>();
        this.bpList.addAll(bpList);
    }

    private class ViewHolder{
        TextView txtBP;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.lv_search_value, null); // 리스트뷰 칸 xml과 연결

            holder = new ViewHolder();
            holder.txtBP = (TextView)convertView.findViewById(R.id.lsv_txtValue);
            convertView.setTag(holder); // 칸 클릭시 뭐 나올지 인듯

//            holder.txtBP.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TextView txt = (TextView) v;
//                    Lv_BPValues vp = (Lv_BPValues) txt.getTag();
//                    Toast.makeText(getContext(), txt.getText() + "를 선택했습니다", Toast.LENGTH_LONG).show();
//                }
//            });

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Lv_BPValues sfSetPart = bpList.get(position);
        //텍스트
        holder.txtBP.setText(sfSetPart.getBpTitle());
        holder.txtBP.setTag(sfSetPart);

        return convertView;
    }
}
