package reminderoli.reminder_oli;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    @BindView(R.id.nama)
    EditText mNama;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.Password)
    EditText mPass;
    @BindView(R.id.button_signup)
    Button btnLogin;
    @BindView(R.id.loading)
    ProgressBar loading;
    ArrayList<User> listUser;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        loading.setVisibility(View.GONE);
        listUser = new ArrayList<>();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                String nama = mNama.getText().toString().trim();
                String pass = mPass.getText().toString().trim();
                email = mEmail.getText().toString().trim();
                String idUser = "";

                if (TextUtils.isEmpty(nama)) {
                    mNama.setError("Tidak Boleh Kosong");
                }
                if (TextUtils.isEmpty(pass)) {
                    mPass.setError("Tidak Boleh Kosong");
                }
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Tidak Boleh Kosong");
                } else {
                    MD5 md5 = new MD5();

                    signUp(nama, md5.EncriptMD5(pass), email);


                }


            }
        });


    }

    private void signUp(String nama, String pass, String email) {

        String URL = "http://reminder.96.lt/setUser.php?user=";
        URL += nama;
        URL += "&pass=" + pass;
        URL += "&email=" + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                Log.d("TAg", response);
                if (response.equals("Anda Sudah Terdaftar")) {
                    Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();
                    Toast.makeText(SignupActivity.this, "Silahkan Login menggunakan akun Anda", Toast.LENGTH_LONG).show();
                    finis();
                }
                getUser();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void setmobili(String nama,String id) {

        String URL = "http://reminder.96.lt/setMobilIdUser.php?user=";
        URL += nama;
        URL += "&oli="+id;
        Log.d("TAG", "setmobili: "+URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                Log.d("TAg", response);
                if (response.equals("Sukses")) {
                    Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();
                    Toast.makeText(SignupActivity.this, "Silahkan Login menggunakan akun Anda", Toast.LENGTH_LONG).show();
                    finis();
                }
                return;


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getUser() {

        String URL = "http://reminder.96.lt/getUser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("data");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        User user = new User(obj);
                        listUser.add(user);
                        Log.d("ss", "onResponse: " + user.getEmail());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String idUser = "";
                for (int i = 0; i < listUser.size(); i++) {
                    User user = new User();
                    user.setEmail(listUser.get(i).email);
                    user.setIdUser(listUser.get(i).IdUser);

                    if (user.getEmail().equalsIgnoreCase(email)) {
                        idUser = user.getIdUser();
                        Log.d("TAG", "onResponse: "+idUser);
                        setmobili(idUser,"");
                    }
                }
                return;
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    void finis() {
        this.onBackPressed();
        this.finish();
    }
}
