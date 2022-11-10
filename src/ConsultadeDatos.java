import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

public class ConsultadeDatos {
    final static String URL = "jdbc:postgresql://localhost:5432/airlines";
    final static String USERNAME = "postgres";
    final static String PASSWORD = "PjXCkbqlhB";

    final static String GREEN = "\u001B[32m";
    final static String RED = "\u001B[31m";
    final static String YELLOW = "\u001B[33m";
    final static String RESET = "\u001B[0m";


    public static void main(String[] args) {
        System.out.println("Programa para consultar horarios de vuelo en una ciudad");
        Scanner sc = new Scanner(System.in);
        boolean check;
        Connection c = getConnection(URL, USERNAME, PASSWORD);

        //Mostrar las ciudades
        String sql_cities = "SELECT city, airport_code FROM airport";
        ResultSet cities = getResultSet(sql_cities, c);

        try {   //Obtener el código para la ciudad a consultar
            int option, counter;
            do {
                counter = 1;
                System.out.println("Elija la ciudad a consultar: ");
                while(cities.next()){
                    System.out.println("\t"+(counter++)+") "+cities.getString(1).replaceAll("\n", " "));
                }
                System.out.print("\nOPCION:>\t");
                option = sc.nextInt();
                check = (option >= 1 && option < counter);
                if(!check){
                    System.out.println("\n\n"+YELLOW+"Opcion ingresada no valida"+RESET);
                    cities.beforeFirst();
                }
            } while (!check);
            //Seleccionar la ciudad
            cities.absolute(option);
            //Obtener el código
            String codigo = cities.getString(2);
            cities.close();

            PreparedStatement st = c.prepareStatement("SELECT arr_apc,date,sch_dep_time,sch_arr_time FROM leg_instance WHERE dep_apc = ?");
            st.setString(1, codigo);

            ResultSet vuelos = st.executeQuery();

            int vuelo = 1;
            while(vuelos.next()){
                System.out.println("\t"+(vuelo++)+") Hacia "+vuelos.getString(1)+". Día: "+vuelos.getString(2)+" - "+vuelos.getDate(3)+" => "+vuelos.getDate(4));
            }
            vuelos.close();
            c.close();
            System.out.println(GREEN+"Sesion completada"+RESET);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(RED+"Hubo un error con los resultados"+RESET);
        }

    }
    public static Connection getConnection(String url, String username, String password){
        try{    //Crear la conexión a la base de datos
            Connection c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println(GREEN+"Conexion a la base de datos exitosa"+RESET);
            return c;
        }catch(SQLException e){
            System.out.println(RED+"No se pudo conectar a la base de datos"+RESET);
            System.out.println(e.getMessage());
            System.exit(0);
            return null;
        }
    }
    public static ResultSet getResultSet(String sql, Connection c){
        try{
            Statement st = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            return st.executeQuery(sql);
        }catch(SQLException e){
            System.out.println(RED+"No se pudo procesar la consulta SQL"+RESET);
            System.out.println(e.getMessage());
            return null;
        }

    }
}
