package com.example.vkinfo.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkUtils {
    //Константы для добавления их в адресную строку

    private static final String VK_API_BASE_URL = "https://api.vk.com/";
    private static final String VK_USERS_GET = "method/users.get";
    private static final String VK_PARAM_USER_ID = "user_ids";
    private static final String VK_PARAM_VERSION = "v";
    private static final String VK_PARAM_ACCESS_TOKEN = "access_token";


    public static URL generateURL(String userIDs) {
        Uri builtUri = Uri.parse(VK_API_BASE_URL + VK_USERS_GET)
                .buildUpon()
                .appendQueryParameter(VK_PARAM_USER_ID, userIDs)//назначаем id пользователя вк
                .appendQueryParameter(VK_PARAM_VERSION, "5.52")// назначаем версию 5.52
                .appendQueryParameter(VK_PARAM_ACCESS_TOKEN, "d372272dd372272dd372272d37d30aa54fdd372d372272db397b62cb66aac152ea42ed8")
                //используем токен приложения (иначе нам api ничего не вернет)
                .build();

        URL url = null;// делаем изначальную ссылку пустой

        //переводим получившуюся ссылку в String и возвращаем результат
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromURL(URL url) throws IOException {
        //создаем соединение
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            //Манипулируем со сканнером
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");//для того, чтобы сразу считать все

            boolean hasInput = scanner.hasNext();

            if (hasInput) { //если есть, что считывать возвращаем строку с ответом,иначе возвращаем пустоту
                return scanner.next();
            } else {
                return null;
            }
        } catch (UnknownHostException exception) {
            return null;
        } finally {//в любом из исходов разрываем соединение
            urlConnection.disconnect();
        }


    }
}
