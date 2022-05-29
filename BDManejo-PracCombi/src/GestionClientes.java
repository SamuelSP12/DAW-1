
import java.util.Scanner;

/**
 *
 * @author lionel
 */
public class GestionClientes {

    public static void main(String[] args) {

        DBManager.loadDriver();
        DBManager.connect();

        boolean salir = false;
        do {
            salir = menuPrincipal();
        } while (!salir);

        DBManager.close();

    }

    public static boolean menuPrincipal() {
        System.out.println("");
        System.out.println("MENU PRINCIPAL");
        System.out.println("1. Listar clientes");
        System.out.println("2. Nuevo cliente");
        System.out.println("3. Modificar cliente");
        System.out.println("4. Eliminar cliente");
        System.out.println("5. Nuevo cliente V2");
        System.out.println("6. Nueva tabla");
        System.out.println("7. Filtrar en clientes");
        System.out.println("8. Crear archivo lista clientes");
        System.out.println("9. Insertar mediante archivo");
        System.out.println("10. Actualizar mediante archivo");
        System.out.println("11. Borrar mediante archivo");
        System.out.println("12. Salir");
        
        Scanner in = new Scanner(System.in);
            
        int opcion = pideInt("Elige una opción: ");
        
        switch (opcion) {
            case 1:
                opcionMostrarClientes();
                return false;
            case 2:
                opcionNuevoCliente();
                return false;
            case 3:
                opcionModificarCliente();
                return false;
            case 4:
                opcionEliminarCliente();
                return false;
            case 5:
            	//Uso procedimiento almacenado pedido
            	opcionNewClienteProcAlma();
                return false;
            case 6:
            	//Crear nueva tabla
            	opcionNewTabla();
                return false;
            case 7:
            	//Metodo filtrar en tabla clientes
            	opcionFiltrarPor();
                return false;
            case 8:
            	//Metodo crear archivo lista clientes
            	opcionArchivoListaClientes();
                return false;
            case 9:
            	//Metodo insertar en tabla mediante archivo
            	opcionInsertPorFichero();
                return false;
            case 10:
            	//Metodo actualizar en tabla mediante archivo
            	opcionActuPorFichero();
                return false;
            case 11:
            	//Metodo borrar en tabla mediante archivo
            	opcionBorrarPorFichero();
                return false;
            case 12:
                return true;
            default:
                System.out.println("Opción elegida incorrecta");
                return false;
        }
        
    }
    
    public static int pideInt(String mensaje){
        
        while(true) {
            try {
                System.out.print(mensaje);
                Scanner in = new Scanner(System.in);
                int valor = in.nextInt();
                //in.nextLine();
                return valor;
            } catch (Exception e) {
                System.out.println("No has introducido un número entero. Vuelve a intentarlo.");
            }
        }
    }
    
    public static String pideLinea(String mensaje){
        
        while(true) {
            try {
                System.out.print(mensaje);
                Scanner in = new Scanner(System.in);
                String linea = in.nextLine();
                return linea;
            } catch (Exception e) {
                System.out.println("No has introducido una cadena de texto. Vuelve a intentarlo.");
            }
        }
    }

    public static void opcionMostrarClientes() {
        System.out.println("Listado de Clientes:");
        DBManager.printTablaClientes();
    }

    public static void opcionNuevoCliente() {
        Scanner in = new Scanner(System.in);

        System.out.println("Introduce los datos del nuevo cliente:");
        String nombre = pideLinea("Nombre: ");
        String direccion = pideLinea("Dirección: ");

        boolean res = DBManager.insertCliente(nombre, direccion);

        if (res) {
            System.out.println("Cliente registrado correctamente");
        } else {
            System.out.println("Error :(");
        }
    }

    public static void opcionModificarCliente() {
        Scanner in = new Scanner(System.in);

        int id = pideInt("Indica el id del cliente a modificar: ");

        // Comprobamos si existe el cliente
        if (!DBManager.existsCliente(id)) {
            System.out.println("El cliente " + id + " no existe.");
            return;
        }

        // Mostramos datos del cliente a modificar
        DBManager.printCliente(id);

        // Solicitamos los nuevos datos
        String nombre = pideLinea("Nuevo nombre: ");
        String direccion = pideLinea("Nueva dirección: ");

        // Registramos los cambios
        boolean res = DBManager.updateCliente(id, nombre, direccion);

        if (res) {
            System.out.println("Cliente modificado correctamente");
        } else {
            System.out.println("Error :(");
        }
    }

