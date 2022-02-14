package com.em_and_ei.company.librarycourse.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.em_and_ei.company.librarycourse.R;
import com.em_and_ei.company.librarycourse.databinding.ActivityMainBinding;
import com.em_and_ei.company.librarycourse.enviroment.BooksProvider;
import com.em_and_ei.company.librarycourse.enviroment.Variables;
import com.em_and_ei.company.librarycourse.interfaces.PresenterInterface;
import com.em_and_ei.company.librarycourse.models.Book;
import com.em_and_ei.company.librarycourse.presenter.Presenter;
import com.em_and_ei.company.librarycourse.view_class_require.BooksAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    PresenterInterface presenter = new Presenter();
    BooksAdapter adapter = new BooksAdapter();
    BottomSheetDialog optionsBookDialog;

    MenuItem searchView;
    int filter;
    Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter.setBooks(presenter.getBooks(getApplicationContext()));
        initView();
    }

    private void initView() {
        LinearLayoutManager linear = new
                LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        binding.mainListBooks.setLayoutManager(linear);

        binding.mainListBooks.setAdapter(adapter);

        binding.uploadBook.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), UploadBook.class);
            startActivity(intent);
        });

        BooksAdapter.setListener((position, isSelected) -> {
            selectedBook = BooksProvider.getBook(adapter.getBook(position).getId(), getApplicationContext());
            openBottomSheet();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.main_menu, menu);
        searchView = menu.findItem(R.id.action_search);
        SearchManager manager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView search = null;
        if (searchView != null) {
            search = (SearchView) searchView.getActionView();
        }
        if (search != null) {
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @SuppressLint("NewApi")
                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.setBooks(presenter.filterBooks(filter, newText.toLowerCase(), getApplicationContext()));
                    return false;
                }
            });
            search.setSearchableInfo(manager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_title:
                filter = 0;
                searchView.expandActionView();
                break;
            case R.id.filter_author:
                filter = 1;
                searchView.expandActionView();
                break;
            case R.id.filter_editorial:
                filter = 2;
                searchView.expandActionView();
                break;
            case R.id.filter_year:
                filter = 3;
                searchView.expandActionView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogDeleteBook(Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Are you sure you want to delete this book?")
                .setMessage("It will be eliminated " + book.getTitle())
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    presenter.removeBook(getApplicationContext(), selectedBook.getId());
                    adapter.setBooks(presenter.getBooks(getApplicationContext()));
                    adapter.setItemSelected(-1);
                    optionsBookDialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private void openBottomSheet() {
        optionsBookDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
        optionsBookDialog.setContentView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet, (RelativeLayout) findViewById(R.id.bottom_sheet)));
        optionsBookDialog.setOnCancelListener(dialogInterface -> adapter.cancelSelection());
        TextView title = optionsBookDialog.findViewById(R.id.bottom_sheet_title);
        ImageView image = optionsBookDialog.findViewById(R.id.bottom_sheet_image);
        Button edit = optionsBookDialog.findViewById(R.id.bottom_sheet_edit_btn);
        Button delete = optionsBookDialog.findViewById(R.id.bottom_sheet_delete_btn);
        assert image != null && title != null && edit != null && delete != null;
        title.setText(selectedBook.getTitle());
        renderImage(image);
        edit.setOnClickListener(bottomSheetEdit);
        delete.setOnClickListener(bottomSheetDelete);
        optionsBookDialog.show();
    }

    private void renderImage(ImageView container) {
        byte[] b = Base64.decode(selectedBook.getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        container.setImageBitmap(bitmap);
    }

    View.OnClickListener bottomSheetDelete = view -> dialogDeleteBook(selectedBook);

    View.OnClickListener bottomSheetEdit = view -> {
         Intent intent = new Intent(getApplicationContext(), UploadBook.class);
           intent.putExtra(Variables.BOOK_POSITION_EXTRA_KEY, selectedBook.getId());
           startActivity(intent);
    };

}