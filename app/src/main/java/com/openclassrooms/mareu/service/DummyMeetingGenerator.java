package com.openclassrooms.mareu.service;

import com.openclassrooms.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyMeetingGenerator {

    static List<Meeting> generateMeetings() { return new ArrayList<>(DUMMY_MEETINGS); }

    private static List<String> DUMMY_MAILS = Arrays.asList("xyz@example", "abc@example", "uvw@example.com");
    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting("Meeting A", "26/02/2020", "09:00", "07", DUMMY_MAILS),
            new Meeting("Meeting B", "28/03/2020", "10:00", "01", DUMMY_MAILS),
            new Meeting("Meeting C", "27/02/2020", "11:00", "02", DUMMY_MAILS),
            new Meeting("Meeting D", "20/03/2020", "14:00", "10", DUMMY_MAILS),
            new Meeting("Meeting E","29/02/2020","09:00", "04", DUMMY_MAILS),
            new Meeting("Meeting F","29/02/2020","09:00", "03", DUMMY_MAILS)
    );
}
