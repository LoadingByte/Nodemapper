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

import java.util.ArrayList;
import java.util.List;
import com.quartercode.nodemapper.tree.Tree;

public class StateManager {

    private final List<Runnable> listeners = new ArrayList<Runnable>();

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

    public void addState(Tree state) {

        index++;
        if (index > 0) {
            states.subList(index, states.size()).clear();
        }
        states.add(state);

        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    public void clearStates() {

        states.clear();
        index = -1;
    }

    public Tree rollState(int direction) {

        if (direction > 0) {
            index++;
        } else if (direction < 0) {
            index--;
        }

        for (Runnable listener : listeners) {
            listener.run();
        }

        return states.get(index).clone();
    }

    public void addListener(Runnable listener) {

        listeners.add(listener);
    }

    public void removeListener(Runnable listener) {

        listeners.remove(listener);
    }

}
