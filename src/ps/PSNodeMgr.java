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
    
    private ArrayList<PSReturnNode> mReturnNodes = null;
    public ArrayList<PSReturnNode> getReturnNodes() {
        return this.mReturnNodes;
    }
    
    // constructor
    public PSNodeMgr() {
        this.mGeneralNodes = new ArrayList<PSGeneralNode>();
        this.mReturnNodes = new ArrayList<PSReturnNode>();
    }
    
    public void addGeneralNode(PSGeneralNode node) {
        this.mGeneralNodes.add(node);
    }
    
    public void removeGeneralNode(int index) {
        this.mGeneralNodes.remove(index);
    }
    
    public void addReturnNode(PSReturnNode node) {
        this.mReturnNodes.add(node);
    }
    
    public void removeReturnNode(int index) {
        this.mReturnNodes.remove(index);
    }
    
    public void removeReturnNode(PSReturnNode node) {
        this.mReturnNodes.remove(node);
    }
}
