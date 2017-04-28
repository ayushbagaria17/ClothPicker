package in.agrostar.ulink.clothpicker.domain;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ayush on 28/4/17.
 */

public enum UploadType {
    SHIRT,
    TROUSER,
    UNKNOWN;

    private static final Map<String, UploadType> map;
    static {
        map = new HashMap<String, UploadType>();
        for (UploadType type : UploadType.values()) {
            map.put(type.toString(), type);
        }
    }

    public static UploadType getState(String typeAsString) {
        if (map.containsKey(typeAsString)) {
            return map.get(typeAsString);
        }

        Log.e("TransferState", "Unknown state " + typeAsString
                + " transfer will be have state set to UNKNOWN.");
        return UNKNOWN;
    }
}
