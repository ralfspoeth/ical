package com.github.ralfspoeth.ical;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

final class FoldingOutputStream implements IO, AutoCloseable {

    private static final ByteBuffer CRLF = ByteBuffer.wrap(new byte[]{CR, LF});

    private final WritableByteChannel out;
    private final CharsetEncoder utf8Enc = StandardCharsets.UTF_8.newEncoder();

    public FoldingOutputStream(OutputStream out) {
        this.out = Channels.newChannel(out);
        utf8Enc.reset();
    }

    private void write(String line) throws IOException {
        var bb = utf8Enc.encode(CharBuffer.wrap(line));
        bb.flip();
        out.write(bb);
        out.write(CRLF);
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
