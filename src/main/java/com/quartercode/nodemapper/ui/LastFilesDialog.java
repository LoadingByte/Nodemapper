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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.quartercode.nodemapper.LastFilesSerializer.LastFileEntry;
import com.quartercode.nodemapper.Main;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.ser.SerializerManager;
import com.quartercode.nodemapper.tree.Tree;

@SuppressWarnings ("serial")
public class LastFilesDialog extends JDialog {

    private final JList            lastFilesList;
    private final DefaultListModel lastFilesModel;

    public LastFilesDialog(final MainFrame parent) {

        super(parent);

        setTitle("Recent Maps");
        setModal(true);
        setBounds(0, 0, 500, 300);
        setLocation(getParent().getX() + getParent().getWidth() / 2 - getWidth() / 2, getParent().getY() + getParent().getHeight() / 2 - getHeight() / 2);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel);

        JPanel lastFilesPanel = new JPanel(new BorderLayout(0, 5));
        lastFilesPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY, 1, true), new EmptyBorder(7, 7, 7, 7)));
        contentPanel.add(lastFilesPanel, BorderLayout.CENTER);

        JLabel versionLabel = new JLabel("Open Recent Map");
        versionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lastFilesPanel.add(versionLabel, BorderLayout.NORTH);

        lastFilesList = new JList();
        lastFilesList.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lastFilesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (lastFilesList.getSelectedValue() instanceof LastFileEntry) {
                    File file = ((LastFileEntry) lastFilesList.getSelectedValue()).getFile();
                    Serializer serializer = SerializerManager.getSerializer( ((LastFileEntry) lastFilesList.getSelectedValue()).getSerializer());
                    Tree tree = ((LastFileEntry) lastFilesList.getSelectedValue()).getTree();
                    parent.openTree(file, serializer, tree);
                    dispose();
                }
            }
        });
        lastFilesPanel.add(lastFilesList, BorderLayout.CENTER);

        lastFilesModel = new DefaultListModel();
        lastFilesList.setModel(lastFilesModel);
        refreshList();

        JButton clearButton = new JButton(new AbstractAction("Clear Recent Maps") {

            @Override
            public void actionPerformed(ActionEvent e) {

                Main.clearLastFiles();
                refreshList();
            }
        });
        lastFilesPanel.add(clearButton, BorderLayout.SOUTH);

        JButton okButton = new JButton(new AbstractAction("Close") {

            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
            }
        });
        contentPanel.add(okButton, BorderLayout.SOUTH);
    }

    public void refreshList() {

        lastFilesModel.clear();

        for (int counter = Main.getLastFiles().size() - 1; counter >= 0; counter--) {
            lastFilesModel.addElement(Main.getLastFiles().get(counter));
        }

        if (lastFilesModel.isEmpty()) {
            lastFilesModel.addElement("No recent Maps");
        }
    }

}
