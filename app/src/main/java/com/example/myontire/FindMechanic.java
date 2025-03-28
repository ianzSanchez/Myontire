package com.example.myontire;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindMechanic extends AppCompatActivity {

    private EditText etSearch;
    private Spinner spLocation;
    private LinearLayout mechanicList;
    private List<View> mechanicCards = new ArrayList<>();
    private List<TextView> mechanicNames = new ArrayList<>();
    private Map<TextView, String> mechanicLocations = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_mechanic);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etSearch = findViewById(R.id.et_search);
        spLocation = findViewById(R.id.spinner_location);
        mechanicList = findViewById(R.id.mechanic_list);

        // Tambahkan daftar lokasi ke Spinner
        String[] locations = {"All", "Jakarta", "Bandung", "Surabaya", "Solo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locations);
        spLocation.setAdapter(adapter);

        // Tambahkan mekanik dan lokasi mereka
        addMechanicCard(R.id.mechanic_name_budi, R.id.mechanic_card_budi, "Jakarta");
        addMechanicCard(R.id.mechanic_name_agus, R.id.mechanic_card_agus, "Bandung");
        addMechanicCard(R.id.mechanic_name_viko, R.id.mechanic_card_viko, "Surabaya");
        addMechanicCard(R.id.mechanic_name_abdul, R.id.mechanic_card_abdul, "Solo");


        Button btnChooseAgus = findViewById(R.id.btn_choose_agus);
        if (btnChooseAgus != null) {
            btnChooseAgus.setOnClickListener(v -> {
                Intent intent = new Intent(FindMechanic.this, Agus.class);
                startActivity(intent);
            });
        }

        Button btnChooseBudi = findViewById(R.id.btn_choose_budi);
        if (btnChooseBudi != null) {
            btnChooseBudi.setOnClickListener(v -> {
                Intent intent = new Intent(FindMechanic.this, Budi.class);
                startActivity(intent);
            });
        }

        Button btnChooseViko = findViewById(R.id.btn_choose_viko);
        if (btnChooseViko != null) {
            btnChooseViko.setOnClickListener(v -> {
                Intent intent = new Intent(FindMechanic.this, Viko.class);
                startActivity(intent);
            });
        }

        Button btnChooseAbdul = findViewById(R.id.btn_choose_abdul);
        if (btnChooseAbdul != null) {
            btnChooseAbdul.setOnClickListener(v -> {
                Intent intent = new Intent(FindMechanic.this, Abdul.class);
                startActivity(intent);
            });
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMechanics();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterMechanics();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void addMechanicCard(int nameId, int cardId, String location) {
        TextView nameView = findViewById(nameId);
        View cardView = findViewById(cardId);
        if (nameView != null && cardView != null) {
            mechanicNames.add(nameView);
            mechanicCards.add(cardView);
            mechanicLocations.put(nameView, location.toLowerCase()); // Simpan lokasi dalam lowercase
        }
    }

    private void filterMechanics() {
        String query = etSearch.getText().toString().toLowerCase();
        String location = spLocation.getSelectedItem().toString().toLowerCase();
        boolean filterByLocation = !location.equals("all");

        for (int i = 0; i < mechanicNames.size(); i++) {
            String name = mechanicNames.get(i).getText().toString().toLowerCase();
            String mechanicLocation = mechanicLocations.get(mechanicNames.get(i));
            boolean matchesSearch = query.isEmpty() || name.contains(query);
            boolean matchesLocation = !filterByLocation || mechanicLocation.equals(location);

            if (matchesSearch && matchesLocation) {
                mechanicCards.get(i).setVisibility(View.VISIBLE);
            } else {
                mechanicCards.get(i).setVisibility(View.GONE);
            }
        }
    }
}
