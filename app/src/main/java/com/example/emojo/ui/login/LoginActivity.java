package com.example.emojo.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emojo.R;
import com.example.emojo.data.model.User;
import com.example.emojo.ui.login.LoginViewModel;
import com.example.emojo.ui.login.LoginViewModelFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Fragment loginFragment;
    private LoginViewModel loginViewModel;
    private ActionCodeSettings actionCodeSettings;
    private String userEmail;
    private User userModelObject;
    SharedPreferences.Editor editor;


//    public static class LoginFragment extends Fragment {
//
//    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);


        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
//        final EditText passwordEditText = findViewById(R.id.password);


        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }


//                if (loginFormState.getUsernameError() != null) {
//                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
//                }
//                if (loginFormState.getPasswordError() != null) {
//                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
//                }
            }
        });

//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
//            }
//        });

        editor =  getSharedPreferences("LoginAct", MODE_PRIVATE).edit();
//        FragmentManager fm = getSupportFragmentManager();
//        fm.beginTransaction().add(new LoginFragment(), "frag");
//
//
//        Log.v("fragmgr", Boolean.toString(fm != null));
//        Log.v("frag", Boolean.toString(fm.findFragmentByTag("frag") != null));



        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
//                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
                loginButton.setEnabled(usernameEditText.getText().toString().replaceAll("\\s+","").length() > 0);

            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    loginViewModel.login(usernameEditText.getText().toString(),
//                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });

        this.actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://chrissims-madsu20.firebaseapp.com")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                "com.example.emojo",
                                true, null)
                        .build();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                userEmail = usernameEditText.getText().toString();

                editor.putString("userEmail", userEmail);
                editor.apply();

                FirebaseAuth auth = FirebaseAuth.getInstance();

                Log.v("!!!", "trying to send email");


                auth.sendSignInLinkToEmail(userEmail,
                        actionCodeSettings)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.v("useremail", userEmail);

                                    if (!task.isSuccessful()) {
                                        task.getException().printStackTrace();
                                    }


                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Email sent.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
            }
        });









    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putCharSequence("userEmail", this.userEmail);
        Log.v("SAVED", "SAVE CALLED!");

    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v("ORS", "ORS CALLED!");
//        this.userEmail = (String)savedInstanceState.getCharSequence("userEmail");
        Log.v("restored email", this.userEmail);


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("pause", "pause CALLED!");
        final EditText usernameEditText = findViewById(R.id.username);
        this.userEmail = usernameEditText.getText().toString();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("onstop", "onstop CALLED!");

    }


    @Override
    public void onResume() {
        super.onResume();

        Log.v("name", Boolean.toString(userEmail != null));
        Log.v("usrobj", Boolean.toString(userModelObject != null));

        if(userModelObject != null) {
            Log.v("email from User obj", userModelObject.getEmail());
        }
        final Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_logged_in);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();

        if (intent.getData() != null) {

            SharedPreferences prefs = getSharedPreferences("LoginAct", MODE_PRIVATE);
            userEmail = prefs.getString("userEmail", null);

            String emailLink = intent.getData().toString();
            Log.v("link: ", emailLink);
//            Log.v("userEmail: ", userEmail);



            if (auth.isSignInWithEmailLink(emailLink)) {
                Log.v("email268: ", this.userEmail);

//                if (savedInstanceState != null) {
//                    Log.v("!!!", "IHATETHIS!!!!!");
//                    this.userEmail = (String) savedInstanceState.getCharSequence("userEmail");


                    // The client SDK will parse the code from the link for you.
                    auth.signInWithEmailLink(userEmail, emailLink)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Successfully signed in!", Toast.LENGTH_SHORT).show();

                                        AuthResult result = task.getResult();
                                        // You can access the new user via result.getUser()
                                        // Additional user info profile *not* available via:
                                        // result.getAdditionalUserInfo().getProfile() == null
                                        // You can check if the user is new or existing:
                                        // result.getAdditionalUserInfo().isNewUser()
//                                        final Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_logged_in);
//
//                                        NavHostFragment.findNavController(frag)
//                                                .navigate(R.id.action_nav_host_fragment_to_fragment_logged_in);

                                        Intent i = new Intent(LoginActivity.this, LoggedInActivity.class);
                                        startActivity(i);

                                    } else {
                                        task.getException().printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Error logging in :( !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

            }
        }



        Log.v("resume", "resume CALLED!");
        Log.v("email: ", Boolean.toString(this.userEmail == null));




    }



//    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
