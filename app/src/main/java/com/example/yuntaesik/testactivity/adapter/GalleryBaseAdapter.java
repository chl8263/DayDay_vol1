package com.example.yuntaesik.testactivity.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.yuntaesik.testactivity.R;
import com.example.yuntaesik.testactivity.function.EnlargementActivity;
import com.example.yuntaesik.testactivity.video.MyVideoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by YunTaeSik on 2016-08-05.
 */
public class GalleryBaseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> strings = new ArrayList<>();
    private ImageView grid_item_image;
    private TextView count_view;
    private MyVideoView grid_item_video;
    private String uri;
    private ImageButton enlargement_btn;

    public GalleryBaseAdapter(Context context, ArrayList<String> strings) {
        super();
        this.context = context;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.gallery_grid_item, parent, false);
        grid_item_image = (ImageView) v.findViewById(R.id.grid_item_image);
        grid_item_video = (MyVideoView) v.findViewById(R.id.grid_item_video);
        count_view = (TextView) v.findViewById(R.id.count_view);
        enlargement_btn = (ImageButton) v.findViewById(R.id.enlargement_btn);

        count_view.setText((position + 1) + "/" + strings.size());
        uri = strings.get(position);
        /*Picasso.with(context).load(uri).into(grid_item_image);*/
        //Log.e("gallery", strings.get(position));
        enlargement_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EnlargementActivity.class);
                intent.putExtra("uri", strings.get(position));
                context.startActivity(intent);
            }
        });
        if (!(uri.indexOf("images") == -1)) {
            Picasso.with(context).load(Uri.parse(uri)).into(grid_item_image);
            grid_item_image.setVisibility(View.VISIBLE);
            grid_item_video.setVisibility(View.GONE);
            enlargement_btn.setVisibility(View.GONE);
        } else if (!(uri.indexOf("video") == -1)) {
            MediaController controller = new MediaController(context);
            grid_item_video.setMediaController(controller);
            grid_item_video.setVideoURI(Uri.parse(uri));
            grid_item_image.setVisibility(View.GONE);
            grid_item_video.setVisibility(View.VISIBLE);
            enlargement_btn.setVisibility(View.VISIBLE);
        } else if (!(uri.indexOf("Today_Record") == -1)) {
            if (!(uri.indexOf("RC") == -1)) {
                grid_item_image.setVisibility(View.GONE);
                grid_item_video.setVisibility(View.VISIBLE);
                enlargement_btn.setVisibility(View.VISIBLE);
                MediaController mediaController = new MediaController(context);
                mediaController.setAnchorView(grid_item_video);
                grid_item_video.setMediaController(new MediaController(context));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String test = "content://media/external/video/media/"
                            + String.valueOf(getVideoIdFromFilePath(Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/" + uri, context.getContentResolver()));
                    grid_item_video.setVideoURI(Uri.parse(test));
                } else {
                    grid_item_video.setVideoPath("/sdcard/" + uri);
                }
                grid_item_video.requestFocus();
            } else if (!(uri.indexOf("P") == -1)) {
                Log.e("uri", uri);
                Picasso.with(context).load(new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/" + uri)).into(grid_item_image);
                grid_item_image.setVisibility(View.VISIBLE);
                grid_item_video.setVisibility(View.GONE);
                enlargement_btn.setVisibility(View.GONE);
            }
        }
        return v;
    }

    private long getVideoIdFromFilePath(String filePath, ContentResolver contentResolver) {
        try {
            long videoId;
            Uri videosUri = MediaStore.Video.Media.getContentUri("external");
            String[] projection = {MediaStore.Video.VideoColumns._ID};
            // TODO This will break if we have no matching item in the MediaStore.
            Cursor cursor = contentResolver.query(videosUri, projection, MediaStore.Video.VideoColumns.DATA + " LIKE ?", new String[]{filePath}, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            videoId = cursor.getLong(columnIndex);
            cursor.close();
            return videoId;
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }
}




