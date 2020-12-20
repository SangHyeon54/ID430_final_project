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
    
    // constructor
    public PSNodeMgr() {
        this.mNodes = new ArrayList<PSNode>();
    }
    
    public void addNode(PSNode node) {
        this.mNodes.add(node);
    }
    
    public void removeNode(int index) {
        this.mNodes.remove(index);
    }
}
