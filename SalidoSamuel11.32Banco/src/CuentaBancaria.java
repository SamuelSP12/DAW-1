import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class CuentaBancaria {

	static Connection conexi=Principal.conexion();
	
	
	public static void altaCuentaBancaria(Connection con, String dni) throws ExcepcionesBanco, SQLException {
		
		String dniCheck, consulta, consulta2, consulta3;
		
		//comprobamos datos validos
		if(dni!="" && dni!=" " && dni.length()==9) {
			dniCheck=dni;
		}else {
			throw new ExcepcionesBanco("DNI Demasiado corto o incorrecto");
		}
		
		if(Clientes.existeCliente(dniCheck)) {
			
			if(Clientes.estadoCliente(dniCheck)) {
				
				if(!existeCuenta(dniCheck)) {
					
					double saldoIni=0, saldoIniCheck=0;
					Scanner ent1 = new Scanner(System.in);//para double
					System.out.println("Introduce el Saldo inicial de la cuenta:");
					saldoIni=ent1.nextDouble();
					
					if(saldoIni>0) {
						saldoIniCheck=saldoIni;
					}else {
						throw new ExcepcionesBanco("SALDO no puede ser negativo");
					}
					
					consulta="insert into cuentas(dni, saldo, estado) values(?,?, true);";
					
					PreparedStatement sentencia=con.prepareStatement(consulta);
					sentencia.setString(1, dniCheck);
					sentencia.setDouble(2, saldoIniCheck);
					sentencia.executeUpdate();
					sentencia.close();
					
					System.out.println("CHECK Se ha creado y dado de alta la cuenta del ciente con DNI: "+dniCheck);
				}else {
					//comprobamos el estado actual de la cuenta
					
					if(estadoCuenta(dniCheck)) {
						//como estadoCuenta=true esta activo
						System.err.println("ERROR Esta cuenta del DNI "+dniCheck+" ya esta dada de alta");
					}else {
						//ya esta dado de baja
						consulta3="update cuentas set estado = true where dni= ? ;";
						
						PreparedStatement sentencia3=con.prepareStatement(consulta3);
						sentencia3.setString(1, dniCheck);
						sentencia3.executeUpdate();
						
						System.out.println("CHECK La cuenta ya existia y se ha dado de alta para el DNI: "+dniCheck);
					}
				}
				
			}else {
				//no esta dado de alta y no se puede hacer
				System.err.println("ERROR El cliente con dni: "+dniCheck+" no esta dado de alta y no puedes crear la cuenta");
			}
			
		}else {
			//no existe un cliente con ese dni
			System.err.println("ERROR No existe ningun cliente con dni: "+dniCheck+" y no puedes crear la cuenta");
		}
		
		
	}
	
	
	public static void bajaCuentaBancaria(Connection con, String dni) throws ExcepcionesBanco, SQLException {
		String dniCheck, consulta, consulta2, consulta3;
		
		//comprobamos datos validos
		if(dni!="" && dni!=" " && dni.length()==9) {
			dniCheck=dni;
		}else {
			throw new ExcepcionesBanco("DNI Demasiado corto o incorrecto");
		}
		
		if(Clientes.existeCliente(dniCheck)) {
			
			if(Clientes.estadoCliente(dniCheck)) {
				
				if(!existeCuenta(dniCheck)) {
					
					System.err.println("ERROR No existe una cuenta para ese DNI");
				}else {
					//comprobamos el estado actual de la cuenta
					
					if(!estadoCuenta(dniCheck)) {
						//como estadoCuenta=false esta de baja
						System.err.println("ERROR Esta cuenta del DNI "+dniCheck+" ya esta dada de baja");
					}else {
						//cuenta dada de alta
						consulta3="update cuentas set estado = false where dni= ? ;";
						
						PreparedStatement sentencia3=con.prepareStatement(consulta3);
						sentencia3.setString(1, dniCheck);
						sentencia3.executeUpdate();
						
						System.out.println("CHECK La cuenta ya existia y se ha dado de baja para el DNI: "+dniCheck);
					}
				}
				
			}else {
				//no esta dado de alta y no se puede hacer
				System.err.println("ERROR El cliente con dni: "+dniCheck+" no esta dado de alta y no puedes crear la cuenta");
			}
			
		}else {
			//no existe un cliente con ese dni
			System.err.println("ERROR No existe ningun cliente con dni: "+dniCheck+" y no puedes crear la cuenta");
		}
	}
	
	public static boolean seleccionarCuenta(String dni) throws ExcepcionesBanco, SQLException {
		boolean estadoDniEnUso=false;
		String dniCheck;
		
		if(dni!="" && dni!=" " && dni.length()==9) {
			dniCheck=dni;
		}else {
			throw new ExcepcionesBanco("DNI Demasiado corto o incorrecto");
		}
		
		if(Clientes.existeCliente(dniCheck)) {
			
			if(Clientes.estadoCliente(dniCheck)) {
				
				if(!existeCuenta(dniCheck)) {
					
					System.err.println("ERROR Para el cliente con dni: "+dniCheck+" no existe una cuenta aun");
					
				}else {
					if(!estadoCuenta(dniCheck)) {
						System.err.println("ERROR La cuenta del cliente con dni: "+dniCheck+" no esta dada de alta y no puedes seleccionar la cuenta");
						estadoDniEnUso=false;
					}else {
						estadoDniEnUso=true;
						System.out.println("CHECK La cuenta para el DNI: "+dniCheck+" ha sido seleccionada");
					}
				}
			}else {
				System.err.println("ERROR El cliente con dni: "+dniCheck+" no esta dado de alta y no puedes seleccionar la cuenta");
				estadoDniEnUso=false;
			}
		}else {
			System.err.println("ERROR No existe ningun cliente con dni: "+dniCheck+" y no puedes seleccionar la cuenta");
			estadoDniEnUso=false;
		}
				
				
			
		
		
		return estadoDniEnUso;
	}
	
	
	public static boolean existeCuenta(String dni) {
		//si hay un dni existente en la base de datos entonces return true
		
		//buscamos el dni en la base de datos
		boolean interruptor=false;
		
		try {
			ArrayList<String> dnis=new ArrayList<String>();
			ResultSet rs= getTabla("cuentas");
			
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
	
	public static boolean estadoCuenta(String dni) throws SQLException {
		//comprobamos el estado actual del cliente
		boolean estadoCuenta=false;
		String consulta2;
		
		consulta2="select estado from cuentas where dni= ? ;";
		
		PreparedStatement sentencia2=conexi.prepareStatement(consulta2);
		sentencia2.setString(1, dni);
		ResultSet rs2 = sentencia2.executeQuery();
		rs2.next();
		estadoCuenta=rs2.getBoolean("estado");
		
		return estadoCuenta;
	}
	
	public static ResultSet getTabla(String tabla) {
		try {
			String consulta="select * from "+tabla+";";
			PreparedStatement sentencia=conexi.prepareStatement(consulta, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet result=sentencia.executeQuery();
			return result;
			
		}catch (SQLException ex){
			return null;
		}
	}
}
