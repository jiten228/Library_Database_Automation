package com.library.step_definitions;

import com.library.utilities.ConfigurationReader;
import com.library.utilities.DB_Utility;

public class Connection {
    public static void main(String[] args) {
        String url = ConfigurationReader.getProperty("library1.database.url");
        String username = ConfigurationReader.getProperty("library1.database.username");
        String password = ConfigurationReader.getProperty("library1.database.password");

        DB_Utility.createConnection(url,username,password);
        DB_Utility.runQuery("Select count(*) from books");
        String booksDb = DB_Utility.getFirstRowFirstColumnCellData();
    }
}
