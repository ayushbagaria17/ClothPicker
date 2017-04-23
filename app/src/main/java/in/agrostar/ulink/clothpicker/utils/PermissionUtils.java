package in.agrostar.ulink.clothpicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 20/4/17.
 */

public class PermissionUtils {

    public static void askForPermissions(int REQUEST_CODE_ASK_PERMISSIONS, Context context, List<String> permissions) {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        for (String permission: permissions) {
            if (!addPermission(permissionsList, permission, context))

                //Currently I am not showing any messgae to user that why I need a particular permission
                permissionsNeeded.add("Need permissions");
        }
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                //  String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 0; i < permissionsNeeded.size(); i++)
                    //      message = message + ", " + permissionsNeeded.get(i);
//                showMessageOkCancel(message, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
                    ActivityCompat.requestPermissions((Activity) context, permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_PERMISSIONS);
//                    }
//                });
                return;
            }
            ActivityCompat.requestPermissions((Activity)context, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
    }

    public static boolean addPermission(List<String> permissionsList, String permission, Context context) {
        if (ActivityCompat.checkSelfPermission((Activity)context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, permission))
                return false;
        }
        return true;
    }



}
