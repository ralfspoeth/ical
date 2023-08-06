package com.github.ralfspoeth.ical;

import java.util.Map;

record ContentLine(String name, Map<String, String> params, String value) implements V {
}
