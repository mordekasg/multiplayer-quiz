package pl.edu.uwr.pum.pumproject2.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import pl.edu.uwr.pum.pumproject2.database.CategoryRoom;
import pl.edu.uwr.pum.pumproject2.model.CategoryDAO;
import pl.edu.uwr.pum.pumproject2.model.CategoryEntity;

public class CategoryRepository {
    private CategoryDAO mCategoryDAO;
    private LiveData<List<CategoryEntity>> mAllCategories;

    public CategoryRepository(Application application) {
        CategoryRoom db = CategoryRoom.getDatabase(application);
        mCategoryDAO = db.categoryDAO();
        mAllCategories = mCategoryDAO.getSorted();
    }

    public LiveData<List<CategoryEntity>> getAllCategories(){
        return mAllCategories;
    }
}
