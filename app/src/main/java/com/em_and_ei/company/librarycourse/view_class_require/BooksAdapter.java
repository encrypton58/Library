package com.em_and_ei.company.librarycourse.view_class_require;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import com.em_and_ei.company.librarycourse.R;
import com.em_and_ei.company.librarycourse.enviroment.Variables;
import com.em_and_ei.company.librarycourse.models.Book;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookHolder> {

    List<Book> books;
    static OnClickItemListener listener;
    int itemSelected = -1;
    int clickCount = 0;

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public Book getBook(int position){
        return books.get(position);
    }

    public void setItemSelected(int itemSelected){
        this.itemSelected = itemSelected;
    }

    public void cancelSelection(){
        itemSelected = -1;
        notifyDataSetChanged();
    }

    public static void setListener(OnClickItemListener listener) {
        BooksAdapter.listener = listener;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        holder.render(books.get(position));
        if(itemSelected == position){
            holder.card.setCardBackgroundColor(Color.parseColor("#F8F8FA"));
            holder.title.setTextColor(Color.parseColor("#c5c5c7"));
            holder.selectedAlert.setVisibility(View.VISIBLE);
        }else {
            Palette.from(((BitmapDrawable) holder.image.getDrawable()).getBitmap()).generate(palette -> {
                assert palette != null;
                int colorDominant = palette.getDominantColor(Color.rgb(100, 100, 100));
                holder.card.setCardBackgroundColor(colorDominant);
                holder.title.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bone_color));
            });

            holder.selectedAlert.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    protected class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, author, selectedAlert;
        ImageView image;
        CardView card;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.book_item_title);
            author = itemView.findViewById(R.id.book_item_author);
            image = itemView.findViewById(R.id.book_item_image);
            card = itemView.findViewById(R.id.card_item_book);
            selectedAlert = itemView.findViewById(R.id.book_item_select_alert);
        }

        public void render(Book book) {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            if (book.getImage() != null) {
                getAndSetImage(book.getImage());
            }

        }

        private void getAndSetImage(String encode){
            byte[] b = Base64.decode(encode, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            image.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View view) {
            clickCount++;
            if(clickCount >= 2 && itemSelected == getAdapterPosition()){
                clickCount = 0;
                itemSelected = -1;
            }else{
                itemSelected = getAdapterPosition();
            }
            listener.onClickItem(getAdapterPosition(), (itemSelected == -1));
            notifyDataSetChanged();
        }
    }

    public interface OnClickItemListener {
        void onClickItem(int position, boolean isSelected);
    }

}
