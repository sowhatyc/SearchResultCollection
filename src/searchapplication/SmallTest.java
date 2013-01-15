/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package searchapplication;

import CommonTools.DataValue;
import CommonTools.FileStorage;
import CommonTools.MatchRegex;
import SearchResultsCollection.WebCrawler;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 *
 * @author admin
 */
public class SmallTest {
    
    public static ArrayList<String> generateDate(String startDate, String endDate, String initialUrl){
        if(startDate.compareTo(endDate) > 0){
            System.out.println("startDate is later than endDate!!!!");
            return null;
        }
        ArrayList<String> urlList = new ArrayList<String>();
        MatchRegex mr = new MatchRegex("(\\d+)");
        DataValue dv = mr.doValueMatch(startDate);
        if(dv.getFindNum() != 3){
            System.out.println("Your date patter is not correct  !!!!!!");
            return null;
        }
        int startYear = Integer.valueOf(dv.getValueAt(0)[0]);
        int startMonth = Integer.valueOf(dv.getValueAt(1)[0]);
        int startDay = Integer.valueOf(dv.getValueAt(2)[0]);
        String newStartDate = startDate;
        mr = new MatchRegex("\\d{4}([-/])\\d{2}([-/])\\d{2}");
        dv = mr.doValueMatch(initialUrl);
        int urlDateStartIndex = -1;
        int urlDateEndIndex = -1;
        urlDateStartIndex = mr.getFirstStartIndex(initialUrl);
        urlDateEndIndex = mr.getFirstEndIndex(initialUrl);
        while(newStartDate.compareTo(endDate) <= 0){
            urlList.add(initialUrl.substring(0, urlDateStartIndex) + newStartDate + initialUrl.substring(urlDateEndIndex));
            ++startDay;
            if(startDay > 28 && startMonth == 2){
                startDay = 1;
                startMonth = 3;
            }else if(startDay > 30 && (startMonth == 4 || startMonth == 6 || startMonth == 9 || startMonth == 11)){
                startDay = 1;
                startMonth += 1;
            }else if(startDay > 31 && (startMonth == 1 || startMonth == 3 || startMonth == 5 || startMonth == 7 || startMonth == 8 || startMonth == 10 || startMonth == 12)){
                startDay = 1;
                startMonth += 1;
            }
            if(startMonth > 12){
                startMonth = 1;
                startYear += 1;
            }
            newStartDate = "" + startYear + dv.getValueAt(0)[0];
            if(startMonth < 10){
                newStartDate += "0";
            }
            newStartDate += startMonth + dv.getValueAt(0)[1];
            if(startDay < 10){
                newStartDate += "0";
            }
            newStartDate += startDay;
        }
        return urlList;
    }
    
