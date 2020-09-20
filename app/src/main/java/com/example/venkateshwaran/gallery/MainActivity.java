package com.example.venkateshwaran.gallery;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {


    ArrayList<image> f = new ArrayList<image>();
    GridView imagegrid;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } else {
            // Permission has already been granted
            initialSetup(savedInstanceState);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void initialSetup(Bundle savedInstanceState) {
        getFromSdcard();
        imagegrid = (GridView) findViewById(R.id.gridview);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
        imagegrid.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Intent myIntent;
        myIntent = new Intent(this, viewimage.class);
        myIntent.putExtra("i", position);
        myIntent.putExtra("e", f);
        startActivity(myIntent);
    }


    public void getFromSdcard() {
             /*   File file= new File(android.os.Environment.getExternalStorageDirectory(),"/Pictures/Saved Pictures");
                if (file.isDirectory())
                {
                    listFile = file.listFiles();
                    for (int i = 0; i < listFile.length; i++) {
                        f.add(listFile[i].getAbsolutePath());
                    }
                }*/
        ContentResolver musicResolver = getContentResolver();
        Uri videoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(videoUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            int idColumn = musicCursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
            do {
                String thisId = musicCursor.getString(idColumn);
                int column_index = musicCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                f.add(new image(musicCursor.getString(column_index), thisId));
            }
            while (musicCursor.moveToNext());
            Collections.sort(f, new Comparator<image>() {
                public int compare(image a, image b) {
                    return b.getdate().compareTo(a.getdate());
                }
            });
        }

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return f.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.gridlayout, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (holder.imageview.getTag() != null) {
                ((ImageGetter) holder.imageview.getTag()).cancel(true);
            }
            ImageGetter task = new ImageGetter(holder.imageview);
            task.execute(f.get(position).getTitle());
            holder.imageview.setTag(task);
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageview;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);
        return bm;
    }

    public class ImageGetter extends AsyncTask<String, Void, Bitmap> {
        private ImageView iv;

        public ImageGetter(ImageView v) {
            iv = v;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return decodeSampledBitmapFromUri(params[0], 220, 220);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            iv.setImageBitmap(result);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
