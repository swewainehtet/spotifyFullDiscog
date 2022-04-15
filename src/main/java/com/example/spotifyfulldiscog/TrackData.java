package com.example.spotifyfulldiscog;

import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TrackData implements Serializable {
    private SimpleStringProperty name;
    private SimpleStringProperty id;
    private String time;
    private String[] artists;

    public TrackData(String name, String id) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
    }

    public String getName() {
        return name.get();
    }

    public String getId() {
        return id.get();
    }

    public String[] getURI() {
        return new String[]{"spotify:track:" + id.get()};
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setId(String id) {
        this.id.set(id);
    }

    @Override
    public String toString() {
        return "TrackData{" + "name=" + name + ", id=" + id + '}';
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(getName());
        out.writeObject(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackData trackData)) return false;

        return getName().equals(trackData.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
