package com.teamtba.quizfoundation;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.teamtba.quizfoundation.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizSelector extends AppCompatActivity {

    //initialize our expandable List view and it's adapter
    ExpandableListView listView;
    com.teamtba.quizfoundation.ExpandableListAdapter listViewAdapter;
    //List<QuizContainer>  subCategories;
    List<String> subCategories; //dummy
    List<String> subjects;
    //Map<QuizDatabase.Subject, List<QuizContainer>> subQuiz;
    Map<String, List<String>> subQuiz;

    QuizDatabase newDB;

    //initialize

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selector);

        newDB = new QuizDatabase();

        listView =  findViewById(R.id.quizSelectorList);
        //listView = new com.teamtba.quizfoundation.ExpandableListAdapter(this, subjects, subQuiz);
        // initializing the listeners
        initListeners();

        // initializing the objects
        initObjects();

        // preparing list data
        initListData();
    }

    private void initListeners() {

        // ExpandableListView on child click listener
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        subjects.get(groupPosition)
                                + " : "
                                + subQuiz.get(
                                subjects.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        // ExpandableListView Group expanded listener
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        subjects.get(groupPosition),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ExpandableListView Group collapsed listener
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        subjects.get(groupPosition),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }



    void initObjects(){

        // initializing the list of groups
        subjects = new ArrayList<>();

        // initializing the list of child
        subCategories = new ArrayList<String>();

        //creating map
        subQuiz = new HashMap<String, List<String>>();

        // initializing the adapter object
        listViewAdapter = new com.teamtba.quizfoundation.ExpandableListAdapter(this, subjects, subQuiz);

        // setting list adapter
        listView.setAdapter(listViewAdapter);

    }

    private void initListData() {


        // Adding group dummy data
        subjects.add("Math");
        subjects.add("History");
        subjects.add("Science");
        subjects.add("Other");



        // Math Children
        List<String> mathList = new ArrayList<>();
        mathList.add("Calculus");
        mathList.add("Add Category");

        //History Children
        List<String> historyList = new ArrayList<>();
        historyList.add("World Wars");
        historyList.add("Add Category");

        // list of Science
        List<String> scienceList = new ArrayList<String>();
        scienceList.add("Chemistry");
        scienceList.add("Add Category");

        // list of Other categories
        List<String> otherList = new ArrayList<>();
        otherList.add("Add Category");

        // Adding child data
        subQuiz.put(subjects.get(0), mathList);
        subQuiz.put(subjects.get(1), historyList);
        subQuiz.put(subjects.get(2), scienceList);
        subQuiz.put(subjects.get(3), otherList);

        // notify the adapter
        listViewAdapter.notifyDataSetChanged();
    }
}
