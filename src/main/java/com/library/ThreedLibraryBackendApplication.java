// src/main/java/com/library/ThreedLibraryBackendApplication.java

package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThreedLibraryBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThreedLibraryBackendApplication.class, args);
		// 💡 サーバー起動メッセージ
        System.out.println("3D Library Backend Application is running on port 8080...");
	}

}
