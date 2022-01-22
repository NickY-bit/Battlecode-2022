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
            MapLocation[] leadVis = rc.senseNearbyLocationsWithLead();
            MapLocation[] leadAct = rc.senseNearbyLocationsWithLead(2);

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
                followEdge();
            }
            Clock.yield();
        }
    }
}
