package com.wordscrawler.counter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class Counter {

	private IndexWriter w;
	private IndexReader r;
	private Directory index = new RAMDirectory();

	public Counter() throws IOException {

		// Specify the analyzer for tokenizing text.
		// The same analyzer should be used for indexing and searching
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

		// Create the index

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);

		w = new IndexWriter(index, config);
		r = DirectoryReader.open(index);
	}

	public void index(String document, String url) throws IOException {

		Document doc = new Document();
		doc.add(new TextField(url, document, Field.Store.YES));
		w.addDocument(doc);

	}

	public List<String> getTopWords() throws Exception {

		TermStats[] commonTerms = HighFreqTerms.getHighFreqTerms(r, 30, "mytextfield");

		List<String> topTerms = new ArrayList<String>();

		for (TermStats commonTerm : commonTerms) {
			topTerms.add(commonTerm.termtext.utf8ToString());
		}

		return topTerms;
	}
}
