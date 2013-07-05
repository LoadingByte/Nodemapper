
package com.quartercode.nodemapper.ser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import com.quartercode.qcutil.io.File;

public class FileInput implements Input {

    private final File                   file;
    private final Map<File, InputStream> streams = new HashMap<File, InputStream>();

    public FileInput(final File file) {

        this.file = file;
    }

    private InputStream getStream(final File file) throws IOException {

        if (!streams.containsKey(file)) {
            streams.put(file, new FileInputStream(file));
        }

        return streams.get(file);
    }

    @Override
    public InputStream getInputStream() throws IOException {

        return getStream(file);
    }

    @Override
    public InputStream getInputStream(final Object... parameters) throws IOException {

        final String ending = file.getName().substring(file.getName().lastIndexOf("."));
        return getStream(new File(File.convert(file.getParentFile()), file.getName().replace(ending, parameters[0] + ((Boolean) parameters[1] ? ending : ""))));
    }

    @Override
    public void close() throws IOException {

        for (final InputStream stream : streams.values()) {
            stream.close();
        }
    }

}
