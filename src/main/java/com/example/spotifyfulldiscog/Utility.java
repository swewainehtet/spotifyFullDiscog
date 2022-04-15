package com.example.spotifyfulldiscog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utility {
    public List<TrackData> clearDuplicates(List<TrackData> tracks) {
        Set<TrackData> set = new HashSet<>(tracks);
        return new ArrayList<>(set);
    }
}
