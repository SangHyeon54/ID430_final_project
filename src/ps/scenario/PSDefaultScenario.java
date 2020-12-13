package ps.scenario;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import ps.PSApp;
import ps.PSEdge;
import ps.PSEdgeInput;
import ps.PSNode;
import ps.PSScene;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class PSDefaultScenario extends XScenario {
    private static PSDefaultScenario mSingleton = null;
    public static PSDefaultScenario getSingle() {
        assert(PSDefaultScenario.mSingleton != null);
        return PSDefaultScenario.mSingleton;
    }
    
    public static PSDefaultScenario createSingleton(XApp app) {
        assert(PSDefaultScenario.mSingleton == null);
        PSDefaultScenario.mSingleton = new PSDefaultScenario(app);
        return PSDefaultScenario.mSingleton;
    }
    
    private PSDefaultScenario (XApp app) {
        super(app);
    }
    
    @Override
    protected void addScenes() {
        this.addScene(PSDefaultScenario.ReadyScene.createSingleton(this));
    }
    
    public static class ReadyScene extends PSScene {
        private static ReadyScene mSingleton = null;
        public static ReadyScene getSingleton() {
            assert(ReadyScene.mSingleton != null);
            return ReadyScene.mSingleton;
        }
        
        public static ReadyScene createSingleton(XScenario scenario) {
            assert(ReadyScene.mSingleton == null);
            ReadyScene.mSingleton = new ReadyScene(scenario);
            return ReadyScene.mSingleton;
        }
        
        private ReadyScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            Point.Double mWorldPt = app.getXform().calcPtFromScreenToWorld(pt);
            ArrayList<PSNode> nodes = app.getNodeMgr().getNodes();
            for (int i = 0; i < nodes.size(); i ++) {
                PSNode node = nodes.get(i);
                // if the point is inside of node.
                if (node.getCenter().distance(mWorldPt.x, mWorldPt.y) < 
                    node.getRadius()) {
                    app.getNodeMgr().setCurNode(node);
                    app.getNodeMgr().removeNode(i);
                    break;
                }
            }
            
            if (app.getNodeMgr().getCurNode() != null) {
                XCmdToChangeScene.execute(app,
                    PSDrawNodeScenario.EditNodeReadyScene.getSingleton(),
                    this);
            }

            ArrayList<PSEdge> edges = app.getEdgeMgr().getEdges();
            for (int i = 0; i < edges.size(); i ++) {
                PSEdge mEdge = edges.get(i);
                PSEdgeInput mInput = mEdge.getInput();
                // if the point is inside of node.
                if (mInput.getCenter().distance(mWorldPt.x, mWorldPt.y) < 
                    mInput.getRadius()) {
                    app.getEdgeMgr().setCurEdge(mEdge);
                    app.getEdgeMgr().removeEdge(i);
                    break;
                }
            }
            
            if (app.getEdgeMgr().getCurEdge() != null) {
                XCmdToChangeScene.execute(app,
                    PSDrawEdgeScenario.EditEdgeReadyScene.getSingleton(),
                    this);
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
                case KeyEvent.VK_S:
                    XCmdToChangeScene.execute(app,
                        PSDrawNodeScenario.DrawNodeScene.getSingleton(), this);
                    break;
                case KeyEvent.VK_D:
                    XCmdToChangeScene.execute(app,
                        PSDrawEdgeScenario.DrawEdgeScene.getSingleton(), this);
                    break;
                case KeyEvent.VK_CONTROL:
                    XCmdToChangeScene.execute(app, 
                        PSNavigateScenario.ZoomNRotateReadyScene.getSingleton(), 
                        this);
                    break;
            }
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
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
        }
    }
}
