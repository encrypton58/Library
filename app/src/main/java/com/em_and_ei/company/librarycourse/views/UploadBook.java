package com.em_and_ei.company.librarycourse.views;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.em_and_ei.company.librarycourse.R;
import com.em_and_ei.company.librarycourse.databinding.ActivityUploadBookBinding;
import com.em_and_ei.company.librarycourse.enviroment.BooksProvider;
import com.em_and_ei.company.librarycourse.enviroment.Variables;
import com.em_and_ei.company.librarycourse.interfaces.PresenterInterface;
import com.em_and_ei.company.librarycourse.models.Book;
import com.em_and_ei.company.librarycourse.presenter.Presenter;

import java.util.Objects;

public class UploadBook extends AppCompatActivity {

    boolean isToEdit = false;
    int bookIndexEditable = -1;
    ActivityUploadBookBinding binding;
    private Book book = new Book();
   final PresenterInterface presenter = new Presenter();

    private final ActivityResultLauncher<Intent> forResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    book.setImage(presenter.convertAndSetImage(binding.uploadImage, result.getData()));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        isToEdit = getIntent().getIntExtra(Variables.BOOK_POSITION_EXTRA_KEY, -1) != bookIndexEditable;
        initView();
    }

    private void initView(){
        presenter.getPermissions(UploadBook.this);
        binding.uploadImageBtn.setOnClickListener(view -> presenter.getImage(forResult));

        if(isToEdit){
            bookIndexEditable = getIntent().getExtras().getInt(Variables.BOOK_POSITION_EXTRA_KEY);
            Toast.makeText(this, bookIndexEditable + "", Toast.LENGTH_SHORT).show();
            book = BooksProvider.getBook(bookIndexEditable, getApplicationContext());
            Objects.requireNonNull(binding.uploadTitle.getEditText()).setText(book.getTitle());
            Objects.requireNonNull(binding.uploadAuthor.getEditText()).setText(book.getAuthor());
            Objects.requireNonNull(binding.uploadEditorial.getEditText()).setText(book.getEditorial());
            Objects.requireNonNull(binding.uploadYear.getEditText()).setText(book.getYear());
            binding.uploadImage.setImageBitmap(BooksProvider.getImageConvertToBook(book.getImage()));
            binding.uploadTitleTextview.setText("Edit Book");
            binding.uploadBtnUp.setText("Edit");
        }


        binding.uploadBtnUp.setOnClickListener(view -> {
            book.setTitle(Objects.requireNonNull(binding.uploadTitle.getEditText()).getText().toString());
            book.setAuthor(Objects.requireNonNull(binding.uploadAuthor.getEditText()).getText().toString());
            book.setEditorial(Objects.requireNonNull(binding.uploadEditorial.getEditText()).getText().toString());
            book.setYear(Objects.requireNonNull(binding.uploadYear.getEditText()).getText().toString());
            if(!checkInputs()){
                if(isToEdit){
                    presenter.editBook(getApplicationContext(), bookIndexEditable, book);
                }else{
                    presenter.uploadBook(getApplicationContext(), book);
                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private boolean checkInputs(){
        int errors = 0;
        if(Objects.requireNonNull(binding.uploadTitle.getEditText()).getText().toString().isEmpty()){
            binding.uploadTitle.setError("This Field Is Empty");
            errors++;
        }else{
            binding.uploadTitle.setError(null);
        }
        if(Objects.requireNonNull(binding.uploadAuthor.getEditText()).getText().toString().isEmpty()){
            binding.uploadAuthor.setError("This Field Is Empty");
            errors++;
        }else{
            binding.uploadAuthor.setError(null);
        }
        if(Objects.requireNonNull(binding.uploadEditorial.getEditText()).getText().toString().isEmpty() ){
            binding.uploadEditorial.setError("This Field Is Empty");
            errors++;
        }else{
            binding.uploadEditorial.setError(null);
        }

        if (Objects.requireNonNull(binding.uploadYear.getEditText()).getText().toString().isEmpty() && binding.uploadYear.getEditText().getText().length() < 4
        || binding.uploadYear.getEditText().getText().toString().length() > 4){
            binding.uploadYear.setError("Has a error");
            errors++;
        }else{
            binding.uploadYear.setError(null);
        }
         if(book.getImage() == null){
             Toast.makeText(this, "No has selected one image, Select one", Toast.LENGTH_LONG).show();
             binding.uploadImageBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_color));
             errors++;
         }else{
             binding.uploadImageBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.purple_500));
         }
        return errors >= 1;
    }

}