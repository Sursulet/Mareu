package com.openclassrooms.mareu.model;

import java.util.List;

public class Meeting {

    private String topic;
    private String date;
    private String time;
    private String room;
    private List<String> participants;

    public Meeting(String topic, String date, String time, String room, List<String> participants) {
        this.topic = topic;
        this.date = date;
        this.time = time;
        this.room = room;
        this.participants = participants;
    }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public List<String> getParticipants() { return participants; }
    public void setParticipants(List<String> participants) { this.participants = participants; }
}
