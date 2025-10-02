# Vehicle Rental / Fleet Manager

Small Java project demonstrating inheritance, interfaces, simple persistence and a Swing GUI for managing a small vehicle fleet (Car/Bike/Truck).

## What it is
- Models: `Vehicle` (base) with `Car`, `Bike`, `Truck` subclasses.
- Interface: `Rentable` for renting/returning vehicles.
- Service: `FleetService` manages the in-memory fleet and reads/writes `fleet.txt` (simple CSV-like format).
- GUI: `FleetGUI` (Swing `JTable`) plus `FleetGUILauncher` to start the UI.

## Requirements
- Java 8+ (JDK)

## Data file
- The app uses `fleet.txt` in the `VehicleRental` folder to persist the fleet. The file format is simple CSV: `Type,ID,Brand,Rented,Extra`.

## Import / Export
- The GUI has Import/Export buttons to read/write a simple CSV file. CSV fields are not quoted — avoid commas inside fields.

## Notes & next improvements
- `Car` extra field now accepts arbitrary strings (not only integers).
- Consider adding a more robust CSV parser, editable table cells (with persistence), and keyboard shortcuts.
- A `.gitignore` was added to ignore `*.class`, docs and IDE files; consider removing large files from the repository history if needed.

