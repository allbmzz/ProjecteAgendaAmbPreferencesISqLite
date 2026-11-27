package com.example.projecteloggerambsql;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SegonaActivity extends AppCompatActivity {

    private EditText etNom, etCognom, etAdreca, etTelefon, etMail, etDataNaixement;
    private Button btnAcceptar, btnCancelar, btnEsborrar;

    private DatabaseOpenHelper dbHelper;
    private Contacte contacteActual;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segona);

        dbHelper = new DatabaseOpenHelper(this);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

        etNom = findViewById(R.id.etDetallNom);
        etCognom = findViewById(R.id.etDetallCognom);
        etAdreca = findViewById(R.id.etDetallAdreca);
        etTelefon = findViewById(R.id.etDetallTelefon);
        etMail = findViewById(R.id.etDetallMail);
        etDataNaixement = findViewById(R.id.etDetallDataNaixement);
        
        btnAcceptar = findViewById(R.id.btnAcceptar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnEsborrar = findViewById(R.id.btnEsborrar);

        // Comprovem si ens han passat un contacte per editar
        if (getIntent() != null && getIntent().hasExtra("contacte")) {
            contacteActual = (Contacte) getIntent().getSerializableExtra("contacte");
        }


        if (contacteActual != null) {
            etNom.setText(contacteActual.getNom());
            etCognom.setText(contacteActual.getCognom());
            etAdreca.setText(contacteActual.getAdreca());
            
            if (contacteActual.getTelefon() != null) {
                etTelefon.setText(String.valueOf(contacteActual.getTelefon()));
            }
            
            etMail.setText(contacteActual.getMail());
            
            if (contacteActual.getDataNaixement() != null) {
                etDataNaixement.setText(dateFormat.format(contacteActual.getDataNaixement()));
            }
            
            btnEsborrar.setVisibility(View.VISIBLE);
        } else {
            btnEsborrar.setVisibility(View.GONE);
        }


        btnAcceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDades();
            }
        });


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        
        btnEsborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contacteActual != null) {
                    dbHelper.esborrarContacte(contacteActual.getId());
                }
                finish();
            }
        });
    }

    private void guardarDades() {
        String nom = etNom.getText().toString();
        String cognom = etCognom.getText().toString();
        String adreca = etAdreca.getText().toString();
        String telefonStr = etTelefon.getText().toString();
        String mail = etMail.getText().toString();
        String dataStr = etDataNaixement.getText().toString();


        if (nom.equals("")) {
            return;
        }
        if (cognom.equals("")) {
            return;
        }

        Integer telefon = null;
        if (!telefonStr.equals("")) {
            try {
                telefon = Integer.parseInt(telefonStr);
            } catch (Exception e) {
                telefon = 0;
            }
        }

        Date dataNaixement = null;
        if (!dataStr.equals("")) {
            try {
                dataNaixement = dateFormat.parse(dataStr);
            } catch (Exception e) {
                dataNaixement = null;
            }
        }


        if (contacteActual == null) {
            Contacte nou = new Contacte(nom, cognom, adreca, telefon, mail, dataNaixement);
            dbHelper.inserirContacte(nou);
        } else {

            contacteActual.setNom(nom);
            contacteActual.setCognom(cognom);
            contacteActual.setAdreca(adreca);
            contacteActual.setTelefon(telefon);
            contacteActual.setMail(mail);
            contacteActual.setDataNaixement(dataNaixement);
            dbHelper.actualitzarContacte(contacteActual);
        }

        finish();
    }
}
