package com.example.trueclub_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText nameBox;
    TextView resultView;
    Button submitBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameBox=findViewById(R.id.name_Box);
        resultView=findViewById(R.id.result_TextView);
        submitBtn=findViewById(R.id.submit_Btn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameBox.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                fetchApiCall(name);
                nameBox.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    private void fetchApiCall(String name) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nationalize.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterFace apiInterFace = retrofit.create(ApiInterFace.class);

        Call<ApiResponse> call = apiInterFace.getResponse(name);

        call.enqueue(new Callback<ApiResponse>() {


            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    String result = "";
                    result+=apiResponse.getName() + "\n"+ "\n";
                    for (ApiResponse.Country country : apiResponse.getCountries()) {
                        result += "Country  :  " + country.getCountryId() + "\n"+ "\n"
                                +"Probablity  :  "+ country.getProbability() + "\n"+ "\n";
                    }
                    resultView.setText(result);
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
