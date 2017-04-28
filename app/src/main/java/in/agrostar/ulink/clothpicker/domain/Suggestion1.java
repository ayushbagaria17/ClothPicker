package in.agrostar.ulink.clothpicker.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ayush on 28/4/17.
 */

public class Suggestion1 {

    @SerializedName("combination")
    List<UploadObject> uploadObjects;


    @SerializedName("id")
    int id;

    @SerializedName("alreadySeen")
    boolean alreadySeen;

    @SerializedName("liked")
    boolean flag;

    public Suggestion1() {

    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isAlreadySeen() {
        return alreadySeen;
    }

    public void setAlreadySeen(boolean alreadySeen) {
        this.alreadySeen = alreadySeen;
    }

    public void setUploadObjects(List<UploadObject> uploadObjects) {
        this.uploadObjects = uploadObjects;
    }

    public List<UploadObject> getUploadObjects() {
        return uploadObjects;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
