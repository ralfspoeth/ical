package com.github.ralfspoeth.ical;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Arrays;
import java.util.Objects;

public class UnfoldingInputStream extends FilterInputStream implements AutoCloseable {

    private static final byte CR = 13;
    private static final byte LF = 10;

    private static final byte WS = ' ';
    private static final byte HTAB = '\t';

    public UnfoldingInputStream(InputStream in) {
        super(in);
        Arrays.fill(buffer, -1);
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    private final int[] buffer = new int[3];
    private transient int mark = -1;

    @Override
    public int read() throws IOException {
        int last;
        if (mark>-1) {
            last = buffer[mark--];
        } else {
            last = in.read();
            buffer[++mark] = last;
            if (last == CR) {
                var next = in.read();  // read ahead

                if (next == LF) {
                    var nextnext = in.read(); // read ahead
                    buffer[++mark] = next;
                    if (nextnext == WS || nextnext == HTAB) {
                        mark = -1;
                        last = in.read();
                    } else {
                        buffer[++mark] = nextnext;
                    }
                }
            }
        }
        return last;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (off < 0 || off + len > Objects.requireNonNull(b).length) throw new IndexOutOfBoundsException();
        int count = 0;
        int last;
        while (len-- > 0 && (last = read()) > -1) {
            b[off++] = (byte) last;
            count++;
        }
        return count;
    }

    @Override
    public long skip(long n) throws IOException {
        long count = 0;
        while (count < n && read() > -1) {
            count++;
        }
        return count;
    }
}
