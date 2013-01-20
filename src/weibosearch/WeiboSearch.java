/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weibosearch;

import CommonTools.FileStorage;
import CommonTools.StaticHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import weibo4j.model.WeiboException;

/**
 *
 * @author admin
 */
public class WeiboSearch {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //******************  get User weibo by date ********************
        /*
        StaticHelper.initialUserNameList();
        for(String userName: StaticHelper.USER_NAME_LIST){
            StaticHelper.START_ROW = 2194;
            WeiboProcesser.getUserWeiboByDate(userName, "2012-10-01 00:00:00", StaticHelper.access_level.test_level.ordinal(), 24);
        }
        * 
        */
        
        //********************* get weibo comments **********************
//        ArrayList<Object> weiboIds = FileStorage.getContentFromExcel("E:\\data\\18WEIBO\\user_domain.xls", 0, 0, 0, 10);
//        for(Object id : weiboIds){
//            StaticHelper.START_ROW = 0;
//            WeiboProcesser.getCommentsById((String) id, StaticHelper.access_level.test_level.ordinal(), 1);
//        }
        
        Map weiboIds = new HashMap<String, ArrayList<String>>();
        ArrayList<String> jiaoyu = new ArrayList<String>();
//        jiaoyu.add("3524991045513610");
        jiaoyu.add("3519128880629039");
//        jiaoyu.add("3530358823836955");
//        shiping.add("3534644961757046");
        weiboIds.put("教育/", jiaoyu);
//        ArrayList<String> yiliao = new ArrayList<String>();
//        yiliao.add("3509482468408157");
//        yiliao.add("3514970903836966");
//        yiliao.add("3521994844358798");
//        yiliao.add("3505418258737165");
//        weiboIds.put("医疗/", yiliao);
//        ArrayList<String> zhufang = new ArrayList<String>();
//        zhufang.add("3517283001122370");
//        zhufang.add("3525771743867368");
//        zhufang.add("3516935377315473");
//        zhufang.add("3532121781847065");
//        weiboIds.put("住房/", zhufang);
        Set<String> domains = weiboIds.keySet();
        Iterator<String> domainName = domains.iterator();
        while(domainName.hasNext()){
            String name = domainName.next();
            ArrayList<String> ids = (ArrayList<String>) weiboIds.get(name);
            for(String id: ids){
                StaticHelper.START_ROW = 4144;
                WeiboProcesser.getCommentsById(id, StaticHelper.access_level.test_level.ordinal(), 43, name);
            }
        }
        
        
//        StaticHelper.START_ROW = 4445;
//        WeiboProcesser.getCommentsById("3523057664651363", StaticHelper.access_level.test_level.ordinal(), 66, "生态/");
    }
}
