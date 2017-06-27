package ru.finance2.simplecrmandroid;

public class Application extends android.app.Application {

    private static Application application;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static Application getInstance() {
        return application;
    }

    public MainDbHelper getMainDbHelper() {
        return MainDbHelper.getInstance(getApplicationContext());
    }

}