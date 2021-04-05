package pl.edu.uwr.pum.pumproject2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pl.edu.uwr.pum.pumproject2.model.Category;
import pl.edu.uwr.pum.pumproject2.model.CategoryEntity;
import pl.edu.uwr.pum.pumproject2.repository.CategoryRepository;

public class CategoryViewModel extends AndroidViewModel {
    private final CategoryRepository mRepository;

    private final LiveData<List<CategoryEntity>> mAllCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        mRepository = new CategoryRepository(application);
        mAllCategories = mRepository.getAllCategories();
    }

    public LiveData<List<CategoryEntity>> getAllCategories() { return mAllCategories; }
}
