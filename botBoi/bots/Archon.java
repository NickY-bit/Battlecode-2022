package botBoi.bots;

import battlecode.common.*;
import java.util.*;

public strictfp class Archon extends Base {

        static int turn = 0;
        static int unitProd = 1;

        public Archon(RobotController rc) {
                super(rc);
        }

        public void loop() throws GameActionException{
                while (true) {
                        if (unitProd % 5 == 0) { //accidentally made production balanced since soldiers are more expensive than miners,
                                tryBuild(RobotType.SOLDIER); //when an archon builds a soldier, it has to wait for every other archon to build miners.
                        } else {
                                tryBuild(RobotType.MINER);
                        }
                        System.out.println(unitProd);
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
                        unitProd++;
                } else{
                        //return -3;
                }

                //return 0;
        }
}
