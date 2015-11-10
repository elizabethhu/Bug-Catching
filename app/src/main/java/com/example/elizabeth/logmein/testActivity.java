package com.example.elizabeth.logmein;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class testActivity extends AppCompatActivity {
    private Firebase myRef;
    private long starttime;
    private String username;
    private long score;
    private long highest;
    private boolean score_saved;
    private final long totoaltime = 30000;

    private RadioGroup radioGroupTJColor;
    private RadioButton radioRed;
    private RadioButton radioWhite;
    private RadioButton radioBlue;

    private TextView yourscore;
    private TextView highestscore;
    private TextView msg;
    private TextView uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        uid = (TextView) findViewById(R.id.textViewUID);
        Button btnStart = (Button) findViewById(R.id.btnGameStart);
        radioGroupTJColor = (RadioGroup) findViewById(R.id.radioGroupTJColor);
        radioRed = (RadioButton) findViewById(R.id.radioRed);
        radioWhite = (RadioButton) findViewById(R.id.radioWhite);
        radioBlue = (RadioButton) findViewById(R.id.radioBlue);
        yourscore = (TextView) findViewById(R.id.textViewYourScore);
        msg = (TextView) findViewById(R.id.textViewMsg);
        highestscore = (TextView) findViewById(R.id.textViewHighScore);

        username = getIntent().getExtras().getString("uid").split("@")[0];
        uid.setText(username);
        highest = 0;

        myRef = new Firebase("https://sweltering-inferno-8943.firebaseio.com/game");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean newUser = true;
                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                    Gamer agamer = gameSnapshot.getValue(Gamer.class);
                    if (username.equals(agamer.getUsername())) {
                        highest = Long.valueOf(agamer.getScore());
                        newUser = false;
                    }
                }
                if (newUser) {
                    Firebase newgamerpath = myRef.child(username);
                    Gamer newgamer = new Gamer(username, "0");
                    newgamerpath.setValue(newgamer);
                }
                highestscore.setText(Long.toString(highest * 1000));
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });

        btnStart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                starttime = System.currentTimeMillis();
                score_saved = false;
                score = 0;
                yourscore.setText(Long.toString(score * 1000));
                msg.setText("");
            }
        });

        radioRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playgame();
            }
        });
        radioWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playgame();
            }
        });
        radioBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playgame();
            }
        });
    }
    protected void playgame (){
        int selectedId = radioGroupTJColor.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
        String radioColor = (String) selectedRadioButton.getText();
        long currenttime = System.currentTimeMillis();
        long timeused = currenttime - starttime;
        if (timeused < totoaltime) {
            if (radioColor.equals(winColor()))
                score++;
        } else {

            if (score > highest){
                highestscore.setText(Long.toString(score * 1000));
                Gamer saveGamer = new Gamer( (String) uid.getText(), Long.toString(score));
                myRef.child(username).setValue(saveGamer);
            }
            msg.setText("Game is over, click on Start Game to play again.");

        }
        yourscore.setText(Long.toString(score * 1000));
    }
    protected String winColor() {
        String winner;
        double winning_number = Math.random();
        if (winning_number <= 0.33)
            winner = "Red";
        else if (winning_number >= 0.67)
            winner = "Blue";
        else
            winner = "White";
        return winner;
    }
}