    public static void readDate(String filePath, int datePosition, int dateCountPosition, ArrayList<String> date, ArrayList<String> dateCount, ArrayList<String> wholeDate){
        try {
            InputStream is = null;
            Workbook workbook = null;
            is = new FileInputStream(filePath);
            workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(0);
            int column = sheet.getColumns();
            int row = sheet.getRows();
            System.out.println("colunm row = " + column + " " + row);
            String startDate = "2100-12-31";
            String endDate = "1900-01-01";
            for(int i=0; i<row; ++i){
                if(sheet.getCell(dateCountPosition, i).getContents() == null || sheet.getCell(dateCountPosition, i).getContents().equals("")){
                    dateCount.add(null);
                    date.add(sheet.getCell(datePosition, i).getContents());
                }else{
                    String datetime = sheet.getCell(datePosition, i).getContents();
                    if(datetime.compareTo(startDate) < 0){
                        startDate = datetime;
                    }
                    if(datetime.compareTo(endDate) > 0){
                        endDate = datetime;
                    }
                    date.add(datetime);
                    dateCount.add(sheet.getCell(dateCountPosition, i).getContents());
                }
            }
            wholeDate.add(startDate);
            wholeDate.add(endDate);
            
        } catch (IOException ex) {
            Logger.getLogger(SmallTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(SmallTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void completeDate(String destFilePath, ArrayList<String> date, ArrayList<String> dateCount, ArrayList<String> wholeDate, int destPosition1, int destPosition2) throws IOException, BiffException, WriteException{
        File file = new File(destFilePath);
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        int digit_index = 0;
        Workbook rw = null;
       
        workbook = Workbook.createWorkbook(file);
//            workbook = Workbook.createWorkbook(os);
        sheet = workbook.createSheet("Sheet1", 1);
        System.out.println(destFilePath + " is created!!!");
        digit_index = sheet.getRows();
        System.err.println("digit_index = "+digit_index + " fileName = " + destFilePath + " itemSetLength = " + date.size()); 
        
        int startRow1 = 0;
        int startRow2 = 0;
        for(int i=0; i<date.size(); ++i){
            if(((i+1) < date.size()) && dateCount.get(i) == null && dateCount.get(i+1) != null){
                sheet.addCell(new Label(destPosition2, startRow1, date.get(i)));
                ++startRow1;
            }
            if(dateCount.get(i) != null){
                for(int j=startRow2; j<wholeDate.size(); ++j){
                    if(wholeDate.get(j).compareTo(date.get(i)) < 0){
                        sheet.addCell(new Label(destPosition1, startRow1, wholeDate.get(j)));
                        sheet.addCell(new Label(destPosition2, startRow1, "0"));
                        ++startRow1;
                    }else{
                        sheet.addCell(new Label(destPosition1, startRow1, wholeDate.get(j)));
                        sheet.addCell(new Label(destPosition2, startRow1, dateCount.get(i)));
                        ++startRow1;
                        startRow2 = j + 1;
                        break;
                    }
                }
            }
            if(dateCount.get(i) != null && dateCount.get(i+1) == null){
                for(int j=startRow2; j<wholeDate.size(); ++j){
                    sheet.addCell(new Label(destPosition1, startRow1, wholeDate.get(j)));
//                    sheet.addCell(new Label(, startRow1, "0"));
                    sheet.addCell(new Label(destPosition2, startRow1, "0"));
                    ++startRow1;
                }
                destPosition1 = 0;
                destPosition2 += 1;
                startRow1 = 0;
                startRow2 = 0;
            }
        }
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
        System.err.println("update excel done!!!!");
        
    }
    
    public static void statisticOfOnWay(String filePath, ArrayList<String> words, int sourcePosition, int destPosition, int startRow) throws IOException, BiffException, WriteException{
        Workbook rw = jxl.Workbook.getWorkbook(new File(filePath));
        WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath),rw);
        WritableSheet sheet = workbook.getSheet(10);
        
        int column = sheet.getColumns();
        int row = sheet.getRows();
        System.out.println("colunm row = " + column + " " + row);
        boolean find = false;
        for(int i=startRow; i<row; ++i){
            String content = sheet.getCell(sourcePosition, i).getContents();
            find = false;
            for(String word: words){
                if(content.contains(word)){
                    try {
                        sheet.addCell(new Label(destPosition, i, "1"));
                        find = true;
                        break;
                    } catch (WriteException ex) {
                        Logger.getLogger(SmallTest.class.getName()).log(Level.SEVERE, null, ex);
                        break;
                    } 
                }
            }
            if(find == false){
                try {
                    sheet.addCell(new Label(destPosition, i, "0"));
                } catch (WriteException ex) {
                    Logger.getLogger(SmallTest.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
        System.err.println("update excel done!!!!");
    }
    
    public static void clearContent(String filePath, int sheetNum, int position, int startRow, String regex) throws IOException, BiffException, WriteException{
        Workbook rw = jxl.Workbook.getWorkbook(new File(filePath));
        WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath),rw);
        WritableSheet sheet = workbook.getSheet(sheetNum);
        
        int column = sheet.getColumns();
        int row = sheet.getRows();
        System.out.println("colunm row = " + column + " " + row);
        
        for(int i=startRow; i<row; ++i){
            String content = sheet.getCell(position, i).getContents();
            MatchRegex mr = new MatchRegex(regex);
            content = mr.doValueReplaceAll(content, "");
            sheet.addCell(new Label(position, i, content.trim()));
        }
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
        System.err.println("update excel done!!!!");
    }
    
    public static void spiltContent(String filePath, int sheetNum, int spiltedPostion, int anotherPostion, int startRow, String spiltTag) throws IOException, BiffException, WriteException{
        Workbook rw = jxl.Workbook.getWorkbook(new File(filePath));
        WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath),rw);
        WritableSheet sheet = workbook.getSheet(sheetNum);
        
        int column = sheet.getColumns();
        int row = sheet.getRows();
        System.out.println("colunm row = " + column + " " + row);
        
        for(int i=startRow; i<row; ++i){
            String content = sheet.getCell(spiltedPostion, i).getContents();
            int spiltIndex = content.indexOf(spiltTag);
            String content_1 = content.substring(0, spiltIndex);
            String content_2 = content = content.substring(spiltIndex+spiltTag.length());
            sheet.addCell(new Label(spiltedPostion, i, content_1.trim()));
            sheet.addCell(new Label(anotherPostion, i, content_2.trim()));
        }
        
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
        System.err.println("update excel done!!!!");
        
    }
    public static void chooseSomething(String filePath, int sheetNum, int sourcePostion, int destPostion, int startRow, String regex, boolean singleOne) throws IOException, BiffException, WriteException{
        Workbook rw = jxl.Workbook.getWorkbook(new File(filePath));
        WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath),rw);
        WritableSheet sheet = workbook.getSheet(sheetNum);
        int column = sheet.getColumns();
        int row = sheet.getRows();
        System.out.println("colunm row = " + column + " " + row);
        for(int i=startRow; i<row; ++i){
            String content = sheet.getCell(sourcePostion, i).getContents();
            String choosedContent = "";
            MatchRegex mr = new MatchRegex(regex);
            DataValue dv = mr.doValueMatch(content);
            if(dv.getFindNum() > 0){
                if(singleOne){
                    choosedContent = dv.getValueAt(0)[0];
                }else{
                    for(int j=0; j<dv.getFindNum(); ++j){
                        choosedContent += dv.getValueAt(j)[0] + " # ";
                    }
                }
                sheet.addCell(new Label(destPostion, i, choosedContent.trim()));
            }
        }
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
        System.err.println("update excel done!!!!");
    }
    
    
    public static void addSomething(String filePath, int sheetNum, int position, int startRow, String something) throws BiffException, IOException, RowsExceededException, WriteException{
        Workbook rw = jxl.Workbook.getWorkbook(new File(filePath));
        WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath),rw);
        WritableSheet sheet = workbook.getSheet(sheetNum);
        
        int column = sheet.getColumns();
        int row = sheet.getRows();
        System.out.println("colunm row = " + column + " " + row);
        
        for(int i=startRow; i<row; ++i){
            String content = sheet.getCell(position, i).getContents();
            content = something + content;
            sheet.addCell(new Label(position, i, content.trim()));
        }
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
        System.err.println("update excel done!!!!");
    }
    
    public static void getContentFromWeb(String filePath, int sheetNum, int position, int startRow, String regex, String something) throws IOException, BiffException, WriteException{
        Workbook rw = jxl.Workbook.getWorkbook(new File(filePath));
        WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath),rw);
        WritableSheet sheet = workbook.getSheet(sheetNum);
        int column = sheet.getColumns();
        int row = sheet.getRows();
        System.out.println("colunm row = " + column + " " + row);
        int stopToRest = 0;
        for(int i=startRow; i<row; ++i){
            String url = sheet.getCell(position, i).getContents();
            String content = WebCrawler.getPageContent(url);
            ++stopToRest;
            MatchRegex mr = new MatchRegex("regex");
            DataValue dv = mr.doValueMatch(content);
            int notFound = 0;
            while(dv.getFindNum() == 0){
                ++notFound;
                content = WebCrawler.getPageContent(url);
                ++stopToRest;
                dv = mr.doValueMatch(content); 
                if(notFound > 2){
                    break;
                }
            }
            if(notFound > 2){
                continue;
            }
            String newUrl = something + dv.getValueAt(0)[0];
            sheet.addCell(new Label(position, i, newUrl));
            if(stopToRest > 7){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SmallTest.class.getName()).log(Level.SEVERE, null, ex);
                }
                stopToRest = 0;
            }
        }
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
        System.err.println("update excel done!!!!");
    }
    
    public static void testSomething(String filePath, int sheetNum, int position, int startRow, String something) throws BiffException, IOException, RowsExceededException, WriteException{
        Workbook rw = jxl.Workbook.getWorkbook(new File(filePath));
        WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath),rw);
        WritableSheet sheet = workbook.getSheet(sheetNum);
        
        int column = sheet.getColumns();
        int row = sheet.getRows();
        System.out.println("colunm row = " + column + " " + row);
        
        for(int i=startRow; i<row; ++i){
            String content = sheet.getCell(position, i).getContents();
            if(!content.contains(something)){
                System.out.println("the " + i + " line does not contain " + something);
            }
//            sheet.addCell(new Label(position, i, content.trim()));
        }
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
        System.err.println("update excel done!!!!");
    }
    
    
    public static void statisticOfRegion(String sourceFilePath, String destFilePath, int startPostion, int startRow) throws IOException, BiffException, WriteException{
        Workbook rw = jxl.Workbook.getWorkbook(new File(sourceFilePath));
        Sheet readSheet = rw.getSheet(0);
        WritableWorkbook workbook = Workbook.createWorkbook(new File(destFilePath));
        WritableSheet saveSheet = workbook.createSheet("qczj_region", 0);
        
        int column = readSheet.getColumns();
        int row = readSheet.getRows();
        System.out.println("colunm row = " + column + " " + row);
        
        Map<String, Integer> regionMap = new HashMap<String, Integer>();
        for(int i=startRow; i<row; ++i){
            for(int j=startPostion; j<column; j=j+2){
                String content = readSheet.getCell(j, i).getContents();
                if(content.equals("")){
                    break;
                }
                int spaceIndex = -1;
                while((spaceIndex = content.indexOf(" ")) != -1){
                    String word = content.substring(0, spaceIndex);
                    content = content.substring(spaceIndex+1);
                    if(regionMap.containsKey(word)){
                        regionMap.put(word, regionMap.get(word)+1);
                    }else{
                        regionMap.put(word, 1);
                    }
                }
                if(regionMap.containsKey(content)){
                    regionMap.put(content, regionMap.get(content)+1);
                }else{
                    regionMap.put(content, 1);
                }
            }
        }
        
        Set<String> regionNames = regionMap.keySet();
        int rowNumber = 0;
        for(String regionName : regionNames){
            saveSheet.addCell(new Label(0, rowNumber, regionName));
            saveSheet.addCell(new Label(1, rowNumber, String.valueOf(regionMap.get(regionName))));
            ++rowNumber;
        }
        
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
        System.err.println("update excel done!!!!");
    }
    
    
    public static void main(String[] args){
        String sourceFilePath = "E:\\data\\By_yc_\\社会1_1.xls";
//        String destFilePath = "GQQCZJ_Region_2012_09_10.xls";
//        ArrayList<String> words = new ArrayList<String>();
//        words.add("自驾");
//        words.add("游");
//        words.add("记");
//        words.add("回家");
//        words.add("旅");
//        words.add("出门");
//        words.add("作业");
//        words.add("出门");
//        words.add("老家");
//        words.add("拍");
////        words.add("路");
//        words.add("车友");
//        words.add("奔");
//        words.add("归来");
//        words.add("行");
//        words.add("打算");
//        words.add("高速");
//        words.add("照片");
//        words.add("堵");
//        words.add("跑");
//        words.add("公里");
//        words.add("km");
//        words.add("油耗");
//        words.add("玩");
//        words.add("美景");
//        words.add("拼车");
//            statisticOfOnWay(sourceFilePath, words, 6, 4, 0);
//            statisticOfRegion(sourceFilePath, destFilePath, 10, 0);
//            clearContent(sourceFilePath, 4, 0);
//            testSomething(sourceFilePath, 1, 5, 0, "十八大");
//            addSomething(sourceFilePath, 0, 0, 0, "http://liaoba.people.com.cn/");
//            clearContent(sourceFilePath, 0, 2, 0);
//            spiltContent(sourceFilePath, 0, 3, 4, 0, "/");
//            clearContent(sourceFilePath, 0, 5, 0, "@.*?<br>");
//            clearContent(sourceFilePath, 0, 5, 0, ".*?-----<br>");
//            clearContent(sourceFilePath, 0, 5, 0, ".*?=====<br>");
//            clearContent(sourceFilePath, 0, 5 ,0, "<[^>]*>");
//            getContentFromWeb(sourceFilePath, 0, 0, 0, "<a href=\"([^\"]+)\">同主题阅读</a>", "http://forum.home.news.cn");
//            for(int i=6; i<12; ++i){
//                addSomething(sourceFilePath, i, 0, 0, "http://bbs.ifeng.com/");
//            }
//            ArrayList<String> fileList = new FileStorage().getFileList("E:/data/18TY_domain/18TY_ZZ");
//            ArrayList<String> fileList = new ArrayList<String>();
//            fileList.add("E:\\data\\18KD_domain\\凯迪社区领域数据.xls");
//            for(String file: fileList){
////                chooseSomething(file, 0, 5, 6, 0, "@([^\\s<+]*?)[\\s<+]", false);
//                addSomething(file, 1, 0, 0, "http://club.kdnet.net");
//                spiltContent(file, 1, 5, 6, 0, "/");
//                System.out.println(file + " is done!");
//            }
//            ArrayList<String> date = new ArrayList<String>();
//            ArrayList<String> dateCount = new ArrayList<String>();
//            ArrayList<String> wholeDate = new ArrayList<String>();
//            readDate(sourceFilePath, 0, 1, date, dateCount, wholeDate);
//            System.out.println(wholeDate.get(0) + " " + wholeDate.get(1));
//            int dotIndex = sourceFilePath.indexOf(".");
//            wholeDate = generateDate(wholeDate.get(0), wholeDate.get(1), "2012-01-13");
//            String destFilePath = sourceFilePath.substring(0, dotIndex) + "_byYc" + sourceFilePath.substring(dotIndex);
        ArrayList<String> date = generateDate("2012-11-14", "2012-12-19", "1988-01-10");
        System.out.println(date.size());
//        try {
//            completeDate(destFilePath, date, dateCount, wholeDate, 0, 1);
//        } catch (IOException ex) {
//            Logger.getLogger(SmallTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (BiffException ex) {
//            Logger.getLogger(SmallTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (WriteException ex) {
//            Logger.getLogger(SmallTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
}
