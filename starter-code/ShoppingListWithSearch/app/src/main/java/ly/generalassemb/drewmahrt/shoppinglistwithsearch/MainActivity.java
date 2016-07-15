package ly.generalassemb.drewmahrt.shoppinglistwithsearch;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ly.generalassemb.drewmahrt.shoppinglistwithsearch.setup.DBAssetHelper;

public class MainActivity extends AppCompatActivity {
    //  private CursorAdapter mCursorAdapter;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_layout);
        //  mCursorAdapter = null;

        //Ignore the two lines below, they are for setup
        DBAssetHelper dbSetup = new DBAssetHelper(MainActivity.this);
        dbSetup.getReadableDatabase();

        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, "");
        onNewIntent(intent);

        mListView = (ListView) findViewById(R.id.items_list_view);

        Cursor getListCursor = ShoppingSQLiteOpenHelper.getInstance(this).getShoppingList();
//        Log.d("curosr", getListCursor.getCount()+"");
//
//        android.support.v4.widget.CursorAdapter cursorAdapter = new android.support.v4.widget.CursorAdapter(this, getListCursor,
//                android.support.v4.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
//            @Override
//            public View newView(Context context, Cursor cursor, ViewGroup parent) {
//                return LayoutInflater.from(MainActivity.this).inflate(R.layout.items_layout, parent, false);
//            }
//
//            @Override
//            public void bindView(View view, Context context, Cursor cursor) {
//                TextView item = (TextView) view.findViewById(R.id.item_text_view);
//                item.setText(cursor.getString(cursor.getColumnIndex(ShoppingSQLiteOpenHelper.COL_ITEM_DESCRIPTION)));
//
////                item.setText(cursor.getString(cursor.getColumnIndex(ShoppingSQLiteOpenHelper.COL_ITEM_NAME)));
////                item.setText("Hey");
//
//            }
//        };

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, getListCursor, new String[]{ShoppingSQLiteOpenHelper.COL_ITEM_NAME}, new int[]{android.R.id.text1}, 0);

        mListView.setAdapter(simpleCursorAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        ComponentName componentName = new ComponentName(this, this.getClass());
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        return true;

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

//        mListview = (ListView) findViewById(R.id.items_list_view);

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            Cursor searchCursor = ShoppingSQLiteOpenHelper.getInstance(this).searchItems(query);

            android.support.v4.widget.CursorAdapter cursorAdapter = new android.support.v4.widget.CursorAdapter(this, searchCursor,
                    android.support.v4.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    return LayoutInflater.from(MainActivity.this).inflate(R.layout.items_layout, parent, false);
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    TextView item = (TextView) view.findViewById(R.id.item_text_view);

//                    item.setText(cursor.getString(cursor.getColumnIndex(ShoppingSQLiteOpenHelper.COL_ITEM_NAME)));
                    item.setText("Hey");

                }
            };
            //           new String[]{ShoppingSQLiteOpenHelper.COL_ITEM_NAME},
            //         new int[]{R.id.item_text_view}, 0);

//            mListview.setAdapter(cursorAdapter);
        }


    }
}
