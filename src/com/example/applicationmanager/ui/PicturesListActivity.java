
package com.example.applicationmanager.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.applicationmanager.R;

public class PicturesListActivity extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ImageListAdapter mAdapter;

    private GridView mGridView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_pictures_list);

        mGridView = (GridView)findViewById(R.id.gridview);

        mAdapter = new ImageListAdapter(getApplicationContext(), null, true);

        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            }
        });
        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        String[] proj = {
                MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE
        };

        Uri CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return new CursorLoader(this, CONTENT_URI, proj, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor newCursor) {
        mAdapter.swapCursor(newCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapter.swapCursor(null);
    }

    private class ImageListAdapter extends CursorAdapter {

        public ImageListAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            String displayName = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
            String videoSize = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));

            long videoId = cursor
                    .getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(),

            videoId, MediaStore.Images.Thumbnails.MINI_KIND, options);

            System.out.println(" thumbnail === > " + thumbnail);

            ViewHolder holder = (ViewHolder)view.getTag();
            holder.thumbnailImage.setImageBitmap(thumbnail);

            // holder.textView.setText(displayName);

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View retView = inflater.inflate(R.layout.video_item_list_item, null);

            ViewHolder holder = new ViewHolder();
            holder.thumbnailImage = (ImageView)retView.findViewById(R.id.img_video);
            //
            // holder.textView =
            // (TextView)retView.findViewById(R.id.txt_installed_app_name);
            // holder.textView.setText(displayName);
            retView.setTag(holder);
            return retView;
        }

    }

    static class ViewHolder {
        ImageView thumbnailImage;

        TextView textView;
    }

}
