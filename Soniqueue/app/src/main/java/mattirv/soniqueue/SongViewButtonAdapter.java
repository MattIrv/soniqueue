package mattirv.soniqueue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Matthew on 11/15/14.
 */
public class SongViewButtonAdapter extends ArrayAdapter<Song> {

    private final Context context;
    private final List<Song> objects;

    public SongViewButtonAdapter(Context context, List<Song> objects) {
        super(context, R.layout.song_list_view_with_button, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Song song = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.song_list_view_with_button, parent, false);
        ImageView albumArtView = (ImageView) rowView.findViewById(R.id.imageview_album_art_b);
        if (song.image != null)
            albumArtView.setImageBitmap(song.image);
        TextView songNameView = (TextView) rowView.findViewById(R.id.textview_song_name_b);
        songNameView.setText(song.songName);
        TextView artistNameView = (TextView) rowView.findViewById(R.id.textview_artist_name_b);
        artistNameView.setText(song.artist);
        TextView albumNameView = (TextView) rowView.findViewById(R.id.textview_album_name_b);
        albumNameView.setText(song.album);

        ImageView btnDel = (ImageView) rowView.findViewById(R.id.btn_song_del);
        btnDel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                AlertDialog.Builder d = new AlertDialog.Builder(context);
                TextView text = new TextView(context);
                text.setText("Are you sure you want to remove "+song.songName+" from the queue?");
                d.setTitle("Confirm remove");
                d.setView(text);
                d.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Thread thread = new Thread(new Runnable() {
                            public void run() {
                                MakeRequest.removeSongFromQueue(MyUser.userId+"", song.song_id);
                            }
                        });
                        thread.start();

                        objects.remove(song);
                        notifyDataSetChanged();
                    }
                });
                d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled. No need to do anything.
                    }
                });
                d.show();
            }
        });

        ImageView btnUp = (ImageView) rowView.findViewById(R.id.btn_song_up);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != 0) {
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            MakeRequest.moveSong(MyUser.userId, song.song_id, -1);
                        }
                    });
                    thread.start();
                    objects.remove(song);
                    objects.add(position-1, song);
                    notifyDataSetChanged();
                }
            }
        });

        ImageView btnDown = (ImageView) rowView.findViewById(R.id.btn_song_down);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != objects.size()-1) {
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            MakeRequest.moveSong(MyUser.userId, song.song_id, 1);
                        }
                    });
                    thread.start();
                    objects.remove(song);
                    objects.add(position+1, song);
                    notifyDataSetChanged();
                }
            }
        });

        return rowView;
    }
}
