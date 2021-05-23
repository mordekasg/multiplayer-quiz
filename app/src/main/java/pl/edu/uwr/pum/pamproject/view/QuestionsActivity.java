package pl.edu.uwr.pum.pamproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pl.edu.uwr.pum.pamproject.R;
import pl.edu.uwr.pum.pamproject.model.Question;
import pl.edu.uwr.pum.pamproject.model.QuestionsSingle;

public class QuestionsActivity extends AppCompatActivity {
    private QuestionsSingle qs;
    private FloatingActionButton btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_questions);

        qs = QuestionsSingle.get(this);

        ViewPager2 viewPager2 = findViewById(R.id.questions_view_pager);

        QuestionsPagerAdapter adapter = new QuestionsPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Question question : qs.getQuestions()) {
                    if (question.correct_answer == question.selectedAnswer)
                        qs.correct++;
                    else
                        qs.wrong++;
                }

                // New activity
                Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // I dont want user to go back
    }
}