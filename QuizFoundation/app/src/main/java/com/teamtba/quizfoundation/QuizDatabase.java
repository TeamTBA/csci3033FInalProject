package com.teamtba.quizfoundation;

import android.content.Context;
import android.nfc.FormatException;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import java.lang.String;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

// this static class holds all the methods for accessing the quiz database.
// this is stored in a binary file, the format of which is implementation-defined and potentially device-specific.
// functions in this class wrap java-friendly containers defined internally.
public class QuizDatabase
{

    // ------------------ //

    // -- public types -- //

    // ------------------ //

    // represents a question from the database
    public static class Question
    {
        public String text = "N/A";
        public String[] choices = {"N/A"};
        public int answer = 0;
    }

    public static class Subcategory
    {
        public String name = "Unnamed";
        public List<Question> questions = new ArrayList<Question>();
    }

    public static class Subject
    {
        public String name = "Unnamed";
        public List<Subcategory> subcategories = new ArrayList<Subcategory>();
    }

    // ------------------- //

    // -- private types -- //

    // ------------------- //

    // represents the question database
    // this is a distinct type so that it can be serialized
    public static class Instance
    {
        // this is a singleton
        private Instance(){}
        private static final Instance instance = new Instance();

        public List<Subject> subjects = new ArrayList<Subject>();
    }

    private static final String dbfilename = "QuestionDatabase.dat";

    // ----------------------- //

    // -- control functions -- //

    // ----------------------- //

    // gets the current database instance
    public static Instance getInstance() { return Instance.instance; }

    // loads the question database from the question file.
    // returns true iff the file was successfully loaded.
    // on failure, the instance is not modified.
    public static boolean load(Context context) throws IOError, IOException, ClassNotFoundException
    {
        try (ObjectInputStream f = new ObjectInputStream(context.openFileInput(dbfilename)))
        {
            Instance obj = (Instance)f.readObject();
            Instance.instance.subjects = obj.subjects;
            return true;
        }
        // if the file doesn't exist, that's ok
        catch (FileNotFoundException ex) { return false; }
    }
    // stores the question database to the question file.
    // returns true iff the file was saved successfully.
    public static boolean store(Context context) throws IOError, IOException, ClassNotFoundException
    {
        try (ObjectOutputStream f = new ObjectOutputStream(context.openFileOutput(dbfilename, 0)))
        {
            f.writeObject(Instance.instance);
            return true;
        }
    }

    public static Subcategory getSubCategory(String string)
    {
        QuizDatabase.Subcategory selectedQuiz = null;
        try
        {
            QuizDatabase.Instance instance = QuizDatabase.getInstance();
            for (int i = 0; i < instance.subjects.size(); i++)
            {
                for (int j = 0; j < instance.subjects.get(i).subcategories.size(); j++)
                {
                    if (instance.subjects.get(i).subcategories.get(j).name == string)
                        selectedQuiz = instance.subjects.get(i).subcategories.get(j);

                }
            }
        }
        catch(Exception e)
        {
            //
        }
        return selectedQuiz;
    }
}
