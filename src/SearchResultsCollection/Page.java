/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

//import CommonTools.CommonStaticConstant;
//import DataStorage.StoreProcess;
//import PageAnalysis.AnalysisFunction;
//import PageAnalysis.PageFilter;
//import WebSerivce.WebCrawler;
//import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class Page {
    private String pageContent;
    private String pageUrl;
    private int pageType;
    private String nextPage;

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public Page() {
        pageContent = null;
        pageUrl = null;
        pageType = -1;
        nextPage = null;
        
    }
    
    
    
    public void setPageUrl(String url){
        this.pageUrl = url;
    }
    
    public String getPageUrl(){
        return this.pageUrl;
    }
    
    public void setPageContent(String content){
        this.pageContent = content;
    }
    
    public String getPageContent(){
        return pageContent;
    }
    
    public void setPageType(int type){
       pageType = type;
    }
    public int getPageType(){
        if(pageType != -1){
            return pageType;
        }
        if(pageUrl == null){
            System.err.println("网页URL为空，无法分析网页");
            System.exit(1);
        }
        pageUrl = pageUrl.toString();
        if(pageUrl.contains("thread")||pageUrl.contains("content")){
            pageType =PageType.ContentPage;
            return pageType;
        }
        if(pageUrl.contains("bbs")||pageUrl.contains("forum")){
            pageType =PageType.BlockPage;
            return pageType;
        }
        if(pageUrl.contains("user")){
            pageType =PageType.UserPage;
            return pageType;
        }
        return -1;
           
    }

    public Page(String pageContent, String pageUrl, int pageType) {
        this.pageContent = pageContent;
        this.pageUrl = pageUrl;
        this.pageType = pageType;
    }
     public Page(String pageContent, String pageUrl) {
        this.pageContent = pageContent;
        this.pageUrl = pageUrl;
        this.pageType = -1;
    }
     public Page(String url) {
//        this.pageContent = WebCrawler.getPageContent(url);
        this.pageUrl = url;
        this.pageContent = null;
        this.pageType = -1;
    }
//     public boolean initPageContent(){
//         pageContent =  Crawler(pageUrl);
//     }

//    public boolean initPageType() {
//        int type = this.pageTypeAnalysis();
//        this.setPageType(type);
//        return false;
//        
//    }
    /**
     * @author wc
     * 网页类型的分析函数
     * @return  返回的是 网页的类型
     */
//    private int pageTypeAnalysis() {
//        if(this.getPageContent() == null){
//            if(this.getPageUrl() == null){
//                return -1;
//            }else{
//                this.setPageContent(WebCrawler.getPageContent(this.getPageUrl()));
//            }
//            
//        }
//        
//        return -1;
//        
//        
//    }
//    public void pageFilter(){
//        PageFilter.addMethod(CommonStaticConstant.PAGERUBBISHTAG);
//        pageContent = PageFilter.firstScriptFilter(pageContent);
//        pageContent = PageFilter.secondOtherFilter(pageContent);
//    }

     
     
}
