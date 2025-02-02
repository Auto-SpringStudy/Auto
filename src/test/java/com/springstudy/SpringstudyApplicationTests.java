package com.springstudy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
class SpringstudyApplicationTests {
    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String username = "sa";

    @Value("${spring.datasource.password}")
    String password = "sa";

    @Test
    void connectTest(){
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
