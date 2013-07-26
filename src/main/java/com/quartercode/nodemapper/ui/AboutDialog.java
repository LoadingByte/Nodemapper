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
import com.quartercode.nodemapper.Main;

@SuppressWarnings ("serial")
public class AboutDialog extends JDialog {

    private final JPanel contentPanel;

    public AboutDialog(JFrame parent) {

        super(parent);

        setTitle("About " + Main.NAME);
        setModal(true);
        setResizable(false);
        setBounds(0, 0, 450, 150);
        setLocation(getParent().getX() + getParent().getWidth() / 2 - getWidth() / 2, getParent().getY() + getParent().getHeight() / 2 - getHeight() / 2);

        contentPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel);

        JLabel nameLabel = new JLabel("Product: " + Main.NAME);
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        contentPanel.add(nameLabel);

        JLabel creatorLabel = new JLabel("Creator: " + Main.CREATOR);
        creatorLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        contentPanel.add(creatorLabel);

        JLabel versionLabel = new JLabel("Version: " + Main.VERSION);
        versionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        contentPanel.add(versionLabel);

        JButton okButton = new JButton(new AbstractAction("Close") {

            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
            }
        });
        contentPanel.add(okButton);
    }

}
