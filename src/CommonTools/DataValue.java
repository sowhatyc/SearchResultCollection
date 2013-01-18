package CommonTools;

import java.util.ArrayList;

public class DataValue {
	String dataItemName;
	int subItemNum; // 正则式中的匹配括号个数
	int findNum; // 找到的匹配个数
        ArrayList<String[]> resultList; // 存储最终的匹配结果

    public int getFindNum() {
        return findNum;
    }

    public void setFindNum(int findNum) {
        this.findNum = findNum;
    }

    public int getSubItemNum() {
        return subItemNum;
    }

    public void setSubItemNum(int subItemNum) {
        this.subItemNum = subItemNum;
    }
	

	DataValue(int subItemNum) {
		this.subItemNum = subItemNum;
		findNum = 0;
		resultList = new ArrayList<String[]>();
	}

	DataValue(int subItemNum, int findNum, String dataItemName) {
		this.dataItemName = dataItemName;
		this.subItemNum = subItemNum;
		this.findNum = 0;
		resultList = new ArrayList<String[]>();
		for (int i = 0; i < findNum; i++) {
			this.addValue(new String[subItemNum]);
		}
	}

	public void addValue(String[] result) {
		resultList.add(result);
		findNum++;
	}

	public void printResult() {
		int i, j;
		String[] result = null;
		System.out.printf("**%s datavalue start***\n", dataItemName);
		for (i = 0; i < findNum; i++) {
			result = resultList.get(i);
			System.out.printf("num0:(%s)", result[0]);
			for (j = 1; j < subItemNum; j++) {
				System.out.printf(" num%d:(%s)", j, result[j]);
			}
			System.out.printf("\n");
		}
		System.out.printf("**%s datavalue over***\n", dataItemName);
	}
     	public String[] getValueAt(int index) {
		if (index >= findNum) {
			return null;
		}
		else {
			return resultList.get(index);
		}
	}
	
        public void setValueAt(String content, int index, int j){
            String[] res = resultList.get(index);
            res[j] = content;
            resultList.set(index, res);
        }
        
        
	public String getResultString(){
		int i, j;
		String[] result = null;
                String output = "";
		System.out.printf("***%s datavalue start***\n", dataItemName);
                output += String.format("***%s datavalue start***\n", dataItemName);
		for(i = 0; i < findNum; i++){
			result = resultList.get(i);
			for(j =0; j < subItemNum; j++){
				System.out.printf(" num%d:(%s)", j, result[j]);
                                output += String.format(" num%d:(%s)", j, result[j]);
                                
			}
			System.out.printf("\n");
                        output += "\n";
		}
		System.out.printf("**%s datavalue over***\n", dataItemName);
                output += String.format("***%s datavalue over***\n", dataItemName);
                return output;
	}
        
        public boolean eraseValueAt(int index){
            if (index >= findNum) {
                return false;
            }
            resultList.remove(index);
            findNum--;
            return true;
        }
}
