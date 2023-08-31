package com.devkraft.karmahealth.Model;

import android.content.ContentValues;
import android.database.Cursor;

import com.devkraft.karmahealth.db.WeightTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Weights {

    private Double height;
    private List<Ranges> ranges;

    public Weights(Double height, List<Ranges> ranges) {
        this.height = height;
        this.ranges = ranges;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public List<Ranges> getRanges() {
        return ranges;
    }

    public void setRanges(List<Ranges> ranges) {
        this.ranges = ranges;
    }

    public static Weights fromCursor(Cursor cursor) {
        String rangeStr = cursor.getString(cursor.getColumnIndex(WeightTable.RANGE));
        List<Ranges> rangesList = null;
        if(rangeStr != null){
            Gson gson = new Gson();
            Type reminderType = new TypeToken<List<Ranges>>() {
            }.getType();
            rangesList = gson.fromJson(rangeStr, reminderType);
        }

        return new Weights(cursor.getDouble(cursor.getColumnIndex(WeightTable.HEIGHT)),rangesList);
    }

    public ContentValues toCV() {
        ContentValues cv = new ContentValues();

        cv.put(WeightTable.HEIGHT,height);

        if(ranges != null){
            cv.put(WeightTable.RANGE,new Gson().toJson(ranges));
        }

        return cv;
    }
}
