package reminderoli.reminder_oli;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    @BindView(R.id.edtUsername)
    EditText mUserName;
    @BindView(R.id.edtPassword)
    EditText mPassword;
    @BindView(R.id.button_login)
    Button mBtnLogin;
    @BindView(R.id.button_signup)
    Button mBtnSingUp;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    Mobil mobil;
    String userName;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.GONE);

        mBtnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MD5 md5 = new MD5();
                userName = mUserName.getText().toString();
                password = md5.EncriptMD5(mPassword.getText().toString());
                Log.d(TAG, "onClick: " + userName);
                Log.d(TAG, "onClick: " + password);
                boolean kosong = true;
                if (TextUtils.isEmpty(userName)) {

                    kosong = false;
                    mUserName.setError("Tidak Boleh Kosong");

                }
                if (TextUtils.isEmpty(password)) {
                    kosong = false;
                    mPassword.setError("Tidak Boleh Kosong");
                }
                if (kosong) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    cekUser();
                }
            }
        });
    }

    private void cekUser() {
        String URL = "http://reminder.96.lt/getUser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    final JSONArray jsonArray = object.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object1 = jsonArray.getJSONObject(i);
                        User userUrl = new User(object1);


                        Log.d(TAG, "onResponse: Url " + userUrl.getUserName());
                        Log.d(TAG, "onResponse: " + userName);

                        Log.d(TAG, "onResponse: Url" + userUrl.getPassword());
                        Log.d(TAG, "onResponse: " + password);


                        if (userUrl.getUserName().equalsIgnoreCase(userName) && userUrl.getPassword().equals(password)) {
                            mobil = new Mobil();
                            mobil.setId_user(userUrl.IdUser);
                            Log.d(TAG, "onResponse: user id "+userUrl.getIdUser());
                            updateFCMToken(userUrl);
                        }

                    }
                    if (mobil == null) {
                        Toast.makeText(LoginActivity.this, "Email/Password Salah", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }

        });
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);

    }

    public void updateFCMToken(final User userUrl) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        String TAG = "FirebaseInstance";

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();
                        String url = "http://reminder.96.lt/updateUserNotificationToken.php?token="+ token +"&id_user=" + userUrl.getIdUser();
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                mProgressBar.setVisibility(View.GONE);

                                if (userUrl.status.equals("admin")) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra(DetailUserActivity.getData, mobil);
                                    startActivity(intent);
                                } else {
                                    mobil.setStatus("user");
                                    Intent intent = new Intent(LoginActivity.this, DetailUserActivity.class);
                                    intent.putExtra(DetailUserActivity.getData, mobil);
                                    startActivity(intent);
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                        requestQueue.add(stringRequest);
                    }
                });
    }
}
