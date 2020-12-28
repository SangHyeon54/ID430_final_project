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
    
    private PSEdgeMgr mEdgeMgr = null;
    public PSEdgeMgr getEdgeMgr() {
        return this.mEdgeMgr;
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
        this.mFrame = new JFrame("Program Scenarist");
        this.mCanvas2D = new PSCanvas2D(this);
        this.mXform = new PSXform();
        this.mEventListener = new PSEventListener(this);
        this.mNodeMgr = new PSNodeMgr();
        this.mEdgeMgr = new PSEdgeMgr();
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
}
