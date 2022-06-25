package com.example.spotifyfulldiscog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Controller {
    private final Utility util = new Utility();
    private String token;
    private SpotifyWrapper wrapper = null;
    private final List<TrackData> playlistTracks = new ArrayList<>();

    @FXML // fx:id="lblAlert";
    private Label lblAlert;

    @FXML // fx:id="lblUserName";
    private Label lblUserName;

    @FXML // fx:id="btnSetToken";
    private Button btnSetToken;

    @FXML // fx:id="lblTrackCount";
    private Label lblTrackCount;

    @FXML // fx:id="tfToken";
    private PasswordField tfToken;

    @FXML // fx:id="tfArtistID";
    private TextField tfArtistID;

    @FXML // fx:id="btnSearch";
    private Button btnSearch;

    @FXML // fx:id="btnGenerate";
    private Button btnGenerate;

    @FXML // fx:id="btnExportCSV";
    private Button btnExportCSV;

    @FXML // fx:id="btnViewRecent";
    private Button btnViewRecent;

    @FXML // fx:id="tvTracks";
    private TableView<TrackData> tvTracks = new TableView<>();

    @FXML // fx:id="tfPlaylistName";
    private TextField tfPlaylistName;

    @FXML // fx:id="dpStart";
    private DatePicker dpStart;

    @FXML // fx:id="dpEnd";
    private DatePicker dpEnd;

    public void setToken() {
        wrapper = new SpotifyWrapper(tfToken.getText());

        // Update username
        lblUserName.setText("Logged in as: " + wrapper.getUserName());

        // Enable Features
        btnGenerate.setDisable(false);
        btnSearch.setDisable(false);
        tfArtistID.setDisable(false);
        tvTracks.setDisable(false);
        btnExportCSV.setDisable(false);
        btnViewRecent.setDisable(false);
        // btnAutoSearch.setDisable(false);
    }

    public void viewRecent() {
        List<TrackData> tracks = wrapper.viewRecent();

        // Clear table
        tvTracks.getItems().clear();
        tvTracks.getColumns().clear();

        System.out.println("Exploring recent tracks...");

        List<TrackData> trackCleaned = util.clearDuplicates(tracks);

        // System.out.println(wrapper.getAllTracks(tfArtistID.getText()));
        ObservableList<TrackData> data = FXCollections.observableList(trackCleaned);

        // Convert pulled data to trackData
        // Add tracks to playlist holder
        // Add tracks to tableview holder
        //            data.add(track);
        playlistTracks.addAll(trackCleaned);

        // Create columns
        TableColumn nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn idCol = new TableColumn("Track ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn releaseDateCol = new TableColumn<>("Release Date");
        releaseDateCol.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        TableColumn albumCol = new TableColumn<>("Album");
        albumCol.setCellValueFactory(new PropertyValueFactory<>("album"));
        TableColumn artistsCol = new TableColumn<>("Artists");
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));

        // Update Track Count
        lblTrackCount.setText(String.valueOf(trackCleaned.size()));

        // Add data to table
        System.out.println("Updating Table...");
        // System.out.println(data);
        tvTracks.getColumns().addAll(nameCol, idCol, timeCol, releaseDateCol, albumCol, artistsCol);
        tvTracks.setItems(data);

        tvTracks.refresh();
        data.removeAll();

        System.out.println("Task Complete.");
    }

    public void exportCSV() {
        System.out.println("Exporting .csv");

        String CSV_SEPARATOR = ";";
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("tracks.csv")));
            for (TrackData track : playlistTracks) {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(track.getName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(track.getId());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(track.getTime());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(track.getReleaseDate());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(track.getAlbum());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(track.getArtists());
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Task Complete.");
    }

    public void generatePlaylist() {
        List<TrackData> list = util.clearDuplicates(playlistTracks);
        list.sort(Comparator.comparingInt(TrackData::getDateComp).reversed());

        wrapper.generatePlaylist(list, tfPlaylistName.getText());

        // Update Alert
//        lblAlert.setText("Generated Playlist");
        System.out.println("Task Complete.");
    }

    public void getTracks() {
        // Clear table
        tvTracks.getItems().clear();
        tvTracks.getColumns().clear();

        System.out.println("Compiling Tracks...");

        List<TrackData> tracks = wrapper.getAllTracks(tfArtistID.getText());
        List<TrackData> trackCleaned = util.clearDuplicates(tracks);
        trackCleaned.sort(Comparator.comparingInt(TrackData::getDateComp).reversed());

        int dateStart = 0;
        int dateEnd = Integer.MAX_VALUE;
        if (!(dpStart.getValue() == null)) {
            dateStart = Integer.parseInt(dpStart.getValue().toString().replace("-", ""));
        } else if (!(dpEnd.getValue() == null)) {
            dateEnd = Integer.parseInt(dpEnd.getValue().toString().replace("-", ""));
        }

        for(Iterator<TrackData> iterator = trackCleaned.iterator(); iterator.hasNext();) {
            if (iterator.next().getDateComp() < dateStart || iterator.next().getDateComp() > dateEnd) {
                iterator.remove();
            }
        }

        // System.out.println(wrapper.getAllTracks(tfArtistID.getText()));
        ObservableList<TrackData> data = FXCollections.observableList(trackCleaned);

        // Convert pulled data to trackData
        // Add tracks to playlist holder
        // Add tracks to tableview holder
        //            data.add(track);
        playlistTracks.addAll(trackCleaned);

        // Create columns
        TableColumn nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn idCol = new TableColumn("Track ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn releaseDateCol = new TableColumn<>("Release Date");
        releaseDateCol.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        TableColumn albumCol = new TableColumn<>("Album");
        albumCol.setCellValueFactory(new PropertyValueFactory<>("album"));
        TableColumn artistsCol = new TableColumn<>("Artists");
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));

        // Update Track Count
        lblTrackCount.setText(String.valueOf(trackCleaned.size()));

        // Add data to table
        System.out.println("Updating Table...");
        // System.out.println(data);
        tvTracks.getColumns().addAll(nameCol, idCol, timeCol, releaseDateCol, albumCol, artistsCol);
        tvTracks.setItems(data);

        tvTracks.refresh();
        data.removeAll();

        System.out.println("Task Complete.");
    }
}