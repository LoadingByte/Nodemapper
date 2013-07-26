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

package com.quartercode.nodemapper.gr.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import com.quartercode.nodemapper.gr.RenderNode;
import com.quartercode.nodemapper.gr.Renderer;

public class NodegeomRenderer implements Renderer {

    private Paint       backgroundPaint        = Color.WHITE;

    private Insets      contentInsets          = new Insets(10, 10, 10, 10);
    private Font        contentFont            = new Font("Tahoma", Font.PLAIN, 15);
    private Paint       contentPaint           = Color.BLACK;
    private Paint       contentBackgroundPaint = Color.LIGHT_GRAY;

    private BasicStroke editStroke             = new BasicStroke(2);
    private Paint       editPaint              = Color.LIGHT_GRAY.darker();

    private BasicStroke borderStroke           = new BasicStroke(10);
    private Paint       borderPaint            = Color.DARK_GRAY;

    private BasicStroke linkStroke             = new BasicStroke();
    private Paint       linkPaint              = Color.BLACK;

    private BasicStroke creationLinkStroke     = new BasicStroke();
    private Paint       creationLinkPaint      = Color.DARK_GRAY;

    public NodegeomRenderer() {

    }

    public Paint getBackgroundPaint() {

        return backgroundPaint;
    }

    public void setBackgroundPaint(Paint backgroundPaint) {

        this.backgroundPaint = backgroundPaint;
    }

    public Insets getContentInsets() {

        return contentInsets;
    }

    public void setContentInsets(Insets contentInsets) {

        this.contentInsets = contentInsets;
    }

    public Font getContentFont() {

        return contentFont;
    }

    public void setContentFont(Font contentFont) {

        this.contentFont = contentFont;
    }

    public Paint getContentPaint() {

        return contentPaint;
    }

    public void setContentPaint(Paint contentPaint) {

        this.contentPaint = contentPaint;
    }

    public Paint getContentBackgroundPaint() {

        return contentBackgroundPaint;
    }

    public void setContentBackgroundPaint(Paint contentBackgroundPaint) {

        this.contentBackgroundPaint = contentBackgroundPaint;
    }

    public BasicStroke getEditStroke() {

        return editStroke;
    }

    public void setEditStroke(BasicStroke editStroke) {

        this.editStroke = editStroke;
    }

    public Paint getEditPaint() {

        return editPaint;
    }

    public void setEditPaint(Paint editPaint) {

        this.editPaint = editPaint;
    }

    public BasicStroke getBorderStroke() {

        return borderStroke;
    }

    public void setBorderStroke(BasicStroke borderStroke) {

        this.borderStroke = borderStroke;
    }

    public Paint getBorderPaint() {

        return borderPaint;
    }

    public void setBorderPaint(Paint borderPaint) {

        this.borderPaint = borderPaint;
    }

    public BasicStroke getLinkStroke() {

        return linkStroke;
    }

    public void setLinkStroke(BasicStroke linkStroke) {

        this.linkStroke = linkStroke;
    }

    public Paint getLinkPaint() {

        return linkPaint;
    }

    public void setLinkPaint(Paint linkPaint) {

        this.linkPaint = linkPaint;
    }

    public BasicStroke getCreationLinkStroke() {

        return creationLinkStroke;
    }

    public void setCreationLinkStroke(BasicStroke creationLinkStroke) {

        this.creationLinkStroke = creationLinkStroke;
    }

    public Paint getCreationLinkPaint() {

        return creationLinkPaint;
    }

    public void setCreationLinkPaint(Paint creationLinkPaint) {

        this.creationLinkPaint = creationLinkPaint;
    }

    @Override
    public Shape getContentShape(RenderNode node, Graphics2D g) {

        int width = 0;
        for (String line : node.getContentLines()) {
            if (g.getFontMetrics(contentFont).stringWidth(line) > width) {
                width = g.getFontMetrics(contentFont).stringWidth(line);
            }
        }
        int height = g.getFontMetrics(contentFont).getHeight() * node.getContentLines().length;

        return new Rectangle(node.getLocation().x - width / 2, node.getLocation().y - height / 2, width, height);
    }

