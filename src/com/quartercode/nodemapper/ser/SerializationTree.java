
package com.quartercode.nodemapper.ser;

import com.quartercode.nodemapper.tree.Tree;

public interface SerializationTree {

    public void serialize(Tree tree);

    public Tree deserialize();

}
