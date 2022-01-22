package botBoi.bots;

import battlecode.common.*;

public strictfp class Miner extends Base {

    public Miner(RobotController rc) {
        super(rc);
    }
    public void loop() throws GameActionException{
        MapLocation[] mapArr = rc.senseNearbyLocationsWithLead();
            for (int i = 0; i < mapArr.length; i++) {
                System.out.println("THERE IS LEAD HERE AT "+mapArr[i]);
            }
        while(true) {
            int p = 1 + (int)(Math.random() * ((2 - 1) + 1));

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
