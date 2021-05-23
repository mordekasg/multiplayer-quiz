package pl.edu.uwr.pum.pamproject.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import pl.edu.uwr.pum.pamproject.database.CategoryRoom;
import pl.edu.uwr.pum.pamproject.model.CategoryDAO;
import pl.edu.uwr.pum.pamproject.model.CategoryEntity;

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
