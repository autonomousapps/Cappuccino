package com.metova.cappuccino.animations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import android.content.Context;
import android.content.pm.PackageManager;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionCheckerTest {

    @Mock Context mockContext;

    private PermissionChecker mPermissionChecker = new PermissionChecker();

    @Test
    public void testHasAnimationPermissionShouldReturnTrue() throws Exception {
        // Setup
        when(mockContext.checkCallingOrSelfPermission(PermissionChecker.ANIMATION_PERMISSION)).thenReturn(PackageManager.PERMISSION_GRANTED);

        animationPermissionStatus(true);
    }

    @Test
    public void testHasAnimationPermissionShouldReturnFalse() throws Exception {
        // Setup
        when(mockContext.checkCallingOrSelfPermission(PermissionChecker.ANIMATION_PERMISSION)).thenReturn(PackageManager.PERMISSION_DENIED);

        animationPermissionStatus(false);
    }

    private void animationPermissionStatus(boolean isGranted) {
        // Exercise
        boolean actual = mPermissionChecker.hasAnimationPermission(mockContext);

        // Verify
        assertThat(actual, is(isGranted));
    }
}