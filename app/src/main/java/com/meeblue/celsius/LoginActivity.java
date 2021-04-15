package com.meeblue.celsius;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.meeblue.celsius.entity.User;
import com.orm.SugarApp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    HttpURLConnection connection;
    InputStream inputStream;
    BufferedReader bufferedReader;
    private  int GET_DATA_SUCCESS = 101;//获取数据提交成功
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(message.what==GET_DATA_SUCCESS){
                String data=message.getData().getString("data");
                TextView textView=findViewById(R.id.name);
                textView.setText(data);
            }
            return false;
        }
    }){};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        Bundle build=getIntent().getExtras();
        User user= (User) build.get("user");
        TextView textView=findViewById(R.id.name);
        textView.setText(user.getName());
        initUI();
        intiData();

    }

    private void initUI() {
        findViewById(R.id.data).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.data:
                intiData();
                break;
        }
    }

    //初始化数据 -->子线程
    private void intiData() {
      new Thread(new Runnable() {


          @Override
          public void run() {
             String data= getDataService();
             Message message = Message.obtain();
             Bundle bundle=new Bundle();
             bundle.putString("data",data);
             message.setData(bundle);
             message.what=GET_DATA_SUCCESS;
             //向主线程发送消息
             handler.sendMessage(message);
          }


      }).start();
    }
    //从服务发送消息
    private String getDataService() {
        try {
            URL url=new URL("Https://v1.hitokoto.cn?c=f&encode=text");

            connection = (HttpURLConnection)url.openConnection();

            if (connection.getResponseCode()==200){
                inputStream = connection.getInputStream();
                bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
               StringBuffer sb=new StringBuffer();
               for (String line="";(line=bufferedReader.readLine())!=null;){
                sb.append(line);
               }
            return sb.toString();
           }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {

                if (bufferedReader!=null)bufferedReader.close();
                if (inputStream!=null) inputStream.close();
                if (connection!=null) connection.disconnect();

            }catch (Exception e){
                e.printStackTrace();
            }


        }
        return "";
    }

}
