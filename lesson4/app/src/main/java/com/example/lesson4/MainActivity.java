package com.example.lesson4;

import androidx.activity.result.*;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String NameSharedPreference = "CALC";
    private final String AppTheme = "APP_THEME";
    private final String keyData = "Key";
    private final String intentKey = "KEY_THEME";

    protected Data data = new Data();

    private TextView subtotal;
    private TextView total;

    private static final StringBuilder builder = new StringBuilder();

    private static final int[] BUTTONS = {
            R.id.buttonC,
            R.id.buttonBack,
            R.id.buttonPercent,
            R.id.buttonDivision,
            R.id.button7,
            R.id.button8,
            R.id.button9,
            R.id.buttonMultip,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.buttonMin,
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.buttonPlus,
            R.id.button0,
            R.id.buttonDot,
            R.id.buttonEqually
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getAppTheme());
        setSpecificLayout(this.getResources().getConfiguration().orientation);
        subtotal = findViewById(R.id.subtotal);
        total = findViewById(R.id.total);
        initButton();
        initButtonSetting();
    }

    private void initButtonSetting() {
        findViewById(R.id.setting)
                .setOnClickListener(v -> {
                    Intent runSettings = new Intent(MainActivity.this, SettingActivity.class);
                    runSettings.putExtra(intentKey, isLight());
                    ActivityResultLauncher.launch(runSettings);
                });
    }

    ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intentResult = result.getData();
                    Bundle params = intentResult.getExtras();
                    if (params != null) {
                        if (params.getBoolean(intentKey) != isLight()) {
                            //Находим текущую тему и выбираем противоположную
                            setAppTheme();
                            // пересоздадим активити, чтобы тема применилась
                            recreate();
                        }
                    }
                }
            });

    private void setAppTheme() {
        //Читаем настройки
        SharedPreferences sharedPref = getSharedPreferences(NameSharedPreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (isLight()) {
            editor.putBoolean(AppTheme, false);
        } else {
            editor.putBoolean(AppTheme, true);
        }
        editor.apply();
    }

    private int getAppTheme() {
        if (isLight()) {
            return R.style.Base;
        }
        return R.style.DarkTheme;
    }

    // Чтение настроек, параметр «тема»
    private boolean isLight() {
        SharedPreferences sharedPref = getSharedPreferences(NameSharedPreference, MODE_PRIVATE);
        return sharedPref.getBoolean(AppTheme, true);
    }


    public void setSpecificLayout(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_land);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_port);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setSpecificLayout(newConfig.orientation);
    }

    // Сохранение данных
    @Override
    public void onSaveInstanceState(@NonNull Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        data.setSubtotalResult(subtotal.getText().toString());
        data.setTotalResult(total.getText().toString());
        instanceState.putParcelable(keyData, data);
    }

    // Восстановление данных
    @Override
    protected void onRestoreInstanceState(Bundle instanceState) {
        super.onRestoreInstanceState(instanceState);
        data = instanceState.getParcelable(keyData);
        setParams();
    }

    // Отображение данных на экране
    private void setParams() {
        subtotal.setText(data.getSubtotalResult());
        total.setText(data.getTotalResult());
    }

    private final View.OnClickListener listener = v -> {
        Button b = (Button) v;
        String buttonText = b.getText().toString();
        //Чистим тотал
        total.setText("");
        //Проверяем на нажатие кнопки
        clickButton(buttonText);
        //Показываем сабтотал
        subtotal.setText(data.getTempString());
    };

    private void clickButton(String buttonText) {
        if (isArithmeticOperation(buttonText)) {
            arithmeticOperation(buttonText);
        } else if (buttonText.equals("=")) {
            if (data.getArgCount() == 3) {
                //Выводим результат
                showResult();
            } else if (data.getArgCount() == 0) {
                return;
            }
        } else if (buttonText.equals("C")) {
            //Очищаем все
            clearAll();
        } else if (buttonText.equals("<<")) {
            //Удаляем последнее действие
            if (data.getTempString() == null) return;
            //Делим строку
            deleteLastAction();
        } else {
            if (data.getTempString() == null && !buttonText.equals(".")) {
                data.setTempString(buttonText);
            } else {
                //Точка
                checkDot(buttonText);
            }
            //Настройки
            if (data.getLastOperation() == 0) {
                data.setLastOperation(1);
                data.setArgCount(1);
            }
            if (data.getLastOperation() == 2) {
                data.setLastOperation(1);
                data.argsInc();
            }
        }
    }

    private void checkDot(String buttonText) {
        if (buttonText.equals(".")) {
            //Делим строку
            String[] tempArray = data.getTempString().split("\\s+");
            //Берем последний элемент массива и проверяем есть ли там точка
            if (!tempArray[tempArray.length - 1].contains(".")) {
                //Добавляем точку
                stringRefactor(buttonText);
            }
        } else {
            stringRefactor(buttonText);
        }
    }

    private void stringRefactor(String buttonText) {
        builder.setLength(0);
        builder.append(data.getTempString());
        if (data.getLastOperation() == 2) {
            builder.append(" ");
        }
        builder.append(buttonText);
        data.setTempString(builder.toString());
    }

    private void deleteLastAction() {
        String[] tempArray = data.getTempString().split("\\s+");
        data.setTempString(null);
        if (tempArray.length != 1) {
            builder.setLength(0);
            //Создаем строку без последней операции
            for (int i = 0; i < tempArray.length - 1; i++) {
                if (i != 0) {
                    builder.append(" ");
                }
                builder.append(tempArray[i]);
            }
            data.setTempString(builder.toString());
        }
        data.argsDec();
        if (data.getLastOperation() == 2) {
            data.setLastOperation(1);
        } else {
            data.setLastOperation(2);
        }
    }

    private void clearAll() {
        subtotal.setText("");
        data.setTempString(null);
        data.setLastOperation(0);
        data.setArgCount(0);
    }

    private boolean isArithmeticOperation(String buttonText) {
        switch (buttonText) {
            case ("+"):
            case ("-"):
            case ("÷"):
            case ("x"):
            case ("%"):
                return true;
            default:
                return false;
        }
    }

    private void arithmeticOperation(String buttonText) {
        //Пустая ли промежуточная строка?
        if (data.getTempString() == null) return;
        //Если последним была введена какая-то операция, то меняем ее
        if (data.getLastOperation() == 2) {
            changeLastOperation(buttonText);
            return;
        }
        //Если в последнем аргументе точка на конце
        String[] tempArray = data.getTempString().split("\\s+");
        if (tempArray[tempArray.length - 1].endsWith(".")) {
            tempArray[tempArray.length - 1] = tempArray[tempArray.length - 1].substring(0, tempArray[tempArray.length - 1].length() - 1);
            data.setTempString(null);
            stringBuild(tempArray);
        }
        //Если промежуточная строка состоит из 3 аргументов, то считаем промежуточный итог
        if (data.getArgCount() == 3) {
            if (divisionByZero(tempArray)) return;
            subtotal();
            data.setArgCount(1);
        }
        //Добавляем операцию в строку
        builder.setLength(0);
        builder.append(data.getTempString());
        builder.append(" ").append(buttonText);
        data.setTempString(builder.toString());

        //Настройки
        data.setLastOperation(2);
        data.argsInc();
    }

    private void stringBuild(String[] tempArray) {
        builder.setLength(0);
        int n = 1;
        for (String s : tempArray) {
            if (n != 1) {
                builder.append(" ");
            }
            builder.append(s);
            n++;
        }
        data.setTempString(builder.toString());
    }

    private boolean divisionByZero(String[] tempArray) {
        //Проверяем на деление на 0
        if (tempArray[tempArray.length - 2].equals("÷") && tempArray[tempArray.length - 1].equals("0")) {
            clearAll();
            total.setText("error");
            return true;
        }
        return false;
    }

    private void changeLastOperation(String buttonText) {
        //Делим строку
        String[] tempArray = data.getTempString().split("\\s+");
        //Меняем последний элемент
        tempArray[tempArray.length - 1] = buttonText;
        data.setTempString(null);
        stringBuild(tempArray);
    }

    private void subtotal() {
        double var1;
        double var2;

        //Делим строку
        String[] tempArray = data.getTempString().split("\\s+");
        data.setTempString(null);
        //Переводим строки в числа
        if (tempArray[0].contains(".")) {
            var1 = Double.parseDouble(tempArray[0]);
        } else {
            var1 = Integer.parseInt(tempArray[0]);
        }
        if (tempArray[2].contains(".")) {
            var2 = Double.parseDouble(tempArray[2]);
        } else {
            var2 = Integer.parseInt(tempArray[2]);
        }
        //Выполняем операцию
        switch (tempArray[1]) {
            case ("+"):
                data.setTempString(convert(var1 + var2));
                break;
            case ("-"):
                data.setTempString(convert(var1 - var2));
                break;
            case ("÷"):
                if (var2 == 0f) {
                    clearAll();
                    data.setTempString("error");
                    break;
                }
                data.setTempString(convert(var1 / var2));
                break;
            case ("x"):
                data.setTempString(convert(var1 * var2));
                break;
            case ("%"):
                data.setTempString(convert((var1 / 100) * var2));
                break;
        }
    }

    private void showResult() {
        //Считаем результат
        subtotal();
        total.setText(data.getTempString());
        clearAll();
    }


    private void initButton() {
        for (int id : BUTTONS) {
            Button button = findViewById(id);
            button.setOnClickListener(listener);
        }
    }

    public static String convert(double num) {
        if (num == (int) num) return (Integer.toString((int) num));
        else return Double.toString(num);
    }
}