package in.agrostar.ulink.clothpicker.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ayush on 23/4/17.
 */

public class Suggestion {
    @SerializedName("description")
    public  String description;

    @SerializedName("id")
    int id;

    @SerializedName("liked")
    boolean flag;

    @SerializedName("imageUrl")
    String imageUrl;

    public boolean isHasAlreadySeen() {
        return hasAlreadySeen;
    }

    public void setHasAlreadySeen(boolean hasAlreadySeen) {
        this.hasAlreadySeen = hasAlreadySeen;
    }

    @SerializedName("hasAlreadySeen")
    boolean hasAlreadySeen;


    public Suggestion() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
