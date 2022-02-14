package com.em_and_ei.company.librarycourse.interfaces;

import android.content.Context;

import com.em_and_ei.company.librarycourse.models.Book;

import java.util.List;

public interface ModelInterface {

    void saveBook(Context context, Book book);

    List<Book> getBooks(Context context);

    void removeBook(int idBook, Context context);

    void editBook(Context context, int index, Book book);

}
