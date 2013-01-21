/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weibosearch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author admin
 */
public class SearchResult {
    
    private String userID;
    private String userName;
    private String text;
    private String mid;
    private int repostsCount;
    private int commentsCount;
    private Date date;
    private String rrcID;
    private String rrcName;
    private String rrcText;
    private Date rrcDate;
    private Map<Integer, Object> attributes = new HashMap<Integer, Object>();
    private int nextNumber = 0;
    /**
     * @return the userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(String userID, int position) {
        this.userID = userID;
        attributes.put(position, this.userID);
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName, int position) {
        this.userName = userName;
        attributes.put(position, this.userName);
    }


    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text, int position) {
        this.text = text;
        attributes.put(position, this.text);
    }

    /**
     * @return the mid
     */
    public String getMid() {
        return mid;
    }

    /**
     * @param mid the mid to set
     */
    public void setMid(String mid, int position) {
        this.mid = mid;
        attributes.put(position, this.mid);
    }

    /**
     * @return the repostsCount
     */
    public int getRepostsCount() {
        return repostsCount;
    }

    /**
     * @param repostsCount the repostsCount to set
     */
    public void setRepostsCount(int repostsCount, int position) {
        this.repostsCount = repostsCount;
        attributes.put(position, this.repostsCount);
    }

    /**
     * @return the commentsCount
     */
    public int getCommentsCount() {
        return commentsCount;
    }

    /**
     * @param commentsCount the commentsCount to set
     */
    public void setCommentsCount(int commentsCount, int position) {
        this.commentsCount = commentsCount;
        attributes.put(position, this.commentsCount);
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date, int position) {
        this.date = date;
        attributes.put(position, this.date);
    }

    /**
     * @return the retweetedID
     */
    public String getRrcID() {
        return rrcID;
    }

    /**
     * @param retweetedID the retweetedID to set
     */
    public void setRrcID(String retweetedID, int position) {
        this.rrcID = retweetedID;
        attributes.put(position, this.rrcID);
    }

    /**
     * @return the retweetedText
     */
    public String getRrcText() {
        return rrcText;
    }

    /**
     * @param retweetedText the retweetedText to set
     */
    public void setRrcText(String retweetedText, int position) {
        this.rrcText = retweetedText;
        attributes.put(position, this.rrcText);
    }

    /**
     * @return the retweetedDate
     */
    public Date getRrcDate() {
        return rrcDate;
    }

    /**
     * @param retweetedDate the retweetedDate to set
     */
    public void setRrcDate(Date retweetedDate, int position) {
        this.rrcDate = retweetedDate;
        attributes.put(position, this.rrcDate);
    }

    /**
     * @return the retweetedName
     */
    public String getRrcName() {
        return rrcName;
    }

    /**
     * @param retweetedName the retweetedName to set
     */
    public void setRrcName(String retweetedName, int position) {
        this.rrcName = retweetedName;
        attributes.put(position, this.rrcName);
    }
    
    public Map<Integer, Object> getAttributes(){
        return attributes;
    }
}
