/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

//import PageAnalysis.AnalysisFunction;
//import java.util.ArrayList;


/**
 *
 * @author Administrator
 */
public class PageType {
    
    public final static int BlockPage = 1;
    public final static int ItemPage = 2;
    public final static int UserPage = 3;
    public final static int ContentPage = 4;
    //private static ArrayList<String> pageTypeList;
    
    //用于分析网页，获取当前网页类型
 
    public static int getPageType(Page page){
        
        //预读取，某个文件...
        
        
        //访问 PageAnalysis相关页面类别鉴定方法 提供静态类别数值返回
     /*  if(url == ....){
            return BlockPage;
        }
      * 
      */
//        测试阶段全部返回值为1 代表是属于BLOCK板块，可以进行网页内容提取
        return 1;
//        return 0;
    }
    
//    public static void initPageType(){
//        
//    }
    
}
