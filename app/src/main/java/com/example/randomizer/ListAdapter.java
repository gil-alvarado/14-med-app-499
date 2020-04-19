package com.example.randomizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private Context mContext;
    DataHelper controldb;
//    SQLiteDatabase db;
    private ArrayList<String> NAME = new ArrayList<String>();//name
    private ArrayList<String> DOSE = new ArrayList<String>();//name
    private ArrayList<String> QTY = new ArrayList<String>();//qty
    private ArrayList<String> REFILLS = new ArrayList<String>();//refills
    private ArrayList<String> INFO = new ArrayList<String>();//rsx
    private ArrayList<String> RSX = new ArrayList<String>();//rsx
    private ArrayList<String> DATE = new ArrayList<String>();//rsx
    private ArrayList<String> TAKEN = new ArrayList<String>();//rsx

    public ListAdapter(Context  context, ArrayList<String> NAME,
                       ArrayList<String> QTY, ArrayList<String> REFILLS, ArrayList<String> RSX)
    {
        this.mContext = context;
        this.NAME = NAME;
        this.QTY = QTY;
        this.REFILLS = REFILLS;
        this.RSX=RSX;
    }

    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
    public ListAdapter(Context context, ArrayList<String> NAME, ArrayList<String> RSX,
                       ArrayList<String> DOSE, ArrayList<String> QTY,
                               ArrayList<String> REFILLS, ArrayList<String> DATE, ArrayList<String> TAKEN,
                                ArrayList<String> INFO)
    {
        this.mContext = context;
        this.NAME = NAME;
        this.QTY = QTY;
        this.REFILLS = REFILLS;
        this.RSX=RSX;
        this.DATE = DATE;
        this.DOSE = DOSE;
        this.INFO = INFO;
        this.TAKEN = TAKEN;


    }

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
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final viewDataSet holder;
        controldb =new DataHelper(mContext);
        LayoutInflater layoutInflater;

        if (convertView == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //table_layout, row_layout
            convertView = layoutInflater.inflate(R.layout.table_layout, null);
            holder = new viewDataSet();

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
            holder = (viewDataSet) convertView.getTag();
        }
//        holder.NAME.append(NAME.get(position));
//        holder.QTY.append(QTY.get(position));
//        holder.REFILL.append(REFILLS.get(position));
//        holder.RSX.append(RSX.get(position));
//
//        holder.DATE.append(DATE.get(position));
//        holder.DOSE.append(DOSE.get(position));
//        holder.INFO.append(INFO.get(position));
//        holder.TAKEN.append(TAKEN.get(position));
        holder.NAME.setText("NAME : " + NAME.get(position));
        holder.QTY.setText("QUANTITY : " + QTY.get(position));
        holder.REFILL.setText("REFILLS : " + REFILLS.get(position));
        holder.RSX.setText("RSX# : " + RSX.get(position));
        holder.NAME.setText("NAME : " + NAME.get(position));
        holder.DATE.setText("DATE : " + DATE.get(position));
        holder.DOSE.setText("DOSE : " + DOSE.get(position));
        holder.INFO.setText("INFO/COMMENTS : \n" + INFO.get(position));
        holder.TAKEN.setText("TAKEN : " + TAKEN.get(position));
        return convertView;
    }
    private static class viewDataSet {
        /*
        this.DATE = DATE;
        this.DOSE = DOSE;
        this.INFO = INFO;
        this.TAKEN = TAKEN;
         */
        TextView NAME, QTY, REFILL, RSX,DATE, DOSE, INFO, TAKEN;
    }
}