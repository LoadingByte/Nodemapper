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
import javax.swing.JOptionPane;
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
import com.quartercode.nodemapper.ui.GeneralException;
import com.quartercode.nodemapper.ui.LastFilesDialog;
import com.quartercode.nodemapper.ui.MainFrame;
import com.quartercode.nodemapper.util.OSUtil;

public class Main {

    private static File                dir;
    private static MainFrame           mainFrame;
    private static List<LastFileEntry> lastFiles = new ArrayList<LastFileEntry>();

    public static void main(String[] args) {

        dir = new File(OSUtil.getDataDir(), ".nodemapper");
        dir.mkdirs();

        InternalReferenceSerializer.register();
        ReferenceXMLSerializer.register();
        DialogueSerializer.register();

        try {
            for (Entry<File, String> entry : LastFilesSerializer.load(new File(dir, "last.txt")).entrySet()) {
                if (entry.getKey().exists()) {
                    // If the given class happens not to be a serializer class, the check afterwards will catch the error
                    @SuppressWarnings ("unchecked")
                    Class<? extends Serializer> serializer = (Class<? extends Serializer>) Class.forName(entry.getValue());
                    if (!Serializer.class.isAssignableFrom(serializer)) {
                        // Ignore entries with wrong serializers
                        continue;
                    } else {
                        lastFiles.add(new LastFileEntry(entry.getKey(), serializer, FileActionUtil.loadTree(entry.getKey(), SerializerManager.getSerializer(serializer))));
                    }
                }
            }
        } catch (Exception e) {
            handle(e);
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
            List<LastFileEntry> newLastFiles = new ArrayList<LastFileEntry>();
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

        for (LastFileEntry entry : new ArrayList<LastFileEntry>(lastFiles)) {
            if (entry.getFile().equals(file)) {
                lastFiles.remove(entry);
            }
        }

        lastFiles.add(new LastFileEntry(file, serializer.getClass(), tree));

        try {
            Map<File, String> data = new LinkedHashMap<File, String>();
            for (LastFileEntry entry : lastFiles) {
                data.put(entry.getFile(), entry.getSerializer().getName());
            }
            LastFilesSerializer.save(data, new File(dir, "last.txt"));
        } catch (IOException e) {
            handle(e);
        }
    }

    public static void clearLastFiles() {

        lastFiles.clear();

        try {
            Map<File, String> data = new LinkedHashMap<File, String>();
            for (LastFileEntry entry : lastFiles) {
                data.put(entry.getFile(), entry.getSerializer().getName());
            }
            LastFilesSerializer.save(data, new File(dir, "last.txt"));
        } catch (IOException e) {
            handle(e);
        }
    }

    public static void handle(Throwable t) {

        if (! (t instanceof GeneralException)) {
            System.err.println("An error occurred:");
            t.printStackTrace();
        }

        if (mainFrame != null) {
            JOptionPane.showMessageDialog(mainFrame, "An error occurred:\n" + t + (t instanceof GeneralException ? "\nLook at the console for more information." : ""), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Main() {

    }

}
