package botBoi.bots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

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
            return false;
        }
    }

    public void moveRng() throws GameActionException{
        int dir = (int)(Math.random() * 8);
        while (!tryMove(directions[dir])) {
            dir++;
            if (dir >= 8) {
                break;
            }
        }
    }

    /**
     * Try to move towards the target location
     * @param l target location
     * @throws GameActionException
     */
    public void moveTowards(MapLocation l) throws GameActionException{
        int xdif = l.x - rc.getLocation().x;
        int ydif = l.y - rc.getLocation().y;
        int dir = 0;

        if (xdif > 0) {
            dir = 2;
        } else if (xdif < 0) {
            dir = 6;
        }
        if (dir != 0) {
            if (ydif > 0) {
                dir -= 1;
            } else if (ydif < 0) {
                dir += 1;
            }
        } else {
            //if ydif > 0 dir remains 0
            if (ydif < 0) {
                dir = 4;
            }
        }

        tryMove(directions[dir]);

    }

    //add communication later
}
