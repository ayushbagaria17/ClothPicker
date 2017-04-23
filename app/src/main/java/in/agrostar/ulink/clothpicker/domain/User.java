package in.agrostar.ulink.clothpicker.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ayush on 20/4/17.
 */

public class User {

    @SerializedName("LoginSource")
    String loginSource;

    @SerializedName("accesstoken")
    String accessToken;

    public User() {

    }

    public String getLoginSource() {
        return loginSource;
    }

    public void setLoginSource(String loginSource) {
        this.loginSource = loginSource;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


}
