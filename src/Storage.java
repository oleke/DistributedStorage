public class Storage {

    public static void putData(int idData, String data){
        System.out.println("Put Data "+ data);
    } 
    public static String getData(int idData){
        System.out.println("Get Data "+ idData);
        return "Data";
    }
}
