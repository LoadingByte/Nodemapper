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
