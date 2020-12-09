package ps.scenario;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import ps.PSApp;
import ps.PSCanvas2D;
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
        this.addScene(PSDefaultScenario.NodeReadyScene.createSingleton(this));
    }
    
    public static class NodeReadyScene extends PSScene {
        private static NodeReadyScene mSingleton = null;
        public static NodeReadyScene getSingleton() {
            assert(NodeReadyScene.mSingleton != null);
            return NodeReadyScene.mSingleton;
        }
        
        public static NodeReadyScene createSingleton(XScenario scenario) {
            assert(NodeReadyScene.mSingleton == null);
            NodeReadyScene.mSingleton = new NodeReadyScene(scenario);
            return NodeReadyScene.mSingleton;
        }
        
        private NodeReadyScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
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
                case KeyEvent.VK_N:
                    XCmdToChangeScene.execute(app,
                        PSDrawNodeScenario.DrawNodeScene.getSingleton(), this);

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
