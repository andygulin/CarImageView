package nginx.mongo.photo.view;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import nginx.mongo.photo.view.config.MongoConfig;

@SpringBootApplication
@Import(MongoConfig.class)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}