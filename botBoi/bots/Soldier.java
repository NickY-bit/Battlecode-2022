package botBoi.bots;

import battlecode.common.*;

import java.util.Map;

public strictfp class Soldier extends Base {

    public Soldier(RobotController rc) {
        super(rc);
    }

    public void loop() throws GameActionException{

        int priority = 6;
        int p = 1 + (int)(Math.random() * 4);
        MapLocation target = new MapLocation(-1, -1);

        while (true) {

            MapLocation me = rc.getLocation();
            RobotInfo[] enmy = rc.senseNearbyRobots(20, rc.getTeam().opponent());
            RobotInfo[] enmyArchons = getArrUnits(enmy, RobotType.ARCHON);

            if (enmyArchons.length > 0) {
                tagBots(enmyArchons);
            }

            if (target.x != -1 && rc.canSenseLocation(target)) {
                //System.out.println("Contact made with target at " + target + " I am at " + me);
                if (rc.canSenseRobotAtLocation(target)) {
                    //.out.println("Something on the target spot");
                    RobotInfo targBot = rc.senseRobotAtLocation(target);
                    if (targBot.team == team) {
                        target = new MapLocation(-1, -1);
                        resetComm(savedIndex);
                        //System.out.println("Archon bonked " + rc.readSharedArray(savedIndex));
                        savedIndex = -1;
                    }
                } else if (savedIndex >= 0){
                    target = new MapLocation(-1, -1);
                    resetComm(savedIndex);
                    //System.out.println("Archon bonked " + rc.readSharedArray(savedIndex));
                    savedIndex = -1;
                }
            } else if (target.x != -1 && savedIndex >= 0) {
                if (readComms(savedIndex) == null) {
                    //System.out.println("Awaiting orders");
                    target = new MapLocation(-1, -1);
                }
            }

            if (target.x == -1) {
                for (int i = 0; i < 64; i++) {
                    int info = rc.readSharedArray(i);
                    if (info != 0) {
                        if (info / 10000 < priority && info / 10000 < 3) {
                            //System.out.println("Acquired new target " + readComms(i));
                            priority = info / 10000;
                            target = readComms(i);
                            savedIndex = i;
                        }
                    } else if (i == savedIndex) {
                        priority = 6;
                        savedIndex = -1;
                    }
                    if (i == 63) { // this is not working as intended but it doesn't actually break anything so im not fixing it for now
                        priority = 6;
                    }
                }
            } else if (me.x != target.x && me.y != target.y && priority == 1) {
                if (!rc.canAttack(target)) {
                    tryMove(me.directionTo(target));
                } else {
                    rc.attack(target);
                }
            } else if (priority == 2) {
                if (!rc.canSenseLocation(target)) {
                    tryMove(me.directionTo(target));
                } else {
                    attackRng(enmy);
                }
            } else if (priority >= 3) {
                if (Math.pow(distanceTo(me, target), 2) < 25) {
                    if (!attackRng(enmy)) {
                        tryMove(me.directionTo(target));
                    }
                }
            }

            if(target.x != -1 && me.x != target.x && me.y != target.y) {
                //System.out.println("My location is" + me + " At index " + savedIndex + " Going to " + target);
                if (!attackRng(enmy)) {
                    tryMove(me.directionTo(target));
                }
            } else if (savedIndex >= 0 && target.x == me.x && target.y == me.y){
                resetComm(savedIndex);
                //System.out.println("Threat neutralized " + rc.readSharedArray(savedIndex));
                savedIndex = -1;
            } else {
                if (!attackRng(enmy)) {
                    movePreset(p);
                }
            }

            Clock.yield();
        }
    }

    private boolean attackRng(RobotInfo[] enmy) throws GameActionException{
        if (rc.isActionReady()) {
            for (RobotInfo bot : enmy) {
                if (rc.canAttack(bot.location)) {
                    rc.attack(bot.location);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
