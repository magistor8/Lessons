package com.example.lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView1);
        textView.setText("Старт программы");

        EditText editText = findViewById(R.id.editText1);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textView.setText("Пользователь набирает текст");
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            textView.setText("Клик по кнопке");
        });

        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            textView.setText("Клик по свичу");
        });

        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnClickListener(v -> {
            textView.setText("Клик по чекбоксу");
        });

        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(v -> {
            textView.setText("Клик по тоглбаттон");
        });

    }
}