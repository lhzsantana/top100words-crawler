package com.wordscrawler.indexer;

public interface Indexer {

	public static String PAGE = "page";
	public static String WORD = "word";

	public void index(String type, String document);
	
	public void search(String term);

}
