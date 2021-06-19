package com.example.lesson4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {

    private boolean isLightMode;
    private final String intentKey = "KEY_THEME";
    private SwitchCompat nightMode;
    private Button apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intentParams = this.getIntent();
        Bundle params = intentParams.getExtras();
        if (params != null) {
            isLightMode = params.getBoolean(intentKey);
            if (isLightMode) {
                setTheme(R.style.Base);
            } else {
                setTheme(R.style.DarkTheme);
            }
        }
        
        setContentView(R.layout.activity_setting);
        nightMode = findViewById(R.id.switch1);
        apply = findViewById(R.id.apply);
        checkSwitch();

        nightMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isLightMode = !isChecked;
        });

        apply.setOnClickListener(v -> {
            Intent intentResult = new Intent();
            intentResult.putExtra(intentKey, isLightMode);
            this.setResult(RESULT_OK, intentResult);
            this.finish();
        });
    }

    private void checkSwitch() {
        nightMode.setChecked(!isLightMode);
    }

}