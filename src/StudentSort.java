import jdk.swing.interop.SwingInterOpUtils;

import java.io.*;
import java.util.ArrayList;


public class StudentSort {



    private BufferedReader reader;
    private FileWriter fileWriter;
    private ArrayList<Name> names;
    private ArrayList<Float> grades;
    private String stringToParse;
    private BufferedWriter writer;
    private boolean isAsc;
    private String csvTitleString;



    public StudentSort() {
        names = new ArrayList<Name>();
        grades = new ArrayList<Float>();

        csvTitleString = "name,grade";

    }

    public void input(String input) throws IOException {
        FileReader fileReader = new FileReader(input);

        reader = new BufferedReader(fileReader);


        // refresh the names and grades
        names = new ArrayList<Name>();
        grades = new ArrayList<Float>();


        // the first line contain the column titles for CSV or the entirety of the JSON file
        stringToParse = reader.readLine();
        stringToParse = stringToParse.toLowerCase();

        // Read JSON
        if (stringToParse.charAt(0) == '[') {

            parseJSON();
        }
        // read CSV
        else {

            // read the CSV lines
            while (true) {
                stringToParse = reader.readLine();


                if (stringToParse == null) {
                    break;
                } else {
                    parseCSV();
                }
            }

        }

    }

    // Used to ouput a file.
    public void output(String output, boolean outputCSV) throws IOException {

        if (outputCSV) {
            output += ".csv";
        } else {
            output += ".json";
        }

        fileWriter = new FileWriter(output);
        writer = new BufferedWriter(fileWriter);


        if (outputCSV) {
            writeCSV();
        } else {
            writeJSON();
        }
    }


    private void parseJSON() {

        boolean readingValue = false;
        // false means we are recording the grade
        boolean isName = true;

        int pos1;
        int pos2;
        String tempString = "";


        for (int i = 0; i < stringToParse.length(); i++) {


            if (readingValue) {
                if (isName) {

                    if (stringToParse.charAt(i) == ',') {
                        readingValue = false;

                        // switch to recording grade now
                        isName = false;


                        // trimmed string
                        names.add(new Name(tempString, true));

                        tempString = "";
                    } else {
                        tempString += stringToParse.charAt(i);
                    }
                }
                // reading a grade
                else {
                    if (stringToParse.charAt(i) == '}') {
                        readingValue = false;

                        grades.add(Float.parseFloat(tempString));
                        tempString = "";

                        // switch to recording a name now
                        isName = true;


                    } else {
                        tempString += stringToParse.charAt(i);
                    }
                }
            } else {
                if (stringToParse.charAt(i) == ':') {
                    readingValue = true;
                    pos1 = i + 1;
                }
            }
        }
    }

    public void writeCSV() throws IOException {


        writer.write(csvTitleString);


        for (int i = 0; i < names.size(); i++) {
            writer.newLine();
            writer.write(names.get(i).toString());
            writer.write(",");
            writer.write(Float.toString(grades.get(i)));
        }

        writer.flush();
        writer.close();


    }

    private void writeJSON() throws IOException {


        writer.write("[");

        for (int i = 0; i < names.size(); i++) {
            writer.write("{\"name\":\"");
            writer.write(names.get(i).toString());
            writer.write("\",\"grade\":");
            writer.write(Float.toString(grades.get(i)));

            if (i == (names.size() - 1)) {
                writer.write("}]");
            } else {
                writer.write("},");
            }
        }

        writer.flush();
        writer.close();

    }


    private void parseCSV() {


        int i;

        for (i = 0; i < stringToParse.length(); i++) {
            if (stringToParse.charAt(i) == ',') {


                stringToParse = stringToParse.toLowerCase();
                //once we have come to the comma, append the substring before that, which will be the name
                names.add(new Name(stringToParse.substring(0, (i))));

                break;
            }

        }

        i++;
        grades.add(Float.parseFloat(stringToParse.substring(i)));
    }


    public void sortByNameAscending() {
        isAsc = true;
        sortByName();
    }

    public void sortByNameDescending() {
        isAsc = false;
        sortByName();
    }

    public void sortByGradeAscending() {
        isAsc = true;
        sortByGrade();
    }

    public void sortByGradeDescending() {
        isAsc = false;
        sortByGrade();
    }

    private void sortByName() {

        float temp;

        for (int i = 0; i < (names.size() - 1); i++)
            for (int j = 0; j < (names.size() - i - 1); j++)


                if (
                        ascTest(
                                names.get(j).compare(names.get(j + 1))
                        )
                ) {

                    // switch grades
                    temp = grades.get(j);
                    grades.set(j, grades.get(j + 1));
                    grades.set(j + 1, temp);


                    //switch names
                    names.get(j).switchNames(names.get(j + 1));
                }

    }


