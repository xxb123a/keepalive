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
# 引用
allprojects {  
		repositories {  
			maven { url 'https://jitpack.io' }  
		}  
}    
dependencies {  
    implementation 'com.github.xxb123a:keepalive:v1.0.1'   
}
