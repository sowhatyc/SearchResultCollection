/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonTools;

import SearchResultsCollection.AnalysisExtratorRegexSets;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class StaticHelper {
    
    public static String SEARCH_URL = null;
    public static String DOMAIN_URL = null;
    public static String RESULTURLS_FILEPATH = null;
    public static String RESULTPAGES_FIELPATH = null;
    public static String LOG_FILEPATH = "E:\\workspace\\SearchApplication\\logInfo.txt";
    public static String SEARCH_CONTENT = null;
    public static int START_ROW = 0;
    
    public static String[] PAGERUBBISHTAG = {"<!--.*?-->","<link[^>]*>","<meta[^>]*>","</meta>","</link>","<!doctype[^>]*>","<style[^>]*>.*?</style>","<font[^>]*>","</font>"
    ,"<br>","</br>","<br\\s*/>","<img[^>]*>","</img>","<input[^>]*>","</input>","<cite[^>]*>","</cite>","<b>","</b>","<h[1-6][^>]*>","</h[1-6]>","<em[^>]*>","</em>","<label[^>]*>","</label>","</script>"
    ,"<strong[^>]*>", "</strong>","<base[^>]*>","</?u>"};
    
    public static ArrayList<AnalysisExtratorRegexSets> BlockRegexSets =  new ArrayList<AnalysisExtratorRegexSets>();
    
    public static void logInfo(String info){
        new FileStorage().saveFile(LOG_FILEPATH, info);
    }
    
    
    public final static int Url_TYPE = 0;
    public final static int Url_Title_TYPE = 1;
    public final static int Url_ReleaseUser_TYPE = 2;
    public final static int Url_ReplyUser_TYPE = 5;
    public final static int Url_Digit_TYPE = 3;
    public final static int Url_DateTime_TYPE = 7;
    
    
    public final static int SingleElement_TYPE = 10;
    public final static int SingleLabel_TYPE = 18;
    
    public final static int NonUrl_ContentText_TYPE = 11;
    public final static int SingleElementWithTime_TYPE = 16;
    
    
    public final static int TimeDate_TYPE = 20;
    public final static int ReplyTime_TYPE = 25;
    public final static int ReleaseTime_TYPE = 22;
    
    public final static int Digit_TYPE = 30;
    public final static int HitsNum_TYPE = 33;
    public final static int ReplyNum_TYPE = 35;
    
    public final static int reply_FloorNum_TYPE = 41;
    
    public final static int Parent_Url_TYPE = 102;
    public final static int Parent_Title_TYPE = 101;
    
    public final static int Other_Information_TYPE = 7474;
    
    
    private static ArrayList<String> searchContents = null;
    
    public static ArrayList<String> initialSearchContents(){
        searchContents = new ArrayList<String>();
        searchContents.add("十八大");
        searchContents.add("生态文明");
        searchContents.add("文化建设");
        searchContents.add("户籍改革");
        searchContents.add("医疗改革");
        searchContents.add("就业问题");
        searchContents.add("教育改革");
        searchContents.add("司法公正");
        searchContents.add("住房问题");
        searchContents.add("收入分配");
        searchContents.add("食品安全");
        searchContents.add("药品安全");
        searchContents.add("社会保障");
        searchContents.add("弱势群体");
        searchContents.add("民主监督");
        searchContents.add("依法行政");
        searchContents.add("反腐倡廉");
        searchContents.add("异地高考");
        searchContents.add("三农问题");
        searchContents.add("新交规");
        searchContents.add("小康社会");
        searchContents.add("计划生育");
        searchContents.add("实名制");
        searchContents.add("环保");
        searchContents.add("速生鸡");
        searchContents.add("污染");
        searchContents.add("道德");
        searchContents.add("价值体系");
        searchContents.add("政治体制改革");
        searchContents.add("依法治国");
        searchContents.add("退休制度");
        searchContents.add("信访制度");
        searchContents.add("土地制度");
        searchContents.add("劳教制度");
        searchContents.add("劳动教养");
        searchContents.add("多党制");
        searchContents.add("三公消费");
        searchContents.add("拆迁");
        return searchContents;
    }
    
    
    public  static String firstScriptFilter(String pageContent){
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
