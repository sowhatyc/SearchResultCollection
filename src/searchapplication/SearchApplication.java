/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package searchapplication;

import CommonTools.FileStorage;
import CommonTools.StaticHelper;
import SearchResultsCollection.FillMissingElement;
import SearchResultsCollection.ResultCollectionTool;
import SearchResultsCollection.SearchDetail;
import SearchResultsCollection.WebCrawler;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class SearchApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
           
//        String url = "http://sou.autohome.com.cn/luntan?q=";
//        String content = "国庆 堵车";
////        url += content;
//        try {
//            String nurl = java.net.URLEncoder.encode(content, "gbk");
//            System.out.println(url + nurl);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(SearchApplication.class.getName()).log(Level.SEVERE, null, ex);
//        }
            
            // TODO code application logic here
            /*
            ResultCollectionTool tianyaTool = new ResultCollectionTool();
            String resultSavePath = "18GlobalSearchResult_ST.xls";
            tianyaTool.setResultUrlsFilePath(resultSavePath);
//            String searchUrl = "http://bbs.tianya.cn/list.jsp?item=free&order=8&k=";
            String searchUrl = "http://www.tianya.cn/search/bbs?q=&pn=1&s=8";
//            String searchUrl = "http://bbs.ifeng.com/search.php?srchtxt=&srchuname=&srchcat=&srchrange=title&srchfrom=&orderby=replies&advancesearch=1&page=1";
            ArrayList<String> searchContent = new ArrayList<String>();
//            searchContent.add("国庆 堵车");
//            searchContent.add("国庆 高速");
//            searchContent.add("国庆 公路");
//            searchContent.add("国庆 交通");
//            searchContent.add("国庆 免费");
            searchContent.add("十八大");
//            searchContent.add("国庆 出行");
            FileStorage.addCreaatedFiles(resultSavePath);
//            ArrayList<SearchDetail> toSearchUrl = new ArrayList<SearchDetail>();
//            toSearchUrl.add(new SearchDetail("http://sou.autohome.com.cn/luntan?q=", ""));
//            toSearchUrl.add(new SearchDetail("http://search.tianya.cn/ns?tn=sty&rn=10&pn=0&s=10&pid=worldlook&f=0&h=1&ma=1&l=utf-8&q=", "天涯国际观察按回复数搜索"));
//            toSearchUrl.add(new SearchDetail("http://search.tianya.cn/ns?tn=sty&rn=10&pn=0&s=10&pid=free&f=0&h=1&ma=1&l=utf-8&q=", "天涯天涯杂谈按回复数搜索"));
//            toSearchUrl.add(new SearchDetail("http://search.tianya.cn/ns?tn=sty&rn=10&pn=0&s=10&pid=20&f=0&h=1&ma=1&l=utf-8&q=", "天涯钓鱼岛_网上谈兵按回复数搜索"));

            //////////堵车///////////////
//            http://sou.autohome.com.cn/luntan?q=%B9%FA%C7%EC+%B6%C2%B3%B5&pvareaid=100834
//            toSearchUrl.add(new SearchDetail("http://sou.autohome.com.cn/luntan?q=", ""));
            ///////// 堵车结束//////////////////////
            for(String content : searchContent){
                tianyaTool.setUrl(searchUrl, "全文点击数");
                tianyaTool.setSearchContent(content);
//                tianyaTool.setSearchContent("钓鱼岛");
                tianyaTool.getResultUrls();
            }
            
           *
           * 
           */
        
        
        //**************************补全Excel**UTF-8******************
        /*
        String resultSavePath = "E:/data/18TY_domain/天涯领域数据.xls";
        FillMissingElement fme = new FillMissingElement();
        int sheetNum = 9;//8
        int startRow = 755;//986
        ArrayList<String> urlList = fme.getFileURLList(resultSavePath, sheetNum);
        fme.setSavePath(resultSavePath);
        FileStorage.addCreaatedFiles(resultSavePath);
//        fme.setMissingElementProperty("<title>(.*?)</title>", 5, true);
//        fme.setMissingElementProperty("<title>[^-]+-([^-]+)-[^<]+</title>", 6, true);
//        fme.setMissingElementProperty("<title>[^_]+_([^_]+)_[^<]*", 6, true);
//        fme.setMissingElementProperty("点击：<span[^>]*>(\\d+)</span>", 2, true);
        fme.setMissingElementProperty("<span>点击：(\\d+)\\s*</span>", 7, true, "visit_count", 0);
//        fme.setMissingElementProperty("回复：<span[^>]*>(\\d+)</span>", 3, true);
//        fme.setMissingElementProperty("<span>回复：(\\d+)\\s*</span>", 3, true);
//        fme.setMissingElementProperty("发表于：<span[^>]*>([^<]+)</span>", 1, true);
//        fme.setMissingElementProperty("<span>时间：([^<]+)</span>", 1, true);
//        fme.setMissingElementProperty("<div class=[^<>]*?>[^<>]*?[u4e00-u9fa5]{1,4}[^\\d]*?(\\d+)[^<>]*?回复[u4e00-u9fa5]{0,2}[^<>]*?\\d+[^\\d]*?</div>", 3, true);
////        fme.setMissingElementProperty("<li class=[^<>]*?><a href=\"([^<>]*?)\" target=[^<>]*? class=[^<>]*? title=[^<>]*? xname=[^<>]*?>[^<>]*?</a>", 7, false);
//        fme.setMissingElementProperty("<div class=[^<>]*?>[^<>]*?[u4e00-u9fa5]{1,5}[^\\d]*?\\d+[^<>]*?回复[u4e00-u9fa5]{0,2}[^<>]*?(\\d+)[^\\d]*?</div>", 2, true);
//        fme.setMissingElementProperty("作者：<a href=\"(.*?)\" target=\"_blank\">", 6, true);
//        fme.setMissingElementProperty("发表日期：([^<]+)", 1, true);
//        fme.setMissingElementProperty("<li>来自：<a title=\"查看该地区论坛\" href=[^<>]*? target=[^<>]*? class=[^<>]*?>([^<>]*?)</a></li>", 8, false);
//        fme.setMissingRegex("发表日期：([^<]+)", 1);
//        fme.setMissingElementProperty("日期：([^<]+)</", 2, false);
//        fme.setMissingElementProperty("<span>楼主：<a\\s+href=\"(.*?)\"[^>]*>.*?</a>", 8, true);
        fme.setMissingElementProperty("<span>楼主：<a\\s+href=\"[^\"]+\"[^>]*>(.*?)</a>", 8, true, "author_id", 0);
        fme.setMissingElementProperty("<span>楼主：<a\\s+href=\"([^\"]+)\"[^>]*>(.*?)</a>", 9, true, "author_id", 0);
        
        fme.fillMissingEle(urlList, startRow, sheetNum);
       
       
       * 
       */
        
        //**************************补全Excel********************
        
        /*
        ResultCollectionTool tianyaTool = new ResultCollectionTool();
        String resultSavePath = "18TY_SpecialCase_1.xls";
        tianyaTool.setResultPagesFilePath(resultSavePath);
        tianyaTool.savePage("http://bbs.tianya.cn/post-worldlook-615454-1.shtml", 0);
        *
        * 
        */
        
        
       
        
        
        
        
        //*******************获取天涯搜索列表**UTF-8******************
        //*******************记得去除getSpecialcase的第一个mep***************
        /*
        FillMissingElement fme = new FillMissingElement();
        ArrayList<String> urlList = new ArrayList<String>();
        int sheetNum = 0;
        urlList.add("http://www.tianya.cn/search/bbs?q=&pn=1&s=10");
        urlList.add("http://www.tianya.cn/search/bbs?q=&pn=1&s=10&f=3");
        ArrayList<String> urlDescription = new ArrayList<String>();
        urlDescription.add("回复全文");
        urlDescription.add("回复标题");
        ArrayList<String> contents = new ArrayList<String>();
//        contents.add("生态文明");
//        contents.add("文化建设");
//        contents.add("户籍改革");
//        contents.add("医疗改革");
//        contents.add("就业问题");
//        contents.add("教育改革");
//        contents.add("司法公正");
//        contents.add("住房问题");
//        contents.add("收入分配");
//        contents.add("食品安全");
//        contents.add("药品安全");
//        contents.add("社会保障");
//        contents.add("弱势群体");
//        contents.add("民主监督");
//        contents.add("依法行政");
//        contents.add("反腐倡廉");
//        contents.add("异地高考");
//        contents.add("三农问题");
//        contents.add("新交规");
//        contents.add("小康社会");
//        contents.add("计划生育");
//        contents.add("实名制");
//        contents.add("环保");
//        contents.add("速生鸡");
//        contents.add("污染");
//        contents.add("道德");
//        contents.add("价值体系");
//        contents.add("政治体制改革");
        contents.add("依法治国");
        contents.add("退休制度");
        contents.add("信访制度");
        contents.add("土地制度");
        contents.add("劳教制度");
        contents.add("劳动教养");
        contents.add("多党制");
        contents.add("三公消费");
        contents.add("拆迁");
        
        
      
        

        for(int i=0; i<contents.size(); ++i){
            for(int j=0; j<urlList.size(); ++j){
                fme.setSavePath("E:/data/18TY_domain/天涯" + contents.get(i) + urlDescription.get(j) + ".xls");
                String encodeUrl = fme.getEncodeUrl(urlList.get(j), contents.get(i));
                if(encodeUrl == null){
                    System.err.println("encode url is null!!!!!!");
                    continue;
                }
                fme.clearMissingElementProperty();
                fme.setMissingElementProperty("<h3><a href=\"([^\"]+)\"[^>]+>", 0, false, "page_url", 0);
                fme.setMissingElementProperty("<h3><a href=\"[^\"]+\"[^>]+>(.*?)</a>", 1, false, "page_title", 0);
                fme.setMissingElementProperty("时间：<span>([^<]+)</span>", 2, false, "date", 0);
                fme.setMissingElementProperty("回复：<span>([^<]+)</span>", 3, false, "reply_count", 0);
                fme.setMissingElementProperty("来自：<a[^>]+>(.*?)</a>", 4, false, "board", 0);
                fme.setMissingElementProperty("", 5, true, urlDescription.get(j), 0);
                fme.getSpecialCase(encodeUrl, 0, 1, sheetNum);
            }
        }
        
        * 
        */
       
        
        
        
        //*******************获取天涯搜索列表******************
        
        //*******************获取帖子内容页面***UTF-8****************
        //*******************记得添加getSpecialcase的第一个mep***************
        //*******************记得更改目录地址***************
       /*
        FillMissingElement fme = new FillMissingElement();
        String pagePath = "E:/data/18TY_domain/天涯领域数据.xls";
        int sheetNum = 8;
        int startRow = 986;
        ArrayList<String> urlList = fme.getFileURLList(pagePath, sheetNum);
        fme.setMissingElementProperty("<title>(.*?)</title>", 1, true, "title", 0);
        fme.setMissingElementProperty("<span>时间：([^<]+)</span>", 2, false, "time", 0);
        fme.setMissingElementProperty("<span>.*?：<a\\s+href=\"([^\"]+)\"[^>]+uname=\"[^\"]+\">", 3, false, "author_url", 0);
        fme.setMissingElementProperty("<span>.*?：<a\\s+href=\"[^\"]+\"[^>]+uname=\"([^\"]+)\">", 4, false, "author_id", 0);
        fme.setMissingElementProperty("<div class=\"bbs-content.*?\">(.*?)</div>", 5, false, "content", 0);
        
        
        for(int i=startRow; i<urlList.size(); ++i){
            System.err.println("doing the " + i + " case!!!!!!!!");
            fme.getSpecialCase(urlList.get(i), 0, i, 0);
        }
        
        * 
        */
        
        
        
        //*******************获取帖子内容页面*******************
       
        
       
        
        //********************获取人民聊吧帖子列表***GBK****************
        //*******************记得去除getSpecialcase的第一个mep***************
        
