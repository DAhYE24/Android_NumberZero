package dh_kang.nozero.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

import dh_kang.nozero.Dialog.DialFlaInfo;
import dh_kang.nozero.DataSet.FlavorValues;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-10-16.
 */
public class FlavorListAdapter extends ArrayAdapter<FlavorValues> {
    private static final String TAG = "NOZERO_FINAL";      /* LOG TEST */
    private ArrayList<FlavorValues> flavorList; /*  Declare java parameters */
    private static StringBuilder selectedFlavors;
    private static String flavorName = null;
    private static long selectedCount = 0;

    public FlavorListAdapter(Context context, int textViewResourceId, ArrayList<FlavorValues> flavorList) {
        super(context, textViewResourceId, flavorList);
        this.flavorList = new ArrayList<FlavorValues>();
        this.flavorList.addAll(flavorList);
    }

    private class ViewHolder {  // ViewHolder's components
        CheckBox chk_flavor1, chk_flavor2, chk_flavor3;
    }

    public static StringBuilder getSelectedFlavors() {
        return selectedFlavors;
    }

    public static String getFlavorName() {
        return flavorName;
    }

    public static long getSelectedCount() {
        return selectedCount;
    }

    public static void setSelectedCount(long selectedCount) {
        FlavorListAdapter.selectedCount = selectedCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = new ViewHolder();
        if (convertView == null) {
            /* Connect xml with fragment layout */
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.lv_search_flavor, null);
            viewholder.chk_flavor1 = (CheckBox) convertView.findViewById(R.id.chk_flavor1);
            viewholder.chk_flavor2 = (CheckBox) convertView.findViewById(R.id.chk_flavor2);
            viewholder.chk_flavor3 = (CheckBox) convertView.findViewById(R.id.chk_flavor3);
            convertView.setTag(viewholder);

            /* Checkbox functions1 : select this flavor */
            viewholder.chk_flavor1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selectFlavors(v);
                }
            });
            viewholder.chk_flavor2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selectFlavors(v);
                }
            });
            viewholder.chk_flavor3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selectFlavors(v);
                }
            });

            /* Checkbox functions2 : show this flavor's information */
            viewholder.chk_flavor1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showFlavorInfoByDialog(v);
                    return false;
                }
            });
            viewholder.chk_flavor2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showFlavorInfoByDialog(v);
                    return false;
                }
            });
            viewholder.chk_flavor3.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showFlavorInfoByDialog(v);
                    return false;
                }
            });

//            // 리스트뷰의 체크박스 연결
//            convertView.setTag(viewholder); // 칸 클릭시 뭐 나올지 인듯
        }
        else { // TODO : 이거 필요할까??
            viewholder = (ViewHolder) convertView.getTag();
        }

        /* Connect xml to data-set */
        FlavorValues flavorData = flavorList.get(position);
        viewholder.chk_flavor1.setText(flavorData.getFlavor1());
        viewholder.chk_flavor1.setChecked(flavorData.isSelected1());
        viewholder.chk_flavor1.setTag(flavorData);
        viewholder.chk_flavor2.setText(flavorData.getFlavor2());
        viewholder.chk_flavor2.setChecked(flavorData.isSelected2());
        viewholder.chk_flavor2.setTag(flavorData);
        viewholder.chk_flavor3.setText(flavorData.getFlavor3());
        viewholder.chk_flavor3.setChecked(flavorData.isSelected3());
        viewholder.chk_flavor3.setTag(flavorData);

        return convertView;
    }

    /* Show flavor's information using dialog */
    private void showFlavorInfoByDialog(View view) {
        CheckBox checkbox = (CheckBox) view;
        DialFlaInfo flaDialog = new DialFlaInfo(getContext());
            flavorName = String.valueOf(checkbox.getText());    // Get this flavor's name
        flaDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        flaDialog.show();
    }

    /* */
    private void selectFlavors(View view) {
        selectedFlavors = new StringBuilder();   // TODO : 위치 이전으로 옮겨야할지?
        CheckBox checkbox = (CheckBox) view;
        FlavorValues flavors = (FlavorValues) checkbox.getTag();
        if (checkbox.isChecked() == true) { // 체크하는 경우
//            flavors.setSelected2(checkbox.isChecked() == false);
            selectedFlavors.append(checkbox.getText() + " ");
            selectedCount++;
        } else { // 체크 해제하는 경우
//            flavors.setSelected2(checkbox.isChecked() == true);
            String temp = String.valueOf(checkbox.getText());
            int pos = selectedFlavors.indexOf(temp);
            if (selectedFlavors.length() > 0) {
                selectedFlavors.delete(pos, pos + temp.length() + 1);
            }
            selectedCount--;
        }

        if (selectedFlavors.length() > 0) {
            Toast.makeText(getContext(), "선택한 향료 : " + selectedFlavors, Toast.LENGTH_SHORT).show();
        }
    }

}
