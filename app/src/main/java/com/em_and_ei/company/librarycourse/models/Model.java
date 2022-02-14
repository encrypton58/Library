package com.em_and_ei.company.librarycourse.models;

import android.content.Context;
import android.widget.Toast;
import com.em_and_ei.company.librarycourse.enviroment.BooksProvider;
import com.em_and_ei.company.librarycourse.enviroment.JsonProvider;
import com.em_and_ei.company.librarycourse.interfaces.ModelInterface;
import org.json.JSONException;
import java.util.List;

public class Model implements ModelInterface {

    JsonProvider provider;

    @Override
    public void saveBook(Context context, Book book) {
        provider = new JsonProvider(context);
        try {
            provider.addJsonObject(book);
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public List<Book> getBooks(Context context) {
        return BooksProvider.getbooks(context);
    }

    @Override
    public void removeBook(int idBook, Context context) {
        provider = new JsonProvider(context);
        try {
            provider.removeBook(idBook);
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void editBook(Context context, int index, Book book) {
        provider = new JsonProvider(context);
        try{
            provider.editJsonObject(book);
        }catch(JSONException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
