package com.openclassrooms.mareu.service;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.model.RoomItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyRoomGenerator {

    static List<RoomItem> generateRooms() { return new ArrayList<>(DUMMY_ROOMS); }

    public static List<RoomItem> DUMMY_ROOMS = Arrays.asList(
            new RoomItem("01", R.color.light_grayish_orange),
            new RoomItem("02", R.color.grayish_cyan_lime_green),
            new RoomItem("03", R.color.very_light_red),
            new RoomItem("04", R.color.light_grayish_blue),
            new RoomItem("05", R.color.light_grayish_blue2),
            new RoomItem("06", R.color.light_grayish_yellow),
            new RoomItem("07", R.color.light_grayish_magenta),
            new RoomItem("08", R.color.light_gray),
            new RoomItem("09", R.color.colorPrimary),
            new RoomItem("10", R.color.very_pale_lime_green)
    );
}
