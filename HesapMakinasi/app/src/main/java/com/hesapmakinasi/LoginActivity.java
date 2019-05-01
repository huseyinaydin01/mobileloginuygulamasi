package com.hesapmakinasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText editTextKullaniciAdi;
    EditText editTextParola;

    Button button;

    String kullaniciAdi = "admin";
    String parola = "1234";

    int hak = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextKullaniciAdi = findViewById(R.id.editTextKullaniciAdi);
        editTextParola = findViewById(R.id.editTextParola);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hak < 2){
                    if (editTextKullaniciAdi.getText().toString().equals(kullaniciAdi) && editTextParola.getText().toString().equals(parola)){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                        hak = hak + 1;
                    }
                }else {
                    button.setVisibility(View.GONE);
                }
            }
        });
    }
}
