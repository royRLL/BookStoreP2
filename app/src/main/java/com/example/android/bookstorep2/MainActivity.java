package com.example.android.bookstorep2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.bookstorep2.data.BookContract.BookEntry;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 0;

    private BookCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.books);


        //        FAB to open the editor activity
        FloatingActionButton btn = findViewById(R.id.fab);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });



        ListView listView = findViewById(R.id.list);

        //      Set the emptyView
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);


        mCursorAdapter = new BookCursorAdapter(this, null);
        listView.setAdapter(mCursorAdapter);

        // List item onclick listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                intent.setData(currentBookUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    private void insertBook() {

        ContentValues values = new ContentValues();
        values.put(BookEntry.BOOK_NAME, "Slaughterhouse-5");
        values.put(BookEntry.BOOK_PRICE, 55);
        values.put(BookEntry.BOOK_QUANTITY, 7);
        values.put(BookEntry.BOOK_SUPPLIER_NAME, "Random");
        values.put(BookEntry.BOOK_SUPPLIER_PHONE, "0012345678");

        getContentResolver().insert(BookEntry.CONTENT_URI, values);
    }




    private void deleteAllBooks() {
        getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
        Toast.makeText(this, "All entries were deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_preview_book:
                insertBook();
                return true;
            case R.id.action_delete_all:
                deleteAllBooks();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.BOOK_NAME,
                BookEntry.BOOK_PRICE,
                BookEntry.BOOK_QUANTITY
        };

        return new CursorLoader(this,
                BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor dataCursor) {
        mCursorAdapter.swapCursor(dataCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
