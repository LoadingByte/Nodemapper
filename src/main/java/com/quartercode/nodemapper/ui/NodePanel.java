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

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import com.quartercode.nodemapper.gr.RenderNode;
import com.quartercode.nodemapper.gr.Renderer;
import com.quartercode.nodemapper.tree.Node;
import com.quartercode.nodemapper.tree.NodeProperty;
import com.quartercode.nodemapper.tree.Tree;
import com.quartercode.nodemapper.tree.TreeUtil;

@SuppressWarnings ("serial")
public class NodePanel extends JPanel {

    private Tree                 tree;
    private Renderer             renderer;
    private StateManager         stateManager;
    private boolean              changed;

    private Point                viewPoint;
    private final List<Runnable> changeListeners = new ArrayList<Runnable>();

    private RenderNode           dragNode;
    private boolean              dragNodeDragged;
    private Point                createStart;
    private Point                createEnd;
    private RenderNode           editNode;
    private int                  editCursor;
    private Point                dragView;

    public NodePanel() {

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                if (tree != null) {
                    release();

                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (e.getClickCount() <= 1) {
                            if (getNodeInner(e.getPoint(), false) != null) {
                                dragNode = new RenderNode(tree, getNodeInner(e.getPoint(), false));
                                dragNodeDragged = false;
                                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                            } else if (getNodeComplete(e.getPoint(), true) != null) {
                                createStart = e.getPoint();
                                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                            } else if (editNode != null) {
                                editNode = null;
                                onChange();
                            } else if (getNodeInner(e.getPoint(), false) == null && e.isControlDown()) {
                                dragView = e.getPoint();
                                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            }
                        } else {
                            if (getNodeInner(e.getPoint(), true) != null) {
                                editNode = new RenderNode(tree, getNodeInner(e.getPoint(), true));
                                editCursor = 0;
                            }
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON2) {
                        dragView = e.getPoint();
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else if (e.getButton() == MouseEvent.BUTTON3 && getNodeInner(e.getPoint(), false) != null) {
                        TreeUtil.removeNode(tree, getNodeInner(e.getPoint(), false));
                        TreeUtil.cleanupNodes(tree);
                        onChange();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if (tree != null) {
                    if (createEnd != null) {
                        Node start = getNodeComplete(createStart, true);
                        if (start != null) {
                            Node end = getNodeComplete(createEnd, true);
                            if (end == null) {
                                end = new Node();
                                Point endPoint = convertPointRelToAbs(createEnd);
                                end.addProperty(new NodeProperty("x", String.valueOf(endPoint.x)));
                                end.addProperty(new NodeProperty("y", String.valueOf(endPoint.y)));
                                TreeUtil.addNode(tree, end, start);
                                editNode = new RenderNode(tree, end);
                                editCursor = 0;
                            } else {
                                if (start.getChildren().contains(end)) {
                                    start.removeChild(end);
                                } else if (end.getChildren().contains(start)) {
                                    end.removeChild(start);
                                } else {
                                    start.addChild(end);
                                }
                            }

                            TreeUtil.cleanupNodes(tree);
                            onChange();
                        }
                    }

                    release();
                }
            }

            private void release() {

                if (dragNode != null && dragNodeDragged) {
                    onChange();
                }

                dragNode = null;
                createStart = null;
                createEnd = null;
                dragView = null;

                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                if (tree != null) {
                    if (dragNode != null) {
                        Point dragPoint = convertPointRelToAbs(e.getPoint());
                        dragNode.getNode().getProperty("x").setValue(String.valueOf(dragPoint.x));
                        dragNode.getNode().getProperty("y").setValue(String.valueOf(dragPoint.y));
                        dragNodeDragged = true;
                        repaint();
                    } else if (createStart != null) {
                        createEnd = e.getPoint();
                        repaint();
                    } else if (dragView != null) {
                        int multiplier = e.isShiftDown() ? 2 : 1;
                        viewPoint = new Point(viewPoint.x + (dragView.x - e.getX()) * multiplier, viewPoint.y + (dragView.y - e.getY()) * multiplier);
                        dragView = e.getPoint();
                        repaint();
                    }
                }
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {

                if (tree != null) {
                    if (e.getID() == KeyEvent.KEY_PRESSED && editNode != null) {
                        UnicodeBlock block = UnicodeBlock.of(e.getKeyChar());
                        if (e.getKeyCode() == KeyEvent.VK_LEFT && editCursor > 0) {
                            editCursor--;
                        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && editCursor < editNode.getContent().length()) {
                            editCursor++;
                        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            editNode.setContent(editNode.getContent().substring(0, editCursor) + "\\" + editNode.getContent().substring(editCursor));
                            editCursor++;
                        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && editCursor > 0) {
                            editNode.setContent(editNode.getContent().substring(0, editCursor - 1) + editNode.getContent().substring(editCursor));
                            editCursor--;
                        } else if (e.getKeyCode() == KeyEvent.VK_DELETE && editCursor < editNode.getContent().length()) {
                            editNode.setContent(editNode.getContent().substring(0, editCursor) + editNode.getContent().substring(editCursor + 1));
                        } else if (!Character.isISOControl(e.getKeyChar()) && e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && block != null && block != Character.UnicodeBlock.SPECIALS && e.getKeyChar() != '\\') {
                            editNode.setContent(editNode.getContent().substring(0, editCursor) + e.getKeyChar() + editNode.getContent().substring(editCursor));
                            editCursor++;
                        }

                        while (editNode.getContent().length() > 0 && editNode.getContent().toCharArray()[0] == '\\') {
                            editNode.setContent(editNode.getContent().substring(1));
                            editCursor = 0;
                        }

                        repaint();
                    }
                }

                return false;
            }
        });
    }

    public NodePanel(Tree tree) {

        this();
        this.tree = tree;
    }

    public NodePanel(Tree tree, Renderer renderer) {

        this(tree);
        this.renderer = renderer;
    }

    public Tree getTree() {

        return tree;
    }

    public void setTree(Tree tree) {

        this.tree = tree;
    }

    public Renderer getRenderer() {

        return renderer;
    }

    public void setRenderer(Renderer renderer) {

        this.renderer = renderer;
    }

    public StateManager getStateManager() {

        return stateManager;
    }

    public void setStateManager(StateManager stateManager) {

        this.stateManager = stateManager;
    }

    public boolean isChanged() {

        return changed;
    }

    public void setChanged(boolean changed) {

        this.changed = changed;

        for (Runnable changeListener : changeListeners) {
            changeListener.run();
        }
    }

    public Point getViewPoint() {

        return viewPoint;
    }

    public Point convertPointRelToAbs(Point relativePoint) {

        return new Point(relativePoint.x + viewPoint.x, relativePoint.y + viewPoint.y);
    }

    public Point convertPointAbsToRel(Point relativePoint) {

        return new Point(relativePoint.x - viewPoint.x, relativePoint.y - viewPoint.y);
    }

    public void addChangeListener(Runnable listener) {

        changeListeners.add(listener);
    }

    public void removeChangeListener(Runnable listener) {

        changeListeners.remove(listener);
    }

    public RenderNode getRenderNode(Node node) {

        return new RenderNode(tree, node, convertPointAbsToRel(getLocation(node)), editNode != null && node.equals(editNode.getNode()) ? editCursor : -1);
    }

    public Point getLocation(Node node) {

        int x = node.getProperty("x") == null ? 0 : Integer.parseInt(node.getProperty("x").getValue());
        int y = node.getProperty("y") == null ? 0 : Integer.parseInt(node.getProperty("y").getValue());

        return new Point(x, y);
    }

    public Node getNodeInner(Point location, boolean acceptRoot) {

        for (Node node : tree.getNodes()) {
            if ( (acceptRoot || node.getId() > 0) && renderer.getInnerShape(getRenderNode(node), (Graphics2D) getGraphics()).contains(location)) {
                return node;
            }
        }

        return null;
    }

    public Node getNodeComplete(Point location, boolean acceptRoot) {

        for (Node node : tree.getNodes()) {
            if ( (acceptRoot || node.getId() > 0) && renderer.getCompleteShape(getRenderNode(node), (Graphics2D) getGraphics()).contains(location)) {
                return node;
            }
        }

        return null;
    }

    public void onChange() {

        setChanged(true);
        stateManager.addState(tree.clone());
    }

    public void resetView() {

        viewPoint = new Point(- (getParent().getWidth() / 2), -150);
        repaint();
    }

    @Override
    public void paint(Graphics g) {

        if (viewPoint == null) {
            resetView();
        }

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderer.drawViewport((Graphics2D) g, getBounds(), viewPoint);
        if (tree != null) {
            paintLinks(g);
            paintNodes(g);
            if (createStart != null && createEnd != null) {
                renderer.drawCreationLink((Graphics2D) g, createStart, createEnd);
            }
        }
    }

    private void paintNodes(Graphics g) {

        for (Node node : tree.getNodes()) {
            renderer.drawNode((Graphics2D) g, getRenderNode(node));
        }
    }

    private void paintLinks(Graphics g) {

        for (Node node : tree.getNodes()) {
            for (Node child : node.getChildren()) {
                renderer.drawLink((Graphics2D) g, getRenderNode(node), getRenderNode(child));
            }
        }
    }

}
