package com.example.mongodb;

import com.example.mongodb.entity.Address;
import com.example.mongodb.entity.Gender;
import com.example.mongodb.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MongoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
			StudentRepository studentRepository,
			MongoTemplate mongoTemplate) {
        return args -> {
            var address = new Address(
                    "England",
                    "London",
                    "NE9");

            var email = "hugh@gmail.com";

            var student = new Student(
                    "Hugh",
                    "Lone",
                    email,
                    Gender.FEMALE,
                    address,
                    List.of("Compouter science", "Maths"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );

			// one of the ways to check if something is already is in db
            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(email));


			List<Student> students = mongoTemplate.find(query, Student.class);

			if (students.size() > 1) {
				throw new IllegalStateException(
						"found many students with email " + email);
			}

			if (students.isEmpty()) {
				System.out.println("Inserting student " + student);
				studentRepository.insert(student);
			} else {
				System.out.println(student + " already exists");
			}
        };
    }

}
