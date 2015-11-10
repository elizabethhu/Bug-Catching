package com.example.elizabeth.logmein;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Firebase myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button login = (Button) findViewById(R.id.mylogin);
        Button register = (Button) findViewById(R.id.myRegister);
        final TextView myText = (TextView) findViewById(R.id.textView2);
        final EditText uid = (EditText) findViewById(R.id.editTextEmail);
        final EditText pwd = (EditText) findViewById(R.id.editTextPWD);

        myRef = new Firebase("https://sweltering-inferno-8943.firebaseio.com/");

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                myRef.createUser(uid.getText().toString(), pwd.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        myText.setText("You have successfully registered a new account, please login to play.");
                        uid.setText("");
                        pwd.setText("");
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        myText.setText("Registration was not successful, please try again.");
                    }
                });

            }
        });


        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                myRef.authWithPassword(uid.getText().toString(), pwd.getText().toString(),
                        new Firebase.AuthResultHandler() {

                            @Override
                            public void onAuthenticated(AuthData authData) {
                                // Authentication just completed successfully :)
                               // System.out.println("Log in Successfully!");
                               // if (authData.getProviderData().containsKey("displayName")) {
                               //     myText.setText("login successfully!");
                                Intent i = new Intent(MainActivity.this, testActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("uid",uid.getText().toString() );
                                bundle.putString("pwd",pwd.getText().toString() );
                                i.putExtras(bundle);
                                startActivity(i);

                               // }
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                                // Something went wrong :(
                                myText.setText("login failed! If you don't have a user name, please register for a free account first.");
                            }

                        });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
