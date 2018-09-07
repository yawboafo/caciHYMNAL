package com.truelight.cacihymn;


public class Application extends android.app.Application{




    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;



    }

    public static synchronized Application getInstance() {
        return mInstance;
    }


}
