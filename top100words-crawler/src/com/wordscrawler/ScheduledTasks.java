package com.wordscrawler;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wordscrawler.crawler.MyCrawler;
import com.wordscrawler.indexer.Indexer;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Component
public class ScheduledTasks {

	final static Logger logger = Logger.getLogger(ScheduledTasks.class);

	@Scheduled(fixedRate = 50000000)
	public void reportCurrentTime() throws Exception {

		logger.info("Starting crawler");

		String crawlStorageFolder = "/data/crawl/root";
		int numberOfCrawlers = 7;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);

		/*
		 * Be polite: Make sure that we don't send more than 1 request per
		 * second (1000 milliseconds between requests).
		 */
		config.setPolitenessDelay(1000);

		/*
		 * You can set the maximum crawl depth here. The default value is -1 for
		 * unlimited depth
		 */
		config.setMaxDepthOfCrawling(2);

		/*
		 * You can set the maximum number of pages to crawl. The default value
		 * is -1 for unlimited number of pages
		 */
		config.setMaxPagesToFetch(1000);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

		CrawlController controllerBrazil = new CrawlController(config, pageFetcher, robotstxtServer);
		controllerBrazil.addSeed("http://www.globo.com/");
		controllerBrazil.start(MyCrawler.class, numberOfCrawlers);

		CrawlController controllerDeutschland = new CrawlController(config, pageFetcher, robotstxtServer);
		controllerDeutschland.addSeed("http://www.spiegel.de/");
		controllerDeutschland.start(MyCrawler.class, numberOfCrawlers);

		CrawlController controllerFrance = new CrawlController(config, pageFetcher, robotstxtServer);
		controllerFrance.addSeed("http://www.lemonde.fr/");
		controllerFrance.start(MyCrawler.class, numberOfCrawlers);

		CrawlController controllerEspana = new CrawlController(config, pageFetcher, robotstxtServer);
		controllerEspana.addSeed("http://www.lanacion.com.ar/");
		controllerEspana.start(MyCrawler.class, numberOfCrawlers);

		for (String topWord : Application.counter.getTopWords()) {
			
			Application.indexer.index(Indexer.WORD, "");
			
			logger.info(topWord);
		}
	}
}