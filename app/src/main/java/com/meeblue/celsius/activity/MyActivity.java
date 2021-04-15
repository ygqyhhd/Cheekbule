package com.meeblue.celsius.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meeblue.celsius.R;
import com.meeblue.celsius.entity.User;

import java.util.List;

public class MyActivity extends BaseAdapter {
    private  Context context;
    private List<User> users;

    public MyActivity(List<User> users, Context context) {
        this.users=users;
        this.context=context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return users.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

          if (view==null){
              view = View.inflate(context, R.layout.user, null);
          }
          TextView id=view.findViewById(R.id.userId);
          TextView name =view.findViewById(R.id.userName);
          TextView sex =view.findViewById(R.id.userSex);
          id.setText(users.get(i).getId()+"");
          name.setText(users.get(i).getName());
          sex.setText(users.get(i).getAge());
        return view;
    }
}
