package com.metova.cappuccino.animations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import android.content.Context;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SystemAnimationsTest {

    @Mock Context mockContext;
    @Mock PermissionChecker mockPermissionChecker;
    @Mock WindowManager mockWindowManager;

    private SystemAnimations mSystemAnimations = new SystemAnimations();

    private void grantPermission(boolean shouldGrantPermission) throws Exception {
        mSystemAnimations.setPermissionChecker(mockPermissionChecker);
        mSystemAnimations.setWindowManager(mockWindowManager);
        when(mockPermissionChecker.hasAnimationPermission(mockContext)).thenReturn(shouldGrantPermission);
    }

    @Test
    public void testPermissionDeniedThrowsException() throws Exception {
        // Setup
        grantPermission(false);

        // Exercise
        try {
            mSystemAnimations.checkPermissionStatus(mockContext);
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getLocalizedMessage(), is(SystemAnimations.getPermissionErrorMessage()));
        }
    }

    @Test
    public void testPermissionGrantedNothingHappens() throws Exception {
        // Setup
        grantPermission(true);

        // Exercise
        mSystemAnimations.checkPermissionStatus(mockContext);

        // Nothing happens is a pass
    }

    @Test
    public void testDisableAll() throws Exception {
        // Setup
        grantPermission(true);
        when(mockWindowManager.getAnimationScales()).thenReturn(new float[]{1f, 1f, 1f});

        // Exercise
        mSystemAnimations.disableAll();

        // Verify
        verify(mockWindowManager).getAnimationScales();
        verify(mockWindowManager).setAnimationScales(new float[]{0f, 0f, 0f});
    }

    @Test
    public void testEnableAll() throws Exception {
        // Setup
        grantPermission(true);
        when(mockWindowManager.getAnimationScales()).thenReturn(new float[]{0f, 0f, 0f});

        // Exercise
        mSystemAnimations.enableAll();

        // Verify
        verify(mockWindowManager).getAnimationScales();
        verify(mockWindowManager).setAnimationScales(new float[]{1f, 1f, 1f});
    }

    @Test
    public void testRestoreAll() throws Exception {
        // Setup
        grantPermission(true);
        when(mockWindowManager.getAnimationScales())
                .thenReturn(new float[]{2f, 0f, 0f})  // disabling from normal
                .thenReturn(new float[]{0f, 0f, 0f}); // restoring from disabled

        // Exercise
        mSystemAnimations.disableAll(); // sets all to 0
        mSystemAnimations.restoreAll(); // restores to {2, 0, 0}

        // Verify
        verify(mockWindowManager, times(2)).getAnimationScales();
        verify(mockWindowManager).setAnimationScales(new float[]{0f, 0f, 0f}); // on disable
        verify(mockWindowManager).setAnimationScales(new float[]{2f, 0f, 0f}); // on restore
    }

    @Test
    public void testRestoreAllFailsIfFirstCall() throws Exception {
        // Setup
        grantPermission(true);

        // Exercise
        try {
            mSystemAnimations.restoreAll();
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getLocalizedMessage(), is(SystemAnimations.getRestoreErrorMessage()));
        }

        // Verify
        verifyNoMoreInteractions(mockWindowManager);
    }

    @Test
    public void testSetScaleThrowsReflectionError() throws Exception {
        // Setup
        grantPermission(true);
        when(mockWindowManager.getAnimationScales()).thenThrow(new ClassNotFoundException());

        // Exercise
        try {
            mSystemAnimations.disableAll();
            fail();
        } catch (InternalError e) {
            assertThat(e.getLocalizedMessage(), is(SystemAnimations.getReflectionErrorMessage(0f)));
        }

        // Verify
        verify(mockWindowManager).getAnimationScales();
        verifyNoMoreInteractions(mockWindowManager);
    }
}