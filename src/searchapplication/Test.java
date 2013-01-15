/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package searchapplication;

import CommonTools.DataValue;
import CommonTools.MatchRegex;
import SearchResultsCollection.WebCrawler;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class Test {
    
    public static void main(String args[]){
            /*
            String url = "http://club.autohome.com.cn/bbs/thread-c-2319-17209324-1.html";
            String content = WebCrawler.getPageContent(url);
            MatchRegex mr1 = new MatchRegex("<title>(.*?)</title>");
            String result = mr1.doValueMatch(content).getValueAt(0)[0];
            System.out.println(result);
    //        MatchRegex mr2 = new MatchRegex("<div class=[^<>]*?>[^<>]*?[u4e00-u9fa5]{1,4}[^\\d]*?(\\d+)[^<>]*?回复[u4e00-u9fa5]{0,2}[^<>]*?\\d+[^\\d]*?</div>");
            MatchRegex mr2 = new MatchRegex("<title>[^<>]*?_([^<>_]{1,20})_[^<>_]{0,20}</title>");
            String result2 = mr2.doValueMatch(content).getValueAt(0)[0];
            System.out.println(result2);
    //        MatchRegex mr3 = new MatchRegex("<div class=[^<>]*?>[^<>]*?[u4e00-u9fa5]{1,5}[^\\d]*?\\d+[^<>]*?回复[u4e00-u9fa5]{0,2}[^<>]*?(\\d+)[^\\d]*?</div>");
            MatchRegex mr3 = new MatchRegex("<li class=[^<>]*?><a href=\"([^<>]*?)\" target=[^<>]*? class=[^<>]*? title=[^<>]*? xname=[^<>]*?>[^<>]*?</a>");
            String result3 = mr3.doValueMatch(content).getValueAt(0)[0];
            System.out.println(result3);
    //        MatchRegex mr4 = new MatchRegex("作者：<a href=\"(.*?)\" target=\"_blank\">");
            //作者：<a href=\"(.*?)\"[^>]*>
            MatchRegex mr4 = new MatchRegex("<li>来自：<a title=\"查看该地区论坛\" href=[^<>]*? target=[^<>]*? class=[^<>]*?>([^<>]*?)</a></li>");
            String result4 = mr4.doValueMatch(content).getValueAt(0)[0];
            System.out.println(result4);
            * 
            */
    //        MatchRegex mr5 = new MatchRegex("作者：<a href=\".*?\" target=\"_blank\">(.*?)</a>");
    //        String result5 = mr5.doValueMatch(content).getValueAt(0)[0];
    //        System.out.println(result5);
    //        MatchRegex mr6 = new MatchRegex("发表日期：([^<]+)");
            //日期：([^<]+)</
    //        String result6 = mr6.doValueMatch(content).getValueAt(0)[0];
    //        System.out.println(result6);
            
    //        int startIndex = -1;
    //        String text = "收费也骂，免费还骂，涨价恐怕更得骂。[哈哈] //@萧山悠悠: //@当代人民英雄黄绍塔: //@善待身边的人F疯爷: sb拍脑袋决定";
    //        ArrayList<String> textNames = new ArrayList<String>();
    //        while((startIndex = text.indexOf("//@", startIndex+3)) != -1){
    //            int otherIndex = text.indexOf(":", startIndex);
    //            String name = text.substring(startIndex+3, otherIndex); 
    //            System.out.println(name);
    //            textNames.add(name);
    //        }
//            String url = "http://paper.people.com.cn/rmrb/html/2012-10/31/nw.D110000renmrb_20121031_2-01.htm";
//            String content = WebCrawler.getPageContent(url);
//            
//            MatchRegex mr = new MatchRegex(" ");
//            System.out.println(mr.doValueReplaceAll(content, "%20"));
//            System.out.println(dv.getFindNum());
//            System.out.println(content);
        int b = 1;
        System.out.println("a");
        System.out.println(b);
    }
    
}
