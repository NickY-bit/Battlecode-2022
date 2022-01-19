package botBoi.bots;

import battlecode.common.*;
import java.util.*;

public strictfp class Soldier extends Base {

    public Soldier(RobotController rc) {
        super(rc);
    }

    public void loop() throws GameActionException{
        while (true) {
            RobotInfo[] bots = rc.senseNearbyRobots();
            if (rc.isActionReady()){
                for (RobotInfo bot : bots) {
                    if (bot.getTeam() == rc.getTeam().opponent()) {
                        if (rc.canAttack(bot.getLocation())) {
                            rc.attack(bot.getLocation());
                        } else {
                            moveTowards(bot.getLocation());
                        }
                    } else {
                        moveRng();
                    }
                }
            }
            Clock.yield();
        }
    }
}
