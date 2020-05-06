package com.example.randomizer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.randomizer.R;
import com.example.randomizer.activities.JournalActivity;
import com.example.randomizer.data.MedicationDataHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringJoiner;

public class ItemArrayListAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<String> extractedData;//names, ages, numbers, etc.


    private ArrayList<String> NAME = null;
    private ArrayList<String> DOSE = null;//name
    private ArrayList<String> QTY = null;//qty
    private ArrayList<String> REFILLS =null;//refills
    private ArrayList<String> INFO = null;//rsx
    private ArrayList<String> RSX = null;//rsx
    private ArrayList<String> DATE = null;//rsx
    private ArrayList<String> TIME = null;//rsx
    private ArrayList<String> TAKEN = null;//rsx
    LinkedHashMap<String, ArrayList<String>> map;
    //------------------------------------------------------------------------
    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9 | CODES

//    JournalActivity.this, NAME,RSX,DOSE,QTY,REFILLS,INFO,DATE,TIME,TAKEN

    public ItemArrayListAdapter(Context context, ArrayList<String> NAME, ArrayList<String> RSX,
                                ArrayList<String> DOSE, ArrayList<String> QTY,
                                ArrayList<String> REFILLS, ArrayList<String> INFO, ArrayList<String> DATE,
                                ArrayList<String> TIME, ArrayList<String> TAKEN){
        this.context = context;
        this.NAME = new ArrayList<>(NAME);
        this.QTY = new ArrayList<>(QTY);
        this.REFILLS = new ArrayList<>(REFILLS);
        this.RSX= new ArrayList<>(RSX);
        this.DATE = new ArrayList<>(DATE);
        this.TIME = new ArrayList<>(TIME);
        this.DOSE = new ArrayList<>(DOSE);
        this.INFO = new ArrayList<>(INFO);
        this.TAKEN = new ArrayList<>(TAKEN);

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
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final DataSet holder;
//        controldb =new MedicationDataHelper(context);
        LayoutInflater layoutInflater;

        if (convertView == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //table_layout, row_layout
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.table_layout, null);
            holder = new DataSet();

            holder.NAME = (TextView) convertView.findViewById(R.id.tableCell_name);
            holder.QTY = (TextView) convertView.findViewById(R.id.tableCell_qty);
            holder.REFILL = (TextView) convertView.findViewById(R.id.tableCell_refill);
            holder.RSX = (TextView) convertView.findViewById(R.id.tableCell_RSX);
            //-----------------------------------
            holder.DATE = (TextView) convertView.findViewById(R.id.tableCell_date);
            holder.DOSE = (TextView) convertView.findViewById(R.id.tableCell_dose);
            holder.INFO = (TextView) convertView.findViewById(R.id.tableCell_info);
            holder.TAKEN = (TextView) convertView.findViewById(R.id.tableCell_intake);

            convertView.setTag(holder);
        } else {
            holder = (DataSet) convertView.getTag();
        }

        //use linkedHashMap to populate fields
        holder.NAME.setText("NAME : " + NAME.get(position));
        holder.QTY.setText("QUANTITY : " +QTY.get(position));
        holder.REFILL.setText("REFILLS : " + REFILLS.get(position));
        holder.RSX.setText("RSX# : " + RSX.get(position));

        String[] dateToken = DATE.get(position).split("\\s");
        String[] timeToken = dateToken[1].split(":");
        int hour = Integer.parseInt(timeToken[0]);
        int minute = Integer.parseInt(timeToken[1]);
        int sec = Integer.parseInt(timeToken[2]);
        String m = (minute < 10) ? "0" + minute : Integer.toString(minute);

        if(hour > 12)
            holder.DATE.setText(dateToken[0] + " "+ (hour-12) +":"+m + ":"+sec+" PM");
        else
            holder.DATE.setText(dateToken[0] + " "+hour +":"+m +":"+sec+ " AM");


        holder.DOSE.setText("DOSE : " + DOSE.get(position));
        holder.INFO.setText("INFO/COMMENTS : \n" + INFO.get(position));
        holder.TAKEN.setText("TAKEN : " + TAKEN.get(position));

        return convertView;

    }
    private static class DataSet {

        TextView NAME, QTY, REFILL, RSX,DATE, DOSE, INFO, TAKEN;
    }
}