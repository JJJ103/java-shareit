package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShareItApp.class, properties = "spring.profiles.active=test")
class ShareItTests {

	@Test
	void contextLoads() {
	}

}
