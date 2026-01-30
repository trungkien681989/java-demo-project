package core.util.scripting.io;

import java.util.HashMap;

public class Clipboard {
    private static final HashMap<String, String> savedData = new HashMap<>();
    private final static Clipboard INSTANCE = new Clipboard();

    public static Clipboard copyTo(String key, String value) {
        savedData.put(key, value);
        return INSTANCE;
    }

    public static String paste(String key) {
        return savedData.get(key);
    }

    public static Clipboard clean() {
        savedData.clear();
        return INSTANCE;
    }

    public static Clipboard clean(String key) {
        savedData.remove(key);
        return INSTANCE;
    }

    public static String getActivatingActorPersonalDeviceId() {
        return paste(Key.KEY_ACTIVATING_ACTOR_ACTIVATING_PERSONAL_DEVICE_ID);
    }

    public static String getActivatingActorKioskDeviceId() {
        return paste(Key.KEY_ACTIVATING_ACTOR_ACTIVATING_KIOSK_DEVICE_ID);
    }

    public static Clipboard setUsingPersonalDevice(String deviceId) {
        copyTo(Key.KEY_ACTIVATING_ACTOR_ACTIVATING_PERSONAL_DEVICE_ID, deviceId);
        return INSTANCE;
    }

    public static Clipboard setUsingKiosk(String deviceId) {
        copyTo(Key.KEY_ACTIVATING_ACTOR_ACTIVATING_KIOSK_DEVICE_ID, deviceId);
        return INSTANCE;
    }

    public static final class Key {

        public static final String ACTIVATING_ACTOR = "core.util.scripting.io.Clipboard.ACTIVATING_ACTOR";
        public static final String ACTIVATING_KIOSK_DEVICE_ID = "ACTIVATING_KIOSK_DEVICE_ID";
        public static final String ACTIVATING_PERSONAL_DEVICE_ID = "ACTIVATING_PERSONAL_DEVICE_ID";
        public static String KEY_ACTIVATING_ACTOR_ACTIVATING_PERSONAL_DEVICE_ID = String.format("%s::%s",
                Clipboard.paste(Key.ACTIVATING_ACTOR), Key.ACTIVATING_PERSONAL_DEVICE_ID
        );
        public static String KEY_ACTIVATING_ACTOR_ACTIVATING_KIOSK_DEVICE_ID = String.format("%s::%s",
                Clipboard.paste(Key.ACTIVATING_ACTOR), Key.ACTIVATING_KIOSK_DEVICE_ID);
    }
}