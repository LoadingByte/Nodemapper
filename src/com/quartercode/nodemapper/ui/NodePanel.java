
package com.quartercode.nodemapper.ui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
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
import com.quartercode.qcutil.Event;
import com.quartercode.qcutil.Listener;

@SuppressWarnings ("serial")
public class NodePanel extends JPanel {

    private Tree                 tree;
    private Renderer             renderer;
    private StateManager         stateManager;
    private boolean              changed;

    private Point                view;
    private final List<Listener> changeListeners = new ArrayList<Listener>();

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
            public void mousePressed(final MouseEvent e) {

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
            public void mouseReleased(final MouseEvent e) {

                if (tree != null) {
                    if (createEnd != null) {
                        final Node start = getNodeComplete(createStart, true);
                        if (start != null) {
                            Node end = getNodeComplete(createEnd, true);
                            if (end == null) {
                                end = new Node();
                                end.addProperty(new NodeProperty("x", String.valueOf(createEnd.x + view.x)));
                                end.addProperty(new NodeProperty("y", String.valueOf(createEnd.y + view.y)));
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
            public void mouseDragged(final MouseEvent e) {

                if (tree != null) {
                    if (dragNode != null) {
                        dragNode.getNode().getProperty("x").setValue(String.valueOf(e.getX() + view.x));
                        dragNode.getNode().getProperty("y").setValue(String.valueOf(e.getY() + view.y));
                        dragNodeDragged = true;
                        repaint();
                    } else if (createStart != null) {
                        createEnd = e.getPoint();
                        repaint();
                    } else if (dragView != null) {
                        final int multiplier = e.isControlDown() || e.isShiftDown() ? 2 : 1;
                        view = new Point(view.x + (dragView.x - e.getX()) * multiplier, view.y + (dragView.y - e.getY()) * multiplier);
                        dragView = e.getPoint();
                        repaint();
                    }
                }
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(final KeyEvent e) {

                if (tree != null) {
                    if (e.getID() == KeyEvent.KEY_PRESSED && editNode != null) {
                        final UnicodeBlock block = UnicodeBlock.of(e.getKeyChar());
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

    public NodePanel(final Tree tree) {

        this();
        this.tree = tree;
    }

    public NodePanel(final Tree tree, final Renderer renderer) {

        this(tree);
        this.renderer = renderer;
    }

    public Tree getTree() {

        return tree;
    }

    public void setTree(final Tree tree) {

        this.tree = tree;
    }

    public Renderer getRenderer() {

        return renderer;
    }

    public void setRenderer(final Renderer renderer) {

        this.renderer = renderer;
    }

    public StateManager getStateManager() {

        return stateManager;
    }

    public void setStateManager(final StateManager stateManager) {

        this.stateManager = stateManager;
    }

    public boolean isChanged() {

        return changed;
    }

    public void setChanged(final boolean changed) {

        this.changed = changed;

        new Event(changeListeners, "changed", changed).fire();
    }

    public Point getView() {

        return view;
    }

    public Rectangle getViewport() {

        return new Rectangle(getView(), getSize());
    }

    public void addChangeListener(final Listener listener) {

        changeListeners.add(listener);
    }

    public void removeChangeListener(final Listener listener) {

        changeListeners.remove(listener);
    }

    public RenderNode getRenderNode(final Node node) {

        return new RenderNode(tree, node, getScreenLocation(node), editNode != null && node.equals(editNode.getNode()) ? editCursor : -1);
    }

    public Point getLocation(final Node node) {

        final int x = node.getProperty("x") == null ? 0 : Integer.parseInt(node.getProperty("x").getValue());
        final int y = node.getProperty("y") == null ? 0 : Integer.parseInt(node.getProperty("y").getValue());

        return new Point(x, y);
    }

    public Point getScreenLocation(final Node node) {

        return new Point(getLocation(node).x - view.x, getLocation(node).y - view.y);
    }

    public Node getNodeInner(final Point location, final boolean acceptRoot) {

        for (final Node node : tree.getNodes()) {
            if ( (acceptRoot || node.getId() > 0) && renderer.getInnerShape(getRenderNode(node), (Graphics2D) getGraphics()).contains(location)) {
                return node;
            }
        }

        return null;
    }

    public Node getNodeComplete(final Point location, final boolean acceptRoot) {

        for (final Node node : tree.getNodes()) {
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

        view = new Point(- (getParent().getWidth() / 2), -150);
        repaint();
    }

    @Override
    public void paint(final Graphics g) {

        if (view == null) {
            resetView();
        }

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderer.drawViewport((Graphics2D) g, getBounds(), view);
        if (tree != null) {
            paintLinks(g);
            paintNodes(g);
            if (createStart != null && createEnd != null) {
                renderer.drawCreationLink((Graphics2D) g, createStart, createEnd);
            }
        }
    }

    private void paintNodes(final Graphics g) {

        for (final Node node : tree.getNodes()) {
            renderer.drawNode((Graphics2D) g, getRenderNode(node));
        }
    }

    private void paintLinks(final Graphics g) {

        for (final Node node : tree.getNodes()) {
            for (final Node child : node.getChildren()) {
                renderer.drawLink((Graphics2D) g, getRenderNode(node), getRenderNode(child));
            }
        }
    }

}
