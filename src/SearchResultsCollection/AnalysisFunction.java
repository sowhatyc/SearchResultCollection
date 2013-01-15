/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

import CommonTools.CTs;
import CommonTools.DataValue;
import CommonTools.MatchRegex;
import CommonTools.StaticHelper;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class AnalysisFunction {
    
    
    public static String findSearchUrl(String domainUrl){
        return null;
    }
    
    public static String getNextPageSimpleMethod(Page page){
        System.err.println("get Next Page Simple Method called!!!!");
        if(page.getNextPage().contains("&pn=") || page.getNextPage().contains("page=")){
            int pageIndex = 0;
            int length = 0;
            if(page.getNextPage().contains("&pn=")){
                pageIndex = page.getNextPage().indexOf("&pn=");
                length = 4;
            }else if(page.getNextPage().contains("pageNo=")){
                pageIndex = page.getNextPage().indexOf("pageNo=");
                length = 7;
            }else if(page.getNextPage().contains("page=")){
                pageIndex = page.getNextPage().indexOf("page=");
                length = 5;
            }
            int pageNum = 0;
            int i = 0;
            for(i=pageIndex+length; i<page.getNextPage().length(); ++i){
                if(page.getNextPage().charAt(i) >= '0' && page.getNextPage().charAt(i) <= '9'){
                    pageNum = pageNum*10 + (page.getNextPage().charAt(i) - '0');
                }else{
//                    url = page.getNextPage().substring(0, pageIndex+length) + (pageNum+1) + page.getNextPage().substring(i);
                    break;
                }
            }
            String url = page.getNextPage().substring(0, pageIndex+length) + (pageNum+1) + page.getNextPage().substring(i);
            return url;
        }
        if(page.getNextPage().contains("_")){
            int slashIndex = page.getNextPage().lastIndexOf("_");
            int dotIndex = page.getNextPage().lastIndexOf(".");
            if(slashIndex > dotIndex){
                return null;
            }
            int pageNum = 0;
            boolean onlyDigit = true;
            for(int i=slashIndex+1; i<dotIndex; ++i){
                if(page.getNextPage().charAt(i) >= '0' && page.getNextPage().charAt(i) <= '9'){
                    pageNum = pageNum*10 + (page.getNextPage().charAt(i) - '0');
                }else{
                    onlyDigit = false;
                }
            }
            if(!onlyDigit){
                return null;
            }else{
                return page.getNextPage().substring(0,slashIndex+1) + (pageNum+1) + page.getNextPage().substring(dotIndex);
            }
        }
        System.err.println("could not find the next page!!!!");
        return null;
        
    }
    
    public  synchronized static String getNextPage(Page page){
        
        /*
         * 本过程中 主要包含两个步骤：
         * 1、分别使用多种下一页的获取和采集策略进行下一页的分析
         * 2、进行相应的比对和分析，获得NEXTPAGE正确性的权值方法。
         *
         */
//        第一部分 进行 第二页的抽取
        
//        方法1 直接抽取下一页
        String url = page.getPageUrl();
        ArrayList<String> nextPage = new ArrayList<String>();
        DataValue webInfo;
//        MatchRegex rg = new MatchRegex("(?<=<a\\s{0,1000}href\\s{0,1000}=\\s{0,1000}\")([^\"]*)\"[^>]*>\\s*下一页\\s*</a>");//成功版本 
        //实验版本1
        /*
         * 实验目的，让其可以适应更多通用性
         */
        
       
//        MatchRegex rg = new MatchRegex("(?<=<a[^<]{0,50}href\\s{0,2}=\\s{0,1000}\")[^>]*?>下一页</a>");
        
//        MatchRegex rg = new MatchRegex("((?<=<a[^<]{0,50}href\\s?=)[^<]*?[\"\\s][^<]*?)><{0,2}[^<]*?下一页");// OK版本
//        MatchRegex rg = new MatchRegex("((?<=<a[^<]{0,50}href\\s?=)[^<]*?[\"\\s][^<]*?)><{0,2}[^<]{0,20}>?[^<]{0,10}下一页");// OK版本副本测试版本
//       MatchRegex rg = new MatchRegex("((?<=<a[^<]{0,50}href\\s?=)[^<]*?)>下一页"); //58同城修改版本
        
        
//        temp
//        MatchRegex rg = new MatchRegex("<a[^<>]{0,50}href\\s{0,2}=([^<>]+)>下一页</a>");//2012年6月18日正确的
        System.out.print("The first process of nextPage analysis is begining... ");
        MatchRegex rg = new MatchRegex("<a[^<>]{0,50}href\\s{0,2}=([^<>]+)>(\\s*?<(?!a)[^<>]*>)?[^<>]*下\\s{0,2}一\\s{0,2}页[^<>]*(</(?!a)[^<>]*>\\s*)?</a>");//2012年6月18日测试使用
        webInfo = rg.doValueMatch(page.getPageContent());
        if(webInfo.getFindNum() != 0){
            if(webInfo.getValueAt(0)!=null){
                 url = webInfo.getValueAt(0)[0];
                 url = parentUrlFilter(page.getPageUrl(),url);
            }
            if(url != null){
                nextPage.add(url);
                System.out.println(" FINISHED!!!");
//                System.out.println("Get the nextPage finished in first method, the nextPage is "+url);
                return url;
            }else{
//                System.out.println("Get the nextPage has failed in first method.");
            }
        }
        else{
            url = null;
            webInfo = null;
        }
        /*
         * 第一套方案无法获得结果，进入第二套方案
         * 第二套方案 为判断是否能获得一连串的数字标签
         */
        
        System.out.print("\n The second process of nextPage analysis is begining... ");
//        CTs.priorPrint("进入第二方案，进行1-N的混合型网页当前状态分析",0);
//        rg = new MatchRegex("(<(?!a)[^<]*>\\d+</[^<]*>[^<]*?\\s*[^<]*?<a[^<]+href\\s*=([^<]*)>[^<]*?\\d+[^<]*?</a>)");//2012年6月18日之前是正确的
        rg = new MatchRegex("((<a[^<>]+href\\s*=([^<>]*)>[^<>]*?\\d+[^<>]*?</a>[^<>]*?)?<(?!a)[^<>]*?>\\s*?\\W?\\d+\\W?\\s*?</[^<>]*?>[^<>]*?<a[^<>]+href\\s*=([^<>]*)>[^<>]*?\\d+[^<>]*?</a>)");//2012年6月18日之前是正确的
//        rg.setRegex("(<[^<>]+>)");
//        rg.setRegex("(<(?!a)[^<>]*>(\\s*<(?!a)[^<>]*>)?[^<>]{0,2}\\d+[^<>]{0,2}(</(?!a)[^<>]*>\\s*)?</[^<>]+>[^<>]*?<a[^<>]+href\\s{0,2}=([^<]*)>(\\s*<(?!a)[^<>]*>)?[^<>]{0,2}\\d+[^<>]{0,2}(</(?!a)[^<>]*>\\s*)?</a>)");//2012年6月18日测试版本
        webInfo = rg.doValueMatch(page.getPageContent());

        if(webInfo.getFindNum() > 0){
            webInfo.printResult();
            //
            System.out.println("尚未测试完成.... 获取第二页方法");
            if(webInfo.getValueAt(0) != null){
                url = webInfo.getValueAt(0)[1+2];
            }
            if(url != null){
                url = parentUrlFilter(page.getPageUrl(),url);
                if(url != null){
                    nextPage.add(url);
                    System.out.println("| Finished in first method, the nextPage is "+url);
                    return url;
                }else{
                    System.err.println("The nextPage Analysis was failed, please propose a new method to extrating");
//                    return null;
                    url = null;
                }
            }
        }
        
        System.err.println("The nextPage Analysis was failed, please propose a new method to extrating");
        return null;
        
        
//        CTs.priorPrint("进入第三方案，进行网页过滤后完成混合型网页当前状态分析",0);
//        rg = new MatchRegex("(<(?!a)[^<]*>\\d+</[^<]*>[^<]*?\\s*[^<]*?<a[^<]+href\\s*=([^<]*)>[^<]*?\\d+[^<]*?</a>)");
        
//        rg = new MatchRegex("(<(?!a)[^<]*>\\d+</[^<]*>\\s*<?[^<]*?>?\\s*[^<]*?\\s*[^<]*?<a[^<]+href\\s*=([^<]*)>[^<]*?\\d+[^<]*?</a>)");
//        PageFilter.addMethod(StaticHelper.PAGERUBBISHTAG);
//        String pageContent = PageFilter.cleanPage(page.getPageContent());
        
//        pageContent = PageFilter.firstScriptFilter(page.getPageContent());
//        pageContent = PageFilter.secondOtherFilter(pageContent);
//        page.setPageContent(pageContent);
//        webInfo = rg.doValueMatch(page.getPageContent());
//        webInfo.getResultString();
//        if(webInfo.getFindNum() != 0){
//            url = webInfo.getValueAt(0)[1];
//            if(url != null){
//                nextPage.add(url);
//                CTs.priorPrint("url = "+parentUrlFilter(page.getPageUrl(),url), 0);
//                return parentUrlFilter(page.getPageUrl(),url);
//                
//            }
//        }
//        
        ////////////////////////////////////////////////////////之前的是完成版
     
        
        
        
//         rg = new MatchRegex("(<a[^<]+>\\d+</a>)");
//         webInfo = rg.doValueMatch(page.getPageContent());
//         CTs.priorPrint("**********************************************************", 0);
//         webInfo.printResult();
//         
//         CTs.priorPrint("**********************************************************", 0);
        /*
         * 第一方案不行 进入第二方案
         */
//        CTs.priorPrint("进入第三方案，进行1-N的混合型网页当前状态分析",0);
//        rg = new MatchRegex("(<a href=.*?>.*?</a>)");//(<(?!a)[^<]+>[^<]+</[^<]+>)\\s
//        webInfo = rg.doValueMatch(page.getPageContent());
//        webInfo.printResult();
        
//        rg = new MatchRegex("(<a href=(.*?)>(.*?)</a>)");//(<(?!a)[^<]+>[^<]+</[^<]+>)\\s
//        webInfo = rg.doValueMatch(page.getPageContent());
//        webInfo.printResult();
        
//        方法2 使用 1,2,3,...等方法进行叶面提取
        //目前先用一个凑合的方法吧
        
//        <a\s*href\s?=?\"?.*?>\s?\d{1,2}\s?<.*?<a\s*href\s?=?\"?.*?>\s?\d{1,2}\s?<
//        MatchRegex rg_way2 = new MatchRegex("<a\\s*href\\s?=\"?(.*?)\"?'?\\s?>[\\s\"]?\\d{1,2}[\\s\"]?<");
//        MatchRegex rg_way2 = new MatchRegex("<a\\s*href\\s?=\"?(.*?)\"?'?\\s?>[\\s\"]?\\d{1,2}[\\s\"]?<.*?<a\\s*href\\s?=\"?(.*?)\"?'?\\s?>[\\s\"]?\\d{1,2}[\\s\"]?");
//        MatchRegex rg_way2 = new MatchRegex("<a href=\"?(.*?)>(\\d)<");
        
//        MatchRegex rg_way2 = new MatchRegex("<a\\s*href\\s?=[\"\\s\']?(.*?)[\"\\s\']?.*?>[\"\\s\']?\\d{1,3}[\"\\s\']?<"); 失败的

//        MatchRegex rg_way2 = new MatchRegex("<a\\s*href\\s?=(.*?) .*?>(.*?)</a>");
//        <a((?!<a).)*>\W{0,2}\d{1,3}</a>
//         MatchRegex rg_way2 = new MatchRegex("(<a\\s*href\\s?=[^<]*?>.*?</a>.*?<a\\s*href\\s?=[^<]*?>.*?</a>)");
        
//        <a\s*href\s?=[^<]*>\d+</a>[^<]*<a\s*href\s?=[^<]*>\d+</a>
//         MatchRegex rg_way2 = new MatchRegex("(<a\\s*href\\s?=[^<]*>[\"\\s]?\\d+[\"\\s]?</a>[^<]*<a\\s*href\\s?=[^<]*>[\"\\s]?\\d+[\"\\s]?</a>)");
//        String content = WebCrawler.getPageContent("http://bbs.city.tianya.cn/tianyacity/content/307/1/12174.shtml");
        
//        (<(?!a)[^<]+>\d<.*?>)?(\s*<a\shref=\".*?\">\d+</a>)+(\s*<(?!a)[^<]+>\d<.*?>)?(\s*<a\shref=\".*?\">\d+</a>)+(\s*<(?!a)[^<]+>\d<.*?>)?
//        MatchRegex rg_way2 = new MatchRegex("(<?!a[^<]+>\\d<.*?>)?(\\s*<a\\shref=\".*?\">\\d+</a>)+(\\s*<?!a[^<]+>\\d<.*?>)?(\\s*<a\\shref=\".*?\">\\d+</a>)+(\\s*<?!a[^<]+>\\d<.*?>)?");//失败版本
        
        
        //测试成功版本  *****
//        (<(?!a)[^<]+>\d<.*?>)?(\s*<a\shref=\".*?\">\d+</a>)+(\s*<(?!a)[^<]+>\d<.*?>)?(\s*<a\shref=\".*?\">\d+</a>)+(\s*<(?!a)[^<]+>\d<.*?>)?

//        MatchRegex rg_way2 = new MatchRegex("(<[^<]+>\\d+<.*?>)");//(\\s*<a\\shref=\".*?\">\\d+</a>)
        
        //测试第二思路 先进性连续的<a 抽取
//        MatchRegex rg_way2 = new MatchRegex("<a[^<]href=([^<]*?)>([^<]*?)</a>\\s(<a[^<]href=[^<]*?>[^<]*?</a>\\s)+");
        
//        MatchRegex rg_way2 = new MatchRegex("(<a[^<]href=[^<]*?>[^<]*?</a>)\\s(<a[^<]href=[^<]*?>[^<]*?</a>\\s)+");
        
        
//        String content = WebCrawler.getPageContent("http://tieba.baidu.com/f?kw=%C7%C7%C8%CE%C1%BA&pn=0#!/m/p0");
//        MatchRegex rg_way2 = new MatchRegex("(<a[^<]href=[^<]*?>\\d+</a>)\\s(<a[^<]href=[^<]*?>\\d+</a>\\s)+"); OK
        
//         String content = WebCrawler.getPageContent("http://bbs.city.tianya.cn/tianyacity/content/307/2/12174.shtml");
//        MatchRegex rg_way2 = new MatchRegex("(<a[^<]*href=[^<]*?>\\d+</a>)");
        
        
//            webInfo = rg_way2.doValueMatch(content);// 仅供测试使用
//            webInfo.printResult();
//            result = webInfo.getValueAt(0);
//            url = result[0];
//
//            result = null;
//            webInfo = null;
        
//        单独处理威风
//        http://bbs.weiphone.com/thread-htm-fid-435.html
//         String content = WebCrawler.getPageContent("http://bbs.weiphone.com/thread-htm-fid-435.html");
////         MatchRegex rg_way2 = new MatchRegex("(<a[^<]*href=[^<]*?>(.*?)</a>[^<]*<a)");
//         MatchRegex rg_way2 = new MatchRegex("(<a(.*?)</a>(.*?)<a(.*?)</a>)");
         
      
//        webInfo = rg_way2.doValueMatch(content);// 仅供测试使用
//        
//        webInfo.printResult();
//        
//        result = webInfo.getValueAt(0);
//        url = result[0];
//
//        result = null;
//        webInfo = null;
//       方法3 使用....
//         (<(?!a)[^<]+>\d<.*?>)?(\s*<a\shref=\".*?\">\d+</a>)+(\s*<(?!a)[^<]+>\d<.*?>)?(\s*<a\shref=\".*?\">\d+</a>)+(\s*<(?!a)[^<]+>\d<.*?>)?

//        rg_way2 = new MatchRegex("(<(?!a)[^<]+>\\d+</[^<]*?>)\\s*(<a[^<]+href=.*?>(\\d+)</a>)+");
////                + "(<(?!a)[^<]+>\\d<.*?>)");
//        webInfo = rg_way2.doValueMatch(content);// 仅供测试使用
////        
//        webInfo.printResult();
        
//        result = webInfo.getValueAt(0);
//        url = result[0];
//
//        result = null;
//        webInfo = null;
        
        
        
//        CTs.priorPrint("url = ", 0);
//        CTs.priorPrint("page.getPageUrl()     = "+page.getPageUrl(), 0);  
        
        
        
        
        
        
       
        
        
        
        /*
         * From Winc
         * 修改 如果 nextPage 不以 Http开头的时候的处理方案
         */
//        url = parentUrlFilter(page.getPageUrl(),url);
//        return url;
        
    }
    
    
    public   synchronized static String parentUrlFilter(String url, String sonUrl){
        //进行url基本过滤的过滤
        
        String parentUrl = url;
        int parentUrlNum = -1;
        if(parentUrl == null || sonUrl == null){
            CTs.priorPrint("sonUrl = "+sonUrl+"为空", 1);
            return sonUrl;
        }
        sonUrl = urlFilter(sonUrl);
//        System.out.println("sonUrl1 = "+sonUrl);
        String urlIdentity ;
        if(sonUrl == null){
            return null;
        }
        if(sonUrl.length()>10){
           urlIdentity  = sonUrl.substring(0,10);
        }else{
            urlIdentity = sonUrl;
        }
        if(urlIdentity.length()<4){
            return sonUrl;
        }
        if(urlIdentity.substring(0, 3).equals("../")){
//            CTs.priorPrint("../", 0);
            parentUrlNum = parentUrl.lastIndexOf("/");
            parentUrl = parentUrl.substring(0, parentUrlNum);
            parentUrlNum = parentUrl.lastIndexOf("/")+1;
            sonUrl = parentUrl.substring(0, parentUrlNum)+sonUrl.substring(3);
//            CTs.priorPrint(parentUrl.substring(0, parentUrlNum)+sonUrl.substring(3), 0);
//            CTs.priorPrint(sonUrl, 0);
//            System.out.println("sonUrl2 = "+sonUrl);
            return sonUrl;
            
            //说明是 父节点
        }else if(urlIdentity.substring(0, 1).equals("/")){
//            CTs.priorPrint("/", 0);
            parentUrlNum = parentUrl.indexOf("//")+2;
            parentUrlNum = parentUrl.indexOf("/", parentUrlNum)+1;
            sonUrl = parentUrl.substring(0, parentUrlNum)+sonUrl.substring(1);
//            CTs.priorPrint(parentUrl.substring(0, parentUrlNum)+sonUrl.substring(1), 0);
//            CTs.priorPrint(sonUrl, 0);
            return sonUrl;
            
            
        }else if(urlIdentity.substring(0, 1).equals("?")){
            parentUrlNum =  parentUrl.indexOf("?");
            sonUrl = parentUrl.substring(0, parentUrlNum)+sonUrl;
            return sonUrl;
        }
        else if(urlIdentity.indexOf(":") != -1){
            //直接返回nextPage子页面
//            CTs.priorPrint("页面正确", 0);
//            CTs.priorPrint(sonUrl, 0);
            return sonUrl;
            
        }else if(new MatchRegex("\\w+").findMatch(sonUrl)){
            parentUrlNum = parentUrl.lastIndexOf("/")+1;
//            CTs.priorPrint("什么都没有包含的", 0);
            sonUrl = parentUrl.substring(0, parentUrlNum)+sonUrl;
//            CTs.priorPrint(parentUrl.substring(0, parentUrlNum)+sonUrl, 0);
//            CTs.priorPrint(sonUrl, 0);
            return sonUrl;
            
            //
        }
        return null;
        
        
        
        
    }
    
    
    private  synchronized static String urlFilter(String url){
//        CTs.priorPrint("你好呀url");
        if(null == url){
            return null;
        }
        url = url.toLowerCase();
        String url_temp = null;
        DataValue webInfo;
        MatchRegex rg = null;
//        if(url.substring(0,1).equals("\"".toString())){
//            rg = new MatchRegex("\"(.*?)\"");
//        }else 
        if(url.contains("javascript")){
            return null;
        }
        if(url.indexOf("\"") >= 0){
            rg = new MatchRegex("\"(.*?)\"");
//        }else if(url.substring(0,1).equals("'".toString())){
        }else if(url.indexOf("'")>=0){
            rg = new MatchRegex("'(.*?)'");
        }
        if(rg == null){
//            CTs.priorPrint("无匹配结果***********************");
            return url;
        }
       
        webInfo = rg.doValueMatch(url);
//        CTs.priorPrint("url ====="+url, 0);
//        webInfo.printResult();
        if(webInfo.getFindNum() != 0){
            url_temp = webInfo.getValueAt(0)[0];
//            CTs.priorPrint("************************URL的过滤结果是："+url_temp,0);
            return url_temp;
        }
        else if(new MatchRegex("[\u4e00-\u9fa5]").findMatch(url)){
            return null;
        }
        else{
            CTs.priorPrint("urlFilter 方法 未能匹配成功 URL字段为:("+url+")",1);
            return url;
        }
        
    }
    
    public static ArrayList<Item> getItemWithElement(Page page){
        String temp_regex = null;
        String urlRegex = new MatchRegex("\\d+").doValueReplaceAll(page.getPageUrl(), "\\\\d+");
        ArrayList<String> BlockExtratorMatchRegexStringList = null;
        for(int i = 0;i<StaticHelper.BlockRegexSets.size();i++){
            if(urlRegex.equals(StaticHelper.BlockRegexSets.get(i).getUrlMatchRegex())){
                BlockExtratorMatchRegexStringList = new ArrayList();
                if(StaticHelper.BlockRegexSets.get(i).getBlockContentMatchRegexSets() == null){
                    BlockExtratorMatchRegexStringList = null;
                    break;
                }
                BlockExtratorMatchRegexStringList.addAll(StaticHelper.BlockRegexSets.get(i).getBlockContentMatchRegexSets());
            }
        }
        if(BlockExtratorMatchRegexStringList == null){
            BlockExtratorMatchRegexStringList = createBlockRegexMatchString(page);//获取块的正则表达式/
            StaticHelper.BlockRegexSets.add(new AnalysisExtratorRegexSets(urlRegex,BlockExtratorMatchRegexStringList));
        }
//             BlockExtratorMatchRegexStringList = ItemAnalysis.createBlockRegexMatchString(page);//获取块的正则表达式/
            
//            if(page.getPageUrl().contains("tianya")&&page.getPageUrl().contains("content")){
//                
//                if(BlockExtratorMatchRegexStringList.get(0).toLowerCase().lastIndexOf("</table>")>BlockExtratorMatchRegexStringList.get(0).length()-10){
//                    System.err.println("BlockExtratorMatchRegexStringList.get(0) = "+BlockExtratorMatchRegexStringList.get(0));
//                     temp_regex = BlockExtratorMatchRegexStringList.get(0).substring(0, BlockExtratorMatchRegexStringList.get(0).toLowerCase().lastIndexOf("</table>"))+"</div>)";
//                }else{
//                    System.err.println("BlockExtratorMatchRegexStringList.get(0) = "+BlockExtratorMatchRegexStringList.get(0));
//                     temp_regex = BlockExtratorMatchRegexStringList.get(0).substring(0, BlockExtratorMatchRegexStringList.get(0).toLowerCase().lastIndexOf("</div>"))+"</table>)";
//                }
//               
//            }else{
//                if(BlockExtratorMatchRegexStringList.size()>1){
//                    temp_regex = BlockExtratorMatchRegexStringList.get(1);
//                }else{
//                    temp_regex = BlockExtratorMatchRegexStringList.get(0);
//                }
//            }
//             System.out.println("temp_regex = "+temp_regex);
//            for(String string:BlockExtratorMatchRegexStringList){
//                System.out.println("BlockExtratorMatchRegexStringList = "+string+" num ="+BlockExtratorMatchRegexStringList.size());
//            }
        ArrayList<Item> itemUntreatListWithElements = null;
        if(BlockExtratorMatchRegexStringList == null){
            System.err.println("The BlockExtratorMatchRegexStringList is null, returning...");
            return itemUntreatListWithElements;
        }
        if(BlockExtratorMatchRegexStringList.size()<2||BlockExtratorMatchRegexStringList.size()>0){
            itemUntreatListWithElements  = getItemSetsWithElement(page, BlockExtratorMatchRegexStringList.get(0));//获取单独的Item withElement
//            ArrayList <Item> itemUntreatListWithElements = ItemAnalysis.CreateBlockAndGetItemsAndElements(page);
//            ItemAnalysis.elementAttributeIdentifier(itemUntreatListWithElements,page);// 本行就是得到每个元素的element_type
        }else{
            itemUntreatListWithElements = getItemSetsWithElement(page, BlockExtratorMatchRegexStringList.get(0));
        }
//        for(int i = 0;i<itemUntreatListWithElements.size();i++){
//            itemUntreatListWithElements.get(i).print();
//        }
        //进行网页细粒度分析
//            ItemAnalysis.Element_FineGrained_Classify(page,itemUntreatListWithElements);
            //存储
        return  itemUntreatListWithElements;
        
    }
    
    
    public synchronized static ArrayList<String> createBlockRegexMatchString(Page page){
        ArrayList <String> BlockRegexStringList;
        
        boolean thirdTimeStruture = false;
//        int matchTimeType = 0;
//        PageFilter.addMethod(StaticHelper.PAGERUBBISHTAG);
//        page.setPageContent(null);
        String matchTimeIdentity;
//        matchTimeIdentity = "((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+)(.*?)((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+)";
        matchTimeIdentity = "(>[^<>]*?(\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+)(.*?)((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+[^<>]*?<)";
//        matchTimeIdentity = "((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+)(.*?((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+).*?)((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+)";
        MatchRegex rg = new MatchRegex(matchTimeIdentity);
        DataValue dv = rg.doValueMatch(page.getPageContent());
//        dv.printResult();
//        System.out.println(page.getPageContent());
//        rg.setRegex("(\\d+:\\d+)");
//        dv = rg.doValueMatch(page.getPageContent());
//        dv.printResult();
        
        if(dv.getFindNum()<3){
            System.out.println("使用时间抽取方案失败.... 正在使用日期作为抽取方式");
            matchTimeIdentity = "(>[^<>]*?(\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+)(.*?)((\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+[^<>]*?<)";
//            matchTimeIdentity = "((\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+)(.*?)((\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+)";
//            matchTimeIdentity = "((\\d{0,4}\\s*-\\s*)?\\d{0,2}\\s*-\\s*\\d{0,2})(.*?)((\\d{0,4}\\s*-\\s*)?\\d{0,2}\\s*-\\s*\\d{0,2})";
//            matchTimeIdentity = "((\\d{0-4}\\s*[-\u4e00-\u9fa5]\\s*)?\\d{0,2}\\s*[-\u4e00-\u9fa5]\\s*\\d{0,2})(.*?)((\\d{0,4}\\s*[-\u4e00-\u9fa5]\\s*)?\\d{0,2}\\s*[-\u4e00-\u9fa5]\\s*\\d{0,2})";
            rg.setRegex(matchTimeIdentity);
            dv = rg.doValueMatch(page.getPageContent());
//            dv.printResult();
//            System.exit(1);
            if(dv.getFindNum()<1){
                System.out.println("使用日期作为抽取方案失败... ^_^加油。 请使用DOM树构造Item单元");
                System.out.println(page.getPageContent());
            }else if(dv.getFindNum()>70){
                System.out.print("使用日期抽取得到内容过多，进行双日期正则构造...");
//                matchTimeIdentity = "((\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+)(.*?((\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+).*?)((\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+)";
                matchTimeIdentity = "(>[^<>]*?(\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+)(.*?((\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+).*?)((\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+[^<>]*?<)";
                rg.setRegex(matchTimeIdentity);
                dv = rg.doValueMatch(page.getPageContent());
                if(dv.getFindNum()<3){
                    System.out.println("构造失败...!");
                    return null;
                }
                else{
                    System.out.println("构造成功...!");
                }
            }
        }else if(dv.getFindNum()>60){
//            matchTimeIdentity = "((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+)(.*?((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+).*?)((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+)";
//            matchTimeIdentity = "((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+)(.*?((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+).*?)((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+)";
            matchTimeIdentity = "(>[^<>]*?(\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+)(.*?((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+).*?)((\\d+\\s*:\\s*)?\\d+\\s*:\\s*\\d+[^<>]*?<)";
            rg.setRegex(matchTimeIdentity);
            dv = rg.doValueMatch(page.getPageContent());
            thirdTimeStruture = true;
            /////////////////////////////////////////////////////////
            System.err.println("三个时间间隔匹配模式开启");
            /////////////////////////////////////////////////////////
//            System.out.println(page.getPageContent());
        }
        System.err.println("抽取到的数量为: "+dv.getFindNum());
//        dv.printResult();
//        System.exit(1);
        int[] num =  new int[dv.getFindNum()];
        int numMaxS = -1;
        int numMax = -1;
        
        int numMaxS2 = -1;
        int numMax2 = -1;
        
        int numMaxS3 = -1;
        int numMax3 = -1;
        boolean flag_temp = false;
        ArrayList<String>  RegexString = new ArrayList<String>();  
        for(int i = 0;i<dv.getFindNum();i++){
            if(num[i] == -1){
                continue;
            }
            RegexString = reviseBlock(dv.getValueAt(i)[2]);
            if(RegexString == null){
                continue;
            }
            num[i] = 0;
            for(int j = i+1;j<dv.getFindNum();j++){
                if(num[j]== -1){
                    continue;
                }
                flag_temp = true;
                for(int k = 0;k<RegexString.size();k++){
                    if(dv.getValueAt(j)[2] == null){
                        flag_temp = false;
                        break;
                    }
                    ArrayList<String> itemTemp = reviseBlock(dv.getValueAt(j)[2]);
//                    if(ItemAnalysis.reviseBlock(dv.getValueAt(j)[2]) == null){
                    if(itemTemp == null||RegexString.size()!=itemTemp.size()||!RegexString.get(k).equals(itemTemp.get(k))){
                        flag_temp = false;
                        break;
                    }
//                    if(RegexString.size()!=reviseBlock(dv.getValueAt(j)[2]).size()){
//
//                        flag_temp = false;
//                        break;
//                    }
//                    if(!RegexString.get(k).equals(reviseBlock(dv.getValueAt(j)[2]).get(k))){
////                        System.out.println("时间规则正则内容尚未不一致性，退出构造。");
//                        flag_temp = false;
//                        break;
//                    }
//                    System.out.println("对比时间规则正则内容尚未发现不一致性。");
                    
                }
                if(flag_temp == true){
                    num[i] ++;
                    num[j] = -1;
                    if(numMax<=num[i]){
                        numMax = num[i];
                        numMaxS = i;
                    }
                    if(numMax2<=num[i]&&i!=numMaxS){
                        numMax2 = num[i];
                        numMaxS2 = i;
                    }
                    if(numMax3<=num[i] && i!=numMaxS && i!=numMaxS2){
                        numMax3 = num[i];
                        numMaxS3 = i;
                    }
                }
            }
            
        }
//        for(int i = 0; i<dv.getFindNum();i++){
//             CTs.priorPrint("num["+i+"] = "+num[i]+"dv.getValueAt"+reviseBlock(dv.getValueAt(i)[2]));
//         }
        if(dv.getFindNum() <=0||numMaxS == -1){
            System.err.println("创造板块抽取的正则过程，异常了");
            return null;
        }
        RegexString = reviseBlock(dv.getValueAt(numMaxS)[2]);
//        CTs.priorPrint("numMax  ="+ numMax +"|numMaxS = "+numMaxS+"| RegexString"+reviseBlock(dv.getValueAt(numMaxS)[2]));
        if(numMax2 != -1){
            CTs.priorPrint("numMax2 ="+ numMax2 +"|numMaxS2 = "+numMaxS2+"| RegexString"+reviseBlock(dv.getValueAt(numMaxS2)[2]));
        }
//        CTs.priorPrint("numMax3 ="+ numMax3 +"|numMaxS3 = "+numMaxS3+"| RegexString"+reviseBlock(dv.getValueAt(numMaxS3)[2]));
//        System.out.println("你为什么不现实么？2");
        System.out.println("用于作为网页ITEM构建的正则式： RegexString = "+RegexString);
        
        boolean tempFlag = true;
        String matchString = "(";
        String matchString2 = null;
        if(numMax2 != -1){
            matchString2 = "(";
        }
//        String matchString3 = "(";
        for(int i = 0; i<RegexString.size();i++){
            matchString = matchString + RegexString.get(i)+".*?";
        }
        if(numMax2 != -1){
            RegexString = reviseBlock(dv.getValueAt(numMaxS2)[2]);
            for(int i = 0; i<RegexString.size();i++){
                matchString2 = matchString2 + RegexString.get(i)+".*?";
            }
        }
//        RegexString = reviseBlock(dv.getValueAt(numMaxS3)[2]);
//        for(int i = 0; i<RegexString.size();i++){
//            matchString3 = matchString3 + RegexString.get(i)+".*?";
//        }
        matchString = matchString.substring(0, matchString.length()-3);
        matchString = matchString +")";
        if(numMax2 != -1){
            matchString2 = matchString2.substring(0, matchString2.length()-3);
            matchString2 = matchString2 +")";
        }
        
        
        
        
        if(matchString != null){
            BlockRegexStringList = new ArrayList();
            BlockRegexStringList.add(matchString);
            if(matchString2 != null){
                BlockRegexStringList.add(matchString2);
            }
            return BlockRegexStringList;
        }
        
        return null;
    }
    
    
    private static ArrayList<String> reviseBlock(String content){
        ArrayList<String> labelList = new ArrayList<String>();
        ArrayList<String> labelListMid = new ArrayList<String>();
        ArrayList<String> labelListAfter = new ArrayList<String>();
        boolean printFlag = false;
//        System.out.println(content);
        MatchRegex mr = new MatchRegex("(<[^<>]*?>)");
        DataValue dv = mr.doValueMatch(content);
//        dv.printResult();
//        System.err.println("*************************************");
        if(printFlag){
            System.err.println("初步的抽取结果....");
            dv.printResult();
            System.err.println("抽取结果完毕....");
        }
        
        int level = 0;
        String temp_label = null;
        if(dv == null){
            return null;
        }
        if(dv.getFindNum() == 0){
            return null;
        }
//        labelList.add(dv.getValueAt(0)[0]);
//        if(dv.getValueAt(0)[0].length()>=2){
//            if(dv.getValueAt(0)[0].substring(1,2).equals("/")){
//                labelListAfter.add(dv.getValueAt(0)[0]);
//                level++;
//            }
//        }
        for(int i = 0; i<dv.getFindNum();i++){
            temp_label = dv.getValueAt(i)[0];
            if(temp_label == null){
                continue;
            }
            if(temp_label.length()<2){
                continue;
            }
            if(!temp_label.substring(1,2).equals("/")){
                labelListMid.add(temp_label);
                labelList.add(temp_label);
                if(printFlag){
                    System.out.println("labelList添加了一个 = "+labelList.toString());
                    System.out.println("labelListMid添加了一个 = "+labelListMid.toString());
                }
            }
            else{
                temp_label = "<"+temp_label.substring(2,temp_label.length()-1);
                if(labelList.isEmpty()){
                    level++;
                    labelListAfter.add(dv.getValueAt(i)[0]);
                    labelListMid.clear();
                    if(printFlag){
                        System.out.println("labelListAfter添加了一个 = "+labelListAfter.toString());
                        System.out.println("labelListMid清空了");
                    }
                    continue;
                }
                if(labelList.get(labelList.size()-1).toLowerCase().indexOf(temp_label.toLowerCase())>-1){
                    labelList.remove(labelList.size()-1);
                    if(printFlag){
                        System.out.println("labelList清除了一个 = "+labelList.toString());
                    }
                    continue;
                }else{
                    while(true){
                        labelList.remove(labelList.size()-1);
                        if(labelList.isEmpty()){
                            level++;
                            labelListAfter.add(dv.getValueAt(i)[0]);
                            labelListMid.clear();
                            if(printFlag){
                                System.out.println("labelListAfter添加了一个 = "+labelListAfter.toString());
                                System.out.println("labelListMid清空了");
                            }
                            break;
                        }
                        if(labelList.get(labelList.size()-1).toLowerCase().indexOf(temp_label.toLowerCase())>-1){
                            
                            break;
                        }
                    }
                }
            }
        }
//        CTs.priorPrint("*****************labelListAfter***********************");
//        for(int i = 0; i<labelListAfter.size();i++){
//            CTs.priorPrint("labelListAfter.get("+i+") = "+labelListAfter.get(i));
//        }
//         CTs.priorPrint("*******************labelList*****************");
//        for(int i = 0; i<labelList.size();i++){
//            CTs.priorPrint("labelList.get("+i+") = "+labelList.get(i));
//        }
        if(labelListMid != null && labelList != null&&labelListMid.size()>0&&labelList.size()>0){
//        if(labelListMid != null && labelList != null&&labelListMid.size()>0&&labelList.size()>0){
            if(!labelList.get(0).equals(labelListMid.get(0))){
//                labelListMid.get(0)){
//                System.out.println("labelListMid.get(0) = "+labelListMid.get(0));
//                System.out.println("labelList.get(0) = "+labelList.get(0));
                labelList.add(0, labelListMid.get(0));
            }
//            else if(labelListMid.size()>=2&&labelList.size()>=2){
//                if(labelList.get(1)!= labelListMid.get(1)){
//                    labelList.add(0, labelListMid.get(1));
//                    labelList.add(0, labelListMid.get(0));
//                }
//            }
        }
        if(labelList != null){
            labelList.addAll(labelListAfter);
        }
        if(printFlag){
            System.out.println("labelList = "+labelList.toString());
        }
        
        MatchRegex mr_content_filter0 = new MatchRegex("=\"\\w+\"");
        MatchRegex mr_content_filter1 = new MatchRegex("\\d+");
        MatchRegex mr_content_filter6 = new MatchRegex("=#\\w+");
        MatchRegex mr_content_filter7 = new MatchRegex("=\"#\\w+\"");
        MatchRegex mr_content_filter2 = new MatchRegex("=[^>]*?\\s+");
        MatchRegex mr_content_filter_5 = new MatchRegex("\"[^<>\"]+\"");
        
        MatchRegex mr_content_filter_8 = new MatchRegex("\"(.*?)\"");
        
//      MatchRegex mr_content_filter2 = new MatchRegex("=[^>]*?\\s+");
        MatchRegex mr_content_filter4 = new MatchRegex("=[^>\\s]*?>");
//      MatchRegex mr_content_filter10 = new MatchRegex("\\{[^>]+\\}");
//      MatchRegex mr_content_filter10 = new MatchRegex("=\"\\{[^>]+\\}\"");
        String content_temp;
        
        
        for(int i = 0; i<labelList.size();i++){
//            CTs.priorPrint("labelList.get("+i+") = "+labelList.get(i));
//            labelList.get(i);
//            CTs.priorPrint("labelList.get(i) = "+labelList.get(i));
            content_temp = mr_content_filter0.doValueReplaceAll(labelList.get(i), "=\""+"\\"+"\\w+"+"\"");
//            CTs.priorPrint("content_temp0 = "+content_temp);
            
            content_temp = mr_content_filter6.doValueReplaceAll(content_temp, "=#\\\\w+");
            content_temp = mr_content_filter7.doValueReplaceAll(content_temp, "=\"#\\\\w+\"");
            content_temp = mr_content_filter1.doValueReplaceAll(content_temp, "\\"+"\\\\d+");
            
//            content_temp = mr_content_filter1.doValueReplaceAll(content_temp, "\""+"[^<>\"]+"+"\"");
//            CTs.priorPrint("content_temp1 = "+content_temp);
//            content_temp = mr_content_filter1.doValueReplaceAll(content_temp, "\\"+"\\\\d+");
//            content_temp = mr_content_filter2.doValueReplaceAll(content_temp, "=[^>]*?"+"\\"+"\\"+"\\s+");
//            CTs.priorPrint("content_temp2 = "+content_temp);
//            content_temp = mr_content_filter10.doValueReplaceAll(content_temp, "="+"\""+"{"+"[^>]+"+"}"+"\"");
//            CTs.priorPrint("content_temp10 = "+content_temp);
//            content_temp = mr_content_filter2.doValueReplaceAll(content_temp, "=[^>]*?"+"\\"+"\\"+"\\s+");
//            content_temp = mr_content_filter3.doValueReplaceAll(content_temp, "=[^>]*?"+"\\"+"\\"+"\\s+");

//            content_temp = mr_content_filter3.doValueReplaceAll(content_temp, "=[^>]*?>");
            labelList.set(i, content_temp);
        }
        if(printFlag){
            System.out.println("labelList处理完成的 = "+labelList.toString());
        }
        for(int j = 0;j<labelList.size();j++){
            String temp =  labelList.get(j);
            DataValue dv_temp = mr_content_filter_8.doValueMatch(temp);
//            dv_temp.printResult();
            if(dv_temp.getFindNum()<1){
                continue;
            }else{
                for(int i = 0;i<dv_temp.getFindNum();i++){
                   
                    if(dv_temp.getValueAt(i)[0] == null||dv_temp.getValueAt(i)[0].length() == 0){
//                        System.out.println("我擦他娘的，等于0了");
//                        System.exit(1);
                        continue;
                    }
                    if(dv_temp.getValueAt(i)[0].length()>3){
                        String tempReplace = dv_temp.getValueAt(i)[0];
//                        System.out.println("labelList = "+labelList.get(j));
                        labelList.set(j, labelList.get(j).substring(0,labelList.get(j).indexOf(tempReplace))+"[^<>]*?"+ labelList.get(j).substring(labelList.get(j).indexOf(tempReplace)+tempReplace.length()));
//                        System.out.println("我擦他娘的，大于3了");
//                        System.out.println("labelList = "+labelList.get(j));
                        continue;
                    }
                    
                }
            }
        }
        
//        System.exit(1);
//        System.exit(1);
//        CTs.priorPrint("***********************************************");
        
        if(!labelList.isEmpty())
            return labelList;
        System.err.println("未能建立通过时间方式的标签序列");
        return null;
        
    }
    
    
    public synchronized static ArrayList<Item> getItemSetsWithElement(Page page, String matchRegexString){
        boolean tempFlag = true;
        boolean blockPrintFlag = true;
        DataValue dv;
        String title;
        MatchRegex mr_title = new MatchRegex("<title>(.*?)</title>");
        title = mr_title.doContentMatch(page.getPageContent());
//        matchString3 = matchString3.substring(0, matchString3.length()-3);
//        matchString3 = matchString3 +")";
       System.out.println("Use matchString is = \""+matchRegexString+"\"");
       MatchRegex mr_items = new MatchRegex(matchRegexString);
       dv = mr_items.doValueMatch(page.getPageContent());
       if(dv.getFindNum()<=0){
            System.out.println("DV = null");
       }else{
            System.out.println("寻找到的抽取数量为 dv.findNum = "+dv.getFindNum());
        }
       if(blockPrintFlag){
//           dv.printResult();
       }

        ArrayList<Item> items = new ArrayList<Item>();
        if(dv != null){
            for(int i = 0; i<dv.getFindNum();i++){
                Item item = null;
                try {
                    item = AnalysisFunction.getElementSet(dv.getValueAt(i)[0], page.getPageUrl(), title);
//                    item.print();
                } catch (Exception ex) {
                    Logger.getLogger(AnalysisFunction.class.getName()).log(Level.SEVERE, null, ex);
                    item = null;
//                    ;
                }
//                item.print();
                if(item != null){
//                    item.print();
                    items.add(item);
//                    elementCountInt[item.getElementNum()]++;
                }
            }
        }
        
        
        if(items == null||items.isEmpty()){
                System.err.println("使用时间过程，抽取的内容为空");
                return null;
         }
        if(items.size()<1){
                System.err.println("使用时间过程，抽取的内容为空");
                return null;
         }
        
        return items;
    }
    
    
    public static Item getElementSet(String itemContent, String url, String webSite) throws Exception{
//        MatchRegex 
        String  String_filtertemp = null;
        int contentIndex = 3;
        int hit_reply_inSameElementFlag = -1;
        boolean releaseUserFlag = false;
        int urlIndex = 2;
        int hrefIndex = 1;
        MatchRegex mr_UrlElement ;
//        mr_UrlElement = new MatchRegex("(a[^<>]+(href)([^<>]*))?>([^<>]+)<");// 6月18日之前的版本 无法处理<a ><font>
        {
            mr_UrlElement = new MatchRegex("(a[^<>]+(href)([^<>]*)(>\\s*?<span[^<>]*?)?(>\\s*?<font[^<>]*?>)?)?>([^<>]+)<");
            contentIndex++;
            contentIndex++;
        }// 6月26尝试处理 <font>
        
//        MatchRegex mr_UrlElement = new MatchRegex("(a[^>]+(href)([^>]*)(>\\s*<font[^<>]*>\\s*<u)?)?>([^>]+)<"); //4//未能成功
//        CTs.priorPrint(itemContent);
//        MatchRegex mr_UrlElement = new MatchRegex("(<a\\s*(href)\\s*=([^<>]*)>\\s*<\\w+>([^>]+)<)"); //4
//        System.out.println(itemContent);
//        System.out.println("********************************************");
        DataValue dv = mr_UrlElement.doValueMatch(itemContent);
//        dv.printResult();
        Item item = new Item();
        String digit_reply_and_visit = null;
        int timeNum = 0;
        int urlElementNum = 0;
        int type = -1;
        int urlElementIndex_1 = -1;
        int urlElementIndex_2 = -1;
        int urlElementIndex_3 = -1;
        int urlElementIndex_4 = -1;
        
        int timeNumIndex_1 = -1;
        int timeNumIndex_2 = -1;
        int timeNumIndex_3 = -1;
        int timeNumIndex_4 = -1;
        if(dv.getFindNum() <= 0){
            return null;
        }
        for(int i = 0; i< dv.getFindNum(); i++){
//            System.out.println("i=         " + i);
            Element element  =  new Element(); 
            
            if(dv.getValueAt(i)[hrefIndex] != null){
                
               
//                String_filtertemp = dv.getValueAt(i)[3];// 6月18日之前的版本 无法处理<a ><font>
                String_filtertemp = dv.getValueAt(i)[contentIndex];
                element.setElementContent(String_filtertemp);
//                CTs.priorPrint("String_filtertemp = "+String_filtertemp );
                String_filtertemp = dv.getValueAt(i)[urlIndex];// 6月18日之前的版本 无法处理<a ><font>
//                CTs.priorPrint(String_filtertemp);
                element.setUrl(AnalysisFunction.parentUrlFilter(url,String_filtertemp));
                if(element.getUrl() == null){
                    timeNum++;
//                    element.setElementType(CommonStaticConstant.SingleElement_TYPE);
                }else{
                    urlElementNum ++;
//                    element.setElementType(CommonStaticConstant.Url_TYPE);
                }
                element.setElementType(-1);
                
//                CTs.priorPrint(AnalysisFunction.parentUrlFilter(url,String_filtertemp));
            }else{
                
//                String_filtertemp = dv.getValueAt(i)[3];// 6月18日之前的版本 无法处理<a ><font>
                String_filtertemp = dv.getValueAt(i)[contentIndex];// 6月18日之前的版本 无法处理<a ><font>
//                CTs.priorPrint("String_filtertemp = "+String_filtertemp );
//                CTs.priorPrint("contentIndex = "+contentIndex );
                if(String_filtertemp != null){
                    element.setElementContent(String_filtertemp);
                    
                    element.setUrl(null);
                    if(element.getElementContent().contains(":")||element.getElementContent().contains("：")||new MatchRegex("(\\d+\\s*[-.]\\s*)?\\d+\\s*[-.]\\s*\\d+").findMatch(element.getElementContent())){
                        timeNum++;
//                        element.setElementType(CommonStaticConstant.TimeDate_TYPE);
                    }else{
//                        element.setElementType(CommonStaticConstant.SingleElement_TYPE);
                    }
                    element.setElementType(-1);
                }
                
            }
//            element.print();
            
//            CTs.priorPrint("\n\n************************处理之前***********************");
//            element.print();
//            element = element.elemenAnalysis(type);
             
            element = elementAnalyzer(element,type,releaseUserFlag);
            
//            CTs.priorPrint("\n\n************************处理之中***********************");
//            element.print();
//            CTs.priorPrint("\n\n************************处理之后***********************");
            if(element != null&&element.getElementType() == StaticHelper.Url_ReleaseUser_TYPE){
                releaseUserFlag = true;
            }
            if(element == null){
//                element.print();
                 continue;
            }
//            
           
//            element.print();
//            CTs.priorPrint("************************处理之后***********************");
//            element.print();
//            CTs.priorPrint("************************处理结束***********************");
            //综合过滤器
            //发现楼
            
            
            if(element.getElementContent()!= null){
                if((element.getElementType() == StaticHelper.Digit_TYPE||element.getElementType() == StaticHelper.Url_Digit_TYPE)&&item.getLastElement()!= null&&(item.getLastElement().getElementType() == StaticHelper.Digit_TYPE||item.getLastElement().getElementType() == StaticHelper.SingleLabel_TYPE||item.getLastElement().getElementType() == StaticHelper.Url_Digit_TYPE)){
//                    System.out.println("element.getElementContent() = "+element.getElementContent());
//                    System.out.println("item.getLastElement().getElementContent()) = "+item.getLastElement().getElementContent());
                    if(item.getLastElement().getElementType() == StaticHelper.SingleLabel_TYPE&&item.getElementNum()>1&&item.getLastElement().getElementContent().contains("/")&&item.getLastElement().getElementContent().length()<4){
                        item.removeLast();
                    }
                    if(new MatchRegex("\\d+").doValueReplaceAll(item.getLastElement().getElementContent(), "").length() == 0&&item.getLastElement().getElementContent().length()<8&&element.getElementContent().length()<8){
//                        System.out.println("element.getElementContent() = ("+element.getElementContent()+")");
//                        System.out.println("item.getLastElement().getElementContent()) = ("+item.getLastElement().getElementContent()+")");
                        if(Integer.valueOf(element.getElementContent())>=Integer.valueOf(item.getLastElement().getElementContent())){                        
                            if(Integer.valueOf(element.getElementContent())-Integer.valueOf(item.getLastElement().getElementContent())!=1&&Integer.valueOf(element.getElementContent())-Integer.valueOf(item.getLastElement().getElementContent())!=-1){
                                item.getLastElement().setElementType(StaticHelper.ReplyNum_TYPE);
                                element.setElementType(StaticHelper.HitsNum_TYPE);
                                item.add(element);
                                continue;
                            }
                        }else{
                            if(Integer.valueOf(element.getElementContent())-Integer.valueOf(item.getLastElement().getElementContent())!=1&&Integer.valueOf(element.getElementContent())-Integer.valueOf(item.getLastElement().getElementContent())!=-1){
                                item.getLastElement().setElementType(StaticHelper.HitsNum_TYPE);
                                element.setElementType(StaticHelper.ReplyNum_TYPE);
                                item.add(element);
                                continue;
                            }
                        }
                        
                    }
                    
                }
                digit_reply_and_visit = new MatchRegex("\\d+\\s*?[\\s/|]\\s*?\\d+").doContentMatch(element.getElementContent());
                if((element.getElementType() == -1||element.getElementType() == StaticHelper.SingleElement_TYPE)&&digit_reply_and_visit != null&&digit_reply_and_visit.contains(":")&&!digit_reply_and_visit.contains("-")&&!digit_reply_and_visit.contains(".")&&element.getUrl() == null){
//                if((element.getElementType() == -1||element.getElementType() == CommonStaticConstant.SingleElement_TYPE)&&digit_reply_and_visit != null&&digit_reply_and_visit.contains(":")&&!digit_reply_and_visit.contains("-")&&!digit_reply_and_visit.contains(".")&&element.getUrl() == null){
                    String beginNumString = new MatchRegex("\\d+").doContentMatch(digit_reply_and_visit);
                    String endNumString = new MatchRegex("\\d+").doContentMatch(new MatchRegex("\\d+").doValueReplaceFirst(digit_reply_and_visit, ""));
                    if(beginNumString.length()<9&&endNumString.length()<9){
//                        element.setElementContent(new MatchRegex("\\d+").doContentMatch(digit_reply_and_visit));
                        element.setElementContent(beginNumString);
    //                    element.setElementType(CommonStaticConstant.HitsNum_TYPE);
                        item.add(element);
                        element = new Element();
//                        element.setElementContent(new MatchRegex("\\d+").doContentMatch(new MatchRegex("\\d+").doValueReplaceFirst(digit_reply_and_visit, "")));
                        element.setElementContent(endNumString);
    //                    element.setElementType(CommonStaticConstant.ReplyNum_TYPE);
                        item.add(element);
    //                    int begin = Integer.valueOf(item.getElement(item.getElementNum()-1).getElementContent());
    //                    System.out.println("item.getLastElement().getElementContent()) = "+item.getLastElement().getElementContent());
    //                    int end = Integer.valueOf(item.getElement(item.getElementNum()-2).getElementContent());
    //                    System.out.println("element.getElementContent() = "+element.getElementContent());

                        if(Integer.valueOf(item.getElement(item.getElementNum()-1).getElementContent())<Integer.valueOf(item.getElement(item.getElementNum()-2).getElementContent())){
                            item.getElement(item.getElementNum()-2).setElementType(StaticHelper.HitsNum_TYPE);
                            item.getElement(item.getElementNum()-1).setElementType(StaticHelper.ReplyNum_TYPE);
                        }else{
                            item.getElement(item.getElementNum()-2).setElementType(StaticHelper.ReplyNum_TYPE);
                            item.getElement(item.getElementNum()-1).setElementType(StaticHelper.HitsNum_TYPE);
                        }
                        continue;
                    }
                }
                if(element.getElementContent().length() == 1){
                    if(element.getElementContent().equals("[")||element.getElementContent().equals("...")||element.getElementContent().equals("]")||element.getElementContent().equals("【")||element.getElementContent().equals("】")){
                        if(element.getUrl() == null||element.getUrl().length() <2){
                            continue;
                        }

                    }
                    if(element.getUrl()!= null){
                        if(new MatchRegex("\\d+").findMatch(element.getElementContent())){
                            element.setElementType(StaticHelper.Url_Digit_TYPE);
                            item.add(element);
                            continue;
                        }
                    }
                }
                if(element.getElementContent().contains("回复")){
                    int index_1 = element.getElementContent().indexOf("回复");
                    String temp = new MatchRegex("回复.{0,3}\\d{1,15}").doContentMatch(element.getElementContent());
//                    System.out.println("temp = "+temp);
                    
                    if(temp!=null&&(new MatchRegex("\\d+").findMatch(temp))){
                        Element elmenet_temp = new Element();
                        elmenet_temp.setElementContent(new MatchRegex("\\d+").doContentMatch(temp));
                        elmenet_temp.setElementType(StaticHelper.ReplyNum_TYPE);
                        item.add(elmenet_temp);
                        temp = new MatchRegex("回复.{0,3}\\d{1,15}").doValueReplaceFirst(element.getElementContent(),"");
                        element.setElementContent(temp);
                        hit_reply_inSameElementFlag = 1;
                    }
                    
                }
                if(element.getElementContent().contains("访问")){
                    int index_1 = element.getElementContent().indexOf("访问");
                    String temp = new MatchRegex("访问.{0,3}\\d{1,15}").doContentMatch(element.getElementContent());
//                    System.out.println("temp = "+temp);
                    
                    if(temp!=null&&new MatchRegex("\\d+").findMatch(temp)){
                        Element elmenet_temp = new Element();
                        elmenet_temp.setElementContent(new MatchRegex("\\d+").doContentMatch(temp));
                        elmenet_temp.setElementType(StaticHelper.HitsNum_TYPE);
                        item.add(elmenet_temp);
                        temp = new MatchRegex("访问.{0,3}\\d{1,15}").doValueReplaceFirst(element.getElementContent(),"");
                        element.setElementContent(temp);
                        if(hit_reply_inSameElementFlag<0){
                            hit_reply_inSameElementFlag = 1;
                        }
                    }
                    
                }
                if(hit_reply_inSameElementFlag == 1){
                    if(element.getElementContent().length()<35){
                            String tempString = timeFilter(element.getElementContent(),-1);
                            if(tempString == null){
                              ;
                            }else{
                                element.setElementType(StaticHelper.ReleaseTime_TYPE);
                                
                            }
                         }
                }
                if(element.getElementContent().contains("楼")||element.getElementContent().contains("帖")||element.getElementContent().contains("#")){
//                     System.err.println("发现楼了,我操!真他妈的爽啊 element.getElementContent() = ("+element.getElementContent()+")");
//                    element.print();
//                    System.out.println("item.getElement(item.getElementNum()) = "+item.getElement(item.getElementNum()-1).getElementContent());
//                    int index_floor = element.getElementContent().indexOf("楼");
//                    if(index_floor>1){
//                        
//                    }
//                    System.out.println("发现楼了,我操!真他妈的爽啊");
                    
                    if(new MatchRegex("\\d+\\s{0,4}\"?[\u4e00-\u9fa5]").findMatch(element.getElementContent())||new MatchRegex("\\d+\\s*{0,4}\"?#").findMatch(element.getElementContent())){
                        element.setElementType(StaticHelper.reply_FloorNum_TYPE);
                        item.add(element);
                    }
                    
                    else if(item.getElementNum()>0){
                        if(item.getElement(item.getElementNum()-1).getElementType() == StaticHelper.Digit_TYPE&&element.getElementContent().length()<5){
                            item.getElement(item.getElementNum()-1).setElementType(StaticHelper.reply_FloorNum_TYPE);
                        }
                    }
                    else{
                        item.add(element);
                    }
                }else{
                    item.add(element);
                }
                
            }
        }
//        System.err.println("...........Item出现了内容不全的情况");
//        item.print();
        if(timeNum == 0||urlElementNum == 0){
            System.out.print("timeNum = "+timeNum+"   urlElementNum = "+urlElementNum);
            System.err.println("...........Item出现了内容不全的情况");
            item.print();
            return item;
        }

//        if(urlElementNum>6){
//            System.err.println("Item出现了冗余内容，该Item自动删除");
//            return null;
//        }
        Element e_title = new Element();
        e_title.setElementContent(webSite);
        e_title.setElementType(StaticHelper.Parent_Title_TYPE);
        item.add(e_title);
        item.print();
        return item;
    }
    
    public synchronized static Element elementAnalyzer(Element element,int type,boolean releaseUserFlag){//String content, int type
        String  afterContent = null;
        String  urlTemp = null;
        afterContent  = element.getElementContent();
        if(element.getUrl() != null){
            urlTemp = element.getUrl().toLowerCase();
        }else{
            urlTemp = null;
        }
//        DataValue dv_DataIdentity;
        if(urlTemp!=null){
            if(urlTemp.length() < 0){
                urlTemp = null;
            }
        }

        //如果模板存在怎么样？
        
        //如果模板不存在继续往下做
        MatchRegex mr = new MatchRegex("[\\s　]+");
//        mr.setRegex("[\\s　]+");
        if(type != -1){
           
        }
        
        if(element.getElementType()<0){
            
            afterContent = elementStringFilter(afterContent,-1);
            
//            System.out.println("afterContent = ("+afterContent+")");
            if(afterContent!=null&&afterContent.length()>0){
                
//                System.out.println("afterContent = ("+afterContent+")");
                if(urlTemp != null){
                    type = StaticHelper.Url_TYPE;
                     mr.setRegex("\\d+\\s*[-.]\\s*\\d+");
                    if((urlTemp.contains("author")||urlTemp.contains("writer")||urlTemp.contains("uid=")||urlTemp.contains("-uid-")||urlTemp.contains("username=")||urlTemp.contains("userid=")||urlTemp.contains("-username-")||urlTemp.contains("user="))&&type ==StaticHelper.Url_TYPE){
                        if(!releaseUserFlag){
                            type = StaticHelper.Url_ReleaseUser_TYPE;
                        }
                        else{
                            type = StaticHelper.Url_ReplyUser_TYPE;
                        }
                    }
//                    if(urlTemp.contains("writer")&&type ==CommonStaticConstant.Url_TYPE){
//                        type = CommonStaticConstant.Url_ReleaseUser_TYPE;
//                    }
                    if((urlTemp.contains("thread")||urlTemp.contains("content") || urlTemp.contains("post"))&&type ==StaticHelper.Url_TYPE){
                        type = StaticHelper.Url_Title_TYPE;
                    }
//                    if(new MatchRegex("\\d+").findMatch(element.getElementContent())){
//                            element.setElementType(CommonStaticConstant.Url_Digit_TYPE);
//                            item.add(element);
//                            continue;
//                        }
                    
                    if((afterContent.contains(":")||afterContent.contains("：")||mr.findMatch(afterContent))){
//                        
//                       if(afterContent.indexOf(":")>0||mr.findMatch(afterContent)){
                        if(afterContent.length()<27){
                            afterContent = timeFilter(afterContent,type);
                            if(afterContent == null){
                               afterContent  = element.getElementContent();
                            }else{
                                type = StaticHelper.Url_DateTime_TYPE;
//                                System.out.println("afterContent = "+afterContent);
//                                System.exit(1);
                            }
                        }
                    }
                    mr.setRegex("(\\d+\\s{0,2}年\\s{0,2})?\\d+\\s{0,2}月\\s{0,2}(\\d+(日)?)?");
                    if(mr.findMatch(afterContent)){
                        
                         if(afterContent.length()<27){
                             afterContent = timeFilter(afterContent,type);
                            if(afterContent == null){
                               afterContent  = element.getElementContent();
                            }else{
                                type = StaticHelper.Url_DateTime_TYPE;
//                                System.out.println("afterContent = "+afterContent);
//                                System.exit(1);
                            }
                         }
                    }
                    mr.setRegex("\\d+");
                    if(mr.doValueReplaceAll(afterContent, "").length() == 0){
                        if(afterContent.length()<27){
                            type = StaticHelper.Url_Digit_TYPE;
                        }
                    }
                }else{
//                  进行时间过滤
                    mr.setRegex("\\d+\\s*[-.]\\s*\\d+");
//                    if((afterContent.indexOf(":")>0||afterContent.contains("：")||mr.findMatch(afterContent))){
                    if((afterContent.contains(":")||afterContent.contains("：")||mr.findMatch(afterContent))){
//                       if(afterContent.indexOf(":")>0||mr.findMatch(afterContent)){
                        if(afterContent.length()<30){
                            afterContent = timeFilter(afterContent,type);
                            if(afterContent == null){
                              type  = -1;   
                              afterContent  = element.getElementContent();
//                              afterContent = elementStringFilter(afterContent,-1);
                            }else{
                                type = StaticHelper.ReleaseTime_TYPE;


                            }
                        }

                        
                    }
                    mr.setRegex("(\\d+\\s{0,2}年\\s{0,2})?\\d+\\s{0,2}月\\s{0,2}(\\d+(日)?)?");
                    if(mr.findMatch(afterContent)){
//                        System.out.println("afterContent = "+afterContent);
//                        System.exit(1);
                         if(afterContent.length()<30){
                             afterContent = timeFilter(afterContent,type);
                            if(afterContent == null){
                               afterContent  = element.getElementContent();
                            }else{
                                type = StaticHelper.ReleaseTime_TYPE;
//                                System.out.println("afterContent = "+afterContent);
//                                System.exit(1);
                            }
                         }
                    }
                    if(type == -1){
                        
                        //进行普通字符串过滤
                        type = StaticHelper.SingleElement_TYPE;
////                        CTs.priorPrint("afterContent = ("+afterContent+")");
////                        CTs.priorPrint("*************************************");
////                        CTs.priorPrint("afterContent.length = "+afterContent.length());
//                        if(afterContent.length()>0){
//                            while(afterContent.substring(0,1).equals(blankFilter)||afterContent.substring(0,1).equals(blankFilterTab)||afterContent.substring(0,1).equals(blankFilterChinese)){
//                                afterContent = afterContent.substring(1);
//                                if(afterContent.length()==0){
//                                    break;
//                                }
//                            }
//                        }
////                        CTs.priorPrint("afterContent.length = "+afterContent.length());
//                        if(afterContent.length()>0){
//                            while(afterContent.endsWith(blankFilter)||afterContent.substring(0,1).equals(blankFilterTab)||afterContent.endsWith(blankFilterChinese)){
//                                afterContent = afterContent.substring(0,afterContent.length()-1);
//                                if(afterContent.length()==0){
//                                    break;
//                                }
//                            }
//                        }
                    }

                    mr.setRegex("[\\s　]+");
                    afterContent = mr.doValueReplaceAll(afterContent, " ");
                    if(afterContent.length() == 1&& afterContent.equals(" ")){
                        afterContent = null;
                    }
//                    CTs.priorPrint("2 afterContent.length = "+afterContent.length());
                    
                    if(afterContent != null){
                        mr.setRegex("&nbsp(;)?");
                        afterContent = mr.doValueReplaceAll(afterContent, "");
                    }
                    
                    if(afterContent != null){
                        mr.setRegex("&[\\w\\d]{0,2}quo;");
                        afterContent = mr.doValueReplaceAll(afterContent, "");
                    }
//                    &gt;
                    
                    if(afterContent != null){
                        mr.setRegex("&gt(;)?");
                        afterContent = mr.doValueReplaceAll(afterContent, "");
                    }
                    
//                    CTs.priorPrint("3 afterContent.length = "+afterContent.length());
//                    System.err.println("************几小时前*****************\nafterContent = "+afterContent);
                    mr.setRegex("(\\d+)");
                    if(afterContent != null&&(type == -1||type == StaticHelper.Url_TYPE||type == StaticHelper.Url_ReplyUser_TYPE||type == StaticHelper.Url_ReleaseUser_TYPE||type == StaticHelper.Url_Title_TYPE||type == StaticHelper.SingleElement_TYPE)){
                        String  digit_String_temp = null;
                        digit_String_temp = mr.doContentMatch(afterContent);
                        if(digit_String_temp !=null){
                            if((digit_String_temp.length()+3)>=afterContent.length()){
                                type = StaticHelper.Digit_TYPE;
                            }
                        }
                        //用来鉴别时间类型的
                        mr.setRegex("小时(前)?");
//                        digit_String_temp = mr.doContentMatch(afterContent);
                        if(mr.findMatch(afterContent)){
                            digit_String_temp = mr.doValueReplaceFirst(afterContent, "");
                            if(digit_String_temp.length()<4){
                                type = StaticHelper.TimeDate_TYPE;
                            }
                        }
                        
                        mr.setRegex("分钟(前)?");
                        if(mr.findMatch(afterContent)){
                            digit_String_temp = mr.doValueReplaceFirst(afterContent, "");
                            if(digit_String_temp.length()<4){
                                type = StaticHelper.TimeDate_TYPE;
                            }
                        }
//                        System.out.println("type = "+type+"\n********************************************");
                      
                    }
                    if(afterContent != null){
                        mr.setRegex("[./]+");
//                        System.out.println("afterContent = "+afterContent);
                        if(mr.doValueReplaceAll(afterContent, "").length() == 0){
                           type = StaticHelper.SingleLabel_TYPE;
                        }
                    }
                    
                    

//                    if(dv != null){
//                        if(dv.getFindNum()==1){
////                            CTs.priorPrint("dv.getValueAt(0)[0] = "+dv.getValueAt(0)[0].length());
////                            CTs.priorPrint("Second afterContent ="+afterContent);
////                            CTs.priorPrint("afterContent = "+afterContent.length());
//                            if(afterContent!=null){       
//                                if(dv.getValueAt(0)[0].length() == afterContent.length()){
//                                    type = CommonStaticConstant.Digit_TYPE;
//                                }
//                            }
//                        }
//                    }
                }
                    
            }
            else{
                return null;
            }
            
            
            
            
        }
//        CTs.priorPrint("4 afterContent.length = "+afterContent.length());
        if(afterContent == null){
            
            element.setElementType(-1);
            element.setElementContent(null);
            element.setUrl(null);
        }else if(afterContent.length()<1){
            element.setElementType(-1);
            element.setElementContent(null);
            element.setUrl(null);
            
        }else{
            element.setElementType(type);
            element.setElementContent(afterContent);
            element.setUrl(urlTemp);
            
        }
//        element.print();
//       
       return element;
   
    }
    
     public synchronized static String elementStringFilter(String content, int type){
        String afterContent  = content;
        MatchRegex mr = new MatchRegex("[\\s　]+");
        String blankFilter = " ";
        afterContent = mr.doValueReplaceAll(afterContent, " ");
//        System.err.println("afterContent  = ("+afterContent+")");
//        mr =  new MatchRegex("[\\s　]+");
        mr =  new MatchRegex("\\s+");
        if(type<0){
            if(afterContent.length()>0){
//                if(afterContent.length()>0){
//                    while(afterContent.substring(0,1).equals(blankFilter)||afterContent.substring(0,1).equals(blankFilterChinese)){
               while(afterContent.length()>0&&afterContent.substring(0,1).equals(blankFilter)){
//               while(afterContent.length()>0&&(afterContent.substring(0,1).equals(blankFilter)||afterContent.substring(0,1).equals(blankFilterChinese))){
//                   afterContent = mr.doValueReplaceFirst(afterContent, "");
//                   System.out.println("1111我要开始 删除了啊呀！("+afterContent+")");
                    afterContent = afterContent.substring(1);
//                   System.out.println("2222我要开始 删除了啊呀！("+afterContent+")");
                    if(afterContent.length()==0){
                        break;
                    }
                   
                }
//                System.err.println("afterContentFirst  = ("+afterContent+")");
//               System.out.println("********************* afterContent  = ("+afterContent+")");
//                if(afterContent.length()>0){
                while(afterContent.length()>0&&afterContent.endsWith(blankFilter)){
//                while(afterContent.length()>0&&(afterContent.endsWith(blankFilter)||afterContent.endsWith(blankFilterChinese))){
                   
//                    System.out.println("3333我要开始 删除了啊呀！("+afterContent+")");
                    afterContent = afterContent.substring(0,afterContent.length()-1);
//                    System.out.println("4444我要开始 删除了啊呀！("+afterContent+")");
                    if(afterContent.length()==0){
                        break;
                    }
                    
                }
//                System.err.println("afterContentEnd  = ("+afterContent+")");
            }
            else{
                return null;
            }
            
//            afterContent = mr.doValueReplaceAll(afterContent, " ");
            mr =  new MatchRegex("&nbsp(;)?");
            afterContent = mr.doValueReplaceAll(afterContent, "");
            
        }
        if(afterContent.length()<1){
            return null;
        }
//        System.err.println("**************************afterContent = ("+afterContent+")");
//        System.err.println();
        return afterContent;
   
    }
    
     public synchronized static String timeFilter(String content, int type){
        String afterContent  = " "+content;
        String dateContent = new String();
        String timeContent = new String();
        MatchRegex mr = new MatchRegex("[^0-9]+");
//        mr.setRegex();
//        mr =  new MatchRegex("([^0-9]*)[0-9]+.*?");
        if(type<0||type == StaticHelper.Url_TYPE ||type ==  StaticHelper.TimeDate_TYPE){
            afterContent = mr.doValueReplaceFirst(afterContent, "");
//            CTs.priorPrint("afterContent =============="+afterContent);
//            mr.setRegex("[^0-9:：]+");
//            afterContent = mr.doValueReplaceAll(afterContent, "-");
//            /////////////////////////////////////////
//            MatchRegex mr1 = new MatchRegex("(\\d+)");
//            CTs.priorPrint("afterContent = "+afterContent);
//            DataValue dv = mr1.doValueMatch(afterContent);
//            dv.printResult();
//            ////////////////////////////////////////////////
//            CTs.priorPrint("afterContent =============="+afterContent);
//            mr.setRegex("(\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+");
            mr.setRegex("(\\d+\\s*[-.]\\s*)?\\d+\\s*[-.]\\s*\\d+");
            
            dateContent = mr.doContentMatch(afterContent);
            if(dateContent!=null){
                if(dateContent.length() == 0){
                    dateContent =null;
                }
            }
            if(dateContent == null){
                 mr.setRegex("(\\d+\\s{0,2}年\\s{0,2})?\\d+\\s{0,2}月\\s{0,2}(\\d+(日)?)?");
                 dateContent = mr.doContentMatch(afterContent);
                 if(dateContent==null||dateContent.length() == 0){
                    dateContent =null;
                 }
            }
//            System.out.println("dateContent = "+dateContent);
//            mr.setRegex("((\\d+\\s*-\\s*)?\\d+\\s*-\\s*\\d+)");
//             dv = mr.doValueMatch(afterContent);
//            if(dv.getFindNum() > 0){
//                timeContent = dv.getValueAt(0)[0];
//            }
//            dv.printResult();
            mr.setRegex("(\\d+\\s*[:：]\\s*)?\\d+\\s*[:：]\\s*\\d+");
//            mr.setRegex("((\\d+\\s*[:：]\\s*)?\\d+\\s*[:：]\\s*\\d+)");
            timeContent = mr.doContentMatch(afterContent);
            if(dateContent != null){
                if(timeContent == null){
                    timeContent = dateContent;
                }else{
                    timeContent = dateContent+" "+timeContent;
                }
            }
//            System.out.println("timeContent = "+timeContent);
//            CTs.priorPrint("\n***************************************************");
//            dv.printResult();
//            CTs.priorPrint("*****************************************************");
//            if(dv.getFindNum() > 0){
//                timeContent = timeContent + " "+dv.getValueAt(0)[0];
//            }
//            CTs.priorPrint(timeContent);
        }
        
        if(timeContent == null||timeContent.length()==0){
            return null;
        }
        return timeContent;
    }
     
    
}
