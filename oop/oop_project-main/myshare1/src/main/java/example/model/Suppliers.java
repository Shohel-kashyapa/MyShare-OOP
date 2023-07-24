package example.model;

public class Suppliers {
    private int id;
    private String name;
    private String order_details;
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_details() {
        return order_details;
    }

    public void setEmail(String order_details) {
        this.order_details = order_details;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Suppliers(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.order_details = email;
        this.phone = phone;
    }



    // toString method to display supplier information
    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", order details " + order_details + ", price " + phone;
    }
}
