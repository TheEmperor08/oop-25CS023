/**
 * Practical 02 - Part A - Program 3 (For Advanced Learners): Parking lot.
 * Two sections with fixed capacities and a static revenue shared by all lots.
 */
public class ParkingLot {

    private static long revenue = 0;

    private int twoWheelers;
    private int fourWheelers;
    private final int twoCap;
    private final int fourCap;

    public ParkingLot(int twoCap, int fourCap) {
        this.twoCap = twoCap;
        this.fourCap = fourCap;
    }

    /** Parks a vehicle of type "two" or "four"; returns false when refused. */
    public boolean park(String type) {
        if (type.equals("two") && twoWheelers < twoCap) {
            twoWheelers++;
            revenue += 20;
            return true;
        }
        if (type.equals("four") && fourWheelers < fourCap) {
            fourWheelers++;
            revenue += 40;
            return true;
        }
        if (type.equals("two") || type.equals("four")) {
            System.out.println("Full");
        } else {
            System.out.println("Unknown type: " + type);
        }
        return false;
    }

    /** A vehicle leaves; the count never goes below zero. */
    public void leave(String type) {
        if (type.equals("two") && twoWheelers > 0) {
            twoWheelers--;
        } else if (type.equals("four") && fourWheelers > 0) {
            fourWheelers--;
        }
    }

    public int getTwoWheelers() {
        return twoWheelers;
    }

    public int getFourWheelers() {
        return fourWheelers;
    }

    public static long getRevenue() {
        return revenue;
    }

    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot(2, 1);
        String[][] events = {
            {"park", "two"}, {"park", "two"}, {"park", "two"},
            {"park", "four"}, {"park", "four"},
            {"leave", "two"}, {"park", "two"},
            {"leave", "four"}, {"leave", "four"}, {"park", "four"}
        };
        for (String[] e : events) {
            String action = e[0];
            String type = e[1];
            if (action.equals("park")) {
                boolean ok = lot.park(type);
                System.out.println("park " + type + " -> " + (ok ? "parked" : "rejected"));
            } else {
                lot.leave(type);
                System.out.println("leave " + type + " -> done");
            }
        }
        System.out.println("Occupancy: two=" + lot.getTwoWheelers()
                + ", four=" + lot.getFourWheelers());
        System.out.println("Revenue: " + ParkingLot.getRevenue());
    }
}
