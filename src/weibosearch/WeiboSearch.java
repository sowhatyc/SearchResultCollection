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
import java.util.Random;
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
        ArrayList<Object> weiboIds = FileStorage.getContentFromExcel("E:\\data\\18WEIBO\\user_domain.xls", 0, 0, 0, 19);
        for(Object id : weiboIds){
            StaticHelper.START_ROW = 0;
            WeiboProcesser.getCommentsById((String) id, StaticHelper.access_level.test_level.ordinal(), 1);
        }
    }
}
