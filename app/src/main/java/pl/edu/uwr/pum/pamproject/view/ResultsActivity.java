package pl.edu.uwr.pum.pamproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pl.edu.uwr.pum.pamproject.R;
import pl.edu.uwr.pum.pamproject.model.QuestionsSingle;

public class ResultsActivity extends AppCompatActivity {
    private QuestionsSingle qs;
    private FloatingActionButton btnStartOver;
    private TextView tvCorrect;
    private TextView tvWrong;
    private TextView tvScore;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int resultValue = 0;
        
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        if (MainActivity.multiMode)  {
            resultValue = intent.getExtras().getInt("resultValue");
            Log.d("Result", String.valueOf(resultValue));
        }

        qs = QuestionsSingle.get(this);

        tvCorrect = findViewById(R.id.tvCorrect);
        tvWrong = findViewById(R.id.tvWrong);
        tvScore = findViewById(R.id.tvScore);
        tvResult = findViewById(R.id.tvResult);

        double score = (((double)qs.correct * 100.0) / (double)(qs.correct + qs.wrong));

        tvCorrect.setText(String.format(getResources().getString(R.string.current_answers_d), qs.correct));
        tvWrong.setText(String.format(getResources().getString(R.string.wrong_answers_d), qs.wrong));
        tvScore.setText(String.format(getResources().getString(R.string.percentage_2f), score));

        if (MainActivity.multiMode) {
            switch (resultValue) {
                case 0:
                    tvResult.setText("You have won!");
                    break;
                case 1:
                    tvResult.setText("You have lost!");
                    break;
                case 2:
                    tvResult.setText("Its a tie!");
                    break;
            }
        }

        btnStartOver = findViewById(R.id.btnStartOver);
        btnStartOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // destroy both activities and go back to Main
                qs.clear();

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // I dont want user to go back
    }
}