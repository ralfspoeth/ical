package com.github.ralfspoeth.ical;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

class ParserTest {

    @Test
    void test2() throws IOException {
        var is = getClass().getResourceAsStream("/test2.ics");
        try (var unfold = new UnfoldingInputStream(is)) {//; var charStream = new InputStreamReader(unfold, Charset.forName("utf-8"))) {
            int c;
            while ((c = unfold.read()) > -1) {
                System.out.print((char) c);
            }
        }
    }

    @Test
    void test1() throws IOException {
        try (var is = getClass().getResourceAsStream("/test1.ics");
             var uf = new UnfoldingInputStream(is);
             var out = new FileOutputStream("test1.out")
        ) {
            uf.transferTo(out);
        }
    }


    @Test
    void testMinus1() throws IOException {
        try (var is = new UnfoldingInputStream(getClass().getResourceAsStream("/test1.ics"))) {
            int ch;
            while((ch=is.read())>-1); // advance till EOF
            try {
                ch += is.read();
                ch += is.read();
                ch += is.read();
                System.out.printf("ch is %d%n", ch);
            }
            catch(IOException ioex) {
                ioex.printStackTrace();
                throw ioex;
            }
        }
    }

    @Test
    void linesTest() throws IOException {
        try (var is = new UnfoldingInputStream(getClass().getResourceAsStream("/test1.ics"));
             var rdr = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        ) {
            rdr.lines().forEach(System.out::println);
        }
    }
}