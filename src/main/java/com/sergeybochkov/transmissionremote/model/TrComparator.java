package com.sergeybochkov.transmissionremote.model;

import java.util.Comparator;

public class TrComparator implements Comparator<Tr> {
    @Override
    public int compare(Tr o1, Tr o2) {
        return Integer.compare(o1.id(), o2.id());
    }
}
