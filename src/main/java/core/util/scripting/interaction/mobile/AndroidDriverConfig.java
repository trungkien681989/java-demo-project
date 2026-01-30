package core.util.scripting.interaction.mobile;

public enum AndroidDriverConfig {
    ANDROID_DRIVER_CONFIG_FILE_PATH("core/appium/android/android-driver.yaml"),
    ANDROID_DRIVER_IMPLICITLY_WAIT("androidDriverImplicitlyWait"),
    ANDROID_ELEMENT_LIMIT_WAIT("androidElementLimitWait"),
    ANDROID_ELEMENT_POLLING_WAIT("androidElementPollingWait"),
    ANDROID_ELEMENT_MINI_WAIT("androidElementMiniWait");

    private final String key;

    AndroidDriverConfig(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
