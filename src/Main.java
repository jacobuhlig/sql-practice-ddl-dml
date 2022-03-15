import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Main {

    private static Scanner scan;


    public static void main(String[] args) {
        String filepath = "data";
        String filename = "imdb-data";

        String ddlTable = createDDLTable(filepath, filename);
        System.out.println(ddlTable);

        createDDL(ddlTable);

        String dmlTable = createDMLTable(filepath, filename);
        System.out.println(dmlTable);

        createDML(dmlTable);
    }


    public static String[] readerCategories(String filepath, String filename) {
        File file = new File(filepath + "/" + filename + ".csv");

        try {
            scan = new Scanner(file);

            String category = scan.nextLine();

            String[] asStringArray = category.split(";");

            return asStringArray;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String createDDLTable(String filepath, String filename) {


        String[] categories = readerCategories(filepath, filename);

        String sql = "";

        for (int i = 0; i < categories.length - 1; i++) {
            sql = sql + categories[i] + " varchar(255), \n";
        }

        for (int i = categories.length - 1; i < categories.length; i++) {
            sql = sql + categories[i] + " varchar(255) \n";
        }

        String createTable = "CREATE TABLE " + filename + " (\n" +
                sql + ");";

        return createTable;
    }


    public static String createDMLTable(String filepath, String filename) {

        String wholeSQLString = "";
        String sql = "INSERT INTO " + filename + " (";
        String sqlCategories = "";

        String[] categories = readerCategories(filepath, filename);

        for (int i = 0; i < categories.length - 1; i++) {
            sqlCategories = sqlCategories + categories[i] + ", ";
        }

        for (int i = categories.length - 1; i < categories.length; i++) {
            sqlCategories = sqlCategories + categories[i] + ")";
        }

        sql = sql + sqlCategories;


        while(scan.hasNext()){
            String currLine = scan.nextLine();
            String[] split = currLine.split(";");
            String data = "";
            for (String value : split) {
                if(split[0].matches(value))
                    data = "'" + value + "'";
                else
                    data = data + ", " + "'" + value + "'";
            }
            sql = sql + "\nVALUES (" + data + ");";
        }
        return sql;
    }


    public static void createDDL(String content) {
        File file = new File("data/ddl.sql");

        try {
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createDML(String content) {
        File file = new File("data/dml.sql");

        try {
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
