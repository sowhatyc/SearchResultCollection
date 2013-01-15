/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

import CommonTools.MatchRegex;

/**
 *
 * @author admin
 */
public class MissingElementProperty {
    private String missingStringRegex;
    private int missingPosition;
    private boolean missingSingle;
    private MatchRegex missingMatchRegex;
    private String description;
    private int selectedPostion;

    

    public MissingElementProperty(String missingRegex, int missingPosition, boolean missingSingle, String description, int selectedPosition) {
        this.missingStringRegex = missingRegex;
        this.missingPosition = missingPosition;
        this.missingSingle = missingSingle;
        this.missingMatchRegex = new MatchRegex(missingStringRegex);
        this.description = description;
        this.selectedPostion = selectedPosition;
    }
    
    /**
     * @return the missingRegex
     */
    public String getMissingRegex() {
        return missingStringRegex;
    }

    /**
     * @param missingRegex the missingRegex to set
     */
    public void setMissingRegex(String missingRegex) {
        this.missingStringRegex = missingRegex;
    }

    /**
     * @return the missingPosition
     */
    public int getMissingPosition() {
        return missingPosition;
    }

    /**
     * @param missingPosition the missingPosition to set
     */
    public void setMissingPosition(int missingPosition) {
        this.missingPosition = missingPosition;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the missingSingle
     */
    public boolean isMissingSingle() {
        return missingSingle;
    }

    /**
     * @param missingSingle the missingSingle to set
     */
    public void setMissingSingle(boolean missingSingle) {
        this.missingSingle = missingSingle;
    }

    /**
     * @return the missingMatchRegex
     */
    public MatchRegex getMissingMatchRegex() {
        return missingMatchRegex;
    }

    /**
     * @return the selectedPostion
     */
    public int getSelectedPostion() {
        return selectedPostion;
    }

    /**
     * @param selectedPostion the selectedPostion to set
     */
    public void setSelectedPostion(int selectedPostion) {
        this.selectedPostion = selectedPostion;
    }
    
    
}
