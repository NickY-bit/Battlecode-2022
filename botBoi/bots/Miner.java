package botBoi.bots;

import battlecode.common.*;

public strictfp class Miner extends Base {

    public Miner(RobotController rc) {
        super(rc);
    }
    public void loop() throws GameActionException{
        while(true) {

            //replace this its just the example code
            MapLocation me = rc.getLocation();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                    while (rc.canMineGold(mineLocation)) {
                        rc.mineGold(mineLocation);
                    }
                    while (rc.canMineLead(mineLocation)) {
                        rc.mineLead(mineLocation);
                    }
                }
            }

            if (rc.isActionReady()) {
                MapLocation[] leadVis = rc.senseNearbyLocationsWithLead();
                MapLocation[] leadAct = rc.senseNearbyLocationsWithLead(2);
                if (leadAct.length == 0) {
                    if (leadVis.length > 0) {
                        for (MapLocation i : leadVis) {
                            tryMove(rc.getLocation().directionTo(i));
                        }
                    }
                }
                else {
                    for (MapLocation i : leadAct) {
                        while (rc.canMineLead(i)) {
                            rc.mineLead(i);
                        }
                    }
                }

                int p = 1 + (int)(Math.random() * ((2 - 1) + 1));
                if (p == 1) {
                    followEdge();
                }
                else {
                    randomDiag();
                }
            }
            Clock.yield();
        }
    }
}
