package pl.edu.uwr.pum.pumproject2.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.edu.uwr.pum.pumproject2.R;
import pl.edu.uwr.pum.pumproject2.model.Question;
import pl.edu.uwr.pum.pumproject2.model.QuestionsSingle;

public class QuestionsPagerAdapter extends RecyclerView.Adapter<QuestionsPagerAdapter.ViewHolder> {
    private Context context;
    private QuestionsSingle qs;
    private Question currQuestion;

    public QuestionsPagerAdapter(Context context){
        qs = qs.get(context);
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvQuestion;
        private RadioGroup rgAnswers;
        private RadioButton rbAnswer1;
        private RadioButton rbAnswer2;
        private RadioButton rbAnswer3;
        private RadioButton rbAnswer4;
        private List<RadioButton> rbAnswers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            rgAnswers = itemView.findViewById(R.id.rgAnswers);
            rbAnswer1 = itemView.findViewById(R.id.rbAnswer1);
            rbAnswer2 = itemView.findViewById(R.id.rbAnswer2);
            rbAnswer3 = itemView.findViewById(R.id.rbAnswer3);
            rbAnswer4 = itemView.findViewById(R.id.rbAnswer4);

            rbAnswers = new ArrayList<>(Arrays.asList(rbAnswer1));
            rbAnswers.add(rbAnswer2);
            rbAnswers.add(rbAnswer3);
            rbAnswers.add(rbAnswer4);

            currQuestion = qs.getQuestions().get(0);

            rgAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup rg, int i) {
                    int rbID = rg.getCheckedRadioButtonId();
                    View rb = rg.findViewById(rbID);
                    int id = rg.indexOfChild(rb);
                    currQuestion.selectedAnswer = ((RadioButton)rg.getChildAt(id)).getText().toString();
                }
            });
        }

        public void bind(Question currentQuestion) {
            currQuestion = currentQuestion;
            tvQuestion.setText(currentQuestion.question);

            int i = 0;
            for (String value : currentQuestion.allAnswers) {
                rbAnswers.get(i).setText(value);
                i++;
            }
        }
    }

    @NonNull
    @Override
    public QuestionsPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.questions_view_pager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsPagerAdapter.ViewHolder holder, int position) {
        Question currentQuestion = qs.getQuestions().get(position);
        holder.bind(currentQuestion);
    }

    @Override
    public int getItemCount() {
        return qs.getQuestions().size();
    }
}
