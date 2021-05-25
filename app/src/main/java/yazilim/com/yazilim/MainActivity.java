package yazilim.com.yazilim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button logout,randevuAl;
    Spinner province,district,salon,service,personel;
    dbConnection connector;
    ProgressDialog progressDialog;
    int IDuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = (Button) findViewById(R.id.back);
        randevuAl = (Button) findViewById(R.id.randevu);
        province = (Spinner) findViewById(R.id.province);
        district = (Spinner) findViewById(R.id.district);
        salon = (Spinner) findViewById(R.id.salon);
        service = (Spinner) findViewById(R.id.service);
        personel = (Spinner) findViewById(R.id.personel);
        connector = new dbConnection();
        progressDialog = new ProgressDialog(this);
        Bundle extras = getIntent().getExtras();
        IDuser = extras.getInt("IDuser");

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

        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yazilim.com.yazilim.province prov = (yazilim.com.yazilim.province) province.getSelectedItem();
                yazilim.com.yazilim.district dist = (yazilim.com.yazilim.district) district.getSelectedItem();
                String gender = cinsiyetNe(IDuser);
                salonlariCek(prov.idProvince,dist.idDistrict,gender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        salon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yazilim.com.yazilim.salon sal = (yazilim.com.yazilim.salon) salon.getSelectedItem();
                servisleriCek(sal.idSalonType);
                personelleriCek(sal.idSalon);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
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

    String cinsiyetNe(int IDuser){
        String gender = null;
        try {
            Connection connect = connector.getConnection();
            PreparedStatement ps = connect.prepareStatement("SELECT gender FROM customer WHERE id_customer = ?");
            ps.setInt(1,IDuser);
            ResultSet result = ps.executeQuery();
            gender = result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gender;
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

    void salonlariCek(int provID,int distID,String gender){
        ArrayList<salon> salons = new ArrayList<salon>();

        try{
            Connection connect = connector.getConnection();
            PreparedStatement ps = connect.prepareStatement("SELECT id_salon,id_salontype,name_salon,id_province,id_district FROM salon INNER JOIN salontype ON salon.id_salontype = salontype.id_salontype WHERE id_province = ? AND id_district = ? AND gender = ?");
            ps.setInt(1,provID);
            ps.setInt(2,distID);
            ps.setString(3,gender);
            ResultSet result = ps.executeQuery();
            while (result.next()){
                salons.add(new salon(result.getInt(1),result.getInt(2),result.getString(3),result.getInt(4),result.getInt(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayAdapter<salon> salonArrayAdapter = new ArrayAdapter<salon>(getApplication(),android.R.layout.simple_spinner_item,salons);
        salonArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salon.setAdapter(salonArrayAdapter);
    }

    void servisleriCek(int typeID){
        ArrayList<service> services = new ArrayList<service>();

        try{
            Connection connect = connector.getConnection();
            PreparedStatement ps = connect.prepareStatement("SELECT * FROM service WHERE id_salontype = ?");
            ps.setInt(1,typeID);
            ResultSet result = ps.executeQuery();
            while (result.next()){
                services.add(new service(result.getInt(1),result.getInt(2),result.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayAdapter<service> serviceArrayAdapter = new ArrayAdapter<service>(getApplication(),android.R.layout.simple_spinner_item,services);
        serviceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        service.setAdapter(serviceArrayAdapter);
    }

    void personelleriCek(int salonID){
        ArrayList<personel> personels = new ArrayList<personel>();

        try{
            Connection connect = connector.getConnection();
            PreparedStatement ps = connect.prepareStatement("SELECT id_personnel,id_salon,name_personnel,surname_personnel FROM personnel WHERE id_salon = ?");
            ps.setInt(1,salonID);
            ResultSet result = ps.executeQuery();
            while (result.next()){
                personels.add(new personel(result.getInt(1),result.getInt(2),result.getString(3),result.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayAdapter<personel> personelArrayAdapter = new ArrayAdapter<personel>(getApplication(),android.R.layout.simple_spinner_item,personels);
        personelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personel.setAdapter(personelArrayAdapter);
    }
}
