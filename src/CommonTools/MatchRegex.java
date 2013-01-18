/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonTools;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author Administrator
 */
public class MatchRegex {
    private String regex;
    private Pattern mpattern;
    private Matcher mmatcher;
    private int groupNum;
    
     public MatchRegex(MatchRegex mr){
            regex = new String(mr.regex);
            mpattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
//            mmatcher = mpattern.matcher("");
            //System.out.printf("regex group num is %d\n", mmather.groupCount());
//            groupNum = mmatcher.groupCount();
            
     }
     public boolean findMatch(String input){
            if(null != regex){
                //mpattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL );
                mmatcher = mpattern.matcher(input);
                return mmatcher.find();
            }
            else{
                return false;
            }
     }
     
      public static int getGroupNum(String regex){
            if(null != regex){
                Pattern mpattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL );
                Matcher mmather = mpattern.matcher("");
                return mmather.groupCount();
            }
            else{
                return 0;
            }
        }
      
	public MatchRegex(String regex) {
		this.regex = regex;
		mpattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL );
                //System.out.println("Initial*****");
		mmatcher = mpattern.matcher("");
		//System.out.printf("regex group num is %d\n", mmather.groupCount());
		groupNum = mmatcher.groupCount();
	}
       

    public String doValueReplaceAll(String input, String replacement){
        mmatcher = mpattern.matcher(input);
//        if(mmatcher.find()){
//            System.out.println(mmatcher.group(0));
//        }else{
//            System.out.println("Not found");
//        }
//        System.out.println(mmatcher.groupCount());
        String newContent = mmatcher.replaceAll(replacement);
        return newContent;
    }
    
    public String doValueReplaceFirst(String input, String replacement){
        if(input == null){
            return null;
        }
        mmatcher = mpattern.matcher(input);
        String newContent = mmatcher.replaceFirst(replacement);
        return newContent;
    }
        
    public DataValue doValueMatch(String input){
                int bool = 0;
		mmatcher = mpattern.matcher(input);
		DataValue mresult = new DataValue(groupNum);
		while(mmatcher.find()){
			String[] matchResult = new String[groupNum];
			for(int i = 0; i < groupNum; i++){
				//保险起见应该使用String cpy
				matchResult[i] = mmatcher.group(i+1);
//                                if(matchResult[i] == null){
//                                    ;
//                                    
//                                }else if(matchResult[i].length() == 0)
//                                {
//                                 //   System.out.println("这个是空白的" + "num"+i);
//                                    bool = 1;
//                                }
			}
//                        if(bool == 0){
//                            mresult.addValue(matchResult);
//                        }
                        mresult.addValue(matchResult);
//                        else{
//                            bool = 0;
//                        }
			
		}
		return mresult;
	}
    public DataValue doValueMatchBoolean(String input) {
        int bool = 0;
		mmatcher = mpattern.matcher(input);
		DataValue mresult = new DataValue(groupNum);
		while(mmatcher.find()){
			String[] matchResult = new String[groupNum];
			for(int i = 0; i < groupNum; i++){
				//保险起见应该使用String cpy
				matchResult[i] = mmatcher.group(i+1);
                                if(matchResult[i].length() == 0)
                                {
                                 //   System.out.println("这个是空白的" + "num"+i);
                                    bool = 1;
                                }
			}
                        if(bool == 0){
                            mresult.addValue(matchResult);
                        }
                        else{
                            bool = 0;
                        }
			
		}
		return mresult;
    }
    
    public void setRegex(String regex) {
        this.regex = regex;
        mpattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL );
        mmatcher = mpattern.matcher("");
            //System.out.printf("regex group num is %d\n", mmather.groupCount());
        groupNum = mmatcher.groupCount();
    }
    
    public String doContentMatch(String input){
            //System.out.println("*******");
            if(input == null){
                return null;
            }
            mmatcher = mpattern.matcher(input);
            String result = null;
            if(mmatcher.find()){
                    result = mmatcher.group(0);
//                        int count = mmatcher.groupCount();
//                        System.out.println(count);
////                        System.out.println(mmatcher.group(count-1));
            }
            return result;
    }
    
    public int getFirstStartIndex(String input){
        if(input == null){
            return -1;
        }
        mmatcher = mpattern.matcher(input);
        mmatcher.find();
        return mmatcher.start();
//        return index;
//        return -1;
    }
    
    public int getFirstEndIndex(String input){
        if(input == null){
            return -1;
        }
        mmatcher = mpattern.matcher(input);
        mmatcher.find();
        return mmatcher.end();
    }
    
}
