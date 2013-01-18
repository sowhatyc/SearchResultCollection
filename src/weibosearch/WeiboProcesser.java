/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weibosearch;

import CommonTools.FileStorage;
import CommonTools.StaticHelper;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import weibo4j.Comments;
import weibo4j.Timeline;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

/**
 *
 * @author admin
 */
public class WeiboProcesser {
    
    public static boolean getUserTimeLineByNameLimitedByDate(String name, int page, Date limitedDate, String accessToken, ArrayList<SearchResult> searchResultList) throws WeiboException{
        Timeline userTimeline = new Timeline();
        userTimeline.setToken(accessToken);
        StatusWapper sw = userTimeline.getUserTimelineByName(name, new Paging(page));
        for(Status s : sw.getStatuses()){
            if(s.getCreatedAt().getTime() < limitedDate.getTime()){
                return false;
            }
            SearchResult sr = new SearchResult();
            sr.setMid(s.getMid());
            sr.setDate(s.getCreatedAt());
            sr.setRepostsCount(s.getRepostsCount());
            sr.setCommentsCount(s.getCommentsCount());
            sr.setText(s.getText());
            sr.setUserID(s.getUser().getId());
            sr.setUserName(s.getUser().getScreenName());
            Status retweed = s.getRetweetedStatus();
            if(retweed != null && retweed.getUser() != null){
                sr.setRrcID(retweed.getMid());
                sr.setRrcDate(retweed.getCreatedAt());
                sr.setRrcText(retweed.getText());
                sr.setRrcName(retweed.getUser().getScreenName());
            }else{
                sr.setRrcID(null);
                sr.setRrcDate(null);
                sr.setRrcText(null);
                sr.setRrcName(null);
            }
            searchResultList.add(sr);
        }
        return true;
    }
    
    
    public static boolean getCommentsById(String id, int page, String accessToken, ArrayList<SearchResult> searchResultList) throws WeiboException{
        Comments cmts = new Comments();
        cmts.setToken(accessToken);
        CommentWapper sw = cmts.getCommentById(id, new Paging(page));
        if(sw.getComments().size() == 0){
            return false;
        }
        for(Comment cmt : sw.getComments()){
            SearchResult sr = new SearchResult();
            sr.setMid(cmt.getMid());
            sr.setDate(cmt.getCreatedAt());
            sr.setText(cmt.getText());
            sr.setUserID(cmt.getUser().getId());
            sr.setUserName(cmt.getUser().getScreenName());
            Comment replyComment = cmt.getReplycomment();
            if(replyComment != null && replyComment.getUser() != null){
                sr.setRrcID(replyComment.getMid());
                sr.setRrcDate(replyComment.getCreatedAt());
                sr.setRrcName(replyComment.getUser().getScreenName());
            }else{
                sr.setRrcID(null);
                sr.setRrcDate(null);
                sr.setRrcName(null);
            }
            searchResultList.add(sr);
        }
        return true;
    }
    
    
    public static void getUserWeiboByDate(String userName, String date, int level, int page){
        StaticHelper.initialTokenList();
        StaticHelper.FILE_PATH = null;
        int tokenListSize = StaticHelper.TOKEN_LIST.size();
        int sleepTime = 60 * 1000;
        int tokenTime = 100;
        if(level == StaticHelper.access_level.test_level.ordinal()){
            sleepTime = 30 * 1000;
            tokenTime = 120;
        }else if(level == StaticHelper.access_level.general_level.ordinal()){
            sleepTime = 5 * 1000;
            tokenTime = 720;
        }
        Date limitedDate = null;
        try {
            limitedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("your date format is not correct!");
            return;
        }
        ArrayList<SearchResult> searchResultList = new ArrayList<SearchResult>();
        int leftTime = tokenTime;
        int tokenNumber = new Random(System.currentTimeMillis()).nextInt(tokenListSize);
        String accessToken = StaticHelper.TOKEN_LIST.get(tokenNumber);
        try {
            while(getUserTimeLineByNameLimitedByDate(userName, page, limitedDate, accessToken, searchResultList)){
                try {
                    if(StaticHelper.FILE_PATH == null){
                        FileStorage.saveWeiboSearchResultExcel(StaticHelper.FILE_DIRECTORY + userName + "微博_1.xls", searchResultList);
                    }else{
                        FileStorage.saveWeiboSearchResultExcel(StaticHelper.FILE_PATH, searchResultList);
                    }
                }catch (IOException ex) {
                    Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BiffException ex) {
                    Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (WriteException ex) {
                    Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
                }
                StaticHelper.START_ROW += searchResultList.size();
                --leftTime;
                if(leftTime == 0){
                    ++tokenNumber;
                    if(tokenNumber == tokenListSize){
                        tokenNumber = 0;
                    }
                    accessToken = StaticHelper.TOKEN_LIST.get(tokenNumber);
                }
                System.out.println("page " + page + " is done, excel updated done! *******  acessToken = " + accessToken);
                searchResultList.clear();
                page += 1;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        } catch (WeiboException ex) {
            Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("***********  something wrong happened in getUserTimeLineByNameLimitedByDate  *************");
            try {
                Thread.sleep(sleepTime * 3);
            } catch (InterruptedException ex1) {
                Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex1);
            }
            getUserWeiboByDate(userName, date, level, page);
        }
        try{
            if(StaticHelper.FILE_PATH == null){
                FileStorage.saveWeiboSearchResultExcel(StaticHelper.FILE_DIRECTORY + userName + "微博_1.xls", searchResultList);
            }else{
                FileStorage.saveWeiboSearchResultExcel(StaticHelper.FILE_PATH, searchResultList);
            }
            System.out.println("page " + page + " is done, excel updated done! *******  acessToken = " + accessToken);
        }catch (IOException ex) {
            Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException ex) {
            Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
        }
        StaticHelper.START_ROW += searchResultList.size();
    }
    
    
    public static void getCommentsById(String id, int level, int page){
        StaticHelper.initialTokenList();
        StaticHelper.FILE_PATH = null;
        int tokenListSize = StaticHelper.TOKEN_LIST.size();
        int sleepTime = 60 * 1000;
        int tokenTime = 100;
        if(level == StaticHelper.access_level.test_level.ordinal()){
            sleepTime = 30 * 1000;
            tokenTime = 120;
        }else if(level == StaticHelper.access_level.general_level.ordinal()){
            sleepTime = 5 * 1000;
            tokenTime = 720;
        }
        ArrayList<SearchResult> searchResultList = new ArrayList<SearchResult>();
        int leftTime = tokenTime;
        int tokenNumber = new Random(System.currentTimeMillis()).nextInt(tokenListSize);
        String accessToken = StaticHelper.TOKEN_LIST.get(tokenNumber);
        try {
            while(getCommentsById(id, page, accessToken, searchResultList)){
                try {
                    if(StaticHelper.FILE_PATH == null){
                        FileStorage.saveWeiboSearchResultExcel(StaticHelper.FILE_DIRECTORY + id + "_评论_1.xls", searchResultList);
                    }else{
                        FileStorage.saveWeiboSearchResultExcel(StaticHelper.FILE_PATH, searchResultList);
                    }
                }catch (IOException ex) {
                    Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BiffException ex) {
                    Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (WriteException ex) {
                    Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
                }
                StaticHelper.START_ROW += searchResultList.size();
                --leftTime;
                if(leftTime == 0){
                    ++tokenNumber;
                    if(tokenNumber == tokenListSize){
                        tokenNumber = 0;
                    }
                    accessToken = StaticHelper.TOKEN_LIST.get(tokenNumber);
                }
                System.out.println("page " + page + " is done, excel updated done! *******  acessToken = " + accessToken);
                searchResultList.clear();
                page += 1;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        } catch (WeiboException ex) {
            Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("***********  something wrong happened in getCommentsById  *************");
            try {
                Thread.sleep(sleepTime * 3);
            } catch (InterruptedException ex1) {
                Logger.getLogger(WeiboProcesser.class.getName()).log(Level.SEVERE, null, ex1);
            }
            getCommentsById(id, level, page);
        }
        StaticHelper.START_ROW += searchResultList.size();
    }
}
