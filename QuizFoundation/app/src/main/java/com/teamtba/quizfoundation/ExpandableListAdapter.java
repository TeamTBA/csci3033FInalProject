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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


//-----------------------------------------------------------------------------
//       Adapter code developed / derived from tutorial found at;
//   http://www.androidtutorialshub.com/android-expandable-list-view-tutorial/
//-----------------------------------------------------------------------------
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;

    // List / Parent items
    QuizDatabase.Instance instance;

    public ExpandableListAdapter(Activity context){
        this.context = context;
        this.instance = QuizDatabase.getInstance();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return instance.subjects.get(groupPosition).subcategories.get(childPosition);
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
        return instance.subjects.get(groupPosition).subcategories.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return instance.subjects.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return instance.subjects.size();
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
