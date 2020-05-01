package com.example.randomizer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.randomizer.R;
import com.example.randomizer.data.MedicationDataHelper;

import java.util.ArrayList;

public class AlarmListAdapter extends BaseAdapter {


    private Context context;
    private MedicationDataHelper controldb;
    //    SQLiteDatabase db;
    private ArrayList<String> NAME = new ArrayList<String>();//name
    private ArrayList<String> INFO = new ArrayList<String>();//rsx
    private ArrayList<String> DATE = new ArrayList<String>();//rsx
//------------------------------------------------------------------------
    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
    public AlarmListAdapter(Context context, ArrayList<String> NAME, ArrayList<String> DATE,
                              ArrayList<String> INFO)
    {
        this.context = context;
        this.NAME = NAME;
        this.DATE = DATE;
        this.INFO = INFO;
    }
//------------------------------------------------------------------------
    @Override
    public int getCount() {
        return NAME.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

//    ------------------------------------------------------------------------
    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final DataSet holder;
        controldb =new MedicationDataHelper(context);
        LayoutInflater layoutInflater;

        if (convertView == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //testing different layouts
//            convertView = layoutInflater.inflate(R.layout.alarm_layout, null);
            convertView = layoutInflater.inflate(R.layout.alarm_item, null);
            holder = new DataSet();

            holder.NAME = (TextView) convertView.findViewById(R.id.NAME_TABLE);
            holder.DATE = (TextView) convertView.findViewById(R.id.TIME_TABLE);
            holder.INFO = (TextView) convertView.findViewById(R.id.INFO_TABLE);

            convertView.setTag(holder);
        } else {
            holder = (DataSet) convertView.getTag();
        }
        holder.NAME.setText(NAME.get(position));
        holder.DATE.setText(DATE.get(position));
        holder.INFO.setText(INFO.get(position));
        return convertView;
    }
    private static class DataSet {

        TextView NAME,DATE, INFO;
    }
}