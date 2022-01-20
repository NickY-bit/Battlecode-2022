package botBoi.bots;

import battlecode.common.*;
import java.util.*;

public strictfp class Miner extends Base {

    public Miner(RobotController rc) {
        super(rc);
    }

    public void loop() throws GameActionException{
        while(true) {
            MapLocation me = rc.getLocation();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                    // Notice that the Miner's action cooldown is very low.
                    // You can mine multiple times per turn!
                    while (rc.canMineGold(mineLocation)) {
                        rc.mineGold(mineLocation);
                    }
                    while (rc.canMineLead(mineLocation)) {
                        rc.mineLead(mineLocation);
                    }
                }
            }

            if (rc.isActionReady()) {
                moveRng();
            }
            Clock.yield();
        }
    }
}
