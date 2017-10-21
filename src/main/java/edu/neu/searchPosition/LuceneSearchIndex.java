/**
 * 
 * @author Jyoti Sharma
 *
 */

package edu.neu.searchPosition;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import edu.neu.controller.ResultsPojo;
 
public class LuceneSearchIndex 
{
	// Stop Words
	public static List<String> stopWords = Arrays.asList("a", "a's", "able", "about", "above", "according", "accordingly", "across",
			"actually", "after", "afterwards", "again", "against", "ain't", "all", "allow", "allows", "almost", "alone",
			"along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any",
			"anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate",
			"appropriate", "are", "aren't", "around", "as", "aside", "ask", "asking", "associated", "at", "available",
			"away", "awfully", "b", "be", "became", "because", "become", "becomes", "becoming", "been", "before",
			"beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between",
			"beyond", "both", "brief", "but", "by", "c", "c'mon", "c's", "came", "can", "can't", "cannot", "cant",
			"cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning",
			"consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could",
			"couldn't", "course", "currently", "d", "definitely", "described", "despite", "did", "didn't", "different",
			"do", "does", "doesn't", "doing", "don't", "done", "down", "downwards", "during", "e", "each", "edu", "eg",
			"eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever",
			"every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "f",
			"far", "few", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly",
			"forth", "four", "from", "further", "furthermore", "g", "get", "gets", "getting", "given", "gives", "go",
			"goes", "going", "gone", "got", "gotten", "greetings", "h", "had", "hadn't", "happens", "hardly", "has",
			"hasn't", "have", "haven't", "having", "he", "he's", "hello", "help", "hence", "her", "here", "here's",
			"hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither",
			"hopefully", "how", "howbeit", "however", "i", "i'd", "i'll", "i'm", "i've", "ie", "if", "ignored",
			"immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar",
			"instead", "into", "inward", "is", "isn't", "it", "it'd", "it'll", "it's", "its", "itself", "j", "just",
			"k", "keep", "keeps", "kept", "know", "known", "knows", "l", "last", "lately", "later", "latter",
			"latterly", "least", "less", "lest", "let", "let's", "like", "liked", "likely", "little", "look", "looking",
			"looks", "ltd", "m", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more",
			"moreover", "most", "mostly", "much", "must", "my", "myself", "n", "name", "namely", "nd", "near", "nearly",
			"necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody",
			"non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "o", "obviously",
			"of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other",
			"others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "p",
			"particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably",
			"probably", "provides", "q", "que", "quite", "qv", "r", "rather", "rd", "re", "really", "reasonably",
			"regarding", "regardless", "regards", "relatively", "respectively", "right", "s", "said", "same", "saw",
			"say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems",
			"seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she",
			"should", "shouldn't", "since", "six", "so", "some", "somebody", "somehow", "someone", "something",
			"sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying",
			"still", "sub", "such", "sup", "sure", "t", "t's", "take", "taken", "tell", "tends", "th", "than", "thank",
			"thanks", "thanx", "that", "that's", "thats", "the", "their", "theirs", "them", "themselves", "then",
			"thence", "there", "there's", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon",
			"these", "they", "they'd", "they'll", "they're", "they've", "think", "third", "this", "thorough",
			"thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too",
			"took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "u", "un", "under",
			"unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses",
			"using", "usually", "uucp", "v", "value", "various", "very", "via", "viz", "vs", "w", "want", "wants",
			"was", "wasn't", "way", "we", "we'd", "we'll", "we're", "we've", "welcome", "well", "went", "were",
			"weren't", "what", "what's", "whatever", "when", "whence", "whenever", "where", "where's", "whereafter",
			"whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who",
			"who's", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without",
			"won't", "wonder", "would", "wouldn't", "x", "y", "yes", "yet", "you", "you'd", "you'll", "you're",
			"you've", "your", "yours", "yourself", "yourselves", "z", "zero");
	
	
	public static CharArraySet stopSet = new CharArraySet(stopWords, false);
	 
