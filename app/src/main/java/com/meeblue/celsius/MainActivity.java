package com.meeblue.celsius;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.meeblue.celsius.entity.User;
import com.meeblue.celsius.util.BluetoothMonitorReceiver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;

import android.view.Window;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    private BlueToothController mController= new BlueToothController();
    private Toast mToast;
    private BluetoothAdapter BA;
    private BluetoothMonitorReceiver mReceive;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initUI();
    }

    public void initUI(){
       findViewById(R.id.button).setOnClickListener(this);
       findViewById(R.id.button2).setOnClickListener(this);
       findViewById(R.id.list).setOnClickListener(this);
       findViewById(R.id.btn4).setOnClickListener(this);
       findViewById(R.id.btn5).setOnClickListener(this);
       findViewById(R.id.bluetooth).setOnClickListener(this);
       findViewById(R.id.bluetooth_off).setOnClickListener(this);
       findViewById(R.id.scan).setOnClickListener(this);
        BA=BluetoothAdapter.getDefaultAdapter();
      /*  Book book=Book.findById(Book.class,1);
       Log.i("book=",book.getAuthor()+"--"+book.getName()+"----"+book.getNumber());*/
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(),"Turned on"
                    ,Toast.LENGTH_LONG).show();
        }
        registerBluetoothReceiver();

    }


    @Override
    public void onClick(View view) {
        Intent intent=new Intent();
        User user=new User();
        user.setAge(18);
        user.setId(1);
        user.setName("杨国钦");
        user.setSex("男");
        intent.putExtra("user",user);
        switch (view.getId()){
            case R.id.button:
                intent.setClass(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.button2:
                intent.setClass(getApplicationContext(),SignActivity.class);
                startActivity(intent);
                break;
            case R.id.list:
                intent.setClass(getApplicationContext(),ListViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn4:
                intent.setClass(getApplicationContext(), BroadcastActivity.class);
                startActivity(intent);
                break;
            case R.id.btn5:
                intent.setClass(getApplicationContext(),RobotActivity.class);
                startActivity(intent);
                break;
            case R.id.bluetooth:
                break;
            case R.id.bluetooth_off:

                if (BA.isEnabled()) {
                   BA.isEnabled();
                }
                break;
            case R.id.scan:
                //动态权限申请
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    goScan();
                }
                break;
            default:
                break;
        }
    }
    private void registerBluetoothReceiver(){
        if(mReceive == null){
            mReceive = new BluetoothMonitorReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        registerReceiver(mReceive, intentFilter);
    }

    private void goScan(){
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScan();
                } else {
                    Toast.makeText(this, "你拒绝了权限申请，可能无法打开相机扫码哟！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //返回的文本内容
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                //返回的BitMap图像
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                Log.i("你扫描到的内容是:",content);
            }
        }
    }

}