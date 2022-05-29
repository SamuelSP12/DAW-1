# DAW-1
Repositorio del curso DAW 1º año.

CODIGO SQL AÑADIDO PARA EL USO DE LA V1.2

--
-- Creacion procedimiento almacenado pedido | Samuel
--

delimiter //
create procedure newCliente(in nom varchar(50), in direc varchar(50))
begin
	insert into clientes(nombre, direccion) values(nom, direc);
end
//

-- Ej: call newCliente('Samuel','Malaga');
