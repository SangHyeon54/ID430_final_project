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
public class PSEdgeMgr {
    private PSEdge mCurEdge = null;
    public PSEdge getCurEdge() {
        return this.mCurEdge;
    }
    public void setCurEdge(PSEdge ptCurve) {
        this.mCurEdge = ptCurve;
    }
    
    private ArrayList<PSEdge> mEdges = null;
    public ArrayList<PSEdge> getEdges() {
        return this.mEdges;
    }
    
    private ArrayList<PSEdge> mSelectedEdges = null;
    public ArrayList<PSEdge> getSelectedEdges() {
        return this.mSelectedEdges;
    }
    
    public PSEdgeMgr() {
        this.mEdges = new ArrayList<PSEdge>();
        this.mSelectedEdges = new ArrayList<PSEdge>();
    }
    
    public void removeEdge(int index) {
        this.mEdges.remove(index);
    }
    
}
