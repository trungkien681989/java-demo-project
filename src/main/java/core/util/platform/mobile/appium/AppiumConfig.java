package core.util.platform.mobile.appium;

public enum AppiumConfig {
    APPIUM_CONFIG_FILE_PATH("core/appium/appium.yaml"),

    APPIUM_DIRECTORY_CONFIG_FILE_PATH("core/appium/appium-directory-%s.yaml"),

    NODE_JS_PATH("nodeJsPath"),
    APPIUM_JS_PATH("appiumJsPath"),
    ANDROID_COMMAND_TIMEOUT("androidCommandTimeout"),
    ANDROID_DEVICE_READY_TIMEOUT("androidDeviceReadyTimeout"),
    LIMIT_RETRY_TO_GET_ANDROID_DEVICE("limitRetryToGetAndroidDevice"),
    ANDROID_APP_PACKAGE("androidAppPackage"),
    ANDROID_APP_ACTIVITY("androidAppActivity");

    private final String key;

    AppiumConfig(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
