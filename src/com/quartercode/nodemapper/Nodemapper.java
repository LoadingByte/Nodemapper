
package com.quartercode.nodemapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
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
import com.quartercode.qcutil.args.Arguments;
import com.quartercode.qcutil.io.File;
import com.quartercode.qcutil.utility.OSUtil;

public class Nodemapper {

    public static final String         NAME      = "Nodemapper";
    public static final String         CREATOR   = "QuarterCode";
    public static final String         VERSION   = "1.1";

    private static File                dir;
    private static MainFrame           mainFrame;
    private static List<LastFileEntry> lastFiles = new ArrayList<LastFileEntry>();

    @SuppressWarnings ("unchecked")
    public static void main(final String[] args) {

        final Arguments arguments = new Arguments(args);
        if (arguments.isParameterSet("dir", true)) {
            dir = new File(arguments.getParameter("dir", true).replaceAll("%appdata%", Matcher.quoteReplacement(OSUtil.getDataDir().getPath())));
        } else {
            dir = new File(OSUtil.getDataDir(), "QuarterCode/Nodemapper");
        }
        dir.mkdirs();

        InternalReferenceSerializer.register();
        ReferenceXMLSerializer.register();
        DialogueSerializer.register();

        try {
            for (final Entry<File, String> entry : LastFilesSerializer.load(new File(dir, "last.txt")).entrySet()) {
                if (entry.getKey().exists()) {
                    final Class<? extends Serializer> serializer = (Class<? extends Serializer>) Class.forName(entry.getValue());
                    lastFiles.add(new LastFileEntry(entry.getKey(), serializer, FileActionUtil.loadTree(entry.getKey(), SerializerManager.getSerializer(serializer))));
                }
            }
        }
        catch (final Exception e) {
            handle(e);
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                try {
                    // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    mainFrame = new MainFrame();
                    mainFrame.getNodePanel().setRenderer(new DarkRenderer());
                    mainFrame.setVisible(true);
                    new LastFilesDialog(mainFrame).setVisible(true);
                }
                catch (final Throwable t) {
                    handle(t);
                }
            }
        });
    }

    public static File getDir() {

        return dir;
    }

    public static List<LastFileEntry> getLastFiles() {

        return lastFiles;
    }

    public static void addLastFile(final File file, final Serializer serializer, final Tree tree) {

        if (lastFiles.size() >= 10) {
            final List<LastFileEntry> newLastFiles = new ArrayList<LastFileEntry>();
            int counter = 0;
            for (final LastFileEntry entry : lastFiles) {
                newLastFiles.add(entry);
                counter++;
                if (counter >= 10) {
                    break;
                }
            }

            lastFiles = newLastFiles;
        }

        for (final LastFileEntry entry : new ArrayList<LastFileEntry>(lastFiles)) {
            if (entry.getFile().equals(file)) {
                lastFiles.remove(entry);
            }
        }

        lastFiles.add(new LastFileEntry(file, serializer.getClass(), tree));

        try {
            final Map<File, String> data = new LinkedHashMap<File, String>();
            for (final LastFileEntry entry : lastFiles) {
                data.put(entry.getFile(), entry.getSerializer().getName());
            }
            LastFilesSerializer.save(data, new File(dir, "last.txt"));
        }
        catch (final IOException e) {
            handle(e);
        }
    }

    public static void clearLastFiles() {

        lastFiles.clear();

        try {
            final Map<File, String> data = new LinkedHashMap<File, String>();
            for (final LastFileEntry entry : lastFiles) {
                data.put(entry.getFile(), entry.getSerializer().getName());
            }
            LastFilesSerializer.save(data, new File(dir, "last.txt"));
        }
        catch (final IOException e) {
            handle(e);
        }
    }

    public static void handle(final Throwable t) {

        if (! (t instanceof GeneralException)) {
            System.err.println("An error occurred:");
            t.printStackTrace();
        }

        if (mainFrame != null) {
            JOptionPane.showMessageDialog(mainFrame, "An error occurred:\n" + t + (t instanceof GeneralException ? "\nLook at the console for more information." : ""), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Nodemapper() {

    }

}
