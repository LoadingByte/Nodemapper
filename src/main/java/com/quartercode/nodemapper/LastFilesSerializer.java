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

package com.quartercode.nodemapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.tree.Tree;

public class LastFilesSerializer {

    private static final String SEPERATOR = ":::";

    public static class LastFileEntry {

        private final File                        file;
        private final Class<? extends Serializer> serializer;
        private final Tree                        tree;

        public LastFileEntry(File file, Class<? extends Serializer> serializer, Tree tree) {

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

    public static void save(Map<File, String> lastFiles, File file) throws IOException {

        PrintWriter writer = new PrintWriter(file);

        for (Entry<File, String> entry : lastFiles.entrySet()) {
            writer.println(entry.getKey().getAbsolutePath() + SEPERATOR + entry.getValue());
        }

        writer.flush();
        writer.close();
    }

    public static Map<File, String> load(File file) throws IOException {

        Map<File, String> lastFiles = new LinkedHashMap<File, String>();

        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = "";
            while ( (line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(SEPERATOR);
                    lastFiles.put(new File(parts[0]), parts[1]);
                }
            }

            reader.close();
        }

        return lastFiles;
    }

    private LastFilesSerializer() {

    }

}
