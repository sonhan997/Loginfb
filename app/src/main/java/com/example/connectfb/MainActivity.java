package com.example.connectfb;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final Pattern EMAIL_ADDRESS =
            Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
                    +"\\@"+
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+
                    "("+
                    "\\."+
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+")+");
    private static final Pattern USERNAME = Pattern.compile("[a-zA-Z ]+");
   private  static  final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
           //"(?=.*[0-9])" +         //at least 1 digit
           //"(?=.*[a-z])" +         //at least 1 lower case letter
           //"(?=.*[A-Z])" +         //at least 1 upper case letter
           "(?=.*[a-zA-Z])" +      //any letter
           "(?=.*[@#$%^&+=])" +    //at least 1 special character
           "(?=\\S+$)" +           //no white spaces
           ".{4,}" +               //at least 4 characters
           "$");
    private TextView textViewDK;
    private Button btnDangKi,btnDangNhap;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebase;
    private String userId;
    private TextInputLayout textInputEmail,textInputNAME,textInputPassword, textInputConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

         mAuth = FirebaseAuth.getInstance();
         Anhxa();




        btnDangKi.setOnClickListener(this);
//        btnDangNhap.setOnClickListener(this);
       // textViewDK.setOnClickListener(this);
    }

    private void DangkiRealtime(String username,String email,String password,String cfpassword){

       /* String username = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPass.getText().toString().trim();
        String cfpassword = editTextConfirm.getText().toString().trim();*/
        User newUser = new User(username,email,password,cfpassword);

        mDatabase.child("UserID").push().setValue(newUser);
    }


    private void Anhxa(){
        //editTextName = (EditText)findViewById(R.id.editTextName);
        btnDangKi = (Button)findViewById(R.id.btnDangki);
        textInputNAME = findViewById(R.id.textInput_NAME);
        textInputEmail = findViewById(R.id.textInput_Email);
        textInputPassword=findViewById(R.id.textInput_Password);
        textInputConfirmPassword=findViewById(R.id.textInputConfirm_Password);


    }
    private  void Dangki(){
        String email = textInputEmail.getEditText().getText().toString().trim();
        String password = textInputPassword.getEditText().getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter Pass", Toast.LENGTH_SHORT).show();
            return;

        }
        progressDialog.setMessage("Register User...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "SUCCESSFUL", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid email address");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }
    private boolean validateUsername() {
        String usernameInput = textInputNAME.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputNAME.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 30) {
            textInputNAME.setError("Username too long");
            return false;
        } else if(!USERNAME.matcher(usernameInput).matches()){
            textInputNAME.setError("Please enter a valid name ");
            return false;
        }
        else {
            textInputNAME.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        String confirmPassword = textInputConfirmPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password too weak");
            return false;
        }

        else {
            textInputPassword.setError(null);
            return true;
        }
    }
    private boolean validateConfirmPassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        String confirmPassword = textInputConfirmPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputConfirmPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputConfirmPassword.setError("Password too weak");
            return false;
        }
        else if(!confirmPassword.equals(passwordInput)){
            textInputConfirmPassword.setError("Password not matching");
            return  false;
        }
        else {
            textInputConfirmPassword.setError(null);
            return true;
        }
    }



    @Override
    public void onClick(View v) {
        String username = textInputNAME.getEditText().getText().toString().trim();
        String email = textInputEmail.getEditText().getText().toString().trim();
        String password = textInputPassword.getEditText().getText().toString().trim();
        String cfpassword = textInputConfirmPassword.getEditText().getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if(v==btnDangKi){
            if(!validateEmail()| !validateUsername() | !validatePassword() | !validateConfirmPassword()){
                Dangki();
                DangkiRealtime(username,email,password,cfpassword);
                return;

            }
            String input = "FullName: " + textInputNAME.getEditText().getText().toString();
            input += "\n";
            input += "Email " + textInputEmail.getEditText().getText().toString();
            input += "\n";
            input += "Password: " + textInputPassword.getEditText().getText().toString();
            input += "\n";
            input +="Confirm Password: " + textInputConfirmPassword.getEditText().getText().toString();
            Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
        }

        if(v == textViewDK){

        }

    }
}
