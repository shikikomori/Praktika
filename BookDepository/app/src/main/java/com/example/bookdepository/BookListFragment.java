package com.example.bookdepository;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.TextView;
//
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;

public class BookListFragment extends Fragment {
    private RecyclerView mBookRecyclerView;
    private static final String SAVED_SUBTITLE_VISIBLE="subtitle";
    private BookAdapter mAdapter;
    private boolean mSubtitleVisible;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancerState){
        View view= inflater.inflate(R.layout.fragment_book_list,container,false);
        mBookRecyclerView=(RecyclerView) view.findViewById(R.id.book_recycler_view);
        mBookRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstancerState !=null){
            mSubtitleVisible=savedInstancerState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_book_list,menu);
        MenuItem subtitleItem=menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_book:
                Book book = new Book();
                BookLab.get(getActivity()).addBook(book);
                Intent intent = BookPagerActivity.newIntent(getActivity(), book.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible=!mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateSubtitle(){
        BookLab bookLab=BookLab.get(getActivity());
        int bookCount=bookLab.getBooks().size();
        String subtitle=getString(R.string.subtitle_format,bookCount);
        if (!mSubtitleVisible){
            subtitle=null;
        }
        AppCompatActivity activity=(AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI(){
        BookLab bookLab= BookLab.get(getActivity());
        List<Book> books=bookLab.getBooks();
        if (mAdapter == null){
            mAdapter= new BookAdapter(books);
            mBookRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setBooks(books);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }
    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private Book mBook;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mReadedCheckBox;
        public BookHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView=(TextView) itemView.findViewById(R.id.list_item_book_title_text_view);
            mDateTextView=(TextView) itemView.findViewById(R.id.list_item_book_date_text_view);
            mReadedCheckBox=(CheckBox)itemView.findViewById(R.id.list_item_book_readed_check_box);
        }
        public void bindBook(Book book){
            mBook=book;
            mTitleTextView.setText(mBook.getTitle().toString());
            mDateTextView.setText(mBook.getDate().toString());
            mReadedCheckBox.setChecked(mBook.isReaded());
        }
        @Override
        public void onClick(View v){
            Intent intent= BookPagerActivity.newIntent(getActivity(),mBook.getId());
            startActivity(intent);
        }
    }
    private class BookAdapter extends RecyclerView.Adapter<BookHolder>{
        private List<Book>mBooks;
        public BookAdapter(List<Book> books){
            mBooks=books;
        }


        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater= LayoutInflater.from(getActivity());
            View view= layoutInflater.inflate(R.layout.list_item_book,parent,false);
            return new BookHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            Book book= mBooks.get(position);
            holder.bindBook(book);
        }

        @Override
        public int getItemCount() {
            return mBooks.size();
        }
        public void setBooks(List<Book> books) {
            mBooks = books;
        }

    }
}