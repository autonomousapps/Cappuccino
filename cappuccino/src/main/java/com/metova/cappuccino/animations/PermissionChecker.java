package com.metova.cappuccino.animations;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

public class PermissionChecker {
    @VisibleForTesting
    static final String ANIMATION_PERMISSION = Manifest.permission.SET_ANIMATION_SCALE;//"android.permission.SET_ANIMATION_SCALE";

    public boolean hasAnimationPermission(@NonNull Context context) {
        return context.checkCallingOrSelfPermission(ANIMATION_PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }
}