package ps.scenario;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import ps.PSApp;
import ps.PSCanvas2D;
import ps.PSNode;
import ps.PSEdge;
import ps.PSEdgeInput;
import ps.PSScene;
import ps.cmd.PSCmdToAddCurEdgeToEdges;
import ps.cmd.PSCmdToAddCurPtCurveToEdgeCmd;
import ps.cmd.PSCmdToAddCurPtCurveToEdgeInput;
import ps.cmd.PSCmdToAddCurPtCurveToNodeName;
import ps.cmd.PSCmdToChangeQuasi;
import ps.cmd.PSCmdToClearCurEdgeInput;
import ps.cmd.PSCmdToClearCurNodeName;
import ps.cmd.PSCmdToCreateCurPtCurve;
import ps.cmd.PSCmdToCreateCurEdge;
import ps.cmd.PSCmdToSaveCurEdge;
import ps.cmd.PSCmdToUpdateCurPtCurve;
import ps.cmd.PSCmdToUpdateEdgeArrow;
import ps.cmd.PSCmdToUpdateNodeRadius;
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
        this.addScene(PSDrawEdgeScenario.DrawEdgeScene.createSingleton(this));
        this.addScene(PSDrawEdgeScenario.EditEdgeReadyScene.createSingleton(this));
        this.addScene(PSDrawEdgeScenario.EditEdgeInputScene.createSingleton(this));
        this.addScene(PSDrawEdgeScenario.EditEdgeCmdScene.createSingleton(this));
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
//            PSApp app = (PSApp) this.mScenario.getApp();
//            Point pt = e.getPoint();
//            PSCmdToCreateCurEdge.execute(app, pt);
            
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            Point.Double mWorldPt = app.getXform().calcPtFromScreenToWorld(pt);
            ArrayList<PSNode> mNodes = app.getNodeMgr().getNodes();
            // if the mouse press inside the node, create the edge
            for (PSNode node : mNodes) {
                if (node.contains(mWorldPt)) {
                    PSCmdToCreateCurEdge.execute(app, pt, node);
                }
            }
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            Point.Double mWorldPt = app.getXform().calcPtFromScreenToWorld(pt);
            ArrayList<PSNode> mNodes = app.getNodeMgr().getNodes();

            boolean isInNode = false;
            // check if the final position includes node or not
            for (PSNode node : mNodes) {
                if (node.contains(mWorldPt)) {
                    isInNode = true;
                }
            }
            if (!isInNode) {
                PSCmdToUpdateEdgeArrow.execute(app, pt);
            }
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
//            PSApp app = (PSApp) this.mScenario.getApp();
//            if (app.getEdgeMgr().getCurEdge() != null) {
//                XCmdToChangeScene.execute(app,
//                    PSDrawEdgeScenario.EditNodeReadyScene.getSingleton(), null);
//            }
            
            boolean isInNode = false;
            PSNode mNode = null;
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            Point.Double mWorldPt = app.getXform().calcPtFromScreenToWorld(pt);
            ArrayList<PSNode> mNodes = app.getNodeMgr().getNodes();
            // check if the final position includes node or not
            for (PSNode node : mNodes) {
                if (node.contains(mWorldPt)) {
                    isInNode = true;
                    mNode = node;
                }
            }
            if (isInNode) {
                System.out.println("INSIDE");
                if (app.getEdgeMgr().getCurEdge() != null) {
                    PSCmdToSaveCurEdge.execute(app, pt, mNode);
                    XCmdToChangeScene.execute(app,
                        PSDrawEdgeScenario.EditEdgeReadyScene.getSingleton(), null);
                }
            } else {
                //Not in node, so invalid => set current edge null
                app.getEdgeMgr().setCurEdge(null);
                XCmdToChangeScene.execute(app, 
                    PSDefaultScenario.ReadyScene.getSingleton(), null);
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
                case KeyEvent.VK_S:
                    app.getNodeMgr().setCurNode(null);
                    XCmdToChangeScene.execute(app, this.mReturnScene, null);
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
            // if the mouse press inside of edge input, edit edge input
            if (edgeInput.contains(mWorldPt)) {
                PSCmdToCreateCurPtCurve.execute(app, pt);
                XCmdToChangeScene.execute(app, 
                    PSDrawEdgeScenario.EditEdgeInputScene.getSingleton(), this);
            } else {
                PSCmdToAddCurEdgeToEdges.execute(app);
                app.getEdgeMgr().setCurEdge(null);
                XCmdToChangeScene.execute(app, 
                    PSDefaultScenario.ReadyScene.getSingleton(), 
                    null);
            }
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
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
                    PSCmdToClearCurEdgeInput.execute(app);
                    break;
                case KeyEvent.VK_DELETE:
                    app.getEdgeMgr().setCurEdge(null);
                    XCmdToChangeScene.execute(app, 
                        PSDefaultScenario.ReadyScene.getSingleton(), 
                        null);
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
            scenario.drawNode(g2);
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
            Point pt = e.getPoint();
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
            scenario.drawNode(g2);
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
    
    Point2D.Double mm = new Point2D.Double(50, 50);
    private PSNode mNode = null;
    public PSNode getNode() {
        return mNode;
    }
    public void setNode(PSNode node) {
        this.mNode = node;
    }
    
    private void drawNode(Graphics2D g2) {
        if (mNode != null) {
            mNode.drawNode(g2, PSCanvas2D.COLOR_CUR_NODE_ELLIPSE,
                PSCanvas2D.STROKE_CUR_NODE_ELLIPSE);
        }
    }
    
//    Point2D.Double mm = new Point2D.Double(50, 50);
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
