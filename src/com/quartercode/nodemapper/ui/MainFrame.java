
package com.quartercode.nodemapper.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import com.quartercode.nodemapper.Nodemapper;
import com.quartercode.nodemapper.ser.FileOutput;
import com.quartercode.nodemapper.ser.InternalSerializer;
import com.quartercode.nodemapper.ser.Output;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.ser.SerializerManager;
import com.quartercode.nodemapper.tree.Node;
import com.quartercode.nodemapper.tree.Tree;
import com.quartercode.nodemapper.tree.TreeUtil;
import com.quartercode.qcutil.Event;
import com.quartercode.qcutil.Listener;
import com.quartercode.qcutil.io.File;

@SuppressWarnings ("serial")
public class MainFrame extends JFrame {

    private JMenuBar        menuBar;
    private JMenu           fileMenu;
    private Action          saveAction;
    private Action          saveAsAction;
    private Action          exportAsAction;
    private JMenu           editMenu;
    private Action          renameAction;
    private JMenu           helpMenu;
    private final NodePanel nodePanel;

    private File            currentFile;
    private File            lastDirectory;

    public MainFrame() {

        setPreferredSize(new Dimension(1300, 900));
        setBounds(0, 0, 1300, 900);
        setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - getWidth() / 2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - getHeight() / 2));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent e) {

                if (closeTree()) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        nodePanel = new NodePanel();
        nodePanel.setStateManager(new StateManager());
        getContentPane().add(nodePanel);
        initalizeMenu();
        refreshTitle();

        nodePanel.addChangeListener(new Listener() {

            @Override
            public Object call(final Event event) {

                refreshTitle();
                return null;
            }
        });
    }

    private void initalizeMenu() {

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(new AbstractAction("Recent Maps...") {

            @Override
            public void actionPerformed(final ActionEvent e) {

                new LastFilesDialog(MainFrame.this).setVisible(true);
                repaint();
            }
        }));

        fileMenu.add(new JMenuItem(new AbstractAction("New...") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
            }

            @Override
            public void actionPerformed(final ActionEvent e) {

                if (closeTree()) {
                    final Object name = JOptionPane.showInputDialog(MainFrame.this, "Enter name for Map:", "New Map", JOptionPane.QUESTION_MESSAGE, null, null, "New Map");

                    if (name != null && !String.valueOf(name).isEmpty()) {
                        final Tree tree = new Tree(String.valueOf(name));
                        final Node rootNode = new Node(0, "Root");
                        TreeUtil.addNode(tree, rootNode);

                        setTree(tree);
                    } else {
                        JOptionPane.showMessageDialog(MainFrame.this, "This isn't a name!", "No Name", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }));

        fileMenu.add(new JMenuItem(new AbstractAction("Open...") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
            }

            @Override
            public void actionPerformed(final ActionEvent e) {

                if (closeTree()) {
                    final JFileChooser fileChooser = FileActionUtil.createFileChooser(lastDirectory);
                    fileChooser.setMultiSelectionEnabled(false);
                    for (final Serializer serializer : SerializerManager.getSerializers(true)) {
                        fileChooser.addChoosableFileFilter(SerializerManager.getFileFilter(serializer));
                    }
                    fileChooser.setFileFilter(SerializerManager.getFileFilter(SerializerManager.getSerializers(true).get(0)));

                    if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                        final Serializer serializer = SerializerManager.getSerializer(fileChooser.getFileFilter());
                        openTree(File.convert(fileChooser.getSelectedFile()), serializer);
                    }
                }
            }
        }));

        saveAction = new AbstractAction("Save") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
                setEnabled(false);
            }

            @Override
            public void actionPerformed(final ActionEvent e) {

                if (currentFile == null) {
                    openSaveDialog(SerializerManager.getSerializers(true));
                } else {
                    saveTo(currentFile, SerializerManager.getSerializers(true).get(0));
                }
            }
        };
        fileMenu.add(saveAction);

        saveAsAction = new AbstractAction("Save as...") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
                setEnabled(false);
            }

            @Override
            public void actionPerformed(final ActionEvent e) {

                openSaveDialog(SerializerManager.getSerializers(true));
            }
        };
        fileMenu.add(saveAsAction);

        exportAsAction = new AbstractAction("Export...") {

            {
                setEnabled(false);
            }

            @Override
            public void actionPerformed(final ActionEvent e) {

                openSaveDialog(SerializerManager.getSerializers(false));
            }
        };
        fileMenu.add(exportAsAction);

        editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        final AbstractAction undoAction = new AbstractAction("Undo") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
                setEnabled(false);
            }

            @Override
            public void actionPerformed(final ActionEvent e) {

                nodePanel.setTree(nodePanel.getStateManager().rollState(-1));
                nodePanel.repaint();
            }
        };
        editMenu.add(undoAction);

        final AbstractAction redoAction = new AbstractAction("Redo") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
                setEnabled(false);
            }

            @Override
            public void actionPerformed(final ActionEvent e) {

                nodePanel.setTree(nodePanel.getStateManager().rollState(1));
                nodePanel.repaint();
            }
        };
        editMenu.add(redoAction);

        final AbstractAction resetViewAction = new AbstractAction("Reset View") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
            }

            @Override
            public void actionPerformed(final ActionEvent e) {

                nodePanel.resetView();
            }
        };
        editMenu.add(resetViewAction);

        nodePanel.getStateManager().addListener(new Listener() {

            @Override
            public Object call(final Event event) {

                undoAction.setEnabled(nodePanel.getStateManager().getIndex() > 0);
                redoAction.setEnabled(nodePanel.getStateManager().getIndex() < nodePanel.getStateManager().getStates().size() - 1);

                return null;
            }
        });

        renameAction = new AbstractAction("Rename") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
                setEnabled(false);
            }

            @Override
            public void actionPerformed(final ActionEvent e) {

                final Object name = JOptionPane.showInputDialog(MainFrame.this, "Enter name for Map:", "Rename Map", JOptionPane.QUESTION_MESSAGE, null, null, nodePanel.getTree().getName());

                if (name != null && !String.valueOf(name).isEmpty()) {
                    nodePanel.getTree().setName(String.valueOf(name));
                    nodePanel.onChange();
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "This isn't a name!", "No Name", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        editMenu.add(renameAction);

        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        helpMenu.add(new JMenuItem(new AbstractAction("About...") {

            @Override
            public void actionPerformed(final ActionEvent e) {

                new AboutDialog(MainFrame.this).setVisible(true);
            }
        }));
    }

    private void refreshTitle() {

        setTitle(Nodemapper.NAME + " " + Nodemapper.VERSION + " - by " + Nodemapper.CREATOR + " - " + (nodePanel.getTree() == null ? "No Document" : nodePanel.getTree().getName() + (nodePanel.isChanged() ? " *" : "")));
    }

    public void setTree(final Tree tree) {

        nodePanel.setTree(tree);
        nodePanel.getStateManager().clearStates();
        nodePanel.setChanged(false);
        nodePanel.resetView();

        if (tree != null) {
            TreeUtil.cleanupNodes(nodePanel.getTree());
            nodePanel.getStateManager().addState(nodePanel.getTree().clone());
        }

        saveAction.setEnabled(tree != null);
        saveAsAction.setEnabled(tree != null);
        exportAsAction.setEnabled(tree != null);
        renameAction.setEnabled(tree != null);
        nodePanel.repaint();
    }

    public void openTree(final File file, final Serializer serializer) {

        openTree(file, serializer, FileActionUtil.loadTree(file, serializer));
    }

    public void openTree(final File file, final Serializer serializer, final Tree tree) {

        currentFile = file;
        lastDirectory = File.convert(file.getParentFile());
        Nodemapper.addLastFile(file, serializer, nodePanel.getTree());

        setTree(tree);
    }

    public boolean closeTree() {

        if (nodePanel.getTree() != null && nodePanel.isChanged()) {
            final int result = JOptionPane.showConfirmDialog(this, "Save before closing?", "Close", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (result == 0) {
                if (currentFile == null) {
                    openSaveDialog(SerializerManager.getSerializers(true));
                } else {
                    saveTo(currentFile, SerializerManager.getSerializers(true).get(0));
                }
            }

            if (result == 0 || result == 1) {
                setTree(null);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void openSaveDialog(final List<Serializer> serializers) {

        final JFileChooser fileChooser = FileActionUtil.createFileChooser(lastDirectory);
        fileChooser.setMultiSelectionEnabled(false);
        for (final Serializer serializer : serializers) {
            fileChooser.addChoosableFileFilter(SerializerManager.getFileFilter(serializer));
        }
        fileChooser.setFileFilter(SerializerManager.getFileFilter(serializers.get(0)));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            lastDirectory = File.convert(fileChooser.getCurrentDirectory());
            final File file = FileActionUtil.completeFile(File.convert(fileChooser.getSelectedFile()), fileChooser.getFileFilter());
            if (SerializerManager.getSerializer(fileChooser.getFileFilter()).getClass().isAnnotationPresent(InternalSerializer.class)) {
                currentFile = file;
            }
            saveTo(file, SerializerManager.getSerializer(fileChooser.getFileFilter()));
        }
    }

    private void saveTo(final File file, final Serializer serializer) {

        final Output output = new FileOutput(file);
        try {
            TreeUtil.cleanupNodes(nodePanel.getTree());
            serializer.serialize(nodePanel.getTree().clone(), output);
            nodePanel.setChanged(false);

            if (serializer.getClass().isAnnotationPresent(InternalSerializer.class)) {
                Nodemapper.addLastFile(file, serializer, nodePanel.getTree());
            }
        }
        catch (final Throwable e) {
            Nodemapper.handle(e);
        }
        finally {
            try {
                output.close();
            }
            catch (final IOException e1) {
                Nodemapper.handle(e1);
            }
        }
    }

    public NodePanel getNodePanel() {

        return nodePanel;
    }

}
