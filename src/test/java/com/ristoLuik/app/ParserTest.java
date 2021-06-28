package com.ristoLuik.app;

import com.ristoLuik.app.library.TimeParser;
import com.ristoLuik.app.library.dto.HighestVisitingInfo;
import com.ristoLuik.app.library.exception.ParserException;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    @Test
    public void shouldNotParseBadFile() throws IOException {
        Path path = Path.of("", "src/test/resources").resolve("BadFile.txt");

        TimeParser parser = new TimeParser(path.toString());
        assertThrows(ParserException.class, () -> {
            parser.parseFile();
        });
    }

    @Test
    public void shouldNotParseBadTimeDelimiter() {
        Path path = Path.of("", "src/test/resources").resolve("BadTimeDelimiter.txt");

        TimeParser parser = new TimeParser(path.toString());
        assertThrows(ParserException.class, () -> {
            parser.parseFile();
        });
    }

    @Test
    public void shouldNotParseBadTime() {
        Path path = Path.of("", "src/test/resources").resolve("badTime.txt");

        TimeParser parser = new TimeParser(path.toString());
        assertThrows(ParserException.class, () -> {
            parser.parseFile();
        });
    }

    @Test
    public void shouldNotParseArrivalAfterLeaving() {
        Path path = Path.of("", "src/test/resources").resolve("ArrivalAfterLeaving.txt");

        TimeParser parser = new TimeParser(path.toString());
        assertThrows(ParserException.class, () -> {
            parser.parseFile();
        });
    }

    @Test
    public void shouldHandleMultipleHighestTimes() throws ParserException, IOException {
        Path path = Path.of("", "src/test/resources").resolve("DualHighestTimes.txt");

        TimeParser parser = new TimeParser(path.toString());
        parser.parseFile();
        HighestVisitingInfo highestVisitingTimes = parser.getHighestVisitingTimes();

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
