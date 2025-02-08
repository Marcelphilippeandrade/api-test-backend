package com.axreng.backend.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SearchTask implements Runnable {

	private final String id;
	private final String keyword;
	private final String baseUrl;
	private final Set<String> foundUrls = ConcurrentHashMap.newKeySet();
	private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
	private volatile String status = "active";

	public SearchTask(String id, String keyword, String baseUrl) {
		this.id = id;
		this.keyword = keyword;
		this.baseUrl = baseUrl;
	}

	@Override
	public void run() {
		long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
		crawl(baseUrl);
		status = "done";
		
		long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = (memoryAfter - memoryBefore) / (1024 * 1024);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        
        System.out.println("⏳ Tempo de execução: " + duration + " ms");
        System.out.println("💾 Memória utilizada: " + memoryUsed + " MB");
	}

	private void crawl(String startUrl) {
		Queue<String> queue = new LinkedList<>();
		queue.add(startUrl);
		visitedUrls.add(startUrl);

		while (!queue.isEmpty()) {
			String url = queue.poll();
			try {
				Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(5000).get();

				if (doc.toString().toLowerCase().contains(keyword)) {
					foundUrls.add(url);
				}

				Elements links = doc.select("a[href]");
				for (Element link : links) {
					String absUrl = link.absUrl("href");
					if (!visitedUrls.contains(absUrl) && absUrl.startsWith(baseUrl)) {
						visitedUrls.add(absUrl);
						queue.add(absUrl);
					}
				}
			} catch (IOException ignored) {
			}
		}
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public List<String> getUrls() {
		return new ArrayList<>(foundUrls);
	}
}