    @Override
    public Shape getInnerShape(RenderNode node, Graphics2D g) {

        Rectangle rect = getRenderShape(node, g).getBounds();
        int x = (int) (rect.x + borderStroke.getLineWidth() / 2);
        int y = (int) (rect.y + borderStroke.getLineWidth() / 2);
        int width = (int) (rect.width - borderStroke.getLineWidth());
        int height = (int) (rect.height - borderStroke.getLineWidth());

        return new Rectangle(x, y, width, height);
    }

    @Override
    public Shape getRenderShape(RenderNode node, Graphics2D g) {

        Rectangle rect = getContentShape(node, g).getBounds();

        return new Rectangle(rect.x - contentInsets.left, rect.y - contentInsets.top, rect.width + contentInsets.left + contentInsets.right, rect.height + contentInsets.top + contentInsets.bottom);
    }

    public Shape getBorderShape(RenderNode node, Graphics2D g) {

        return getRenderShape(node, g);
    }

    @Override
    public Shape getCompleteShape(RenderNode node, Graphics2D g) {

        Rectangle rect = getRenderShape(node, g).getBounds();
        int x = (int) (rect.x - borderStroke.getLineWidth() / 2);
        int y = (int) (rect.y - borderStroke.getLineWidth() / 2);
        int width = (int) (rect.width + borderStroke.getLineWidth());
        int height = (int) (rect.height + borderStroke.getLineWidth());

        return new Rectangle(x, y, width, height);
    }

    @Override
    public void drawViewport(Graphics2D g, Rectangle viewport, Point view) {

        g.setPaint(backgroundPaint);
        g.fill(viewport);
    }

    @Override
    public void drawNode(Graphics2D g, RenderNode node) {

        g.setFont(contentFont);

        Rectangle contentRect = getContentShape(node, g).getBounds();
        Rectangle innerRect = getRenderShape(node, g).getBounds();

        g.setPaint(contentBackgroundPaint);
        g.fill(innerRect);

        String[] lines = node.getContentLines();
        for (int line = 0; line < lines.length; line++) {
            g.setPaint(contentPaint);
            int x = contentRect.x + contentRect.width / 2 - g.getFontMetrics(contentFont).stringWidth(lines[line]) / 2;
            int y = (int) (contentRect.y + g.getFontMetrics(contentFont).getAscent() + line * contentRect.height / lines.length * 1.1);
            g.drawString(lines[line], x, y);
        }

        if (node.isEdit()) {
            int cursor = 0;
            for (int line = 0; line < lines.length; line++) {
                if (cursor > 0) {
                    cursor++;
                }

                if (node.getEditCursor() >= cursor && node.getEditCursor() <= cursor + lines[line].length()) {
                    g.setStroke(editStroke);
                    g.setPaint(editPaint);
                    int x = contentRect.x + contentRect.width / 2 - g.getFontMetrics(contentFont).stringWidth(lines[line]) / 2;
                    int y = (int) (contentRect.y + g.getFontMetrics(contentFont).getAscent() + line * contentRect.height / lines.length * 1.1);
                    String before = "";
                    char[] chars = lines[line].toCharArray();
                    for (int index = 0; index < node.getEditCursor() - cursor; index++) {
                        before += chars[index];
                    }
                    x += g.getFontMetrics(contentFont).stringWidth(before);
                    g.drawLine(x, y + g.getFontMetrics(contentFont).getDescent(), x, y - g.getFontMetrics(contentFont).getAscent());
                    break;
                } else {
                    cursor += lines[line].length();
                }
            }
        }

        g.setStroke(borderStroke);
        g.setPaint(borderPaint);
        g.draw(getBorderShape(node, g));
    }

    @Override
    public void drawLink(Graphics2D g, RenderNode node1, RenderNode node2) {

        Rectangle rect1 = getContentShape(node1, g).getBounds();
        Rectangle rect2 = getContentShape(node2, g).getBounds();

        g.setStroke(linkStroke);
        g.setPaint(linkPaint);
        g.drawLine(rect1.x + rect1.width / 2, rect1.y + rect1.height / 2, rect2.x + rect2.width / 2, rect2.y + rect2.height / 2);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke());
    }

    @Override
    public void drawCreationLink(Graphics2D g, Point point1, Point point2) {

        g.setStroke(creationLinkStroke);
        g.setPaint(creationLinkPaint);
        g.drawLine(point1.x, point1.y, point2.x, point2.y);
    }

}
