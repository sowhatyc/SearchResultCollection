/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonTools;


import java.io.*;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import weibosearch.SearchResult;
        
/**
 *
 * @author admin
 */
public class FileStorage {
    
        
    
    
    
    public static boolean  saveWeiboSearchResultExcel(String path, ArrayList<SearchResult> searchResults) throws IOException, BiffException, WriteException{
        if(searchResults.isEmpty()){
            return false;
        }
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        int digit_index = 0;
        Workbook rw = null;
        File file = new File(path);
        if(file.exists()){
            rw = jxl.Workbook.getWorkbook(file);
            Sheet st = rw.getSheet(0);
            digit_index = st.getRows();
            if(StaticHelper.START_ROW + searchResults.size() > 65535){
                int numberIndex = path.lastIndexOf("_");
                int dotIndex = path.indexOf(".");
                String newFileName = path.substring(0, numberIndex+1) + (Integer.valueOf(path.substring(numberIndex+1, dotIndex)) + 1) + path.substring(dotIndex);
                rw.close();
                StaticHelper.START_ROW = 0;
                return saveWeiboSearchResultExcel(newFileName, searchResults);
            }
            workbook = Workbook.createWorkbook(file,rw);
            sheet = workbook.getSheet(0);
            digit_index = sheet.getRows();
            System.err.println("digit_index = "+digit_index + " statrRow = " + StaticHelper.START_ROW + " fileName = " + path + " itemSetLength = " + searchResults.size());
        }else{
            workbook = Workbook.createWorkbook(file);
            sheet = workbook.createSheet("Sheet1", 0);
            System.out.println(path + " is created!!!");
            digit_index = sheet.getRows();
            System.err.println("digit_index = "+digit_index + " statrRow = " + StaticHelper.START_ROW + " fileName = " + path + " itemSetLength = " + searchResults.size());
        }
        StaticHelper.FILE_PATH = path;
        for(int i = 0; i<searchResults.size();i++){
            Map<Integer, Object> attributes = searchResults.get(i).getAttributes();
            Iterator<Integer> keysIter = attributes.keySet().iterator();
            while(keysIter.hasNext()){
                Integer position = keysIter.next();
                if(position.intValue() >= 0){
                    Object o = attributes.get(position);
                    if(o instanceof String){
                        sheet.addCell(new Label(position.intValue(), i+StaticHelper.START_ROW, (String) o));
                    }else if(o instanceof Date){
                        sheet.addCell(new DateTime(position.intValue(), i+StaticHelper.START_ROW, (Date) o));
                    }else if(o instanceof Integer){
                        sheet.addCell(new jxl.write.Number(position.intValue(), i+StaticHelper.START_ROW, (int) o));
                    }
                }
            }
//            sheet.addCell(new Label(k++,i+StaticHelper.START_ROW,searchResults.get(i).getMid()));
//            sheet.addCell(new DateTime(k++,i+StaticHelper.START_ROW,searchResults.get(i).getDate()));
//            sheet.addCell(new jxl.write.Number(k++,i+StaticHelper.START_ROW,searchResults.get(i).getRepostsCount()));
//            sheet.addCell(new jxl.write.Number(k++,i+StaticHelper.START_ROW,searchResults.get(i).getCommentsCount()));
//            sheet.addCell(new Label(k++,i+StaticHelper.START_ROW,searchResults.get(i).getText()));
//            sheet.addCell(new Label(k++,i+StaticHelper.START_ROW,searchResults.get(i).getUserID()));
//            sheet.addCell(new Label(k++,i+StaticHelper.START_ROW,searchResults.get(i).getUserName()));
//            if(searchResults.get(i).getRetweetedID() != null){
//                sheet.addCell(new Label(k++,i+StaticHelper.START_ROW,searchResults.get(i).getRetweetedID()));
//                sheet.addCell(new DateTime(k++,i+StaticHelper.START_ROW,searchResults.get(i).getRetweetedDate()));
//                sheet.addCell(new Label(k++,i+StaticHelper.START_ROW,searchResults.get(i).getRetweetedText()));
//                sheet.addCell(new Label(k++,i+StaticHelper.START_ROW,searchResults.get(i).getRetweetedName()));
//            }
        }
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
//        os.close();
        return true;    
    }
    
    
    
    public ArrayList<String> getFileList(String filePath){
        ArrayList<String> fileList = new ArrayList<String>();
        if(filePath == null){
            System.out.println("filePath has not benn given!!!");
            return null;
        }
        File fileDirectory = new File(filePath);
        if(!fileDirectory.isDirectory()){
            System.out.println("given filepath is not a directory!!!");
            return null;
        }
        File[] files = fileDirectory.listFiles();
        for(File file : files){
            if(file.isDirectory()){
                fileList.addAll(getFileList(file.getPath()));
            }else{
                fileList.add(file.getPath());
            }
        }
        return fileList;
    }
    
    public static ArrayList<Object> getContentFromExcel(String filePath, int sheetNum, int position, int startRow, int endRow){
        ArrayList<Object> contentList = new ArrayList<Object>();
        Workbook workbook = null;
        File file = new File(filePath);
        if(!file.exists()){
            System.err.println("************  file is not exist ***********");
            return null;
        }
        try {
            workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(sheetNum);
            int column = sheet.getColumns();
            int row = sheet.getRows();
            System.out.println("colunm row = " + column + " " + row);
            if(endRow < 0){
                endRow = row;
            }
            for(int i=startRow; i<endRow; ++i){
                contentList.add(sheet.getCell(position,i).getContents());
            }
        }catch (IOException ex) {
            Logger.getLogger(FileStorage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(FileStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contentList;
    }
}
