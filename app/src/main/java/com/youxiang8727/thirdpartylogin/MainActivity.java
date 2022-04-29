package com.youxiang8727.thirdpartylogin;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.GsonBuilder;
import com.youxiang8727.thirdpartylogin.Utils.CustomAlertDialogUtils;

public class MainActivity extends AppCompatActivity {
    private SignInButton button_sign_in;
    private CustomAlertDialogUtils customAlertDialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        initUtils();

        button_sign_in.setOnClickListener(v -> signIn());

    }

    private void initComponent() {
        button_sign_in = findViewById(R.id.button_sign_in);
    }

    private void initUtils() {
        customAlertDialogUtils = new CustomAlertDialogUtils(this);
    }

    private void signIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken("654403956437-stupf9pas9fdo623qp0p17ga7n80h1u1.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        googleSignInResultLauncher.launch(googleSignInClient.getSignInIntent());

    }

    private final ActivityResultLauncher<Intent> googleSignInResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount googleSignInAccount = googleSignInAccountTask.getResult(ApiException.class);
                            customAlertDialogUtils.showAlert("登入成功",
                                    googleSignInAccount.getAccount().name,
                                    "確定",
                                    (dialog, which) -> firebaseAuthWithGoogle(googleSignInAccount.getIdToken()),
                                    false);
                        } catch (ApiException e) {
                            customAlertDialogUtils.showAlert("登入失敗",
                                    e.toString(),
                                    "確定",
                                    null,
                                    false);
                        }
                    } else {
                        customAlertDialogUtils.showAlert("登入失敗",
                                "resultCode = " + result.getResultCode(),
                                "確定",
                                null,
                                false);
                    }
                }
            });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        System.out.println("user: " + new GsonBuilder().setPrettyPrinting().create().toJson(user));
    }
}