    public static ArrayList<ResultsPojo> getDoc(String args, ArrayList<ResultsPojo> resultsFetched) throws Exception 
    {
    	String INDEX_DIR = "D:\\Sem 5\\Program Structure and Algorithms\\FinalProject\\Document Search\\DocumentSearchUsingLucene (2)\\DocumentSearchUsingLucene\\indexedFiles";
    	
        //Get directory reference
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
         
        //Index reader - an interface for accessing a point-in-time view of a lucene index
        IndexReader reader = DirectoryReader.open(dir);
         
        //Create lucene searcher. It search over a single IndexReader.
        IndexSearcher searcher = new IndexSearcher(reader);
         
        //analyzer with the default stop words
        Analyzer analyzer = new StandardAnalyzer(stopSet);
         
        //Query parser to be used for creating TermQuery
        QueryParser qp = new QueryParser("contents", analyzer);
         
        //Create the query
        //Query query = qp.parse("cottage private discovery concluded");
        
        Query query = null;
        // Handling Conjunction, Disjunction and Negation
        if(args.toLowerCase().contains("not")) {
        	String[] split = args.toLowerCase().split("not");
        	Query query1 = qp.parse(split[0]);
        	Query query2 = qp.parse(split[1]);
        	query = new BooleanQuery.Builder().add(query1, BooleanClause.Occur.MUST)
    				.add(query2, BooleanClause.Occur.MUST_NOT).build();
        }else {
        	if(args.toLowerCase().contains("and")) {
            	String[] split = args.toLowerCase().split("and");
            	if(split.length>0){
            		Query query1 = qp.parse(split[0]);
                	Query query2 = qp.parse(split[1]);
                	query = new BooleanQuery.Builder().add(query1, BooleanClause.Occur.MUST)
            				.add(query2, BooleanClause.Occur.MUST).build();
            	}
            
            }else {
            	if(args.toLowerCase().contains("or")) {
                	String[] split = args.toLowerCase().split("or");
                	Query query1 = qp.parse(split[0]);
                	Query query2 = qp.parse(split[1]);
                	query = new BooleanQuery.Builder().add(query1, BooleanClause.Occur.SHOULD)
            				.add(query2, BooleanClause.Occur.SHOULD).build();
                }else {
                	query = qp.parse(args);
                }
            }
        }
                 
        //Search the lucene documents
        TopDocs hits = searcher.search(query, 10);
                  
        //Uses HTML &lt;B&gt;&lt;/B&gt; tag to highlight the searched terms
        Formatter formatter = new SimpleHTMLFormatter();
         
        //It scores text fragments by the number of unique query terms found
        //Basically the matching score in layman terms
        QueryScorer scorer = new QueryScorer(query);
         
        //used to markup highlighted terms found in the best sections of a text
        Highlighter highlighter = new Highlighter(formatter, scorer);
         
        //It breaks text up into same-size texts but does not split up spans
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 20);
         
        //breaks text up into same-size fragments with no concerns over spotting sentence boundaries.
        //Fragmenter fragmenter = new SimpleFragmenter(10);
         
        //set fragmenter to highlighter
        highlighter.setTextFragmenter(fragmenter);
        
        //Show hits for query
        System.out.println("hits for query: "+hits.totalHits);
         
        //Iterate over found results
        for (int i = 0; i < hits.scoreDocs.length; i++) 
        {
            int docid = hits.scoreDocs[i].doc;
            Document doc = searcher.doc(docid);
            String title = doc.get("path");
             
            //Printing - to which document result belongs
            System.out.println("Path " + " : " + title);
             
            //Get stored text from found document
            String text = doc.get("contents");
 
            //Create token stream
            TokenStream stream = TokenSources.getAnyTokenStream(reader, docid, "contents", analyzer);
             
            System.out.println("(" + hits.scoreDocs[i].score + ")");
            //i added
            ResultsPojo giveResult = new ResultsPojo();
            giveResult.setHits(hits.totalHits);
            giveResult.setScore(hits.scoreDocs[i].score);
            giveResult.setDocPath(doc.get("path"));
            
            //i added
            
            //Get highlighted text fragments
            String[] frags = highlighter.getBestFragments(stream, text, 30);
            giveResult.setHighlightedText(frags);
            for (String frag : frags) 
            {
                System.out.println("=======================");
                System.out.println(frag);
                
            }
            resultsFetched.add(giveResult);
        }
        dir.close();
        return resultsFetched;
    }
}