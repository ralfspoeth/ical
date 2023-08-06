package com.github.ralfspoeth.ical;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser implements AutoCloseable {

    private final BufferedReader reader;

    public Parser(InputStream is) {
        this.reader = new BufferedReader(
                new InputStreamReader(new UnfoldingInputStream(is), StandardCharsets.UTF_8)
        );
    }


    private final Stack<V> stack = new Stack<>();

    public boolean hasNext() throws IOException {
        readNext();
        return !stack.isEmpty();
    }

    public V next(){
        return stack.pop();
    }

    private void readNext() throws IOException {
        boolean atEndOrBreak = false;
        String contentLine = null;
        do {
            contentLine = reader.readLine();
            if(contentLine!=null) {
                int firstIndexOfColon = contentLine.indexOf(':');
                String nameAndParams = contentLine.substring(0, firstIndexOfColon);
                int indexOfSemiColon = nameAndParams.indexOf(';');
                String name = nameAndParams;
                var params = new HashMap<String, String>();
                if(indexOfSemiColon>0) {
                    name = nameAndParams.substring(0, indexOfSemiColon);
                    var paramList = nameAndParams.substring(indexOfSemiColon + 1).split(",");
                    for(var p: paramList) {
                        var pair = p.split("=");
                        params.put(pair[0], pair[1]);
                    }
                }
                String value = contentLine.substring(firstIndexOfColon+1);
                if(name.equalsIgnoreCase("BEGIN")) {
                    stack.push(new CalComponent(value, new ArrayList<>()));
                }
                else if(name.equalsIgnoreCase("END")) {
                    var top = stack.pop();
                    if(top instanceof CalComponent comp && comp.type().equalsIgnoreCase(value)) {
                        if(stack.top() instanceof CalComponent parent) {
                            parent.subComponents().add(top);
                        }
                        else if(stack.isEmpty()) {
                            stack.push(top);
                            atEndOrBreak = true;
                        }
                        else {
                            throw new AssertionError();
                        }
                    }
                    else {
                        throw new AssertionError();
                    }
                } else if(stack.top() instanceof CalComponent recent) {
                    recent.subComponents().add(new ContentLine(name, params, value));
                }
                else {
                    stack.push(new ContentLine(name, params, value));
                    atEndOrBreak = true;
                }
            } else {
                atEndOrBreak = true;
            }
        } while (!atEndOrBreak);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
