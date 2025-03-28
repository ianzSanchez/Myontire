package com.example.myontire;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class FindMechanic extends AppCompatActivity {

    private EditText etSearch;
    private LinearLayout mechanicList;
    private List<View> mechanicCards = new ArrayList<>();
    private List<TextView> mechanicNames = new ArrayList<>();

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
        mechanicList = findViewById(R.id.mechanic_list);

        addMechanicCard(R.id.mechanic_name_budi, R.id.mechanic_card_budi);
        addMechanicCard(R.id.mechanic_name_agus, R.id.mechanic_card_agus);
        addMechanicCard(R.id.mechanic_name_viko, R.id.mechanic_card_viko);
        addMechanicCard(R.id.mechanic_name_abdul, R.id.mechanic_card_abdul);

        // Handle button clicks for mechanics
        setChooseButtonClickListener(R.id.btn_choose_budi);
        setChooseButtonClickListener(R.id.btn_choose_agus);
        setChooseButtonClickListener(R.id.btn_choose_viko);
        setChooseButtonClickListener(R.id.btn_choose_abdul);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMechanics(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Method to handle button clicks dynamically for each "choose" button
    private void setChooseButtonClickListener(int buttonId) {
        Button chooseButton = findViewById(buttonId);
        if (chooseButton != null) {
            chooseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open RequestStatusActivity when a button is clicked
                    Intent intent = new Intent(FindMechanic.this, RequestStatusActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void addMechanicCard(int nameId, int cardId) {
        TextView nameView = findViewById(nameId);
        View cardView = findViewById(cardId);
        if (nameView != null && cardView != null) {
            mechanicNames.add(nameView);
            mechanicCards.add(cardView);
        }
    }

    private void filterMechanics(String query) {
        query = query.toLowerCase();

        if (query.isEmpty()) {
            for (View card : mechanicCards) {
                card.setVisibility(View.VISIBLE);
            }
            return;
        }
        for (View card : mechanicCards) {
            card.setVisibility(View.GONE);
        }

        for (int i = 0; i < mechanicNames.size(); i++) {
            String name = mechanicNames.get(i).getText().toString().toLowerCase();
            if (name.contains(query)) {
                mechanicCards.get(i).setVisibility(View.VISIBLE);
                break;
            }
        }
    }
}
