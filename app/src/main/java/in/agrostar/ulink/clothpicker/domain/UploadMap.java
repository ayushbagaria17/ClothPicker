package in.agrostar.ulink.clothpicker.domain;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by ayush on 28/4/17.
 */

public class UploadMap {

    @SerializedName("uploadMap")
    HashMap<Integer, UploadObject> uploadMap;

    public HashMap<Integer, UploadObject> getUploadMap() {
        return uploadMap;
    }

    public void setUploadMap(HashMap<Integer, UploadObject> uploadMap) {
        this.uploadMap = uploadMap;
    }

    public UploadMap() {

    }
}
