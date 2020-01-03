package com.ruffian.library.demo;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    @Override
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        /*if (!BlockCanaryEx.isInSamplerProcess(this)) {
            BlockCanaryEx.install(new Config(this));
        }*/
    }

}
