package ps;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PSPenMarkMgr {
    
    // constants
    public static final int MAX_NUM_PEN_MARKS = 10;
    
    // fields
    private ArrayList<PSPenMark> mPenMarks = null;
    public ArrayList<PSPenMark> getPenMarks() {
        return this.mPenMarks;
    }
    
    // constructor
    public PSPenMarkMgr () {
        this.mPenMarks = new ArrayList<PSPenMark>();
    }
    
    public void addPenMark(PSPenMark penMark) {
        this.mPenMarks.add(penMark);
        if (this.mPenMarks.size() > PSPenMarkMgr.MAX_NUM_PEN_MARKS) {
            this.mPenMarks.remove(0);
            assert(this.mPenMarks.size() <= PSPenMarkMgr.MAX_NUM_PEN_MARKS);
        }
    }
    
    public PSPenMark getLastPenMark() {
        int size = this.mPenMarks.size();
        if (size == 0) {
            return null;
        }
        else {
            return this.mPenMarks.get(size - 1);
        } 
    }
    
    // the last pen mark if i = 0;
    public PSPenMark getRecentPenMark(int i) {
        int size = this.mPenMarks.size();
        int index = size - 1 - i;
        if (index < 0 || index >= size) {
            return null;
        } else {
            return this.mPenMarks.get(index);
        }
    }
    
    public boolean mousePressed(MouseEvent e) {
        Point pt = e.getPoint();
        PSPenMark penMark = new PSPenMark(pt);
        this.addPenMark(penMark);
        return true;
    }
    
    public boolean mouseDragged(MouseEvent e) {
        Point pt = e.getPoint();
        PSPenMark penMark = this.getLastPenMark();
        //if penmark is not null then return true
        return penMark != null && penMark.addPt(pt);
    }
    
    public boolean mouseReleased(MouseEvent e) {
        return true;
    }
}
