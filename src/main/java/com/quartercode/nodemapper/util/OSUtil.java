/*
 * This file is part of Nodemapper.
 * Copyright (c) 2013 QuarterCode <http://www.quartercode.com/>
 *
 * Nodemapper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nodemapper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nodemapper. If not, see <http://www.gnu.org/licenses/>.
 */

package com.quartercode.nodemapper.util;

import java.io.File;

public class OSUtil {

    public enum OperatingSystem {

        WINDOWS, OS_X, LINUX_UNIX, SOLARIS, OTHER;
    }

    public enum BitArchitecture {

        EIGHT_BIT (8), SIXTEEN_BIT (16), THIRTYTWO_BIT (32), SIXTYFOUR_BIT (64), OTHER (-1);

        public final int bits;

        BitArchitecture(final int bits) {

            this.bits = bits;
        }

        public int getBits() {

            return bits;
        }
    }

    public static OperatingSystem getOperatingSystem() {

        final String name = System.getProperty("os.name");

        if (name.toLowerCase().contains("win")) {
            return OperatingSystem.WINDOWS;
        } else if (name.toLowerCase().contains("mac") || name.toLowerCase().contains("darwin")) {
            return OperatingSystem.OS_X;
        } else if (name.toLowerCase().contains("linux") || name.toLowerCase().contains("unix")) {
            return OperatingSystem.LINUX_UNIX;
        } else if (name.toLowerCase().contains("solaris") || name.toLowerCase().contains("sunos")) {
            return OperatingSystem.SOLARIS;
        } else {
            return OperatingSystem.OTHER;
        }
    }

    public static String getArchitectureName() throws NumberFormatException {

        return System.getProperty("os.arch");
    }

    public static BitArchitecture getBitArchitecture() throws NumberFormatException {

        final int bits = Integer.parseInt(System.getProperty("sun.arch.data.model"));
        for (final BitArchitecture architecture : BitArchitecture.values()) {
            if (bits == architecture.getBits()) {
                return architecture;
            }
        }

        return BitArchitecture.OTHER;
    }

    public static File getDataDir() {

        final OperatingSystem system = getOperatingSystem();

        if (system == OperatingSystem.WINDOWS && System.getenv("appdata") != null) {
            return new File(System.getenv("appdata"));
        } else if (system == OperatingSystem.OS_X) {
            return new File(new File(System.getProperty("user.home", ".")), "Library/Application Support");
        } else {
            return new File(System.getProperty("user.home", "."));
        }
    }

    private OSUtil() {

    }

}
