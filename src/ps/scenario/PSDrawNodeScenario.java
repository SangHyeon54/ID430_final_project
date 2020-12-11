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
import ps.cmd.PSCmdToCreateNode;
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
        this.addScene(PSDrawNodeScenario.EditNodeScene.createSingleton(this));
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
            PSCmdToCreateNode.execute(app, pt);
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
                    PSDrawNodeScenario.EditNodeScene.getSingleton(), null);
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
                case KeyEvent.VK_N:
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
            PSApp app = (PSApp) this.mScenario.getApp();
            PSDrawNodeScenario scenario = (PSDrawNodeScenario) this.mScenario;
            scenario.drawNode(g2);
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
        }
    }
    
    public static class EditNodeScene extends PSScene {
        private static EditNodeScene mSingleton = null;
        public static EditNodeScene getSingleton() {
            assert(EditNodeScene.mSingleton != null);
            return EditNodeScene.mSingleton;
        }
        
        public static EditNodeScene createSingleton(XScenario scenario) {
            assert(EditNodeScene.mSingleton == null);
            EditNodeScene.mSingleton = new EditNodeScene(scenario);
            return EditNodeScene.mSingleton;
        }
        
        private EditNodeScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
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
            mNode.drawNode(g2);
        }
    }
    
}
