package com.library.utilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DB_Utility {
    private static Connection con; // make it static field so we can reuse in every methods we write
    private static Statement stm;
    private static ResultSet rs;
    private static ResultSetMetaData rsmd;

    /**
     * Create connection method , just checking one connection successful or not
     */
    public static void createConnection() {

        String url = ConfigurationReader.getProperty("hr.database.url");
        String username = ConfigurationReader.getProperty("hr.database.username");
        String password = ConfigurationReader.getProperty("hr.database.password");

//        try {
//            con = DriverManager.getConnection(url, username, password);
//            System.out.println("Connection Successful");
//        } catch (SQLException e) {
//            System.out.println("Connection has Failed!!!" + e.getMessage());
//        }
        createConnection(url,username,password);
    }

    /**
     * Create Connection by jdbc url and username, password provided
     *
     * @param url
     * @param username
     * @param password
     */

    public static void createConnection(String url, String username, String password) {

        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Successful");
        } catch (SQLException e) {
            System.out.println("Connection has Failed!!!" + e.getMessage());
        }
    }

    /**
     * Create a method called runQuery that accept a SQL Query
     *
     * @param sql
     * @return ResultSet Object thai contains data
     */
    public static ResultSet runQuery(String sql) {
        try {
            stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql); //Setting the value of ResultSet object
            rsmd = rs.getMetaData(); // Setting the value of ResultSetMetaData for reuse
        } catch (SQLException e) {
            System.out.println("Error Occurred While Running Query" + e.getMessage());
        }
        return rs;
    }

    /**
     * This method will reset the cursor to before first location
     */
    private static void resetCursor(){
        try {
            rs.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * create a method to clean up all the connection Statement and ResultSet
     */
    public static void destroy() {
        try {
            // We have to check if we have the valid object first before closing the resource
            // Because we can not take action on an OBJECT that does not exist
            if (rs != null) rs.close();
            if (rs != null) stm.close();
            if (rs != null) con.close();
            System.out.println("Connection Successfully Closed!!!");
        } catch (SQLException e) {
            System.out.println("Error Occurred While Closing Resources " + e.getMessage());
        }
    }

    /**
     * find out how many row we have
     * @return the row count of the ResultSet we got
     */
    public static int getRowCount() {
        int rowCount = 0;
        try {
            rs.last();
            rowCount = rs.getRow();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowCount;
    }

    /**
     * find out how many column we have
     *
     * @return the column count of the ResultSet we got
     */
    public static int getColumnCount() {
        int columnCount = 0;
        try {
            columnCount = rsmd.getColumnCount();
        } catch (SQLException e) {
            System.out.println("Error Occurred While Getting Column Count " + e.getMessage());
        }
        return columnCount;
    }

    /**
     * Get all the Column names into a list object
     *
     * @return All Column Names as a List of String
     */
    public static List<String> getColumnNamesAsList() {

        List<String> columnNameLst = new ArrayList<>();

        for (int colIndex = 1; colIndex <= getColumnCount(); colIndex++) {
            try {
                columnNameLst.add(rsmd.getColumnName(colIndex));
            } catch (SQLException e) {
                System.out.println("Error occured while getting Column names " + e.getMessage());
            }
        }
        return columnNameLst;
    }

    /**
     * Get entire entire row of data according to row number that you provide
     * @param rowNum
     * @return Perticular Row data that you provided rowNum
     */
    public static List<String> getRowDataAsList(int rowNum) {

        List<String> rowDataAsLst = new ArrayList<>();
        int colCount = getColumnCount();

        try {
            rs.absolute(rowNum);
            for (int colIndex = 1; colIndex <= colCount; colIndex++) {
                rowDataAsLst.add(rs.getString(colIndex));
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while get perticular row data as List " + e.getMessage());
        }
        return rowDataAsLst;
    }

    /**
     * Getting the cell value according to row num and column index
     * @param rowNum row number
     * @param colIndex column number
     * @return Cell value as String
     */
    public static String getCellValue(int rowNum, int colIndex) {
        String cellValue = "" ;

        try {
            rs.absolute(rowNum);
            cellValue = rs.getString(colIndex);
        } catch (SQLException e) {
            System.out.println( "Error occurred while getting cell value " + e.getMessage() );
        }
        return cellValue;
    }

    /**
     * Getting the cell value according to row num and column index
     * @param rowNum row number
     * @param colName column Name
     * @return Cell value as String
     * =
     */
    public static String getCellValue(int rowNum, String colName) {
        String cellValue = "" ;

        try {
            rs.absolute(rowNum);
            cellValue = rs.getString(colName);
        } catch (SQLException e) {
            System.out.println( "Error occurred while getting cell value " + e.getMessage() );
        }
        return cellValue;
    }

    /**
     * return value of all cells in that column
     * @param colNum the column number you want to get the list out of
     * @return value of all cells in that column as a List<String>
     */
    public static List<String> getColumnDataAsList(int colNum){
        resetCursor();
        List<String> columnDatalst = new ArrayList<>();
        try {
            while (rs.next()){
                columnDatalst.add(rs.getString(colNum));
            }
        } catch (SQLException e) {
            System.out.println( "Error occurred while getting cell data of a perticular column " + e.getMessage() );
        }finally {
            resetCursor();
        }
        return columnDatalst;
    }

    /**
     * return value of all cells in that column
     * @param colName the column name you want to get the list out of
     * @return value of all cells in that column as a List<String>
     */
    public static List<String> getColumnDataAsList(String colName){
        resetCursor();
        List<String> columnDatalst = new ArrayList<>();
        try {
            while (rs.next()){
                columnDatalst.add(rs.getString(colName));
            }
        } catch (SQLException e) {
            System.out.println( "Error occurred while getting cell data of a particular column " + e.getMessage() );
        }finally {
            resetCursor();
        }
        return columnDatalst;
    }

    /**
     * A method that display all the result set data on console
     */
    public static void displayAllData(){
        resetCursor();
        try {
            while (rs.next()){
                int rowNum = rs.getRow();
                for (int colIndex = 1; colIndex <= getColumnCount(); colIndex++) {
                    //System.out.print(rs.getString(colIndex)+"\t");
                    //for making it pretty
                    System.out.printf( "%-20s", rs.getString(colIndex));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error While getting All data from the table " + e.getMessage());
        } finally {
            resetCursor();
        }
    }

    /**
     *  Save entire row data as Map<String, String>
     * @param rowNum
     * @return Map object that contains key value pair
     *      key     : column name
     *      value   : cell value
     */
    public static Map<String,String> getRowMap(int rowNum){
        Map<String,String> rowMap = new LinkedHashMap<>();
        try {
            rs.absolute(rowNum);
            for (int colIndex = 1; colIndex <= getColumnCount(); colIndex++) {
                String columnName = rsmd.getColumnName(colIndex);
                String cellValue  = rs.getString(colIndex);
                rowMap.put(columnName, cellValue);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred While getRowMap " + e.getMessage());
        } finally {
            resetCursor();
        }
        return rowMap;
    }

    /**
     * We know how to store one row as map object
     * Now Store All rows as List of Map object
     * @return List of List of Map object that contain each row data as Map<String,String>
     */
    public static List<Map<String,String>> getAllRowAsListOfMap(){

        List<Map<String,String>> allRowLstOfMap = new ArrayList<>();
        int rowCount = getRowCount();
        // move from first row till last row
        // get each row as map object and add it to the list

        for (int rowIndex = 1; rowIndex <= rowCount; rowIndex++) {

            Map<String,String> rowMap = getRowMap(rowIndex);
            allRowLstOfMap.add(rowMap);
        }
        resetCursor();
        System.out.println("allRowLstOfMap = " + allRowLstOfMap);
        return allRowLstOfMap;
    }

    /**
     *  Get first cell value at first row first column
     */
    public static String getFirstRowFirstColumnCellData(){
        return getCellValue(1,1);
    }
}
