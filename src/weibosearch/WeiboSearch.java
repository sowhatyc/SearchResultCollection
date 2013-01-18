/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weibosearch;

import CommonTools.StaticHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        String id = "3535374674423031";
        StaticHelper.START_ROW = 0;
        WeiboProcesser.getCommentsById(id, StaticHelper.access_level.test_level.ordinal(), 1);
    }
}
