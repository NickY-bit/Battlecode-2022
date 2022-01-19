package botBoi.bots;

import battlecode.common.*;
import java.util.*;

public strictfp class Archon extends Base {

        static int turn = 0;

        public Archon(RobotController rc) {
                super(rc);
        }

        public void loop() throws GameActionException{
                switch(turn % 4) {
                        case 0: tryBuild(RobotType.MINER);
                        default: tryBuild(RobotType.SOLDIER);
                }
        }

        private int tryBuild(RobotType t) throws GameActionException {

                if (rc.getTeamLeadAmount(rc.getTeam()) < t.buildCostLead) {
                        return -1;
                } else if (rc.getTeamGoldAmount(rc.getTeam()) < t.buildCostGold) {
                        return -2;
                }
                int i = 0;
                while (i < 8 && !rc.canBuildRobot(t, directions[i])) {
                        i++;
                }
                if(i < 8) {
                        rc.buildRobot(t, directions[i]);
                } else{
                        return -3;
                }

                return 0;
        }
}
