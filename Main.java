import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PetDAO dao = new PetDAO();
        AdoptionDAO adao = new AdoptionDAO();

        while (true) {
            System.out.println("\n=== Pet Adoption and Vet Records ===");
            System.out.println("1. Add Pet");
            System.out.println("2. View All Pets");
            System.out.println("3. Update Pet Status");
            System.out.println("4. Delete Pet");
            System.out.println("5. Register Adopter");
            System.out.println("6. Adopt a Pet");
            System.out.println("7. Add Vaccination");
            System.out.println("8. Vaccinations Due (next 30 days)");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Species (Dog/Cat/Bird): ");
                        String species = sc.nextLine();
                        System.out.print("Breed: ");
                        String breed = sc.nextLine();
                        System.out.print("Age: ");
                        int age = Integer.parseInt(sc.nextLine());
                        dao.addPet(name, species, breed, age);
                        System.out.println("Pet added successfully.");
                        break;
                    case "2":
                        for (Pet p : dao.getAllPets()) {
                            System.out.println(p);
                            System.out.println("   Care: " + p.getCareInfo());
                        }
                        break;
                    case "3":
                        System.out.print("Pet ID: ");
                        int uid = Integer.parseInt(sc.nextLine());
                        System.out.print("New status (Available/Adopted): ");
                        String status = sc.nextLine();
                        dao.updateStatus(uid, status);
                        System.out.println("Status updated.");
                        break;
                    case "4":
                        System.out.print("Pet ID to delete: ");
                        int did = Integer.parseInt(sc.nextLine());
                        dao.deletePet(did);
                        System.out.println("Pet deleted.");
                        break;
                    case "5":
                        System.out.print("Adopter name: ");
                        String an = sc.nextLine();
                        System.out.print("Phone: ");
                        String ph = sc.nextLine();
                        System.out.print("Address: ");
                        String ad = sc.nextLine();
                        int newId = adao.addAdopter(an, ph, ad);
                        System.out.println("Adopter registered. Adopter ID = " + newId);
                        break;
                    case "6":
                        System.out.print("Pet ID: ");
                        int pid = Integer.parseInt(sc.nextLine());
                        System.out.print("Adopter ID: ");
                        int aid = Integer.parseInt(sc.nextLine());
                        adao.adoptPet(pid, aid);
                        System.out.println("Adoption successful! Pet status is now Adopted.");
                        break;
                    case "7":
                        System.out.print("Pet ID: ");
                        int vid = Integer.parseInt(sc.nextLine());
                        System.out.print("Vaccine name: ");
                        String vn = sc.nextLine();
                        System.out.print("Given date (yyyy-mm-dd): ");
                        String gd = sc.nextLine();
                        System.out.print("Next due date (yyyy-mm-dd): ");
                        String nd = sc.nextLine();
                        adao.addVaccination(vid, vn, gd, nd);
                        System.out.println("Vaccination recorded.");
                        break;
                    case "8":
                        List<String> due = adao.getVaccinesDue(30);
                        if (due.isEmpty()) {
                            System.out.println("No vaccinations due in the next 30 days.");
                        } else {
                            for (String s : due) System.out.println(s);
                        }
                        break;
                    case "0":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (PetAlreadyAdoptedException e) {
                System.out.println("Cannot adopt: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}