    public static void opcionEliminarCliente() {
        Scanner in = new Scanner(System.in);

        int id = pideInt("Indica el id del cliente a eliminar: ");

        // Comprobamos si existe el cliente
        if (!DBManager.existsCliente(id)) {
            System.out.println("El cliente " + id + " no existe.");
            return;
        }

        // Eliminamos el cliente
        boolean res = DBManager.deleteCliente(id);

        if (res) {
            System.out.println("Cliente eliminado correctamente");
        } else {
            System.out.println("Error :(");
        }
    }
    
    //////////////////////////////////////////////////
    // MÉTODOS NUEVOS PEDIDOS | Samuel
    //////////////////////////////////////////////////
    
    public static void opcionNewClienteProcAlma() {
    	Scanner in = new Scanner(System.in);

        System.out.println("Introduce los datos del nuevo cliente:");
        String nombre = pideLinea("Nombre: ");
        String direccion = pideLinea("Dirección: ");

        boolean res = DBManager.newClienteProcAlma(nombre, direccion);

        if (res) {
            System.out.println("Cliente registrado correctamente");
        } else {
            System.out.println("Error :(");
        }
    }
    
    public static void opcionNewTabla() {
    	Scanner in = new Scanner(System.in);

        System.out.println("Introduce los datos de la tabla:");
        String nombre = pideLinea("Nombre tabla: ");
        String fila1 = pideLinea("Nombre campo1: ");
        String tipoFila1 = pideLinea("Tipo campo1 (Ej: varchar(50) ): ");
        String fila2 = pideLinea("Nombre campo2: ");
        String tipoFila2 = pideLinea("Tipo campo2 (Ej: int ): ");

        boolean res = DBManager.newTabla(nombre, fila1, tipoFila1, fila2, tipoFila2);

        if (res) {
            System.out.println("Tabla creada correctamente");
        } else {
            System.out.println("Error :(");
        }
    }
    
    public static void opcionFiltrarPor() {
    	Scanner in = new Scanner(System.in);

        System.out.println("Introduce los datos para filtrar:");
        String campo = pideLinea("Nombre campo (id ó nombre ó direccion): ");
        String filtro = pideLinea("Valor para filtrar (Ej: 1 ó Luis ó Valencia ): ");

        boolean res = DBManager.filtrarPor(campo,filtro);

        if (res) {
            System.out.println("Lista obtenida correctamente");
        } else {
            System.out.println("Error :(");
        }
    }
    
    public static void opcionArchivoListaClientes() {
        System.out.println("Creando archivo con listado de clientes...");
        DBManager.escribirListadoClientes();
        System.out.println("Archivo ListaClientes.txt creado");
    }
    
    public static void opcionInsertPorFichero() {
    	Scanner in = new Scanner(System.in);

        System.out.println("Introduce los datos del fichero:");
        String ruta = pideLinea("Ruta del fichjero (Ej: ficheroPrueba.txt ): ");

        boolean res = DBManager.insertarDatosFichero(ruta);

        if (res) {
            System.out.println("Datos insertados correctamente");
        } else {
            System.out.println("Error :(");
        }
    }
    
    public static void opcionActuPorFichero() {
    	Scanner in = new Scanner(System.in);

        System.out.println("Introduce los datos del fichero:");
        String ruta = pideLinea("Ruta del fichjero (Ej: ficheroPrueba.txt ): ");

        boolean res = DBManager.actualizarDatosFichero(ruta);

        if (res) {
            System.out.println("Datos actualizados correctamente");
        } else {
            System.out.println("Error :(");
        }
    }
    
    public static void opcionBorrarPorFichero() {
    	Scanner in = new Scanner(System.in);

        System.out.println("Introduce los datos del fichero:");
        String ruta = pideLinea("Ruta del fichjero (Ej: ficheroPrueba.txt ): ");

        boolean res = DBManager.borrarDatosFichero(ruta);

        if (res) {
            System.out.println("Datos borrados correctamente");
        } else {
            System.out.println("Error :(");
        }
    }
}
