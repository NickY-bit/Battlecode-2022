package botBoi.bots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public abstract strictfp class Base {
    static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
    };

    public final RobotController rc;

    public Base(final RobotController rc) {
        this.rc = rc;
    }

    public abstract void loop() throws GameActionException;

    //use this for communication later
}
