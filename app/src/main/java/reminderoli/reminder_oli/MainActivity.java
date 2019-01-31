package reminderoli.reminder_oli;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rv_list_user)
    RecyclerView rvCatagory;

    MobilAdapter adapter;

    ArrayList<Mobil> listMobil;
    ArrayList<User> listUser;

    String getMobilURL = "http://reminder.96.lt/getMobil.php";
    String getUserURL = "http://reminder.96.lt/getUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        listMobil = new ArrayList<>();
        adapter = new MobilAdapter(this);
        adapter.setList(listMobil);

        listMobil = new ArrayList<>();
        listUser = new ArrayList<>();

        rvCatagory.setLayoutManager(new LinearLayoutManager(this));
        rvCatagory.setAdapter(adapter);
        ItemClickSupport.addTo(rvCatagory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                Mobil mobil = listMobil.get(position);
                mobil.setStatus("admin");

                Intent intent = new Intent(MainActivity.this, DetailUserActivity.class);
                intent.putExtra(DetailUserActivity.getData, mobil);
                startActivity(intent);
            }
        });

        loadUserData();
    }
    void loadMobilData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getMobilURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Mobil> mobilData = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Mobil mobil = new Mobil(obj);
                        mobilData.add(mobil);
                    }

                    listMobil.clear();

                    for(int i = 0; i < listUser.size(); i++) {
                        Mobil mobil = new Mobil();

                        mobil.setId_user(listUser.get(i).getIdUser());
                        mobil.setNamaUser(listUser.get(i).userName);
                        mobil.setIsInsert(1);

                        for(int j=0; j < mobilData.size(); j++){
                            if (listUser.get(i).IdUser.equals(mobilData.get(j).id_user)) {
                                mobil.setIsInsert(0);
                                mobil.noPol = mobilData.get(j).noPol;
                                mobil.kmSekarang = mobilData.get(j).kmSekarang;
                                mobil.kmService = mobilData.get(j).kmService;
                                mobil.namaMobil = mobilData.get(j).namaMobil;
                                mobil.jenisMobil = mobilData.get(j).jenisMobil;
                            }
                        }
                        listMobil.add(mobil);
                    }

                    adapter.setList(listMobil);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    void loadUserData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getUserURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<User> userData = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        User user = new User(obj);
                        userData.add(user);
                    }

                    listUser.clear();
                    listUser.addAll(userData);

                    loadMobilData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
