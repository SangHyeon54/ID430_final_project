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
    
//    private ArrayList<PSNode> mSelectedNodes = null;
//    public ArrayList<PSNode> getSelectedNodes() {
//        return this.mSelectedNodes;
//    }
    
    private PSReturnNode mCurReturnNode = null;
    public PSReturnNode getCurReturnNode() {
        return this.mCurReturnNode;
    }
    public void setCurReturnNode(PSReturnNode rNode) {
        this.mCurReturnNode = rNode;
    }
    
    private ArrayList<PSReturnNode> mReturnNodes = null;
    public ArrayList<PSReturnNode> getReturnNodes() {
        return this.mReturnNodes;
    }
    
    public PSNodeMgr() {
        this.mNodes = new ArrayList<PSNode>();
        this.mReturnNodes = new ArrayList<PSReturnNode>();
//        this.mSelectedNodes = new ArrayList<PSNode>();
    }
    
    public void addNode(PSNode node) {
        this.mNodes.add(node);
    }
    
    public void removeNode(int index) {
        this.mNodes.remove(index);
    }
    
    public void addReturnNode(PSReturnNode rNode) {
        this.mReturnNodes.add(rNode);
    }
    
    public void removeReturnNode(int index) {
        this.mReturnNodes.remove(index);
    }
    
}
