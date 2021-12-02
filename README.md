# Keepalive--
    @Override
    public void onCreate() {
        super.onCreate();
        DaemonSDK.onCreate(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DaemonSDK.attachBaseContext(base);
    }
}
