package hw13;

public class Student {
    private String name;
    private double height;
    private double weight;
    private double bmi;

    public Student(String name, double height, double weight) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.bmi = calculateBMI();
    }

    private double calculateBMI() {
        return weight / ((height / 100) * (height / 100));
    }

    public String getName() {
        return name;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public double getBmi() {
        return bmi;
    }
}
