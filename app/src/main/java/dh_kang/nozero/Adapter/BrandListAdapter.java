package dh_kang.nozero.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dh_kang.nozero.DataSet.BrandValues;
import dh_kang.nozero.R;

/**
 * Created by dh93 on 2016-11-16.
 */
public class BrandListAdapter extends ArrayAdapter<BrandValues> {
    private static final String TAG = "NOZERO_FINAL";      /* LOG TEST */
    private ArrayList<BrandValues> brandList; /*  Declare java parameters */

    public BrandListAdapter(Context context, int textViewResourceId, List<BrandValues> brandList) {
        super(context, textViewResourceId, brandList);
        this.brandList = new ArrayList<BrandValues>();
        this.brandList.addAll(brandList);
    }

    private class ViewHolder {
        TextView txt_brandName;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewholder = new ViewHolder();
        BrandValues brandData = brandList.get(position);
        Log.i(TAG, brandData.toString());
        Log.i(TAG, brandData.getBrandTitle());
        viewholder.txt_brandName.setText(brandData.getBrandTitle());
        viewholder.txt_brandName.setTag(brandData);

        if (convertView == null) {
            /* Connect xml with fragment layout */
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.lv_search_brand, null);
            viewholder.txt_brandName = (TextView) convertView.findViewById(R.id.txt_brandName);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        convertView.setTag(viewholder);
        return convertView;
    }
}
