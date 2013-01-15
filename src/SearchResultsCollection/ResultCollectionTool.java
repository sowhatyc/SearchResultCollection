/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

import CommonTools.CTs;
import CommonTools.FileStorage;
import CommonTools.StaticHelper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

/**
 *
 * @author admin
 */
public class ResultCollectionTool {
    
    private boolean toFindSearchUrl = false;
    private String urlDescription = null;
    private boolean rankByReply = false;
    private boolean rankByHot = false;
    private int toRankCount = 0;
    private ArrayList<SearchResult> resultList = null;
    private Set<String> dealedUrl = new HashSet<String>();
//    boolean newCrawlerOpen = true;
    
    public void rankByReplyCount(){
        this.rankByReply = true;
        this.rankByHot = false;
    }
    
    public void rankByHot(){
        this.rankByHot = true;
        this.rankByReply = false;
    }
    
    public ArrayList<SearchResult> getSearchResult(){
        return resultList;
    }
    
    public void initialDealedUrl(String filePath){
        if(resultList == null){
            readSearchResult(filePath);
        }
        for(SearchResult result : resultList){
            dealedUrl.add(result.getUrl());
        }
        System.out.println("resultList size : " + resultList.size());
    }
    
    public void getRankResult(int dealedRankCount){
        if(resultList == null || resultList.isEmpty()){
            CTs.priorPrint("result list is empty", 1);
        }
        if(rankByHot == false && rankByReply == false){
            rankByReply = true;
        }
//        if(toRankCount > resultList.size()){
//            toRankCount = resultList.size();
//        }
        if(rankByHot){
            rankResultListByHot();
            getRankResultByHot();
        }else if(rankByReply){
            rankResultListByReply();
            getRankResultByReply(dealedRankCount);
        }
        
//        for(int i=0; i<toRankCount; ++i){
//            //multi thread here
//            savePage(resultList.get(i).getUrl());
//        }
    }
    
    private void getRankResultByHot(){
        
    }
    
    private void getRankResultByReply(int dealedRankCount){
        int currentRankedCount = -1;
        for(SearchResult result : resultList){
            currentRankedCount++;
            if(Integer.valueOf(result.getReplyCount()) < toRankCount){
//                savePage(result.getUrl());
                System.out.println("all need ranked results have been done!!!");
                break;
            }
            if(currentRankedCount < dealedRankCount){
                continue;
            }
            System.out.println("get url = " + result.getUrl() + " replyCount = " + result.getReplyCount());
            System.out.println("doing " + (currentRankedCount+1) + " ranked result!!!!");
            savePage(result.getUrl(), 0);
        }
    }
    
