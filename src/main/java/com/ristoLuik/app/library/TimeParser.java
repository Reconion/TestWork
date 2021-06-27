package com.ristoLuik.app.library;

import com.ristoLuik.app.library.dto.HighestVisitingInfo;
import com.ristoLuik.app.library.exception.ParserException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TimeParser {
    private final HashMap<LocalDateTime, Integer> visitorsPerMinute = new HashMap<>();
    private final String fileName;

    private static final String DELIMITER = ",";
    private static final String TIME_DELIMITER = ":";

    /**
     * constructor
     *
     * @param fileName name of the file needed to parse
     */
    public TimeParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parses the file to see how many visitors were present at any given time
     *
     * @return HashMap<LocalDateTime, Integer> A map of times and amount of visitors with minute percission
     * @throws ParserException When file contains invalid format
     * @throws IOException
     */
    public HashMap<LocalDateTime, Integer> parseFile() throws ParserException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] pieces = line.split(DELIMITER);
                if (pieces.length != 2) {
                    throw new ParserException(String.format("Failed to parse times file: invalid format \"%s}\"", line));
                }
                LocalDateTime arrivalTime = parseTimeString(pieces[0]);
                LocalDateTime leavingTime = parseTimeString(pieces[1]);

                if (arrivalTime.isAfter(leavingTime)) {
                    throw new ParserException(String.format("Arrival time can't be after leaving time (%S)", line));
                }

                visitorsPerMinute.merge(arrivalTime, 1, Integer::sum);
                for (LocalDateTime time = arrivalTime; !(time = time.plusMinutes(1)).isAfter(leavingTime); ) {
                    visitorsPerMinute.merge(time, 1, Integer::sum);
                }
            }
        }

        return visitorsPerMinute;
    }

    /**
     * Finds the times that had the most visitors and returns them with the amount
     *
     * @return HighestVisitingInfo
     */
    public HighestVisitingInfo getHighestVisitingTimes() {
        int maxVisitors = 0;
        LinkedList<String> highestTimes = new LinkedList<>();
        for (Map.Entry<LocalDateTime, Integer> entry : visitorsPerMinute.entrySet()) {
            int visitorsAmount = entry.getValue();
            if (maxVisitors > visitorsAmount) {
                continue;
            }

            LocalDateTime time = entry.getKey();
            if (maxVisitors == visitorsAmount) {
                highestTimes.add(time.format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            if (maxVisitors < visitorsAmount) {
                maxVisitors = visitorsAmount;
                highestTimes.clear();
                highestTimes.add(time.format(DateTimeFormatter.ofPattern("HH:mm")));
            }
        }
        return new HighestVisitingInfo(maxVisitors, highestTimes);
    }

    /**
     * Parses the time to LocalDateTime so it's easier to work with
     *
     * @param timeString time string needed to be parsed, formatted to "HH:mm"
     * @return LocalDateTime parsed time
     * @throws ParserException When time is formatted badly
     */
    private static LocalDateTime parseTimeString(String timeString) throws ParserException {
        String[] pieces = timeString.split(TIME_DELIMITER);
        if (pieces.length != 2) {
            throw new ParserException(String.format("Invalid time format: %S", timeString));
        }
        String hoursString = pieces[0];

        String minutesString = pieces[1];
        try {
            return LocalDateTime
                .MIN
                .withHour(Integer.parseInt(hoursString))
                .withMinute(Integer.parseInt(minutesString));
        } catch (NumberFormatException e) {
            throw new ParserException(String.format("Invalid time format: %S", timeString));
        }
    }
}
