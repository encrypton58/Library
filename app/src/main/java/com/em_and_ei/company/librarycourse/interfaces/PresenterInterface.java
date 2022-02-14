package com.em_and_ei.company.librarycourse.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;

import com.em_and_ei.company.librarycourse.models.Book;

import java.util.List;

public interface PresenterInterface {

    void getImage(ActivityResultLauncher<Intent> forResult);

    void getPermissions(Activity activity);

    String convertAndSetImage(ImageView imageView, Intent intent);

    void uploadBook(Context context, Book book);

    List<Book> getBooks(Context context);

    List<Book> filterBooks(int filter, String query, Context context);

    void removeBook(Context context, int position);

    void editBook(Context context, int position, Book book);

}
