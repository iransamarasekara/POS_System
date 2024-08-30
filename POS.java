import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class POS{
    static Map<String, GloceryItem> gloceryItemMap = new HashMap<>();
    //static GetItem getItem = new GetItem();
    public static void main(String[] args){
        String cashiersName, branchName;
        String customerName;
        InputStreamReader r;
        BufferedReader br;
        System.out.println("Enter cashiers name: ");
        try {
            r = new InputStreamReader(System.in);
            br = new BufferedReader(r);
            cashiersName = br.readLine();
            System.out.println("Enter branch name: ");
            branchName = br.readLine();
            // br.close(); 
            // r.close();
        } catch (Exception e) {
            System.out.println("Error reading input");
            return;
        }

        Bill bill1 = null;
        while(true){
            String isSaved;
            System.out.println("Do you want to get saved customer? (Y/N)");
            try {
                r = new InputStreamReader(System.in);
                br = new BufferedReader(r);
                isSaved = br.readLine();
                // br.close(); 
                // r.close();
            } catch (Exception e) {
                System.out.println("Error reading input");
                return;
            }
            if(isSaved.equals("Y")){
                try {
                    r = new InputStreamReader(System.in);
                    br = new BufferedReader(r);
                    customerName = br.readLine();
                    // br.close(); 
                    // r.close();
                } catch (Exception e) {
                    System.out.println("Error reading input");
                    return;
                }

                try{
                    FileInputStream fileStream = new FileInputStream("Bill.ser");
                    ObjectInputStream os = new ObjectInputStream(fileStream);
                    Object obj = os.readObject();
                    if (obj instanceof Bill) {
                        bill1 = (Bill) obj;
                    } else {
                        System.out.println("Invalid object found in the file. Creating a new bill.");
                        bill1 = new Bill();
                    }
                    os.close();
                }catch(Exception e){
                    e.printStackTrace();
                }                
            }else{
                
                System.out.println("Enter customer name: ");
                try {
                    r = new InputStreamReader(System.in);
                    br = new BufferedReader(r);
                    customerName = br.readLine();
                    // br.close(); 
                    // r.close();
                } catch (Exception e) {
                    System.out.println("Error reading input");
                    return;
                }
                bill1 = new Bill();
            }

            
            
            System.out.println("Now you can add items...");

            addItems(bill1);

            String isSave;
            System.out.println("Do want to save this bill?(Y/N)");
            try {
                r = new InputStreamReader(System.in);
                br = new BufferedReader(r);
                isSave = br.readLine();
                // br.close(); 
                // r.close();
            } catch (Exception e) {
                System.out.println("Error reading input");
                return;
            }
            if(isSave.equals("Y")){
                try{
                    FileOutputStream fileStream = new FileOutputStream("Bill.ser");
                    ObjectOutputStream os = new ObjectOutputStream(fileStream);
                    os.writeObject(bill1);
                    os.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            bill1.display(cashiersName, branchName, customerName, new ArrayList<GloceryItem>(), LocalDate.now(), LocalTime.now());
        }    
        
    }
    public static void addItems(Bill bill1){
        String isMore;
        InputStreamReader r;
        BufferedReader br;
        
        do{
            GloceryItem item = null;
            //GloceryItem item = GetItem.getItemDetails(bill1);
            while(item == null){
                item = GetItem.getItemDetails(bill1);
            }
            //prints current date 
            //LocalDate date = LocalDate.now(); // Create a date object
            //System.out.println(myObj); // Display the current date
            
            //prints current time
            LocalTime currentTime = LocalTime.now();
            // System.out.println("Current time is: " + currentTime);
            bill1.addToBill(item, item.getQuantity(), item.getDiscount(), currentTime);

            System.out.println("Do you want to add more items? (Y/N)");
            
            try {
                r = new InputStreamReader(System.in);
                br = new BufferedReader(r);
                isMore = br.readLine();
                // br.close(); 
                // r.close();
            } catch (Exception e) {
                System.out.println("Error reading input");
                return;
            }

        }while(isMore.equals("Y"));
    }

    
}

class GetItem{
    public static GloceryItem getItemDetails(Bill bill1){
        GloceryItem item = null;
        int item_quantity;
        try {
            String item_code;
            System.out.println("Enter item code: ");
            InputStreamReader r = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(r);
            item_code = br.readLine();
            
            
            try{
                if(item_code.equals("F001")){
                    item = new Apple();
                }else{
                    throw new ItemCodeNotFound("Item code not found. Please try again.");
                }
            }
            catch(ItemCodeNotFound e){
                System.out.println("re enter item code! "+e );
                return null;
            }
            System.out.println("Enter item quantity: ");
            item_quantity = Integer.parseInt(br.readLine());
            item.setQuantity(item_quantity);
            
            return item;
        } catch (IOException e) {
            System.out.println("Error reading input");
        }
        return item;
    }
}

class ItemCodeNotFound extends RuntimeException{
    //public ItemCodeNotFound(){GetItem.getItemDetails();}

    public ItemCodeNotFound(String message){
        
        super(message);
        //GetItem.getItemDetails();
    }
}

class GloceryItem implements Serializable{

    private double price;
    private String name;
    private double weight;
    private String size;
    private String expireDate;
    private String manufactureDate;
    private double discount;
    private int quantity;
    public GloceryItem(double price, String name, double weight, String size, String expireDate, String manufactureDate, double discount, int quantity){
        this.price = price;
        this.name = name;
        this.weight = weight;
        this.size = size;
        this.expireDate = expireDate;
        this.manufactureDate = manufactureDate;
        this.discount = discount;
        this.quantity = quantity;

    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }

    public double getDiscount() {
        return this.discount;
    }

    public void setDiscount(LocalTime time) {
        if(time.isAfter(LocalTime.of(10, 0)) && time.isBefore(LocalTime.of(12, 0))){
            this.discount = 0.5;
        }else{  
            this.discount = 0.2;
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }
}

class Apple extends GloceryItem{
    public Apple(){
        super(100.0, "Apple", 100, "Small", "2023-12-31", "2024-03-20", 0, 0);
    }
}


class Bill implements Serializable{

    double total = 0;
    double totalDiscount = 0;

    public void addToBill(GloceryItem item, int item_quantity, double discount, LocalTime currentTime){
        // Calculate the total bill
        item.setManufactureDate(null);
        
        item.setDiscount(currentTime);
        total += ((item.getPrice()- (item.getPrice()*item.getDiscount()) ) * item_quantity)  ;

        ArrayList<GloceryItem> itemList = new ArrayList<GloceryItem>();
        itemList.add(item);
          
        totalDiscount += item.getPrice()*item.getDiscount() *item_quantity;
    }

    public void display(String cashiersName, String branchName, String customerName, ArrayList<GloceryItem> itemList, LocalDate date, LocalTime time){
        //print cashiers name, branch name, customer name, item list, discount, total, date and time
 
        System.out.println("\t\t\t\t--------------------Invoice-----------------");  
        System.out.println("\t\t\t\t\t "+"  "+"BISON Grocery Shop");  
        System.out.println("\t\t\t\t\t3/A Ward Place, Colombo 07");  
        System.out.println("\t\t\t\t\t"  +"    " +"Opposite Metro Walk");  
        System.out.println("\t\t\t\t\t"  +"    " +date + " " + time);
        System.out.println("GSTIN: 03AWBPP8756K592"+"\t\t\t\t\t\t\tContact: (+94) 77 576 8994");  
        System.out.println("Cashier: "+cashiersName+"\t");
        for(GloceryItem item : itemList){
            System.out.println("Item: "+item.getName()+"\t"+ "Quantity: "+item.getQuantity()+"Unit_price" + item.getPrice()+"\t"+"Discount: "+item.getDiscount()+"\t"+"Net Price: "+(item.getPrice()- item.getPrice()*item.getDiscount() ) * item.getQuantity());
        }

        System.out.println("Total Discount: "+totalDiscount);
        System.out.println("Total: "+total);
    }
}
