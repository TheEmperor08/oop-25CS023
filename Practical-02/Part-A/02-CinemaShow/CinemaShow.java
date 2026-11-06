/**
 * Practical 02 - Part A - Program 2: Cinema show.
 * Seats can only change through book() and cancel(); a static counter
 * tracks the seats booked across all shows.
 */
public class CinemaShow {

    private static int totalBooked = 0;

    private String title;
    private int seatsAvailable;
    private final int capacity;

    public CinemaShow(String title, int capacity) {
        this.title = title;
        this.capacity = capacity;
        this.seatsAvailable = capacity;
    }

    /** A show with the default capacity of 100 seats. */
    public CinemaShow(String title) {
        this(title, 100);
    }

    /** Books n seats if enough are free; otherwise leaves the show unchanged. */
    public boolean book(int n) {
        if (n > 0 && n <= seatsAvailable) {
            seatsAvailable -= n;
            totalBooked += n;
            return true;
        }
        return false;
    }

    /** Releases n seats, never going above the capacity. */
    public void cancel(int n) {
        if (n <= 0) {
            return;
        }
        seatsAvailable = Math.min(capacity, seatsAvailable + n);
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public String getTitle() {
        return title;
    }

    public static int getTotalBooked() {
        return totalBooked;
    }

    public static void main(String[] args) {
        CinemaShow show = new CinemaShow("Interstellar", 10);
        CinemaShow matinee = new CinemaShow("Inception");   // capacity 100

        System.out.println(show.getTitle() + ": " + show.getSeatsAvailable() + " seats");
        System.out.println(matinee.getTitle() + ": " + matinee.getSeatsAvailable() + " seats");

        printBooking(show, 4);
        printBooking(show, 8);          // more than available -> false
        show.cancel(2);
        System.out.println("cancel(2)  -> seats " + show.getSeatsAvailable());
        printBooking(show, 8);
        show.cancel(15);
        System.out.println("cancel(15) -> seats " + show.getSeatsAvailable()
                + " (never above capacity 10)");
        printBooking(matinee, 30);
        printBooking(matinee, 80);      // more than available -> false

        System.out.println("Total booked: " + CinemaShow.getTotalBooked());
    }

    /** Books n seats on the show and prints the result and the seats left. */
    private static void printBooking(CinemaShow s, int n) {
        boolean ok = s.book(n);
        System.out.println(s.getTitle() + " book(" + n + ") -> " + ok
                + ", seats " + s.getSeatsAvailable());
    }
}
