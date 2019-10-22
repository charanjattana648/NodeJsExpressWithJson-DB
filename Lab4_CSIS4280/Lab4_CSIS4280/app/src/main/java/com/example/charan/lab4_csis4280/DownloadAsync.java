package com.example.charan.lab4_csis4280;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadAsync extends AsyncTask<String,Integer,String> {

    public OnEventListener<String> theCallBack;
    public DownloadAsync(OnEventListener theCallBack){
        this.theCallBack=theCallBack;
    }
    public String typeCall="data";

    @Override
    protected String doInBackground(String... data) {
        String site=data[0];
        String params=data[1];
        if(data.length==3)
        {
            typeCall=data[2];
        }
        Log.d("lllll...", "doInBackground: "+data.length);
        String result=getRemoteData(site,params);

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("onPostExecute", "onPostExecute: "+s);
        if(typeCall.equalsIgnoreCase("countryLanguages"))
        {
            theCallBack.onSuccessCountryLanguage(s);
        }else if(typeCall.equalsIgnoreCase("countryData"))
        {
            theCallBack.onSuccessCountry(s);
        }else{
            theCallBack.onSuccess(s);
        }

    }

    private String getRemoteData(String site, String params) {

        HttpURLConnection c=null;
        String dataFromServer="";
        try {
            URL url=new URL(site);
            c= (HttpURLConnection) url.openConnection();
            c.setRequestMethod("POST");
            c.setDoInput(true);
            c.setDoOutput(true);

            Log.d("testing..", "site:"+site+"---params:--"+params);
            OutputStreamWriter writer=new OutputStreamWriter(c.getOutputStream());
            writer.write(params);
            writer.flush();
            writer.close();
            Log.d(this.getClass().getName(), "Connecting... ");
            c.connect();
            int status=c.getResponseCode();
            switch (status)
            {
                case 200:
                case 201:
                    BufferedReader br=new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb=new StringBuilder();
                    String line="";
                    while ((line=br.readLine())!=null)
                    {
                        sb.append(line+"\n");
                    }
                    br.close();
                    dataFromServer=sb.toString();
                    break;
                case 404:
                    return "Can't find";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(c!=null)
            {
                try {
                    c.disconnect();
                }catch (Exception e)
                {

                }
            }
        }
        return dataFromServer;
    }
}
