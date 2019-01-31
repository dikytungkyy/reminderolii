package reminderoli.reminder_oli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminActivity extends AppCompatActivity {

    @BindView(R.id.admin_idoli)
    Spinner spinnerIdoli;

    @BindView(R.id.admin_noPol)
    EditText anoPol;

    @BindView(R.id.admin_kmawal)
    EditText akmAwal;

    @BindView(R.id.admin_kmservice)
    EditText akmService;

    @BindView(R.id.admin_namamobil)
    EditText anamaMobil;

    @BindView(R.id.admin_jenismobil)
    EditText ajenisMobil;

    @BindView(R.id.admin_input_selesai)
    Button aInput;

    @BindView(R.id.admin_loading)
    ProgressBar loading;

    Integer oliId;

    Mobil mobil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);

        mobil = new Mobil();
        mobil = getIntent().getParcelableExtra("mobil");

        Toast.makeText(this, mobil.getId_user(), Toast.LENGTH_SHORT).show();

        setupSpinner(mobil);
        loadMobilData(mobil);

        aInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                String noPol = "";
                String kmAwal = "";
                String kmService = "";
                String namaMobil = "";
                String jenisMobil = "";

                try {
                    noPol = URLEncoder.encode(anoPol.getText().toString().trim(), "UTF-8");
                    kmAwal = URLEncoder.encode(akmAwal.getText().toString().trim(), "UTF-8");
                    kmService = URLEncoder.encode(akmService.getText().toString().trim(), "UTF-8");
                    namaMobil = URLEncoder.encode(anamaMobil.getText().toString().trim(), "UTF-8");
                    jenisMobil = URLEncoder.encode(ajenisMobil.getText().toString().trim(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                if (TextUtils.isEmpty(noPol)) {
                    anoPol.setError("Tidak Boleh Kosong");
                    valid = false;
                }

                if (TextUtils.isEmpty(kmAwal)) {
                    akmAwal.setError("Tidak Boleh Kosong");
                    valid = false;
                }

                if (TextUtils.isEmpty(kmService)) {
                    akmService.setError("Tidak Boleh Kosong");
                    valid = false;
                }

                if (TextUtils.isEmpty(namaMobil)) {
                    anamaMobil.setError("Tidak Boleh Kosong");
                    valid = false;
                }

                if (TextUtils.isEmpty(jenisMobil)) {
                    ajenisMobil.setError("Tidak Boleh Kosong");
                    valid = false;
                }

                if (valid) {
                    editMobilDataRequest(mobil.getId_user(), oliId, mobil.isInsert, noPol, kmAwal, kmService, namaMobil, jenisMobil);
                }
            }
        });

    }

    private void setupSpinner(Mobil mobil) {
        ArrayList<String> oilList = new ArrayList<>();
        oilList.add("Castrol");
        oilList.add("Shell Hx-7");
        oilList.add("primaxp");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, oilList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdoli.setAdapter(adapter);
        if (mobil.getId_oli() != null) {
            try {
                spinnerIdoli.setSelection(Integer.parseInt(mobil.getId_oli()) - 1);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        spinnerIdoli.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                oliId = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading.setVisibility(View.GONE);
    }

    void editMobilDataRequest(String userId, Integer oliId, Integer isInsert, String noPol, String kmAwal, String kmService, String namaMobil, String jenisMobil) {
        String URL = "http://reminder.96.lt/setMobil.php?nopol=";
        URL += noPol;
        URL += "&kmawal=" + kmAwal;
        URL += "&kmservice=" + kmService;
        URL += "&namamobil=" + namaMobil;
        URL += "&jenismobil=" + jenisMobil;
        URL += "&user_id=" + userId;
        URL += "&oli_id=" + oliId;
        URL += "&is_insert=" + isInsert;

        Log.d("FOO", URL);

        loading.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminActivity.this, "Error", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(AdminActivity.this);
        requestQueue.add(stringRequest);
    }

    public void loadMobilData(Mobil mobil) {
        anoPol.setText(mobil.noPol);
        akmAwal.setText(mobil.kmSekarang);
        akmService.setText(mobil.kmService);
        anamaMobil.setText(mobil.namaMobil);
        ajenisMobil.setText(mobil.jenisMobil);
    }
}
