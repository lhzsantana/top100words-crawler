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
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Component;

@Component
public class CounterImpl implements Counter {

	private IndexWriterConfig config;
	private RAMDirectory index;

	public CounterImpl() throws IOException {

		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
	    index = new RAMDirectory();	    
	}

	@Override
	public void index(String document, String url) throws IOException {

		IndexWriter w = new IndexWriter(index, config);
	    
		Document doc = new Document();
		doc.add(new TextField(url, document, Field.Store.YES));
		w.addDocument(doc);
		
		w.commit();
		w.close();
	}

	@Override
	public List<String> getTopWords() throws Exception {

		IndexReader r = DirectoryReader.open(index);
		TermStats[] commonTerms = HighFreqTerms.getHighFreqTerms(r, 30, "mytextfield");

		List<String> topTerms = new ArrayList<String>();

		for (TermStats commonTerm : commonTerms) {
			topTerms.add(commonTerm.termtext.utf8ToString());
		}

		r.close();
		
		return topTerms;
	}
}
