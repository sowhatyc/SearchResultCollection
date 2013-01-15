/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

/**
 *
 * @author admin
 */
public class SearchDetail{
    private String searchUrl = null;
    private String searchDescription = null;

    public SearchDetail(String searchUrl, String searchDescription){
        this.searchUrl = searchUrl;
        this.searchDescription = searchDescription;
    }

    /**
        * @return the searchUrl
        */
    public String getSearchUrl() {
        return searchUrl;
    }

    /**
        * @return the searchDescription
        */
    public String getSearchDescription() {
        return searchDescription;
    }
        
        
        
}
