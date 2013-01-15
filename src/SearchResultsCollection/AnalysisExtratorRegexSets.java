/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class AnalysisExtratorRegexSets {

  
    String urlMatchRegex;
    ArrayList<String> blockContentMatchRegex;
    
    
    public AnalysisExtratorRegexSets(String urlMatchRegex, ArrayList<String> blockContentMatchRegex) {
        this.urlMatchRegex = urlMatchRegex;
        this.blockContentMatchRegex = blockContentMatchRegex;
    }
    public AnalysisExtratorRegexSets() {
        this.urlMatchRegex = null;
        this.blockContentMatchRegex = new ArrayList();
    }
    public ArrayList<String> getBlockContentMatchRegexSets() {
        return blockContentMatchRegex;
    }
    public String getBlockContentMatchRegex(int index){
        return blockContentMatchRegex.get(0);
    }
    public boolean insertBlockContentMatchRegex(String urlRegex , String temp){
        if(urlMatchRegex == null){
            urlMatchRegex = urlRegex;
        }else{
            if(!urlMatchRegex.equals(urlRegex)){
                return false;
            }
        }
        if(temp != null){
            this.blockContentMatchRegex.add(temp);
            return true;
        }
        return false;
    }
    public boolean insertBlockContentMatchRegex(String urlRegex,String temp, int index){
        if(urlMatchRegex == null){
            urlMatchRegex = urlRegex;
        }else{
            if(!urlMatchRegex.equals(urlRegex)){
                return false;
            }
        }
        if(temp != null&&index>-1){
            this.blockContentMatchRegex.add(index, temp);
            return true;
        }
        return false;
    }
    public boolean setBlockContentMatchRegex(String urlRegex , ArrayList<String> blockContentMatchRegex) {
        if(urlMatchRegex == null){
            urlMatchRegex = urlRegex;
        }else{
            if(!urlMatchRegex.equals(urlRegex)){
                return false;
            }
        }
        this.blockContentMatchRegex = blockContentMatchRegex;
        return true;
    }

    public String getUrlMatchRegex() {
        return urlMatchRegex;
    }

    public void setUrlMatchRegex(String urlMatchRegex) {
        this.urlMatchRegex = urlMatchRegex;
    }
    public void init(){
        //
        System.err.println("初始化方法还未写出");
    }
}
