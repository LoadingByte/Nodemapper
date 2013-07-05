
package com.quartercode.nodemapper.ser;

import java.io.IOException;
import java.io.InputStream;

public interface Input {

    public InputStream getInputStream() throws IOException;

    public InputStream getInputStream(Object... parameters) throws IOException;

    public void close() throws IOException;

}
