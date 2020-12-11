package ps.scenario;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import ps.PSApp;
import ps.PSCanvas2D;
import ps.PSScene;
import ps.PSXform;
import ps.cmd.PSCmdToPan;
import ps.cmd.PSCmdToSetStartingScreenPtForXform;
import ps.cmd.PSCmdToZoomNRotate;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class PSNavigateScenario extends XScenario {
    private static PSNavigateScenario mSingleton = null;
    public static PSNavigateScenario getSingle() {
        assert(PSNavigateScenario.mSingleton != null);
        return PSNavigateScenario.mSingleton;
    }
    
    public static PSNavigateScenario createSingleton(XApp app) {
        assert(PSNavigateScenario.mSingleton == null);
        PSNavigateScenario.mSingleton = new PSNavigateScenario(app);
        return PSNavigateScenario.mSingleton;
    }
    
    private PSNavigateScenario (XApp app) {
        super(app);
    }
    
    @Override
    protected void addScenes() {
        this.addScene(PSNavigateScenario.ZoomNRotateReadyScene.
            createSingleton(this));
        this.addScene(PSNavigateScenario.ZoomNRotateScene.
            createSingleton(this));
        this.addScene(PSNavigateScenario.PanReadyScene.createSingleton(this));
        this.addScene(PSNavigateScenario.PanScene.createSingleton(this));
    }
    
    public static class ZoomNRotateReadyScene extends PSScene {

        private static ZoomNRotateReadyScene mSingleton = null;
        public static ZoomNRotateReadyScene getSingleton() {
            assert(ZoomNRotateReadyScene.mSingleton != null);
            return ZoomNRotateReadyScene.mSingleton;
        }
        
        public static ZoomNRotateReadyScene createSingleton(
            XScenario scenario) {
            assert(ZoomNRotateReadyScene.mSingleton == null);
            ZoomNRotateReadyScene.mSingleton = 
                new ZoomNRotateReadyScene(scenario);
            return ZoomNRotateReadyScene.mSingleton;
        }
        
        private ZoomNRotateReadyScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            PSCmdToSetStartingScreenPtForXform.execute(app, pt);
            XCmdToChangeScene.execute(app, 
                PSNavigateScenario.ZoomNRotateScene.getSingleton(),
                this.getReturnScene());
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
            PSApp app = (PSApp) this.mScenario.getApp();
            
            switch (code) {
                case KeyEvent.VK_ALT:
                    XCmdToChangeScene.execute(app, 
                        PSNavigateScenario.PanReadyScene.getSingleton(), 
                        this.getReturnScene());
                    break;
            }
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            int code = e.getKeyCode();
            PSApp app = (PSApp) this.mScenario.getApp();
            
            switch (code) {
                case KeyEvent.VK_CONTROL:
                    XCmdToChangeScene.execute(app, 
                        this.getReturnScene(), 
                        this.getReturnScene());
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
            PSNavigateScenario scenario = (PSNavigateScenario) this.mScenario;
            scenario.drawCrosshair(g2);
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
        }
        
    }
    
    public static class ZoomNRotateScene extends PSScene {
        private static ZoomNRotateScene mSingleton = null;
        public static ZoomNRotateScene getSingleton() {
            assert(ZoomNRotateScene.mSingleton != null);
            return ZoomNRotateScene.mSingleton;
        }
        
        public static ZoomNRotateScene createSingleton(XScenario scenario) {
            assert(ZoomNRotateScene.mSingleton == null);
            ZoomNRotateScene.mSingleton = new ZoomNRotateScene(scenario);
            return ZoomNRotateScene.mSingleton;
        }
        
        private ZoomNRotateScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            PSCmdToZoomNRotate.execute(app,pt);
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();            
            XCmdToChangeScene.execute(app, 
                PSNavigateScenario.ZoomNRotateReadyScene.getSingleton(),
                this.getReturnScene());
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
            int code = e.getKeyCode();
            PSApp app = (PSApp) this.mScenario.getApp();
            
            switch (code) {
                case KeyEvent.VK_ALT:
                    Point penPt = app.getPenMarkMgr().
                        getLastPenMark().getLastPt();
                    PSCmdToSetStartingScreenPtForXform.execute(app, penPt);
                    XCmdToChangeScene.execute(app, 
                        PSNavigateScenario.PanScene.getSingleton(),
                        this.getReturnScene());
                    break;
            }
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            int code = e.getKeyCode();
            PSApp app = (PSApp) this.mScenario.getApp();
            
            switch (code) {
                case KeyEvent.VK_CONTROL:
                    XCmdToChangeScene.execute(app, 
                        this.getReturnScene(), 
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
            PSNavigateScenario scenario = (PSNavigateScenario) this.mScenario;
            scenario.drawCrosshair(g2);
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
            //PSApp app = (PSApp) this.mScenario.getApp();
            //app.getXform().setStartingScreenPt(null);
        }
    }
    
    public static class PanReadyScene extends PSScene {
        private static PanReadyScene mSingleton = null;
        public static PanReadyScene getSingleton() {
            assert(PanReadyScene.mSingleton != null);
            return PanReadyScene.mSingleton;
        }
        
        public static PanReadyScene createSingleton(XScenario scenario) {
            assert(PanReadyScene.mSingleton == null);
            PanReadyScene.mSingleton = new PanReadyScene(scenario);
            return PanReadyScene.mSingleton;
        }
        
        private PanReadyScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            PSCmdToSetStartingScreenPtForXform.execute(app, pt);
            
            XCmdToChangeScene.execute(app, 
                PSNavigateScenario.PanScene.getSingleton(),
                this.getReturnScene());
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
            PSApp app = (PSApp) this.mScenario.getApp();
            
            switch (code) {
                case KeyEvent.VK_CONTROL:
                    XCmdToChangeScene.execute(app, 
                        this.getReturnScene(),null);
                    break;
                case KeyEvent.VK_ALT:
                    XCmdToChangeScene.execute(app, 
                        PSNavigateScenario.ZoomNRotateReadyScene.
                        getSingleton(), this.getReturnScene());
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
    
    public static class PanScene extends PSScene {
        private Point mPoint = null;
        
        private static PanScene mSingleton = null;
        public static PanScene getSingleton() {
            assert(PanScene.mSingleton != null);
            return PanScene.mSingleton;
        }
        
        public static PanScene createSingleton(XScenario scenario) {
            assert(PanScene.mSingleton == null);
            PanScene.mSingleton = new PanScene(scenario);
            return PanScene.mSingleton;
        }
        
        private PanScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            mPoint = pt;
            PSCmdToPan.execute(app,pt);
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            PSApp app = (PSApp) this.mScenario.getApp();            
            XCmdToChangeScene.execute(app, 
                PSNavigateScenario.PanReadyScene.getSingleton(),
                this.getReturnScene());
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            
            int code = e.getKeyCode();
            PSApp app = (PSApp) this.mScenario.getApp();
            switch (code) {
                case KeyEvent.VK_CONTROL:
                    XCmdToChangeScene.execute(app, 
                        this.getReturnScene(), 
                        null);
                    break;
                case KeyEvent.VK_ALT:
                    Point penPt = app.getPenMarkMgr().getLastPenMark().
                        getLastPt();
                    PSCmdToSetStartingScreenPtForXform.execute(app, penPt);
                    XCmdToChangeScene.execute(app, 
                        PSNavigateScenario.ZoomNRotateScene.getSingleton(), 
                        this.getReturnScene());
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
            PSNavigateScenario scenario = (PSNavigateScenario) this.mScenario;
            scenario.drawPanCrosshair(g2);
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
            //PSApp app = (PSApp) this.mScenario.getApp();
            //app.getXform().setStartingScreenPt(null);
        }
        
    }
    
    private void drawCrosshair (Graphics2D g2) {
        PSScene curScene = (PSScene)this.mApp.getScenarioMgr().getCurScene();

        double r = 30;
        Point ctr = PSXform.PIVOT_PT;
        Line2D.Double hline = new Line2D.Double(ctr.x - r, ctr.y, 
            ctr.x + r, ctr.y);
        Line2D.Double vline = new Line2D.Double(ctr.x, ctr.y - r, 
            ctr.x, ctr.y + r);

        g2.setColor(PSCanvas2D.COLOR_CROSSHAIR);
        g2.setStroke(PSCanvas2D.STROKE_CROSSHAIR);
        g2.draw(hline);
        g2.draw(vline);
    } 
    
    private void drawPanCrosshair(Graphics2D g2) {
        PSApp app = (PSApp) this.getApp();
        
        Point penPt = app.getPenMarkMgr().getLastPenMark().getLastPt();
        Line2D.Double hline = new Line2D.Double(0, penPt.y,
            app.getCanvas2D().getWidth(), penPt.y);
        Line2D.Double vline = new Line2D.Double(penPt.x, 0, penPt.x,
            app.getCanvas2D().getHeight());
        
        g2.setColor(PSCanvas2D.COLOR_CROSSHAIR);
        g2.setStroke(PSCanvas2D.STROKE_CROSSHAIR);
        g2.draw(hline);
        g2.draw(vline);
    }
}
