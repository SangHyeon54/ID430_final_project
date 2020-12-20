package ps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JPanel;

public class PSCanvas2D extends JPanel {
    //constants (final keywords)
    private static final Color COLOR_PT_CURVE_DEFAULT = new Color(0, 0, 0, 192);
    public static final Color COLOR_CUR_NODE_ELLIPSE = new Color(51, 51, 255, 255);
    public static final Color COLOR_NODE_ELLIPSE = Color.BLACK;
    public static final Color COLOR_NODE_ELLIPSE_BG = Color.WHITE;
     public static final Color COLOR_CUR_EDGE_ARROW = new Color(51, 51, 255, 255);
    public static final Color COLOR_EDGE_ARROW = Color.BLACK;
    public static final Color COLOR_CROSSHAIR = new Color(255, 0, 0, 64);
    private static final Color COLOR_INFO = new Color(255, 0, 0, 128);
    
    private static final Stroke STROKE_PT_CURVE_DEFAULT = new BasicStroke(2.0f,
        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static final Stroke STROKE_CUR_NODE_ELLIPSE = new BasicStroke(3.0f);
    public static final Stroke STROKE_NODE_ELLIPSE = new BasicStroke(3.0f);
    public static final Stroke STROKE_CUR_EDGE_ARROW = new BasicStroke(2.0f);
    public static final Stroke STROKE_EDGE_ARROW = new BasicStroke(2.0f);
    public static final Stroke STROKE_CROSSHAIR = new BasicStroke(5.0f);
    private static final Font FONT_INFO = 
        new Font("Monospaced", Font.PLAIN, 24);
    private static final Font FONT_MODE_INFO = 
        new Font("Monospaced", Font.PLAIN, 40);
    private static final Font FONT_EDGE_RETURN_SCENE = 
        new Font("Monospaced", Font.PLAIN, 10);

    public static final double CROSSHAIR_RADIUS = 30;
    public static final float STROKE_WIDTH_INCREASEMENT = 1.0f;
    
    //memb variables
    private PSApp mApp = null;
    private Stroke mCurStrokeForPtCurve = null;
    public Stroke getCurStrokeForPtCurve() {
        return this.mCurStrokeForPtCurve;
    }
    
    private Color mCurColorForPtCurve = null;
    public Color getCurColorForPtCurve() {
        return this.mCurColorForPtCurve;
    }
    public void setCurColorForPtCurve(Color c) {
        this.mCurColorForPtCurve = c;
    }
    
    //constructor
    public PSCanvas2D(PSApp jsi) {
        this.mApp = jsi;
        this.mCurStrokeForPtCurve = PSCanvas2D.STROKE_PT_CURVE_DEFAULT;
        this.mCurColorForPtCurve = PSCanvas2D.COLOR_PT_CURVE_DEFAULT;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; //changing graphics to graphics2D

        //transform the coordinate system from screen to world.
        //note : the transformation of a coordinate system is the reversed
        //process of the transformation of a geometry.
        g2.transform(this.mApp.getXform().getCurXformFromWorldToScreen());
        
        // render common world objects.
        this.drawNodes(g2);
        this.drawCurNode(g2);
        this.drawEdges(g2);
        this.drawCurEdge(g2);
        this.drawCurPtCurve(g2);
        
        // render the current scene's world objects.
        PSScene curScene = (PSScene) this.mApp.getScenarioMgr().getCurScene();
        curScene.renderWorldOjbects(g2);
        
        // transform the coordinate system from world to screen.
        g2.transform(this.mApp.getXform().getCurXformFromScreenToWorld());
        
        // render common screen objects.
        this.drawInfo(g2);
        
        // render curscene objects
        curScene.renderScreenOjbects(g2);
    }

    private void drawPtCurves(Graphics2D g2) {
        for (PSPtCurve ptCurve : this.mApp.getPtCurveMgr().getPtCurves()) {
            this.drawPtCurve(g2, ptCurve, ptCurve.getColor(), 
                ptCurve.getStroke());
        }
    }
    
    private void drawNodes(Graphics2D g2) {
        for (PSNode node : this.mApp.getNodeMgr().getNodes()) {
            this.drawNode(g2, node, PSCanvas2D.COLOR_NODE_ELLIPSE,
                PSCanvas2D.STROKE_NODE_ELLIPSE);
        }    
    }
    
    private void drawCurNode(Graphics2D g2) {
        PSNode node = this.mApp.getNodeMgr().getCurNode();
        if (node != null) { 
            // draw ellipse
            this.drawNode(g2, node, PSCanvas2D.COLOR_CUR_NODE_ELLIPSE,
                PSCanvas2D.STROKE_CUR_NODE_ELLIPSE);
        }
    }
    
    private void drawNode(Graphics2D g2, PSNode node, Color c, Stroke s) {
        // draw basic shape of the node
        node.drawNode(g2, c, s);
        
        // draw(write) mode of node
        g2.setFont(PSCanvas2D.FONT_MODE_INFO);
        
        int x_mode = (int) Math.round(node.getCenter().x - 10);
        int y_mode = (int) Math.round(node.getCenter().y + node.getRadius() 
            - 10);
        
        if (node.getIsQuasi()) {
            g2.drawString("Q", x_mode, y_mode);
        } else {
            g2.drawString("M", x_mode, y_mode);
        }
        
        // draw node name pt curves
        for (PSPtCurve namePtCurve : node.getName()) {
                this.drawPtCurve(g2, namePtCurve, 
                    namePtCurve.getColor(),
                    namePtCurve.getStroke());
        }
    }
    
    private void drawEdges (Graphics2D g2) {
        // since edges are not selected, set the edgeCmd box invisible
        for (PSEdge edge : this.mApp.getEdgeMgr().getEdges()) {
            edge.getCmd().setInvisible();
            this.drawEdge(g2, edge, PSCanvas2D.COLOR_EDGE_ARROW,
                PSCanvas2D.STROKE_EDGE_ARROW);
        }    
    }
    
    private void drawCurEdge (Graphics2D g2) {
        // since edge is currently selected, show the edgeCmd box.
        PSEdge edge = this.mApp.getEdgeMgr().getCurEdge();
        if (edge != null) { 
            edge.getCmd().setVisible();
            this.drawEdge(g2, edge, PSCanvas2D.COLOR_CUR_EDGE_ARROW,
                PSCanvas2D.STROKE_CUR_EDGE_ARROW);
        }
    }
    
    private void drawEdge (Graphics2D g2, PSEdge edge, Color c, Stroke s) {
        g2.setFont(PSCanvas2D.FONT_EDGE_RETURN_SCENE);
        edge.drawEdge(g2, c, s);
        
        for (PSPtCurve edgeInputPtCurve : edge.getInput().getInputPtCurve()) {
            this.drawPtCurve(g2, edgeInputPtCurve, 
                edgeInputPtCurve.getColor(),
                edgeInputPtCurve.getStroke());
        }
        
        for (PSPtCurve edgeCmdPtCurve : edge.getCmd().getCmdPtCurve()) {
            this.drawPtCurve(g2, edgeCmdPtCurve, 
                edgeCmdPtCurve.getColor(),
                edgeCmdPtCurve.getStroke());
        }
    }

    private void drawCurPtCurve(Graphics2D g2) {
        PSPtCurve ptCurve = this.mApp.getPtCurveMgr().getCurPtCurve();
        if (ptCurve != null) {
            this.drawPtCurve(g2, ptCurve, ptCurve.getColor(), 
                ptCurve.getStroke());
        }
    }

    private void drawPtCurve(Graphics2D g2, PSPtCurve ptCurve, 
        Color c, Stroke s) {
        Path2D.Double path = new Path2D.Double();
        ArrayList<Point2D.Double> pts = ptCurve.getPts();
        if (pts.size () < 2) {
            return;
        }

        Point2D.Double pt0 = pts.get(0);
        path.moveTo(pt0.x, pt0.y);
        for (int i = 1; i < pts.size(); i++) {
            Point2D.Double pt = pts.get(i);
            path.lineTo(pt.x, pt.y);
        }

        g2.setColor(c);
        g2.setStroke(s);
        g2.draw(path);
    }
    
    //get information of the current scene and print out
    private void drawInfo(Graphics2D g2) {
        PSScene curScene = (PSScene) this.mApp.getScenarioMgr().getCurScene();
        String str = curScene.getClass().getSimpleName();
        g2.setColor(PSCanvas2D.COLOR_INFO);
        g2.setFont(PSCanvas2D.FONT_INFO);
        g2.drawString(str, 20, 30);
    }
}
