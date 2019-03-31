package com.psut.pool.Shared;

import android.app.Activity;
import android.content.Context;

public interface MapsPermissions {
    Boolean getLocationPermission(Activity activity, Context context);

    void intitMap(Context context);
}
