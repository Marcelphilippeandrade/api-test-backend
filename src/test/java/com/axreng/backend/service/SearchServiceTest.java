package com.axreng.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.ExecutorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.axreng.backend.dto.SearchRequestDTO;
import com.axreng.backend.dto.SearchResponseDTO;
import com.axreng.backend.task.SearchTask;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

	@InjectMocks
	private SearchService searchService;

	@Mock
	private SearchTask searchTask;

	@Mock
	private ExecutorService executorService;


	@BeforeEach
	void setUp() {
	}

	@Test
	void testStartSearch_InvalidKeyword() {
		SearchRequestDTO request = new SearchRequestDTO();
		request.setKeyword("abc");

		SearchResponseDTO response = searchService.startSearch(request);

		assertEquals(400, response.getStatusCode());
		assertEquals("Error. A palavra-chave deve ter entre 4 e 32 caracteres.", response.getStatus());
	}

	@Test
	void testStartSearch_ValidKeyword() {
		SearchRequestDTO request = new SearchRequestDTO();
		request.setKeyword("testKeyword");

		SearchResponseDTO response = searchService.startSearch(request);

		assertNotNull(response.getId());
		assertEquals(200, response.getStatusCode());
		assertEquals("active", response.getStatus());
	}

	@Test
	void testGetSearchResult_NotFound() {
		SearchResponseDTO response = searchService.getSearchResult("invalidID");

		assertEquals(404, response.getStatusCode());
		assertEquals("Error. Pesquisa não encontrada.", response.getStatus());
	}
}
