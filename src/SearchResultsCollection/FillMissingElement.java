/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

import CommonTools.DataValue;
import CommonTools.FileStorage;
import CommonTools.MatchRegex;
import CommonTools.StaticHelper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

/**
 *
 * @author admin
 */
public class FillMissingElement {
    
//    private Map<String, Integer> missingRegexs = new HashMap<String, Integer>();
    private ArrayList<MissingElementProperty> mepList = new ArrayList<MissingElementProperty>();
    private ArrayList<MissingResult> missingResultList = new ArrayList<MissingResult>();
    private String savePath = null;
   
    
    public String getEncodeUrl(String url, String content){
        String encodeUrl = null;
        try {
            String encodeContent = java.net.URLEncoder.encode(content, "utf-8");
            int queryIndex;
            if((queryIndex = url.indexOf("q=")) > 0){
                encodeUrl = url.substring(0, queryIndex+2) + encodeContent + url.substring(queryIndex+2);
            }else if((queryIndex = url.indexOf("k=")) > 0){
                encodeUrl = url.substring(0, queryIndex+2) + encodeContent + url.substring(queryIndex+2);
            }else if((queryIndex = url.indexOf("txt=")) > 0){
                encodeUrl = url.substring(0, queryIndex+4) + encodeContent + url.substring(queryIndex+4);
            }else if((queryIndex = url.indexOf("title=")) > 0){
                encodeUrl = url.substring(0, queryIndex+6) + encodeContent + url.substring(queryIndex+6);
            }else{
                System.err.println("could not find the query tag!!!!!");
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return encodeUrl;
        }
    }
    
    public void clearMissingElementProperty(){
        mepList.clear();
    }
    
    public void setMissingElementProperty(String missingRegex, int missingPosition, boolean missingSingle, String description, int selectedPostion){
        mepList.add(new MissingElementProperty(missingRegex, missingPosition, missingSingle, description, selectedPostion));
    }
    
    public void setSavePath(String savePath){
        this.savePath = savePath;
        StaticHelper.RESULTPAGES_FIELPATH = savePath;
    }
    
    public ArrayList<String> getFileURLList(String path, int sheetNum){
        ArrayList<String> urlList = null;
        try {
            urlList = new FileStorage().getFileURLListFromExcel(path, sheetNum);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
//            System.err.println("There is something wrong in reading FileURLList!!!!");
            return urlList;
        }
    }
    
    public void fillMissingEle(ArrayList<String> urlList, int startRow, int sheetNum){
        int count = -1;
        int saveStartRow = startRow;
        int toSaveCount = 50;
        int stopToRest = 0;
        for(String url : urlList){
            ++count;
            if(count < startRow){
                continue;
            }
            if(url.equals("null")){
                missingResultList.add(null);
            }else{
                System.out.println("doing url = " + url + " count = " + count);
//                String content= WebCrawler.getPageContentByNekoHtml(sr.getUrl());
                String content = WebCrawler.getPageContent(url);
                ++stopToRest;
                if(content == null){
                    System.err.println("logging info");
                    missingResultList.add(null);
                    continue;
                }
                int errorCount = 0;    
                while(content.length()<50){
                    try {
                        System.err.println("抓取异常，需要重新抓取");
    //                    System.out.println(content);
                        Thread.sleep(5000);
                        errorCount++;
    //                    ThreadErrorNum++;

                    } catch (InterruptedException ex) {
                        Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(errorCount > 3){
                        System.err.println("抓取异常，停止抓取");
                        System.err.println("logging info");
                        break;
                    }
                    content = WebCrawler.getPageContent(url);
                    ++stopToRest;
                    if(content == null){
                        System.err.println("logging info");
                    }
                }
                if(errorCount > 3){
                    missingResultList.add(null);
                    continue;
                }
                MissingResult missingResult = new MissingResult();
                for(MissingElementProperty mep : mepList){
                    DataValue dv = mep.getMissingMatchRegex().doValueMatch(content);
                    if(dv.getFindNum() == 0){
                        missingResult = null;
                        break;
                    }
                    if(mep.isMissingSingle()){
                        MissingElement me = new MissingElement();
                        me.setMisstingContent(dv.getValueAt(0)[mep.getSelectedPostion()].trim());
                        me.setMissingPosition(mep.getMissingPosition());
                        missingResult.addElement(me);
                    }else{
                        for(int i=0; i<dv.getFindNum(); ++i){
                            MissingElement me = new MissingElement();
                            me.setMisstingContent(dv.getValueAt(i)[mep.getSelectedPostion()]);
                            me.setMissingPosition(mep.getMissingPosition() + i*2);
                            missingResult.addElement(me);
                        }
                    }
                }
                missingResultList.add(missingResult);
            }
            
            if(missingResultList.size() == toSaveCount){
                try {
                    new FileStorage().updateSearchResultExcel(savePath, missingResultList, saveStartRow, sheetNum);
                    saveStartRow += toSaveCount;
                    missingResultList.clear();
                    stopToRest = 0;
                } catch (IOException ex) {
                    Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BiffException ex) {
                    Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
                } catch (WriteException ex) {
                    Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(stopToRest >= 10){
                try {
                    Thread.sleep(5000);
                    stopToRest = 0;
                } catch (InterruptedException ex) {
                    Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try {
            new FileStorage().updateSearchResultExcel(savePath, missingResultList, saveStartRow, sheetNum);
        } catch (IOException ex) {
            Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException ex) {
            Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getSpecialCase(String url, int startRow, int tag, int sheetNum){
//        String nextPageUrl = url;
        String title = null;
        Set<String> hasDealedUrl = new HashSet<String>();
        int allZerosCount = 0;
        boolean allZeros = true;
        int stopToRest = 0;
        boolean nextSimple = true;
        boolean nextComplicate = true;
        ArrayList<MissingElementProperty> singleMEPList = new ArrayList<MissingElementProperty>();
        ArrayList<MissingElementProperty> multipleMEPList = new ArrayList<MissingElementProperty>();
        for(int i=0; i<mepList.size(); ++i){
            if(mepList.get(i).isMissingSingle()){
                singleMEPList.add(mepList.get(i));
//                mepList.remove(i);
            }else{
                multipleMEPList.add(mepList.get(i));
            }
        }
        Page page = new Page(url);
        page.setNextPage(url);
        hasDealedUrl.add(url);
        while(page.getNextPage() != null){
            allZeros = true;
            System.out.println("******************begin url=**********   " + page.getNextPage());
            missingResultList.clear();
            String content = WebCrawler.getPageContent(page.getNextPage());
            ++stopToRest;
            int errorCount = 0;    
            while(content.length()<50){
                try {
                    System.err.println("抓取异常，需要重新抓取");
//                    System.out.println(content);
                    Thread.sleep(5000);
                    errorCount++;
//                    ThreadErrorNum++;

                } catch (InterruptedException ex) {
                    Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(errorCount > 3){
                    System.err.println("抓取异常，停止抓取");
                    System.err.println("logging info");
                    break;
                }
                content = WebCrawler.getPageContent(page.getNextPage());
                ++stopToRest;
            }
            if(errorCount > 3){
                System.err.println("can not crawl the page!!! stop working!!!");
                break;
            }
            if(content.equals(page.getPageContent())){
                break;
            }
            page.setPageContent(content);
//            System.out.println(content);
            
            
            
            
            ArrayList<DataValue> dvList = new ArrayList<DataValue>();
            int dvPostion[] = new int[multipleMEPList.size()];
            boolean checkIntegerity = true;
            int index = 0;
            for(MissingElementProperty mep : multipleMEPList){
                DataValue dv = mep.getMissingMatchRegex().doValueMatch(content);
                if(dv.getFindNum() > 0){
                    allZeros = false;
                }
                if(mep.getDescription().equals("page_title")){
                    for(int i=0; i<dv.getFindNum(); ++i){
                        String page_title = dv.getValueAt(i)[mep.getSelectedPostion()];
                        MatchRegex mr = new MatchRegex("<[^>]+>");
                        page_title = mr.doValueReplaceAll(page_title, "");
                        dv.setValueAt(page_title, i, mep.getSelectedPostion());
                    }
                }
                System.err.println("*************findnum = " + dv.getFindNum());
                if(dvList.isEmpty()){
                    dvList.add(dv);
                    dvPostion[index] = mep.getMissingPosition();
                    ++index;
                }else if(dvList.get(dvList.size()-1).getFindNum() == dv.getFindNum()){
                    dvList.add(dv); 
                    dvPostion[index] = mep.getMissingPosition();
                    ++index;
                }else if(mep.getDescription().equals("content") && ((dv.getFindNum() + 1) == dvList.get(dvList.size()-1).getFindNum())){
                    System.err.println("if you are not doing TY, please be careful!!!");
                    for(DataValue dvalue : dvList){
                       dvalue.eraseValueAt(0); 
                    }
                    dvList.add(dv);
                    dvPostion[index] = mep.getMissingPosition();
                    ++index;
                }else{
                    checkIntegerity = false;
                    System.err.println("your regex find number is not consistent!!!!");
                    System.err.println(mep.getMissingRegex() + " " + dv.getFindNum() + " " + dvList.get(dvList.size()-1).getFindNum());
//                    for(int i=0; i<dv.getFindNum(); ++i){
//                        System.out.println(dv.getValueAt(i)[0]);
//                    }
                    break;
                } 
            }
//            System.out.println("********************title************** = ");
            if(checkIntegerity){
//                System.out.println("********************title************** = ");
                try {
                    for(int i=0; i<dvList.get(0).getFindNum(); ++i){
                        MissingResult missingResult = new MissingResult();
                        for(int j=0; j<dvList.size(); ++j){
                            MissingElement me = new MissingElement();
                            String mrContent = dvList.get(j).getValueAt(i)[multipleMEPList.get(j).getSelectedPostion()].trim();
                            if(mrContent.length() > 32767){
                                System.out.println("content Size is too large!!");
                                mrContent = StaticHelper.firstScriptFilter(mrContent);
                                if(mrContent.length() > 32767){
                                    System.out.println("content Size is still too large!!");
                                    MatchRegex mr = new MatchRegex("<[^>]+>");
                                    mrContent = mr.doValueReplaceAll(mrContent, "");
                                    if(mrContent.length() > 32767){
                                        System.out.println("content Size is still too too large!!");
                                        mrContent = mrContent.substring(0, 32766);
                                    }
                                }
                            }
                            me.setMisstingContent(mrContent);
                            me.setMissingPosition(dvPostion[j]);
                            missingResult.addElement(me);
                        }
                        
                        for(MissingElementProperty mep : singleMEPList){
//                            System.out.println("********************innertitle************** = ");
                            if(mep.getDescription().equals("title")){
                                title = mep.getMissingMatchRegex().doValueMatch(content).getValueAt(0)[mep.getSelectedPostion()].trim();
//                                 System.out.println("********************title************** = "+title);
                            }
                            MissingElement me = new MissingElement();
                            if(mep.getMissingRegex().equals("")){
                                me.setMisstingContent(mep.getDescription());
                            }else{
                                me.setMisstingContent(mep.getMissingMatchRegex().doValueMatch(content).getValueAt(0)[mep.getSelectedPostion()].trim());
                            }
                            me.setMissingPosition(mep.getMissingPosition());
                            missingResult.addElement(me);
                        }
                        
                        //****************************添加第一个mep**********************
                        
                        MissingElement me = new MissingElement();
                        me.setMisstingContent(page.getNextPage());
                        me.setMissingPosition(0);
                        missingResult.addElement(me);
                        
                        //*****************************添加第一个mep**********************
                        
                        
                        missingResultList.add(missingResult);
                    }
                    if(savePath == null){
                        if(StaticHelper.RESULTPAGES_FIELPATH == null){
                            MatchRegex mr_NameChange = new MatchRegex("[?<>/\\|:\"*.]");
//                            System.out.println("********************title************** = "+title);
                            if(title == null || title.equals("")){
                                return;
                            }
                            title = mr_NameChange.doValueReplaceAll(title, "");
                            //**************************更改目录地址*****************************
                            
                            StaticHelper.RESULTPAGES_FIELPATH = "E:/data/18KD_domain/kd/" + title + "_"+ tag + "_1.xls";  
                            
                            //**************************更改目录地址*****************************
                        }
                        
                        new FileStorage().updateSearchResultExcel(StaticHelper.RESULTPAGES_FIELPATH, missingResultList, startRow, sheetNum);
                    }else{
                        new FileStorage().updateSearchResultExcel(StaticHelper.RESULTPAGES_FIELPATH, missingResultList, startRow, sheetNum);
                    }
                    startRow = StaticHelper.START_ROW;
                    startRow += dvList.get(0).getFindNum();
                    StaticHelper.START_ROW = startRow;
                } catch (IOException ex) {
                    Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BiffException ex) {
                    Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
                } catch (WriteException ex) {
                    Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
//            page.setNextPage(AnalysisFunction.getNextPage(page));
            if(allZeros){
                ++allZerosCount;
                if(allZerosCount >= 3){
                    break;
                }
            }
//            page.setNextPage(AnalysisFunction.getNextPageSimpleMethod(page));
            String nextUrl = null;
            if(nextComplicate){
                nextUrl = AnalysisFunction.getNextPage(page);
                if(nextUrl != null){
                    MatchRegex mr1 = new MatchRegex("amp;");
                    nextUrl = mr1.doValueReplaceAll(nextUrl, "");
                    mr1 = new MatchRegex(" ");
                    nextUrl = mr1.doValueReplaceAll(nextUrl, "%20");
                    nextSimple = false;
                }
            }
            if(nextUrl == null && nextSimple){
                nextUrl = AnalysisFunction.getNextPageSimpleMethod(page);
                nextComplicate = false;
            }
            if(hasDealedUrl.contains(nextUrl)){
                break;
            }else{
                hasDealedUrl.add(nextUrl);
            }
            page.setNextPage(nextUrl);
            if(stopToRest >= 3){
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FillMissingElement.class.getName()).log(Level.SEVERE, null, ex);
                }
                stopToRest = 0;
            }
        }
        StaticHelper.RESULTPAGES_FIELPATH = null;
    }
    
}
