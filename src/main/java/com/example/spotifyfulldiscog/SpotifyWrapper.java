package com.example.spotifyfulldiscog;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsAlbumsRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpotifyWrapper {
    public SpotifyApi spotifyApi;
    private String userName;
    private String userID;

    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }

    // Constructor for Wrapper
    public SpotifyWrapper(String accessToken) {
        userName = "Invalid";
        User user = null;

        spotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build();
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi.getCurrentUsersProfile()
                .build();
        try {
            user = getCurrentUsersProfileRequest.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert user != null;
        userName = user.getDisplayName();
        userID = user.getId();
    }

    public void generatePlaylist(List<TrackData> playlistTracks) {
        CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userID, "Auto Generated Playlist Demo")
//          .collaborative(false)
//          .public_(false)
                .description("Auto Generated Playlist Demo Custom Description")
                .build();

        try {
            Playlist playlist = createPlaylistRequest.execute();
            String playlistID = playlist.getId();
            System.out.println(playlistID);

            for (int i = 0; i < playlistTracks.size(); i++) {
                AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi
                        .addItemsToPlaylist(playlistID, playlistTracks.get(i).getURI())
                        .build();
                SnapshotResult snapshotResult = addItemsToPlaylistRequest.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<TrackData> getAllTracks(String artistID) {
        GetArtistsAlbumsRequest albumsRequest = spotifyApi.getArtistsAlbums(artistID).album_type("album").limit(50).build();
        GetArtistsAlbumsRequest singlesRequest = spotifyApi.getArtistsAlbums(artistID).album_type("single").limit(50).build();
        GetArtistsAlbumsRequest compilationsRequest = spotifyApi.getArtistsAlbums(artistID).album_type("compilation").limit(50).build();

        List<TrackData> trackData = new ArrayList<>();
        try {
            final Paging<AlbumSimplified> albumSimplifiedPaging = albumsRequest.execute();
            List<AlbumSimplified> album = Arrays.stream(albumSimplifiedPaging.getItems()).toList();

            for (AlbumSimplified albumSimplified : album) {
                String albumID = albumSimplified.getId();
                GetAlbumsTracksRequest getAlbumsTracksRequest = spotifyApi.getAlbumsTracks(albumID).build();
                Paging<TrackSimplified> trackSimplifiedPaging = getAlbumsTracksRequest.execute();

                List<TrackSimplified> tracks = Arrays.stream(trackSimplifiedPaging.getItems()).toList();
                for (TrackSimplified track : tracks) {
                    trackData.add(new TrackData(track.getName(), track.getId()));
                }
            }

            final Paging<AlbumSimplified> singlesSimplifiedPaging = singlesRequest.execute();
            List<AlbumSimplified> singles = Arrays.stream(singlesSimplifiedPaging.getItems()).toList();

            for (AlbumSimplified singlesSimplified : singles) {
                String albumID = singlesSimplified.getId();
                GetAlbumsTracksRequest getAlbumsTracksRequest = spotifyApi.getAlbumsTracks(albumID).build();
                Paging<TrackSimplified> trackSimplifiedPaging = getAlbumsTracksRequest.execute();

                List<TrackSimplified> tracks = Arrays.stream(trackSimplifiedPaging.getItems()).toList();
                for (TrackSimplified track : tracks) {
                    trackData.add(new TrackData(track.getName(), track.getId()));
                }
            }

            final Paging<AlbumSimplified> compSimplifiedPaging = compilationsRequest.execute();
            List<AlbumSimplified> comps = Arrays.stream(compSimplifiedPaging.getItems()).toList();

            for (AlbumSimplified compsSimplified : comps) {
                String albumID = compsSimplified.getId();
                GetAlbumsTracksRequest getAlbumsTracksRequest = spotifyApi.getAlbumsTracks(albumID).build();
                Paging<TrackSimplified> trackSimplifiedPaging = getAlbumsTracksRequest.execute();

                List<TrackSimplified> tracks = Arrays.stream(trackSimplifiedPaging.getItems()).toList();
                for (TrackSimplified track : tracks) {
                    trackData.add(new TrackData(track.getName(), track.getId()));
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return trackData;
    }
}
