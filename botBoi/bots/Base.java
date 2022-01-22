package botBoi.bots;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public abstract void loop() throws GameActionException;
    public static int savedIndex;
    public Team team;
    public Base(final RobotController rc) {
        this.rc = rc; savedIndex = -1; team = rc.getTeam();
    }

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

    public void followEdgeRev() throws GameActionException { // use map size and spawn location to find the closest edge
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
            tryMove(Direction.SOUTH);
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
        else if (randomNum == 4) {
            tryMove(Direction.SOUTHWEST);
        }
    }

    public void movePreset(int p) throws GameActionException {
        if (p == 1) {
            followEdge();
        } else if (p == 2) {
            randomDiag();
        } else {
            followEdgeRev();
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

    public void wipeComm() throws GameActionException{ //should only be used for debugging but probably wont be
        for(int i = 0; i < 64; i++) {
            rc.writeSharedArray(i, 0);
        }
    }

    public MapLocation readComms(int ind) throws GameActionException{
        int info = rc.readSharedArray(ind);

        if (info == 0) {
            return null;
        }

        MapLocation target;
        info = info % 10000;
        int x = ((info / 1000) * 10) + ((info % 1000) / 100);
        int y = (((info % 100) / 10) * 10) + (info % 10);

        target = new MapLocation(x, y);

        return target;
    }

    public boolean msgAlreadyExists(int fir, MapLocation tar) throws GameActionException{
        int sec = tar.x / 10;
        int thi = tar.x % 10;
        int fou = tar.y / 10;
        int fiv = tar.y % 10;

        int msg = (fir * 10000) + (sec * 1000) + (thi * 100) + (fou * 10) + fiv;

        for (int i = 0; i < 64; i++) {
            if (rc.readSharedArray(i) == msg) {
                return true;
            }
        }
        return false;
    }

    public int getInd(int fir, MapLocation tar) throws GameActionException{
        int sec = tar.x / 10;
        int thi = tar.x % 10;
        int fou = tar.y / 10;
        int fiv = tar.y % 10;

        int msg = (fir * 10000) + (sec * 1000) + (thi * 100) + (fou * 10) + fiv;
        for(int i = 0; i < 64; i++) {
            if (rc.readSharedArray(i) == msg) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Write a message to the sharedArray of bots' locations
     * @param b array of robots to be tagged
     * @throws GameActionException
     */
    public void tagBots(RobotInfo[] b) throws GameActionException{
        for (RobotInfo bot : b) {
            if (!msgAlreadyExists(1, bot.location)) {
                for (int i = 0; i < 64; i++) {
                    int arr = rc.readSharedArray(i);
                    if (arr == 0) {
                        writeComms(1, bot.location, i);
                        break;
                    }
                }
            }
        }
    }

    public void tagLoc(MapLocation l) throws GameActionException{
        boolean redundant = false;
        outer:
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                MapLocation tagLoc = new MapLocation(l.x + dx, l.y + dy);
                if (msgAlreadyExists(4, tagLoc)) {
                    redundant = true;
                    break outer;
                }
            }
        }
        if (!redundant) {
            for (int i = 0; i < 64; i++) {
                int arr = rc.readSharedArray(i);
                if (arr == 0) {
                    writeComms(4, l, i);
                    break;
                }
            }
        }
    }

    public int getNumUnits(RobotInfo[] l, RobotType t) {
        int num = 0;
        for (RobotInfo b : l) {
            if (b.type == t) {
                num++;
            }
        }
        return num;
    }

    public RobotInfo[] getArrUnits(RobotInfo[] l, RobotType t) {
        List<RobotInfo> units = new ArrayList<RobotInfo>();

        for (RobotInfo b : l) {
            if (b.type == t) {
                units.add(b);
            }
        }

        return units.toArray(new RobotInfo[0]);
    }

    public int distanceTo(MapLocation self, MapLocation targ) {
        int dx = targ.x - self.x;
        int dy = targ.y - self.y;
        int d = (int) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        return d;
    }

    public MapLocation meanLocation(MapLocation[] locs) {
        int x = 0;
        int y = 0;
        for (MapLocation l : locs) {
            x += l.x;
            y += l.y;
        }
        x /= locs.length;
        y /= locs.length;

        return new MapLocation(x, y);
    }

    public boolean archonFound() throws GameActionException{
        for (int i = 0; i < 64; i++) {
            int info = rc.readSharedArray(i);
            if (info / 10000 == 1) {
                return true;
            }
        }
        return false;
    }

}
