package pl.edu.uwr.pum.pumproject2.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insert(CategoryEntity category);

    public void insertWithTimestamp(CategoryEntity category) {
       category.setMod_at(System.currentTimeMillis());
       insert(category);
    }

    @Query("DELETE FROM tbl_category")
    abstract public void deleteAll();

    @Query("SELECT * FROM tbl_category ORDER BY id ASC")
    abstract public LiveData<List<CategoryEntity>> getSorted();

    @Query("SELECT MAX(mod_at) FROM tbl_category")
    abstract public long getLatest();
}
