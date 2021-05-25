package yazilim.com.yazilim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    EditText email,pass;
    Button login,register;
    dbConnection connector;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);


      //  register = (Button) findViewById(R.id.register);



        connector = new dbConnection();
        progressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisYap giris = new girisYap();
                giris.execute();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private class girisYap extends AsyncTask{

        String stremail = email.getText().toString();
        String strpass = pass.getText().toString();
        int id;
        String z;
        Boolean giris = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Lütfen bekleyin...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Object[] objects) {
            if(stremail.trim().equals("")||strpass.trim().equals("")){
                z = "Lütfen girilecek alanları boş bırakmayınız.";
            }
            else{
                try {
                    Connection connect = connector.getConnection();
                    if(connect == null){
                        z = "Lütfen internet bağlantınızı kontrol edin.";
                    }
                    else{
                        PreparedStatement ps = connect.prepareStatement("SELECT * FROM customer WHERE email = ? AND password = ?");
                        ps.setString(1,stremail);
                        ps.setString(2,strpass);
                        ResultSet result = ps.executeQuery();
                        if(result.next()){
                            z = "Giriş başarılı.";
                            giris = true;
                            id = (int) result.getInt(1);
                        }
                        else{
                            z = "Giriş başarısız.";
                            giris = false;
                        }
                    }
                } catch (SQLException e) {
                    z = "Hata :" + e;
                    giris = false;
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(getApplicationContext(),z,Toast.LENGTH_LONG).show();

            if(giris){
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("IDuser",id);
                startActivity(intent);
            }
            progressDialog.hide();
        }
    }
}
