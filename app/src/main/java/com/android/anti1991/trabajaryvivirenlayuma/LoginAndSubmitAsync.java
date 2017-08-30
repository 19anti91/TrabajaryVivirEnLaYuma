package com.android.anti1991.trabajaryvivirenlayuma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anti1991 on 8/16/2017.
 */

public class LoginAndSubmitAsync extends AsyncTask <HashMap<String,String>, Void,String> {
    private Context context;
    private Activity act;
    public ProgressDialog dialog;
    public LoginAndSubmitAsync(Context context, Activity act){
        this.context = context;
        this.act = act;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        this.dialog.setMessage("Por favor espere");
        this.dialog.show();

    }

    protected String doInBackground(HashMap<String,String>... map){


        URL url;
        OutputStream outputPost;
        BufferedReader in;
        HttpURLConnection client;
        MessageDigest md;
        String response = "";
        String link;
        try {
            // thread to sleep for 1000 milliseconds
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {


            String apiKey="0fba332183817c6445459326f0a057b4857f7a29a4331fafda56d2fefaea55c1";
            String data="";

            if(map[0].get("action").equals("RegRegister") || map[0].get("action").equals("FBRegister") || map[0].get("action").equals("GRegister")){
                link = "https://www.passtrunk.com/trabapi/user/user";
             data+= URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(map[0].get("fName"), "UTF-8");
            data += "&" + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(map[0].get("lName"), "UTF-8");
            data += "&" + URLEncoder.encode("emailaddress", "UTF-8") + "=" + URLEncoder.encode(map[0].get("email"), "UTF-8");
            data += "&" + URLEncoder.encode("apikey", "UTF-8") + "=" + URLEncoder.encode(apiKey, "UTF-8");
            if(map[0].get("action").equals("RegRegister")) {
                md = MessageDigest.getInstance("SHA-256");
                md.update(map[0].get("password").getBytes());
                byte byteData[] = md.digest();
                StringBuilder encPass = new StringBuilder();
                for (int i = 0; i < byteData.length; i++) {
                    encPass.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(encPass.toString(), "UTF-8");
                data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");

            }else if(map[0].get("action").equals("FBRegister")){
                data += "&" + URLEncoder.encode("fbid", "UTF-8") + "=" + URLEncoder.encode(map[0].get("fbid"), "UTF-8");
                data += "&" + URLEncoder.encode("profpicturl", "UTF-8") + "=" + URLEncoder.encode(map[0].get("profpicturl"), "UTF-8");
            }else if(map[0].get("action").equals("GRegister")){
                data += "&" + URLEncoder.encode("gid", "UTF-8") + "=" + URLEncoder.encode(map[0].get("gid"), "UTF-8");
                data += "&" + URLEncoder.encode("profpicturl", "UTF-8") + "=" + URLEncoder.encode(map[0].get("profpicturl"), "UTF-8");
                data += "&" + URLEncoder.encode("gtoken", "UTF-8") + "=" + URLEncoder.encode(map[0].get("gtoken"), "UTF-8");
            }
            }else{
                link = "https://www.passtrunk.com/trabapi/user/auth";
                data += "&" + URLEncoder.encode("apikey", "UTF-8") + "=" + URLEncoder.encode(apiKey, "UTF-8");
                if(map[0].get("action").equals("GSignIn")){
                    data += "&" + URLEncoder.encode("signinmethod", "UTF-8") + "=" + URLEncoder.encode("google", "UTF-8");
                    data += "&" + URLEncoder.encode("emailaddress", "UTF-8") + "=" + URLEncoder.encode(map[0].get("email"), "UTF-8");
                    data += "&" + URLEncoder.encode("gid", "UTF-8") + "=" + URLEncoder.encode(map[0].get("gtoken"), "UTF-8");
                }else if(map[0].get("action").equals("FBSignIn")){
                    data += "&" + URLEncoder.encode("signinmethod", "UTF-8") + "=" + URLEncoder.encode("facebook", "UTF-8");
                    data += "&" + URLEncoder.encode("emailaddress", "UTF-8") + "=" + URLEncoder.encode(map[0].get("email"), "UTF-8");
                    data += "&" + URLEncoder.encode("fbid", "UTF-8") + "=" + URLEncoder.encode(map[0].get("fbid"), "UTF-8");

                }else if(map[0].get("action").equals("RegSignIn")){
                    md = MessageDigest.getInstance("SHA-256");
                    md.update(map[0].get("password").getBytes());
                    byte byteData[] = md.digest();
                    StringBuilder encPass = new StringBuilder();
                    for (int i = 0; i < byteData.length; i++) {
                        encPass.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                    }

                    data += "&" + URLEncoder.encode("signinmethod", "UTF-8") + "=" + URLEncoder.encode("regular", "UTF-8");
                    data += "&" + URLEncoder.encode("emailaddress", "UTF-8") + "=" + URLEncoder.encode(map[0].get("email"), "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(encPass.toString(), "UTF-8");
                }

            }


            url = new URL(link);

            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            client.setRequestProperty("Accept-Charset","UTF-8" );
            client.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            outputPost = new BufferedOutputStream(client.getOutputStream());
            outputPost.write(data.getBytes());
            outputPost.flush();
            Log.d("CODE", String.valueOf(client.getResponseCode()));
            if(client.getResponseCode() >= 400){
                in = new BufferedReader(new InputStreamReader(client.getErrorStream()));
            }else {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;

            //Read response
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            response = sb.toString();
            Log.d("Result", response);
            in.close();
            outputPost.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


        protected void onPostExecute(String response) {
            JSONObject data;

//send to main activity
            try{
                data = new JSONObject(response);
                if(data.getInt("error") == 1){
                    Toast.makeText(context,data.getString("message"),Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,data.getString("message"),Toast.LENGTH_LONG).show();
                }

            }
            catch(Exception e) {
                e.printStackTrace();
            }

            if(dialog.isShowing())
                dialog.dismiss();


    }
}
