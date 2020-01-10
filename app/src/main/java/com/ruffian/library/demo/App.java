package com.ruffian.library.demo;

import android.app.Application;
import android.content.Context;

import com.letv.sarrsdesktop.blockcanaryex.jrt.BlockCanaryEx;
import com.letv.sarrsdesktop.blockcanaryex.jrt.Config;

public class App extends Application {

    @Override
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        if (!BlockCanaryEx.isInSamplerProcess(this)) {
            BlockCanaryEx.install(new Config(this));
        }
    }

}
