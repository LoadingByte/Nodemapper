
package com.quartercode.nodemapper.ser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import com.quartercode.qcutil.io.File;

public class FileOutput implements Output {

    private final File                    file;
    private final Map<File, OutputStream> streams = new HashMap<File, OutputStream>();

    public FileOutput(final File file) {

        this.file = file;
    }

    private OutputStream getStream(final File file) throws IOException {

        if (!streams.containsKey(file)) {
            streams.put(file, new FileOutputStream(file));
        }

        return streams.get(file);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {

        return getStream(file);
    }

    @Override
    public OutputStream getOutputStream(final Object... parameters) throws IOException {

        final String ending = file.getName().substring(file.getName().lastIndexOf("."));
        return getStream(new File(File.convert(file.getParentFile()), file.getName().replace(ending, parameters[0] + ((Boolean) parameters[1] ? ending : ""))));
    }

    @Override
    public void close() throws IOException {

        for (final OutputStream stream : streams.values()) {
            stream.close();
        }
    }

}
