package radhakrishnan.familytracker.UserInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import radhakrishnan.familytracker.R;
import radhakrishnan.familytracker.base.BaseActivity;
import radhakrishnan.familytracker.traking.TrackingActivity;
import radhakrishnan.familytracker.utill.PrefrenceHandler;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoginView, GoogleApiClient.OnConnectionFailedListener, OnClickListener {

    private static final int RC_SIGN_IN = 412;
    private static final String TAG = "LoginActivity";
    // UI references.
    LoginPresenter presenter;
    GoogleApiClient mGoogleApiClient;
    SignInButton mEmailSignInButton;
    EditText EdName;
    Button btDone;
    private View mProgressView;

    public static Intent getLoginIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        presenter = new LoginPresenter(this);
        // Set up the login form.
        EdName = (EditText) findViewById(R.id.ed_name);
        btDone = (Button) findViewById(R.id.LA_BT_done);
        btDone.setOnClickListener(this);

        if (!presenter.checkUserLoggedIn()) {
            mEmailSignInButton = (SignInButton) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(this);

            mProgressView = findViewById(R.id.login_progress);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        } else {
            logInSuccess();
        }

    }

    private void signIn() {
        mProgressView.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mEmailSignInButton.setVisibility(View.VISIBLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        mProgressView.setVisibility(View.GONE);

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                presenter.setEmailAddress(acct.getEmail());
                presenter.checkUserAlreadyPresentInOurDb(acct.getEmail());
            }

        } else {
            Log.d(TAG, result.getStatus() + "");
            mEmailSignInButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Sorry we have issues please contact admin", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void setVisibilityName(boolean visibility) {
        EdName.setVisibility((visibility) ? View.VISIBLE : View.GONE);
        btDone.setVisibility((visibility) ? View.VISIBLE : View.GONE);
        mEmailSignInButton.setVisibility((!visibility) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initSharedPred() {
        PrefrenceHandler.init(getBaseContext());
    }

    @Override
    public String getName() {
        return EdName.getText().toString();
    }

    @Override
    public void setName(String s) {
        EdName.setText(s);
    }

    @Override
    public void logInSuccess() {
        startActivity(TrackingActivity.getIntent(this));
        finish();
    }

    @Override
    public void setErrorName(int nameShouldNotBeEmpty) {
        EdName.setError(getStringFromInt(nameShouldNotBeEmpty));
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == mEmailSignInButton.getId()) {
            mEmailSignInButton.setVisibility(View.GONE);
            signIn();
        } else if (v.getId() == btDone.getId()) {
            presenter.checkLoginProcess();
        }

    }

}

