package com.example.sashok.testapplication.model.realm;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;

import com.example.sashok.testapplication.model.Comment;
import com.example.sashok.testapplication.model.Image;
import com.example.sashok.testapplication.utils.SessionManager;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.example.sashok.testapplication.network.Constance.IMAGES_PER_PAGE;

/**
 * Created by sashok on 27.10.17.
 */

public class RealmController {
    private static RealmController instance;
    private final Realm realm;
    private Context mContext;

    private RealmController(Application application) {
        realm = Realm.getDefaultInstance();
        mContext = application;
    }

    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController with(Fragment fragment) {
        instance = new RealmController(fragment.getActivity().getApplication());

        return instance;

    }

    public static RealmController getInstance() {

        return instance;
    }

    private int getCurrentUserId() {
        return SessionManager.getUserId(mContext);
    }

    public Realm getRealm() {

        return realm;
    }

    public void addImage(final Image image) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                image.setUserId(getCurrentUserId());
                Image image1 = realm.copyToRealmOrUpdate(image);
            }
        });
    }

    public void deleteAll() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Image.class).findAll().deleteAllFromRealm();
                realm.where(Comment.class).findAll().deleteAllFromRealm();

            }
        });
    }

    public List<Image> getImages(final int page) {
        final List<Image> imageList = new RealmList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Image> images = realm.where(Image.class).equalTo("UserId", getCurrentUserId()).findAll().sort("ID", Sort.DESCENDING);
                try {
                    int max = images.size();
                    int end = page * IMAGES_PER_PAGE + IMAGES_PER_PAGE;
                    if (end > max) end = max;
                    imageList.addAll(images.subList(page * IMAGES_PER_PAGE, end));
                } catch (IndexOutOfBoundsException e) {

                }

            }
        });
        return imageList;
    }

    public List<Image> getImages() {
        final List<Image> imageList = new RealmList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Image> images = realm.where(Image.class).equalTo("UserId", getCurrentUserId()).findAll();
                imageList.addAll(images);
            }
        });
        return imageList;
    }

    public void deleteImage(final Image image) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Comment.class).equalTo("imageId", image.getID()).findAll().deleteAllFromRealm();
                realm.where(Image.class).equalTo("ID", image.getID()).findFirst().deleteFromRealm();
            }
        });
    }

    public Image getImageById(final int id) {
        final Image[] images = new Image[1];
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Image result = realm.where(Image.class).equalTo("ID", id).findFirst();
                images[0] = result;
            }
        });
        return images[0];
    }

    public void addComment(final Comment comment) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Comment realmComment = realm.copyToRealmOrUpdate(comment);
                realm.where(Image.class).equalTo("ID", realmComment.getImageId()).findFirst().setComment(realmComment);

            }
        });
    }

    public List<Comment> getComments(final int imageId) {
        final List<Comment> commentList = new RealmList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Comment> comments = realm.where(Comment.class).equalTo("imageId", imageId).findAll();
                commentList.addAll(comments);

            }
        });
        return commentList;
    }

    public List<Comment> getComments(final int imageId, int page) {
        final List<Comment> commentList = new RealmList<>();
        RealmResults<Comment> comments = realm.where(Comment.class).equalTo("imageId", imageId).findAll().sort("ID", Sort.ASCENDING);
        try {
            int max = comments.size();
            int end = page * IMAGES_PER_PAGE + IMAGES_PER_PAGE;
            if (end > max) end = max;
            commentList.addAll(comments.subList(page * IMAGES_PER_PAGE, end));
        } catch (IndexOutOfBoundsException e) {

        }
        return commentList;
    }

    public void deleteComment(final Comment comment) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Image image = realm.where(Image.class).equalTo("ID", comment.getImageId()).findFirst();
                if (image != null) image.removeComment(comment);
                realm.where(Comment.class).equalTo("ID", comment.getID()).findFirst().deleteFromRealm();
            }
        });
    }

    public void deleteComments(final int imageId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Comment.class).equalTo("imageId", imageId).findAll().deleteAllFromRealm();
            }
        });
    }


}
