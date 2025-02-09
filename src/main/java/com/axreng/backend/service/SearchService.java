package com.axreng.backend.service;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.axreng.backend.dto.SearchRequestDTO;
import com.axreng.backend.dto.SearchResponseDTO;
import com.axreng.backend.task.SearchTask;

public class SearchService {
	private static final String BASE_URL = System.getenv("BASE_URL");
	private static final ExecutorService executor = Executors.newFixedThreadPool(10);
	private final Map<String, SearchTask> searches = new ConcurrentHashMap<>();

	public SearchResponseDTO startSearch(SearchRequestDTO request) {
		if (request.getKeyword() == null || request.getKeyword().length() < 4 || request.getKeyword().length() > 32) {
			return new SearchResponseDTO(null, "Error. A palavra-chave deve ter entre 4 e 32 caracteres.", Collections.emptyList(), 400);
		}

		String id = UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 8);
		SearchTask task = new SearchTask(id, request.getKeyword().toLowerCase(), BASE_URL);
		searches.put(id, task);
		executor.submit(task);

		return new SearchResponseDTO(id, "active", Collections.emptyList(), 200);
	}

	public SearchResponseDTO getSearchResult(String id) {
		SearchTask task = searches.get(id);
		if (task == null) {
			return new SearchResponseDTO(null, "Error. Pesquisa não encontrada.", Collections.emptyList(), 404);
		}
		return new SearchResponseDTO(task.getId(), task.getStatus(), task.getUrls(), 200);
	}
}
