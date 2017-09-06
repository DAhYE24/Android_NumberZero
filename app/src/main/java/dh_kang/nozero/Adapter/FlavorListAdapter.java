package dh_kang.nozero.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

import dh_kang.nozero.Dialog.FlavorInfoDialog;
import dh_kang.nozero.DataSet.FlavorValues;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-10-16.
 */
public class FlavorListAdapter extends ArrayAdapter<FlavorValues> {
    private static final String TAG = "NOZERO_FINAL";      /* LOG TEST */
    private ArrayList<FlavorValues> flavorList; /*  Declare java parameters */
    private static StringBuilder selectedFlavors;
    private static String flavorName;
    private static long selectedCount;

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
    }   // Return selected list in SearchFragment

    public static long getSelectedCount() {
        return selectedCount;
    }   // Return selected count in SearchFragment

    public static String getFlavorName() {
        return flavorName;
    }

    public static void setSelectedCount(long selectedCount) {
        FlavorListAdapter.selectedCount = selectedCount;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewholder = new ViewHolder();
        selectedFlavors = new StringBuilder();  // Init StringBuilder

        if (convertView == null) {
            /* Connect xml with fragment layout */
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.lv_search_flavor, null);
            viewholder.chk_flavor1 = (CheckBox) convertView.findViewById(R.id.chk_flavor1);
            viewholder.chk_flavor2 = (CheckBox) convertView.findViewById(R.id.chk_flavor2);
            viewholder.chk_flavor3 = (CheckBox) convertView.findViewById(R.id.chk_flavor3);

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
        } else {
            // TODO : convertView가 null이 아닌경우에는 기존의 View를 재활용 하기때문에 새롭게 View를 inflate할 필요 없이 데이터만 바꾸는 작업을 하면 된다
            // TODO : 지우게 되면 오류가 생김
            viewholder = (ViewHolder) convertView.getTag();
        }

        convertView.setTag(viewholder);
        return convertView;
    }

    /* Show flavor's information using dialog */
    private void showFlavorInfoByDialog(View view) {
        CheckBox checkbox = (CheckBox) view;
        FlavorInfoDialog flavorInfoDialog = new FlavorInfoDialog(getContext());
        flavorName = String.valueOf(checkbox.getText());    // TODO : fragment에서 dailog로 데이터 전달하는 다른 방법이 있는지
        flavorInfoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        flavorInfoDialog.show();
    }

    /* Show selected flavors list */
    private void selectFlavors(View view) {
        CheckBox checkbox = (CheckBox) view;
        FlavorValues flavors = (FlavorValues) checkbox.getTag();

        // Recognize checkbox state
        if (checkbox.isChecked()) {
//            flavors.setSelected2(checkbox.isChecked() == false);
            selectedFlavors.append(checkbox.getText() + " ");
            selectedCount++;
        } else {
//            flavors.setSelected2(checkbox.isChecked() == true);
//            String temp = String.valueOf(checkbox.getText());
            int position = selectedFlavors.indexOf(String.valueOf(checkbox.getText()));
            if (selectedFlavors.length() > 0) {
                selectedFlavors.delete(position, position + checkbox.getText().length() + 1);
            }
            selectedCount--;
        }

        // Notice flavor list that user selected
        if (selectedFlavors.length() > 0) {
            Toast.makeText(getContext(), "선택한 향료 : " + selectedFlavors, Toast.LENGTH_SHORT).show();
        }
    }
}