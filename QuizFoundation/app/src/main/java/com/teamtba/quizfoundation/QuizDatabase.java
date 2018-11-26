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

// this static class holds all the methods for accessing the quiz database.
// this is stored in a binary file, the format of which is implementation-defined and potentially device-specific.
// functions in this class wrap java-friendly containers defined internally.
public class QuizDatabase {

    // ---------------------------------------------------- //

    // convenience type definitions

    // ---------------------------------------------------- //

    // represents a question from the databasee
    public static class Question{
        private long id = -1;

        private String text = null;
        private String[] choices = null;
        private int answer = 0;

        // --------------------

        public long getId() { return id; }

        public String getText() { return text; }
        public void setText(String newText) { text = newText; }

        public String[] getChoices() { return choices; }
        public void setChoices(String[] newChoices) { choices = newChoices; }

        public int getAnswer() { return answer; }
        public void setAnswer(int newAnswer) { answer = newAnswer; }
    }

    // ---------------------------------------------------- //

    // the stuff in this section is implementation-specific
    // skip to interface for the usable stuff

    // ---------------------------------------------------- //

    /*
    question database binary formats:

    database:
        header: 8 bytes = number of questions
        content: that many questions

    string:
        length:  1 bytes
        content: 255 bytes (only up to length is counted)

    question:
        answer: 1 byte
        text:   string (see above)
        choices 4x strings (see above)
    */

    private static final int databaseMaxStringLen = 255;
    private static final Charset databaseStringCharset = StandardCharsets.UTF_8;

    private static final long databaseQuestionLen = 1 + 256 + 4*256;

    // -- low level io functions -- //

    private static long readLong(FileInputStream database) throws IOException, FormatException
    {
        byte[] buf = new byte[8];
        if (database.read(buf) != buf.length) throw new FormatException("question database corrupted");
        return ByteBuffer.wrap(buf).getLong();
    }
    private static int readInt(FileInputStream database) throws IOException, FormatException
    {
        byte[] buf = new byte[4];
        if (database.read(buf) != buf.length) throw new FormatException("question database corrupted");
        return ByteBuffer.wrap(buf).getInt();
    }
    private static short readShort(FileInputStream database) throws IOException, FormatException
    {
        byte[] buf = new byte[2];
        if (database.read(buf) != buf.length) throw new FormatException("question database corrupted");
        return ByteBuffer.wrap(buf).getShort();
    }
    private static int readByte(FileInputStream database) throws IOException, FormatException
    {
        int buf = database.read();
        if (buf == -1) throw new FormatException("question database corrupted");
        return (byte)buf;
    }

    private static void writeLong(FileOutputStream database, long val) throws IOException
    {
        database.write(ByteBuffer.allocate(8).putLong(val).array());
    }
    private static void writeInt(FileOutputStream database, int val) throws IOException
    {
        database.write(ByteBuffer.allocate(4).putInt(val).array());
    }
    private static void writeShort(FileOutputStream database, short val) throws IOException
    {
        database.write(ByteBuffer.allocate(2).putShort(val).array());
    }
    private static void writeByte(FileOutputStream database, byte val) throws IOException
    {
        database.write(val);
    }

    // -- special low-level io functions -- //

    private static String readString(FileInputStream database) throws IOException, FormatException
    {
        int header;
        byte[] content = new byte[255];

        if ((header = database.read()) == -1) throw new FormatException("question database corrupted");
        if (database.read(content) != content.length) throw new FormatException("question database corrupted");

        return new String(content, 0, header, databaseStringCharset);
    }
    private static void writeString(FileOutputStream database, String string) throws IOException, IllegalArgumentException
    {
        byte[] encoded_string = string.getBytes(databaseStringCharset);

        if (encoded_string.length > databaseMaxStringLen) throw new IllegalArgumentException("string exceeds maximum length");

        database.write(string.length());
        database.write(Arrays.copyOf(encoded_string, databaseMaxStringLen)); // extend to the full size before writing
    }

    // ---------------------------------------------------- //

    // database interface functions

    // ---------------------------------------------------- //

    private static final String databaseFileName = "qdatabase.bin";

    // gets the number of questions in the question database
    public static long GetQuestionCount(Context context) throws IOException, FormatException
    {
        try (FileInputStream database = context.openFileInput(databaseFileName))
        {
            // read number of questions in the database
            return readLong(database);
        }
        // if there's no file, there are no questions
        catch (FileNotFoundException ex) { return 0; }
    }

    // gets the question with the specified id
    public static Question GetQuestion(Context context, int id) throws IOException, FormatException
    {
        try (FileInputStream database = context.openFileInput(databaseFileName))
        {
            // get number of questions
            long qcount = readLong(database);

            // if requested id is out of bounds, return null
            if (id >= qcount) return null;

            // otherwise, skip to the correct question
            long skip_count = id * databaseQuestionLen;
            if (database.skip(skip_count) != skip_count) throw new FormatException("question database corrupted");

            Question question = new Question();

            question.id = id;
            question.answer = readByte(database);
            question.text = readString(database);
            question.choices = new String[4];
            for (int i = 0; i < question.choices.length; ++i) question.choices[i] = readString(database);

            return question;
        }
        catch (FileNotFoundException ex) { return null; }
    }
}
