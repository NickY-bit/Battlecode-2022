package botBoi.bots;

import battlecode.common.*;
import java.util.*;

public strictfp class Archon extends Base {

        static int turn = 0;

        public Archon(RobotController rc) {
                super(rc);
        }

        public void loop() throws GameActionException{
                while (true) {
                        switch(turn % 4) {
                                case 0: tryBuild(RobotType.MINER);
                                default: tryBuild(RobotType.SOLDIER);
                        }
                        System.out.println("I end my turn");
                        Clock.yield();
                }
        }

        private void tryBuild(RobotType t) throws GameActionException {

                boolean poor = false;

                if (rc.getTeamLeadAmount(rc.getTeam()) < t.buildCostLead) {
                        poor = true;
                        //return -1;
                } else if (rc.getTeamGoldAmount(rc.getTeam()) < t.buildCostGold) {
                        poor = true;
                        //return -2;
                }
                int i = 0;
                while (i < 8 && !rc.canBuildRobot(t, directions[i]) && !poor) {
                        i++;
                }
                if(i < 8 && rc.canBuildRobot(t, directions[i]) && rc.isActionReady()) {
                        rc.buildRobot(t, directions[i]);
                } else{
                        //return -3;
                }

                //return 0;
        }
}
