package com.github.ralfspoeth.ical;

sealed interface IO permits UnfoldingInputStream, FoldingOutputStream {
    final byte CR = 13;
    final byte LF = 10;

    final byte WS = ' ';
    final byte HTAB = '\t';
}
