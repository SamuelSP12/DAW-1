import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Clientes {
	
	static Connection conexi=Principal.conexion();
	
	public static void altaCliente(Connection con, String dni, String nombre, String tlf, String direc) throws ExcepcionesBanco, SQLException{
		String dniCheck, nombreCheck, tlfCheck, direcCheck, consulta="", consulta2, consulta3;
		
		//comprobamos datos validos
		if(dni!="" && dni!=" " && dni.length()==9) {
			dniCheck=dni;
		}else {
			throw new ExcepcionesBanco("DNI Demasiado corto o incorrecto");
		}
		if(nombre!="" && nombre!=" ") {
			nombreCheck=nombre;
		}else {
			throw new ExcepcionesBanco("NOMBRE Vacío o incorrecto");
		}
		if(tlf!="" && tlf!=" ") {
			tlfCheck=tlf;
		}else {
			throw new ExcepcionesBanco("TLF Vacío o incorrecto");
		}
		if(direc!="" && direc!=" ") {
			direcCheck=direc;
		}else {
			throw new ExcepcionesBanco("DIRECCIÓN Vacía o incorrecto");
		}
		
		//comprobamos si existe un cliente
		if(!existeCliente(dniCheck)) {
			
			consulta="insert into clientes values(?,?,?,?, true);";
			
			PreparedStatement sentencia=con.prepareStatement(consulta);
			sentencia.setString(1, dniCheck);
			sentencia.setString(2, nombreCheck);
			sentencia.setString(3, tlfCheck);
			sentencia.setString(4, direcCheck);
			sentencia.executeUpdate();
			
			System.out.println("CHECK Se ha creado y dado de alta el cliente con DNI: "+dniCheck);
		}else {
			//comprobamos el estado actual del cliente
			
			if(estadoCliente(dniCheck)) {
				//como estadoCliente=true esta activo
				System.err.println("ERROR Este cliente con DNI "+dniCheck+" ya esta dado de alta");
			}else {
				//ya esta dado de baja
				consulta3="update clientes set estado = true where dni= ? ;";
				
				PreparedStatement sentencia3=con.prepareStatement(consulta3);
				sentencia3.setString(1, dniCheck);
				sentencia3.executeUpdate();
				
				System.out.println("CHECK El cliente ya existia y se ha dado de alta el DNI: "+dniCheck);
			}
		}
		
	}
	
	public static void bajaCliente(Connection con, String dni) throws ExcepcionesBanco, SQLException{
		String dniCheck, consulta, consulta2;
		
		//comprobamos que el dni sea valido
		if(dni!="" && dni!=" " && dni.length()==9) {
			dniCheck=dni;
		}else {
			throw new ExcepcionesBanco("DNI Demasiado corto o incorrecto");
		}
		
		if(!existeCliente(dniCheck)) {
			
			System.err.println("ERROR No existe un cliente de alta con ese DNI");
			
		}else {
			//comprobamos el estado actual del cliente
			
			if(estadoCliente(dniCheck)) {
				//como estadoCliente=true esta activo
				consulta2="update clientes set estado = false where dni= ? ;";
				
				PreparedStatement sentencia2=con.prepareStatement(consulta2);
				sentencia2.setString(1, dniCheck);
				sentencia2.executeUpdate();
				sentencia2.close();
				
				System.out.println("CHECK El cliente con DNI: "+dniCheck+" se ha dado de Baja");
			}else {
				//ya esta dado de baja
				System.err.println("ERROR Este cliente ya esta dado de baja");
			}
			
		}
		
	}
	
	
	public static void actuDatosCliente(Connection con, String dni) throws ExcepcionesBanco, SQLException{
		String dniCheck, nombreCheck, tlfCheck, direcCheck, consulta="", consulta2;
		
		//comprobamos que el dni sea valido
		if(dni!="" && dni!=" " && dni.length()==9) {
			dniCheck=dni;
		}else {
			throw new ExcepcionesBanco("DNI Demasiado corto o incorrecto");
		}
		
		//comprobamos si existe un cliente con ese dni
		if(!existeCliente(dniCheck)) {
			
			System.out.println("ERROR No existe el cliente con DNI: "+dniCheck);
		}else {
			//comprobamos el estado actual del cliente
			
			if(estadoCliente(dniCheck)) {
				//si esta dado de alta podra modificar datos
				Scanner ent1 = new Scanner(System.in);//para int
				Scanner ent2 = new Scanner(System.in);//para string
				int menu=0;
				
				do {
					System.out.println("*MENÚ* Elige una opción\n"
							+ "1 Actu NOMBRE | 2 Actu TLF | 3 Actu DIREC | 4 CERRAR Actualizar");
					//Dentro de movimientos>listar los movimientos entre fechas, ver saldo, ingresar y retirar dinero, hacer transferencias
					menu=ent1.nextInt();
					
					switch (menu) {
					
					case 1: {
						String nombre;
						System.out.println("Nombre completo nuevo:");
						nombre=ent2.nextLine();
						
						if(nombre!="" && nombre!=" ") {
							nombreCheck=nombre;
							
							consulta="update clientes set nombre = ? where dni = ? ;";
							
							PreparedStatement sentencia=con.prepareStatement(consulta);
							sentencia.setString(1, nombreCheck);
							sentencia.setString(2, dniCheck);
							sentencia.executeUpdate();
							sentencia.close();
							
							System.out.println("CHECK El cliente con DNI: "+dniCheck+" ha actualizado el nombre a "+nombreCheck);
							
						}else {
							throw new ExcepcionesBanco("NOMBRE Vacío o incorrecto");
						}
						break;
					}
					case 2: {
						String tlf;
						System.out.println("TLF nuevo:");
						tlf=ent2.nextLine();
						
						if(tlf!="" && tlf!=" ") {
							tlfCheck=tlf;
							
							consulta="update clientes set tlf = ? where dni= ? ;";
							
							PreparedStatement sentencia=con.prepareStatement(consulta);
							sentencia.setString(1, tlfCheck);
							sentencia.setString(2, dniCheck);
							sentencia.executeUpdate();
							sentencia.close();
							
							System.out.println("CHECK El cliente con DNI: "+dniCheck+" ha actualizado el TLF a "+tlfCheck);
							
						}else {
							throw new ExcepcionesBanco("TLF Vacío o incorrecto");
						}
						break;
					}
					case 3: {
						String direc;
						System.out.println("Direccion nueva:");
						direc=ent2.nextLine();
						
						if(direc!="" && direc!=" ") {
							direcCheck=direc;
							
							consulta="update clientes set direc = ? where dni= ? ;";
							
							PreparedStatement sentencia=con.prepareStatement(consulta);
							sentencia.setString(1, direcCheck);
							sentencia.setString(2, dniCheck);
							sentencia.executeUpdate();
							sentencia.close();
							
							System.out.println("CHECK El cliente con DNI: "+dniCheck+" ha actualizado la Direccion a "+direcCheck);
							
						}else {
							throw new ExcepcionesBanco("DIRECCIÓN Vacío o incorrecto");
						}
						break;
					}
					case 4: {
						
						System.out.println("Cerrando menú modificaciones...");
						
						break;
					}
					default: {
						System.out.println("La opcion ``"+menu+"`` no es admitida. Introduzca una opción valida");
						break; 
					}
				}
					
				}while (menu!=4);
				
			}else {
				//debe estar dado de alta para modificiar datos
				System.err.println("ERROR Este cliente con DNI "+dniCheck+" debe estar dado de alta para ser modificado");
			}
		}
		
	}
	
	
	public static boolean existeCliente(String dni) {
		//si hay un dni existente en la base de datos entonces return true
		
		//buscamos el dni en la base de datos
		boolean interruptor=false;
		
		try {
			ArrayList<String> dnis=new ArrayList<String>();
			ResultSet rs= CuentaBancaria.getTabla("clientes");
			
			while(rs.next()) {
				String dnitabla=rs.getString("dni");
				dnis.add(dnitabla);
			}
			for (int i=0;i<dnis.size();i++) {
				if(dni.equals(dnis.get(i))) {
					interruptor=true;
				}
			}
			rs.close();
			//interruptor=false;
			
		}catch (SQLException ex) {
			ex.printStackTrace();
			interruptor=false;
		}catch(Exception ex) {
			ex.printStackTrace();
			interruptor=false;
		}
		
		return interruptor;
		
	}
	
	public static boolean estadoCliente(String dni) throws SQLException {
		//comprobamos el estado actual del cliente
		boolean estadoCliente=false;
		String consulta2;
		
		consulta2="select estado from clientes where dni= ? ;";
		
		PreparedStatement sentencia2=conexi.prepareStatement(consulta2);
		sentencia2.setString(1, dni);
		ResultSet rs2 = sentencia2.executeQuery();
		rs2.next();
		estadoCliente=rs2.getBoolean("estado");
		
		return estadoCliente;
	}
}
