package dh_kang.nozero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dh93 on 2016-10-15.
 */
public class Lv_InfoAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> infoGroup;
    private HashMap<String, ArrayList<String>> infoChild;

    public Lv_InfoAdapter(Context context, ArrayList<String> infoGroup, HashMap<String, ArrayList<String>> infoChild) {
        super();
        this.context = context;
        this.infoGroup = infoGroup;
        this.infoChild = infoChild;
    }

    @Override
    public int getGroupCount() {
        //return 0;
        return infoGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //return 0;
        return infoChild.get( infoGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        //return null;
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //return null;
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //return null;
        String groupName = infoGroup.get(groupPosition);
        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (LinearLayout)inflater.inflate(R.layout.lv_info_group, null);
        }
        TextView textGroup = (TextView)v.findViewById(R.id.lig_txtGroup);
        textGroup.setText(groupName);

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childName = infoChild.get(infoGroup.get(groupPosition)).get(childPosition);
        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (RelativeLayout)inflater.inflate(R.layout.lv_info_child, null);
        }
        TextView textChild = (TextView)v.findViewById(R.id.lic_txtChild);
        textChild.setText(childName);

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true; //클릭 가능하게 설정
    }
}
