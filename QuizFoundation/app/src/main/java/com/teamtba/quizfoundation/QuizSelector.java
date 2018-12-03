package com.teamtba.quizfoundation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.teamtba.quizfoundation.ExpandableListAdapter;

import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizSelector extends AppCompatActivity implements View.OnClickListener {

    //initialize our expandable List view and it's adapter
   // ExpandableListView listView;
    //com.teamtba.quizfoundation.ExpandableListAdapter listViewAdapter;
    List<QuizDatabase.Subcategory> subCategories;
    List<QuizDatabase.Subject> subjects;
    Map<QuizDatabase.Subject, List<QuizDatabase.Subcategory>> subQuiz;


    QuizDatabase.Instance instance = QuizDatabase.getInstance();

    String selectedQuiz;

    //initialize
    ImageButton addNewQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selector);

        //QuizDatabase.load(this);

        addNewQuiz = findViewById(R.id.addNewQuizButton);
       // listView =  findViewById(R.id.quizSelectorList);
        // initializing the listeners for the expandable activity
        //startExListeners();

        // initializing the objects
        //initializeActivityObjects();

        // preparing list data
        //initializeAdapterData();

        //if addNewQuiz button is clicked, move to the QuestionEditorActivity Page
        addNewQuiz.setOnClickListener(this);


    }
/*
    private void startExListeners() {

        // ExpandableListView on child click listener
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //grab the selected quiz Category, store that information into
                //selectedQuiz variable
                selectedQuiz = subQuiz.get(subjects.get(groupPosition)).get(childPosition).name;
                Toast.makeText(
                        getApplicationContext(),
                        subjects.get(groupPosition).name
                                + " : "
                                + subQuiz.get(
                                subjects.get(groupPosition)).get(
                                childPosition).name, Toast.LENGTH_SHORT)
                        .show();
                //once item is selected, move to QuizAction
                Intent intent = new Intent(QuizSelector.this, QuizAction.class);
                //attach our selectedQuiz to our intent
                intent.putExtra("selectedQuiz", selectedQuiz);
                startActivity(intent);
                return false;
            }
        });
        // ExpandableListView Group expanded listener
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        subjects.get(groupPosition).name,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ExpandableListView Group collapsed listener
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        subjects.get(groupPosition).name,
                        Toast.LENGTH_SHORT).show();

            }
        });
    }



    void initializeActivityObjects(){

        // initializing the list of groups
        subjects = new ArrayList<>();

        // initializing the list of child
        subCategories = new ArrayList<>();

        //creating map
        subQuiz = new HashMap<QuizDatabase.Subject, List<QuizDatabase.Subcategory>>();

        // initializing the adapter object
        //listViewAdapter = new com.teamtba.quizfoundation.ExpandableListAdapter(this, subjects, subQuiz);

        // setting list adapter
        listView.setAdapter(listViewAdapter);

    }


    private void initializeAdapterData() {


        // data properly from quizDB disk, update once full functionality is established

        for (int i = 0; i < instance.subjects.size(); i++)
        {
            subjects.add(instance.subjects.get(i));
            subQuiz.put(instance.subjects.get(i), instance.subjects.get(i).subcategories);
        }

        /*Adding option for a +add Item to each SubQuiz Subject
        subQuiz.put(subjects.get(0), mathList);
        subQuiz.put(subjects.get(1), historyList);
        subQuiz.put(subjects.get(2), scienceList);
        subQuiz.put(subjects.get(3), otherList);

        //build map using the Subjects and SubCategories from DB
        //map info is stored into subjects List and subcategories list


         Adding group dummy data
        subjects.add("Math");
        subjects.add("History");
        subjects.add("Science");
        subjects.add("Other");



        // Math Children
        List<String> mathList = new ArrayList<>();
        mathList.add("Calculus");
        mathList.add("+ Add Category");

        //History Children
        List<String> historyList = new ArrayList<>();
        historyList.add("World Wars");
        historyList.add("+ Add Category");

        // list of Science
        List<String> scienceList = new ArrayList<String>();
        scienceList.add("Chemistry");
        scienceList.add("+ Add Category");

        // list of Other categories
        List<String> otherList = new ArrayList<>();
        otherList.add("+ Add Category");

        // Adding child data
        subQuiz.put(subjects.get(0), mathList);
        subQuiz.put(subjects.get(1), historyList);
        subQuiz.put(subjects.get(2), scienceList);
        subQuiz.put(subjects.get(3), otherList);

        // notify the adapter
        listViewAdapter.notifyDataSetChanged();
    }
*/
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(QuizSelector.this, QuestionEditorActivity.class);
        startActivity(intent);
    }
}
