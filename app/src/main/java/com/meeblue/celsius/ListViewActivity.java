package com.meeblue.celsius;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meeblue.celsius.activity.MyActivity;
import com.meeblue.celsius.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity<onClick> extends AppCompatActivity implements View.OnClickListener {
    private   ListView mListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        //初始化
        initUI();

        //实例化数据
        initData();
    }

    private void initData() {
        //UserDao userDao=UserDao.getInstance(getApplicationContext());
        //初始化数据
        List<User> users=new ArrayList<User>();
        for (int i=0;i<=100;i++){
            User user=new User();
            user.setId(1);
            user.setAge(18);
            user.setName("meeblue");
            user.setSex("男");
            users.add(user);
        }
        //List<User> users=userDao.findAll();
        MyActivity myActivity = new MyActivity(users, getApplicationContext());
        mListView.setAdapter(myActivity);
    }

    private void initUI() {
         mListView = findViewById(R.id.listView);
        findViewById(R.id.save).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save:

                showAlertDialog();

                break;

        }
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog  alertDialog = builder.create();
        final View inflate = View.inflate(getApplicationContext(), R.layout.alert_save, null);
        alertDialog.setView(inflate);
        final TextView edName = inflate.findViewById(R.id.edName);
        final TextView edAge = inflate.findViewById(R.id.edAge);
        //自动弹窗
        alertDialog.show();
        inflate.findViewById(R.id.saveOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String name=edName.getText().toString();
               String age=edAge.getText().toString();
               save(name,age);

            }
        });
        inflate.findViewById(R.id.savaOFF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private void save(String name, String age) {


    }

}
