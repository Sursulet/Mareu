package com.openclassrooms.mareu.service;

import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.model.RoomItem;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MeetingApiServiceTest {

    private MeetingApiService service;

    @Before
    public void setUp() throws Exception {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getMeetings() {
        List<Meeting> meetings = service.getMeetings();
        assertEquals(6, meetings.size());
    }

    @Test
    public void getRooms() {
        List<RoomItem> meetings = service.getRooms();
        List<RoomItem> expectedRooms = DummyRoomGenerator.DUMMY_ROOMS;
        assertThat(meetings, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedRooms.toArray()));
    }

    @Test
    public void addMeeting() {
        service.getMeetings().clear();
        List<String> EMAILS = Arrays.asList("xyz@example", "abc@example", "uvw@example.com");
        Meeting meeting = new Meeting("Réunion A", "23/02/2020", "09:00","01", EMAILS);
        service.addMeeting(meeting);
        assertEquals(1, service.getMeetings().size());
    }

    @Test
    public void deleteMeeting() {
        List<String> EMAILS = Arrays.asList("xyz@example", "abc@example", "uvw@example.com");
        Meeting meeting = new Meeting("Réunion A", "23/02/2020", "09:00","01", EMAILS);
        service.addMeeting(meeting);
        Meeting neighbourToDelete = service.getMeetings().get(0);
        service.deleteMeeting(neighbourToDelete);
        assertFalse(service.getMeetings().contains(neighbourToDelete));
    }

    @Test
    public void getFilterByDay() {
        List<Meeting> mFilteredList = service.getFilterByDay("28/03/2020");
        assertEquals(1, mFilteredList.size());
    }

    @Test
    public void getFilterByRoom() {
        List<Meeting> mFilteredList = service.getFilterByRoom("07");
        assertEquals(1, mFilteredList.size());
    }
}