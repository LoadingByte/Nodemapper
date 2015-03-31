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

package com.quartercode.nodemapper.gr.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import com.quartercode.nodemapper.gr.RenderNode;

public class DarkRenderer extends NodegeomRenderer {

    public DarkRenderer() {

        setBackgroundPaint(new Color(64, 64, 64));

        setContentInsets(new Insets(15, 30, 15, 30));
        setContentFont(new Font("Tahoma", Font.BOLD, 20));
        setContentPaint(new Color(192, 192, 192));
        setContentBackgroundPaint(new Color(58, 58, 58));

        setBorderStroke(new BasicStroke(20));
        setBorderPaint(new Color(50, 50, 50));

        setEditStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND));
        setEditPaint(new Color(100, 100, 100));

        setLinkStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        setLinkPaint(new Color(44, 44, 44));

        setCreationLinkStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        setCreationLinkPaint(new Color(50, 50, 50));
    }

    @Override
    public Shape getBorderShape(RenderNode node, Graphics2D g) {

        Rectangle rect = getRenderShape(node, g).getBounds();
        return new RoundRectangle2D.Double(rect.x, rect.y, rect.width, rect.height, 20, 5);
    }

    @Override
    public void drawViewport(Graphics2D g, Rectangle viewport, Point view) {

        super.drawViewport(g, viewport, view);

        g.setStroke(new BasicStroke(1.5F));
        g.setPaint(new Color(61, 61, 61));
        g.drawLine(-10000 - view.x, -view.y, 10000 - view.x, -view.y);
        g.drawLine(-view.x, -10000 - view.y, -view.x, 10000 - view.y);
    }

    @Override
    public void drawNode(Graphics2D g, RenderNode node) {

        super.drawNode(g, node);

        if (node.isEdit()) {
            g.setStroke(new BasicStroke(2));
            g.setPaint(new Color(40, 40, 40));
            g.draw(getInnerShape(node, g));
        }
    }

}
