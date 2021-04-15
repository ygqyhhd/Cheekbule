package com.meeblue.celsius;

import com.orm.SugarApp;
import com.orm.SugarContext;

public class App extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void init(){

    }
}
