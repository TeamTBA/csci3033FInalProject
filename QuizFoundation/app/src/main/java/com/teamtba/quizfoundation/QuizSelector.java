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

import static com.teamtba.quizfoundation.QuizDatabase.load;

public class QuizSelector extends AppCompatActivity {


    //initialize our expandable List view and it's adapter
    ExpandableListView listView;
    com.teamtba.quizfoundation.ExpandableListAdapter listViewAdapter;
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

        // -- example pre-populated data -- //
        /**/

        QuizDatabase.Instance instance = QuizDatabase.getInstance();

        QuizDatabase.Subcategory calculus = new QuizDatabase.Subcategory();
        calculus.name = "Calculus";
        QuizDatabase.Subcategory algebra = new QuizDatabase.Subcategory();
        algebra.name = "Algebra";
        QuizDatabase.Subcategory geometry = new QuizDatabase.Subcategory();
        geometry.name = "Geometry";

        QuizDatabase.Subcategory physics = new QuizDatabase.Subcategory();
        physics.name = "Physics";
        QuizDatabase.Subcategory chemistry = new QuizDatabase.Subcategory();
        chemistry.name = "Chemistry";

        QuizDatabase.Subject math = new QuizDatabase.Subject();
        math.name = "Math";
        math.subcategories.add(calculus);
        math.subcategories.add(algebra);
        math.subcategories.add(geometry);

        QuizDatabase.Subject science = new QuizDatabase.Subject();
        science.name = "Science";
        science.subcategories.add(physics);
        science.subcategories.add(chemistry);

        instance.subjects.add(math);
        instance.subjects.add(science);

        /**/
        // ----------------------------------------

        addNewQuiz = findViewById(R.id.addNewQuizButton);


        // initialized all objects needed
        ActivityObjects();

        // call function that prepared our list
        AdapterData();

        //set our listeners for expandable list
        startExListeners();

        //if addNewQuiz button is clicked, move to the QuestionEditorActivity Page
        addNewQuiz.setOnClickListener(e ->
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
                selectedQuiz = subQuiz.get(subjects.get(groupPosition)).get(childPosition).name;
                //once item is selected, move to QuizAction
                Intent intent = new Intent(QuizSelector.this, QuizAction.class);
                //attach our selectedQuiz to our intent
                intent.putExtra("selectedQuiz", selectedQuiz);
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


    void ActivityObjects() {

        // initializing the list of groups
        subjects = new ArrayList<>();

        // initializing the list of child
        subCategories = new ArrayList<>();

        //creating map
        subQuiz = new HashMap<QuizDatabase.Subject, List<QuizDatabase.Subcategory>>();

        // setting list adapter
        listView.setAdapter(listViewAdapter);

    }


    private void AdapterData() {


        // data populates from quizDB disk,

        for (int i = 0; i < instance.subjects.size(); i++) {
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
        */
        // notify the adapter
        listViewAdapter.notifyDataSetChanged();
    }

}
