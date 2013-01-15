/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

import CommonTools.MatchRegex;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class PageFilter {
    
    /**
     * 私有的过滤标签的方法集合
     */
    private static ArrayList<String> filterMethods = new ArrayList<String>();
    /**
     * 
     * @param method 加入的方法
     */
    public  synchronized static void addMethod(String method){
        
        filterMethods.add(method);
    }
    public  synchronized static void addMethod(String[] methodList){
        int methodNum = methodList.length;
        for(int i = 0;i<methodNum;i++){
            filterMethods.add(methodList[i]);
        }
    }
    
    public synchronized static String cleanPage(String pageContent){
        pageContent = firstScriptFilter(pageContent);
        return secondOtherFilter(pageContent);
    }
    
    /**
     * 
     * @param pageContent
     * @return 过滤后的网页内容
     * @description 根据已有的过滤方法集合来过滤网页
     */
    public  synchronized static String secondOtherFilter(String pageContent){
        String replacement = " ";
//        String content = "";
        for(String method : filterMethods){
            MatchRegex  rg = new MatchRegex(method);
            pageContent = rg.doValueReplaceAll(pageContent, replacement); 
        }
        return pageContent;
    }
    /**
     * 
     * @param pageContent
     * @return 过滤script标签的网页内容
     * @description 由于script标签过滤的方式较为特殊，因此暂时单独写为一个方法
     */
    public  synchronized static String firstScriptFilter(String pageContent){
        String replacement = "";
//        String content = "";
        MatchRegex rg = new MatchRegex("<script[^>]*>((?!<script).)*?</script>");
        pageContent = rg.doValueReplaceAll(pageContent, replacement);
        MatchRegex mrg = new MatchRegex("<script ");
        while(mrg.findMatch(pageContent)){
            pageContent = rg.doValueReplaceAll(pageContent, replacement);
        }
        return pageContent;
    }
    
}
