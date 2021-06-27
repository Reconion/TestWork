package com.ristoLuik.app.library.dto;

import java.util.LinkedList;

public final class HighestVisitingInfo {
    public final int numberOfVisitors;
    public final LinkedList<String> times;

    public HighestVisitingInfo(int numberOfVisitors, LinkedList<String> times) {
        this.numberOfVisitors = numberOfVisitors;
        this.times = times;
    }
}
