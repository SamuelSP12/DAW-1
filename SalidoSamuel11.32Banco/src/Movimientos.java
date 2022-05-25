import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimientos {

	public static void getSaldoCuenta(Connection con, String dni) throws SQLException {
		//como ya se habria comprobado que el dni y la cuenta es correcta no harian falta las anteriores verificaciones
		
		String consulta2;
		
		consulta2="select saldo from cuentas where dni= ? ;";
		
		PreparedStatement sentencia2=con.prepareStatement(consulta2);
		sentencia2.setString(1, dni);
		ResultSet rs2 = sentencia2.executeQuery();
		rs2.next();
		
		System.out.println(" * | Saldo actual: "+rs2.getString("saldo"));
	}
	
	public static void ingresoDinero(Connection con, String dni, double dinero) throws SQLException, ExcepcionesBanco {
		String consulta, consulta2, consulta3;
		
		if(dinero>0) {
			//actualizamos saldo en tabla cuentas
			consulta2="update cuentas set saldo = saldo+? where dni= ? ;";
			PreparedStatement sentencia2=con.prepareStatement(consulta2);
			sentencia2.setDouble(1, dinero);
			sentencia2.setString(2, dni);
			sentencia2.executeUpdate();
			
			//seleccionamos el id de la cuenta
			consulta3="select id from cuentas where dni= ? ;";
			
			PreparedStatement sentencia3=con.prepareStatement(consulta3);
			sentencia3.setString(1, dni);
			ResultSet rs3 = sentencia3.executeQuery();
			rs3.next();
			int idCuenta=rs3.getInt("id");
			
			//conseguimos la fecha con su formato
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			//actualizamos tabla movimientos
			consulta="insert into movimientos(idcuenta, fecha, tipo, dinero) values(?,?,?,?);";
			
			PreparedStatement sentencia=con.prepareStatement(consulta);
			sentencia.setInt(1, idCuenta);
			sentencia.setString(2, dtf.format(LocalDateTime.now()));
			sentencia.setString(3, "Ingreso | +");
			sentencia.setDouble(4, dinero);
			sentencia.executeUpdate();
			
			System.out.println(" * | Ingreso de "+dinero+"€ realizado con exito a la cuenta con DNI: "+dni);
		}else {
			throw new ExcepcionesBanco("ERROR Cantidad de dinero incorrecta");
		}
		
	}
	
	public static void retiradaDinero(Connection con, String dni, double dinero) throws SQLException, ExcepcionesBanco {
		String consulta, consulta2, consulta3;
		
		//tambien se podria comprobar si tiene suficiente saldo
		if(dinero>0) {
			//actualizamos saldo en tabla cuentas
			consulta2="update cuentas set saldo = saldo-? where dni= ? ;";
			PreparedStatement sentencia2=con.prepareStatement(consulta2);
			sentencia2.setDouble(1, dinero);
			sentencia2.setString(2, dni);
			sentencia2.executeUpdate();
			
			//seleccionamos el id de la cuenta
			consulta3="select id from cuentas where dni= ? ;";
			
			PreparedStatement sentencia3=con.prepareStatement(consulta3);
			sentencia3.setString(1, dni);
			ResultSet rs3 = sentencia3.executeQuery();
			rs3.next();
			int idCuenta=rs3.getInt("id");
			
			//conseguimos la fecha con su formato
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			//actualizamos tabla movimientos
			consulta="insert into movimientos(idcuenta, fecha, tipo, dinero) values(?,?,?,?);";
			
			PreparedStatement sentencia=con.prepareStatement(consulta);
			sentencia.setInt(1, idCuenta);
			sentencia.setString(2, dtf.format(LocalDateTime.now()));
			sentencia.setString(3, "Retirada | -");
			sentencia.setDouble(4, dinero);
			sentencia.executeUpdate();
			
			System.out.println(" * | Retirada de "+dinero+"€ realizada con exito a la cuenta con DNI: "+dni);
		}else {
			throw new ExcepcionesBanco("ERROR Cantidad de dinero incorrecta");
		}
		
	}
	
	public static void hacerTransferencia(Connection con, String dni, double dinero, int cuentaRecep, String concepto) throws SQLException, ExcepcionesBanco {
		String consulta, consulta2, consulta3, consulta4, consulta5;
		
		if(dinero>0 && cuentaRecep>0) {
			//PRIMERO CUENTA QUE ENVIA DINERO
			//actualizamos saldo en tabla cuentas
			consulta="update cuentas set saldo = saldo-? where dni= ? ;";
			PreparedStatement sentencia=con.prepareStatement(consulta);
			sentencia.setDouble(1, dinero);
			sentencia.setString(2, dni);
			sentencia.executeUpdate();
			
			consulta2="update cuentas set saldo = saldo+? where ID= ? ;";
			PreparedStatement sentencia2=con.prepareStatement(consulta2);
			sentencia2.setDouble(1, dinero);
			sentencia2.setInt(2, cuentaRecep);
			sentencia2.executeUpdate();
			
			//seleccionamos el id de la cuenta que envia
			consulta3="select id from cuentas where dni= ? ;";
			
			PreparedStatement sentencia3=con.prepareStatement(consulta3);
			sentencia3.setString(1, dni);
			ResultSet rs3 = sentencia3.executeQuery();
			rs3.next();
			int idCuenta=rs3.getInt("id");
			
			//conseguimos la fecha con su formato
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			//actualizamos tabla movimientos
			consulta4="insert into movimientos(idcuenta, fecha, tipo, dinero, idcuentatransfe, concepto) values(?,?,?,?,?,?);";
			
			PreparedStatement sentencia4=con.prepareStatement(consulta4);
			sentencia4.setInt(1, idCuenta);
			sentencia4.setString(2, dtf.format(LocalDateTime.now()));
			sentencia4.setString(3, "Transferencia | >");
			sentencia4.setDouble(4, dinero);
			sentencia4.setInt(5, cuentaRecep);
			sentencia4.setString(6, concepto);
			sentencia4.executeUpdate();
			
			//creamos otra linea para la otra cuenta que recibe el dinero
			consulta5="insert into movimientos(idcuenta, fecha, tipo, dinero, idcuentatransfe, concepto) values(?,?,?,?,?,?);";
			
			PreparedStatement sentencia5=con.prepareStatement(consulta5);
			sentencia5.setInt(1, cuentaRecep);
			sentencia5.setString(2, dtf.format(LocalDateTime.now()));
			sentencia5.setString(3, "Transferencia | <");
			sentencia5.setDouble(4, dinero);
			sentencia5.setInt(5, idCuenta);
			sentencia5.setString(6, concepto);
			sentencia5.executeUpdate();
			
			System.out.println(" * | Transferencia de "+dinero+"€ realizada con exito a la cuenta con Id: "+cuentaRecep);
			
		}else {
			throw new ExcepcionesBanco("ERROR Cantidad de dinero o cuenta receptora incorrectas");
		}
		
	}
	
	public static void listarMovimientos(Connection con, String dni, String fechaInicio, String fechaFin) throws SQLException {
		//suponemos que ha introducido el formato correcto de fechas
		String consulta, consulta2, consulta3;
		
		consulta="select * from movimientos where fecha>= ? and fecha<= ? and ? ;";
		
		PreparedStatement sentencia=con.prepareStatement(consulta);
		sentencia.setString(1, fechaInicio);
		sentencia.setString(2, fechaFin);
		sentencia.setString(3, dni);
		ResultSet rs = sentencia.executeQuery();
		
		System.out.println(" * | Historial de movimientos de tu cuenta");
		while(rs.next()) {
			
			System.out.println("\tIdMov: "+rs.getInt("idmov")+"\tTipo: "+rs.getString("tipo")+"\tCantidad: "+rs.getDouble("dinero")
			+"€\tFecha: "+rs.getString("fecha")+"\tCuentaTransfe: "+rs.getString("idcuentatransfe")+"\tConcepto: "+rs.getString("concepto"));
		}
		
		
	}
}
