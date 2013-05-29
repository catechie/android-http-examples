package com.publicobject.pictures;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PicturesActivity extends Activity {
  private ImagesLoader imagesLoader = new ImagesLoader();
  private LinearLayout progress;
  private TextView error;
  private ListView picturesList;
  private ImageListAdapter adapter;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    progress = (LinearLayout) findViewById(R.id.progress);
    picturesList = (ListView) findViewById(R.id.picturesList);
    error = (TextView) findViewById(R.id.error);

    adapter = new ImageListAdapter();
    picturesList.setAdapter(adapter);

    error.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        imagesLoader.load(getApp());
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();
    imagesLoader.setTarget(this);
    imagesLoader.load(getApp());
  }

  @Override protected void onPause() {
    imagesLoader.setTarget(null);
    super.onPause();
  }

  private PicturesApp getApp() {
    return (PicturesApp) getApplication();
  }

  public static class ImagesLoader {
    PicturesActivity target;

    public void setTarget(PicturesActivity target) {
      this.target = target;
    }

    public void load(PicturesApp app) {
      target.progress.setVisibility(View.VISIBLE);
      target.picturesList.setVisibility(View.GONE);
      target.error.setVisibility(View.GONE);

      ImageService imageService = app.getImageService();
      imageService.listImages("", new Callback<List<String>>() {
        @Override public void success(List<String> images, Response response) {
          if (target != null) {
            target.progress.setVisibility(View.GONE);
            target.picturesList.setVisibility(View.VISIBLE);
            target.error.setVisibility(View.GONE);
            target.adapter.images = images;
            target.adapter.notifyDataSetChanged();
          }
        }

        @Override public void failure(RetrofitError retrofitError) {
          if (target != null) {
            target.error.setVisibility(View.VISIBLE);
            target.progress.setVisibility(View.GONE);
            target.picturesList.setVisibility(View.GONE);
            target.error.setText("Error: " + retrofitError.getMessage());
          }
        }
      });
    }
  }

  public class ImageListAdapter extends BaseAdapter {
    private List<String> images = Collections.emptyList();

    @Override public int getCount() {
      return images.size();
    }

    @Override public Object getItem(int position) {
      return images.get(position);
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      LinearLayout view = convertView != null
          ? (LinearLayout) convertView
          : (LinearLayout) getLayoutInflater().inflate(R.layout.image, parent, false);
      TextView text = (TextView) view.findViewById(R.id.text);
      text.setText(images.get(position));
      return view;
    }
  }
}
