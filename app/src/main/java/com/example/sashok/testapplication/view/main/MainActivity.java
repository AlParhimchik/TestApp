package com.example.sashok.testapplication.view.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.sashok.testapplication.Abs.AbsFragment;
import com.example.sashok.testapplication.Abs.AbsUtils;
import com.example.sashok.testapplication.ApiService;
import com.example.sashok.testapplication.App;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.model.Comment;
import com.example.sashok.testapplication.model.Image;
import com.example.sashok.testapplication.model.realm.RealmController;
import com.example.sashok.testapplication.network.model.ResponseConverter;
import com.example.sashok.testapplication.network.model.comment.request.AddCommentRequest;
import com.example.sashok.testapplication.network.model.comment.response.AddCommentResponse;
import com.example.sashok.testapplication.network.model.comment.response.DeleteCommentResponse;
import com.example.sashok.testapplication.network.model.image.ImageResponse;
import com.example.sashok.testapplication.network.model.image.request.AddImageRequest;
import com.example.sashok.testapplication.network.model.image.response.AddImageResponse;
import com.example.sashok.testapplication.network.model.image.response.DeleteImageResponse;
import com.example.sashok.testapplication.utils.LocationUtils;
import com.example.sashok.testapplication.utils.SessionManager;
import com.example.sashok.testapplication.view.main.listener.DataSetChangedListener;
import com.example.sashok.testapplication.view.main.listener.ImageInfoListener;
import com.example.sashok.testapplication.view.main.listener.ImageListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by sashok on 26.10.17.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ImageListener, ImageInfoListener, DataSetChangedListener, OnMapReadyCallback {
    private Toolbar toolbar;
    private FragmentTag CURRENT_TAG;
    private static String BUNDLE_ITEM_ID = "BUNDLE_ITEM_ID";
    private static String BUNDLE_CURRENT_TAG = "cur_tag";

    private static String TAG = "TAG_MAIN_ACTIVITY";
    private static MenuItem cur_item;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private TextView userName;
    private GoogleMap mMap;
    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        userName= (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userName.setText(SessionManager.getLogin(this));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    createScreenShot();

                }


            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        if (savedInstanceState == null) {
            CURRENT_TAG = FragmentTag.TAG_IMAGE_LIST;
            cur_item = navigationView.getMenu().findItem(R.id.photo_folder);
            if (cur_item != null) cur_item.setChecked(true);
            replaceFragment();
        }


    }

    private void createScreenShot() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/TestApp");
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", myDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurrentPhotoPath = image.getAbsolutePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Glide.with(MainActivity.this).load(new File(mCurrentPhotoPath)).asBitmap().toBytes(Bitmap.CompressFormat.JPEG, 70).atMost().fitCenter().override(1200, 800).into(new SimpleTarget<byte[]>() {
                @Override
                public void onResourceReady(byte[] byteArray, GlideAnimation<? super byte[]> glideAnimation) {
                    new File(mCurrentPhotoPath).delete();
                    final String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    final Image image = new Image();
                    image.setDate((int) (Calendar.getInstance().getTimeInMillis() / 1000));
                    final Location myLocation = LocationUtils.getLastBestLocation(MainActivity.this);
                    AddImageRequest request = new AddImageRequest(encoded, image.getDate(), myLocation.getLatitude(), myLocation.getLongitude());
                    ApiService.getInstance().addImage(request).enqueue(new Callback<AddImageResponse>() {
                        @Override
                        public void onResponse(Call<AddImageResponse> call, Response<AddImageResponse> response) {
                            AddImageResponse addImageResponse = response.body();
                            if (addImageResponse == null) {
                                addImageResponse = (AddImageResponse) ResponseConverter.convertErrorResponse(response.errorBody(), AddImageResponse.class);
                            }
                            if (addImageResponse.status == 200) {
                                ImageResponse imageResponse = response.body().getData();
                                Image image1 = new Image(imageResponse);
                                RealmController.with(MainActivity.this).addImage(image1);
                                onImageAdded(image1);
                            }
                            if (addImageResponse.status == 400) {
                                Toast.makeText(MainActivity.this, "too big image", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<AddImageResponse> call, Throwable t) {
                            Log.d(TAG, "onFailure: ");
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_layout, fragment, CURRENT_TAG.getFragmentName());
        fragmentTransaction.commit();
        //drawerLayout.closeDrawers();
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.hide(getSupportFragmentManager().findFragmentByTag(FragmentTag.TAG_IMAGE_LIST.getFragmentName()));
        fragmentTransaction.add(R.id.frame_layout, fragment, CURRENT_TAG.getFragmentName());
        fragmentTransaction.commit();
    }

    public void replaceFragment() {
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG.getFragmentName()) != null) {
            drawerLayout.closeDrawers();
            return;
        }
        replaceFragment(getFragment());


    }

    private Fragment getFragment() {
        switch (CURRENT_TAG) {

            case TAG_IMAGE_LIST:
                ImageFragment imageFragment = ImageFragment.newInstance();
                setHomeButton(false);
                return imageFragment;

            default:
                return ImageFragment.newInstance();
        }

    }

    public void setHomeButton(boolean isback) {
        if (isback) {
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBarDrawerToggle.setToolbarNavigationClickListener(null);

        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;

        }
        fab.show();
        uncheckFolders();
        cur_item = navigationView.getMenu().findItem(R.id.photo_folder);
        cur_item.setChecked(true);
        setHomeButton(false);

        if (CURRENT_TAG == FragmentTag.TAG_IMAGE_INFO) {
            CURRENT_TAG = FragmentTag.TAG_IMAGE_LIST;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.show(getSupportFragmentManager().findFragmentByTag(CURRENT_TAG.getFragmentName()));
            fragmentTransaction.remove(getSupportFragmentManager().findFragmentByTag(FragmentTag.TAG_IMAGE_INFO.getFragmentName()));
            fragmentTransaction.commit();

        } else if
                (CURRENT_TAG != FragmentTag.TAG_IMAGE_LIST) {
            CURRENT_TAG = FragmentTag.TAG_IMAGE_LIST;
            replaceFragment();
            android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.remove(getFragmentManager().findFragmentByTag(FragmentTag.TAG_MAP.getFragmentName()));
            transaction.commit();
        } else {
            SessionManager.logout(getApplicationContext());
            super.onBackPressed();
        }
    }

    public void uncheckFolders() {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setChecked(false);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photo_folder:
                CURRENT_TAG = FragmentTag.TAG_IMAGE_LIST;
                android.app.FragmentTransaction mfragmentTransaction =
                        getFragmentManager().beginTransaction();
                MapFragment mFragment = (MapFragment) getFragmentManager().findFragmentByTag(FragmentTag.TAG_MAP.getFragmentName());
                if (mFragment != null) mfragmentTransaction.remove(mFragment);
                mfragmentTransaction.commit();
                replaceFragment();
                break;
            case R.id.map_folder:
                CURRENT_TAG = FragmentTag.TAG_MAP;
                MapFragment mMapFragment = MapFragment.newInstance();
                android.app.FragmentTransaction fragmentTransaction =
                        getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, mMapFragment, FragmentTag.TAG_MAP.getFragmentName());
                fragmentTransaction.commit();
                mMapFragment.getMapAsync(this);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(getSupportFragmentManager().findFragmentByTag(FragmentTag.TAG_IMAGE_LIST.getFragmentName()));
                transaction.commit();
                break;

            default:

                return true;
        }
        cur_item = item;
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CURRENT_TAG = savedInstanceState.getParcelable(BUNDLE_CURRENT_TAG);
        cur_item = navigationView.getMenu().findItem(savedInstanceState.getInt(BUNDLE_ITEM_ID));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_CURRENT_TAG, CURRENT_TAG);
        outState.putInt(BUNDLE_ITEM_ID, cur_item.getItemId());
    }

    @Override
    public void onImageClicked(Image image) {
        fab.hide();
        CURRENT_TAG = FragmentTag.TAG_IMAGE_INFO;
        setHomeButton(true);
        ImageInfoFragment imageInfoFramgent = ImageInfoFragment.newInstance(image.getID());
        addFragment(imageInfoFramgent);

    }

    @Override
    public void onImageAdded(Image image) {
        onDataSetChanged();
        if (CURRENT_TAG == FragmentTag.TAG_MAP) putMarkersOnMap();
    }

    @Override
    public void onImageLongClicked(final Image image) {
        Log.d(TAG, "onImageLongClicked: ");
        ApiService.getInstance().deleteImage(image.getID()).enqueue(new Callback<DeleteImageResponse>() {
            @Override
            public void onResponse(Call<DeleteImageResponse> call, Response<DeleteImageResponse> response) {
                Log.d(TAG, "onResponse: ");
                if (response.body() != null) {
                    if (response.body().status == 200) {
                        RealmController.with(MainActivity.this).deleteImage(image);
                        onDataSetChanged();
                    } else
                        Toast.makeText(MainActivity.this, response.body().error, Toast.LENGTH_LONG).show();
                } else {
                    Converter<ResponseBody, DeleteImageResponse> errorConverter =
                            ApiService.getRetrofit().responseBodyConverter(DeleteImageResponse.class, new Annotation[0]);
                    try {
                        DeleteImageResponse error = errorConverter.convert(response.errorBody());
                        Log.d(TAG, "onResponse: ");
                        if (error.status == 500)
                            Toast.makeText(MainActivity.this, "Can't delete this photo", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<DeleteImageResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    @Override
    public void onCommentDelete(final Comment comment) {
        Log.d(TAG, "onCommentDelete: ");
        ApiService.getInstance().deleteComment(comment.getImageId(), comment.getID()).enqueue(new Callback<DeleteCommentResponse>() {
            @Override
            public void onResponse(Call<DeleteCommentResponse> call, Response<DeleteCommentResponse> response) {
                if (response.body() != null) {
                    if (response.body().status == 200) {
                        RealmController.with(MainActivity.this).deleteComment(comment);
                        onDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteCommentResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCommentAdded(final Comment comment) {
        Log.d(TAG, "onCommentAdded: ");
        ApiService.getInstance().addComment(comment.getImageId(), new AddCommentRequest(comment.getText())).enqueue(new Callback<AddCommentResponse>() {
            @Override
            public void onResponse(Call<AddCommentResponse> call, Response<AddCommentResponse> response) {
                if (response.body() != null) {
                    if (response.body().status == 200) {
                        Comment new_comment = new Comment(response.body().getData());
                        new_comment.setImageId(comment.getImageId());
                        RealmController.with(MainActivity.this).addComment(new_comment);
                        onDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddCommentResponse> call, Throwable t) {

            }
        });

    }


    @Override
    public void onDataSetChanged() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof AbsFragment) {
                ((AbsFragment) fragment).onUpdateView();
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                putMarkersOnMap();
            }
        });
    }

    public void putMarkersOnMap() {
        Marker marker;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        final List<Marker> markers = new ArrayList<>();
        List<Image> imageList = RealmController.with(this).getImages();
        LatLng latLng;
        for (Image image : imageList
                ) {
            latLng = new LatLng(image.getLat(), image.getLng());
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(AbsUtils.getFormatTimeFromSec(image.getDate(), "dd.MM.yy kk:mm")));
            builder.include(marker.getPosition());
            markers.add(marker);
            final LatLngBounds bounds = builder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
//                googleMap.moveCamera(cu);
            mMap.animateCamera(cu);

        }
    }
}
