package com.publicobject.pictures;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestBuilder;
import java.util.Collections;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PicturesActivity extends Activity {
  private PictureListLoader pictureListLoader = new PictureListLoader();
  private LinearLayout progress;
  private TextView error;
  private ListView picturesList;
  private PictureListAdapter adapter;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    progress = (LinearLayout) findViewById(R.id.progress);
    picturesList = (ListView) findViewById(R.id.picturesList);
    error = (TextView) findViewById(R.id.error);

    adapter = new PictureListAdapter();
    picturesList.setAdapter(adapter);

    error.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        pictureListLoader.load(getApp());
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();
    pictureListLoader.setTarget(this);
    pictureListLoader.load(getApp());
  }

  @Override protected void onPause() {
    pictureListLoader.setTarget(null);
    super.onPause();
  }

  private PicturesApp getApp() {
    return (PicturesApp) getApplication();
  }

  public static class PictureListLoader {
    PicturesActivity target;

    public void setTarget(PicturesActivity target) {
      this.target = target;
    }

    public void load(PicturesApp app) {
      target.progress.setVisibility(View.VISIBLE);
      target.picturesList.setVisibility(View.GONE);
      target.error.setVisibility(View.GONE);

      PictureService pictureService = app.getPictureService();
      pictureService.listPictures("", new Callback<List<String>>() {
        @Override public void success(List<String> pictures, Response response) {
          if (target == null) return;

          target.progress.setVisibility(View.GONE);
          target.picturesList.setVisibility(View.VISIBLE);
          target.error.setVisibility(View.GONE);

          target.adapter.setPictureFileNames(pictures);
        }

        @Override public void failure(RetrofitError retrofitError) {
          if (target == null) return;

          target.error.setVisibility(View.VISIBLE);
          target.progress.setVisibility(View.GONE);
          target.picturesList.setVisibility(View.GONE);

          target.error.setText("Error: " + retrofitError.getMessage());
        }
      });
    }
  }

  public class PictureListAdapter extends BaseAdapter {
    private PicturesApp app = getApp();
    private List<String> pictureFileNames = Collections.emptyList();

    public void setPictureFileNames(List<String> pictureFileNames) {
      this.pictureFileNames = pictureFileNames;
      notifyDataSetChanged();
    }

    @Override public int getCount() {
      return pictureFileNames.size();
    }

    @Override public Object getItem(int position) {
      return pictureFileNames.get(position);
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      LinearLayout view = convertView != null
          ? (LinearLayout) convertView
          : (LinearLayout) getLayoutInflater().inflate(R.layout.pictureitem, parent, false);
      TextView text = (TextView) view.findViewById(R.id.text);
      ImageView picture = (ImageView) view.findViewById(R.id.picture);

      String pictureFileName = pictureFileNames.get(position);
      text.setText(pictureFileName);

      // Create a RequestBuilder to fetch the image and load it into the image view.
      Picasso picasso = app.getPicasso();
      RequestBuilder requestBuilder = isImage(pictureFileName)
          ? picasso.load(app.fileToUrl(pictureFileName))
          : picasso.load(R.drawable.doc);

      // Apply local transformations to the image before displaying it.
      requestBuilder.placeholder(R.drawable.loading)
          .resize(300, 260)
          .centerCrop()
          .into(picture);

      return view;
    }
  }

  private boolean isImage(String pictureFileName) {
    return pictureFileName.endsWith(".png")
        || pictureFileName.endsWith(".gif")
        || pictureFileName.endsWith(".jpg")
        || pictureFileName.endsWith(".jpeg");
  }
}
