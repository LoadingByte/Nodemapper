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

package com.quartercode.nodemapper.ui;

import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JOptionPane;
import com.quartercode.nodemapper.Main;

public class GUIExceptionHandler extends Handler {

    public GUIExceptionHandler() {

    }

    @Override
    public void publish(LogRecord record) {

        if (record.getLevel() == Level.SEVERE) {

            StringBuilder message = new StringBuilder();
            message.append("An unexpected error occurred:\n");
            if (record.getMessage() != null) {
                message.append(record.getMessage());
            } else if (record.getThrown().getMessage() != null) {
                message.append(record.getThrown().getMessage());
            } else {
                message.append("No message provided; see stack trace for more details");
            }
            message.append("\n");
            message.append("Please view the log under '" + new File(Main.getDir(), "nodemapper.log").getAbsolutePath() + "' and report the error!");

            JOptionPane.showMessageDialog(null, message.toString(), "Unexpected Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void flush() {

        // Unused
    }

    @Override
    public void close() throws SecurityException {

        // Unused
    }

}
