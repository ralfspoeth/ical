package com.github.ralfspoeth.ical;

import java.util.List;

record CalComponent(String type, List<V> subComponents) implements V {
}