    // bubble sort
    private void sortByGrade() {

        float temp;

        Name tempName;

        // size - 1 because you don't want out of bounds error

        for (int i = 0; i < (grades.size() - 1); i++)

            /*
             -i because the end elements don't need to checked
             as time goes on because they are already sorted.
              */

            for (int j = 0; j < (grades.size() - i - 1); j++)


                // ascending
                if (
                        ascTest(
                                grades.get(j) > grades.get(j + 1)
                        )
                ) {

                    // switch grades
                    temp = grades.get(j);
                    grades.set(j, grades.get(j + 1));
                    grades.set(j + 1, temp);
                    

                    //switch names
                    tempName = names.get(j);
                    names.set(j, names.get(j + 1));
                    names.set(j + 1, tempName);
                }

    }


    // used for switching ascending and descending
    // flips a boolean in the case of wanting to do descending ordering
    private boolean ascTest(boolean b) {

        if (isAsc) {
            return b;
        }

        return !b;
    }


    // for testing etc
    public void print() {


        System.out.println("-----CSV style output----");
        System.out.println(csvTitleString);

        for (int i = 0; i < names.size(); i++) {

            System.out.println(names.get(i) + "," + grades.get(i));
        }

        System.out.println("------------end----------");

    }


    public static void main(String[] args) throws IOException {

        /*
        Instructions:
        1. create a StudentSort object
        2. call the input method ( input a CSV or JSON file - auto-detected , but the formatting is strict )
            input the correct directory in the fileDir String
        3. optionally call various sorting methods, ascending/descending according to name/grade.
        4. output the file using the output method, with the file path,
        and a boolean signalling true for CSV, false for JSON. The file extension is added automatically so leave this out
         */


        // change the String to the correct directory below
        String fileDir = "C:\\Users\\J\\Desktop\\Onaware\\";

        String input;
        String output;

        StudentSort ss = new StudentSort();

        // testing of all possible operations is below.

                /*
        No input file will only result in files lacking content,
        and calls to sorting methods will not crash the program
         */

        output = fileDir + "blank file with no input";

        ss.sortByNameAscending();
        ss.sortByNameDescending();
        ss.sortByGradeAscending();
        ss.sortByGradeDescending();

        ss.output(output,true);
        ss.output(output,false);



        /*
            Using an extended set of names,
            to test the sorting according to all the characters of a string,
            and the standardisation of names to lowercase for proper sorting

         */

        input = fileDir + "students - extra names.csv";
        ss.input(input);

        ss.sortByNameAscending();
        output = fileDir + "students - extra names ascending order";
        ss.output(output,true);

        ss.sortByNameDescending();
        output = fileDir + "students - extra names descending order";
        ss.output(output,true);

        // test output
        ss.print();




        // Below are all other remaining tests
        /*
            CSV input
         */

        input = fileDir + "students.csv";
        ss.input(input);


        // CSV --> CSV
        // Sort Names
        ss.sortByNameAscending();
        output = fileDir + "students - CSV to CSV - names ascending";
        ss.output(output, true);

        ss.sortByNameDescending();
        output = fileDir + "students - CSV to CSV - names descending";
        ss.output(output, true);


        // Sort Grades
        ss.sortByGradeAscending();
        output = fileDir + "students - CSV to CSV - grades ascending";
        ss.output(output, true);

        ss.sortByGradeDescending();
        output = fileDir + "students - CSV to CSV - grades descending";
        ss.output(output, true);


        // CSV to JSON
        // Sort Names
        ss.sortByNameAscending();
        output = fileDir + "students - CSV to JSON - names ascending";
        ss.output(output, false);

        ss.sortByNameDescending();
        output = fileDir + "students - CSV to JSON - names descending";
        ss.output(output, false);


        // Sort Grades
        ss.sortByGradeAscending();
        output = fileDir + "students - CSV to JSON - grades ascending";
        ss.output(output, false);

        ss.sortByGradeDescending();
        output = fileDir + "students - CSV to JSON - grades descending";
        ss.output(output, false);


        /*
            JSON input
         */
        input = fileDir + "students.json";
        ss.input(input);

        // JSON --> CSV
        // Sort Names
        ss.sortByNameAscending();
        output = fileDir + "students - JSON to CSV - names ascending";
        ss.output(output, true);

        ss.sortByNameDescending();
        output = fileDir + "students - JSON to CSV - names descending";
        ss.output(output, true);


        // Sort Grades
        ss.sortByGradeAscending();
        output = fileDir + "students - JSON to CSV - grades ascending";
        ss.output(output, true);

        ss.sortByGradeDescending();
        output = fileDir + "students - JSON to CSV - grades descending";
        ss.output(output, true);


        // JSON to JSON
        // Sort Names
        ss.sortByNameAscending();
        output = fileDir + "students - JSON to JSON - names ascending";
        ss.output(output, false);

        ss.sortByNameDescending();
        output = fileDir + "students - JSON to JSON - names descending";
        ss.output(output, false);


        // Sort Grades
        ss.sortByGradeAscending();
        output = fileDir + "students - JSON to JSON - grades ascending";
        ss.output(output, false);

        ss.sortByGradeDescending();
        output = fileDir + "students - JSON to JSON - grades descending";
        ss.output(output, false);

        ss.print();




    }

}
