package botBoi.bots;

import battlecode.common.*;

import java.util.Map;

public strictfp class Soldier extends Base {

    public Soldier(RobotController rc) {
        super(rc);
    }

    public void loop() throws GameActionException{
        while (true) {

            MapLocation target = rc.getLocation();
            int priority = 6;

            for (int i = 0; i < 64; i++) {
                int info = rc.readSharedArray(i);
                if (info != 0) {
                    if (info / 10000 < priority) {
                        priority = info / 10000;
                        target = readComms(i);
                        savedIndex = i;
                    }
                }
            }

            RobotInfo[] enmy = rc.senseNearbyRobots(20, rc.getTeam().opponent());

            if(rc.getLocation() != target) {
                attackRng(enmy);
            } else if (savedIndex >= 0){
                resetComm(savedIndex);
                savedIndex = -1;
            } else {
                attackRng(enmy);
                int p = 1 + (int)(Math.random() * ((2 - 1) + 1));
                if (p == 1) {
                    followEdge();
                }
                else {
                    followEdge();
                    randomDiag();
                }
            }

            for (RobotInfo bot : enmy) {
                if (bot.type == RobotType.ARCHON) {
                    for (int i = 0; i < 64; i++) {
                        int arr = rc.readSharedArray(i);
                        if (arr == 0) {
                            writeComms(2, bot.location, i);
                            break;
                        }
                    }
                }
            }

            Clock.yield();
        }
    }

    private void attackRng(RobotInfo[] enmy) throws GameActionException{
        if (rc.isActionReady()) {
            for (RobotInfo bot : enmy) {
                if (rc.canAttack(bot.location)) {
                    rc.attack(bot.location);
                }
            }
        }
    }
}
