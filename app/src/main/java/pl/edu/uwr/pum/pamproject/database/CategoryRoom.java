package pl.edu.uwr.pum.pamproject.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.edu.uwr.pum.pamproject.model.Category;
import pl.edu.uwr.pum.pamproject.model.CategoryDAO;
import pl.edu.uwr.pum.pamproject.model.CategoryEntity;
import pl.edu.uwr.pum.pamproject.model.TriviaCategory;
import pl.edu.uwr.pum.pamproject.model.TriviaService;

@Database(entities = {CategoryEntity.class}, version = 1, exportSchema = false)
public abstract class CategoryRoom extends RoomDatabase {
    public abstract CategoryDAO categoryDAO();

    private static volatile CategoryRoom INSTANCE;
    private static final int NUM_OF_THREADS = 4;
    private static final long CATEGORY_DATA_TIMEOUT = 30*60;//*1000;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUM_OF_THREADS);

    public static CategoryRoom getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(),
                    CategoryRoom.class, "category_database_java")
                    .addCallback(sRoomDatabaseCallback)
                    .build();
        }

        return INSTANCE;
    }

    private static final Callback sRoomDatabaseCallback =
        new Callback() {
            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);

                CategoryRoom.databaseWriteExecutor.execute(() -> {
                    CategoryDAO mCategoryDao = CategoryRoom.INSTANCE.categoryDAO();

                    if (System.currentTimeMillis() - mCategoryDao.getLatest() >= CATEGORY_DATA_TIMEOUT) {
                        (new TriviaService()).getCategories()
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableSingleObserver<TriviaCategory>() {
                                    @Override
                                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull TriviaCategory values) {
                                        CategoryRoom.databaseWriteExecutor.execute(() -> {
                                            CategoryDAO mCategoryDao = CategoryRoom.INSTANCE.categoryDAO();
                                            mCategoryDao.deleteAll();

                                            if (values != null)
                                                for (Category category : values.getTriviaCategories())
                                                    mCategoryDao.insertWithTimestamp(new CategoryEntity(category.id, category.name));
                                        });
                                    }

                                    @Override
                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) { }
                                });
                    }
                });
            }

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }
        };
}
