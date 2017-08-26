package dh_kang.nozero;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by dh93 on 2016-10-16.
 */
public class Lv_MyFlaAdapter extends ArrayAdapter<Lv_FlaValues> {
    /* 로그 테스트 */
    private static final String TAG = "NOZERO_FINAL";

    public ArrayList<Lv_FlaValues> flavouringList; // 향료 리스트뷰
    public static StringBuffer responseText;
    public static long cnt = 0;

    /* 향료 정보 띄우기 */
    DialFlaInfo flaDialog;
    public static String flaName;

    public static String getFlaName() {
        return flaName;
    }

    // FragmentSearch에 선택된 값 보내기
    public static StringBuffer getResponseText() {
        return responseText;
    }

    public static long getCnt() {
        return cnt;
    }

    public static void setCnt(long cnt) {
        Lv_MyFlaAdapter.cnt = cnt;
    }

    // 생성자 만들기
    public Lv_MyFlaAdapter(Context context, int textViewResourceId,
                           ArrayList<Lv_FlaValues> flavouringList) {
        super(context, textViewResourceId, flavouringList);
        this.flavouringList = new ArrayList<Lv_FlaValues>();
        this.flavouringList.addAll(flavouringList);
    }

    // 체크박스
    private class ViewHolder{
        CheckBox chBoxOne, chBoxTwo, chBoxThree;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        responseText = new StringBuffer();

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.lv_search_fla, null); // 리스트뷰 칸 xml과 연결

            holder = new ViewHolder();
            holder.chBoxOne = (CheckBox) convertView.findViewById(R.id.lsf_chkOne); // 리스트뷰의 체크박스 연결
            convertView.setTag(holder); // 칸 클릭시 뭐 나올지 인듯

            holder.chBoxOne.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    chkBoxWork(v);
                }
            });
            holder.chBoxOne.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longFlaInfo(v);
                    return false;
                }
            });

            // chkbox.isChecked() => 선택되면 true, 아니면 false
            // chkbox.getText() -> 말그대로 선택된거 text가 뜨는

            holder.chBoxTwo = (CheckBox) convertView.findViewById(R.id.lsf_chkTwo); // 리스트뷰의 체크박스 연결
            convertView.setTag(holder); // 칸 클릭시 뭐 나올지 인듯

            holder.chBoxTwo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    chkBoxWork(v);
                }
            });

            holder.chBoxTwo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longFlaInfo(v);
                    return false;
                }
            });

            holder.chBoxThree = (CheckBox) convertView.findViewById(R.id.lsf_chkThree); // 리스트뷰의 체크박스 연결
            convertView.setTag(holder); // 칸 클릭시 뭐 나올지 인듯

            holder.chBoxThree.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    chkBoxWork(v);
                }
            });
            holder.chBoxThree.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longFlaInfo(v);
                    return false;
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Lv_FlaValues sfSetPart = flavouringList.get(position);
        //체크박스1
        holder.chBoxOne.setText(sfSetPart.getFlavOne());
        holder.chBoxOne.setChecked(sfSetPart.isSelOne());
        holder.chBoxOne.setTag(sfSetPart);
        //체크박스2
        holder.chBoxTwo.setText(sfSetPart.getFlavTwo());
        holder.chBoxTwo.setChecked(sfSetPart.isSelTwo());
        holder.chBoxTwo.setTag(sfSetPart);
        //체크박스3
        holder.chBoxThree.setText(sfSetPart.getFlavThree());
        holder.chBoxThree.setChecked(sfSetPart.isSelThree());
        holder.chBoxThree.setTag(sfSetPart);

//        holder.chBoxOne.setChecked(false);
//        holder.chBoxTwo.setChecked(false);
//        holder.chBoxThree.setChecked(false);

        return convertView;
    }

    private void longFlaInfo(View v) {
        CheckBox ckbox = (CheckBox) v;
        flaName = String.valueOf(ckbox.getText()); // 해당 체크박스의 값 저장하기
        flaDialog = new DialFlaInfo(getContext());
        flaDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        flaDialog.show();
    }

    private void chkBoxWork(View v) {
        CheckBox ckbox = (CheckBox) v;
        Lv_FlaValues sf = (Lv_FlaValues) ckbox.getTag();
        Log.i("NOERROR", String.valueOf(sf));
        if(ckbox.isChecked() == true){ // 체크하는 경우
            sf.setSelTwo(ckbox.isChecked() == false);
            responseText.append(ckbox.getText() + " ");
            cnt++;
        }else{ // 체크 해제하는 경우
            sf.setSelTwo(ckbox.isChecked() == true);
            String temp = String.valueOf(ckbox.getText());
            int pos = responseText.indexOf(temp);
            if(responseText.length() > 0){
                responseText.delete(pos, pos + temp.length() + 1);
            }
            cnt--;
        }

        if(responseText.length() > 0) {
            Toast.makeText(getContext(), "선택한 향료 : " + responseText, Toast.LENGTH_SHORT).show();
       }
    }

}
