package com.aaxena.ourjuet;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.aaxena.ourjuet.data.DateSheet;

import java.util.List;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DateSheetDao {
    @Insert(onConflict = REPLACE)
    void insert(DateSheet data);

    @Query("SELECT * from datesheet")
    LiveData<List<DateSheet>> dateSheet();

    @Query("DELETE FROM datesheet")
    void deleteAll();
}