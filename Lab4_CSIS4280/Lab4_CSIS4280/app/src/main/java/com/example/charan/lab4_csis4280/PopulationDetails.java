package com.example.charan.lab4_csis4280;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PopulationDetails extends AppCompatActivity implements OnEventListener<String>{

    Intent intent,moreDetails_intent;
    String[] fields=new String[3];
    String[] fieldsValues=new String[3];
    String[] new_fields=new String[7];
    String[] new_fieldsValues=new String[7];
    int maxPopulation;
    RangeSeekBar rangeSeekBar;
    EditText txt_min,txt_max;
    Button btnDetails,btnMoreDetails;
    String site;
    TextView res_continent,res_code,res_GForm,res_country,res_region,res_languages;
    String[] countries;
    Spinner countryName_spinner;
    String result=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_population_details);
        rangeSeekBar=findViewById(R.id.rangeSeekBar);
        txt_min=findViewById(R.id.et_min);
        txt_max=findViewById(R.id.et_max);
        countryName_spinner=findViewById(R.id.spinner_country);
        btnDetails=findViewById(R.id.btn_detail);
        btnMoreDetails=findViewById(R.id.btn_Moredetails);
        res_country=findViewById(R.id.txt_country);
        res_code=findViewById(R.id.txt_code);
        res_region=findViewById(R.id.txt_Region);
        res_GForm=findViewById(R.id.txt_GForm);
        res_languages=findViewById(R.id.txt_languages);
        res_continent=findViewById(R.id.txt_continent);
        btnMoreDetails.setVisibility(View.INVISIBLE);




        intent=getIntent();
        if(intent!=null)
        {
            site=intent.getStringExtra("site");
            fields=intent.getStringArrayExtra("fields");
            fieldsValues=intent.getStringArrayExtra("fieldsValues");
            maxPopulation=intent.getIntExtra("max",0);
            countries=intent.getStringArrayExtra("countriesName");
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,countries);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countryName_spinner.setAdapter(adapter);
            rangeSeekBar.setSelectedMaxValue(maxPopulation);
            rangeSeekBar.setSelectedMinValue(0);
            rangeSeekBar.setRangeValues(0,maxPopulation,maxPopulation/100);
            txt_min.setText(rangeSeekBar.getSelectedMinValue().toString());
            txt_max.setText(rangeSeekBar.getSelectedMaxValue().toString());
        }
        for(int i=0;i<fields.length;i++)
        {
            new_fields[i]=fields[i];
            new_fieldsValues[i]=fieldsValues[i];
        }
        new_fields[3]="min";
        new_fields[4]="max";
        new_fields[5]="country";
        new_fields[6]="code";

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                Number min_value=bar.getSelectedMinValue();
                Number max_value=bar.getSelectedMaxValue();
                int min= (int) min_value;
                int max= (int) max_value;
                txt_min.setText(min+"");
                txt_max.setText(max+"");

            }
        });
        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new_fieldsValues[3]=txt_min.getText().toString();
                new_fieldsValues[4]=txt_max.getText().toString();
                new_fieldsValues[5]=countryName_spinner.getSelectedItem().toString();
                new_fieldsValues[6]="";
                new_fieldsValues[2]="data";
                String params=putParamsTogether(new_fields,new_fieldsValues);
                new DownloadAsync(PopulationDetails.this).execute(site,params,"data");
                new_fieldsValues[2]="countryData";
                params=putParamsTogether(new_fields,new_fieldsValues);
                new DownloadAsync(PopulationDetails.this).execute(site,params,"countryData");
                res_country.setText("Country : "+new_fieldsValues[5]);

            }
        });


        moreDetails_intent=new Intent(PopulationDetails.this,MoreDetailsActivity.class);


        btnMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(moreDetails_intent);
            }
        });
    }

    private String putParamsTogether(String[] fields, String[] fieldsValues) {
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<fields.length;i++)
        {
            try {
                fieldsValues[i]= URLEncoder.encode(fieldsValues[i],"UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if(sb.length()>0)
            {
                sb.append("&");
            }
            sb.append(fields[i]).append("=").append(fieldsValues[i]);

        }
        return sb.toString();
    }

    @Override
    public void onSuccess(String result) {
        this.result=result;
    }

    @Override
    public void onSuccessCountry(String result) {


        String code=setCountryDetails(result);
        if(code!=null)
        {

            new_fieldsValues[2]="countryLanguages";
            new_fieldsValues[6]=code;
            String params=putParamsTogether(new_fields,new_fieldsValues);
            new DownloadAsync(PopulationDetails.this).execute(site,params,"countryLanguages");
        }
        Log.d("test ...2", "onSuccessCountry: "+result);
    }

    @Override
    public void onSuccessCountryLanguage(String result) {

        Log.d("code res", "onSuccessCountryLanguage: "+result);
          setCountryLanguages(result);
          if(this.result!=null)
          {
             // Toast.makeText(this, "Ready to explore", Toast.LENGTH_SHORT).show();
              moreDetails_intent.putExtra("result",this.result);
              btnMoreDetails.setVisibility(View.VISIBLE);
          }
    }

    private void setCountryLanguages(String countryLanguages) {
        try {
            JSONArray jsonArray=new JSONArray(countryLanguages);
            JSONObject objLanguage;
            if(jsonArray.length()>0)
            {
                StringBuilder sb=new StringBuilder();
                for(int i=0;i<jsonArray.length();i++)
                {

                    objLanguage= (JSONObject) jsonArray.get(i);
                    if(sb.length()>0)
                    {
                        sb.append(", ");
                    }
                    sb.append(objLanguage.getString("Language"));

                    Log.d("test con..", "setCountryLanguages: "+jsonArray.get(i));

                }
                res_languages.setText("Languages : "+sb.toString());

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String setCountryDetails(String countryDetails) {

        String code="";
        try {
            JSONArray jsonArray=new JSONArray(countryDetails);
            JSONObject objCountry;
            if(jsonArray.length()>0)
            {
                StringBuilder sb=new StringBuilder();
                objCountry= (JSONObject) jsonArray.get(0);
                Log.d("test obj..", "setCountryDetails: "+objCountry.toString());
                code=objCountry.getString("Code");
                res_continent.setText("Continent : "+objCountry.getString("Continent"));
                res_region.setText("Region : "+objCountry.getString("Region"));
                res_code.setText("Code : "+code);
                res_GForm.setText("GovernmentForm : "+objCountry.getString("GovernmentForm"));
               return code;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onFailure(Exception e) {

    }
}