    public void savePage(String url, int startPageNum){
        Page page = new Page(url);
        page.setNextPage(page.getPageUrl());
        String content = null ;
        int pageNum = startPageNum;
        ArrayList<Item> itemList = new ArrayList<Item>();
        while(true){
            CTs.priorPrint("doing page " + pageNum + " *********************************************");
            if(page.getPageContent() != null){
                page.setNextPage(AnalysisFunction.getNextPage(page));
//                    System.out.println("下一页分析完成");
            }
            if(page.getNextPage() == null){
               break;
            }
            content= WebCrawler.getPageContentByNekoHtml(page.getNextPage());
            if(content == null){
                System.err.println("logging info");
                StaticHelper.logInfo("doing pageNum = " + pageNum + "!! get page content failed!!!  url=" + page.getPageUrl() + "\r\n");
                break;
            }
//                    AdaptiveWebObjectExtration.crawler_Index = -1;
//                    Thread.interrupted();   
//                    Logger.getLogger(NextPageThread.class.getName()).log(Level.SEVERE, null, ex);
            int errorCount = 0;    
            while(content.length()<50){
                try {
                    System.err.println("抓取异常，需要重新抓取");
//                    System.out.println(content);
                    Thread.sleep(5000);
                    errorCount++;
//                    ThreadErrorNum++;

                } catch (InterruptedException ex) {
                    Logger.getLogger(ResultCollectionTool.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(errorCount > 4){
                    System.err.println("下一页抽取内容不足，第二页方法分析结束");
                    System.err.println("logging info");
                    StaticHelper.logInfo("doing pageNum = " + pageNum + "!! get page content failed!!!  url=" + page.getPageUrl() + "\r\n");
                    break;
                }
                if(errorCount > 3){
                    content = WebCrawler.getPageContent(page.getNextPage());
                    continue;
                }
                
               
                content = WebCrawler.getPageContentByNekoHtml(page.getNextPage());
                if(content == null){
                    System.err.println("logging info");
                    StaticHelper.logInfo("doing pageNum = " + pageNum + "!! get page content failed!!!  url=" + page.getPageUrl() + "\r\n");
                }
            }
            if(errorCount > 4){
                    break;
            }
            PageFilter.addMethod(StaticHelper.PAGERUBBISHTAG);
            page.setPageContent(PageFilter.cleanPage(content));
            ArrayList<Item> newItems = AnalysisFunction.getItemWithElement(page);
            if(newItems != null){
                itemList.addAll(newItems);
                if(itemList.size() > 5000){
                    saveItemResult(itemList, url);
                    itemList.clear();
                }
            }
            pageNum++;
        }
        saveItemResult(itemList, url);
    }
    
    public void rankResultListByHot(){
        Collections.sort(resultList, new Comparator<SearchResult>(){

            @Override
            public int compare(SearchResult o1, SearchResult o2) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
           
        });
    }
    
    public void rankResultListByReply(){
        Collections.sort(resultList, new Comparator<SearchResult>(){

            @Override
            public int compare(SearchResult o1, SearchResult o2) {
                return Integer.valueOf(o2.getReplyCount()) - Integer.valueOf(o1.getReplyCount());
            }
            
        });
    }
    
    public void setUrl(String url, String urlDescription){
        if(toFindSearchUrl){
            StaticHelper.DOMAIN_URL = url;
            StaticHelper.SEARCH_URL = AnalysisFunction.findSearchUrl(StaticHelper.DOMAIN_URL);
        }else{
            StaticHelper.SEARCH_URL = url;
        }
        if(url == null){
            CTs.priorPrint("url is null!!!", 1);
        }
        this.urlDescription = urlDescription;
    }
    
    public void needToFindSearchUrl(){
        toFindSearchUrl = true;
    }
    
    public void setResultUrlsFilePath(String filePath){
        StaticHelper.RESULTURLS_FILEPATH = filePath;
    }
    
    public void setResultPagesFilePath(String filePath){
        StaticHelper.RESULTPAGES_FIELPATH = filePath;
    }
    
    public void setSearchContent(String content){
        StaticHelper.SEARCH_CONTENT = content;
    }
    
    public boolean getResultUrls(){
        if(StaticHelper.SEARCH_URL == null || StaticHelper.SEARCH_CONTENT == null){
            StaticHelper.logInfo("getResultUrls failed, searchUrl is null or searchContent is null\r\n");
            return false;
        }
        String encodeContent = null;
        try {
            encodeContent = java.net.URLEncoder.encode(StaticHelper.SEARCH_CONTENT, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ResultCollectionTool.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("encode content failed!!!!!!!!!");
            return false;
        }
        String searchUrl = null;
        int queryIndex;
        if((queryIndex = StaticHelper.SEARCH_URL.indexOf("q=")) > 0){
            searchUrl = StaticHelper.SEARCH_URL.substring(0, queryIndex+2) + encodeContent + StaticHelper.SEARCH_URL.substring(queryIndex+2);
        }else if((queryIndex = StaticHelper.SEARCH_URL.indexOf("k=")) > 0){
            searchUrl = StaticHelper.SEARCH_URL.substring(0, queryIndex+2) + encodeContent + StaticHelper.SEARCH_URL.substring(queryIndex+2);
        }else if((queryIndex = StaticHelper.SEARCH_URL.indexOf("txt=")) > 0){
            searchUrl = StaticHelper.SEARCH_URL.substring(0, queryIndex+4) + encodeContent + StaticHelper.SEARCH_URL.substring(queryIndex+4);
        }else{
            System.err.println("could not find the query tag!!!!!");
            return false;
        }
        CTs.priorPrint(searchUrl);
        getResultUrls(searchUrl, this.urlDescription);
       
        
//        saveSearchResult();
        return true;
    }
    
    private void getResultUrls(String searchUrl, String urlDescription){
        Page page = new Page(searchUrl);
        resultList = new ArrayList<SearchResult>();
        page.setNextPage(page.getPageUrl());
        String content = null ;
        int pageNum = 0;
        while(true){
            CTs.priorPrint("doing page " + pageNum + " *********************************************");
            if(page.getPageContent() != null){
                page.setNextPage(AnalysisFunction.getNextPage(page));
//                    System.out.println("下一页分析完成");
            }
            if(page.getNextPage() == null){
               break;
            }
            
//                System.out.println("开始进行抓取");
            CTs.priorPrint("doing page url    " + page.getNextPage());
            
            content= WebCrawler.getPageContentByNekoHtml(page.getNextPage());
            if(content == null){
                System.err.println("logging info");
                StaticHelper.logInfo("doing pageNum = " + pageNum + "!! get page content failed!!!  url=" + page.getPageUrl() + "\r\n");
                break;
//                    AdaptiveWebObjectExtration.crawler_Index = -1;
//                    Thread.interrupted();   
//                    Logger.getLogger(NextPageThread.class.getName()).log(Level.SEVERE, null, ex);
            }
//                System.out.println("抓取完毕");
            int errorCount = 0;    
            while(content.length()<50){
                try {
                    System.err.println("抓取异常，需要重新抓取");
                    Thread.sleep(5000);
                    errorCount++;
//                    ThreadErrorNum++;

                } catch (InterruptedException ex) {
                    Logger.getLogger(ResultCollectionTool.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(errorCount > 4){
                    System.err.println("下一页抽取内容不足，第二页方法分析结束");
                    System.err.println("logging info");
                    StaticHelper.logInfo("doing pageNum = " + pageNum + "!! get page content failed!!!  url=" + page.getPageUrl() + "\r\n");
                    break;
                }
                if(errorCount > 3){
                    content = WebCrawler.getPageContent(page.getNextPage());
                    continue;
                }
                content = WebCrawler.getPageContentByNekoHtml(page.getNextPage());
                if(content == null){
                    System.err.println("logging info");
                    StaticHelper.logInfo("doing pageNum = " + pageNum + "!! get page content failed!!!  url=" + page.getPageUrl() + "\r\n");
                }
            }
            if(errorCount > 4){
                break;
            }
//                pagePrint = true;
            PageFilter.addMethod(StaticHelper.PAGERUBBISHTAG);
            content = PageFilter.cleanPage(content);
            if(content.equals(page.getPageContent())){
                break;
            }
            page.setPageContent(content);
//            if(dealedUrl.contains(page.getNextPage())){
//                continue;
//            }
               
//                CommonStaticConstant.CrawlerNum++;
                 //进行分析啊
//                System.out.println("正在进行页面下一页抓取的分析工作 url = "+page.getPageUrl());
            System.out.println("Crawler finished, Analysis is begining...");
//                System.err.println("这里没有存储哦......");
            ArrayList<Item> itemList = AnalysisFunction.getItemWithElement(page);    
            if(itemList == null){
                System.err.println("pageUrl   " + page.getNextPage() + "    get no itemList!!!!!!!!!");
                pageNum++;
                continue;
            }
            for(Item item : itemList){
                boolean hasDealed = false;
                ArrayList<Element> elementList = item.getElementList();
                SearchResult result = new SearchResult();
                for(Element element : elementList){
                    if(!result.isNotFullResult()){
                        break;
                    }
                    switch(element.getElementType()){
                        case StaticHelper.Url_Title_TYPE:
                            if(dealedUrl.contains(element.getUrl())){
                                hasDealed = true;
                                break;
                            }
                            if(result.getUrl() != null){
                                break;
                            }
                            result.setUrl(element.getUrl());
                            dealedUrl.add(element.getUrl());
                            break;
                        case StaticHelper.HitsNum_TYPE:
                            result.setVisitCount(element.getElementContent());
                            break;
                        case StaticHelper.ReplyNum_TYPE:
                            result.setReplyCount(element.getElementContent());
                            break;
                        case StaticHelper.ReleaseTime_TYPE:
                            result.setPostTime(element.getElementContent());
                            break;
                        default:
                            break;
                    }
                }
                if(hasDealed == true){
                    continue;
                }
                result.setDescription(urlDescription);
                result.makeFullResult();
//                if(result.getReplyCount() == null){
//                    result.setReplyCount("0");
//                }
                resultList.add(result);
            }
            pageNum++;
            if(resultList.size() > 100){
                saveSearchResult();
                resultList.clear();
            }
//            dealedUrl.add(page.getNextPage());
        }
        saveSearchResult();
    }
    
    public void saveItemResult(ArrayList<Item> itemList, String url){
        if(StaticHelper.RESULTPAGES_FIELPATH == null || itemList.isEmpty() || itemList == null){
            System.err.println("logging info");
            StaticHelper.logInfo("The reuslt item list save path is null or itemList is empty, saving file failed!!! \r\n url = " + url + "\r\n");
        }
        try {
            new FileStorage().saveItemExcel(StaticHelper.RESULTPAGES_FIELPATH, itemList, url);
        } catch (IOException ex) {
            Logger.getLogger(ResultCollectionTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(ResultCollectionTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException ex) {
            Logger.getLogger(ResultCollectionTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void saveSearchResult(){
        if(StaticHelper.RESULTURLS_FILEPATH == null || resultList.isEmpty()){
            StaticHelper.logInfo("The reuslt url list save path is null or resultList is empty, saving file failed!!! \r\n");
            return;
        }
        try {
            new FileStorage().saveSearchResultExcel(StaticHelper.RESULTURLS_FILEPATH, resultList, null);
        } catch (IOException ex) {
            Logger.getLogger(ResultCollectionTool.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("logging info");
            StaticHelper.logInfo("saving file failed!!! \r\n");
        } catch (BiffException ex) {
            Logger.getLogger(ResultCollectionTool.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("logging info");
            StaticHelper.logInfo("saving file failed!!! \r\n");
        } catch (WriteException ex) {
            Logger.getLogger(ResultCollectionTool.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("logging info");
            StaticHelper.logInfo("saving file failed!!! \r\n");
        }
    }

    /**
     * @param toRankCount the toRankCount to set
     */
    public void setToRankCount(int toRankCount) {
        this.toRankCount = toRankCount;
    }
    
    public void readSearchResult(){
        if(StaticHelper.RESULTURLS_FILEPATH == null){
            StaticHelper.logInfo("The reuslt url list save path is null, reading file failed!!! \r\n");
            System.err.println("logging info");
            return;
        }
        readSearchResult(StaticHelper.RESULTURLS_FILEPATH);
//        resultList = new FileStorage().readSearchResultExcel(StaticHelper.RESULTURLS_FILEPATH);
    }
    
    public void readSearchResult(String filePath){
        resultList = new FileStorage().readSearchResultExcel(filePath);
    }
}
