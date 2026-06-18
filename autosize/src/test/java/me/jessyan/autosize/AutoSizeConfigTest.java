package me.jessyan.autosize;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AutoSizeConfigTest {

    @Test
    public void rejectsNonPositiveScreenSize() {
        assertFalse(AutoSizeConfig.isUsableScreenSize(0, 1080));
        assertFalse(AutoSizeConfig.isUsableScreenSize(1920, 0));
    }

    @Test
    public void rejectsTransientWindowSizeThatIsMuchSmallerThanKnownScreen() {
        assertFalse(AutoSizeConfig.shouldAcceptScreenSize(1920, 1080, 960, 540, true));
        assertFalse(AutoSizeConfig.shouldAcceptScreenSize(1920, 1080, 960, 1080, true));
    }

    @Test
    public void acceptsSmallerWindowWhenRawFallbackIsDisabled() {
        assertTrue(AutoSizeConfig.shouldAcceptScreenSize(1920, 1080, 960, 540, false));
    }

    @Test
    public void acceptsSameOrStableFullScreenSize() {
        assertTrue(AutoSizeConfig.shouldAcceptScreenSize(1920, 1080, 1920, 1080));
        assertTrue(AutoSizeConfig.shouldAcceptScreenSize(0, 0, 1920, 1080));
    }

    @Test
    public void normalizesTransientPortraitSizeForLandscapeActivity() {
        int[] screenSize = new int[]{1080, 2340};
        AutoSizeConfig.normalizeScreenSize(screenSize, false);
        assertTrue(screenSize[0] == 2340);
        assertTrue(screenSize[1] == 1080);
    }

    @Test
    public void appliesRawSizeOnlyWhenFallbackIsAllowed() {
        int[] smallerWindow = new int[]{960, 540};
        AutoSizeConfig.applyRawScreenSizeIfNeeded(smallerWindow, new int[]{1920, 1080}, false);
        assertEquals(960, smallerWindow[0]);
        assertEquals(540, smallerWindow[1]);

        int[] transientWindow = new int[]{960, 540};
        AutoSizeConfig.applyRawScreenSizeIfNeeded(transientWindow, new int[]{1920, 1080}, true);
        assertEquals(1920, transientWindow[0]);
        assertEquals(1080, transientWindow[1]);
    }
}
