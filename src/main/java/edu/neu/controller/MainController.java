/**
 * 
 * @author Jyoti Sharma
 *
 */

package edu.neu.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.neu.searchPosition.LuceneSearchIndex;

@Controller
public class MainController {

	private static int counter = 0;
	private static final String VIEW_INDEX = "index";
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MainController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(ModelMap model , HttpServletRequest req) {
		model.addAttribute("message", "Welcome");
		model.addAttribute("counter", ++counter);

		HttpSession session = req.getSession();
		session.setAttribute("ErrorMessage", "");
		
		return VIEW_INDEX;

	}
	
	@RequestMapping(value = "/searchResults.htm", method = RequestMethod.GET)
	public String getSearchedWords(ModelMap model, HttpServletRequest req) throws Exception {

		ArrayList<ResultsPojo> searchResultList = new ArrayList<ResultsPojo>();		
		
		HttpSession session = req.getSession();
		session.setAttribute("foundResults", null);
		session.setAttribute("searctText", null);
		
		String requiredTextToSearch = req.getParameter("searchText");
		if(requiredTextToSearch!=null&&!requiredTextToSearch.trim().isEmpty()){
		
			System.out.println("Text to search for: "+req.getParameter("searchText"));
			
			ArrayList<ResultsPojo> documentList = LuceneSearchIndex.getDoc(requiredTextToSearch, searchResultList);
			session.setAttribute("searctText", req.getParameter("searchText"));
			session.setAttribute("ErrorMessage", "");
			if(documentList!=null && documentList.size() >0){
				session.setAttribute("foundResults", documentList);
				return VIEW_INDEX;
			}else{
				session.setAttribute("ErrorMessage", "There was no matching result found");
				return VIEW_INDEX;
			}
		}else{
			session.setAttribute("ErrorMessage", "There was no matching result found");
			return VIEW_INDEX;
			
		}
	}

}