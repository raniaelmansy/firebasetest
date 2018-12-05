package com.example.rania.test.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.rania.test.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        new Handler().postDelayed(new Runnable()
        {

            public void run()
            {


                if (auth.getCurrentUser() != null) {
                    // already signed in
                    // Create an Intent that will start the Popular Artists activity.
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();

                } else {
                    // not signed in
                    // Create an Intent that will start the Popular Artists activity.
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.PhoneBuilder().build()
                                    ))
                                    .setTheme(R.style.FirebaseCustomTheme)
                                    .setLogo(R.mipmap.ic_launcher)
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        }, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successful sign in
                String phoneNumber = response.getPhoneNumber();
                Log.d("splash", "Phone number: " + phoneNumber);
                showSnackBar("Phone number: " + phoneNumber);

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackBar("Sing in canceled");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar("Your device is not connected");
                    return;
                }

                showSnackBar("Something went wrong, please try again later!! ");
                Log.e("splash", "Sign-in error: ", response.getError());
            }
//            updateUI();
        }
    }

    private void showSnackBar(String messageString){

        Snackbar.make(findViewById(R.id.containerLayout), messageString, Snackbar.LENGTH_LONG).show();
    }
}
