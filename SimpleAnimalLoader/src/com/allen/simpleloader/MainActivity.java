package com.allen.simpleloader;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<List<String>>{
    private final static String TAG = "MainActivity";
    private final static int LOADER_ID = 1;
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mListView = (ListView)findViewById(R.id.listview);

        //Initialize loader
        getLoaderManager().initLoader(LOADER_ID,null,this).forceLoad();
    }

    @Override
    public void onLoaderReset(Loader<List<String>> listLoader) {
        //Set adapter to null in case of reset to avoid handling bad data
        mListView.setAdapter(null);
    }

    @Override
    public Loader<List<String>> onCreateLoader(int i, Bundle bundle) {
        //Returns new loader
        return new AnimalLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<String>> listLoader, List<String> strings) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strings);
        mListView.setAdapter(adapter);
    }

    //Custom AsyncTaskLoader
    private static class AnimalLoader extends AsyncTaskLoader<List<String>>{

        public AnimalLoader(Context c){
            super(c);
        }

        @Override
        public List<String> loadInBackground() {
            final String[] animals = new String[]{"dog","cat","bird","dinosaur",
                    "penguin","cheetah","lion","tiger","mouse","deer"};
            final ArrayList<String> animalList = new ArrayList<String>();

            //Loop through the list and add to arraylist
            for(int i =0;i<animals.length;i++){
                animalList.add(animals[i]);
                try{
                    Thread.sleep(200); //Simulate delay
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            Log.d(TAG,"Done loading animals");
            return animalList;
        }
    }
}
