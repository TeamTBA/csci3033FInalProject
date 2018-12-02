package com.teamtba.quizfoundation;

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

import java.util.ArrayList;
import java.util.List;

public class QuestionEditorActivity extends AppCompatActivity {

    // these static fields are how we'll communicate between activities.
    // since android can't have multiple instances of the same activity open at any time this is safe.

    // accept marks if the user confirmed the changes.
    // subject/subcategory are >= 0 for a pre-existing value or < 0 for a new value.
    // if < 0 (new value) is selected, the text field is the name of the desired new subject/subcategory.
    // choices length must be in the range [0, 4]

    public static boolean ACCEPT = false;

    public static int SUBJECT = -1;
    public static String SUBJECT_TEXT = null;

    public static int SUBCATEGORY = -1;
    public static String SUBCATEGORY_TEXT = null;

    public static String TEXT = null;
    public static String[] CHOICES = null;
    public static int ANSWER = -1;

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

    private SelectorSet subjectSelector, subcategorySelector;
    private EditText textBox;
    private ChoiceSet[] choices;

    private String[] subjects;
    private String[][] subcategories;

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

                subcategorySelector.spinner.setAdapter(new ArrayAdapter<String>(
                        QuestionEditorActivity.this, android.R.layout.simple_list_item_1, subcategories[position]));
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

        for (ChoiceSet choice : choices){
            choice.checkbox.setOnCheckedChangeListener((e, v) -> {
                if (v) {
                    for (ChoiceSet other : choices)
                        other.checkbox.setChecked(other == choice);
                }
            });
        }

        findViewById(R.id.accept_button).setOnClickListener(e -> {
            if (StoreValues()) {
                ACCEPT = true;
                finish();
            }
        });
    }

    // stores the current values into the static variables
    private boolean StoreValues()
    {

        int subject = subjectSelector.spinner.getSelectedItemPosition();
        SUBJECT = subject < subjectSelector.spinner.getCount() - 1 ? subject : -1;

        int subcategory = subcategorySelector.spinner.getSelectedItemPosition();
        SUBCATEGORY = subcategory < subcategorySelector.spinner.getCount() - 1 ? subcategory : -1;

        SUBJECT_TEXT = SUBJECT < 0 ? subjectSelector.text.getText().toString() : null;
        SUBCATEGORY_TEXT = SUBCATEGORY < 0 ? subcategorySelector.text.getText().toString() : null;

        TEXT = textBox.getText().toString();

        List<String> choi = new ArrayList<String>(choices.length);
        int answer = -1;

        for (int i = 0; i < choices.length; ++i)
        {
            if (choices[i].checkbox.isChecked()) {
                if (answer < 0) answer = i;
                else {
                    Toast.makeText(this, "There can only be one answer", Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            String str = choices[i].text.getText().toString().trim();

            if (!str.isEmpty()) choi.add(str);
            else if (choices[i].checkbox.isChecked()) {
                Toast.makeText(this, "Answer cannot be blank", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (choi.isEmpty()){
            Toast.makeText(this, "Must have at least one choice", Toast.LENGTH_LONG).show();
            return false;
        }
        if (answer < 0){
            Toast.makeText(this, "Must have an answer", Toast.LENGTH_LONG).show();
            return false;
        }

        String[] _choi = new String[choi.size()];
        for (int i = 0; i < choi.size(); ++i) _choi[i] = choi.get(i);
        CHOICES = _choi;

        ANSWER = answer;

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // mark as not accepted
        ACCEPT = false;

        // -- set up the spinners -- //

        UpdateCategories();

        subjectSelector.spinner.setSelection(0);
        subcategorySelector.spinner.setSelection(0);

        subjectSelector.spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjects));
        subcategorySelector.spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subcategories[0]));

        // -- set up the requested info -- //

        textBox.setText(TEXT != null ? TEXT : "");

        for (int i = 0; i < choices.length; ++i){
            choices[i].text.setText(CHOICES != null && i < CHOICES.length ? CHOICES[i] : "");
            choices[i].checkbox.setChecked(ANSWER == i);
        }
    }

    // updates the categories arrays but not the spinner adapters.
    // this should be called each time the activity is shown.
    private void UpdateCategories(){
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
    }
}
