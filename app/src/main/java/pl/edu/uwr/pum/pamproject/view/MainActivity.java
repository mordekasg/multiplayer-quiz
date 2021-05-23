package pl.edu.uwr.pum.pamproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

import pl.edu.uwr.pum.pamproject.R;
import pl.edu.uwr.pum.pamproject.model.CategoryEntity;
import pl.edu.uwr.pum.pamproject.model.Question;
import pl.edu.uwr.pum.pamproject.model.QuestionsSingle;
import pl.edu.uwr.pum.pamproject.model.TriviaQuestions;
import pl.edu.uwr.pum.pamproject.model.TriviaService;
import pl.edu.uwr.pum.pamproject.viewmodel.CategoryViewModel;

public class MainActivity extends AppCompatActivity {
    private CategoryViewModel viewModel;
    private QuestionsSingle qs;
    private androidx.constraintlayout.widget.ConstraintLayout clGameModeSelect, clCategorySelect, clMultiplayer, clWaiting;
    public static boolean multiMode = false;
    private static String username = "";
    private static URI serverURL;
    public static WebSocketClient wsClient;
    private static String gamestatus = "waitingforplayers";
    private static int myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qs = QuestionsSingle.get(this);

        TextView tvWaiting = findViewById(R.id.tvWaiting);

        try {
            // HOST IP: 10.0.2.2
            serverURL = new URI("ws://10.0.2.2:1337/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        wsClient = new WebSocketClient(serverURL) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                wsClient.send(username);
            }

            @Override
            public void onMessage(String s) {
                JSONObject obj = null;
                String type = null;
                try {
                    obj = new JSONObject(s);
                } catch (JSONException e) {
                    Log.d("WebSocket", "Cannot read message: " + e.getMessage());
                }

                try {
                    type = obj.getString("type");
                } catch (JSONException e) {
                    Log.d("WebSocket", "Cannot read message: " + e.getMessage());
                }

                switch (type) {
                    case "gamestatus":
                        try {
                            gamestatus = obj.getString("data");
                        } catch (JSONException e) {
                            Log.d("WebSocket", "Cannot read message: " + e.getMessage());
                        }
                        break;

                    case "yourid":
                        try {
                            myID = obj.getInt("data");
                        } catch (JSONException e) {
                            Log.d("WebSocket", "Cannot read message: " + e.getMessage());
                        }
                        break;

                    case "category":
                        gamestatus = "inprogress_category";

                        clWaiting.setVisibility(View.INVISIBLE);
                        clCategorySelect.setVisibility(View.VISIBLE);
                        tvWaiting.setText("Waiting for results...");
                        break;

                    case "questions":
                        Log.d("WebSocket", obj.toString());
                        clWaiting.setVisibility(View.VISIBLE);
                        clCategorySelect.setVisibility(View.INVISIBLE);
                        List<Question> values = null;

                        try {
                            JSONArray questions = obj.getJSONArray("data");
                            Question q;

                            values = new ArrayList<>();
                            for (int i = 0; i < questions.length(); i++) {
                                q = new Question();
                                q.question = questions.getJSONObject(i).getString("question");
                                q.correct_answer = questions.getJSONObject(i).getString("correct_answer");
                                JSONArray iA = questions.getJSONObject(i).getJSONArray("incorrect_answers");
                                List<String> iA2 = new ArrayList<String>();

                                for(int j = 0; j < iA.length(); j++){
                                    iA2.add(iA.getString(j));
                                }
                                q.incorrect_answers = iA2;

                                values.add(q);
                            }
                        } catch (JSONException e) {
                            Log.d("WebSocket", "Cannot read message: " + e.getMessage());
                        }

                        qs.setQuestions(values);

                        // New activity
                        Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);

                        break;

                    case "results":
                        int me = 0, max = 0, tie = -1;

                        try {
                            JSONArray res = obj.getJSONArray("data");

                            tie = max = me = res.getJSONObject(myID).getInt("correct");

                            for (int i = 0; i < res.length(); i++) {
                                if (i != myID) {
                                    int they = res.getJSONObject(i).getInt("correct");

                                    if (they > max) max = they;
                                    if (they != tie) tie = -1;
                                }
                            }
                        } catch (JSONException e) {
                            Log.d("WebSocket", "Cannot read message: " + e.getMessage());
                        }

                        /*
                            0 - won
                            1 - lost
                            2 - tie
                         */
                        int resultValue = 0;

                        if (me >= max) resultValue = 0;
                        else if (me < max) resultValue = 1;
                        else if (tie != -1) resultValue = 2;

                        // New activity
                        Intent intentR = new Intent(getApplicationContext(), ResultsActivity.class);
                        intentR.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentR.putExtra("resultValue", resultValue);
                        getApplicationContext().startActivity(intentR);
                        break;
                }

                Log.d("WebSocket", obj.toString());
            }

            @Override
            public void onClose(int i, String s, boolean b) {
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Log.d("WebSocket", "Connection error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Websocket connection error: " + e.getMessage(), Toast.LENGTH_SHORT);
                });
            }
        };

        EditText etUsername = findViewById(R.id.etUsername);

        clGameModeSelect = findViewById(R.id.clGameModeSelect);
        clCategorySelect = findViewById(R.id.clCategorySelect);
        clMultiplayer = findViewById(R.id.clMultiplayer);
        clWaiting = findViewById(R.id.clWaiting);

        Button btnSingle = findViewById(R.id.btnSingle);
        Button btnMulti = findViewById(R.id.btnMulti);
        Button btnMultiStart = findViewById(R.id.btnMultiStart);

        btnSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiMode = false;
                clGameModeSelect.setVisibility(View.INVISIBLE);
                clCategorySelect.setVisibility(View.VISIBLE);
                clMultiplayer.setVisibility(View.INVISIBLE);
            }
        });

        btnMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiMode = true;
                clGameModeSelect.setVisibility(View.INVISIBLE);
                clCategorySelect.setVisibility(View.INVISIBLE);
                clMultiplayer.setVisibility(View.VISIBLE);
            }
        });

        btnMultiStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clGameModeSelect.setVisibility(View.INVISIBLE);
                clCategorySelect.setVisibility(View.INVISIBLE);
                clMultiplayer.setVisibility(View.INVISIBLE);
                clWaiting.setVisibility(View.VISIBLE);
                username = etUsername.getText().toString();

                wsClient.connect();
            }
        });

        Spinner spinnerCategory = findViewById(R.id.spinnerCategories);
        spinnerCategory.setEnabled(false);

        NumberPicker npQuestions = findViewById(R.id.npQuestions);
        npQuestions.setMinValue(3);
        npQuestions.setMaxValue(20);
        npQuestions.setValue(5);

        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setEnabled(false);

                if (multiMode) {
                    CategoryEntity category = (CategoryEntity) spinnerCategory.getSelectedItem();
                    wsClient.send("{\"type\":\"category\",\"data\":\"" + category.getId() + "\"}");

                    tvWaiting.setText("Waiting for other opponents...");
                    clCategorySelect.setVisibility(View.INVISIBLE);
                    clWaiting.setVisibility(View.VISIBLE);
                }
                else {
                    CategoryEntity category = (CategoryEntity) spinnerCategory.getSelectedItem();

                    (new TriviaService()).getQuestions(npQuestions.getValue(), category.getId())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<TriviaQuestions>() {
                                @Override
                                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull TriviaQuestions values) {
                                    if (values != null) {
                                        qs.setQuestions(values.getTriviaQuestions());

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

    private void wsClientOnMessage(String s) {
        final String message = s;
        System.out.println(s);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}