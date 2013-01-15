/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;


import CommonTools.CTs;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class Item {
    ArrayList<Element> elementList;
    int elementNum;
    int itemType = -1;
//    int[] elementType;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getElementNum() {
        return elementNum;
    }

    public Item(ArrayList<Element> elementList) {
        this.elementList = elementList;
        elementNum =0;
    }
    public Item() {
        elementList = new ArrayList<Element>();
        elementNum =0;
     
    }
    public void itemPrint(){
        elementList.size();
    }
    /**
     * 
     * @param index
     * @param element
     */
    public void add(int index, Element element) {
        elementList.add(index, element);
        elementNum++;
    }

    public boolean add(Element e) {
        elementNum++;
        return elementList.add(e);
        
    }
    public boolean remove(int index) {
        if(index>0&&index<elementNum){
            elementNum--;
            elementList.remove(index);
            return true;
        }else{
            return false;
        }
        
    }
    public boolean removeLast() {
        if(elementNum>0){
            elementNum--;
            elementList.remove(elementNum);
            return true;
        }else{
            return false;
        }
        
        
    }
    public ArrayList<Element> getElementList(){
        return elementList;
    }

    public void setElementList(ArrayList<Element> elementList) {
        this.elementList = elementList;
    }
    public void clear(){
        elementNum = 0;
        elementList.clear();
        itemType = -1;
    }

    public void print() {
        CTs.priorPrint("elementList.size() = "+elementList.size(),0);
       for(int i = 0; i<elementList.size();i++){
           if(elementList.get(i).getUrl()!= null){
               CTs.priorPrint("***************"+"第"+i+"项"+"******************"+"| Type = "+elementList.get(i).getElementType()+"| Content = ("+elementList.get(i).getElementContent()+") Url = ("+elementList.get(i).getUrl()+")");
           }else{
            CTs.priorPrint("***************"+"第"+i+"项"+"******************"+"| Type = "+elementList.get(i).getElementType()+") Content = ("+elementList.get(i).getElementContent()+")");
           }
     
           
       }
        
    }

    public void save() {
        
    }

    public Element  getElement(int i) {
        return elementList.get(i);
    }
    public Element  getLastElement() {
        if(elementList.size() == 0){
            return null;
        }
        return elementList.get(elementList.size()-1);
    }

    
}
