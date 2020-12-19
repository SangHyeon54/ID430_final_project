package ps.scenario;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import ps.PSApp;
import ps.PSEdge;
import ps.PSEdgeCmd;
import ps.PSEdgeInput;
import ps.PSNode;
import ps.PSScene;
import ps.cmd.PSCmdToAddCurNodeToNodes;
import ps.cmd.PSCmdToMoveNode;
import ps.cmd.PSCmdToSetMovePoint;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class PSMoveNodeScenario extends XScenario {
    private static PSMoveNodeScenario mSingleton = null;
    public static PSMoveNodeScenario getSingle() {
        assert(PSMoveNodeScenario.mSingleton != null);
        return PSMoveNodeScenario.mSingleton;
    }
    
    public static PSMoveNodeScenario createSingleton(XApp app) {
        assert(PSMoveNodeScenario.mSingleton == null);
        PSMoveNodeScenario.mSingleton = new PSMoveNodeScenario(app);
        return PSMoveNodeScenario.mSingleton;
    }
    
    private PSMoveNodeScenario (XApp app) {
        super(app);
    }
    
    @Override
    protected void addScenes() {
        this.addScene(PSMoveNodeScenario.MoveNodeReadyScene.createSingleton(this));
        this.addScene(PSMoveNodeScenario.MoveNodeScene.createSingleton(this));
    }
    
    public static class MoveNodeReadyScene extends PSScene {
        private static MoveNodeReadyScene mSingleton = null;
        public static MoveNodeReadyScene getSingleton() {
            assert(MoveNodeReadyScene.mSingleton != null);
            return MoveNodeReadyScene.mSingleton;
        }
        
        public static MoveNodeReadyScene createSingleton(XScenario scenario) {
            assert(MoveNodeReadyScene.mSingleton == null);
            MoveNodeReadyScene.mSingleton = new MoveNodeReadyScene(scenario);
            return MoveNodeReadyScene.mSingleton;
        }
        
        private MoveNodeReadyScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            PSApp app = (PSApp)this.mScenario.getApp();
            Point pt = e.getPoint();
            Point.Double mWorldPt = app.getXform().calcPtFromScreenToWorld(pt);
            
            // if three is already curNode, it is from Edit Node Scene
            // so, just move the curNode
            if (app.getNodeMgr().getCurNode() != null) {
                // the point is in node
                if(app.getNodeMgr().getCurNode().contains(mWorldPt))
                {
                    PSCmdToSetMovePoint.execute(app, pt,
                        app.getNodeMgr().getCurNode());
                    XCmdToChangeScene.execute(app,
                        PSMoveNodeScenario.MoveNodeScene.getSingleton(),
                        this.mReturnScene);
                    
                }
                return;
            }
            
            ArrayList<PSNode> nodes = app.getNodeMgr().getNodes();
            for (int i = 0; i < nodes.size(); i ++) {
                PSNode node = nodes.get(i);
                // if the point is in node.
                if (node.contains(mWorldPt)) {
                    app.getNodeMgr().setCurNode(node);
                    app.getNodeMgr().removeNode(i);
                    break;
                }
            }
            
            if (app.getNodeMgr().getCurNode() != null) {
                PSCmdToSetMovePoint.execute(app, pt,
                    app.getNodeMgr().getCurNode());
                XCmdToChangeScene.execute(app,
                    PSMoveNodeScenario.MoveNodeScene.getSingleton(),
                    this.mReturnScene);
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
                case KeyEvent.VK_SHIFT:
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
    
    public static class MoveNodeScene extends PSScene {
        private static MoveNodeScene mSingleton = null;
        public static MoveNodeScene getSingleton() {
            assert(MoveNodeScene.mSingleton != null);
            return MoveNodeScene.mSingleton;
        }
        
        public static MoveNodeScene createSingleton(XScenario scenario) {
            assert(MoveNodeScene.mSingleton == null);
            MoveNodeScene.mSingleton = new MoveNodeScene(scenario);
            return MoveNodeScene.mSingleton;
        }
        
        private MoveNodeScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            PSApp app = (PSApp)this.mScenario.getApp();
            Point pt = e.getPoint();
            PSCmdToMoveNode.execute(app, pt, app.getNodeMgr().getCurNode());
            
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            PSApp app = (PSApp)this.mScenario.getApp();
            
            // save current node
            
            XCmdToChangeScene.execute(app,
                PSMoveNodeScenario.MoveNodeReadyScene.getSingleton(),
                this.mReturnScene);
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            int code = e.getKeyCode();
            PSApp app = (PSApp)this.mScenario.getApp();
            
            switch (code) {
                case KeyEvent.VK_SHIFT:
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
            PSApp app = (PSApp)this.mScenario.getApp();
            if(this.mReturnScene == PSDefaultScenario.ReadyScene.getSingleton()) {
                PSCmdToAddCurNodeToNodes.execute(app);
                app.getNodeMgr().setCurNode(null);
            }
        }
    }
}
