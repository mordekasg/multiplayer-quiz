package pl.edu.uwr.pum.pumproject2.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.edu.uwr.pum.pumproject2.R;
import pl.edu.uwr.pum.pumproject2.model.CategoryEntity;
import pl.edu.uwr.pum.pumproject2.model.Question;
import pl.edu.uwr.pum.pumproject2.model.QuestionsSingle;
import pl.edu.uwr.pum.pumproject2.model.TriviaQuestions;
import pl.edu.uwr.pum.pumproject2.model.TriviaService;
import pl.edu.uwr.pum.pumproject2.viewmodel.CategoryViewModel;

public class MainActivity extends AppCompatActivity {
    private CategoryViewModel viewModel;
    private QuestionsSingle qs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qs = QuestionsSingle.get(this);

        Spinner spinnerCategory = findViewById(R.id.spinnerCategories);
        spinnerCategory.setEnabled(false);

        NumberPicker npQuestions = findViewById(R.id.npQuestions);
        npQuestions.setMinValue(3);
        npQuestions.setMaxValue(20);

        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                TODO
                 */
                // Download questions

                btnStart.setEnabled(false);
                CategoryEntity category = (CategoryEntity)spinnerCategory.getSelectedItem();

                (new TriviaService()).getQuestions(npQuestions.getValue(), category.getId())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<TriviaQuestions>() {
                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull TriviaQuestions values) {
                            if (values != null) {
                                qs.setCrimes(values.getTriviaQuestions());

                                // New activity
                                Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Toast.makeText(getApplicationContext(), "Cannot download questions. Please try again later.", Toast.LENGTH_SHORT);
                        }
                    });
            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                btnStart.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                btnStart.setEnabled(false);
            }
        });

        viewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(this.getApplication())).get(CategoryViewModel.class);

        viewModel.getAllCategories().observe(this, categoryEntities -> {
            ArrayAdapter spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryEntities);
            spinnerCategory.setAdapter(spinnerAdapter);
            spinnerCategory.setEnabled(true);
        });
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}