package botBoi.bots;

import battlecode.common.*;

public abstract strictfp class Base {
    //East - 1 = Northeast, East + 1 = Southeast, same with west
    static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.NORTHWEST,
            Direction.WEST,
            Direction.SOUTHWEST,
    };

    public final RobotController rc;
    public Base(final RobotController rc) {
        this.rc = rc;
    }
    public abstract void loop() throws GameActionException;
    public int savedIndex = -1; //index for the array

    static final int UNIT_VIS_RAD = 20;

    /**
     * Move north until an edge is found then follow it clockwise.
     * @throws GameActionException
     */
    public void followEdge() throws GameActionException { // use map size and spawn location to find the closest edge
        int vis = (int) Math.sqrt(UNIT_VIS_RAD);
        MapLocation loc = rc.getLocation();
        if (!rc.onTheMap(loc.translate(0, vis))) {
            if (!rc.onTheMap(loc.translate(vis, 0))) {
                tryMove(Direction.SOUTH);
            } else {
                tryMove(Direction.EAST);
            }
        } else if (!rc.onTheMap(loc.translate(vis, 0))) {
            if (!rc.onTheMap(loc.translate(0, -vis))) {
                tryMove(Direction.WEST);
            } else {
                tryMove(Direction.SOUTH);
            }
        } else if (!rc.onTheMap(loc.translate(0, -vis))) {
            if (!rc.onTheMap(loc.translate(-vis, 0))) {
                tryMove(Direction.NORTH);
            } else {
                tryMove(Direction.WEST);
            }
        } else {
            tryMove(Direction.NORTH);
        }
    }

    public void randomDiag() throws GameActionException { // randomly moves diagonally
        int vis = (int) Math.sqrt(UNIT_VIS_RAD);
        MapLocation loc = rc.getLocation();
        int randomNum = 1 + (int)(Math.random() * ((4 - 1) + 1));
        if (randomNum == 1) {
            tryMove(Direction.NORTHEAST);
        }
        else if (randomNum == 2) {
            tryMove(Direction.NORTHWEST);
        }
        else if (randomNum == 3) {
            tryMove(Direction.SOUTHEAST);
        }
        else {
            tryMove(Direction.SOUTHWEST);
        }
    }

    public void findLead() throws GameActionException{
        int vis = (int) Math.sqrt(UNIT_VIS_RAD);

    }

    /**
     * Try to move in the given direction
     * @param dir direction to move in
     * @return whether or not the bot moved
     * @throws GameActionException
     */
    public boolean tryMove(Direction dir) throws GameActionException{
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else {
            int d = -3;
            switch (dir) { // this is super scuffed but it works
                case NORTH: d = 0; break;
                case NORTHEAST: d = 1; break;
                case EAST: d = 2; break;
                case SOUTHEAST: d = 3; break;
                case SOUTH: d = 4; break;
                case NORTHWEST: d = 5; break;
                case WEST: d = 6; break;
                case SOUTHWEST: d = 7; break;
            }
            if (d == 2 || d == 6) {
                if (rc.canMove(directions[d + 1])) {
                    rc.move(directions[d + 1]); return true;
                } else if (rc.canMove(directions[d - 1])) {
                    rc.move(directions[d - 1]); return true;
                }
            } else if (d == 0 || d == 4) {
                if (d == 0) {
                    if (rc.canMove(Direction.NORTHEAST)) {
                        rc.move(Direction.NORTHEAST); return true;
                    } else if (rc.canMove(Direction.NORTHWEST)) {
                        rc.move(Direction.NORTHWEST); return true;
                    }
                } else {
                    if (rc.canMove(Direction.SOUTHEAST)) {
                        rc.move(Direction.SOUTHEAST); return true;
                    } else if (rc.canMove(Direction.SOUTHWEST)) {
                        rc.move(Direction.SOUTHWEST); return true;
                    }
                }
            } else {
                moveRng();
            }
            return false;
        }
    }

    public void moveRng() throws GameActionException{
        int dir = (int)(Math.random() * 8);
        while (rc.canMove(directions[dir])) {
            dir++;
            if (dir >= 8) {
                break;
            }
        }
    }

    /**
     * Get robot with the lowest health from an array of robots
     * @param arr array of robots to be scanned
     * @param t type of robot
     * @return RobotInfo of lowest hp bot, null if there are no valid bots.
     */
    public RobotInfo getLowestHealth(RobotInfo[] arr, RobotType t){

        RobotInfo lowest = null;

        for (RobotInfo b : arr) {
            if (b.type == t && (lowest == null || b.health < lowest.health)) {
                lowest = b;
            }
        }

        return lowest;
    }

    public RobotInfo getLowestHealth(RobotInfo[] arr){

        RobotInfo lowest = null;

        for (RobotInfo b : arr) {
            if (lowest == null || b.health < lowest.health) {
                lowest = b;
            }
        }

        return lowest;
    }

    /**
     * We don't have time to write a bit-encoder, so we're going with simple decimal based comms.
     * @param fir priority, 0 nothing, 2 archon imminent danger, 1 enemy archon, 4 large lead vein, 3 enemy spotted
     * @param tar target MapLocation
     * @param ind index
     * @throws GameActionException invalid number
     */
    public void writeComms(int fir, MapLocation tar, int ind) throws GameActionException{
        int sec = tar.x / 10;
        int thi = tar.x % 10;
        int fou = tar.y / 10;
        int fiv = tar.y % 10;

        int msg = (fir * 10000) + (sec * 1000) + (thi * 100) + (fou * 10) + fiv;
        rc.writeSharedArray(ind, msg);
    }

    public void resetComm(int ind) throws GameActionException{
        rc.writeSharedArray(ind, 0);
    }

    public MapLocation readComms(int ind) throws GameActionException{
        int info = rc.readSharedArray(ind);
        MapLocation target;
        info = info % 10000;
        int x = ((info / 1000) * 10) + ((info % 1000) / 100);
        int y = (((info % 100) / 10) * 10) + (info % 10);

        target = new MapLocation(x, y);

        return target;
    }

}