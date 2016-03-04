package com.murach.tipcalculator;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    //database variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tips.db";
    private static final String TABLE_TIPS = "tips";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_BILL_DATE = "bill_date";
    private static final String COLUMN_BILL_AMOUNT = "bill_amount";
    private static final String COLUMN_TIP_PERCENT = "tip_percent";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_TIPS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BILL_DATE + " INTEGER, " +  COLUMN_BILL_AMOUNT + " REAL, " + COLUMN_TIP_PERCENT + " REAL );";
        db.execSQL(query);

        String row1 = "INSERT INTO " + TABLE_TIPS + " ( " + COLUMN_ID + " , " + COLUMN_BILL_DATE + ", " + COLUMN_BILL_AMOUNT +
                ", " + COLUMN_TIP_PERCENT + ") " + "VALUES (" + "1" + ", " + "0" + ", " + "40.60" + ", " + "0.15" + ");";
        db.execSQL(row1);

        String row2 = "INSERT INTO " + TABLE_TIPS + " ( " + COLUMN_ID + " , " + COLUMN_BILL_DATE + ", " + COLUMN_BILL_AMOUNT +
                ", " + COLUMN_TIP_PERCENT + ") " + "VALUES ( " + "2" + ", " + "0" + ", " + "25.40" + ", " + ".20" + " );";
        db.execSQL(row2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPS);
        onCreate(db);
    }



    public void saveTips(Tip t) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_DATE, t.getDateMillis());
        values.put(COLUMN_BILL_AMOUNT, t.getBillAmount());
        values.put(COLUMN_TIP_PERCENT, t.getTipPercent());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TIPS, null, values);
        db.close();
    }



    public Tip lastTipSaved() {
        ArrayList<Tip> list = getTips();

        Tip t = list.get(list.size() - 1);

        return t;
    }


    public float getAvgTipPercent() {
        ArrayList<Tip> tips = getTips();
        float total = 0;
        for (Tip tip : tips) {
            total += tip.getTipPercent();
        }
        float average = total / tips.size();
        return average;
    }


    public ArrayList<Tip> getTips() {
        ArrayList<Tip> tips = new ArrayList<Tip>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TIPS + " WHERE 1";

        //cursor point to a location in your results
        Cursor c = db.rawQuery(query, null);

        //move to the first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            Tip tip = cursorToTip(c);
            tips.add(tip);
            c.moveToNext();
        }

        db.close();
        return tips;
    }

    private Tip cursorToTip(Cursor c) {
        Tip tip = new Tip();
        tip.setId(c.getInt(0));
        tip.setDateMillis(c.getLong(1));
        tip.setBillAmount(c.getFloat(2));
        tip.setTipPercent(c.getFloat(3));
        return tip;
    }


}
