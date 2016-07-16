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
  //  ListView mItemListView;
    TextView mTextView;
    CursorAdapter mCursorAdapter;
    Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  mCursorAdapter = null;

        //Ignore the two lines below, they are for setup
        DBAssetHelper dbSetup = new DBAssetHelper(MainActivity.this);
        dbSetup.getReadableDatabase();

//        Intent intent = new Intent(Intent.ACTION_SEARCH);
//        Intent intent = getIntent();
//        intent.putExtra(SearchManager.QUERY, "");

//        onNewIntent(getIntent());

        mListView = (ListView) findViewById(R.id.items_list_view);
//        mTextView = (TextView) findViewById(R.id.item_text_view);

        mCursor = ShoppingSQLiteOpenHelper.getInstance(this).getShoppingList();
        Log.d("cursor", mCursor.getCount()+"");
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

        mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.
                simple_list_item_1, mCursor, new String[]{ShoppingSQLiteOpenHelper.
                COL_ITEM_NAME}, new int[]{android.R.id.text1}, 0);

        mListView.setAdapter(mCursorAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        ComponentName componentName = new ComponentName(this, this.getClass());
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;

    }

    protected void onNewIntent(Intent intent) {
        Log.d("MainActivity", "onNewIntent: yeah");
//        super.onNewIntent(intent);
        setIntent(intent);
    //    setContentView(R.layout.activity_main);

   //     mItemListView = (ListView) findViewById(R.id.item_list_view);
        mTextView = (TextView) findViewById(R.id.item_text_view);

//        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            if(query != null){
                    Log.d("cursor2", query);
            }
            mCursor = ShoppingSQLiteOpenHelper.getInstance(this).searchItems(query);
            Log.d("cursor2", mCursor.getCount()+"");



            mCursorAdapter = new CursorAdapter(this, mCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    return LayoutInflater.from(MainActivity.this).inflate(R.layout.items_layout, parent, false);
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    //         ListView items = (ListView) view.findViewById(R.id.item_list_view);
                    TextView item = (TextView) view.findViewById(R.id.item_text_view);

                    item.setText(cursor.getString(cursor.getColumnIndex(ShoppingSQLiteOpenHelper.COL_ITEM_NAME)));
//                    item.setText("Hey");

                }
            };
            //           new String[]{ShoppingSQLiteOpenHelper.COL_ITEM_NAME},
            //         new int[]{R.id.item_text_view}, 0);

            mCursorAdapter.changeCursor(mCursor);

            mListView.setAdapter(mCursorAdapter);
        }
    }
}
