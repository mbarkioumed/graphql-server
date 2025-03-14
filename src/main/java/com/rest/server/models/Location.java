package com.rest.server.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    private String locationId;
    private String locationStreet;
    private String locationCity;
    private String locationState;
    private String locationCountry;
    private String locationTimezone;

    // GraphQL field adapters
    public String getId() {
        return this.locationId;
    }

    public String getStreet() {
        return this.locationStreet;
    }

    public String getCity() {
        return this.locationCity;
    }

    public String getState() {
        return this.locationState;
    }

    public String getCountry() {
        return this.locationCountry;
    }

    public String getTimezone() {
        return this.locationTimezone;
    }
}
