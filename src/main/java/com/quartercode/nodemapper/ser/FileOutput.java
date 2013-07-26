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

package com.quartercode.nodemapper.ser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileOutput implements Output {

    private final File                    file;
    private final Map<File, OutputStream> streams = new HashMap<File, OutputStream>();

    public FileOutput(File file) {

        this.file = file;
    }

    private OutputStream getStream(File file) throws IOException {

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
    public OutputStream getOutputStream(Object... parameters) throws IOException {

        String ending = file.getName().substring(file.getName().lastIndexOf("."));
        return getStream(new File(file.getParentFile(), file.getName().replace(ending, parameters[0] + ((Boolean) parameters[1] ? ending : ""))));
    }

    @Override
    public void close() throws IOException {

        for (OutputStream stream : streams.values()) {
            stream.close();
        }
    }

}
