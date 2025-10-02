package VehicleRental;

import java.io.*;
import java.util.*;

public class FleetService {
    private List<Vehicle> fleet = new ArrayList<>();
    private File dataFile;

    public FleetService(File dataFile){
        this.dataFile = dataFile;
        loadFleet();
    }

    public FleetService(){ this(new File("fleet.txt")); }

    public List<Vehicle> getFleet(){ return fleet; }

    public boolean existsId(String id){
        for (Vehicle v: fleet) if (v.getId().equals(id)) return true;
        return false;
    }

    public void addVehicle(Vehicle v){
        fleet.add(v);
        saveFleet();
    }

    public Vehicle searchVehicle(String id){
        for (Vehicle v: fleet) if (v.getId().equals(id)) return v;
        return null;
    }

    public String rentVehicle(String id){
        Vehicle v = searchVehicle(id);
        if (v==null) return "Vehicle not found.";
        if (!(v instanceof Rentable)) return "Vehicle not rentable.";
        try { ((Rentable)v).rent(); saveFleet(); return (v instanceof Car?"Car":v instanceof Bike?"Bike":v instanceof Truck?"Truck":"Vehicle") + " " + id + " rented."; }
        catch (VehicleNotAvailableException e){ return e.getMessage(); }
    }

    public String returnVehicle(String id){
        Vehicle v = searchVehicle(id);
        if (v==null) return "Vehicle not found.";
        if (!(v instanceof Rentable)) return "Vehicle not rentable.";
        ((Rentable)v).returnVehicle(); saveFleet();
        return (v instanceof Car?"Car":v instanceof Bike?"Bike":v instanceof Truck?"Truck":"Vehicle") + " " + id + " returned.";
    }

    public void clearData(){
        fleet.clear();
        saveFleet();
    }

    public void importCsv(File f) throws IOException{
        try(BufferedReader br=new BufferedReader(new FileReader(f))){
            String line; while((line=br.readLine())!=null){
                String[] p=line.split(",");
                if (p.length<3) continue;
                String type=p[0], id=p[1], brand=p[2];
                if (existsId(id)) continue; // skip duplicates
                if ("Car".equals(type) && p.length>=4) fleet.add(new Car(id,brand,p[3]));
                else if ("Bike".equals(type) && p.length>=4) fleet.add(new Bike(id,brand,p[3]));
                else if ("Truck".equals(type) && p.length>=4) fleet.add(new Truck(id,brand,Double.parseDouble(p[3])));
            }
        }
        saveFleet();
    }

    public void exportCsv(File f) throws IOException{
        try(PrintWriter pw=new PrintWriter(new FileWriter(f))){
            for(Vehicle v: fleet){
                String type = v instanceof Car?"Car":v instanceof Bike?"Bike":v instanceof Truck?"Truck":"Vehicle";
                String extra = "";
                if (v instanceof Car) extra = ((Car)v).getSeats();
                else if (v instanceof Bike) extra = ((Bike)v).getType();
                else if (v instanceof Truck) extra = Double.toString(((Truck)v).getLoadCapacity());
                pw.print(type + "," + v.getId() + "," + v.getBrand() + "," + extra);
                pw.println();
            }
        }
    }

    private void loadFleet(){
        if (!dataFile.exists()) return;
        try(BufferedReader br=new BufferedReader(new FileReader(dataFile))){
            String line; while((line=br.readLine())!=null){
                String[] p=line.split(","); if (p.length<4) continue;
                String type=p[0], id=p[1], brand=p[2]; boolean rented=Boolean.parseBoolean(p[3]);
                Vehicle v=null;
                if ("Car".equals(type) && p.length>=5) v=new Car(id,brand,p[4]);
                else if ("Bike".equals(type) && p.length>=5) v=new Bike(id,brand,p[4]);
                else if ("Truck".equals(type) && p.length>=5) v=new Truck(id,brand,Double.parseDouble(p[4]));
                if (v!=null){ v.setRented(rented); fleet.add(v); }
            }
        } catch(IOException e){ /* ignore */ }
    }

    private void saveFleet(){
        try(PrintWriter pw=new PrintWriter(new FileWriter(dataFile))){
            for(Vehicle v: fleet){
                String type = v instanceof Car?"Car":v instanceof Bike?"Bike":v instanceof Truck?"Truck":"Vehicle";
                String extra = "";
                if (v instanceof Car) extra = ((Car)v).getSeats();
                else if (v instanceof Bike) extra = ((Bike)v).getType();
                else if (v instanceof Truck) extra = Double.toString(((Truck)v).getLoadCapacity());
                pw.print(type + "," + v.getId() + "," + v.getBrand() + "," + v.isRented() + "," + extra);
                pw.println();
            }
        } catch(IOException e){ e.printStackTrace(); }
    }
}
