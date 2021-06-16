package com.aaxena.ourjuet;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.aaxena.ourjuet.data.ExamMarks;

import java.util.List;


import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExamMarksDao {
    @Insert(onConflict = REPLACE)
    void insert(ExamMarks data);

    @Query("SELECT * from exammarks")
    LiveData<List<ExamMarks>> examMarks();

    @Query("DELETE FROM exammarks")
    void deleteAll();
}
