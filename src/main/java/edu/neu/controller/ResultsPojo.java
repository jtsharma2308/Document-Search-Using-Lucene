/**
 * 
 * @author Jyoti Sharma
 *
 */

package edu.neu.controller;

public class ResultsPojo {

	private double score;
	private String[] highlightedText;
	private String docPath;
	private long hits;
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	public String[] getHighlightedText() {
		return highlightedText;
	}
	public void setHighlightedText(String[] highlightedText) {
		this.highlightedText = highlightedText;
	}
	public String getDocPath() {
		return docPath;
	}
	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}
	public long getHits() {
		return hits;
	}
	public void setHits(long hits) {
		this.hits = hits;
	}
	
	
	
}
