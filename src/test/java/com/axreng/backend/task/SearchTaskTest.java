package com.axreng.backend.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SearchTaskTest {

	@Mock
	private Document documentMock;

	@Mock
	private Elements elementsMock;

	@Mock
	private Element elementMock;

	@InjectMocks
	private SearchTask searchTask;

	private final String id = "1";
	private final String keyword = "Lorem";
	private final String baseUrl = "https://br.lipsum.com/";

	@BeforeEach
	void setUp() {
		searchTask = new SearchTask(id, keyword, baseUrl);
	}

	@Test
	void testSearchTaskInitialization() {
		assertEquals(id, searchTask.getId());
		assertEquals("active", searchTask.getStatus());
		assertTrue(searchTask.getUrls().isEmpty());
	}

	@Test
	void testCrawlFindsKeyword() throws IOException {
		searchTask.run();
		assertEquals("done", searchTask.getStatus());
	}
}
