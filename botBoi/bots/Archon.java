package botBoi.bots;

import battlecode.common.*;

public strictfp class Archon extends Base {

        static int turn = 0;
        static int unitProd = 0;
        final int NUM_ARCHON = rc.getArchonCount();
        int reserve = 75 * NUM_ARCHON;
        final Team team = rc.getTeam();
        boolean wait = false;
        int soldierProd = 3;

        public Archon(RobotController rc) {
                super(rc);
        }

        public void loop() throws GameActionException{

                for (int i = 0; i < 3; i++) {
                        if (rc.canBuildRobot(RobotType.MINER, Direction.NORTH)) {
                                tryBuild(RobotType.MINER);
                                unitProd--;
                        } else {
                                i--;
                        }
                }

                while (true) {
                        System.out.println("I am an archon and I have produced " + unitProd + " units!");
                        RobotInfo[] frens = rc.senseNearbyRobots(34, team);
                        RobotInfo[] enmy = rc.senseNearbyRobots(34, team.opponent());

                        if (NUM_ARCHON > 1 && unitProd > NUM_ARCHON && !wait) {
                                wait = true;
                                System.out.println("I am waiting");
                        }

                        if (turn > 500) {
                                soldierProd = 2;
                        }

                        if (!wait) {
                                if (rc.getTeamLeadAmount(team) > reserve || enmy.length > 0) {
                                        int buildStat = -3;
                                        if ((getNumMiners(frens) >= 3 || enmy.length > 0 || unitProd / soldierProd >= 1)) {
                                                buildStat = tryBuild(RobotType.SOLDIER);
                                        } else {
                                                buildStat = tryBuild(RobotType.MINER);
                                        }
                                        if (buildStat < 0) {
                                                healLowest(frens, RobotType.SOLDIER);
                                        }
                                }
                        } else {
                                if(rc.isActionReady()) {
                                        healLowest(frens);
                                }
                                unitProd--;
                                if (unitProd <= 0) {
                                        wait = false;
                                        System.out.println("I am done waiting");
                                }
                        }

                        Clock.yield();
                }
        }

        private int tryBuild(RobotType t) throws GameActionException {

                if (rc.getTeamLeadAmount(team) < t.buildCostLead) {
                        return -1;
                } else if (rc.getTeamGoldAmount(team) < t.buildCostGold) {
                        return -1;
                }
                int i = 0;
                while (i < 8 && !rc.canBuildRobot(t, directions[i])) {
                        i++;
                }
                if(i < 8 && rc.canBuildRobot(t, directions[i]) && rc.isActionReady()) {
                        rc.buildRobot(t, directions[i]);
                        unitProd++;
                        return 0;
                }
                return -2;
        }

        private int getNumMiners(RobotInfo[] l) throws GameActionException {
                int num = 0;
                for (RobotInfo b : l) {
                        if (b.type == RobotType.MINER) {
                                num++;
                        }
                }
                return num;
        }

        private void healLowest(RobotInfo[] arr, RobotType t) throws GameActionException{
                RobotInfo targ = getLowestHealth(arr, t);
                if (targ != null && rc.canAttack(targ.location)) { //change this to only target robots in range and so it always heals when possible.
                        rc.attack(targ.location);
                }
        }

        private void healLowest(RobotInfo[] arr) throws GameActionException{
                RobotInfo targ = getLowestHealth(arr);
                if (targ != null && rc.canAttack(targ.location)) { //change this to only target robots in range and so it always heals when possible.
                        rc.attack(targ.location);
                }
        }
}
