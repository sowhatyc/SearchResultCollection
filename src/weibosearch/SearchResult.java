/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weibosearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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
    private ArrayList<Object> attributes = new ArrayList<Object>();
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
    public void setUserID(String userID) {
        this.userID = userID;
        attributes.add(this.userID);
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
    public void setUserName(String userName) {
        this.userName = userName;
        attributes.add(this.userName);
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
    public void setText(String text) {
        this.text = text;
        attributes.add(this.text);
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
    public void setMid(String mid) {
        this.mid = mid;
        attributes.add(this.mid);
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
    public void setRepostsCount(int repostsCount) {
        this.repostsCount = repostsCount;
        attributes.add(this.repostsCount);
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
    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
        attributes.add(this.commentsCount);
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
    public void setDate(Date date) {
        this.date = date;
        attributes.add(this.date);
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
    public void setRrcID(String retweetedID) {
        this.rrcID = retweetedID;
        attributes.add(this.rrcID);
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
    public void setRrcText(String retweetedText) {
        this.rrcText = retweetedText;
        attributes.add(this.rrcText);
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
    public void setRrcDate(Date retweetedDate) {
        this.rrcDate = retweetedDate;
        attributes.add(this.rrcDate);
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
    public void setRrcName(String retweetedName) {
        this.rrcName = retweetedName;
        attributes.add(this.rrcName);
    }
    
    public Object getNext(){
        if(nextNumber < this.attributes.size()){
            ++nextNumber;
            return attributes.get(nextNumber-1);
        }else{
            return null;
        }
    }
}
