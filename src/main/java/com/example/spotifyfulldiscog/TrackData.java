package com.example.spotifyfulldiscog;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class TrackData implements Serializable {
    private SimpleStringProperty name;
    private SimpleStringProperty id;
    private SimpleStringProperty time;
    private SimpleStringProperty artists;
    private SimpleStringProperty releaseDate;
    private SimpleStringProperty album;

    public int getDateComp() {
        return Integer.parseInt(releaseDate.get().replace("-",""));
    }

    public TrackData(String name, String id, Integer time, String releaseDate, String album, String artists) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        if (time / 1000 % 60 < 10) {
            this.time = new SimpleStringProperty(time / 1000 / 60 + ":0" + time / 1000 % 60);
        } else {
            this.time = new SimpleStringProperty(time / 1000 / 60 + ":" + time / 1000 % 60);
        }
        this.releaseDate = new SimpleStringProperty(releaseDate);
        this.album = new SimpleStringProperty(album);
        this.artists = new SimpleStringProperty(artists);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackData)) return false;

        TrackData trackData = (TrackData) o;

        if (!getName().equals(trackData.getName())) return false;
        if (!getTime().equals(trackData.getTime())) return false;
        return getArtists().equals(trackData.getArtists());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getTime().hashCode();
        result = 31 * result + getArtists().hashCode();
        return result;
    }

    public String getAlbum() {
        return album.get();
    }

    public SimpleStringProperty albumProperty() {
        return album;
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }


    public String getReleaseDate() {
        return releaseDate.get();
    }

    public SimpleStringProperty releaseDateProperty() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate.set(releaseDate);
    }

    public String getArtists() {
        return artists.get();
    }

    public SimpleStringProperty artistsProperty() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists.set(artists);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
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
}
