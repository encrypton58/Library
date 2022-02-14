package com.em_and_ei.company.librarycourse.enviroment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import com.em_and_ei.company.librarycourse.models.Book;
import com.em_and_ei.company.librarycourse.models.SharedPreferencesSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BooksProvider {

    public static List<Book> getbooks(Context context){
        List<Book> books = new ArrayList<>();
        try {
            JSONArray array = new JsonProvider(context).getJsonArray();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Book book = new Book();
                book.setId(object.getInt(Variables.BOOKS_ID_KEY));
                book.setTitle(object.getString(Variables.BOOKS_TITLE_KEY));
                book.setAuthor(object.getString(Variables.BOOKS_AUTHOR_KEY));
                book.setEditorial(object.getString(Variables.BOOKS_EDITORIAL_KEY));
                book.setYear(object.getString(Variables.BOOKS_YEAR_KEY));
                book.setImage(object.getString(Variables.BOOKS_IMAGE_KEY));
                book.setCategory(object.getString(Variables.BOOKS_CATEGORY_KEY));
                book.setPrice(object.getInt(Variables.BOOKS_PRICE_KEY));
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return books;
        }

        return books;
    }

    public static Book getBook(int idBook, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return  getbooks(context).stream().filter(book -> book.getId() == idBook).collect(Collectors.toList()).get(0);
        }
        return null;
    }

    public static Bitmap getImageConvertToBook(String decode){
        byte[] b = Base64.decode(decode, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

}
