package botBoi;

import battlecode.common.*;
import java.util.*;
import botBoi.bots.Archon;
import botBoi.bots.Miner;
import botBoi.bots.Soldier;

@SuppressWarnings("unused")
public strictfp class RobotPlayer {

    public static void run(RobotController rc) {
        try {

            switch (rc.getType()) {
                case ARCHON:
                    new Archon(rc).loop();
                    break;
                case MINER:
                    new Miner(rc).loop();
                    break;
                case SOLDIER:
                    new Soldier(rc).loop();
                    break;
                case LABORATORY:
                case WATCHTOWER:
                case BUILDER:
                case SAGE:
                    break;
            }
        } catch (GameActionException e) {
            System.out.println(rc.getType() + " Exception");
            e.printStackTrace();

        } catch (Exception e) {
            System.out.println(rc.getType() + " Exception");
            e.printStackTrace();

        } finally {
            Clock.yield();
        }
    }
}

