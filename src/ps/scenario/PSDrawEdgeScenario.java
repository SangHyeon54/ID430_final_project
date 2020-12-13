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
import ps.PSScene;
import ps.cmd.PSCmdToAddCurNodeToNodes;
import ps.cmd.PSCmdToAddCurPtCurveToNodeName;
import ps.cmd.PSCmdToChangeQuasi;
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
        this.addScene(PSDrawEdgeScenario.EditNodeReadyScene.createSingleton(this));
        this.addScene(PSDrawEdgeScenario.EditNodeNameScene.createSingleton(this));
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
                        PSDrawEdgeScenario.EditNodeReadyScene.getSingleton(), null);
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
    
    public static class EditNodeReadyScene extends PSScene {
        private static EditNodeReadyScene mSingleton = null;
        public static EditNodeReadyScene getSingleton() {
            assert(EditNodeReadyScene.mSingleton != null);
            return EditNodeReadyScene.mSingleton;
        }
        
        public static EditNodeReadyScene createSingleton(XScenario scenario) {
            assert(EditNodeReadyScene.mSingleton == null);
            EditNodeReadyScene.mSingleton = new EditNodeReadyScene(scenario);
            return EditNodeReadyScene.mSingleton;
        }
        
        private EditNodeReadyScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            Point.Double mWorldPt = app.getXform().calcPtFromScreenToWorld(pt);
            PSNode node = app.getNodeMgr().getCurNode();
            // if the mouse press inside of ellipse, make node name
            if (node.contains(mWorldPt)) {
                PSCmdToCreateCurPtCurve.execute(app, pt);
                XCmdToChangeScene.execute(app, 
                    PSDrawEdgeScenario.EditNodeNameScene.getSingleton(), this);
            } else {
                PSCmdToAddCurNodeToNodes.execute(app);
                app.getNodeMgr().setCurNode(null);
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
                    PSCmdToClearCurNodeName.execute(app);
                    break;
                case KeyEvent.VK_M:
                    PSCmdToChangeQuasi.execute(app);
                    break;
                case KeyEvent.VK_DELETE:
                    app.getNodeMgr().setCurNode(null);
                    XCmdToChangeScene.execute(app, 
                        PSDefaultScenario.ReadyScene.getSingleton(), 
                        null);
                    break;
                case KeyEvent.VK_ENTER:
                    PSCmdToAddCurNodeToNodes.execute(app);
                    app.getNodeMgr().setCurNode(null);
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
    
    public static class EditNodeNameScene extends PSScene {
        private static EditNodeNameScene mSingleton = null;
        public static EditNodeNameScene getSingleton() {
            assert(EditNodeNameScene.mSingleton != null);
            return EditNodeNameScene.mSingleton;
        }
        
        public static EditNodeNameScene createSingleton(XScenario scenario) {
            assert(EditNodeNameScene.mSingleton == null);
            EditNodeNameScene.mSingleton = new EditNodeNameScene(scenario);
            return EditNodeNameScene.mSingleton;
        }
        
        private EditNodeNameScene(XScenario scenario) {
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
            PSCmdToAddCurPtCurveToNodeName.execute(app);
            XCmdToChangeScene.execute(app, 
                PSDrawEdgeScenario.EditNodeReadyScene.getSingleton(), null);
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
