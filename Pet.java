public abstract class Pet {
    private int id;
    private String name;
    private String breed;
    private int age;
    private String status;

    public Pet(int id, String name, String breed, int age, String status) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBreed() { return breed; }
    public int getAge() { return age; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public abstract String getSpecies();
    public abstract String getCareInfo();

    public String toString() {
        return id + " | " + name + " | " + getSpecies() + " | " + breed
                + " | Age " + age + " | " + status;
    }
}

class Dog extends Pet {
    public Dog(int id, String name, String breed, int age, String status) {
        super(id, name, breed, age, status);
    }
    public String getSpecies() { return "Dog"; }
    public String getCareInfo() { return "Needs daily walks and rabies vaccine yearly."; }
}

class Cat extends Pet {
    public Cat(int id, String name, String breed, int age, String status) {
        super(id, name, breed, age, status);
    }
    public String getSpecies() { return "Cat"; }
    public String getCareInfo() { return "Needs a litter box and FVRCP vaccine yearly."; }
}

class Bird extends Pet {
    public Bird(int id, String name, String breed, int age, String status) {
        super(id, name, breed, age, status);
    }
    public String getSpecies() { return "Bird"; }
    public String getCareInfo() { return "Needs a spacious cage and regular health checks."; }
}