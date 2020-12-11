package ps;

import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.JFrame;
import x.XApp;
import x.XLogMgr;
import x.XScenarioMgr;

public class PSApp extends XApp {
    // Declare some member variables (field)
    // mFrame= member Frame
    private JFrame mFrame = null; 
    private PSCanvas2D mCanvas2D = null;
    public PSCanvas2D getCanvas2D() {
        return mCanvas2D;
    }

    private PSXform mXform = null;
    public PSXform getXform() {
        return this.mXform;
    }
    
    private PSEventListener mEventListener = null;
    
    private PSNodeMgr mNodeMgr = null;
    public PSNodeMgr getNodeMgr() {
        return this.mNodeMgr;
    }
    
    private PSPenMarkMgr mPenMarkMgr = null;
    public PSPenMarkMgr getPenMarkMgr() {
        return this.mPenMarkMgr;
    }
    
    private PSPtCurveMgr mPtCurveMgr = null;
    public PSPtCurveMgr getPtCurveMgr() {
        return this.mPtCurveMgr;
    }

    private XScenarioMgr mScenarioMgr = null;
    @Override
    public XScenarioMgr getScenarioMgr() {
        return this.mScenarioMgr;
    }

    private XLogMgr mLogMgr = null;
    @Override
    public XLogMgr getLogMgr() {
        return this.mLogMgr;
    }
    
    // Constructor
    public PSApp() {
        // step 1: create components
        // 1) frame, 2) canvas, 3) other components,
        // 4) eventlistener 5) manager
        this.mFrame = new JFrame("JustSketchIt");
        this.mCanvas2D = new PSCanvas2D(this);
        this.mXform = new PSXform();
        this.mEventListener = new PSEventListener(this);
        this.mNodeMgr = new PSNodeMgr();
        this.mPenMarkMgr = new PSPenMarkMgr();
        this.mPtCurveMgr = new PSPtCurveMgr();
        this.mScenarioMgr = new PSScenarioMgr(this);
        this.mLogMgr = new XLogMgr();
        mLogMgr.setPrintOn(true);

        // step 2: build and show visual components
        this.mFrame.add(this.mCanvas2D);
        this.mFrame.setSize(800,600);
        this.mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mFrame.setVisible(true);
        
        // step 3: connect event listeners.
        this.mCanvas2D.addMouseListener(this.mEventListener);
        this.mCanvas2D.addMouseMotionListener(this.mEventListener);
        this.mCanvas2D.addKeyListener(this.mEventListener);
        this.mCanvas2D.setFocusable(true);
    }

    public static void main(String[] args) {
        // Create a new PS object
        new PSApp();
    }
    
    public void increaseStrokeWidthForSelected(float f) {
        //from the point curve manager, get number of the selected point curves
        int size_s = this.mPtCurveMgr.getSelectedPtCurves().size();
        for (int i = 0; i < size_s; i++) {
            BasicStroke bs = (BasicStroke)this.mPtCurveMgr.
                getSelectedPtCurves().get(i).getStroke();
            float w = bs.getLineWidth();
            w += f;
            if (w < 1.0f) {
                w = 1.0f;
            }
            
            //reset the stroke with new width
            this.mPtCurveMgr.getSelectedPtCurves().get(i).
                setStroke(new BasicStroke(w, bs.getEndCap(), bs.getLineJoin()));
        }
    }
    
    public void setColorForSelectedPtCurve(Color c) {
        int size_s = this.mPtCurveMgr.getSelectedPtCurves().size();
        for (int i = 0; i < size_s; i++) {
            this.mPtCurveMgr.getSelectedPtCurves().get(i).setColor(c);
        }
    }
}
