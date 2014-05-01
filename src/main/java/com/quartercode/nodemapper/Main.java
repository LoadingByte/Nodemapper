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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import com.quartercode.nodemapper.LastFilesSerializer.LastFileEntry;
import com.quartercode.nodemapper.gr.render.DarkRenderer;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.ser.SerializerManager;
import com.quartercode.nodemapper.ser.types.DialogueSerializer;
import com.quartercode.nodemapper.ser.types.InternalReferenceSerializer;
import com.quartercode.nodemapper.ser.types.ReferenceXMLSerializer;
import com.quartercode.nodemapper.tree.Tree;
import com.quartercode.nodemapper.ui.FileActionUtil;
import com.quartercode.nodemapper.ui.LastFilesDialog;
import com.quartercode.nodemapper.ui.MainFrame;

public class Main {

    private static final Logger        LOGGER    = Logger.getLogger(Main.class.getName());

    private static File                dir;
    private static File                lastFilesFile;
    private static MainFrame           mainFrame;
    private static List<LastFileEntry> lastFiles = new ArrayList<>();

    public static void main(String[] args) {

        dir = new File(System.getProperty("user.home"), ".nodemapper");
        dir.mkdirs();

        // Load logging configuration
        try {
            LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/config/logging.properties"));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Can't load logging configuration", e);
            return;
        }

        LOGGER.info("Starting up ...");

        lastFilesFile = new File(dir, "last.txt");

        InternalReferenceSerializer.register();
        ReferenceXMLSerializer.register();
        DialogueSerializer.register();

        try {
            for (Entry<File, String> entry : LastFilesSerializer.load(lastFilesFile).entrySet()) {
                if (entry.getKey().exists()) {
                    // If the given class happens not to be a serializer class, the check afterwards will catch the error
                    @SuppressWarnings ("unchecked")
                    Class<? extends Serializer> serializer = (Class<? extends Serializer>) Class.forName(entry.getValue());
                    if (!Serializer.class.isAssignableFrom(serializer)) {
                        // Ignore entries caused by invalid serializers
                        continue;
                    } else {
                        lastFiles.add(new LastFileEntry(entry.getKey(), serializer, FileActionUtil.loadTree(entry.getKey(), SerializerManager.getSerializer(serializer))));
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can't read the file storing the lastly opened files ('" + lastFilesFile.getAbsolutePath() + "')", e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Unknown serializer: " + e.getMessage(), e);
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                mainFrame = new MainFrame();
                mainFrame.getNodePanel().setRenderer(new DarkRenderer());
                mainFrame.setVisible(true);
                new LastFilesDialog(mainFrame).setVisible(true);
            }
        });

        LOGGER.info("Successfully started up");
    }

    public static String getTitle() {

        return Main.class.getPackage().getImplementationTitle();
    }

    public static String getVersion() {

        return Main.class.getPackage().getImplementationVersion();
    }

    public static String getVendor() {

        return Main.class.getPackage().getImplementationVendor();
    }

    public static File getDir() {

        return dir;
    }

    public static List<LastFileEntry> getLastFiles() {

        return lastFiles;
    }

    public static void addLastFile(File file, Serializer serializer, Tree tree) {

        if (lastFiles.size() >= 10) {
            List<LastFileEntry> newLastFiles = new ArrayList<>();
            int counter = 0;
            for (LastFileEntry entry : lastFiles) {
                newLastFiles.add(entry);
                counter++;
                if (counter >= 10) {
                    break;
                }
            }

            lastFiles = newLastFiles;
        }

        for (LastFileEntry entry : new ArrayList<>(lastFiles)) {
            if (entry.getFile().equals(file)) {
                lastFiles.remove(entry);
            }
        }

        lastFiles.add(new LastFileEntry(file, serializer.getClass(), tree));

        try {
            Map<File, String> data = new LinkedHashMap<>();
            for (LastFileEntry entry : lastFiles) {
                data.put(entry.getFile(), entry.getSerializer().getName());
            }
            LastFilesSerializer.save(data, lastFilesFile);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can't write the file storing the lastly opened files ('" + lastFilesFile.getAbsolutePath() + "')", e);
        }
    }

    public static void clearLastFiles() {

        lastFiles.clear();

        try {
            Map<File, String> data = new LinkedHashMap<>();
            for (LastFileEntry entry : lastFiles) {
                data.put(entry.getFile(), entry.getSerializer().getName());
            }
            LastFilesSerializer.save(data, lastFilesFile);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can't write the file storing the lastly opened files ('" + lastFilesFile.getAbsolutePath() + "')", e);
        }
    }

    private Main() {

    }

}
