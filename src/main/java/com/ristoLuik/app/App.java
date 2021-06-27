package com.ristoLuik.app;

import com.ristoLuik.app.library.TimeParser;
import com.ristoLuik.app.library.dto.HighestVisitingInfo;
import com.ristoLuik.app.library.exception.ParserException;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        TimeParser parser = new TimeParser("kylastusajad.txt");
        try {
            parser.parseFile();
            HighestVisitingInfo highestVisitingInfo = parser.getHighestVisitingTimes();

            System.out.println(
                String.format("%S had the most visitors at the same time (%s)",
                    highestVisitingInfo.times,
                    highestVisitingInfo.numberOfVisitors)
            );
        } catch (ParserException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
