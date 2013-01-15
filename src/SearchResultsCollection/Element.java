/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

import CommonTools.CTs;




/**
 *
 * @author Administrator
 */
public class Element {
    
    private String content;
    private String url;
    private int type;
    
//    public String getContent() {
//        return content;
//    }

    public void setElementContent(String content) {
        this.content = content;
    }

    
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
    

    public Element(String content, String url, int type) {
        this.content = content;
        this.url = url;
        this.type = type;
    }

    public Element(Element element) {
        this.content = element.getElementContent();
        this.url = element.getUrl();
        this.type = element.getElementType();
    }

    public Element() {
        this.content = null;
        this.url = null;
        this.type = -1;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
    public Element(String content, String Url) {
        this.content = content;
        /*if(type == null){
            this.type = getType(content,Url);
        }
         * 
         */
        this.url = Url;
        this.type = -1;
    }
    
    
    
    //获得意义
    public int getElementType(){
        
        return type;
    }
    
    public void setElementType(int type){
        this.type = type;
    }
      
    public String getElementContent(){
        return content;
    }

    public void print() {
//        CTs.priorPrint("content = ("+content+ ")     url = ("+url+ ")     type = ("+type+")",0);
    }
    public boolean elementFilter(){
        
        return false;
      
    }

//    public Element elemenAnalysis(int type) {
//        
//        return ItemAnalysis.elementStringFilter(this,type);
////        return true;
//    }
}
