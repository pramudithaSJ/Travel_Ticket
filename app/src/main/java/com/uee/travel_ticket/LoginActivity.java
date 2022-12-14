package com.uee.travel_ticket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button createAccount, login;
    EditText username, password;
    TextView tvSignUp;
    DatabaseReference databaseUsers;
    Spinner usersSpinner;
    public static String userType;
//    login_to userprofile;
    public static String loggedUser;
    public String savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//Initialize.
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        createAccount = findViewById(R.id.signUp);
        login = findViewById(R.id.login);
        tvSignUp = findViewById(R.id.textView4);

        // spinner element
        usersSpinner = (Spinner) findViewById(R.id.userSpinner);
        // Spinner click listener
        usersSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        // Spinner Drop down elements
        List<String> users = new ArrayList<String>();
        users.add("Local Passenger");
        users.add("Foreign Passenger");
        users.add("Inspector");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, users);
        // Drop down layout style - list view with radio button
        dataAdapterType.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // attaching data adapter to spinner
        usersSpinner.setAdapter(dataAdapterType);

//        userprofile = new login_to();


            Intent intent = new Intent(LoginActivity.this, SplashScreenActivity.class);
            startActivity(intent);

//
////            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                    new HomeFragment()).commit();
////            navigationView.setCheckedItem(R.id.nav_home);


        //go to the create account acctivity.
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateAcount();
            }
        });

        //go to the create user profile acctivity.
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( userType.equals("Local Passenger") ) {
                    openlogin();
                }
                if ( userType.equals("Foreign Passenger") ) {
                    foreignLogin();
                }
                if ( userType.equals("Inspector") ) {
                    createAccount.setVisibility(createAccount.INVISIBLE);
                    tvSignUp.setVisibility(tvSignUp.INVISIBLE);
                    inspectorLogin1();
                }
            }
        });

    }

    public void openCreateAcount() {
        Intent i = new Intent(this, SelectUserActivity.class);
        startActivity(i);
    }

    public void openlogin() {

        final String userEnteredUsername = username.getText().toString().trim();
        final String userEnteredPassword = password.getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("LocalPassnger");

        Query checkUser = reference.orderByChild("id").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);


                    if (passwordFromDB.equals(userEnteredPassword)) {
                        Toast.makeText(getApplicationContext(), "valid user", Toast.LENGTH_SHORT).show();
                        loggedUser = username.getText().toString();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("user",username.getText().toString());
                        startActivity(intent);
                        finish();


                    } else {
                        password.setError("Invalid Password");
//                        Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }else{
                    password.setError("Invalid Username");
//                    Toast.makeText(getApplicationContext(), "No such User exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        userType = parent.getItemAtPosition(position).toString();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void foreignLogin() {

        final String userEnteredUsername = username.getText().toString().trim();
        final String userEnteredPassword = password.getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ForeignPassanger");

        Query checkUser = reference.orderByChild("id").equalTo(userEnteredUsername);
        Log.e("","" + checkUser);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)) {
                        Toast.makeText(getApplicationContext(), "valid user", Toast.LENGTH_SHORT).show();
                        loggedUser = username.getText().toString();
                        Intent intent = new Intent(LoginActivity.this, ForeignerWelcomeActivity.class);
                        intent.putExtra("user",username.getText().toString());
                        startActivity(intent);
                        finish();


                    } else {
                        password.setError("Invalid Password");
//                        Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }else{
                    password.setError("Invalid password");
//                    Toast.makeText(getApplicationContext(), "No such User exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void inspectorLogin1() {

        final String userEnteredUsername = username.getText().toString().trim();
        final String userEnteredPassword = password.getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("TravelInspector");

        Query checkUser = reference.orderByChild("id").equalTo(userEnteredUsername);
        Log.e("","" + checkUser);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)) {
                        Toast.makeText(getApplicationContext(), "valid user", Toast.LENGTH_SHORT).show();
                        loggedUser = username.getText().toString();
                        Intent intent = new Intent(LoginActivity.this, ReportPassenger.class);
                        intent.putExtra("user",username.getText().toString());
                        startActivity(intent);
                        finish();


                    } else {
                        password.setError("Invalid Password");
//                        Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }else{
                    username.setError("Invalid Username");
//                    Toast.makeText(getApplicationContext(), "No such User exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}