package ps.cmd;

import java.awt.Point;
import ps.PSApp;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToZoomNRotate extends XLoggableCmd{
    private Point mScreenPt = null;
    
    //private constructor
    private PSCmdToZoomNRotate(XApp app, Point pt) {
        super(app);
        this.mScreenPt = pt;
    }
    
    public static boolean execute(XApp app, Point pt) {
        PSCmdToZoomNRotate cmd = new PSCmdToZoomNRotate(app, pt);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        app.getXform().zoomNRotateTo(this.mScreenPt);
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        sb.append(this.mScreenPt).append("\t");
        return sb.toString();
    }
}
