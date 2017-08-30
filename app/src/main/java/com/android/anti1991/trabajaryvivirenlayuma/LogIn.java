package com.android.anti1991.trabajaryvivirenlayuma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;


public class LogIn extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final int FB_SIGN_IN = 9010;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private static final String TAG = "SignInActivity";
    CallbackManager callbackManager;
    private View gView;
    private View fView;
    private Activity act = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_log_in);
        ActionBar actionBar = getSupportActionBar();
        gView = (View) findViewById(R.id.sign_in_button);
        fView = (View) findViewById(R.id.login_button);
      //  actionBar.setTitle("Entrar");
        actionBar.hide();
        LoginManager.getInstance().logOut();


        Button reg = (Button)findViewById(R.id.btn_register_LogIn);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToRegister = new Intent(view.getContext(),Register.class);
                view.getContext().startActivity(goToRegister);
            }
        });

        //------------Start of Google Sign in--------------//
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // .requestEmail()
                .requestIdToken("742105067605-ld4cj85dl9gg4r9lcmbl8k0jnl18d8ss.apps.googleusercontent.com")
                .requestEmail()
                .build();
         mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.login_button).setOnClickListener(this);

        //----------End of Google Sign In--------------------//

        //Start of Facebook Sign In---------------------//
        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        LoginButton loginButton = (LoginButton) this.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("FACEBOOK", "Success\n"+ loginResult);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response){
                        HashMap<String, String> data = new HashMap<>();
                        try {

                            data.put("action", "FBSignIn");
                            data.put("email", response.getJSONObject().getString("email"));
                            data.put("fbid", response.getJSONObject().getString("id"));

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        LoginAndSubmitAsync register = new LoginAndSubmitAsync(fView.getContext(),act);
                        register.execute(data);
                        Log.d("RESPONSE FB",response.toString());
                    }
                });
                Bundle param = new Bundle();
                param.putString("fields","id,first_name,last_name,email,picture");
                request.setParameters(param);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("FACEBOOK", "cancel\n");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("FACEBOOK", "Error\n" + error.getMessage());
            }
        });
//--------------------End of Facebook Sign in----------------------//
        //logInFb();

    }
    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();

    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.sign_in_button:
                signInG();
                break;
            case R.id.sign_out_button:
                signOutG();
                break;
           /* case R.id.login_button:
                logInFb();
                break;*/

            // ...
        }
    }


    //-------------Google Sign In helper functions-------------------//
    private void signInG() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOutG() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        Log.d("Signed out", "singed out");
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            HashMap<String,String> data = new HashMap<>();
            data.put("action","GSignIn");
            data.put("email",acct.getEmail());
            data.put("gtoken",acct.getIdToken());

            Log.d("TOKENNN",acct.getIdToken());
            LoginAndSubmitAsync register = new LoginAndSubmitAsync(gView.getContext(),act);
            register.execute(data);
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }


    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            Log.d("yolo", "Logged in");
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }
//-------------------------End of Google Helper Functions--------------------//


    public void onBackPressed() {
        moveTaskToBack(true);
        System.exit(0);

    }

    public void regularLogin(View v){
        EditText test = (EditText) findViewById(R.id.txtb_email_LogIn);
test.getText();
        LoginAndSubmitAsync login = new LoginAndSubmitAsync(v.getContext(),this);

        HashMap<String,String> data = new HashMap();
        data.put("action","RegSignIn");
        data.put("email",((EditText) findViewById(R.id.txtb_email_LogIn)).getText().toString());
        data.put("password",((EditText) findViewById(R.id.txtb_password_LogIn)).getText().toString());
        //login.execute(data);
        Intent goToRegister = new Intent(v.getContext(),Home.class);
        v.getContext().startActivity(goToRegister);
    }

}
