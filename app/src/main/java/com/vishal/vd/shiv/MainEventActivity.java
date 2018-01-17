package com.vishal.vd.shiv;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.vishal.vd.shiv.helper.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainEventActivity extends AppCompatActivity {

    String BASE_URL = "http://datasyninfo.000webhostapp.com/shiv";

    EditText edt_name, edt_age, edt_mobile, edt_mail;
    Button btn_insert, btn_display;
    Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edt_name = (EditText) findViewById(R.id.name);
        edt_age = (EditText) findViewById(R.id.age);
        edt_mobile = (EditText) findViewById(R.id.mobile);
        edt_mail = (EditText) findViewById(R.id.mail);
        btn_insert = (Button) findViewById(R.id.insert);
        btn_display = (Button) findViewById(R.id.retrieve);

       final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edt_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert_data();
            }
        });

        btn_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DisplayData.class);
                startActivity(i);
            }
        });

    }

    public void insert_data() {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL) //Setting the Root URL
                .build();

        AppConfig.insert api = adapter.create(AppConfig.insert.class);

        api.insertData(
                edt_name.getText().toString(),
                edt_age.getText().toString(),
                edt_mobile.getText().toString(),
                edt_mail.getText().toString(),
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {

                        try {

                            BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            String resp;
                            resp = reader.readLine();
                            Log.d("success", "" + resp);

                            JSONObject jObj = new JSONObject(resp);
                            int success = jObj.getInt("success");

                            if(success == 1){
                                Toast.makeText(getApplicationContext(), "Successfully inserted", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            Log.d("Exception", e.toString());
                        } catch (JSONException e) {
                            Log.d("JsonException", e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(MainEventActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edt_mobile.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
