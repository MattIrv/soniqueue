package mattirv.soniqueue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
                song.id = songID;
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
}
