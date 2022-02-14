package com.em_and_ei.company.librarycourse.enviroment;

import android.content.Context;
import com.em_and_ei.company.librarycourse.models.Book;
import com.em_and_ei.company.librarycourse.models.SharedPreferencesSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonProvider {

    final SharedPreferencesSingleton preferences;

    public JsonProvider(final Context context){
        preferences = SharedPreferencesSingleton.getInstance(context);
    }

    private JSONObject fromObjectToJsonObject(Book book) throws JSONException {
        final JSONObject bookJson = new JSONObject();
        bookJson.put(Variables.BOOKS_ID_KEY, book.getId());
        bookJson.put(Variables.BOOKS_TITLE_KEY, book.getTitle());
        bookJson.put(Variables.BOOKS_AUTHOR_KEY, book.getAuthor());
        bookJson.put(Variables.BOOKS_YEAR_KEY, book.getYear());
        bookJson.put(Variables.BOOKS_EDITORIAL_KEY, book.getEditorial());
        bookJson.put(Variables.BOOKS_IMAGE_KEY, book.getImage());
        bookJson.put(Variables.BOOKS_PRICE_KEY, book.getPrice());
        bookJson.put(Variables.BOOKS_CATEGORY_KEY, book.getCategory());
        return bookJson;
    }

    public JSONArray getJsonArray() throws JSONException {
        final String json = preferences.getString(Variables.BOOKS_PREFERENCES_KEY);
        if(!json.isEmpty()){
            final JSONArray jsonArray = new JSONArray(json);
            return jsonArray;
        }
        final JSONArray jsonArray = new JSONArray();
        preferences.saveString(Variables.BOOKS_PREFERENCES_KEY, jsonArray.toString());
        return jsonArray;
    }

    private void saveJsonArray(final JSONArray array){
        preferences.saveString(Variables.BOOKS_PREFERENCES_KEY, array.toString());
    }

    private int getLastId() throws JSONException {
        JSONArray array = getJsonArray();
        if(array.length() != 0){
            return array.getJSONObject(array.length() - 1).getInt(Variables.BOOKS_ID_KEY) + 1;
        }
        return 1;
    }

    public void addJsonObject(final Book book) throws JSONException {
        final JSONObject bookJson = new JSONObject();
        bookJson.put(Variables.BOOKS_ID_KEY, getLastId());
        bookJson.put(Variables.BOOKS_TITLE_KEY, book.getTitle());
        bookJson.put(Variables.BOOKS_AUTHOR_KEY, book.getAuthor());
        bookJson.put(Variables.BOOKS_YEAR_KEY, book.getYear());
        bookJson.put(Variables.BOOKS_EDITORIAL_KEY, book.getEditorial());
        bookJson.put(Variables.BOOKS_IMAGE_KEY, book.getImage());
        bookJson.put(Variables.BOOKS_PRICE_KEY, book.getPrice());
        bookJson.put(Variables.BOOKS_CATEGORY_KEY, book.getCategory());
        final JSONArray arrayJs = getJsonArray();
        arrayJs.put(bookJson);
       saveJsonArray(arrayJs);
    }

    public void removeBook(int idBook) throws JSONException {
        final JSONArray array = getJsonArray();
        for(int i = 0; i < array.length(); i++){
            if(array.getJSONObject(i).getInt(Variables.BOOKS_ID_KEY) == idBook){
                array.remove(i);
                saveJsonArray(array);
                break;
            }
        }

    }

    public void editJsonObject(Book book) throws JSONException {
        final JSONArray array = getJsonArray();
        JSONObject jsonBook = fromObjectToJsonObject(book);
        int index = 0;
        for(int i = 0; i < array.length(); i++){
            if(array.getJSONObject(i).getInt(Variables.BOOKS_ID_KEY) == jsonBook.getInt(Variables.BOOKS_ID_KEY)){
                index = i;
                break;
            }
        }
        array.put(index, jsonBook);
        saveJsonArray(array);
    }

}
