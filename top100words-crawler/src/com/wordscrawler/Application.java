package com.wordscrawler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.wordscrawler.counter.Counter;
import com.wordscrawler.counter.impl.CounterImpl;
import com.wordscrawler.indexer.Indexer;
import com.wordscrawler.indexer.impl.IndexerImpl;

@SpringBootApplication
@EnableScheduling
@ComponentScan 
public class Application {
	
	public static Counter counter = new CounterImpl();
	public static Indexer indexer = new IndexerImpl();

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);
    }
}