//        FillMissingElement fme = new FillMissingElement();
//        ArrayList<String> urlList = new ArrayList<String>();
//        int sheetNum = 0;
//        int startRow = 0;
//        urlList.add("http://liaoba.people.com.cn/topic.html?method=query&tb_code=12474");
//        fme.setSavePath("E:/data/18RMLB/rmlb.xls");
//        for(int j=0; j<urlList.size(); ++j){
//            fme.setMissingElementProperty("<td class=\"tie_3j_04a\"\\s*>\\s*<a href=\"([^\"]+)\"[^>]*>·.*?</a>", 0, false, "page_url", 0);
//            fme.setMissingElementProperty("<td class=\"tie_3j_04a\"\\s*>\\s*<a href=\"[^\"]+\"[^>]*>·(.*?)</a>", 1, false, "page_title", 0);
//            fme.setMissingElementProperty("<td class=\"tie_3j_04b\"[^>]+>(.*?)</", 2, false, "author_id_url", 0);
//            fme.setMissingElementProperty("<td class=\"tie_3j_04c\"[^>]+>(.*?)</td>", 3, false, "visit_reply_count", 0);
//            fme.getSpecialCase(urlList.get(j), startRow, 1, sheetNum);
//        }
        
        
        //********************获取人民聊吧帖子列表****GBK***************
        
        //**************************补全Excel********************
        
        /*
        String filePath = "E:/data/18RMLB/rmlb.xls";
        FillMissingElement fme = new FillMissingElement();
        int sheetNum = 0;
        int startRow = 358;
        ArrayList<String> urlList = fme.getFileURLList(filePath, sheetNum);
        fme.setSavePath(filePath);
        FileStorage.addCreaatedFiles(filePath);
        fme.setMissingElementProperty("时间：(.*?)</", 5, true, "date", 0);
        fme.fillMissingEle(urlList, startRow, sheetNum);
        * 
        */
        
        
        //**************************补全Excel********************
        
        
        
        //*******************获取人民聊吧帖子内容页面******GBK*************
        //*******************记得添加getSpecialcase的第一个mep***************
        //*******************记得更改目录地址***************
        
