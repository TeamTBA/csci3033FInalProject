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

    // List / Parent items
    List<QuizDatabase.Subject> subjects;

    //the children in expandable list are stored as a map
    //each list of children is mapped to 1 subject
    Map<QuizDatabase.Subject, List<QuizDatabase.Subcategory>> subQuiz;


    public ExpandableListAdapter(Activity context, List<QuizDatabase.Subject> subjects, Map<QuizDatabase.Subject, List<QuizDatabase.Subcategory>> subQuiz) {
        this.context = context;
        this.subjects = subjects;
        this.subQuiz = subQuiz;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.subQuiz.get(this.subjects.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View view, ViewGroup parent) {

        QuizDatabase.Subcategory childCategory = (QuizDatabase.Subcategory) getChild(groupPosition, childPosition);

        final String categoryName = childCategory.name;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.activity_quiz_selector_list_child, null);
        }

        TextView selectedChild = view
                .findViewById(R.id.quizSelectorListChild);

        selectedChild.setText(categoryName);
        return view;
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
                             View view, ViewGroup parent) {

        QuizDatabase.Subject groupSubject = (QuizDatabase.Subject) getGroup(groupPosition);
        String subjectName = groupSubject.name;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.activity_quiz_selector_list_items, null);
        }

        TextView selectedGroup = view
                .findViewById(R.id.categorySelection);
        selectedGroup.setText(subjectName);

        return view;
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
