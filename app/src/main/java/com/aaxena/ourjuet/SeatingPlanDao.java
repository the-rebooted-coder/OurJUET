package com.aaxena.ourjuet;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.aaxena.ourjuet.data.SeatingPlan;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SeatingPlanDao {
    @Insert(onConflict = REPLACE)
    void insert(SeatingPlan data);

    @Query("SELECT * from seatingplan")
    LiveData<List<SeatingPlan>> seatingPlan();

    @Query("DELETE FROM seatingplan")
    void deleteAll();
}
