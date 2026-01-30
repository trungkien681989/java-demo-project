package core.util.platform.android.application;

public enum AndroidApplicationConfig {
    ANDROID_APPLICATION_CONFIG_FILE_PATH("core/android/application.yaml"),
    IS_FORCE_TO_INSTALL_APK("isForceToInstallAPK");

    private String key;

    AndroidApplicationConfig(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
