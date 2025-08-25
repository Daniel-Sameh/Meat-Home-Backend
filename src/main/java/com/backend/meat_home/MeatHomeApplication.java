package com.backend.meat_home;

import com.backend.meat_home.entity.User;
import com.backend.meat_home.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MeatHomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeatHomeApplication.class, args);
	}

    //Hardcoded Admin may be removed later
    @Bean
    CommandLineRunner init(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin_user_name").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin_user_name");
                admin.setName("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(User.Role.ADMIN);
                admin.setStatus(User.Status.ACTIVE);
                admin.setEmail("admin@example.com");
                repo.save(admin);
            }
        };
    }
}
