package com.github.android.flux;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.android.flux.actions.ActionsCreator;
import com.github.android.flux.dispatcher.Dispatcher;
import com.github.android.flux.model.Singer;
import com.squareup.otto.Bus;

/**
 * Created by zlove on 2018/2/5.
 */

public class FillInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionsCreator actionsCreator;
    private Dispatcher dispatcher;

    private EditText etName;
    private EditText etGender;
    private EditText etAge;
    private Button btnAdd;

    private String name;
    private String gender;
    private String age;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);

        initDependencies();

        etName = (EditText) findViewById(R.id.name);
        etGender = (EditText) findViewById(R.id.gender);
        etAge = (EditText) findViewById(R.id.age);

        btnAdd = (Button) findViewById(R.id.add);
        btnAdd.setOnClickListener(this);
    }

    private void initDependencies() {
        dispatcher = Dispatcher.get(new Bus());
        actionsCreator = ActionsCreator.get(dispatcher);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            addSinger();
        }
    }

    private void addSinger() {
        name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast("Please Input Name!");
            return;
        }
        gender = etGender.getText().toString();
        if (TextUtils.isEmpty(gender)) {
            showToast("Please Input Gender!");
            return;
        }
        age = etAge.getText().toString();
        if (TextUtils.isEmpty(gender)) {
            showToast("Please Input Age!");
            return;
        }
        long id = System.currentTimeMillis();
        int ageValue = Integer.valueOf(age);
        Singer singer = new Singer(id, name, gender, ageValue);
        actionsCreator.create(singer);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
