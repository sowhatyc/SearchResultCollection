/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class StaticHelper {
    

    public static int START_ROW = 0;
    
    public static String FILE_DIRECTORY = "E:/data/18WEIBO/comments/";
    public static String FILE_PATH = null;
    
    public static enum access_level{test_level, general_level}
    
    public static ArrayList<String> TOKEN_LIST = new ArrayList<String>();
    public static ArrayList<String> USER_NAME_LIST = new ArrayList<String>();
    
    public static void initialTokenList(){
        TOKEN_LIST.add("2.00LhH9oBa66y5E8eea3a0cb701dURf");
        TOKEN_LIST.add("2.00LhH9oBUr2S1B50c41ef8e0lhiZsC");
        TOKEN_LIST.add("2.00LhH9oB1qXG3B3a1d426967HK1c7C");
//        TOKEN_LIST.add("2.006FPoQDMBZ9tCe23adab65cCdJarB");
//        TOKEN_LIST.add("2.006FPoQDONoHKD2c4191cdb2to8HCD");
//        TOKEN_LIST.add("2.006FPoQDeWyIeB04a45bcd2aRYcC1D");
    }
    
    public static void initialUserNameList(){
//        USER_NAME_LIST.add("薛蛮子");
//        USER_NAME_LIST.add("李承鹏");
//        USER_NAME_LIST.add("慕容雪村");
//        USER_NAME_LIST.add("南方周末");
//        USER_NAME_LIST.add("头条新闻");
//        USER_NAME_LIST.add("李开复");
//        USER_NAME_LIST.add("任志强");
//        USER_NAME_LIST.add("南方都市报");
//        USER_NAME_LIST.add("茅于轼");
//        USER_NAME_LIST.add("袁腾飞");
//        USER_NAME_LIST.add("人民日报");
//        USER_NAME_LIST.add("人民网");
//        USER_NAME_LIST.add("芮成钢");
//        USER_NAME_LIST.add("于建嵘");
//        USER_NAME_LIST.add("许小年");
//        USER_NAME_LIST.add("刘胜军改革");
//        USER_NAME_LIST.add("经济观察报");
//        USER_NAME_LIST.add("央视新闻");
//        USER_NAME_LIST.add("三联生活周刊");
//        USER_NAME_LIST.add("微博新鲜事");
//        USER_NAME_LIST.add("潘石屹");
//        USER_NAME_LIST.add("南都周刊");
//        USER_NAME_LIST.add("大鹏看天下");
//        USER_NAME_LIST.add("新周刊");
//        USER_NAME_LIST.add("新闻晨报");
        USER_NAME_LIST.add("财经网");
    }
    
    public static void  priorPrint(String printContent){
        priorPrint(printContent,0);
    }
    
    public static void  priorPrint(String printContent,int controlLevel){
        //普通显示
        if(controlLevel == 0){
            System.out.println(printContent);
            return;
        }
        //异常显示
        if(controlLevel == 1){
            System.err.println(printContent);
        }
    }
    
    public static void priorPrint(int elementType, int i) {
        String type = elementType+""; 
        priorPrint(type,i);
       
    }
    
}
