package com.wordscrawler.counter;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

public interface Counter {

	public void index(String document, String url) throws IOException;

	public List<String> getTopWords() throws Exception;
}
