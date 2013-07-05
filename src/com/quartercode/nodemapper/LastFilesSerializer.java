
package com.quartercode.nodemapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.tree.Tree;
import com.quartercode.qcutil.io.File;

public class LastFilesSerializer {

    public static class LastFileEntry {

        private final File                        file;
        private final Class<? extends Serializer> serializer;
        private final Tree                        tree;

        public LastFileEntry(final File file, final Class<? extends Serializer> serializer, final Tree tree) {

            this.file = file;
            this.serializer = serializer;
            this.tree = tree;
        }

        public File getFile() {

            return file;
        }

        public Class<? extends Serializer> getSerializer() {

            return serializer;
        }

        public Tree getTree() {

            return tree;
        }

        @Override
        public String toString() {

            return tree.getName() + " - " + file.getPath();
        }
    }

    public static void save(final Map<File, String> lastFiles, final File file) throws IOException {

        final PrintWriter writer = new PrintWriter(file);

        for (final Entry<File, String> entry : lastFiles.entrySet()) {
            writer.println(entry.getKey().getAbsolutePath() + ";;;" + entry.getValue());
        }

        writer.flush();
        writer.close();
    }

    public static Map<File, String> load(final File file) throws IOException {

        final Map<File, String> lastFiles = new LinkedHashMap<File, String>();

        if (file.exists()) {
            final BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = "";
            while ( (line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    final String[] parts = line.split(";;;");
                    lastFiles.put(new File(parts[0]), parts[1]);
                }
            }

            reader.close();
        }

        return lastFiles;
    }

}