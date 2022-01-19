package botBoi.bots;

import battlecode.common.*;
import java.util.*;

public strictfp class Soldier extends Base {

    public Soldier(RobotController rc) {
        super(rc);
    }

    public void loop() throws GameActionException{
        RobotInfo[] bots = rc.senseNearbyRobots();
        for (RobotInfo bot : bots) {
            if (bot.getTeam() == rc.getTeam().opponent()) {
                if (rc.canAttack(bot.getLocation())) {
                    rc.attack(bot.getLocation());
                }
            }
        }
    }
}
