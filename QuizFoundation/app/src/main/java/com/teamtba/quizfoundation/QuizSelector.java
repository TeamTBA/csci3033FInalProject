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


//-----------------------------------------------------------------------------
//    ExpandableListView code developed / derived from tutorial found at;
//   http://www.androidtutorialshub.com/android-expandable-list-view-tutorial/
//-----------------------------------------------------------------------------

public class QuizSelector extends AppCompatActivity {


    //initialize our expandable List view and it's adapter
    ExpandableListView listView;
    com.teamtba.quizfoundation.ExpandableListAdapter listViewAdapter;
    //initialize

    Map<QuizDatabase.Subject, List<QuizDatabase.Subcategory>> subQuiz;
    //Grab the most up to date instance of our QuizDatabase
    //QuizDatabase.Instance instance = QuizDatabase.getInstance();
    //selectedQuiz is to be passed to QuizAction activity
    String selectedQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selector);

        try { QuizDatabase.load(this); }
        catch(Exception ex) { Toast.makeText(this, "ERORR LOADING DATABASE", Toast.LENGTH_LONG).show(); }

        listView = findViewById(R.id.quizSelectorList);

        // -- link subject adapter -- //

        QuizDatabase.Instance instance = QuizDatabase.getInstance();

        //creating map
        subQuiz = new HashMap<>();



        //subQuiz.put(math, math.subcategories);
        //subQuiz.put(science, science.subcategories);

        listViewAdapter = new com.teamtba.quizfoundation.ExpandableListAdapter(this, instance.subjects, subQuiz);

        // setting list adapter
        listView.setAdapter(listViewAdapter);

        //set listeners for our Expandable List Object
        startExListeners();

        //if addNewQuiz button is clicked, move to the QuestionEditorActivity Page
        findViewById(R.id.addNewQuizButton).setOnClickListener(e ->
        {
            Intent intent = new Intent(QuizSelector.this, QuestionEditorActivity.class);

            QuestionEditorActivity.IntentArgs args = QuestionEditorActivity.IntentArgs.NewQuestion();
            intent.putExtra(QuestionEditorActivity.INTENT_ARGS_NAME, args);

            startActivity(intent);
        });
    }

    private void startExListeners() {

        // ExpandableListView on child click listener
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //grab the selected quiz Category, store that information into
                //selectedQuiz variable
                selectedQuiz = subQuiz.get(QuizDatabase.getInstance().subjects.get(groupPosition)).get(childPosition).name;
                //once item is selected, move to QuizAction
                Intent intent = new Intent(QuizSelector.this, QuizAction.class);
                //attach our selectedQuiz to our intent
                intent.putExtra("selectedQuiz",selectedQuiz);
                startActivity(intent);
                return false;
            }
        });
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                //do nothing
            }
        });
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
               //do nothing
            }
        });
    }

}
