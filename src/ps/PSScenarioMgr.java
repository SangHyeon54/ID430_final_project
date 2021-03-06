package ps;

import ps.scenario.PSDefaultScenario;
import ps.scenario.PSDrawEdgeScenario;
import ps.scenario.PSDrawNodeScenario;
import ps.scenario.PSMoveNodeScenario;
import ps.scenario.PSNavigateScenario;

import x.XScenarioMgr;

public class PSScenarioMgr extends XScenarioMgr {
    //constructor
    public PSScenarioMgr(PSApp app) {
        super(app);
    }

    @Override
    protected void addScenarios() {
        this.mScenarios.add(PSDefaultScenario.createSingleton(this.mApp));
        this.mScenarios.add(PSDrawNodeScenario.createSingleton(this.mApp));
        this.mScenarios.add(PSDrawEdgeScenario.createSingleton(this.mApp));
        this.mScenarios.add(PSNavigateScenario.createSingleton(this.mApp));
        this.mScenarios.add(PSMoveNodeScenario.createSingleton(this.mApp));
    }

    @Override
    protected void setInitCurScene() {
        this.setCurScene(PSDefaultScenario.ReadyScene.getSingleton());
    }
}
