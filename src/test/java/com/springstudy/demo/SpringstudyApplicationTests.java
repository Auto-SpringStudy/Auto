package com.springstudy.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringstudyApplicationTests {

    @Test
    void connectTest(){
        String url = "jdbc:mysql://localhost:3306/spring_study";
        String username = "sa";
        String password = "sa";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            if (conn != null) {
                System.out.println("✅ 데이터베이스 연결 성공!");
            } else {
                System.out.println("❌ 데이터베이스 연결 실패!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