//        FillMissingElement fme = new FillMissingElement();
//        String pagePath = "E:/data/18RMLB/rmlb.xls";
//        int sheetNum = 0;
//        int startRow = 3;
//        ArrayList<String> urlList = fme.getFileURLList(pagePath, sheetNum);
//        fme.setMissingElementProperty("<title>(.*?)</title>", 1, true, "title", 0);
//        fme.setMissingElementProperty("时间：(.*?)</span>", 2, false, "date", 0);
//        fme.setMissingElementProperty("\\[([^\\]]+)\\]\\s{0,1}<[bs]", 3, false, "author_id", 0);
//        fme.setMissingElementProperty("(?s)[v\"] style=\"word-wrap:break-word\">(.*?)</div>", 4, false, "content", 0);
        
//        for(int i=startRow; i<urlList.size(); ++i){
//            System.err.println("doing the " + i + " case!!!!!!!!");
//            fme.getSpecialCase(urlList.get(i), 0, i, 0);
//        }
        
        
        //***********************SpecialCase*******************
        
//        fme.setMissingElementProperty("<title>(.*?)</title>", 1, true, "title", 0);
//        fme.setMissingElementProperty("发表于 (.*?)\\s*<a", 2, false, "date", 0);
//        fme.setMissingElementProperty("<span>(.*?)</span>发表于", 3, false, "author_id", 0);
////        fme.setMissingElementProperty("时间：(.*?)</span>", 2, false, "date", 0);
////        fme.setMissingElementProperty("\\[([^\\]]+)\\]\\s{0,1}<[bs]", 3, false, "author_id", 0);
//        fme.setMissingElementProperty("(?s)[v\"] style=\"word-wrap:break-word\">(.*?)</div>", 4, false, "content", 0);
//        String url = "http://liaoba.people.com.cn/vote.html?method=detail&vt_code=9041";
//        fme.getSpecialCase(url, 0, 0, 0);
        
        //***********************SpecialCase*******************
        
        //*******************获取帖子内容页面*******************
        
        
        //*******************获取新华社区搜索列表**UTF-8******************
        //*******************记得去除getSpecialcase的第一个mep***************
       /*
        FillMissingElement fme = new FillMissingElement();
        ArrayList<String> urlList = new ArrayList<String>();
        int sheetNum = 0;
        urlList.add("http://search.home.news.cn/forumbookSearch.do?title=&adv=1&content=&nickName=&bid=0&start=2012-10-01+00%3A00&end=&pageSize=100&&&&&pageNo=1");
        ArrayList<String> contents = StaticHelper.initialSearchContents();
        for(int i=0; i<contents.size(); ++i){
            for(int j=0; j<urlList.size(); ++j){
                fme.setSavePath("E:/data/18XHSQ_domain/新华社区" + contents.get(i) + ".xls");
                String encodeUrl = fme.getEncodeUrl(urlList.get(j), contents.get(i));
                if(encodeUrl == null){
                    System.err.println("encode url is null!!!!!!");
                    continue;
                } 
                fme.clearMissingElementProperty();
                fme.setMissingElementProperty("<a\\s+href=\"([^\"]+)\"\\s+class=\"sb14b\"[^>]+>", 0, false, "page_url", 0);
                fme.setMissingElementProperty("class=\"sb14b\"[^>]+>(.*?)</a>", 1, false, "page_title", 0);
                fme.setMissingElementProperty("<a href=\"([^\"]+)\"[^>]+><span class=\"sk\">", 2, false, "author_url", 0);
                fme.setMissingElementProperty("<span class=\"sk\">(.*?)</span>", 3, false, "author_id", 0);
                fme.setMissingElementProperty("images/watch.gif\"[^>]+>([^<]+)\\s*</td>", 4, false, "date", 0);
                fme.getSpecialCase(encodeUrl, 0, 1, sheetNum);
            }
        }
        * 
        */
        
        
        
        //*******************获取新华社区搜索列表**UTF-8******************
        
        
        
        //*******************获取凤凰论坛搜索列表**UTF-8******************
        //*******************记得去除getSpecialcase的第一个mep***************
        /*
        FillMissingElement fme = new FillMissingElement();
        ArrayList<String> urlList = new ArrayList<String>();
        int sheetNum = 0;
        urlList.add("http://bbs.ifeng.com/search.php?srchtxt=&srchuname=&srchcat=&srchrange=all&srchfrom=8640000&orderby=replies&advancesearch=1&page=1");
        ArrayList<String> contents = StaticHelper.initialSearchContents();
        for(int i=0; i<contents.size(); ++i){
            for(int j=0; j<urlList.size(); ++j){
                fme.setSavePath("E:/data/18FH_domain/凤凰论坛全文" + contents.get(i) + "_1.xls");
                String encodeUrl = fme.getEncodeUrl(urlList.get(j), contents.get(i));
                if(encodeUrl == null){
                    System.err.println("encode url is null!!!!!!");
                    continue;
                } 
                fme.clearMissingElementProperty();
                fme.setMissingElementProperty("</label>\\s*<a href=\"([^\"]+)\"[^>]*>", 0, false, "page_url", 0);
                fme.setMissingElementProperty("target=\"_blank\" >(.*?)</a>\\s*</th>", 1, false, "page_title", 0);
                fme.setMissingElementProperty("cite>\\s*<a href=\"([^\"]+)\">", 2, false, "author_url", 0);
                fme.setMissingElementProperty("<cite>\\s*<a[^>]+>(.*?)</a>", 3, false, "author_id", 0);
                fme.setMissingElementProperty("</cite>\\s*<em>([^<]+)</em>", 4, false, "date", 0);
                fme.setMissingElementProperty("<td class=\"nums\"><strong>(.*?)</strong>", 5, false, "reply_count", 0);
                fme.setMissingElementProperty("</strong> / <em>(.*?)</em>", 6, false, "vist_count", 0);
                fme.setMissingElementProperty("\"forum\"><a[^>]+>(.*?)</a>", 7, false, "board", 0);
                fme.getSpecialCase(encodeUrl, 0, 1, sheetNum);
            }
        }
        
        * 
        */
         //*******************获取凤凰论坛搜索列表**UTF-8******************
        
        
        //*******************获取帖子内容页面*******************
        //*******************记得添加getSpecialcase的第一个mep***************
        //*******************记得更改目录地址***************
        /*
        FillMissingElement fme = new FillMissingElement();
        String pagePath = "E:/data/18FH_domain/凤凰领域数据.xls";
        int sheetNum = 10;
        int startRow = 0;
        ArrayList<String> urlList = fme.getFileURLList(pagePath, sheetNum);
        fme.setMissingElementProperty("<title>(.*?)</title>", 1, true, "title", 0);
        fme.setMissingElementProperty("\"c(Red|Gray)\">(\\w\\w\\w\\w-[^<]+)</", 2, false, "date", 1);
        fme.setMissingElementProperty("(<h3>|作者：)<a\\s+href=\"([^\"]+)\"[^>]+>", 3, false, "author_url", 1);
        fme.setMissingElementProperty("(<h3>|作者：)<a[^>]+>([^<]+)</a>", 4, false, "author_id", 1);
        fme.setMissingElementProperty("(?s)id=\"postmessage_[^>]+>(.*?)</div>", 5, false, "content", 0);
        
        
        for(int i=startRow; i<urlList.size(); ++i){
            System.err.println("doing the " + i + " case!!!!!!!!");
            fme.getSpecialCase(urlList.get(i), startRow, i, 0);
        }
        * 
        */
        
        //*******************获取帖子内容页面*******************
        //*******************记得添加getSpecialcase的第一个mep***************
        //*******************记得更改目录地址***************
        //******************** encode = gbk *******************
        /*
        FillMissingElement fme = new FillMissingElement();
        String pagePath = "E:/data/18XH_domain/新华社区领域数据.xls";
        int sheetNum = 2;
        int startRow = 150;
        ArrayList<String> urlList = fme.getFileURLList(pagePath, sheetNum);
        fme.setMissingElementProperty("<title>(.*?)</title>", 1, true, "title", 0);
        fme.setMissingElementProperty("于&nbsp;(.*?)&nbsp;发表", 2, false, "date", 0);
        fme.setMissingElementProperty("id=\"author[^\"]+\">\\s*<a href=\"([^\"]+)\"[^>]*>", 3, false, "author_url", 0);
        fme.setMissingElementProperty("id=\"author[^\"]+\">\\s*<a[^>]*>(.*?)</a>", 4, false, "author_id", 0);
        fme.setMissingElementProperty("(?s)<div id=\"message[^\"]+\">(.*?)</div>", 5, false, "content", 0);
        
        
        for(int i=startRow; i<urlList.size(); ++i){
            System.err.println("doing the " + i + " case!!!!!!!!");
            fme.getSpecialCase(urlList.get(i), 0, i, 0);
        }
        * 
        */
        
        
        //***************************凯迪社区**********************
        //***************************版块列表***********************
        //*******************记得去除getSpecialcase的第一个mep***************
        //************************ encode = gb2312 ************************
        /*
        FillMissingElement fme = new FillMissingElement();
        ArrayList<String> urlList = new ArrayList<String>();
        int sheetNum = 0;
        
        urlList.add("http://club.kdnet.net/list.asp?t=0&boardid=1&seltimelimit=0&action=&topicmode=0&s=&page=1809&tid=2012-10-16%2018:28:52.283");
//        urlList.add("http://club.kdnet.net/list.asp?boardid=101");
//        urlList.add("http://club.kdnet.net/list.asp?boardid=89");
//        urlList.add("http://club.kdnet.net/list.asp?t=0&boardid=52&seltimelimit=0&action=&topicmode=0&s=&page=103&tid=2012-10-19%2009:19:50.080");
        ArrayList<String> urlDescription = new ArrayList<String>();
        urlDescription.add("猫眼看人");
//        urlDescription.add("舆情观察");
//        urlDescription.add("时局深度");
//        urlDescription.add("原创评论");
        FileStorage.addCreaatedFiles("E:/data/18KD_domain/凯迪社区猫眼看人_2.xls");
        fme.setMissingElementProperty("class=\"f14px\"><a href=\"([^\"]+)\"", 0, false, "page_url", 0);
        fme.setMissingElementProperty("title=\"《(.*?)》", 1, false, "page_title", 0);
        fme.setMissingElementProperty("class=\"author\">\\s*<a target=\"_blank\" href=\"([^\"]+)\"", 2, false, "author_url", 0);
        fme.setMissingElementProperty("class=\"author\">\\s*<a.*?title=\"([^\"]+)\"", 3, false, "author_id", 0);
        fme.setMissingElementProperty("发表于：([^&]+)&#13;", 4, false, "date", 0);
        fme.setMissingElementProperty("<td class=\"statistics clearfix\"\\s*>(.*?)</td>", 5, false, "reply_visit_count", 0);
        for(int i=0; i<urlList.size(); ++i){
            fme.setSavePath("E:/data/18KD_domain/凯迪社区" + urlDescription.get(i) + "_2.xls");
            fme.getSpecialCase(urlList.get(i), 24915, 1, sheetNum);
        }
        * 
        */
        
        
        //*******************获取帖子内容页面*******************
        //*******************记得添加getSpecialcase的第一个mep***************
        //*******************记得更改目录地址***************
        //******************** encode = gb2312 *******************
        
        FillMissingElement fme = new FillMissingElement();
        String pagePath = "E:/data/18KD_domain/凯迪社区领域数据.xls";
        int sheetNum = 2;
        int startRow = 67;
        ArrayList<String> urlList = fme.getFileURLList(pagePath, sheetNum);
        fme.setMissingElementProperty("<title>(.*?)</title>", 1, true, "title", 0);
        fme.setMissingElementProperty("</span> 于?\\s*(\\d{4}[/-]\\d{1,2}[/-]\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}) [发&]", 2, false, "date", 0);
        fme.setMissingElementProperty("id=\"detailinfo_[^\"]+\"></div>\\s+<span class=\"name c-main\">\\s+<a\\s+href=\"([^\"]+)\"", 3, false, "author_url", 0);
        fme.setMissingElementProperty("id=\"detailinfo_[^\"]+\"></div>\\s+<span class=\"name c-main\">\\s+<a[^>]+>(.*?)</a>", 4, false, "author_id", 0);
        fme.setMissingElementProperty("(?s)</div>\\s*<div class=\"(posts-cont|replycont-text)\">(.*?)</div>", 5, false, "content", 1);
        
        
        for(int i=startRow; i<urlList.size(); ++i){
            System.err.println("doing the " + i + " case!!!!!!!!");
            fme.getSpecialCase(urlList.get(i), 0, i, 0);
        }
        
        //************  special case **********************
        //http://club.kdnet.net/dispbbs.asp?boardid=1&id=8748348&page=43&uid=&usernames=&userids=&action=
