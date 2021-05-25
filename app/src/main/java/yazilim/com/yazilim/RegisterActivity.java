package yazilim.com.yazilim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    EditText name,surname,email,password,gsm,address;
    Button register,back;
    Spinner gender,province,district;
    dbConnection connector;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        gsm = (EditText) findViewById(R.id.gsm);
        address = (EditText) findViewById(R.id.address);
        register = (Button) findViewById(R.id.register);
        back = (Button) findViewById(R.id.back);
        gender = (Spinner) findViewById(R.id.gender);
        province = (Spinner) findViewById(R.id.province);
        district = (Spinner) findViewById(R.id.district);
        connector = new dbConnection();
        progressDialog = new ProgressDialog(this);

        illeriCek();

        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yazilim.com.yazilim.province prov = (yazilim.com.yazilim.province) province.getSelectedItem();
                ilceleriCek(prov.idProvince);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String[] genders = {"erkek","kız"};
        gender.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,genders));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayitOl kayit = new kayitOl();
                kayit.execute();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    private class kayitOl extends AsyncTask{

        String nameStr = name.getText().toString();
        String surnameStr = surname.getText().toString();
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        String gsmStr = gsm.getText().toString();
        String addressStr = address.getText().toString();
        String genderStr = gender.getSelectedItem().toString();
        province prov = (province) province.getSelectedItem();
        district dist = (district) district.getSelectedItem();

        String z;
        Boolean kayit = false;

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Ekleniyor...");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if(nameStr.trim().equals("")||surnameStr.trim().equals("")||emailStr.trim().equals("")||passwordStr.trim().equals("")
                    ||gsmStr.trim().equals("")||addressStr.trim().equals("")){
                z = "Lütfen gerekli alanları doldurun.";
            }
            else{
                Connection connect = connector.getConnection();
                if(connect == null){
                    z = "Lütfen internet bağlantınızı kontrol edin.";
                }
                else{
                    try{
                        PreparedStatement ps = connect.prepareStatement("INSERT INTO customer (name_customer,surname_customer,email,password,gsm_no,gender,id_province,id_district,address) VALUES (?,?,?,?,?,?,?,?,?)");
                        ps.setString(1,nameStr);
                        ps.setString(2,surnameStr);
                        ps.setString(3,emailStr);
                        ps.setString(4,passwordStr);
                        ps.setString(5,gsmStr);
                        ps.setString(6,genderStr);
                        ps.setInt(7,prov.idProvince);
                        ps.setInt(8,dist.idDistrict);
                        ps.setString(9,addressStr);
                        int result = ps.executeUpdate();
                        if(result > 0){
                            kayit = true;
                            z = "Kayıt edildi. Oluşturduğunuz kayıt ile giriş yapabilirisiniz.";
                        }
                        else{
                            kayit = false;
                            z = "Kayıt edilemedi.";
                        }
                    } catch (SQLException e) {
                        z = "Hata : " + e;
                        kayit = false;
                    }
                }
            }

            return z;
        }

        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(getApplicationContext(),z,Toast.LENGTH_LONG).show();

            if(kayit){
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
            progressDialog.hide();
        }
    }

    void illeriCek(){
        ArrayList<province> provinces = new ArrayList<province>();
        try{
            Connection connect = connector.getConnection();
            PreparedStatement ps = connect.prepareStatement("SELECT * FROM province");
            ResultSet result = ps.executeQuery();
            while (result.next()){
                provinces.add(new province((int) result.getInt(1),(String) result.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayAdapter<province> provinceArrayAdapter = new ArrayAdapter<province>(getApplication(),android.R.layout.simple_spinner_item,provinces);
        provinceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(provinceArrayAdapter);
    }

    void ilceleriCek(int id){
        ArrayList<district> districts = new ArrayList<district>();

        try{
            Connection connect = connector.getConnection();
            PreparedStatement ps = connect.prepareStatement("SELECT * FROM district WHERE id_province = ?");
            ps.setInt(1,id);
            ResultSet result = ps.executeQuery();
            while (result.next()){
                districts.add(new district((int) result.getInt(1),(int) result.getInt(2),(String) result.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayAdapter<district> districtArrayAdapter = new ArrayAdapter<district>(getApplication(),android.R.layout.simple_spinner_item,districts);
        districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(districtArrayAdapter);
    }
}
