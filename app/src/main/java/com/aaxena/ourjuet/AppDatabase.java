package com.aaxena.ourjuet;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.aaxena.ourjuet.data.AttendenceData;
import com.aaxena.ourjuet.data.AttendenceDetails;
import com.aaxena.ourjuet.data.DateSheet;
import com.aaxena.ourjuet.data.ExamMarks;
import com.aaxena.ourjuet.data.SeatingPlan;


@Database(entities = {AttendenceData.class, AttendenceDetails.class, DateSheet.class, SeatingPlan.class, ExamMarks.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private  static com.aaxena.ourjuet.AppDatabase mAppDatabase;

    public static com.aaxena.ourjuet.AppDatabase newInstance(Context context) {
        if (mAppDatabase == null)
        mAppDatabase = Room.databaseBuilder(context.getApplicationContext(), com.aaxena.ourjuet.AppDatabase.class, "myjuet.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
    return mAppDatabase;
    }
    public abstract AttendenceDataDao AttendenceDao();
    public abstract AttendenceDetailsDao AttendenceDetailsDao();
    public abstract DateSheetDao DateSheetDao();
    public abstract SeatingPlanDao SeatingPlanDao();
    public abstract ExamMarksDao ExamMarksDao();
}
