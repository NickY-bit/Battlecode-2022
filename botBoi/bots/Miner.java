package botBoi.bots;

import battlecode.common.*;
import java.util.*;

public strictfp class Miner extends Base {

    public Miner(RobotController rc) {
        super(rc);
    }

    public void loop() throws GameActionException{
        while(true) {
            if (rc.isActionReady()) {
                moveRng();
            }
            Clock.yield();
        }
    }
}
