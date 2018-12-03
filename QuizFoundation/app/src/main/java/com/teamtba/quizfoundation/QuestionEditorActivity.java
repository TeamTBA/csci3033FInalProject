package com.teamtba.quizfoundation;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionEditorActivity extends AppCompatActivity {

    // -- intent objects -- //

    // the name of the expected intent args object
    public static final String INTENT_ARGS_NAME = "intent_args_name";

    private enum TransactionMode { add, edit }

    // the type of object to pass to the intent args (with above name)
    public static class IntentArgs implements Serializable
    {
        // the transaction mode of this invocation
        private TransactionMode mode;

        // the subject/subcategory for the question
        private int subject;
        private int subcategory;

        // the location of the question in the subject/subcategory pair - only used for edit mode
        private int question;

        // --------------------------------

        private IntentArgs() {}

        // creates args for adding a new question to the database.
        static IntentArgs NewQuestion()
        {
            IntentArgs args = new IntentArgs();

            args.mode = TransactionMode.add;
            args.subject = args.subcategory = -1;
            args.question = -1;

            return args;
        }
        // creates args for adding a new question to the specified subject and subcategory.
        // this is only the starting position - the user can elect to put it elsewhere.
        static IntentArgs NewQuestion(int subject, int subcategory)
        {
            IntentArgs args = new IntentArgs();

            args.mode = TransactionMode.add;
            args.subject = subject;
            args.subcategory = subcategory;
            args.question = -1;

            return args;
        }

        // creates args for editing a pre-existing question given its subject and subcategory
        static IntentArgs EditQuestion(int subject, int subcategory, int question)
        {
            IntentArgs args = new IntentArgs();

            args.mode = TransactionMode.edit;
            args.subject = subject;
            args.subcategory = subcategory;
            args.question = question;

            return args;
        }
    }

    // -------------------------------------------------

    // represents a set of a spinner and an edit text for selecting/adding enum values
    private class SelectorSet {
        public final Spinner spinner;
        public final EditText text;

        SelectorSet(Spinner _spinner, EditText _text){
            spinner = _spinner;
            text = _text;
        }
    }
    // represents a set of check box and edit text for possible/correct answers
    private class ChoiceSet{
        public final LinearLayout layout;
        public final CheckBox checkbox;
        public final EditText text;

        ChoiceSet(LinearLayout _layout, CheckBox _checkbox, EditText _text){
            layout = _layout;
            checkbox = _checkbox;
            text = _text;
        }
    }

    IntentArgs args = null;
    QuizDatabase.Question question = null; // the question to edit (only defined in edit mode)

    private SelectorSet subjectSelector, subcategorySelector;
    private EditText textBox;
    private ChoiceSet[] choices;

    private String[] subjects = null;
    private String[][] subcategories = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        subjectSelector = new SelectorSet(findViewById(R.id.subject_spinner), findViewById(R.id.subject_text));
        subcategorySelector = new SelectorSet(findViewById(R.id.subcategory_spinner), findViewById(R.id.subcategory_text));

        textBox = findViewById(R.id.text_edit);

        choices = new ChoiceSet[] {
                new ChoiceSet(findViewById(R.id.choice_0), findViewById(R.id.choice_0_check), findViewById(R.id.choice_0_text)),
                new ChoiceSet(findViewById(R.id.choice_1), findViewById(R.id.choice_1_check), findViewById(R.id.choice_1_text)),
                new ChoiceSet(findViewById(R.id.choice_2), findViewById(R.id.choice_2_check), findViewById(R.id.choice_2_text)),
                new ChoiceSet(findViewById(R.id.choice_3), findViewById(R.id.choice_3_check), findViewById(R.id.choice_3_text)),
        };

        // ---------------- //

        // -- tie events -- //

        // ---------------- //

        subjectSelector.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectSelector.text.setVisibility(position == subjectSelector.spinner.getAdapter().getCount() - 1 ? View.VISIBLE : View.INVISIBLE);

                subcategorySelector.spinner.setAdapter(new ArrayAdapter<String>(QuestionEditorActivity.this, android.R.layout.simple_list_item_1, subcategories[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subcategorySelector.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subcategorySelector.text.setVisibility(position == subcategorySelector.spinner.getAdapter().getCount() - 1 ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for (ChoiceSet choice : choices)
        {
            choice.checkbox.setOnCheckedChangeListener((e, v) -> {
                if (v) {
                    for (ChoiceSet other : choices)
                        other.checkbox.setChecked(other == choice);
                }
            });
        }

        findViewById(R.id.accept_button).setOnClickListener(e -> ProcessAccept());

    }

    @Override
    protected void onStart() {
        super.onStart();

        // get the intent args
        args = (IntentArgs)getIntent().getSerializableExtra(INTENT_ARGS_NAME);

        // -- set up the spinners -- //

        UpdateCategories();

        subjectSelector.spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjects));
        subcategorySelector.spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subcategories[0]));

        // navigate to the pre-set item
        subjectSelector.spinner.setSelection(args.subject);
        subcategorySelector.spinner.setSelection(args.subcategory);

        // -- set up the requested info -- //

        // if we're in edit mode, get question info
        if (args.mode == TransactionMode.edit)
        {
            // get the question
            question = QuizDatabase.getInstance()
                    .subjects.get(args.subject)
                    .subcategories.get(args.subcategory)
                    .questions.get(args.question);

            textBox.setText(question.text);

            for (int i = 0; i < choices.length; ++i)
            {
                choices[i].text.setText(i < question.choices.length ? question.choices[i] : "");
            }

            // check answer - this will propagate and uncheck all others due to the event handler
            choices[question.answer].checkbox.setChecked(true);
        }
        // if we're in add mode, fill with blanks
        else if (args.mode == TransactionMode.add)
        {
            question = null;

            textBox.setText("");

            for (int i = 0; i < choices.length; ++i)
            {
                choices[i].text.setText("");
                choices[i].checkbox.setChecked(false);
            }
        }
        // otherwise unknown transaction mode
        else { throw new IllegalArgumentException("unknown question editor transaction mode"); }
    }

    // processes the actions of the accept button
    private void ProcessAccept()
    {
        QuizDatabase.Instance instance = QuizDatabase.getInstance();

        // -- validate subject / subcategory -- //

        int subject = subjectSelector.spinner.getSelectedItemPosition();
        subject = subject < subjectSelector.spinner.getCount() - 1 ? subject : -1;

        int subcategory = subcategorySelector.spinner.getSelectedItemPosition();
        subcategory = subcategory < subcategorySelector.spinner.getCount() - 1 ? subcategory : -1;

        String subject_text = null;
        String subcategory_text = null;

        if (subject < 0){
            subject_text = subjectSelector.text.getText().toString().trim();

            // can't be empty string
            if (subject_text.isEmpty()){
                Toast.makeText(this, "New subject name cannot be blank", Toast.LENGTH_LONG).show();
                return;
            }
            // can't already exist
            if (instance.findSubject(subject_text) != null){
                Toast.makeText(this, "New subject name already exists", Toast.LENGTH_LONG).show();
                return;
            }
        }

        if (subcategory < 0){
            subcategory_text = subcategorySelector.text.getText().toString().trim();

            // can't be empty string
            if (subcategory_text.isEmpty()){
                Toast.makeText(this, "New subcategory name cannot be blank", Toast.LENGTH_LONG).show();
                return;
            }
            // can't already exist
            if (subject < instance.subjects.size() && instance.subjects.get(subject).findSubcategory(subcategory_text) != null){
                Toast.makeText(this, "New subcategory name already exists", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // -- validate text -- //

        String text = textBox.getText().toString().trim();

        // can't be empty
        if (text.isEmpty()){
            Toast.makeText(this, "Question field cannot be blank", Toast.LENGTH_LONG).show();
            return;
        }

        // -- calidate choices / aswer -- //

        List<String> _choices = new ArrayList<String>(choices.length);
        int answer = -1;

        for (int i = 0; i < choices.length; ++i)
        {
            if (choices[i].checkbox.isChecked()) {
                if (answer < 0) answer = i;
                else {
                    Toast.makeText(this, "There can only be one answer", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            String str = choices[i].text.getText().toString().trim();

            if (!str.isEmpty()) _choices.add(str);
            else if (choices[i].checkbox.isChecked()) {
                Toast.makeText(this, "Answer cannot be blank", Toast.LENGTH_LONG).show();
                return;
            }
        }

        if (_choices.isEmpty()){
            Toast.makeText(this, "Must have at least one choice", Toast.LENGTH_LONG).show();
            return;
        }
        if (answer < 0){
            Toast.makeText(this, "Must have an answer", Toast.LENGTH_LONG).show();
            return;
        }

        // -- handle database insertions -- //

        // switch on mode
        switch(args.mode)
        {
            case add:

                // create the subject if it doesn't exist
                if (subject < 0)
                {
                    QuizDatabase.Subject s = new QuizDatabase.Subject();
                    s.name = subject_text;

                    subject = instance.subjects.size();
                    instance.subjects.add(s);
                }
                // create the subcategory if it doesn't exist
                if (subcategory < 0)
                {
                    QuizDatabase.Subcategory s = new QuizDatabase.Subcategory();
                    s.name = subcategory_text;

                    subcategory = instance.subjects.get(subject).subcategories.size();
                    instance.subjects.get(subject).subcategories.add(s);
                }

                // add the question
                question = new QuizDatabase.Question();
                instance.subjects.get(subject).subcategories.get(subcategory).questions.add(question);

                break;

            case edit:

                // no extra work here - merges with add case below

                break;

            default: throw new IllegalArgumentException("unknown question editor args mode");
        }

        // -- final touches - apply the changes -- //

        // update the question
        question.text = text;
        question.choices = _choices.toArray(new String[0]);
        question.answer = answer;

        // save the result and return
        try
        {
            QuizDatabase.store(this);
            finish(); // we're finally done! :D
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "FAILED TO SAVE QUESTION DATABASE", Toast.LENGTH_LONG).show();
            return;
        }
    }

    // updates the categories arrays but not the spinner adapters.
    // this should be called each time the activity is shown.
    private void UpdateCategories()
    {
        QuizDatabase.Instance instance = QuizDatabase.getInstance();

        subjects = new String[instance.subjects.size() + 1];
        subcategories = new String[instance.subjects.size() + 1][];

        for (int i = 0; i < instance.subjects.size(); ++i) {
            QuizDatabase.Subject subj = instance.subjects.get(i);

            subjects[i] = subj.name;
            subcategories[i] = new String[subj.subcategories.size() + 1];

            for (int j = 0; j < subj.subcategories.size(); ++j) subcategories[i][j] = subj.subcategories.get(j).name;

            subcategories[i][subj.subcategories.size()] = "New Entry";
        }

        subjects[instance.subjects.size()] = "New Entry";
        subcategories[instance.subjects.size()] = new String[] { "New Entry" };
    }
}
