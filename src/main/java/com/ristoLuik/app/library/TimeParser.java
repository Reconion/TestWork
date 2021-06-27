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
    private HashMap<LocalDateTime, Integer> visitorsPerMinute = new HashMap<>();
    private String fileName;

    private static final String DELIMITER = ",";
    private static final String TIME_DELIMITER = ":";

    public TimeParser(String fileName) {
        this.fileName = fileName;
    }

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
