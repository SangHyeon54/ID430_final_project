package ps.scenario;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import ps.PSApp;
import ps.PSCanvas2D;
import ps.PSNode;
import ps.PSScene;
import ps.cmd.PSCmdToAddCurNodeToNodes;
import ps.cmd.PSCmdToAddCurPtCurveToNodeName;
import ps.cmd.PSCmdToChangeQuasi;
import ps.cmd.PSCmdToClearCurNodeName;
import ps.cmd.PSCmdToCreateCurPtCurve;
import ps.cmd.PSCmdToCreateCurNode;
import ps.cmd.PSCmdToDeleteNodeEdge;
import ps.cmd.PSCmdToUpdateCurPtCurve;
import ps.cmd.PSCmdToUpdateNodeRadius;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class PSDrawNodeScenario extends XScenario {
    private static PSDrawNodeScenario mSingleton = null;
    public static PSDrawNodeScenario getSingle() {
        assert(PSDrawNodeScenario.mSingleton != null);
        return PSDrawNodeScenario.mSingleton;
    }
    
    public static PSDrawNodeScenario createSingleton(XApp app) {
        assert(PSDrawNodeScenario.mSingleton == null);
        PSDrawNodeScenario.mSingleton = new PSDrawNodeScenario(app);
        return PSDrawNodeScenario.mSingleton;
    }
    
    private PSDrawNodeScenario (XApp app) {
        super(app);
    }
    
    @Override
    protected void addScenes() {
        this.addScene(PSDrawNodeScenario.DrawNodeScene.createSingleton(this));
        this.addScene(PSDrawNodeScenario.EditNodeReadyScene.
            createSingleton(this));
        this.addScene(PSDrawNodeScenario.EditNodeNameScene.
            createSingleton(this));
    }
    
    public static class DrawNodeScene extends PSScene {
        private static DrawNodeScene mSingleton = null;
        public static DrawNodeScene getSingleton() {
            assert(DrawNodeScene.mSingleton != null);
            return DrawNodeScene.mSingleton;
        }
        
        public static DrawNodeScene createSingleton(XScenario scenario) {
            assert(DrawNodeScene.mSingleton == null);
            DrawNodeScene.mSingleton = new DrawNodeScene(scenario);
            return DrawNodeScene.mSingleton;
        }
        
        private DrawNodeScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            PSCmdToCreateCurNode.execute(app, pt);
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            if (app.getNodeMgr().getCurNode() != null) {
                PSCmdToUpdateNodeRadius.execute(app, pt);
            }
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            if (app.getNodeMgr().getCurNode() != null) {
                XCmdToChangeScene.execute(app,
                    PSDrawNodeScenario.EditNodeReadyScene.getSingleton(), null);
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
                    PSDrawNodeScenario.EditNodeNameScene.getSingleton(), this);
            } else {
                PSCmdToAddCurNodeToNodes.execute(app);
                app.getNodeMgr().setCurNode(null);
                XCmdToChangeScene.execute(app, 
                    PSDefaultScenario.ReadyScene.getSingleton(), null);
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
            int code = e.getKeyCode();
            PSApp app = (PSApp)this.mScenario.getApp();
            
            switch (code) {
                case KeyEvent.VK_SHIFT:
                    XCmdToChangeScene.execute(app, 
                        PSMoveNodeScenario.MoveNodeReadyScene.getSingleton(), 
                        this);
                    break;
                case KeyEvent.VK_CONTROL:
                    XCmdToChangeScene.execute(app, 
                        PSNavigateScenario.ZoomReadyScene.getSingleton(), this);
                    break;
                case KeyEvent.VK_A:
                    PSCmdToAddCurNodeToNodes.execute(app);
                    app.getNodeMgr().setCurNode(null);
                    XCmdToChangeScene.execute(app, 
                        PSDrawEdgeScenario.DrawEdgeScene.getSingleton(), this);
                    break;
            }
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
                    PSCmdToDeleteNodeEdge.execute(app, 
                        app.getNodeMgr().getCurNode());
                    app.getNodeMgr().setCurNode(null);                    
                    XCmdToChangeScene.execute(app, 
                        PSDefaultScenario.ReadyScene.getSingleton(), null);
                    break;
                case KeyEvent.VK_ENTER:
                    PSCmdToAddCurNodeToNodes.execute(app);
                    app.getNodeMgr().setCurNode(null);
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
                PSDrawNodeScenario.EditNodeReadyScene.getSingleton(), null);
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
            PSDrawNodeScenario scenario = (PSDrawNodeScenario) this.mScenario;
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
    
}
