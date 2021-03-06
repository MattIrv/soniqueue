package mattirv.soniqueue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matthew on 11/15/14.
 */
public class MakeRequest {
    public static void makeSearch(final SongSearch context, String searchTerm, String userName) {
        HttpClient client = new DefaultHttpClient();
        searchTerm = searchTerm.replace(" ", "+");
        String url = "https://api.spotify.com/v1/search?q=" + searchTerm + "&type=track&market=US&limit=10";
        HttpGet request = new HttpGet(url);
        HttpResponse response;
        try {
            response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
                throw new IOException("Request failed with status " + status.getStatusCode());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            JSONObject o = new JSONObject(result.toString());
            JSONObject tracks = o.getJSONObject("tracks");
            JSONArray items = tracks.getJSONArray("items");
            int len = items.length();
            ArrayList<Song> songs = new ArrayList<Song>();
            for (int i=0; i<len; i++) {
                Song song = new Song();
                JSONObject track = items.getJSONObject(i);
                JSONObject album = track.getJSONObject("album");
                String albumName = album.getString("name");
                song.album = albumName;
                JSONArray imageArray = album.getJSONArray("images");
                if (imageArray.length() > 0) {
                    JSONObject lastImage = imageArray.getJSONObject(imageArray.length()-1);
                    String imgURL = lastImage.getString("url");
                    song.setImage(imgURL);
                }
                JSONArray artists = track.getJSONArray("artists");
                String artist = artists.getJSONObject(0).getString("name");
                song.artist = artist;
                String songName = track.getString("name");
                song.songName = songName;
                String songID = track.getString("id");
                song.spotify_id = songID;
                song.queuedBy = userName;
                songs.add(song);
            }
            final ArrayList<Song> finalSongs = songs;
            context.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    context.updateSongs(finalSongs);
                }
            });
        }
        catch (IOException e) {
            System.out.println("ERROR: " + e);
            return;
        }
        catch (JSONException e) {
            System.out.println("ERROR: " + e);
            return;
        }
    }
    public static Bitmap downloadBitmap(String url) throws IOException {
        HttpUriRequest request = new HttpGet(url.toString());
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(entity);

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                    bytes.length);
            return bitmap;
        } else {
            System.out.println("Download failed, HTTP response code "
                    + statusCode + " - " + statusLine.getReasonPhrase());
        }
        return null;
    }

    public static void getParties(final PartyList context) {
        HttpClient client = new DefaultHttpClient();
        String url = "http://soniqueue.com/lobby/list";
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
                throw new IOException("Request failed with status " + status.getStatusCode());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            JSONObject o = new JSONObject(result.toString());
            JSONArray partyList = o.getJSONArray("party_list");
            int len = partyList.length();
            ArrayList<Party> parties = new ArrayList<Party>();
            for (int i=0; i<len; i++) {
                Party party = new Party();
                JSONObject p = partyList.getJSONObject(i);
                int partyId = p.getInt("party_id");
                String name = p.getString("name");
                String location = p.getString("location");
                int hostId = p.getInt("host_id");
                String hostAlias = p.getString("host_alias");
                party.partyId = partyId;
                party.partyName = name;
                party.location = location;
                party.hostId = hostId;
                party.hostAlias = hostAlias;
                parties.add(party);
            }
            final ArrayList<Party> finalParties = parties;
            context.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    context.updateParties(finalParties);
                }
            });
        }
        catch (IOException e) {
            System.out.println("ERROR: " + e);
            return;
        }
        catch (JSONException e) {
            System.out.println("ERROR: " + e);
            return;
        }
    }

    public static int createParty(int userId, String name, String location) {
        HttpClient client = new DefaultHttpClient();
        String url = "http://soniqueue.com/lobby/create/" + userId;
        HttpPost request = new HttpPost(url);
        JSONObject json = new JSONObject();
        try {
            json.put("party_name", name);
            json.put("location", location);
            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            request.setEntity(se);
            HttpResponse response;
            response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
                throw new IOException("Request failed with status " + status.getStatusCode());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            JSONObject o = new JSONObject(result.toString());
            int partyId = o.getInt("party_id");
            MyUser.partyId = o.getInt("party_id");
            return partyId;
        }
        catch (IOException e) {
            System.out.println("ERROR (" + url + "): " + e);
            return -1;
        }
        catch (JSONException e) {
            System.out.println("ERROR: (" + url + "): " + e);
            return -1;
        }
    }

    public static int login(String email, String location) {
        HttpClient client = new DefaultHttpClient();
        String url = "http://soniqueue.com/lobby/login";
        HttpPost request = new HttpPost(url);
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("location", location);
            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            request.setEntity(se);
            HttpResponse response;
            response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
                throw new IOException("Request failed with status " + status.getStatusCode());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            JSONObject o = new JSONObject(result.toString());
            int userId = o.getInt("user_id");
            MyUser.userId = userId;
            //MyUser.partyId = o.getInt("party_id");
            MyUser.alias = o.getString("alias");
            MyUser.email = o.getString("email");
            MyUser.location = o.getString("location");
            return userId;
        }
        catch (IOException e) {
            System.out.println("ERROR: " + e);
            return -1;
        }
        catch (JSONException e) {
            System.out.println("ERROR: " + e);
            return -1;
        }
    }

    public static void getUserPosition(final MyQueue context, int party_id, int user_id){
        HttpClient client = new DefaultHttpClient();
        String url = "http://soniqueue.com/party/" + party_id + "/getposition/" + user_id;
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
                throw new IOException("Request failed with status " + status.getStatusCode());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            JSONObject o = new JSONObject(result.toString());
            final int user_pos = o.getInt("user_pos");

            context.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    context.updateUserPosition(user_pos);
                }
            });
        }
        catch (IOException e) {
            System.out.println("ERROR: " + e);
            return;
        }
        catch (JSONException e) {
            System.out.println("ERROR: " + e);
            return;
        }
    }

    public static void getPartyQueue(final NowPlaying context) {
        HttpClient client = new DefaultHttpClient();
        int pid = context.partyId;
        String url = "http://soniqueue.com/party/"+pid+"/list";
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
                throw new IOException("Request failed with status " + status.getStatusCode());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            JSONObject o = new JSONObject(result.toString());
            JSONArray partyList = o.getJSONArray("party_queue");
            int len = partyList.length();
            ArrayList<Song> rgsong = new ArrayList<Song>();
            for (int i=0; i<len; i++) {
                JSONObject p = partyList.getJSONObject(i);

                String imageURL = p.getString("album_cover_url");
                String songName = p.getString("song_name");
                String artist = p.getString("artist_name");
                String album = p.getString("album_name");
                String queuedBy = p.getString("user_alias");
                String song_id = p.getString("song_id");
                String spotify_id = p.getString("spotify_id");

                rgsong.add(new Song(imageURL,songName,artist,album,queuedBy,song_id,spotify_id));
            }
            final ArrayList<Song> rgsongFinal = rgsong;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    context.updateSongs(rgsongFinal);
                }
            });
        }
        catch (IOException e) {
            System.out.println("ERROR: " + e);
            return;
        }
        catch (JSONException e) {
            System.out.println("ERROR: " + e);
            return;
        }
    }

    public static void getUserQueue(final MyQueue context, int uId) {
        HttpClient client = new DefaultHttpClient();
        String url = "http://soniqueue.com/user/"+uId+"/list";
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
                throw new IOException("Request failed with status " + status.getStatusCode());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            JSONObject o = new JSONObject(result.toString());
            JSONArray partyList = o.getJSONArray("user_queue");
            int len = partyList.length();
            ArrayList<Song> rgsong = new ArrayList<Song>();
            for (int i=0; i<len; i++) {
                JSONObject p = partyList.getJSONObject(i);

                String imageURL = p.getString("album_cover_url");
                String songName = p.getString("song_name");
                String artist = p.getString("artist_name");
                String album = p.getString("album_name");
                String queuedBy = p.getString("user_alias");
                String song_id = p.getString("song_id");
                String spotify_id = p.getString("spotify_id");

                rgsong.add(new Song(imageURL,songName,artist,album,queuedBy,song_id,spotify_id));
            }
            final ArrayList<Song> rgsongFinal = rgsong;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    context.updateSongs(rgsongFinal);
                }
            });
        }
        catch (IOException e) {
            System.out.println("ERROR: " + e);
            return;
        }
        catch (JSONException e) {
            System.out.println("ERROR: " + e);
            return;
        }
    }



    public static void playNextSong(final MusicPlayer context, int partyID) {
        HttpClient client = new DefaultHttpClient();
        String url = "http://soniqueue.com/party/" + partyID + "/next";
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
                throw new IOException("Request to url " + url + "failed with status " + status.getStatusCode());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            JSONObject o = new JSONObject(result.toString());
            //{song_id: Int, spotify_id: String, user_id: Int, user_alias: String}
            int songID = o.getInt("song_id");
            String spotifyID = o.getString("spotify_id");
            int userID = o.getInt("user_id");
            String userAlias = o.getString("user_alias");
            Song song = new Song();
            song.song_id = songID + "";
            song.queuedBy = userAlias;
            song.spotify_id = spotifyID;
            MusicPlayer.getInstance().play(song);
        }
        catch (IOException e) {
            Log.e("MakeRequest: getNowPlaying", e.toString());
        }
        catch (JSONException e) {
            Log.e("MakeRequest: getNowPlaying", e.toString());
        }
    }

    public static void endParty(final PartyScreen context){
        HttpClient client = new DefaultHttpClient();
        int pid = context.partyId;
        String url = "http://soniqueue.com/party/"+pid+"/end";
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void removeSongFromQueue(String uid, String sID) {
        HttpClient client = new DefaultHttpClient();
        String url = "http://soniqueue.com/user/" + uid + "/remove/" + sID;
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addSong(int user_id, String spotify_id, String songName, String artistName, String albumName, String albumCoverURL){
        HttpClient client = new DefaultHttpClient();
        String url = "http://soniqueue.com/user/" + user_id + "/add/" + spotify_id;
        HttpPost request = new HttpPost(url);
        JSONObject json = new JSONObject();
        HttpResponse response;
        try {
            json.put("song_name", songName);
            json.put("artist_name", artistName);
            json.put("album_name", albumName);
            json.put("album_cover_url", albumCoverURL);
            //Log.d("MakeRequest", json.toString());
            StringEntity se = new StringEntity(json.toString());
            se.setContentType("application/json");
            request.setEntity(se);
            response = client.execute(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void moveSong(int user_id, String song_id, int displacement){
        HttpClient client = new DefaultHttpClient();
        String url = "http://soniqueue.com/user/" + user_id + "/move/" + song_id + "/" + displacement;
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void leaveParty(final PartyScreen context, int user_id){
        HttpClient client = new DefaultHttpClient();
        int party_id = context.partyId;
        String url = "http://soniqueue.com/party/" + party_id + "/leave/" + user_id;
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void nowPlayingFromPartyScreen(final PartyScreen context){
        HttpClient client = new DefaultHttpClient();
        int party_id = context.partyId;
        String url = "http://soniqueue.com/party/" + party_id + "/nowplaying";
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
                throw new IOException("Request to url " + url + "failed with status " + status.getStatusCode());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            Log.d("MakeRequest: nowPlayingFromPartyScreen", result.toString());

            JSONObject o = new JSONObject(result.toString());
            //{song_id: Int, spotify_id: String, user_id: Int, user_alias: String}
            int songID = o.getInt("song_id");
            String spotifyID = o.getString("spotify_id");
            int userID = o.getInt("user_id");
            String userAlias = o.getString("user_alias");
            String imgURL = o.getString("album_cover_url");
            String songName = o.getString("song_name");
            String artistName = o.getString("artist_name");
            String albumName = o.getString("album_name");
            Song song = new Song();
            song.song_id = songID + "";
            song.queuedBy = userAlias;
            song.spotify_id = spotifyID;
            song.songName = songName;
            song.album = albumName;
            song.artist = artistName;
            song.setImage(imgURL);
            final Song finalSong = song;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    context.setNowPlaying(finalSong);
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void nowPlayingFromMyQueue(final NowPlaying context){
        HttpClient client = new DefaultHttpClient();
        int party_id = context.partyId;
        String url = "http://soniqueue.com/party/" + party_id + "/nowplaying";
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() >= 300) {
                throw new IOException("Request to url " + url + "failed with status " + status.getStatusCode());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            Log.d("MakeRequest: nowPlayingFromPartyScreen", result.toString());

            JSONObject o = new JSONObject(result.toString());
            //{song_id: Int, spotify_id: String, user_id: Int, user_alias: String}
            int songID = o.getInt("song_id");
            String spotifyID = o.getString("spotify_id");
            int userID = o.getInt("user_id");
            String userAlias = o.getString("user_alias");
            String imgURL = o.getString("album_cover_url");
            String songName = o.getString("song_name");
            String artistName = o.getString("artist_name");
            String albumName = o.getString("album_name");
            Song song = new Song();
            song.song_id = songID + "";
            song.queuedBy = userAlias;
            song.spotify_id = spotifyID;
            song.songName = songName;
            song.album = albumName;
            song.artist = artistName;
            song.setImage(imgURL);
            final Song finalSong = song;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    context.setNowPlaying(finalSong);
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void clearQueue(int user_id){
        HttpClient client = new DefaultHttpClient();
        String url = "http://soniqueue.com/user/" + user_id + "/clear";
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void joinParty(int user_id, int party_id) {
        HttpClient client = new DefaultHttpClient();
        String url = "http://soniqueue.com/party/" + party_id + "/join/" + user_id;
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
