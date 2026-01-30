package core.util.platform.mobile.android;

public enum ApplicationConfig {
    ANDROID_APPLICATION_CONFIG_FILE_PATH("core/appium/android/application.yaml"),
    IS_FORCE_TO_INSTALL_APK("isForceToInstallAPK");

    private final String key;

    ApplicationConfig(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
