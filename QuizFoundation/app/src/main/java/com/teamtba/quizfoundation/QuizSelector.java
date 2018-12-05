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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selector);

        try { QuizDatabase.load(this); }
        catch(Exception ex) { Toast.makeText(this, "ERORR LOADING DATABASE", Toast.LENGTH_LONG).show(); }

        listView = findViewById(R.id.quizSelectorList);

        // -- link subject adapter -- //

        QuizDatabase.Instance instance = QuizDatabase.getInstance();

        listViewAdapter = new com.teamtba.quizfoundation.ExpandableListAdapter(this);

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

    @Override
    public void onResume(){
        super.onResume();
        listViewAdapter.notifyDataSetChanged();

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

    @Override
    public void onPause(){
        super.onPause();
        listViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        listViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart(){
        super.onStart();
        listViewAdapter.notifyDataSetChanged();

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
                QuizDatabase.Subcategory selectedQuiz = (QuizDatabase.Subcategory)listViewAdapter.getChild(groupPosition, childPosition);

                //once item is selected, move to QuizAction
                Intent intent = new Intent(QuizSelector.this, QuizAction.class);
                //attach our selectedQuiz to our intent
                intent.putExtra("selectedQuiz", selectedQuiz.name);
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
