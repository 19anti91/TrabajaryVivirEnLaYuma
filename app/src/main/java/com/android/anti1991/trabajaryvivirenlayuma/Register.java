package com.android.anti1991.trabajaryvivirenlayuma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final int FB_SIGN_IN = 9010;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private static final String TAG = "SignInActivity";
    CallbackManager callbackManager;
    private boolean passwordOK = false;
    private Activity act = this;
    private View gView;
    private View fView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();

       gView = (View) findViewById(R.id.sign_in_button);
        fView = (View) findViewById(R.id.login_button);
        //------------Handling regular register-----------------//
            Button register = (Button)findViewById(R.id.btn_register_Register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap <String,String> data = new HashMap<>();
                String fName = ((EditText)(findViewById(R.id.txtb_fname_Register))).getText().toString();
                String lName = ((EditText)(findViewById(R.id.txtb_lname_Register))).getText().toString();
                String email = ((EditText)(findViewById(R.id.txtb_emailaddress_Register))).getText().toString();

                if(!(((EditText)(findViewById(R.id.txtb_password_Register))).getText().toString().equals(((EditText)(findViewById(R.id.txtb_confirmpassword_Register))).getText().toString())) || fName.isEmpty() || lName.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
                    Toast.makeText(Register.super.getApplicationContext(),"Por favor confirme sus datos y asegurese de que los passwords coincidan",Toast.LENGTH_LONG).show();
                }else{
                    String password = ((EditText)(findViewById(R.id.txtb_confirmpassword_Register))).getText().toString();
                    data.put("action","RegRegister");
                    data.put("fName",fName);
                    data.put("lName",lName);
                    data.put("email",email);
                    data.put("password",password);
                    LoginAndSubmitAsync register = new LoginAndSubmitAsync(view.getContext(),act);
                    register.execute(data);


                }
            }
        });

//--------------End of Regular register----------//

        LoginManager.getInstance().logOut();
        //------------Start of Google Sign in--------------//
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
        //findViewById(R.id.btn_register_Register).setOnClickListener(this);
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

                        data.put("action", "FBRegister");
                        data.put("fName", response.getJSONObject().getString("first_name"));
                        data.put("lName", response.getJSONObject().getString("last_name"));
                        data.put("email", response.getJSONObject().getString("email"));
                        data.put("fbid", response.getJSONObject().getString("id"));
                        data.put("profpicturl", response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url"));


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
            data.put("action","GRegister");
            data.put("fName",acct.getGivenName());
            data.put("lName",acct.getFamilyName());
            data.put("email",acct.getEmail());
            data.put("gid",acct.getId());
            data.put("gtoken",acct.getIdToken());
            data.put("profpicturl",acct.getPhotoUrl().toString());
Log.d("TOKENNN",acct.getIdToken());
            LoginAndSubmitAsync register = new LoginAndSubmitAsync(gView.getContext(),act);
            register.execute(data);
            //updateUI(true);
        } else {

            Toast.makeText(this,"Lo sentimos, hubo un error con su cuenta de Google",Toast.LENGTH_LONG).show();
            updateUI(false);
        }
    }


    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            Log.d("yolo", "Logged in");

        } else {

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);

        }
    }
//-------------------------End of Google Helper Functions--------------------//




}
