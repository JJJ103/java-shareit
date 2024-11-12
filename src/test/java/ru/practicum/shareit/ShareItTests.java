package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ShareItApp.class, properties = "spring.profiles.active=test")
class ShareItTests {

	@Test
	void contextLoads() {
	}

}
