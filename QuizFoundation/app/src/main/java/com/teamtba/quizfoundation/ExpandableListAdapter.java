package com.teamtba.quizfoundation;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;

    // List Items
    List<String> subjects;

    //proper List Items
   // List<QuizDatabase.Subject> subjects;

    // Child List Items
    //Map<QuizDatabase.Subject, List<QuizDatabase.Subcategory>> subCategories;

    //dummy child list Items to test
    //Map<String, List<String>> subCategories;
    //proper storage
    Map<QuizDatabase.Subject, List<QuizDatabase.Subcategory>> subQuiz;


    public ExpandableListAdapter(Activity context, List<String> subjects, Map<QuizDatabase.Subject, List<QuizDatabase.Subcategory>> subQuiz) {
        this.context = context;
        this.subjects = subjects;
        this.subQuiz = subQuiz;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.subQuiz.get(this.subjects.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_quiz_selector_list_child, null);
        }

        TextView textViewChild = convertView
                .findViewById(R.id.quizSelectorListChild);

        textViewChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.subQuiz.get(this.subjects.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.subjects.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.subjects.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_quiz_selector_list_items, null);
        }

        TextView textViewGroup = convertView
                .findViewById(R.id.categorySelection);
        textViewGroup.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
