package com.aaxena.ourjuet;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.aaxena.ourjuet.data.AttendenceData;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface AttendenceDataDao {
    @Insert(onConflict = REPLACE)
    void insert(AttendenceData data);

    @Query("SELECT * from attendencedata")
    LiveData<List<AttendenceData>> AttendanceDataObserver();

 @Query("SELECT COUNT(*) from attendencedata WHERE loading = 0")
 LiveData<Integer> AttendanceLoadingPendingCountObserver();

    @Query("SELECT * from attendencedata")
    List<AttendenceData> AttendanceData();

    @Query("SELECT * from attendencedata WHERE id = :id")
    LiveData<AttendenceData> getAttendenceById(String id);

    @Query("UPDATE  attendencedata SET mCountPresent = :p , mCountAbsent = :a, mOnNext=:attendNext, mOnLeaving = :leaveNext WHERE id = :id ")
    void updatePresentAbsent(String id, int p, int a, int attendNext, int leaveNext);

    @Query("UPDATE  attendencedata SET loading = :loading WHERE id = :id")
    void updateLoading(String id, boolean loading);

    @Query("UPDATE  attendencedata SET loading = :loading")
    void updateLoading(boolean loading);

    @Query("DELETE FROM  attendencedata")
    void clearData();
}
