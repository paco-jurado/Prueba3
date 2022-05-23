package GestionBD;
/**
 *
 * @author FJJJ
 * Version instalada en Casa con Netbeans de prueba
 * Tercer cambio 
 */
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
/**
 *
 * @author FJJJ
 */
public class DBManager {
//hola
    // Conexión a la base de datos
    private static Connection conn = null;

    // Configuración de la conexión a la base de datos
    // private static String DB_url = "";
    public static String DB_driver = "jdbc:mysql://" ;
    public static String DB_host = "localhost";
    public static String DB_port = "3306";
    public static String DB_propiedades = "?serverTimezone=UTC";
        
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Laleche41$";
    private static String DB_name = "Tienda";
                                                                 // Configuración de la tabla Clientes
    private static String DB_Tabla = "clientes";
    private static String DB_Select = "SELECT * FROM " + DB_Tabla;
    private static final String DB_CLI_ID = "id";
    private static final String DB_CLI_NOM = "nombre";
    private static final String DB_CLI_DIR = "direccion";
    
    // Mensajes
    private static final String DB_MSQ_CONN_OK = "CONEXIÓN CORRECTA";
    private static final String DB_MSQ_CONN_NO = "ERROR EN LA CONEXIÓN";
      
    //////////////////////////////////////////////////
    // MÉTODOS DE CONEXIÓN A LA BASE DE DATOS
    //////////////////////////////////////////////////
      
 
    /**
     * Intenta conectar con la base de datos con los valores por defecto. La BBDD tienda y la tabla cliente ...el punto de partida.
     *
     * @return true si pudo conectarse, false en caso contrario
     */
    public static boolean connect() {
      String db_url;
      
      db_url= DB_driver + DB_host + ":" + DB_port + "/" + DB_name + DB_propiedades; 
      try {
            System.out.print("Conectando a la base de datos...");
            conn = DriverManager.getConnection(db_url, DB_USER, DB_PASS);
            System.out.println("OK!");
            return true;
        } catch (SQLException ex) {
            System.out.println(DB_MSQ_CONN_NO);
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean connect(String db_driver, String db_host, String db_port, String db_name, String db_propiedades) {
      String db_url;
      
      db_driver         = (db_driver      =="")?DB_driver:db_driver; 
      db_host           = (db_host        =="")?DB_host:db_host; 
      db_port           = (db_port        =="")?DB_port:db_port; 
      db_name           = (db_name        =="")?DB_name:db_name; 
      db_propiedades    = (db_propiedades =="")?DB_propiedades:db_propiedades; 
      
      db_url= db_driver + db_host + ":" + db_port + "/" + db_name + db_propiedades;
      try {
            System.out.print("Conectando a la base de datos...");
            conn = DriverManager.getConnection(db_url, DB_USER, DB_PASS);
            System.out.println("OK!");
            return true;
        } catch (SQLException ex) {
            System.out.println(DB_MSQ_CONN_NO);
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Comprueba la conexión y muestra su estado por pantalla
     *
     * @return true si la conexión existe y es vÃ¡lida, false en caso contrario
     */
    public static boolean isConnected() {
        // Comprobamos estado de la conexión
        try {
            if (conn != null && conn.isValid(0)) {
                System.out.println(DB_MSQ_CONN_OK);
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(DB_MSQ_CONN_NO);
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Cierra la conexión con la base de datos
     */
    public static void close() {
        try {
            System.out.print("Cerrando la conexión...");
            conn.close();
            System.out.println("OK!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // MÉTODOS DE TABLA CLIENTES
    //////////////////////////////////////////////////
    ;
    
    // Devuelve 
    // Los argumentos indican el tipo de ResultSet deseado
    /**
     * Obtiene toda la tabla clientes de la base de datos
     * @param resultSetType Tipo de ResultSet
     * @param resultSetConcurrency Concurrencia del ResultSet
     * @return ResultSet (del tipo indicado) con la tabla, null en caso de error
     */
    public static ResultSet getTablaClientes(int resultSetType, int resultSetConcurrency) {
        try {
            Statement stmt = conn.createStatement(resultSetType, resultSetConcurrency);
            ResultSet rs = stmt.executeQuery(DB_Select);
            //stmt.close();
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * Obtiene toda la tabla clientes de la base de datos
     *
     * @return ResultSet (por defecto) con la tabla, null en caso de error
     */
    public static ResultSet getTablaClientes() {
        return getTablaClientes(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * Imprime por pantalla el contenido de la tabla clientes
     */
    public static void printTablaClientes() {
        try {
            ResultSet rs = getTablaClientes(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            while (rs.next()) {
                int id = rs.getInt(DB_CLI_ID);
                String n = rs.getString(DB_CLI_NOM);
                String d = rs.getString(DB_CLI_DIR);
                System.out.println(id + "\t" + n + "\t" + d);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // MÉTODOS DE UN SOLO CLIENTE
    //////////////////////////////////////////////////
    ;
    
    /**
     * Solicita a la BD el cliente con id indicado
     * @param id id del cliente
     * @return ResultSet con el resultado de la consulta, null en caso de error
     */
    public static ResultSet getCliente(int id) {
        try {
            // Realizamos la consulta SQL
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = DB_Select + " WHERE " + DB_CLI_ID + "='" + id + "';";
            //System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            //stmt.close();
            
            // Si no hay primer registro entonces no existe el cliente
            if (!rs.first()) {
                return null;
            }

            // Todo bien, devolvemos el cliente
            return rs;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Comprueba si en la BD existe el cliente con id indicado
     *
     * @param id id del cliente
     * @return verdadero si existe, false en caso contrario
     */
    public static boolean existsCliente(int id) {
        try {
            // Obtenemos el cliente
            ResultSet rs = getCliente(id);

            // Si rs es null, se ha producido un error
            if (rs == null) {
                return false;
            }

            // Si no existe primer registro
            if (!rs.first()) {
                rs.close();
                return false;
            }

            // Todo bien, existe el cliente
            rs.close();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Imprime los datos del cliente con id indicado
     *
     * @param id id del cliente
     */
    public static void printCliente(int id) {
        try {
            // Obtenemos el cliente
            ResultSet rs = getCliente(id);
            if (rs == null || !rs.first()) {
                System.out.println("Cliente " + id + " NO EXISTE");
                return;
            }
            
            // Imprimimos su información por pantalla
            int cid = rs.getInt(DB_CLI_ID);
            String nombre = rs.getString(DB_CLI_NOM);
            String direccion = rs.getString(DB_CLI_DIR);
            System.out.println("Cliente " + cid + "\t" + nombre + "\t" + direccion);

        } catch (SQLException ex) {
            System.out.println("Error al solicitar cliente " + id);
            ex.printStackTrace();
        }
    }

    /**
     * Solicita a la BD insertar un nuevo registro cliente
     *
     * @param nombre nombre del cliente
     * @param direccion dirección del cliente
     * @return verdadero si pudo insertarlo, false en caso contrario
     */
    public static boolean insertCliente(String nombre, String direccion) {
        try {
            // Obtenemos la tabla clientes
            System.out.print("Insertando cliente " + nombre + "...");
            ResultSet rs = getTablaClientes(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            // Insertamos el nuevo registro
            rs.moveToInsertRow();
            rs.updateString(DB_CLI_NOM, nombre);
            rs.updateString(DB_CLI_DIR, direccion);
            rs.insertRow();

            // Todo bien, cerramos ResultSet y devolvemos true
            rs.close();
            System.out.println("OK!");
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Solicita a la BD modificar los datos de un cliente
     *
     * @param id id del cliente a modificar
     * @param nombre nuevo nombre del cliente
     * @param direccion nueva dirección del cliente
     * @return verdadero si pudo modificarlo, false en caso contrario
     */
    public static boolean updateCliente(int id, String nuevoNombre, String nuevaDireccion) {
        try {
            // Obtenemos el cliente
            System.out.print("Actualizando cliente " + id + "... ");
            ResultSet rs = getCliente(id);

            // Si no existe el Resultset
            if (rs == null) {
                System.out.println("Error. ResultSet null.");
                return false;
            }

            // Si tiene un primer registro, lo eliminamos
            if (rs.first()) {
                rs.updateString(DB_CLI_NOM, nuevoNombre);
                rs.updateString(DB_CLI_DIR, nuevaDireccion);
                rs.updateRow();
                rs.close();
                System.out.println("OK!");
                return true;
            } else {
                System.out.println("ERROR. ResultSet vacÃ­o.");
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Solicita a la BD eliminar un cliente
     *
     * @param id id del cliente a eliminar
     * @return verdadero si pudo eliminarlo, false en caso contrario
     */
    public static boolean deleteCliente(int id) {
        try {
            System.out.print("Eliminando cliente " + id + "... ");

            // Obtenemos el cliente
            ResultSet rs = getCliente(id);

            // Si no existe el Resultset
            if (rs == null) {
                System.out.println("ERROR. ResultSet null.");
                return false;
            }

            // Si existe y tiene primer registro, lo eliminamos
            if (rs.first()) {
                rs.deleteRow();
                rs.close();
                System.out.println("OK!");
                return true;
            } else {
                System.out.println("ERROR. ResultSet vacÃ­o.");
                return false;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}