package com.axreng.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static spark.Spark.awaitInitialization;
import static spark.Spark.awaitStop;
import static spark.Spark.port;
import static spark.Spark.stop;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axreng.backend.dto.SearchRequestDTO;
import com.axreng.backend.dto.SearchResponseDTO;
import com.google.gson.Gson;

class WebCrawlerAppTest {
    private static final Gson gson = new Gson();
    private static String baseUrl;

    @BeforeAll
    static void setUpBeforeClass() {
        port(0); // Porta aleatória
    }

    @BeforeEach
    void setUp() {
        WebCrawlerApp.main(new String[]{}); // Iniciar a aplicação
        awaitInitialization(); // Esperar inicialização dos endpoints
        baseUrl = "http://localhost:4567"; // Ajuste conforme necessário
    }

    @AfterEach
    void tearDown() {
        stop();
        awaitStop(); // Esperar a parada do servidor
    }

    @Test
    void testPostCrawlEndpoint() throws Exception {
        SearchRequestDTO searchRequest = new SearchRequestDTO();
        searchRequest.setKeyword("Lorem");
        SearchResponseDTO expectedResponse = new SearchResponseDTO("123", "active", Arrays.asList(""), 200);

        // Criar conexão HTTP
        URL url = new URL(baseUrl + "/crawl");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = gson.toJson(searchRequest).getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Verificar resposta
        assertEquals(200, conn.getResponseCode());
        try (Scanner scanner = new Scanner(conn.getInputStream(), "utf-8")) {
            String responseJson = scanner.useDelimiter("\\A").next();
            SearchResponseDTO actualResponse = gson.fromJson(responseJson, SearchResponseDTO.class);
            assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        }
    }
}
