/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class MissingResult {
    private ArrayList<MissingElement> missingElements = new ArrayList<MissingElement>();
    

    public void addElement(MissingElement me) {
        getMissingElements().add(me);
    }

    /**
     * @return the missingElements
     */
    public ArrayList<MissingElement> getMissingElements() {
        return missingElements;
    }
}
