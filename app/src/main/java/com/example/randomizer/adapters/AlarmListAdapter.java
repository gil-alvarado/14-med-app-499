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
    private ArrayList<String> NAME = null;//new ArrayList<String>();//name
    private ArrayList<String> TIME = null;//new ArrayList<String>();//rsx
    private ArrayList<String> CODES = null;//new ArrayList<String>();//rsx
//------------------------------------------------------------------------
    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
    public AlarmListAdapter(Context context, ArrayList<String> NAME, ArrayList<String> TIME,
                              ArrayList<String> CODES)
    {
        this.context = context;
        this.NAME = new ArrayList<>(NAME);
        this.TIME = new ArrayList<>(TIME);
        this.CODES = new ArrayList<>(CODES);
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
            holder.TIME = (TextView) convertView.findViewById(R.id.TIME_TABLE);
            holder.CODES = (TextView) convertView.findViewById(R.id.INFO_TABLE);

            convertView.setTag(holder);
        } else {
            holder = (DataSet) convertView.getTag();
        }
//        ----------------------------------------------------------
//        ----------------------------------------------------------


        holder.NAME.setText(NAME.get(position));//arrayList to get data

        String[] token = TIME.get(position).split(":");
        int hour = Integer.parseInt(token[0]);
        int minute = Integer.parseInt(token[1]);
        String m = (minute < 10) ? "0" + minute : Integer.toString(minute);

        if(hour > 12)
            holder.TIME.setText((hour-12) +":"+m + " PM");
        else
            holder.TIME.setText(hour +":"+m + " AM");
//        holder.CODES.setText(CODES.get(position));


        String[] dayCodes = CODES.get(position).split("\\s");
        StringBuilder days = new StringBuilder();
        String prefix = "";
        if (dayCodes[0].equals("8")){
            holder.CODES.setText("daily");
        }
        else {

            for (String d : dayCodes) {
                switch (d) {
                    case "1":
                        days.append(prefix);
                        prefix = ",";
                        days.append("Su");
                        break;
                    case "2":
                        days.append(prefix);
                        prefix = ",";
                        days.append("Mo");
                        break;
                    case "3":
                        days.append(prefix);
                        prefix = ",";
                        days.append("Tu");
                        break;
                    case "4":
                        days.append(prefix);
                        prefix = ",";
                        days.append("We");
                        break;
                    case "5":
                        days.append(prefix);
                        prefix = ",";
                        days.append("Th");
                        break;
                    case "6":
                        days.append(prefix);
                        prefix = ",";
                        days.append("Fr");
                        break;
                    case "7":
                        days.append(prefix);
                        prefix = ",";
                        days.append("Sa");
                        break;
                }
            }
            holder.CODES.setText(days.toString());
        }

        return convertView;
    }
    private static class DataSet {

        TextView NAME,TIME, CODES;
    }
}