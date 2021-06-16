package com.aaxena.ourjuet;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.aaxena.ourjuet.data.AttendenceDetails;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface AttendenceDetailsDao {
    @Insert(onConflict = REPLACE)
    void insert(AttendenceDetails data);

    @Query("SELECT * from attendencedetails WHERE mSubjectId = :id")
    LiveData<List<AttendenceDetails>> AttendenceDetails(String id);

    @Query("DELETE FROM  attendencedetails")
    void clearData();

}
