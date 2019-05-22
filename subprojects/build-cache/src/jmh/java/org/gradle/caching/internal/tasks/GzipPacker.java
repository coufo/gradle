/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.caching.internal.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipPacker implements Packer {
    private final Packer delegate;

    public GzipPacker(Packer delegate) {
        this.delegate = delegate;
    }

    @Override
    public void pack(List<DataSource> inputs, DataTarget output) throws IOException {
        delegate.pack(inputs, new MyDelegatingDataTarget(output));
    }

    @Override
    public void unpack(DataSource input, DataTargetFactory targetFactory) throws IOException {
        delegate.unpack(new MyDelegatingDataSource(input), targetFactory);
    }

    private static class MyDelegatingDataSource extends DelegatingDataSource {
        public MyDelegatingDataSource(DataSource input) {
            super(input);
        }

        @Override
        public InputStream openInput() throws IOException {
            return new GZIPInputStream(super.openInput());
        }
    }

    private static class MyDelegatingDataTarget extends DelegatingDataTarget {
        public MyDelegatingDataTarget(DataTarget output) {
            super(output);
        }

        @Override
        public OutputStream openOutput() throws IOException {
            return new GZIPOutputStream(super.openOutput());
        }
    }
}
