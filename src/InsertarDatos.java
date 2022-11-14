import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class InsertarDatos{
    final static String URL = "jdbc:postgresql://localhost:5432/airlines";
    final static String USERNAME = "postgres";
    final static String PASSWORD = "PjXCkbqlhB";

    final static String GREEN = "\u001B[32m";
    final static String RED = "\u001B[31m";
    final static String YELLOW = "\u001B[33m";
    final static String RESET = "\u001B[0m";

    public static void main(String[] args) {
        String sql = "INSERT INTO airport(airport_code,name,city,state) VALUES(?,?,?,?)";
        String unicidad_sql = "SELECT name FROM airport WHERE airport_code = ?";
        String codigo = "",nombre = "",ciudad = "",estado = "";
        Scanner sc = new Scanner(System.in);
        boolean check;

        System.out.println("Programa para insertar aeropuertos");
        Connection c = getConnection(URL, USERNAME, PASSWORD);

        do {
            System.out.print("\nIntroduzca el nombre del aeropuerto: ");
            nombre = sc.nextLine().replace("\n", "");
            check = (nombre.length() <= 255);

            if(!check){
                System.out.println(YELLOW+"Longitud de campo no soportada - Máximo 255 caracteres"+RESET);
            }

        } while (!check);

        do {
            System.out.print("\n\nIntroduzca la ciudad del aeropuerto: ");
            ciudad = sc.nextLine().replace("\n", "");
            check = (nombre.length() <= 255);

            if(!check){
                System.out.println(YELLOW+"Longitud de campo no soportada - Máximo 255 caracteres"+RESET);
            }

        } while (!check);
        do {
            System.out.print("\n\nIntroduzca el estado del aeropuerto: ");
            estado = sc.nextLine().replace("\n", "");
            check = (nombre.length() <= 255);

            if(!check){
                System.out.println(YELLOW+"Longitud de campo no soportada - Máximo 255 caracteres"+RESET);
            }

        } while (!check);

        do {
            System.out.print("\n\nIntroduzca el código del aeropuerto: ");
            codigo = sc.nextLine().replace("\n", "");
            check = (codigo.length() == 3);
            if(!check){
                System.out.println(YELLOW+"Longitud de campo no soportada"+RESET);
            }
            try {
                PreparedStatement unicidad = c.prepareStatement(unicidad_sql);
                unicidad.setString(1, codigo);
                // System.out.println(unicidad.toString());
                ResultSet iguales = unicidad.executeQuery();
                if(iguales.next() == true){
                    check = false;
                    System.out.println(YELLOW+"El codigo introducido ya esta registrado"+RESET);
                }
                iguales.close();
            } catch (SQLException e) {
                System.out.println(YELLOW+"Error de consulta"+RESET);
            }
        } while (!check);

        try{
            PreparedStatement st = c.prepareStatement(sql);
            st.setString(1, codigo);
            st.setString(2, nombre);
            st.setString(3, ciudad);
            st.setString(4, estado);

            st.execute();
            c.close();

            System.out.println(GREEN+"Datos agregados"+RESET);
        }catch(SQLException e){
            System.out.println(e.getMessage());
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
}
