package dg.webserviceusepoc;

/**
 * Created by KZ-Tech on 9/21/2017.
 */

public class Venue {
    String VenueId;
    String Venue;

    public Venue(String venueId, String venue) {
        VenueId = venueId;
        Venue = venue;
    }

    public String getVenueId() {
        return VenueId;
    }

    public void setVenueId(String venueId) {
        VenueId = venueId;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    @Override
    public String toString() {
        return Venue;
    }
}
