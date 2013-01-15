/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonTools;

/**
 *
 * @author Winc
 */
public class CTs {
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
