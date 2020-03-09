package com.openclassrooms.mareu.service;

import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.model.RoomItem;

import java.util.List;

public interface MeetingApiService {
    List<Meeting> getMeetings();
    List<RoomItem> getRooms();
    void addMeeting(Meeting meeting);
    void deleteMeeting(Meeting meeting);
}
