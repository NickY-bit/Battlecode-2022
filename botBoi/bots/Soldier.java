package botBoi.bots;

import battlecode.common.*;

public strictfp class Soldier extends Base {

    public Soldier(RobotController rc) {
        super(rc);
    }

    static final RobotType PRIORITY[] = {
            RobotType.BUILDER,
            RobotType.ARCHON,
            RobotType.WATCHTOWER,
            RobotType.SAGE,
            RobotType.SOLDIER,
            RobotType.MINER,
            RobotType.LABORATORY
    };

    public void loop() throws GameActionException{
        while (true) {

            //no coordination, just chases enemies when it sees one
            RobotInfo[] bots = rc.senseNearbyRobots();
            if (rc.isActionReady()){
                for (RobotInfo bot : bots) {
                    if (bot.getTeam() == rc.getTeam().opponent()) {
                        if (rc.canAttack(bot.location)) {
                            rc.attack(bot.getLocation());
                        } else {
                            tryMove(rc.getLocation().directionTo(bot.location));
                        }
                    } else {
                        if (rc.isActionReady()) {
                            followEdge();
                        }
                    }
                }
            }
            Clock.yield();
        }
    }
}
