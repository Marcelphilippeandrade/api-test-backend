package com.axreng.backend;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.Map;

import com.axreng.backend.dto.SearchRequestDTO;
import com.axreng.backend.dto.SearchResponseDTO;
import com.axreng.backend.service.SearchService;
import com.google.gson.Gson;

public class WebCrawlerApp {
	private static final Gson gson = new Gson();
	private static final SearchService searchService = new SearchService();

	public static void main(String[] args) {
		port(4567);
		configureRoutes();
	}

	private static void configureRoutes() {
		post("/crawl", (req, res) -> {
			SearchRequestDTO request = gson.fromJson(req.body(), SearchRequestDTO.class);
			SearchResponseDTO response = searchService.startSearch(request);

			res.status(response.getStatusCode());
			return gson.toJson(Map.of("id", response.getId()));
		});

		get("/crawl/:id", (req, res) -> {
			String id = req.params("id");
			SearchResponseDTO response = searchService.getSearchResult(id);

			res.status(response.getStatusCode());
			return gson.toJson(response);
		});
	}
}
