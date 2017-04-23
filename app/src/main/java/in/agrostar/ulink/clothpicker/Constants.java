package in.agrostar.ulink.clothpicker;

import com.amazonaws.auth.AWSCognitoIdentityProvider;

/**
 * Created by ayush on 20/4/17.
 */

public class Constants {
    public static final String LOGGER_TAG = "ClothPicker" ;
    public static final String PREFERENCES_FILENAME = "ClothPicker" ;
    public static final String IS_REGISTRATION_DONE = "isRegistrationDone";
    public static final String SUGGESTIONS = "suggestions";

    public static class KEY {
        public static final String FACEBOOK = "facebook";
        public static final String GOOGLE = "google";
        public static final String USER_TOKEN = "token" ;
        public static final String LOGIN_SOURCE = "loginSource" ;
        public static String userName = "userName";
    }

    public class VALUES {
        //AWS values
        public static final String COGNITO_POOL_ID = "us-east-1:f27a7438-726c-40c2-81fa-db6948550f78";
        public static final String BUCKET_NAME = "sampleproject-userfiles-mobilehub-636799597";    }
}
