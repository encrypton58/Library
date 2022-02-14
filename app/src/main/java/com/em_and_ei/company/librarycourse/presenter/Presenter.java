package com.em_and_ei.company.librarycourse.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import com.em_and_ei.company.librarycourse.interfaces.ModelInterface;
import com.em_and_ei.company.librarycourse.interfaces.PresenterInterface;
import com.em_and_ei.company.librarycourse.models.Book;
import com.em_and_ei.company.librarycourse.models.Model;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class Presenter implements PresenterInterface{

    ModelInterface model = new Model();

    @Override
    public void getImage(ActivityResultLauncher<Intent> forResult) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        forResult.launch(intent);
    }

    @Override
    public void getPermissions(Activity activity) {
        int REQUEST_GALLERY = 999;
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY);
    }

    @Override
    public String convertAndSetImage(ImageView imageView, Intent intent) {
        try {
            final Uri imageUri = intent.getData();
            InputStream stream = imageView.getContext().getContentResolver().openInputStream(imageUri);
            Bitmap map = BitmapFactory.decodeStream(stream);
            imageView.setImageBitmap(map);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            map.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void uploadBook(Context context, Book book) {
        model.saveBook(context, book);
    }

    @Override
    public List<Book> getBooks(Context context) {
        return model.getBooks(context);
    }

    @Override
    public List<Book> filterBooks(int filter, String query, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            switch (filter){
                case 0 : //title
                    return getBooks(context).stream().filter(book -> book.getTitle().toLowerCase().contains(query)).collect(Collectors.toList());
                case 1 : //Author
                    return getBooks(context).stream().filter(book -> book.getAuthor().toLowerCase().contains(query)).collect(Collectors.toList());
                case 2: //editorial
                    return getBooks(context).stream().filter(book -> book.getEditorial().toLowerCase().contains(query)).collect(Collectors.toList());
                case 3: //year
                    return getBooks(context).stream().filter(book -> book.getYear().toLowerCase().contains(query)).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public void removeBook(Context context, int idBook) {
        model.removeBook(idBook, context);
    }

    @Override
    public void editBook(Context context, int position, Book book) {
        Toast.makeText(context, "" + book.getId(), Toast.LENGTH_SHORT).show();
        model.editBook(context, position, book);
    }
}
