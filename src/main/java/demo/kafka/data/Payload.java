package demo.kafka.data;

import java.util.ArrayList;
import java.util.Date;

public class Payload{
    public Dragon dragon;
    public String name;
    public String type;
    public boolean reused;
    public String launch;
    public ArrayList<String> customers;
    public ArrayList<Integer> norad_ids;
    public ArrayList<String> nationalities;
    public ArrayList<String> manufacturers;
    public int mass_kg;
    public int mass_lbs;
    public String orbit;
    public String reference_system;
    public String regime;
    public double longitude;
    public double semi_major_axis_km;
    public double eccentricity;
    public double periapsis_km;
    public double apoapsis_km;
    public double inclination_deg;
    public double period_min;
    public int lifespan_years;
    public Date epoch;
    public double mean_motion;
    public double raan;
    public double arg_of_pericenter;
    public double mean_anomaly;
    public String id;
}

class Dragon{
    public String capsule;
    public int mass_returned_kg;
    public int mass_returned_lbs;
    public int flight_time_sec;
    public String manifest;
    public boolean water_landing;
    public boolean land_landing;
}


