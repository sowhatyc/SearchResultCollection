/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

/**
 *
 * @author admin
 */
public class SearchResult {
    
    private String url = null;
    private String postTime = null;
    private String replyCount = null;
    private String visitCount = null;
    private String description = null;

    /**
     * @return the url
     */
    
    public SearchResult(){
        this.url = null;
        this.replyCount = null;
        this.postTime = null;
        this.visitCount = null;
        
    }
    
    public SearchResult(String url, String postTime, String replyCount, String visitCount, String description){
        this.url = url;
        this.postTime = postTime;
        this.replyCount = replyCount;
        this.visitCount = visitCount;
        this.description = description;
    }
    
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the postTime
     */
    public String getPostTime() {
        return postTime;
    }

    /**
     * @param postTime the postTime to set
     */
    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

   
    
    public boolean isNotFullResult(){
        if(url == null || visitCount == null || replyCount == null || postTime == null){
            return true;
        }
        return false;
    }

    /**
     * @return the replyCount
     */
    public String getReplyCount() {
        return replyCount;
    }

    /**
     * @param replyCount the replyCount to set
     */
    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    /**
     * @return the visitCount
     */
    public String getVisitCount() {
        return visitCount;
    }

    /**
     * @param visitCount the visitCount to set
     */
    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void makeFullResult(){
        if(this.url == null){
            this.url = "null";
        }
        if(this.postTime == null){
            this.postTime = "0000-00-00 00:00";
        }
        if(this.description == null){
            this.description = "null";
        }
        if(this.replyCount == null){
            this.replyCount = "0";
        }
        if(this.visitCount == null){
            this.visitCount = "0";
        }
    }
    
}
