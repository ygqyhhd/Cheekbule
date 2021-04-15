package com.meeblue.celsius;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;

public class BlueToothController {

    public BluetoothAdapter mAdaoter;

    public   BlueToothController(){
        //Bluetooth=blue吐死
        mAdaoter=BluetoothAdapter.getDefaultAdapter();

    }

    /**
     * 是否支持蓝牙
     * @return true 支持，false 不支持
     */
    public boolean isSupportBlueTooth(){
        if (mAdaoter!=null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断蓝牙当前状态
     * @return  true 打开，false 关闭
     */
    public boolean getBulueToothStatus(){
        assert (mAdaoter != null);
        return  mAdaoter.isEnabled();
    }



}