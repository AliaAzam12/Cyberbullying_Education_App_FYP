package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SelfAssessment extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    TextView textPage, textQuestion1;
    Button chooseA, chooseB, btnNextQ1;
    int currQuestion = 0;
    boolean isClickBtn = false;
    String valueChoose = "";
    int result = 0;
    int totalQuestions = Questions.questions.length;
    String selectedAnswer = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_assessment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textPage = findViewById(R.id.textViewpage1);
        textQuestion1 = findViewById(R.id.textViewQ1);
        chooseA = findViewById(R.id.buttonQ1A);
        chooseB = findViewById(R.id.buttonQ1B);
        btnNextQ1 = findViewById(R.id.buttonNextQ1);

        chooseA.setOnClickListener(this);
        chooseB.setOnClickListener(this);
        btnNextQ1.setOnClickListener(this);

        data();
    }

    @Override
    public void onClick(View view){

        //chooseA.setBackgroundColor(Color.WHITE);
        //chooseB.setBackgroundColor(Color.WHITE);

        Button btnClick = (Button) view;
        if (btnClick.getId() == R.id.buttonNextQ1){

            if (selectedAnswer.equals(Questions.correct_list[currQuestion])) {
                result = result + 1;
            }
            currQuestion++;
            data();
            chooseA.setBackgroundResource(R.drawable.bg_btn_choose);
            chooseB.setBackgroundResource(R.drawable.bg_btn_choose);
        }else {
            selectedAnswer = btnClick.getText().toString();
            btnClick.setBackgroundResource(R.drawable.bg_choose_color);
        }

    }

    void data(){
        if (currQuestion == totalQuestions - 1) {
            btnNextQ1.setText("SUBMIT");
        } else {
            btnNextQ1.setText("NEXT");
        }

        if (currQuestion == totalQuestions){
            finishTest();
            return;
        }
        textPage.setText((currQuestion+1) + "/" + Questions.questions.length);
        textQuestion1.setText(Questions.questions[currQuestion]);
        chooseA.setText(Questions.chooseList[currQuestion][0]);
        chooseB.setText(Questions.chooseList[currQuestion][1]);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void finishTest(){
        String passStatus = "";
        if (result > totalQuestions * 0.50){
            passStatus = "BAD RESULT";
            new AlertDialog.Builder(this)
                    .setTitle(passStatus)
                    .setMessage("Your result is " + result + "/" + totalQuestions)
                    .setPositiveButton("Meet Counselor Now", (dialogInterface, i) -> goToCounselor())
                    .setCancelable(false)
                    .show();
        }else {
            passStatus = "GOOD RESULT";
            new AlertDialog.Builder(this)
                    .setTitle(passStatus)
                    .setMessage("Your result is " + result + "/" + totalQuestions)
                    .setPositiveButton("Self-Help", (dialogInterface, i) -> goToSelfHelp())
                    .setCancelable(false)
                    .show();
        }
    }

    private void goToSelfHelp() {
        Intent intent = new Intent(SelfAssessment.this, SelfHelpPage.class);
        startActivity(intent);
    }

    private void goToCounselor() {
        Intent intent = new Intent(SelfAssessment.this, Counselor.class);
        startActivity(intent);
    }
}