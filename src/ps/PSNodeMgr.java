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
    private PSNode mCurNode = null;
    public PSNode getCurNode() {
        return this.mCurNode;
    }
    public void setCurNode(PSNode ptCurve) {
        this.mCurNode = ptCurve;
    }
    
    private ArrayList<PSNode> mNodes = null;
    public ArrayList<PSNode> getNodes() {
        return this.mNodes;
    }
    
    private ArrayList<PSNode> mSelectedNodes = null;
    public ArrayList<PSNode> getSelectedNodes() {
        return this.mSelectedNodes;
    }
    
    public PSNodeMgr() {
        this.mNodes = new ArrayList<PSNode>();
        this.mSelectedNodes = new ArrayList<PSNode>();
    }
    
    public void removeNode(int index) {
        this.mNodes.remove(index);
    }
    
}
