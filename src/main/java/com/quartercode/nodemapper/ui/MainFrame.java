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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import com.quartercode.nodemapper.Main;
import com.quartercode.nodemapper.ser.FileOutput;
import com.quartercode.nodemapper.ser.InternalSerializer;
import com.quartercode.nodemapper.ser.Output;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.ser.SerializerManager;
import com.quartercode.nodemapper.tree.Node;
import com.quartercode.nodemapper.tree.Tree;
import com.quartercode.nodemapper.tree.TreeUtil;

@SuppressWarnings ("serial")
public class MainFrame extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(MainFrame.class.getName());

    private Action              saveAction;
    private Action              saveAsAction;
    private Action              exportAsAction;
    private Action              renameAction;
    private final NodePanel     nodePanel;

    private File                currentFile;
    private File                lastDirectory;

    public MainFrame() {

        setPreferredSize(new Dimension(1300, 900));
        setBounds(0, 0, 1300, 900);
        setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - getWidth() / 2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - getHeight() / 2));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

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

        nodePanel.addChangeListener(new Runnable() {

            @Override
            public void run() {

                refreshTitle();
            }
        });
    }

    private void initalizeMenu() {

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(new AbstractAction("Recent Maps...") {

            @Override
            public void actionPerformed(ActionEvent e) {

                new LastFilesDialog(MainFrame.this).setVisible(true);
                repaint();
            }
        }));

        fileMenu.add(new JMenuItem(new AbstractAction("New...") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
            }

            @Override
            public void actionPerformed(ActionEvent e) {

                if (closeTree()) {
                    Object name = JOptionPane.showInputDialog(MainFrame.this, "Enter name for Map:", "New Map", JOptionPane.QUESTION_MESSAGE, null, null, "New Map");

                    if (name != null && !String.valueOf(name).isEmpty()) {
                        Tree tree = new Tree(String.valueOf(name));
                        Node rootNode = new Node(0, "Root");
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
            public void actionPerformed(ActionEvent e) {

                if (closeTree()) {
                    JFileChooser fileChooser = FileActionUtil.createFileChooser(lastDirectory);
                    fileChooser.setMultiSelectionEnabled(false);
                    for (Serializer serializer : SerializerManager.getSerializers(true)) {
                        fileChooser.addChoosableFileFilter(SerializerManager.getFileFilter(serializer));
                    }
                    fileChooser.setFileFilter(SerializerManager.getFileFilter(SerializerManager.getSerializers(true).get(0)));

                    if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                        Serializer serializer = SerializerManager.getSerializer(fileChooser.getFileFilter());
                        openTree(fileChooser.getSelectedFile(), serializer);
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
            public void actionPerformed(ActionEvent e) {

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
            public void actionPerformed(ActionEvent e) {

                openSaveDialog(SerializerManager.getSerializers(true));
            }
        };
        fileMenu.add(saveAsAction);

        exportAsAction = new AbstractAction("Export...") {

            {
                setEnabled(false);
            }

            @Override
            public void actionPerformed(ActionEvent e) {

                openSaveDialog(SerializerManager.getSerializers(false));
            }
        };
        fileMenu.add(exportAsAction);

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        final AbstractAction undoAction = new AbstractAction("Undo") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
                setEnabled(false);
            }

            @Override
            public void actionPerformed(ActionEvent e) {

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
            public void actionPerformed(ActionEvent e) {

                nodePanel.setTree(nodePanel.getStateManager().rollState(1));
                nodePanel.repaint();
            }
        };
        editMenu.add(redoAction);

        AbstractAction resetViewAction = new AbstractAction("Reset View") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
            }

            @Override
            public void actionPerformed(ActionEvent e) {

                nodePanel.resetView();
            }
        };
        editMenu.add(resetViewAction);

        nodePanel.getStateManager().addListener(new Runnable() {

            @Override
            public void run() {

                undoAction.setEnabled(nodePanel.getStateManager().getIndex() > 0);
                redoAction.setEnabled(nodePanel.getStateManager().getIndex() < nodePanel.getStateManager().getStates().size() - 1);
            }
        });

        renameAction = new AbstractAction("Rename") {

            {
                putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
                setEnabled(false);
            }

            @Override
            public void actionPerformed(ActionEvent e) {

                Object name = JOptionPane.showInputDialog(MainFrame.this, "Enter name for Map:", "Rename Map", JOptionPane.QUESTION_MESSAGE, null, null, nodePanel.getTree().getName());

                if (name != null && !String.valueOf(name).isEmpty()) {
                    nodePanel.getTree().setName(String.valueOf(name));
                    nodePanel.onChange();
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "This isn't a name!", "No Name", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        editMenu.add(renameAction);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        helpMenu.add(new JMenuItem(new AbstractAction("About...") {

            @Override
            public void actionPerformed(ActionEvent e) {

                new AboutDialog(MainFrame.this).setVisible(true);
            }
        }));
    }

    private void refreshTitle() {

        setTitle(Main.getTitle() + " " + Main.getVersion() + " - by " + Main.getVendor() + " - " + (nodePanel.getTree() == null ? "No Document" : nodePanel.getTree().getName() + (nodePanel.isChanged() ? " *" : "")));
    }

    public void setTree(Tree tree) {

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

    public void openTree(File file, Serializer serializer) {

        openTree(file, serializer, FileActionUtil.loadTree(file, serializer));
    }

    public void openTree(File file, Serializer serializer, Tree tree) {

        currentFile = file;
        lastDirectory = file.getParentFile();
        Main.addLastFile(file, serializer, nodePanel.getTree());

        setTree(tree);
    }

    public boolean closeTree() {

        if (nodePanel.getTree() != null && nodePanel.isChanged()) {
            int result = JOptionPane.showConfirmDialog(this, "Save before closing?", "Close", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

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

    private void openSaveDialog(List<Serializer> serializers) {

        JFileChooser fileChooser = FileActionUtil.createFileChooser(lastDirectory);
        fileChooser.setMultiSelectionEnabled(false);
        for (Serializer serializer : serializers) {
            fileChooser.addChoosableFileFilter(SerializerManager.getFileFilter(serializer));
        }
        fileChooser.setFileFilter(SerializerManager.getFileFilter(serializers.get(0)));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            lastDirectory = fileChooser.getCurrentDirectory();
            File file = FileActionUtil.completeFile(fileChooser.getSelectedFile(), fileChooser.getFileFilter());
            if (SerializerManager.getSerializer(fileChooser.getFileFilter()).getClass().isAnnotationPresent(InternalSerializer.class)) {
                currentFile = file;
            }
            saveTo(file, SerializerManager.getSerializer(fileChooser.getFileFilter()));
        }
    }

    private void saveTo(File file, Serializer serializer) {

        try (Output output = new FileOutput(file)) {
            TreeUtil.cleanupNodes(nodePanel.getTree());
            serializer.serialize(nodePanel.getTree().clone(), output);
            nodePanel.setChanged(false);

            if (serializer.getClass().isAnnotationPresent(InternalSerializer.class)) {
                Main.addLastFile(file, serializer, nodePanel.getTree());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can't serialize tree to '" + file.getAbsolutePath() + "'", e);
        }
    }

    public NodePanel getNodePanel() {

        return nodePanel;
    }

}
