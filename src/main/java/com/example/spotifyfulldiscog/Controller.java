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
import java.util.List;

public class Controller {
    private Utility util = new Utility();
    private String token;
    private SpotifyWrapper wrapper = null;
    private List<TrackData> playlistTracks = new ArrayList<>();

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

    @FXML // fx:id="tvTracks";
    private TableView<TrackData> tvTracks = new TableView<>();

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
        // btnAutoSearch.setDisable(false);
    }

    public void exportCSV() {
        String CSV_SEPARATOR = ",";
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("tracks.csv")));
            for (TrackData track : playlistTracks) {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(track.getName());

                oneLine.append(CSV_SEPARATOR);
                oneLine.append(track.getId());
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generatePlaylist() {
        wrapper.generatePlaylist(playlistTracks);

        // Update Alert
        lblAlert.setText("Generated Playlist");
    }

    public void getTracks() {
        // Clear table
        tvTracks.getItems().clear();
        tvTracks.getColumns().clear();

        List<TrackData> tracks = wrapper.getAllTracks(tfArtistID.getText());
        List<TrackData> trackCleaned = util.clearDuplicates(tracks);

        System.out.println(wrapper.getAllTracks(tfArtistID.getText()));

        // Convert pulled data to trackData
        ObservableList<TrackData> data = FXCollections.observableArrayList();

        for (TrackData track : trackCleaned) {
            // Add tracks to playlist holder
            playlistTracks.add(track);

            // Add tracks to tableview holder
            data.add(track);
        }

        // Create columns
        TableColumn nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn idCol = new TableColumn("Track ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Update Track Count
        lblTrackCount.setText(String.valueOf(trackCleaned.size()));

        // Add data to table
        System.out.println(data);
        tvTracks.getColumns().addAll(nameCol, idCol);
        tvTracks.setItems(data);
    }
}