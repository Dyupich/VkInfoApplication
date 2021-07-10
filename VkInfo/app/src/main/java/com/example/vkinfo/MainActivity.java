package com.example.vkinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.vkinfo.utils.NetworkUtils.generateURL;
import static com.example.vkinfo.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {
    private EditText searchField;
    private Button searchButton;
    private TextView result;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;

    //Функции для смены отображения результата и сообщения об ошибке
    private void showResultTextView() {
        result.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorTextView() {
        result.setVisibility(View.INVISIBLE);
        result.setVisibility(View.VISIBLE);
    }

    //Класс для реализации второго потока, отвечающего за соединение с интернетом
    //И получения данных от api вконтакте
    private class VKQueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected void onPreExecute() {
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        // Получаем данные введенные пользователем
        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        //После завершения
        @Override
        protected void onPostExecute(String response) {
            String firstName = null;
            String lastName = null;
            String id = null;
            String address = null;
            if (response != null && !response.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    String resultingString = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject userInfo = jsonArray.getJSONObject(i);

                        firstName = userInfo.getString("first_name");
                        lastName = userInfo.getString("last_name");

                        resultingString += "Имя : " + firstName + "\n" + "Фамилия: " + lastName
                                + "\n\n";


                    }
                    result.setText(resultingString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showResultTextView();
            } else {
                showErrorTextView();
            }

            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// Делаем лэйаут activity_main.xml


        //Далее настраиваем наши объекты под их id
        searchField = findViewById(R.id.et_search_field);
        searchButton = findViewById(R.id.b_vk_search);
        result = findViewById(R.id.tv_result);
        errorMessage = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        //Если кнопка нажата, создаем экземпляр VKQueryTask и выполняем его
        //С веденными значениями
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL generatedURL = generateURL(searchField.getText().toString());
                new VKQueryTask().execute(generatedURL);
            }
        });
    }
}