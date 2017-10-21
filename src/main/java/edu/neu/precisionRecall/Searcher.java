/**
 * 
 * @author Jyoti Sharma
 *
 */

package edu.neu.precisionRecall;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.parser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {

	public static final String CONTENTS="contents";
	public static final int MAX_SEARCH = 10;
	IndexSearcher indexSearcher;
	QueryParser queryParser;
	Query query;

	public Searcher(String indexDirectoryPath) throws IOException{
		
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
		IndexReader ir = DirectoryReader.open(indexDirectory);
		indexSearcher = new IndexSearcher(ir);	
		queryParser = new QueryParser(CONTENTS, new StandardAnalyzer());
		
	}

	public TopDocs search(String searchQuery) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException{
		
		query = queryParser.parse(searchQuery);
		
		return indexSearcher.search(query, MAX_SEARCH);
		
	}

	public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException{
		
		return indexSearcher.doc(scoreDoc.doc);
		
	}
}