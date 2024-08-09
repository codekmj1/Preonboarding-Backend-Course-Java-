package com.example.onboarding;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/onboarding", "root1", "1234");
            System.out.println("연결 성공!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}