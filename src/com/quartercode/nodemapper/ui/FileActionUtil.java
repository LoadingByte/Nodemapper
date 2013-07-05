
package com.quartercode.nodemapper.ui;

import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.quartercode.nodemapper.Nodemapper;
import com.quartercode.nodemapper.ser.FileInput;
import com.quartercode.nodemapper.ser.Input;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.tree.Tree;
import com.quartercode.qcutil.io.File;

public class FileActionUtil {

    public static Tree loadTree(final File file, final Serializer serializer) {

        final Input input = new FileInput(file);
        try {
            return serializer.deserialize(input);
        }
        catch (final IOException e1) {
            Nodemapper.handle(e1);
        }
        finally {
            try {
                input.close();
            }
            catch (final IOException e1) {
                Nodemapper.handle(e1);
            }
        }

        return null;
    }

    public static JFileChooser createFileChooser(final File lastDirectory) {

        final JFileChooser fileChooser = new JFileChooser(lastDirectory);
        for (final FileFilter fileFilter : fileChooser.getChoosableFileFilters()) {
            fileChooser.removeChoosableFileFilter(fileFilter);
        }
        return fileChooser;
    }

    public static File completeFile(final File file, final FileFilter fileFilter) {

        if (fileFilter instanceof FileNameExtensionFilter) {
            for (final String extension : ((FileNameExtensionFilter) fileFilter).getExtensions()) {
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
