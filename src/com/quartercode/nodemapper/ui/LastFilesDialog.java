
package com.quartercode.nodemapper.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
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
import com.quartercode.nodemapper.Nodemapper;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.ser.SerializerManager;
import com.quartercode.nodemapper.tree.Tree;
import com.quartercode.qcutil.io.File;

@SuppressWarnings ("serial")
public class LastFilesDialog extends JDialog {

    private final JPanel           contentPanel;
    private final JList            lastFilesList;
    private final DefaultListModel lastFilesModel;

    public LastFilesDialog(final MainFrame parent) {

        super(parent);

        setTitle("Recent Maps");
        setModal(true);
        setBounds(0, 0, 500, 300);
        setLocation(getParent().getX() + getParent().getWidth() / 2 - getWidth() / 2, getParent().getY() + getParent().getHeight() / 2 - getHeight() / 2);

        contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel);

        final JPanel lastFilesPanel = new JPanel(new BorderLayout(0, 5));
        lastFilesPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY, 1, true), new EmptyBorder(7, 7, 7, 7)));
        contentPanel.add(lastFilesPanel, BorderLayout.CENTER);

        final JLabel versionLabel = new JLabel("Open Recent Map");
        versionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lastFilesPanel.add(versionLabel, BorderLayout.NORTH);

        lastFilesList = new JList();
        lastFilesList.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lastFilesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {

                if (lastFilesList.getSelectedValue() instanceof LastFileEntry) {
                    final File file = ((LastFileEntry) lastFilesList.getSelectedValue()).getFile();
                    final Serializer serializer = SerializerManager.getSerializer( ((LastFileEntry) lastFilesList.getSelectedValue()).getSerializer());
                    final Tree tree = ((LastFileEntry) lastFilesList.getSelectedValue()).getTree();
                    parent.openTree(file, serializer, tree);
                    dispose();
                }
            }
        });
        lastFilesPanel.add(lastFilesList, BorderLayout.CENTER);

        lastFilesModel = new DefaultListModel();
        lastFilesList.setModel(lastFilesModel);
        refreshList();

        final JButton clearButton = new JButton(new AbstractAction("Clear Recent Maps") {

            @Override
            public void actionPerformed(final ActionEvent e) {

                Nodemapper.clearLastFiles();
                refreshList();
            }
        });
        lastFilesPanel.add(clearButton, BorderLayout.SOUTH);

        final JButton okButton = new JButton(new AbstractAction("Close") {

            @Override
            public void actionPerformed(final ActionEvent e) {

                dispose();
            }
        });
        contentPanel.add(okButton, BorderLayout.SOUTH);
    }

    public void refreshList() {

        lastFilesModel.clear();

        for (int counter = Nodemapper.getLastFiles().size() - 1; counter >= 0; counter--) {
            lastFilesModel.addElement(Nodemapper.getLastFiles().get(counter));
        }

        if (lastFilesModel.size() <= 0) {
            lastFilesModel.addElement("No recent Maps");
        }
    }

}
