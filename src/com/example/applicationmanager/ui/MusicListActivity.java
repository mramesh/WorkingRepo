
package com.example.applicationmanager.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.applicationmanager.R;

public class MusicListActivity extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    VideoListAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        mAdapter = new VideoListAdapter(getApplicationContext(), null, true);
        getSupportLoaderManager().initLoader(1, null, this);
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION
        };

        Uri CONTENT_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return new CursorLoader(this, CONTENT_URI, projection, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor newCursor) {
        mAdapter.swapCursor(newCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapter.swapCursor(null);
    }

    private class VideoListAdapter extends CursorAdapter {

        public VideoListAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            String displayName = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            long duration = cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            ViewHolder holder = (ViewHolder)view.getTag();

            holder.textView.setText(displayName);
            holder.imgIcon.setImageResource(R.drawable.ic_pdf);

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View retView = inflater.inflate(R.layout.music_list_item, null);

            ViewHolder holder = new ViewHolder();

            holder.textView = (TextView)retView.findViewById(R.id.title);

            holder.imgIcon = (ImageView)retView.findViewById(R.id.icon);

            retView.setTag(holder);
            return retView;
        }

    }

    static class ViewHolder {
        ImageView imgIcon;

        TextView textView;
    }

}
