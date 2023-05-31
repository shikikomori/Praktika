package com.example.bookdepository;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.UUID;

public class BookActivity extends SingleFragmentActivity {
    public static final String EXTRA_BOOK_ID="ru.rsue.android.bookdepository.book_id";
    public static Intent newIntent(Context packageContext, UUID bookID){
        Intent intent=new Intent(packageContext,BookActivity.class);
        intent.putExtra(EXTRA_BOOK_ID,bookID);
        return intent;
    }
    @Override
    protected Fragment createFragment(){
        UUID bookId=(UUID) getIntent().getSerializableExtra(EXTRA_BOOK_ID);
        return BookFragment.newInstance(bookId);
    }
}