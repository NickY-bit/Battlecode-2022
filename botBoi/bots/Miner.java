package botBoi.bots;

import battlecode.common.*;

public strictfp class Miner extends Base {

    public Miner(RobotController rc) {
        super(rc);
    }

    public void loop() throws GameActionException{

        int p = 1 + (int)(Math.random() * 3);
        MapLocation target = new MapLocation(-1, -1);

        while(true) {
            MapLocation me = rc.getLocation();
            MapLocation[] leadVis = rc.senseNearbyLocationsWithLead();
            MapLocation[] leadAct = rc.senseNearbyLocationsWithLead(2);
            RobotInfo[] enmy = rc.senseNearbyRobots(20, rc.getTeam().opponent());
            RobotInfo[] enmyArchons = getArrUnits(enmy, RobotType.ARCHON);

            if (enmyArchons.length > 0) {
                tagBots(enmyArchons);
            }

            int ttlPb = 0;
            for (MapLocation l : leadVis) {
                ttlPb += rc.senseLead(l);
            }
            if (ttlPb > 400) {
                MapLocation mLoc = meanLocation(leadVis);
                tagLoc(mLoc);
                System.out.println("Large vein at " + mLoc);
            }

            if (target.x == -1) {
                for (int i = 0; i < 64; i++) {
                    int info = rc.readSharedArray(i);
                    if (info != 0) {
                        if (info / 10000 == 4) {
                            MapLocation nearest = readComms(i);
                            if (distanceTo(me, nearest) < distanceTo(me, target) || target.x == -1) {
                                target = nearest;
                                savedIndex = i;
                            }
                        }
                    } else if (i == savedIndex) { //info is 0
                        savedIndex = -1;
                        target = new MapLocation(-1, -1);
                    }
                }
            }
            if (savedIndex >= 0 && Math.pow(distanceTo(me, target), 2) < 16) {
                if (ttlPb < 200) {
                    resetComm(savedIndex);
                    savedIndex = -1;
                }
            }

            if (rc.isActionReady()) {
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
            }
            if (target.x != -1) {
                tryMove(rc.getLocation().directionTo(target));
            } else {
                movePreset(p);
            }
            Clock.yield();
        }
    }
}
