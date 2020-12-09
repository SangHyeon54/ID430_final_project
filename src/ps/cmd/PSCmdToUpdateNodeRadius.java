package ps.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import ps.PSXform;
import ps.scenario.PSDrawNodeScenario;
import x.XApp;
import x.XLoggableCmd;

public class PSCmdToUpdateNodeRadius extends XLoggableCmd {
        //fields
    private Point mScreenPt = null;
    private Point.Double mWorldPt = null;

    //private constructor
    private PSCmdToUpdateNodeRadius(XApp app, Point pt) {
        super(app);
        this.mScreenPt = pt;
    }
    
    public static boolean execute(XApp app, Point pt) {
        PSCmdToUpdateNodeRadius cmd = new PSCmdToUpdateNodeRadius(app, pt);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        //PSDrawNodeScenario scenario = PSDrawNodeScenario.getSingleton();
        //scenario.getNode().updateEllipse(mScreenPt);
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
