package ps;


import java.util.ArrayList;

public class PSPtCurveMgr {
    private PSPtCurve mCurPtCurve = null;
    public PSPtCurve getCurPtCurve() {
        return this.mCurPtCurve;
    }
    public void setCurPtCurve(PSPtCurve ptCurve) {
        this.mCurPtCurve = ptCurve;
    }
    
    private ArrayList<PSPtCurve> mPtCurves = null;
    public ArrayList<PSPtCurve> getPtCurves() {
        return this.mPtCurves;
    }
    
    private ArrayList<PSPtCurve> mSelectedPtCurves = null;
    public ArrayList<PSPtCurve> getSelectedPtCurves() {
        return this.mSelectedPtCurves;
    }
    
    public PSPtCurveMgr() {
        this.mPtCurves = new ArrayList<PSPtCurve>();
        this.mSelectedPtCurves = new ArrayList<PSPtCurve>();
    }
}
