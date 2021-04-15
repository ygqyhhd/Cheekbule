package com.meeblue.celsius;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.meeblue.celsius.util.ble.DeviceAdapter;
import com.meeblue.celsius.util.ble.comm.ObserverManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BroadcastActivity extends AppCompatActivity implements View.OnClickListener {

    private Animation operatingAnim;
    private DeviceAdapter mDeviceAdapter;
    private ProgressDialog progressDialog;


    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_broadcast_layout);
       // textView = findViewById(R.id.tv1);
        initView();

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);


    }

    private void initView() {
        findViewById(R.id.gain).setOnClickListener(this);
        mDeviceAdapter = new DeviceAdapter(this);
        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                Log.i("执行顺序","21");
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);
                }
            }

            @Override
            public void onDisConnect(final BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().disconnect(bleDevice);
                }
            }

            @Override
            public void onDetail(BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {

                }
            }

        });
    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {

            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                if (isActiveDisConnected) {

                } else {

                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        showConnectedDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }

    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        mDeviceAdapter.clearConnectedDevice();
        for (BleDevice bleDevice : deviceList) {
            mDeviceAdapter.addDevice(bleDevice);
        }
        mDeviceAdapter.notifyDataSetChanged();

    }

    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }
                }
                break;
        }
    }

    private void checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "请先打开蓝牙", Toast.LENGTH_LONG).show();
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("当前手机扫描蓝牙需要打开定位功能")
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                            .setPositiveButton("前往设置",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    setScanRule();
                    startScan();
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }




    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();

            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                Log.i("mac",bleDevice.getMac()+"");
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                Log.i("错误3","错误3");
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                Log.i("错误4",scanResultList.size()+"");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.gain:
                checkPermissions();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (checkGPSIsOpen()) {

                startScan();
            }
        }
    }


    private void setScanRule() {
        String[] uuids;
        String str_uuid = "ef5b0000-2c63-5f62-8f69-179ce3efd266";
        if (TextUtils.isEmpty(str_uuid)) {
            uuids = null;
        } else {
            uuids = str_uuid.split(",");
        }
        UUID[] serviceUuids = null;
        if (uuids != null && uuids.length > 0) {
            serviceUuids = new UUID[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                String name = uuids[i];
                String[] components = name.split("-");
                if (components.length != 5) {
                    serviceUuids[i] = null;
                } else {
                    serviceUuids[i] = UUID.fromString(uuids[i]);
                }
            }
        }

        String[] names;
        String str_name ="s";
        if (TextUtils.isEmpty(str_name)) {
            names = null;
        } else {
            names = str_name.split(",");
        }


        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
                .setDeviceMac("F6:51:45:D3:73:9B")
                .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

}