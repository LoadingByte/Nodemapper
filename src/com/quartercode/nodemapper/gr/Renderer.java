
package com.quartercode.nodemapper.gr;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

public interface Renderer {

    public Shape getContentShape(RenderNode node, Graphics2D g);

    public Shape getInnerShape(RenderNode node, Graphics2D g);

    public Shape getRenderShape(RenderNode node, Graphics2D g);

    public Shape getCompleteShape(RenderNode node, Graphics2D g);

    public void drawViewport(Graphics2D g, Rectangle viewport, Point view);

    public void drawNode(Graphics2D g, RenderNode node);

    public void drawLink(Graphics2D g, RenderNode node1, RenderNode node2);

    public void drawCreationLink(Graphics2D g, Point point1, Point point2);

}
