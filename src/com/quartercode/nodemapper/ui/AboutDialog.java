
package com.quartercode.nodemapper.ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.quartercode.nodemapper.Nodemapper;

@SuppressWarnings ("serial")
public class AboutDialog extends JDialog {

    private final JPanel contentPanel;

    public AboutDialog(final JFrame parent) {

        super(parent);

        setTitle("About " + Nodemapper.NAME);
        setModal(true);
        setResizable(false);
        setBounds(0, 0, 450, 150);
        setLocation(getParent().getX() + getParent().getWidth() / 2 - getWidth() / 2, getParent().getY() + getParent().getHeight() / 2 - getHeight() / 2);

        contentPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel);

        final JLabel nameLabel = new JLabel("Product: " + Nodemapper.NAME);
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        contentPanel.add(nameLabel);

        final JLabel creatorLabel = new JLabel("Creator: " + Nodemapper.CREATOR);
        creatorLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        contentPanel.add(creatorLabel);

        final JLabel versionLabel = new JLabel("Version: " + Nodemapper.VERSION);
        versionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        contentPanel.add(versionLabel);

        final JButton okButton = new JButton(new AbstractAction("Close") {

            @Override
            public void actionPerformed(final ActionEvent e) {

                dispose();
            }
        });
        contentPanel.add(okButton);
    }

}
