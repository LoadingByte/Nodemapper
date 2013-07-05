
package com.quartercode.nodemapper.ser;

import java.io.IOException;
import java.io.OutputStream;

public interface Output {

    public OutputStream getOutputStream() throws IOException;

    public OutputStream getOutputStream(Object... parameters) throws IOException;

    public void close() throws IOException;

}
