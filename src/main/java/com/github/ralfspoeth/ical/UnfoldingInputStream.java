package com.github.ralfspoeth.ical;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Objects;

final class UnfoldingInputStream extends FilterInputStream implements IO, AutoCloseable {

    public UnfoldingInputStream(InputStream in) {
        super(new PushbackInputStream(in, 2));
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int read() throws IOException {
        var pbis = (PushbackInputStream)in;
        int last = pbis.read();
        if (last == CR) {
            var next = pbis.read();  // read ahead
            if (next == LF) {
                var nextnext = pbis.read(); // read ahead
                if (nextnext == WS || nextnext == HTAB) {
                    last = pbis.read();
                } else if (nextnext > -1) {
                    pbis.unread(nextnext);
                    pbis.unread(next);
                }
            }
            else if (next > -1) {
                pbis.unread(next);
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
        return count==0?-1:count;
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
