package com.example.projecteloggerambsql;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button btnNouContacte;
    private ListView listViewContactes;

    private DatabaseOpenHelper dbHelper;
    private List<Contacte> contactesList;
    private ArrayAdapter<Contacte> adapter;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseOpenHelper(this);

        btnNouContacte = findViewById(R.id.btnNouContacte);
        listViewContactes = findViewById(R.id.listViewContactes);

        btnNouContacte.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SegonaActivity.class);
            startActivity(intent);
        });

        listViewContactes.setOnItemClickListener((parent, view, position, id) -> {
            Contacte contacteSeleccionat = contactesList.get(position);
            Intent intent = new Intent(MainActivity.this, SegonaActivity.class);
            intent.putExtra("contacte", contacteSeleccionat);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarContactes();
    }

    private void carregarContactes() {
        contactesList = dbHelper.obtenirTotsElsContactes();
        adapter = new ArrayAdapter<Contacte>(this, R.layout.activity_item_contacte, R.id.tvNomComplet, contactesList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tvNomComplet = view.findViewById(R.id.tvNomComplet);
                TextView tvDetalls = view.findViewById(R.id.tvDetalls);

                Contacte contacte = getItem(position);
                if (contacte != null) {
                    tvNomComplet.setText(contacte.getNomComplet());
                    String telefonStr = contacte.getTelefon() != null ? String.valueOf(contacte.getTelefon()) : "-";
                    String mailStr = contacte.getMail() != null && !contacte.getMail().isEmpty() ? contacte.getMail() : "-";
                    String adrecaStr = contacte.getAdreca() != null && !contacte.getAdreca().isEmpty() ? contacte.getAdreca() : "-";
                    String dataStr = contacte.getDataNaixement() != null ? dateFormat.format(contacte.getDataNaixement()) : "-";

                    String detalls = view.getContext().getString(R.string.detalls_contacte, telefonStr, mailStr, adrecaStr, dataStr);
                    tvDetalls.setText(detalls);
                }
                return view;
            }
        };
        listViewContactes.setAdapter(adapter);
    }
}
