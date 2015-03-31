/*
 * This file is part of Nodemapper.
 * Copyright (c) 2013 QuarterCode <http://quartercode.com/>
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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.quartercode.nodemapper.ser.FileInput;
import com.quartercode.nodemapper.ser.Input;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.tree.Tree;

public class FileActionUtil {

    private static final Logger LOGGER = Logger.getLogger(FileActionUtil.class.getName());

    public static Tree loadTree(File file, Serializer serializer) {

        try (Input input = new FileInput(file)) {
            return serializer.deserialize(input);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can't deserialize tree from '" + file.getAbsolutePath() + "'", e);
        }

        return null;
    }

    public static JFileChooser createFileChooser(File lastDirectory) {

        JFileChooser fileChooser = new JFileChooser(lastDirectory);
        for (FileFilter fileFilter : fileChooser.getChoosableFileFilters()) {
            fileChooser.removeChoosableFileFilter(fileFilter);
        }
        return fileChooser;
    }

    public static File completeFile(File file, FileFilter fileFilter) {

        if (fileFilter instanceof FileNameExtensionFilter) {
            for (String extension : ((FileNameExtensionFilter) fileFilter).getExtensions()) {
                if (file.getName().endsWith("." + extension)) {
                    return file;
                }
            }

            return new File(file.getPath() + "." + ((FileNameExtensionFilter) fileFilter).getExtensions()[0]);
        } else {
            return file;
        }
    }

    private FileActionUtil() {

    }

}
