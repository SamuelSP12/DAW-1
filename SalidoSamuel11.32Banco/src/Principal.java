import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

//Author: Samuel Salido Palacios
//Version: 1.0
//Date: 25/05/2022
//Exercise: BancoFinal
//All rights reserved Copyright©2021 | Samuel SP

public class Principal {
	public static void main(String args[]) {
		
		System.out.println("Iniciando programa... Conectando a la base de datos del sistema...");
		//Conectamos a la base de datos
		Connection conex=null;
		 
	    /*Almacenamos lo que nos devuelve el método Conexion()
	    en la variable conex*/
	    conex = conexion();
	 
	    // Si la variable objeto conex es diferente de nulo informamos que la conexión es correcta
	    if(conex != null){
	        System.out.println("BD Conectada correctamente.");
	    }else{ // Sino informamos que no nos podemos conectar
	        System.out.println("No has podido conectarte a la BD.");
	    }
		
		
	    String dniEnUso="";
	    boolean estadoDniEnUso=false;
	    
		@SuppressWarnings("resource")
		Scanner ent1 = new Scanner(System.in);
		
		//Menu de opciones
		int menu=0;
		do {
			System.out.println("*MENÚ* Elige una opción\n"
					+ "1 Alta CLIENTE | 2 Baja CLIENTE | 3 Modificar CLIENTE | 4 Alta CUENTA | 5 Baja CUENTA | "
					+ "6 Inicio CUENTA | 7 Salir CUENTA | 8 >Movimientos | 9 Cerrar App");
			//Dentro de movimientos>listar los movimientos entre fechas, ver saldo, ingresar y retirar dinero, hacer transferencias
			menu=ent1.nextInt();
			
			switch (menu) {
			
				case 1: {
					String dni, nombre, tlf, direc;
					@SuppressWarnings("resource")
					Scanner ent2 = new Scanner(System.in);
					System.out.println("DNI a dar de Alta:");
					dni=ent2.nextLine();
					System.out.println("Nombre completo a dar de alta:");
					nombre=ent2.nextLine();
					System.out.println("TLF a dar de alta:");
					tlf=ent2.nextLine();
					System.out.println("Direccion a dar de alta:");
					direc=ent2.nextLine();
				
					//Crear cliente
					try {
						Clientes.altaCliente(conex, dni, nombre, tlf, direc);
					} catch (ExcepcionesBanco | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					break;
				}
				case 2: {
					String dni;
					@SuppressWarnings("resource")
					Scanner ent2 = new Scanner(System.in);
					System.out.println("DNI a dar de Baja:");
					dni=ent2.nextLine();
					//eliminar cliente
				
					try {
						Clientes.bajaCliente(conex, dni);
					} catch (ExcepcionesBanco | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				}
				case 3: {
					String dni;
					@SuppressWarnings("resource")
					Scanner ent2 = new Scanner(System.in);
					System.out.println("DNI de cliente para Modificar datos:");
					dni=ent2.nextLine();
					//cambiar datos cliente mediante un switch para indicar q dato cambiar
					
					try {
						Clientes.actuDatosCliente(conex, dni);
					} catch (ExcepcionesBanco | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					break;
				}
				case 4: {
					
					//crear cuenta
					String dni;
					@SuppressWarnings("resource")
					Scanner ent2 = new Scanner(System.in);
					System.out.println("DNI para cuenta a dar de Alta:");
					dni=ent2.nextLine();
				
					try {
						CuentaBancaria.altaCuentaBancaria(conex, dni);
					} catch (ExcepcionesBanco | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				}
				case 5: {
					
					//estado cuenta=false
					String dni;
					@SuppressWarnings("resource")
					Scanner ent2 = new Scanner(System.in);
					System.out.println("DNI para cuenta a dar de Baja:");
					dni=ent2.nextLine();
					
					try {
						CuentaBancaria.bajaCuentaBancaria(conex, dni);
					} catch (ExcepcionesBanco | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					break;
				}
				case 6: {
					
					//seleccionamos una cuenta y asignamos valores a los dnienuso
					
					String dni;
					@SuppressWarnings("resource")
					Scanner ent2 = new Scanner(System.in);
					System.out.println("DNI para cuenta a Seleccionar para usar:");
					dni=ent2.nextLine();
					
					try {
						estadoDniEnUso=CuentaBancaria.seleccionarCuenta(dni);
						
						dniEnUso=dni;
					} catch (ExcepcionesBanco | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					break;
				}
				case 7: {
					
					//deseleccionar cuenta
					estadoDniEnUso=false;
					System.out.println("CHECK La cuenta para el DNI: "+dniEnUso+" ha sido deseleccionada");
					dniEnUso="";
					
					break;
				}
				case 8: {
					
					//si hay una cuenta seleccionada entonces
					if(estadoDniEnUso) {
						
						@SuppressWarnings("resource")
						Scanner ent2 = new Scanner(System.in);
						int menuMov=0;
						
						do {
							System.out.println("*MENÚ Movimientos* Elige una opción\n"
									+ "1 Listar MOVIMIENTOS | 2 SALDO Actual | 3 Ingreso € | 4 Retirada € | 5 Hacer TRANSFERENCIA | 6 Atrás");
							menuMov=ent2.nextInt();
							
							switch (menuMov) {
							
								case 1: {
								
									String fechaInicio, fechaFin;
									Scanner ent5 = new Scanner(System.in);
									System.out.println("Fecha inicio del historial: (yyyy-MM-dd HH:mm:ss)");
									fechaInicio=ent5.nextLine();
									System.out.println("Fecha fin del historial: (yyyy-MM-dd HH:mm:ss)");
									fechaFin=ent5.nextLine();
								
									try {
										Movimientos.listarMovimientos(conex, dniEnUso, fechaInicio, fechaFin);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									break;
								}
								case 2: {
									
									try {
										Movimientos.getSaldoCuenta(conex, dniEnUso);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
									break;
								}
								case 3: {
									double dinero=0;
									@SuppressWarnings("resource")
									Scanner ent3 = new Scanner(System.in);
									System.out.println("Indique la cantidad de € a Ingresar:");
									dinero=ent3.nextDouble();
									
									//metodo pa ingresar y la cant
									try {
										Movimientos.ingresoDinero(conex, dniEnUso, dinero);
									} catch (SQLException | ExcepcionesBanco e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
									break;
								}
								case 4: {
									
									//metodo pa retirar y la cant
									double dinero=0;
									@SuppressWarnings("resource")
									Scanner ent3 = new Scanner(System.in);
									System.out.println("Indique la cantidad de € a Retirar:");
									dinero=ent3.nextDouble();
									
									try {
										Movimientos.retiradaDinero(conex, dniEnUso, dinero);
									} catch (SQLException | ExcepcionesBanco e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
									break;
								}
								case 5: {
									double dinero=0;
									String concepto;
									int cuentaRecep;
									
									Scanner ent3 = new Scanner(System.in);
									Scanner ent4 = new Scanner(System.in);
									Scanner ent5 = new Scanner(System.in);
									System.out.println("Indique la cantidad de € a Transferir:");
									dinero=ent3.nextDouble();
									System.out.println("Indique el nº de cuenta a transferirlo:");
									cuentaRecep=ent4.nextInt();
									System.out.println("Si lo desea, escriba un Concepto de transferencia:");
									concepto=ent5.nextLine();
									
									try {
										Movimientos.hacerTransferencia(conex, dniEnUso, dinero, cuentaRecep, concepto);
									} catch (SQLException | ExcepcionesBanco e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
									break;
								}
								case 6: {
									
									System.out.println("Volviendo al menú principal...");
								
									break;
								}
								default: {
									System.out.println("La opcion ``"+menuMov+"`` no es admitida. Introduzca una opción valida");
									break; 
								}
							}
							
						}while (menuMov!=6); //Cuando se seleccione la opcion cerrar sesion se cerrara el menu de eleccion
						
						
					}else {
						System.err.println("ERROR No hay ninguna cuenta seleccionada");
					}
				
					break;
				}
				case 9: {
					
					System.out.println("Cerrando App...");
					cerrarConex(conex);
					
					break;
				}
				default: {
					System.out.println("La opcion ``"+menu+"`` no es admitida. Introduzca una opción valida");
					break; 
				}
			}
			
		}while (menu!=9); //Cuando se seleccione la opcion cerrar sesion se cerrara el menu de eleccion
		
		
		System.out.println("App cerrada con exito.");
	}

	public static Connection conexion() {
		Connection conex=null;
		
		try {
			//Class.forName(driver);
			conex=DriverManager.getConnection("jdbc:mysql://localhost:3306/bancodaw", "root", "");
		} catch (Exception e) {
	        System.out.println("Error al conectar con la base de datos.\n"
	                + e.getMessage().toString());
	    }
		
		return conex;
	}
	
	public static void cerrarConex(Connection conex) {
		try {
			//Cerramos la conexion
			conex.close();
			System.out.println("Base de datos desconectada.");
		} catch (SQLException e) {
			//Controlamos posible excepcion
			System.out.println(e.getMessage().toString());
		}
	}
}
