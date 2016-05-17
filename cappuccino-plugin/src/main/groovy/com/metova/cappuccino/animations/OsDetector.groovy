package com.metova.cappuccino.animations

import org.apache.commons.lang3.SystemUtils

class OsDetector {
    boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS
    }
}