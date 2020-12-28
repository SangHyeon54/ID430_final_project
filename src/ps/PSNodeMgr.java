/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ps;

import java.util.ArrayList;

/**
 *
 * @author Sanghyeon
 */
public class PSNodeMgr {
    
    // fields
    // CurNode is for edit, move
    private PSGeneralNode mCurNode = null;
    public PSGeneralNode getCurNode() {
        return this.mCurNode;
    }
    
    public void setCurNode(PSGeneralNode ptCurve) {
        this.mCurNode = ptCurve;
    }
    
    private ArrayList<PSGeneralNode> mGeneralNodes = null;
    public ArrayList<PSGeneralNode> getGeneralNodes() {
        return this.mGeneralNodes;
    }
    
    // constructor
    public PSNodeMgr() {
        this.mGeneralNodes = new ArrayList<PSGeneralNode>();
    }
    
    public void addNode(PSGeneralNode node) {
        this.mGeneralNodes.add(node);
    }
    
    public void removeNode(int index) {
        this.mGeneralNodes.remove(index);
    }
}
