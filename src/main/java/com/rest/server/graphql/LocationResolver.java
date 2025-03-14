package com.rest.server.graphql;

import com.rest.server.exception.ResourceNotFoundException;
import com.rest.server.models.Location;
import com.rest.server.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Controller
public class LocationResolver {
    private static final Logger logger = LoggerFactory.getLogger(LocationResolver.class);

    @Autowired
    private LocationService locationService;

    // Query to get all locations with pagination
    @QueryMapping
    public List<Location> locations(@Argument Integer page, @Argument Integer limit) {
        int pageNum = page != null ? page : 0;
        int pageSize = limit != null ? limit : 10;

        Page<Location> locationsPage = locationService.allLocations(PageRequest.of(pageNum, pageSize));
        return locationsPage.getContent();
    }

    // Query to get a single location by ID
    @QueryMapping
    public Location location(@Argument String id) {
        try {
            Optional<Location> locationOptional = locationService.singleLocation(id);
            return locationOptional.orElseThrow(() -> new ResourceNotFoundException("Location not found"));
        } catch (Exception e) {
            logger.error("Error fetching location with ID: {}", id, e);
            return null;
        }
    }

    // Mutation to create a location
    @MutationMapping
    public Location createLocation(@Argument String street, @Argument String city, @Argument String state, @Argument String country, @Argument String timezone) {

        Location newLocation = new Location();
        newLocation.setLocationStreet(street);
        newLocation.setLocationCity(city);
        newLocation.setLocationState(state);
        newLocation.setLocationCountry(country);
        newLocation.setLocationTimezone(timezone);

        return locationService.createLocation(newLocation);
    }

    // Mutation to update a location
    @MutationMapping
    public Location updateLocation(@Argument String id, @Argument String street, @Argument String city, @Argument String state, @Argument String country, @Argument String timezone) {

        Optional<Location> locationOptional = locationService.singleLocation(id);
        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();
            if (street != null) location.setLocationStreet(street);
            if (city != null) location.setLocationCity(city);
            if (state != null) location.setLocationState(state);
            if (country != null) location.setLocationCountry(country);
            if (timezone != null) location.setLocationTimezone(timezone);

            return locationService.updateLocation(id, location);
        }
        return null;
    }

    // Mutation to delete a location
    @MutationMapping
    public String deleteLocation(@Argument String id) {
        locationService.deleteLocation(id);
        return id;
    }
}