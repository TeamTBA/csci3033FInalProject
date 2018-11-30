package com.teamtba.quizfoundation;

import android.content.Context;
import android.nfc.FormatException;
import android.util.Xml;

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
public class QuizDatabase {

    // ------------------ //

    // -- public types -- //

    // ------------------ //

    // represents a question from the databasee
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

    // ----------------------- //

    // -- control functions -- //

    // ----------------------- //

    public static Instance getInstance() { return Instance.instance; }
}