//        fme.setMissingElementProperty("<title>(.*?)</title>", 1, true, "title", 0);
//        fme.setMissingElementProperty("</span> 于?\\s*(\\d{4}[/-]\\d{1,2}[/-]\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}) [发&]", 2, false, "date", 0);
//        fme.setMissingElementProperty("id=\"detailinfo_[^\"]+\"></div>\\s+<span class=\"name c-main\">\\s+<a\\s+href=\"([^\"]+)\"", 3, false, "author_url", 0);
//        fme.setMissingElementProperty("id=\"detailinfo_[^\"]+\"></div>\\s+<span class=\"name c-main\">\\s+<a[^>]+>(.*?)</a>", 4, false, "author_id", 0);
//        fme.setMissingElementProperty("(?s)</div>\\s*<div class=\"(posts-cont|replycont-text)\">(.*?)</div>", 5, false, "content", 1);
//        String url = "http://club.kdnet.net/dispbbs.asp?boardid=1&id=8681319&page=196&uid=&usernames=&userids=&action=";
//        fme.setSavePath("E:/data/18KD_domain/kd/[转贴]坐牢两年半 挣了几千万 【猫眼看人】-凯迪社区_3_1.xls");
//        FileStorage.addCreaatedFiles("E:/data/18KD_domain/kd/[转贴]坐牢两年半 挣了几千万 【猫眼看人】-凯迪社区_3_1.xls");
//        fme.getSpecialCase(url, 2910, 3, 0);
//        ArrayList<String> urlList = new ArrayList<String>();
//        urlList.add("http://club.kdnet.net/dispbbs.asp?page=1&boardid=1&id=8712803");
//        urlList.add("http://club.kdnet.net/dispbbs.asp?page=1&boardid=1&id=8681319");
//        urlList.add("http://club.kdnet.net/dispbbs.asp?page=1&boardid=1&id=8847584");
//        urlList.add("http://club.kdnet.net/dispbbs.asp?page=1&boardid=1&id=8700949");
//        for(String ul : urlList){
//            fme.setSavePath(null);
//            fme.getSpecialCase(ul, 0, 1, 0);
//        }
        
        
    }
    
    
}
