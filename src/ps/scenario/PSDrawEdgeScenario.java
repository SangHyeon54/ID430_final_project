package ps.scenario;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import ps.PSApp;
import ps.PSCanvas2D;
import ps.PSGeneralNode;
import ps.PSNode;
import ps.PSEdge;
import ps.PSEdgeCmd;
import ps.PSEdgeInput;
import ps.PSReturnNode;
import ps.PSScene;
import ps.cmd.PSCmdToAddCurEdgeToEdges;
import ps.cmd.PSCmdToAddCurPtCurveToEdgeCmd;
import ps.cmd.PSCmdToAddCurPtCurveToEdgeInput;
import ps.cmd.PSCmdToClearCurEdgeCmd;
import ps.cmd.PSCmdToClearCurEdgeInput;
import ps.cmd.PSCmdToCreateCurPtCurve;
import ps.cmd.PSCmdToCreateCurEdge;
import ps.cmd.PSCmdToDeleteEdge;
import ps.cmd.PSCmdToSaveCurEdge;
import ps.cmd.PSCmdToUpdateCurPtCurve;
import ps.cmd.PSCmdToUpdateEdgeArrow;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class PSDrawEdgeScenario extends XScenario {
    private static PSDrawEdgeScenario mSingleton = null;
    public static PSDrawEdgeScenario getSingle() {
        assert(PSDrawEdgeScenario.mSingleton != null);
        return PSDrawEdgeScenario.mSingleton;
    }
    
    public static PSDrawEdgeScenario createSingleton(XApp app) {
        assert(PSDrawEdgeScenario.mSingleton == null);
        PSDrawEdgeScenario.mSingleton = new PSDrawEdgeScenario(app);
        return PSDrawEdgeScenario.mSingleton;
    }
    
    private PSDrawEdgeScenario (XApp app) {
        super(app);
    }
    
    @Override
    protected void addScenes() {
        this.addScene(PSDrawEdgeScenario.DrawEdgeScene.
            createSingleton(this));
        this.addScene(PSDrawEdgeScenario.EditEdgeReadyScene.
            createSingleton(this));
        this.addScene(PSDrawEdgeScenario.EditEdgeInputScene.
            createSingleton(this));
        this.addScene(PSDrawEdgeScenario.EditEdgeCmdScene.
            createSingleton(this));
        this.addScene(PSDrawEdgeScenario.ClearEdgeInputOrCmdScene.
            createSingleton(this));
    }
    
    public static class DrawEdgeScene extends PSScene {
        private static DrawEdgeScene mSingleton = null;
        public static DrawEdgeScene getSingleton() {
            assert(DrawEdgeScene.mSingleton != null);
            return DrawEdgeScene.mSingleton;
        }
        
        public static DrawEdgeScene createSingleton(XScenario scenario) {
            assert(DrawEdgeScene.mSingleton == null);
            DrawEdgeScene.mSingleton = new DrawEdgeScene(scenario);
            return DrawEdgeScene.mSingleton;
        }
        
        private DrawEdgeScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            PSApp app = (PSApp)this.mScenario.getApp();
            Point pt = e.getPoint();
            Point.Double mWorldPt = app.getXform().calcPtFromScreenToWorld(pt);
            ArrayList<PSGeneralNode> mNodes = app.getNodeMgr().getGeneralNodes();
            // create edge only when the mouse position is inside any of nodes
            for (PSNode node : mNodes) {
                if (node.getBound().contains(mWorldPt)) {
                    PSCmdToCreateCurEdge.execute(app, pt, node);
                }
            }
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            PSApp app = (PSApp)this.mScenario.getApp();
            Point pt = e.getPoint();
            Point.Double mWorldPt = app.getXform().calcPtFromScreenToWorld(pt);
            ArrayList<PSGeneralNode> mNodes = app.getNodeMgr().getGeneralNodes();
            PSEdge curEdge = app.getEdgeMgr().getCurEdge();
            
            if (curEdge != null) {
                PSNode startingNode = curEdge.getStartingNode();
                
                boolean isInNode = false;
                boolean isSelfLoopCondition = false;
                // check if the final position is inside any node
                // and whether the node is the same as initial node (self loop)
                for (PSGeneralNode mNode : mNodes) {
                    if (mNode.getBound().contains(mWorldPt)) {
                        isInNode = true;
                        curEdge.setEndingNode(mNode);
                        curEdge.calcArrowEnd();
                        if (mNode == startingNode) {
                            isSelfLoopCondition = true;
                        }
                    }
                }

                // do not update node when is in different node
                // so that arrow remains in the boundary
                if (!isInNode || isSelfLoopCondition) {
                    PSCmdToUpdateEdgeArrow.execute(
                        app, pt, isSelfLoopCondition);
                }
            }

           
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            PSApp app = (PSApp)this.mScenario.getApp();
            Point pt = e.getPoint();
            Point.Double mWorldPt = app.getXform().calcPtFromScreenToWorld(pt);
            ArrayList<PSGeneralNode> mNodes = app.getNodeMgr().getGeneralNodes();
            boolean isInNode = false;
            PSNode mNode = null;
            
            // check if the final position includes node or not
            for (PSGeneralNode node : mNodes) {
                if (node.getBound().contains(mWorldPt)) {
                    isInNode = true;
                    mNode = node;
                }
            }
            
            if (isInNode) {
                if (app.getEdgeMgr().getCurEdge() != null) {
                    PSCmdToSaveCurEdge.execute(app, pt, mNode);
                    XCmdToChangeScene.execute(app, PSDrawEdgeScenario.
                        EditEdgeReadyScene.getSingleton(), null);
                }
            } else {
                //Not in node, so invalid => set current edge null
//                app.getEdgeMgr().setCurEdge(null);
//                XCmdToChangeScene.execute(app, 
//                    PSDefaultScenario.ReadyScene.getSingleton(), null);
                if (app.getEdgeMgr().getCurEdge() != null) {
                    mNode = new PSReturnNode(mWorldPt);
                    app.getNodeMgr().addReturnNode((PSReturnNode)mNode);
                    app.getEdgeMgr().getCurEdge().cutArrowEnd(20);
                    PSCmdToSaveCurEdge.execute(app, pt, mNode);
                    XCmdToChangeScene.execute(app, PSDrawEdgeScenario.
                        EditEdgeReadyScene.getSingleton(), null);
                }
            }
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
            
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            int code = e.getKeyCode();
            PSApp app = (PSApp)this.mScenario.getApp();
            
            switch (code) {
                case KeyEvent.VK_A:
                    app.getEdgeMgr().setCurEdge(null);
                    XCmdToChangeScene.execute(app,
                        PSDefaultScenario.ReadyScene.getSingleton(), null);
                    break;
            }
        }

        @Override
        public void updateSupportObjects() {
        }

        @Override
        public void renderWorldOjbects(Graphics2D g2) {
        }

        @Override
        public void renderScreenOjbects(Graphics2D g2) {
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
        }
    }
    
    public static class EditEdgeReadyScene extends PSScene {
        private static EditEdgeReadyScene mSingleton = null;
        public static EditEdgeReadyScene getSingleton() {
            assert(EditEdgeReadyScene.mSingleton != null);
            return EditEdgeReadyScene.mSingleton;
        }
        
        public static EditEdgeReadyScene createSingleton(XScenario scenario) {
            assert(EditEdgeReadyScene.mSingleton == null);
            EditEdgeReadyScene.mSingleton = new EditEdgeReadyScene(scenario);
            return EditEdgeReadyScene.mSingleton;
        }
        
        private EditEdgeReadyScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            Point.Double mWorldPt = app.getXform().calcPtFromScreenToWorld(pt);
            PSEdgeInput edgeInput = app.getEdgeMgr().getCurEdge().getInput();
            PSEdgeCmd edgeCmd = app.getEdgeMgr().getCurEdge().getCmd();
            
            // depending on where the mouse is, editing part is different
            if (edgeInput.contains(mWorldPt)) { 
                // if inside of edge input, go edit input
                PSCmdToCreateCurPtCurve.execute(app, pt);
                XCmdToChangeScene.execute(app, 
                    PSDrawEdgeScenario.EditEdgeInputScene.getSingleton(), this);
            } else if (edgeCmd.contains(mWorldPt)) { 
                // if in cmd, go edit cmd
                PSCmdToCreateCurPtCurve.execute(app, pt);
                XCmdToChangeScene.execute(app, 
                    PSDrawEdgeScenario.EditEdgeCmdScene.getSingleton(), this);
            } else { 
                // else we save the edge
                PSCmdToAddCurEdgeToEdges.execute(app);
                app.getEdgeMgr().setCurEdge(null);
                XCmdToChangeScene.execute(app, 
                    PSDefaultScenario.ReadyScene.getSingleton(), null);
            }
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
            int code = e.getKeyCode();
            PSApp app = (PSApp)this.mScenario.getApp();
            
            switch (code) {
                case KeyEvent.VK_C:
                    XCmdToChangeScene.execute(app, PSDrawEdgeScenario.
                        ClearEdgeInputOrCmdScene.getSingleton(), this);
                    break;
            }
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            int code = e.getKeyCode();
            PSApp app = (PSApp)this.mScenario.getApp();
            
            switch (code) {
                case KeyEvent.VK_DELETE:
                    // delete the edge info from both starting and ending nodes
                    PSCmdToDeleteEdge.execute(app, 
                        app.getEdgeMgr().getCurEdge(), 
                        app.getEdgeMgr().getCurEdge().getStartingNode(),
                        app.getEdgeMgr().getCurEdge().getEndingNode());
                    app.getEdgeMgr().setCurEdge(null);
                    XCmdToChangeScene.execute(app, 
                        PSDefaultScenario.ReadyScene.getSingleton(), null);
                    break;
                case KeyEvent.VK_ENTER:
                    PSCmdToAddCurEdgeToEdges.execute(app);
                    app.getEdgeMgr().setCurEdge(null);
                    XCmdToChangeScene.execute(app, 
                        PSDefaultScenario.ReadyScene.getSingleton(), 
                        null);
                    break;
            }
        }

        @Override
        public void updateSupportObjects() {
        }

        @Override
        public void renderWorldOjbects(Graphics2D g2) {
        }

        @Override
        public void renderScreenOjbects(Graphics2D g2) {
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
        }
    }
    
    public static class EditEdgeInputScene extends PSScene {
        private static EditEdgeInputScene mSingleton = null;
        public static EditEdgeInputScene getSingleton() {
            assert(EditEdgeInputScene.mSingleton != null);
            return EditEdgeInputScene.mSingleton;
        }
        
        public static EditEdgeInputScene createSingleton(XScenario scenario) {
            assert(EditEdgeInputScene.mSingleton == null);
            EditEdgeInputScene.mSingleton = new EditEdgeInputScene(scenario);
            return EditEdgeInputScene.mSingleton;
        }
        
        private EditEdgeInputScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            PSCmdToUpdateCurPtCurve.execute(app, pt);
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            PSCmdToAddCurPtCurveToEdgeInput.execute(app);
            XCmdToChangeScene.execute(app, 
                PSDrawEdgeScenario.EditEdgeReadyScene.getSingleton(), null);
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
            
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
        }

        @Override
        public void updateSupportObjects() {
        }

        @Override
        public void renderWorldOjbects(Graphics2D g2) {
        }

        @Override
        public void renderScreenOjbects(Graphics2D g2) {
            PSApp app = (PSApp) this.mScenario.getApp();
            PSDrawEdgeScenario scenario = (PSDrawEdgeScenario) this.mScenario;
            scenario.drawEdge(g2);
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
            PSApp app = (PSApp) this.mScenario.getApp();
            app.getPtCurveMgr().setCurPtCurve(null);
        }
    }
    
    public static class EditEdgeCmdScene extends PSScene {
        private static EditEdgeCmdScene mSingleton = null;
        public static EditEdgeCmdScene getSingleton() {
            assert(EditEdgeCmdScene.mSingleton != null);
            return EditEdgeCmdScene.mSingleton;
        }
        
        public static EditEdgeCmdScene createSingleton(XScenario scenario) {
            assert(EditEdgeCmdScene.mSingleton == null);
            EditEdgeCmdScene.mSingleton = new EditEdgeCmdScene(scenario);
            return EditEdgeCmdScene.mSingleton;
        }
        
        private EditEdgeCmdScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            PSCmdToUpdateCurPtCurve.execute(app, pt);
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            PSCmdToAddCurPtCurveToEdgeCmd.execute(app);
            XCmdToChangeScene.execute(app, 
                PSDrawEdgeScenario.EditEdgeReadyScene.getSingleton(), null);
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
            
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
        }

        @Override
        public void updateSupportObjects() {
        }

        @Override
        public void renderWorldOjbects(Graphics2D g2) {
        }

        @Override
        public void renderScreenOjbects(Graphics2D g2) {
            PSApp app = (PSApp) this.mScenario.getApp();
            PSDrawEdgeScenario scenario = (PSDrawEdgeScenario) this.mScenario;
            scenario.drawEdge(g2);
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
            PSApp app = (PSApp) this.mScenario.getApp();
            app.getPtCurveMgr().setCurPtCurve(null);
        }
    }
    
    public static class ClearEdgeInputOrCmdScene extends PSScene {
        private static ClearEdgeInputOrCmdScene mSingleton = null;
        public static ClearEdgeInputOrCmdScene getSingleton() {
            assert(ClearEdgeInputOrCmdScene.mSingleton != null);
            return ClearEdgeInputOrCmdScene.mSingleton;
        }
        
        public static ClearEdgeInputOrCmdScene createSingleton(
            XScenario scenario) {
            assert(ClearEdgeInputOrCmdScene.mSingleton == null);
            ClearEdgeInputOrCmdScene.mSingleton = 
                new ClearEdgeInputOrCmdScene(scenario);
            return ClearEdgeInputOrCmdScene.mSingleton;
        }
        
        private ClearEdgeInputOrCmdScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            Point.Double mWorldPt = app.getXform().calcPtFromScreenToWorld(pt);
            PSEdgeInput edgeInput = app.getEdgeMgr().getCurEdge().getInput();
            PSEdgeCmd edgeCmd = app.getEdgeMgr().getCurEdge().getCmd();
            
            // depending on clicked mouse position, clear either input or cmd
            if (edgeInput.contains(mWorldPt)) {
                PSCmdToClearCurEdgeInput.execute(app);
            } else if (edgeCmd.contains(mWorldPt)) {
                PSCmdToClearCurEdgeCmd.execute(app);
            }
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
            
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            int code = e.getKeyCode();
            PSApp app = (PSApp)this.mScenario.getApp();
            
            switch (code) {
                case KeyEvent.VK_C:
                    XCmdToChangeScene.execute(app, 
                        PSDrawEdgeScenario.EditEdgeReadyScene.getSingleton(),
                        null);
                    break;
            }
        }

        @Override
        public void updateSupportObjects() {
        }

        @Override
        public void renderWorldOjbects(Graphics2D g2) {
        }

        @Override
        public void renderScreenOjbects(Graphics2D g2) {
            PSApp app = (PSApp) this.mScenario.getApp();
            PSDrawEdgeScenario scenario = (PSDrawEdgeScenario) this.mScenario;
            scenario.drawEdge(g2);
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
            PSApp app = (PSApp) this.mScenario.getApp();
            app.getPtCurveMgr().setCurPtCurve(null);
        }
    }
    
    private PSEdge mEdge = null;
    public PSEdge getEdge() {
        return mEdge;
        
    }
    public void setEdge (PSEdge edge) {
        this.mEdge = edge;
    }
    
    private void drawEdge (Graphics2D g2) {
        if (mEdge != null) {
            mEdge.drawEdge(g2, PSCanvas2D.COLOR_CUR_EDGE_ARROW,
                PSCanvas2D.STROKE_CUR_EDGE_ARROW);
        }
    }
}
