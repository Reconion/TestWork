package com.ristoLuik.app;

import com.ristoLuik.app.library.TimeParser;
import com.ristoLuik.app.library.dto.HighestVisitingInfo;
import com.ristoLuik.app.library.exception.ParserException;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private final static String testResourcePath = "/src/java/com/ristoLuik/app/";

    @Test
    public void shouldNotParseBadFile() {
        TimeParser parser = new TimeParser("BadFile.txt");
        assertThrows(ParserException.class, () -> {
            parser.parseFile();
        });
    }

    @Test
    public void shouldNotParseBadTimeDelimiter() {
        TimeParser parser = new TimeParser("BadTimeDelimiter.txt");
        assertThrows(ParserException.class, () -> {
            parser.parseFile();
        });
    }

    @Test
    public void shouldNotParseBadTime() {
        TimeParser parser = new TimeParser("badTime.txt");
        assertThrows(ParserException.class, () -> {
            parser.parseFile();
        });
    }

    @Test
    public void shouldNotParseArrivalAfterLeaving() {
        TimeParser parser = new TimeParser("ArrivalAfterLeaving.txt");
        assertThrows(ParserException.class, () -> {
            parser.parseFile();
        });
    }

    @Test
    public void shouldHandleMultipleHighestTimes() throws ParserException, IOException {
        TimeParser parser = new TimeParser("DualHighestTimes.txt");
        parser.parseFile();
        HighestVisitingInfo highestVisitingTimes = parser.getHighestVisitingTimes();
        System.out.println(highestVisitingTimes.times);

        LinkedList<String> expectedTimes = new LinkedList<>();
        expectedTimes.add("13:04");
        expectedTimes.add("13:05");
        for (String time : highestVisitingTimes.times) {
            assertTrue(expectedTimes.contains(time));
        }
        for (String time : expectedTimes) {
            assertTrue(highestVisitingTimes.times.contains(time));
        }
        assertEquals(highestVisitingTimes.numberOfVisitors, 2);
    }
}
