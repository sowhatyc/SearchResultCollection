/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonTools;

import SearchResultsCollection.SearchResult;
import SearchResultsCollection.Item;
import SearchResultsCollection.MissingElement;
import SearchResultsCollection.MissingResult;
import java.io.*;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
        
/**
 *
 * @author admin
 */
public class FileStorage {
    private static Set<String> createdExcelFiles = new HashSet<String>();
    
    
    
    public static void addCreaatedFiles(String fileName){
        createdExcelFiles.add(fileName);
    }
//    private String fileDirectory;
    
    /**
     * @return the fileDirectory
     */
//    public String getFilePath() {
//        return fileDirectory;
//    }
//
//    /**
//     * @param fileDirectory the fileDirectory to set
//     */
//    public void setFilePath(String savePath) {
//        this.fileDirectory = savePath;
//    }
    
//    public boolean saveExcel(String path, ArrayList<SearchResult> searchResults, String url,String title) throws BiffException, WriteException, IOException{
////        ResouceType.Element element_title = new ResouceType.Element();
////        element_title.setElementContent(title);
////        element_title.setUrl(url);
////        for(int i = 0; i<itemString.size();i++){
////            itemString.get(i).add(0,element_title);
////        }
//        
//        return saveExcel(path,searchResults,url);
//    }
    
    
    public boolean  saveItemExcel(String path, ArrayList<Item> itemString, String url) throws IOException, BiffException, WriteException{
       
        if(itemString.isEmpty()){
            return false;
        }
//        this.readExcel(path);
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        int digit_index = 0;
        Workbook rw = null;
//        OutputStream os = null;
        if(createdExcelFiles.contains(path)){
            rw = jxl.Workbook.getWorkbook(new File(path));
            Sheet st = rw.getSheet(0);
            digit_index = st.getRows();
            if(digit_index + itemString.size() > 65535){
                int numberIndex = path.indexOf("_");
                int dotIndex = path.indexOf(".");
                String newFileName = path.substring(0, numberIndex+1) + (Integer.valueOf(path.substring(numberIndex+1, dotIndex)) + 1) + path.substring(dotIndex);
//                workbook.write();
                StaticHelper.RESULTPAGES_FIELPATH = newFileName;
//                workbook.close();
                rw.close();
                return saveItemExcel(newFileName, itemString, url);
            }
            workbook = Workbook.createWorkbook(new File(path),rw);
            sheet = workbook.getSheet(0);
            digit_index = sheet.getRows();
            System.err.println("digit_index = "+digit_index + " fileName = " + path + " itemSetLength = " + itemString.size());
            
//            rw.close();
        }else{
//            os = new FileOutputStream(path);
            workbook = Workbook.createWorkbook(new File(path));
//            workbook = Workbook.createWorkbook(os);
            sheet = workbook.createSheet("tianya", 0);
            createdExcelFiles.add(path);
            System.out.println(path + " is created!!!");
            digit_index = sheet.getRows();
            System.err.println("digit_index = "+digit_index + " fileName = " + path + " itemSetLength = " + itemString.size());
            
        }
        
//        WritableWorkbook workbook = Workbook.createWorkbook(new File(path));
        

//        int digit_index = sheet.getRows();
//        System.err.println("digit_index = "+digit_index + " fileName = " + path + " itemSetLength = " + itemString.size());
//        if(digit_index + itemString.size() > 300){
//            int numberIndex = path.indexOf("_");
//            int dotIndex = path.indexOf(".");
//            String newFileName = path.substring(0, numberIndex+1) + (Integer.valueOf(path.substring(numberIndex+1, dotIndex)) + 1) + path.substring(dotIndex);
//            workbook.write();
//            workbook.close();
//            saveItemExcel(newFileName, itemString, url);
//        }
//        Cell cell ;
        
//        if(path == null){
//            path = "E:\\AdaptiveDataFile\\guomeimei\\test.xls";
//        }
//        jxl.Workbook rw = jxl.Workbook.getWorkbook(new File(path));
//        WritableWorkbook workbook = Workbook.createWorkbook(new File(path),rw);
//        WritableSheet sheet = workbook.getSheet(0);
//        while(true){
//           
//           cell = sheet.getCell(0,digit_index);
////           System.out.println("cell = "+cell.getContents());
//           if(cell.getContents().length() == 0){
//               break;
//           }else{
//               digit_index++;
//           }
//        }
//        digit_index++;
        
//        Label label = new Label(1,digit_index, url); 
//        sheet.addCell(label);
        
        for(int i = 0; i<itemString.size();i++){
            int k =0;
            for(int j = 0;j<itemString.get(i).getElementNum();j++){
                if(j == 0){
                    sheet.addCell(new Label(0,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
                    sheet.addCell(new Label(1,i+digit_index,itemString.get(i).getElement(j).getUrl()));
                }
                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.Url_TYPE){
                    sheet.addCell(new Label(6,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
                    sheet.addCell(new Label(7,i+digit_index,itemString.get(i).getElement(j).getUrl()));
                }
                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.Url_ReleaseUser_TYPE){
                    sheet.addCell(new Label(8,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
                    sheet.addCell(new Label(9,i+digit_index,itemString.get(i).getElement(j).getUrl()));
                }
                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.Url_ReplyUser_TYPE){
                    sheet.addCell(new Label(10,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
                    sheet.addCell(new Label(11,i+digit_index,itemString.get(i).getElement(j).getUrl()));
                }
                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.TimeDate_TYPE){
                    sheet.addCell(new Label(5,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
                }
                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.ReplyTime_TYPE){
                    sheet.addCell(new Label(4,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
                }
                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.ReleaseTime_TYPE){
                    sheet.addCell(new Label(3,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
                }
                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.Parent_Title_TYPE){
                    sheet.addCell(new Label(12,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
                }
//                sheet.addCell(new Label(k,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
//                if(itemString.get(i).getElement(j).getUrl()!= null){
//                    sheet.addCell(new Label(k+1,i+digit_index,itemString.get(i).getElement(j).getUrl()));
//                    k++;
//                }
//                k++;
                
            }
        }
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
//        os.close();
        System.out.println("WebPage is saved whose url is ：("+url);
        return true;     
            
            
//            Label label = new Label(1,3, "A label record"); 
//            jxl.write.Number num;
//            num = new jxl.write.Number(i+2,digit_index ,number[i]);
//            sheet.addCell(num);
//        }
        
//        if(digit_index == 0){
//            Label label0 = new Label(2,0, "time"); 
//            Label label9 = new Label(3,0, "Data"); 
//            Label label1 = new Label(4,0, "urlSuperShort"); 
//            Label label2 = new Label(5,0, "urlShort"); 
//            Label label3 = new Label(6,0, "urlLong"); 
//            Label label4 = new Label(7,0, "nonUrlSuperShort"); 
//            Label label5 = new Label(8,0, "nonUrlShort"); 
//            Label label6 = new Label(9,0, "nonUrlLong"); 
//            Label label7 = new Label(10,0, "SuperLong"); 
//            Label label8 = new Label(11,0, "digit"); 
//            sheet.addCell(label0);
//            sheet.addCell(label1);
//            sheet.addCell(label2);
//            sheet.addCell(label3);
//            sheet.addCell(label4);
//            sheet.addCell(label5);
//            sheet.addCell(label6);
//            sheet.addCell(label7);
//            sheet.addCell(label8); 
//            sheet.addCell(label9);
//            digit_index++;
//        }
//        time_type = 0;
//    public final static int date_type = 1;
//    public final static int urlSuperShort_type = 2;
//    public final static int urlShort_type = 3;
//    public final static int urlLong_type =4;
//    public final static int nonUrlSuperShort_type =5;
//    public final static int nonUrlShort_type =6;
//    public final static int nonUrlLong_type =7;
//    public final static int nonUrlSuperLong_type =8;
//    public final static int digit_type =9;
        
        
      
    }
    
//    private void updateExcel(WritableSheet sheet, ArrayList<Item> itemString, int digit_index) throws WriteException{
//        
//        for(int i = 0; i<itemString.size();i++){
//            int k =0;
//            for(int j = 0;j<itemString.get(i).getElementNum();j++){
//                if(j == 0){
//                    sheet.addCell(new Label(0,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
//                    sheet.addCell(new Label(1,i+digit_index,itemString.get(i).getElement(j).getUrl()));
//                }
//                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.Url_TYPE){
//                    sheet.addCell(new Label(6,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
//                    sheet.addCell(new Label(7,i+digit_index,itemString.get(i).getElement(j).getUrl()));
//                }
//                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.Url_ReleaseUser_TYPE){
//                    sheet.addCell(new Label(8,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
//                    sheet.addCell(new Label(9,i+digit_index,itemString.get(i).getElement(j).getUrl()));
//                }
//                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.Url_ReplyUser_TYPE){
//                    sheet.addCell(new Label(10,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
//                    sheet.addCell(new Label(11,i+digit_index,itemString.get(i).getElement(j).getUrl()));
//                }
//                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.TimeDate_TYPE){
//                    sheet.addCell(new Label(5,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
//                }
//                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.ReplyTime_TYPE){
//                    sheet.addCell(new Label(4,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
//                }
//                if(itemString.get(i).getElement(j).getElementType()==StaticHelper.ReleaseTime_TYPE){
//                    sheet.addCell(new Label(3,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
//                }
////                sheet.addCell(new Label(k,i+digit_index,itemString.get(i).getElement(j).getElementContent()));
////                if(itemString.get(i).getElement(j).getUrl()!= null){
////                    sheet.addCell(new Label(k+1,i+digit_index,itemString.get(i).getElement(j).getUrl()));
////                    k++;
////                }
////                k++;
//                
//            }
//        }
//    }
    
    public boolean updateSearchResultExcel(String path, ArrayList<MissingResult> missingResults, int startRow, int sheetNum) throws IOException, BiffException, WriteException{
        if(missingResults.isEmpty()){
            return false;
        }
        
        
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        int digit_index = 0;
        Workbook rw = null;
        
        StaticHelper.START_ROW = startRow;
        
        if(createdExcelFiles.contains(path)){
            rw = jxl.Workbook.getWorkbook(new File(path));
            Sheet st = rw.getSheet(sheetNum);
            digit_index = st.getRows();
            if(digit_index + missingResults.size() > 65535){
                int numberIndex = path.lastIndexOf("_");
                int dotIndex = path.indexOf(".");
                String newFileName = path.substring(0, numberIndex+1) + (Integer.valueOf(path.substring(numberIndex+1, dotIndex)) + 1) + path.substring(dotIndex);
//                workbook.write();
                StaticHelper.RESULTPAGES_FIELPATH = newFileName;
//                workbook.close();
                rw.close();
                return updateSearchResultExcel(newFileName, missingResults, 0, sheetNum);
            }
            workbook = Workbook.createWorkbook(new File(path),rw);
            sheet = workbook.getSheet(sheetNum);
            digit_index = sheet.getRows();
            System.err.println("digit_index = "+digit_index + " fileName = " + path + " itemSetLength = " + missingResults.size());
            
//            rw.close();
        }else{
//            os = new FileOutputStream(path);
            workbook = Workbook.createWorkbook(new File(path));
//            workbook = Workbook.createWorkbook(os);
            sheet = workbook.createSheet("tianya", sheetNum);
            createdExcelFiles.add(path);
            System.out.println(path + " is created!!!");
            digit_index = sheet.getRows();
            System.err.println("digit_index = "+digit_index + " fileName = " + path + " itemSetLength = " + missingResults.size());
            
        }
        
//        Workbook rw = jxl.Workbook.getWorkbook(new File(path));
//        WritableWorkbook workbook = Workbook.createWorkbook(new File(path),rw);
//        WritableSheet sheet = workbook.getSheet(0);
        for(int i=0; i<missingResults.size(); ++i){
            if(missingResults.get(i) == null){
                System.out.println("do not update line " + (i+startRow+1));
                continue;
            }
            ArrayList<MissingElement> missingElements = missingResults.get(i).getMissingElements();
            for(MissingElement me : missingElements){
                sheet.addCell(new Label(me.getMissingPosition(),i+startRow,me.getMisstingContent()));
            }
        }
        
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
//        os.close();
        System.err.println("update excel done!!!!");
        return true; 
//        workbook.write(); 
//        workbook.close();
//        if(rw != null){
//            rw.close();
//        }
//        System.err.println("update excel done!!!!");
//        return true;
    }
    
    
    public boolean  saveSearchResultExcel(String path, ArrayList<SearchResult> searchResults, String url) throws IOException, BiffException, WriteException{
        if(searchResults.isEmpty()){
            return false;
        }
//        this.readExcel(path);
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        int digit_index = 0;
        Workbook rw = null;
//        OutputStream os = null;
        if(createdExcelFiles.contains(path)){
            rw = jxl.Workbook.getWorkbook(new File(path));
            Sheet st = rw.getSheet(1);
            digit_index = st.getRows();
            if(digit_index + searchResults.size() > 65535){
                int numberIndex = path.indexOf("_");
                int dotIndex = path.indexOf(".");
                String newFileName = path.substring(0, numberIndex+1) + (Integer.valueOf(path.substring(numberIndex+1, dotIndex)) + 1) + path.substring(dotIndex);
//                workbook.write();
                StaticHelper.RESULTPAGES_FIELPATH = newFileName;
//                workbook.close();
                rw.close();
                return saveSearchResultExcel(newFileName, searchResults, url);
            }
            workbook = Workbook.createWorkbook(new File(path),rw);
            sheet = workbook.getSheet(1);
            digit_index = sheet.getRows();
            System.err.println("digit_index = "+digit_index + " fileName = " + path + " itemSetLength = " + searchResults.size());

//            rw.close();
        }else{
//            os = new FileOutputStream(path);
            workbook = Workbook.createWorkbook(new File(path));
//            workbook = Workbook.createWorkbook(os);
            sheet = workbook.createSheet("tianya", 1);
            createdExcelFiles.add(path);
            System.out.println(path + " is created!!!");
            digit_index = sheet.getRows();
            System.err.println("digit_index = "+digit_index + " fileName = " + path + " itemSetLength = " + searchResults.size());

        }
//        this.readExcel(path);
        
//        int digit_index =0;
//         Cell cell ;
////        WritableWorkbook workbook = Workbook.createWorkbook(new File(path));
//         if(searchResults.isEmpty()){
//             return false;
//         }
//        if(path == null){
//            CTs.priorPrint("save excel path is null", 1);
//            return false;
//        }
//        jxl.Workbook rw = jxl.Workbook.getWorkbook(new File(path));
//        WritableWorkbook workbook = Workbook.createWorkbook(new File(path),rw);
//        
//        WritableSheet sheet = workbook.getSheet(0);
//        digit_index =  sheet.getRows();
////        while(true){
////           
////           cell = sheet.getCell(0,digit_index);
//////           System.out.println("cell = "+cell.getContents());
////           if(cell.getContents().length() == 0){
////               break;
////           }else{
////               digit_index++;
////           }
////        }
////        digit_index++;
//        System.err.println("digit_index = "+digit_index);
//        Label label = new Label(1,digit_index, url); 
//        sheet.addCell(label);
        
        for(int i = 0; i<searchResults.size();i++){
            int k=0;
            sheet.addCell(new Label(k++,i+digit_index,searchResults.get(i).getUrl()));
            sheet.addCell(new Label(k++,i+digit_index,searchResults.get(i).getPostTime()));
            sheet.addCell(new Label(k++,i+digit_index,searchResults.get(i).getReplyCount()));
            sheet.addCell(new Label(k++,i+digit_index,searchResults.get(i).getVisitCount()));
            sheet.addCell(new Label(k++,i+digit_index,searchResults.get(i).getDescription()));
        }
        workbook.write(); 
        workbook.close();
        if(rw != null){
            rw.close();
        }
//        os.close();
        System.out.println("WebPage is saved whose url is ：("+url);
        return true;    
            
            
//            Label label = new Label(1,3, "A label record"); 
//            jxl.write.Number num;
//            num = new jxl.write.Number(i+2,digit_index ,number[i]);
//            sheet.addCell(num);
//        }
        
//        if(digit_index == 0){
//            Label label0 = new Label(2,0, "time"); 
//            Label label9 = new Label(3,0, "Data"); 
//            Label label1 = new Label(4,0, "urlSuperShort"); 
//            Label label2 = new Label(5,0, "urlShort"); 
//            Label label3 = new Label(6,0, "urlLong"); 
//            Label label4 = new Label(7,0, "nonUrlSuperShort"); 
//            Label label5 = new Label(8,0, "nonUrlShort"); 
//            Label label6 = new Label(9,0, "nonUrlLong"); 
//            Label label7 = new Label(10,0, "SuperLong"); 
//            Label label8 = new Label(11,0, "digit"); 
//            sheet.addCell(label0);
//            sheet.addCell(label1);
//            sheet.addCell(label2);
//            sheet.addCell(label3);
//            sheet.addCell(label4);
//            sheet.addCell(label5);
//            sheet.addCell(label6);
//            sheet.addCell(label7);
//            sheet.addCell(label8); 
//            sheet.addCell(label9);
//            digit_index++;
//        }
//        time_type = 0;
//    public final static int date_type = 1;
//    public final static int urlSuperShort_type = 2;
//    public final static int urlShort_type = 3;
//    public final static int urlLong_type =4;
//    public final static int nonUrlSuperShort_type =5;
//    public final static int nonUrlShort_type =6;
//    public final static int nonUrlLong_type =7;
//    public final static int nonUrlSuperLong_type =8;
//    public final static int digit_type =9;
        
        
      
    }
    
    public boolean saveFile(String fileDirectory, String fileName, String fileContent){
//      
        fileName = verifyFileName(fileName + "===" + System.currentTimeMillis() + ".txt");
        return saveFile(fileDirectory+fileName, fileContent);
    }
    
    public boolean saveFile(String filePath, String content){
        File file = new File(filePath);
//        if(file.exists()){
//            System.out.println("File already exist!!!!");
//            return false;
//        }
        if(!file.getParentFile().exists()){
            System.out.println("parent directory is now creating!!!");
            if(!file.getParentFile().mkdirs()){
                System.out.println("create parent directory is failed!!!");
                return false;
            }
        }
        try {
//            file.createNewFile();
            FileWriter fw;
            fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            fw.close();
            return true;
        } catch (IOException ex) {
            System.out.println("File create fail!!!!");
            ex.printStackTrace();
            return false;
        }
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
    
    public String readFile(String filePath){
        String fileContent = null;
        File file = new File(filePath);
        if(file.exists()){
            BufferedReader reader = null;
            try {
                fileContent = " ";
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                String line = null;
                while((line = reader.readLine()) != null){
//                    System.out.println(line);
                    fileContent += line;
                    fileContent += "  ";
                }
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
            } finally{
                if(reader != null)
                    try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return fileContent;
    }
    
    private String verifyFileName(String fileName){
        MatchRegex mr = new MatchRegex("[<>/\\|:\"*?、《》：“”？]");
        return mr.doValueReplaceAll(fileName, "_");
    }
    
    public ArrayList<SearchResult> readSearchResultExcel(String filePath){
        ArrayList<SearchResult> resultList = new ArrayList<SearchResult>();
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = new FileInputStream(filePath);
            workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(2);
            int column = sheet.getColumns();
            int row = sheet.getRows();
            System.out.println("colunm row = " + column + " " + row);
            for(int i=0; i<row; ++i){
//                System.out.println("******" + i + "**********");
//                String url = sheet.getCell(0, i).getContents();
//                String time = sheet.getCell(1, i).getContents();
//                String reply = sheet.getCell(2, i).getContents();
//                String visit = sheet.getCell(3, i).getContents();
                resultList.add(new SearchResult(sheet.getCell(0, i).getContents(), sheet.getCell(1, i).getContents(), sheet.getCell(2, i).getContents(), sheet.getCell(3, i).getContents(), sheet.getCell(4, i).getContents()));
            }
        } catch (IOException ex) {
            Logger.getLogger(FileStorage.class.getName()).log(Level.SEVERE, null, ex);
            return resultList;
        } catch (BiffException ex) {
            Logger.getLogger(FileStorage.class.getName()).log(Level.SEVERE, null, ex);
            return resultList;
        } 
        return resultList;
    }
    
    public ArrayList<String> getFileURLListFromExcel(String filePath, int sheetNum) throws FileNotFoundException, IOException, BiffException{
        ArrayList<String> resultList = new ArrayList<String>();
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = new FileInputStream(filePath);
            workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(sheetNum);
            int column = sheet.getColumns();
            int row = sheet.getRows();
            System.out.println("colunm row = " + column + " " + row);
            for(int i=0; i<row; ++i){
                resultList.add(sheet.getCell(0,i).getContents());
            }
        }catch (IOException ex) {
            Logger.getLogger(FileStorage.class.getName()).log(Level.SEVERE, null, ex);
            return resultList;
        } catch (BiffException ex) {
            Logger.getLogger(FileStorage.class.getName()).log(Level.SEVERE, null, ex);
            return resultList;
        } 
        return resultList;
    }
}
