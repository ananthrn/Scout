package scout.random_player;

import scout.sim.*;

import java.util.*;

public class Player extends scout.sim.Player {
    List<Point> enemyLocations;
    List<Point> safeLocations;
    Random gen;
    int t,n;
    int x = -1;
    int y = -1;
    public Player(int id) {
        super(id);
    }

    @Override
    public void init(String id, int s, int n, int t, List<Point> landmarkLocations) {
        enemyLocations = new ArrayList<>();
        safeLocations = new ArrayList<>();
        gen = new Random(100);
        this.t = t;
        this.n = n;
    }

    /**
     * nearby IDs is a 3 x 3 grid of nearby IDs with you in the center (1,1) position. A position is null if it is off the board.
     * Enemy IDs start with 'E', Player start with 'P', Outpost with 'O' and landmark with 'L'.
     *
     */
    @Override
    public Point move(ArrayList<ArrayList<ArrayList<String>>> nearbyIds, List<CellObject> concurrentObjects) {
        System.out.println("I'm at " + x + " " + y);
        for(int i = 0 ; i < 3; ++ i) {
            for(int j = 0 ; j < 3 ; ++ j) {
                boolean safe = true;
                if(nearbyIds.get(i).get(j) == null) continue;
                for(String ID : nearbyIds.get(i).get(j)) {
                    if(ID.charAt(0) == 'E') {
                        safe = false;
                    }
                }
                if(x != -1) {
                    Point consideredLocation = new Point(x + i - 1, y + j - 1);
                    if(safe) {
                        if(!safeLocations.contains(consideredLocation)) {
                            safeLocations.add(consideredLocation);
                        }
                    } else {
                        if(!enemyLocations.contains(consideredLocation)) {
                            enemyLocations.add(consideredLocation);
                        }
                    }
                }
            }
        }
        for(CellObject obj : concurrentObjects) {
            if (obj instanceof Player) {
                //communicate?
            } else if (obj instanceof Enemy) {

            } else if (obj instanceof Landmark) {
                x = ((Landmark) obj).getLocation().x;
                y = ((Landmark) obj).getLocation().y;
            } else if (obj instanceof Outpost) {
                for(Point safe : safeLocations) {
                    ((Outpost) obj).addSafeLocation(safe);
                }
                for(Point unsafe : enemyLocations) {
                    ((Outpost) obj).addEnemyLocation(unsafe);
                }
            }
        }

        if(x!=-1) {
            //move to NW outpost
            if (nearbyIds.get(0).get(0) != null) {
                //move up and left
                x -= 1;
                y -= 1;
                return new Point(-1, -1);
            }
            if (nearbyIds.get(0).get(1) != null) {
                //move up
                y -= 1;
                return new Point(0, -1);
            }
            if (nearbyIds.get(1).get(0) != null) {
                //move left
                x -= 1;
                return new Point(-1, 0);
            }
            return new Point(0, 0);
        }

        //return x \in {-1,0,1}, y \in {-1,0,1}
        return new Point((gen.nextInt(3)) - 1, (gen.nextInt(3)) - 1);
    }

    /**
     * same as move but without actually returning anything, called every turn
     */
    @Override
    public void communicate(ArrayList<ArrayList<ArrayList<String>>> nearbyIds, List<CellObject> concurrentObjects) {

    }
}
