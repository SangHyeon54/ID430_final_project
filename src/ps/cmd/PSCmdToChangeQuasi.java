package ps.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import ps.PSApp;
import ps.PSNode;
import ps.PSPtCurve;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToChangeQuasi extends XLoggableCmd {
    private boolean before_q = false;
    private boolean after_q = false;
    
    //private constructor
    private PSCmdToChangeQuasi(XApp app) {
        super(app);
    }
    
    public static boolean execute(XApp app) {
        PSCmdToChangeQuasi cmd = new PSCmdToChangeQuasi(app);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        PSApp app = (PSApp) this.mApp;
        this.before_q = app.getNodeMgr().getCurNode().getIsQuasi();
        app.getNodeMgr().getCurNode().changeQuasi();
        this.after_q = app.getNodeMgr().getCurNode().getIsQuasi();
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        sb.append(this.before_q).append("\t");
        sb.append(this.after_q);
        return sb.toString();
    }
}
