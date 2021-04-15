package com.meeblue.celsius;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignActivity extends AppCompatActivity {

    private EditText mID;
    private EditText mPass;
    private CheckBox mRemember;
    private String M_QQ_ID = "m_qq_id";
    private String M_QQ_PASSWORD = "m_qq_password";
    private String M_QQ_REMEMBER="m_qq_remember";
    private SharedPreferences sharedPreferences;
    private Boolean mISCheckBox=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_main);


        intiUI();

        //初始化数据
        initData();
    }

    private void initData() {

        if (sharedPreferences==null){
            sharedPreferences = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        mID.setText(sharedPreferences.getString(M_QQ_ID,""));
        mPass.setText(sharedPreferences.getString(M_QQ_PASSWORD,""));
        Log.i("它的值为：",sharedPreferences.getBoolean(M_QQ_REMEMBER ,false)+"");
        mISCheckBox=sharedPreferences.getBoolean(M_QQ_REMEMBER ,false);
        if (mISCheckBox){
            mRemember.setChecked(mISCheckBox);
        }

    }

    private void intiUI() {

        mID = findViewById(R.id.qq_id);
        mID.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
              if (mISCheckBox){
                  sharedPreferences = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                  SharedPreferences.Editor edit = sharedPreferences.edit();
                  edit.putString(M_QQ_ID,mID.getText().toString());
                  edit.commit();
              }
            }
        });
        mPass = findViewById(R.id.qq_pass);
        mPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mISCheckBox){
                    sharedPreferences = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString(M_QQ_PASSWORD,mPass.getText().toString());
                    edit.commit();
                }
            }
        });
        mRemember = findViewById(R.id.remember);
        //点击选项框
        mRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mISCheckBox=b;
                Log.e("SignActivity",b+"");
              if (b){
                  //实例化sharedPreferences对象
                  if (sharedPreferences==null){
                      sharedPreferences = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                  }
                  //实例化sharedPreferences编写对象
                  SharedPreferences.Editor edit = sharedPreferences.edit();
                  //存储数据
                  edit.putString(M_QQ_ID,mID.getText().toString());
                  edit.putString(M_QQ_PASSWORD,mPass.getText().toString());
                  edit.putBoolean(M_QQ_REMEMBER,b);
                  edit.commit();
              }else{
                  Log.i("SignActivity","清除edit数据");
                  sharedPreferences = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                  SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.remove(M_QQ_ID);
                    edit.remove(M_QQ_PASSWORD);
                    edit.remove(M_QQ_REMEMBER);
                  edit.commit();
              }
            }
        });

    }
}
