package com.laudhoot.fragment;

/**
 * Created by root on 2/3/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.laudhoot.Laudhoot;
import com.laudhoot.R;
import com.laudhoot.activity.MainActivity;
import com.laudhoot.model.Category;
import com.laudhoot.model.Item;
import com.laudhoot.persistence.CategoryRepository;
import com.laudhoot.persistence.ItemRepository;
import com.laudhoot.util.WebTask;
import com.laudhoot.web.RestClient;
import com.laudhoot.web.TestAPI;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private Button button1;
    private Button button2;

    @InjectView(R.id.section_label)
    public TextView textView;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(Integer sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);
        button1 = (Button) rootView.findViewById(R.id.button_1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SampleWebTask(getActivity()).execute();
            }
        });

        button2 = (Button) rootView.findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryRepository categoryRepository = new CategoryRepository();
                ItemRepository itemRepository = new ItemRepository();

                Category category1 = categoryRepository.createCategory("Category1");
                Log.d(Laudhoot.LOG_TAG, category1.toString());
                Category category2 = categoryRepository.createCategory("Category2");
                Log.d(Laudhoot.LOG_TAG, category2.toString());

                Item item1 = itemRepository.createItem("Item1", category1);
                Item item2 = itemRepository.createItem("Item2", category1);
                Item item3 = itemRepository.createItem("Item3", category1);

                Item item4 = itemRepository.createItem("Item4", category2);

                List<Item> itemsInCategory1 = category1.items();
                List<Item> itemsInCategory2 = category2.items();

                String text1 = category1.toString();
                for(Item item : itemsInCategory1){
                    text1 = text1 + " : " + item.toString();
                    Log.d(Laudhoot.LOG_TAG, item.toString());
                }

                String text2 = category2.toString();
                for(Item item : itemsInCategory2){
                    text2 = text2 + " : " + item.toString();
                    Log.d(Laudhoot.LOG_TAG, item.toString());
                }

                textView.setText(text1 + " | --- | " + text2);
            }
        });
        return rootView;
    }

    class SampleWebTask extends WebTask {

        public SampleWebTask(Activity activity){
            super(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            super.doInBackground(params);
            TestAPI webTestApi = new RestClient().getTestWebService();
            return webTestApi.testController1();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Toast.makeText(
                    activity.getApplicationContext(), "Executing /test/1... Result:" + response,
                    Toast.LENGTH_LONG
            ).show();
        }
    }

}