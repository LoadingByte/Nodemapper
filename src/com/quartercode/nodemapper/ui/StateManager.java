
package com.quartercode.nodemapper.ui;

import java.util.ArrayList;
import java.util.List;
import com.quartercode.nodemapper.tree.Tree;
import com.quartercode.qcutil.Event;
import com.quartercode.qcutil.Listener;

public class StateManager {

    private final List<Listener> listeners = new ArrayList<Listener>();

    private final List<Tree>     states    = new ArrayList<Tree>();
    private int                  index     = -1;

    public StateManager() {

    }

    public List<Tree> getStates() {

        return states;
    }

    public int getIndex() {

        return index;
    }

    public void addState(final Tree state) {

        index++;
        if (index > 0) {
            states.subList(index, states.size()).clear();
        }
        states.add(state);

        new Event(listeners, "action", "add").fire();
    }

    public void clearStates() {

        states.clear();
        index = -1;
    }

    public Tree rollState(final int direction) {

        if (direction > 0) {
            index++;
        } else if (direction < 0) {
            index--;
        }

        new Event(listeners, "action", "roll", "direction", direction).fire();
        return states.get(index).clone();
    }

    public void addListener(final Listener listener) {

        listeners.add(listener);
    }

    public void removeListener(final Listener listener) {

        listeners.remove(listener);
    }

}
