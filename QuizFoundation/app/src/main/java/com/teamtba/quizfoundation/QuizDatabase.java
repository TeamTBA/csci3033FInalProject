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
    public static class Question implements Serializable
    {
        public String text = "N/A";
        public String[] choices = {"N/A"};
        public int answer = 0;
    }

    public static class Subcategory implements Serializable
    {
        public String name = "Unnamed";
        public List<Question> questions = new ArrayList<Question>();
    }

    public static class Subject implements Serializable
    {
        public String name = "Unnamed";
        public List<Subcategory> subcategories = new ArrayList<Subcategory>();
    }

    // ------------------- //

    // -- private types -- //

    // ------------------- //

    // represents the question database
    // this is a distinct type so that it can be serialized
    public static class Instance implements Serializable
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
    // if the file doesn't exist, loads nothing (as if the file existed and held an empty database)
    public static void load(Context context) throws IOError, IOException, ClassNotFoundException
    {
        try (ObjectInputStream f = new ObjectInputStream(context.openFileInput(dbfilename)))
        {
            Instance obj = (Instance)f.readObject();
            Instance.instance.subjects = obj.subjects;
        }
        // if the file doesn't exist, that's ok
        catch (FileNotFoundException ex)
        {
            Instance obj = new Instance();
            Instance.instance.subjects = obj.subjects;
        }
    }
    // stores the question database to the question file.
    public static void store(Context context) throws IOError, IOException, ClassNotFoundException
    {
        try (ObjectOutputStream f = new ObjectOutputStream(context.openFileOutput(dbfilename, 0)))
        {
            f.writeObject(Instance.instance);
        }
    }

    // ---------------------------------

    // if (name) is the name of a subject, returns the subject. otherwise returns null.
    public static Subject getSubject(String name)
    {
        for (Subject subject : getInstance().subjects)
            if (subject.name == name) return subject;

        return null;
    }

    // if (name) is the name of a subcategory, returns the subcategory. otherwise returns null.
    public static Subcategory getSubCategory(String name)
    {
        for (Subject subject : getInstance().subjects)
            for (Subcategory subcategory : subject.subcategories)
                if (subcategory.name == name) return subcategory;

        return null;
    }
}
