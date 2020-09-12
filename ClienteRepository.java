package ar.com.eduit.curso.java.web.repositories;

import ar.com.eduit.curso.java.web.connectors.ConnectorJDBC;
import ar.com.eduit.curso.java.web.entities.Cliente;
import ar.com.eduit.curso.java.web.enums.TipoDocumento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    private Connection conn;
    public ClienteRepository(){
        conn=new ConnectorJDBC().getConnection();
    }
    public void save(Cliente cliente){
        if(cliente==null) return;
        try (PreparedStatement ps = conn.prepareStatement(
        "insert into clientes (nombre,tipoDocumento,numeroDocumento,direccion,telefono,comentarios) values (?,?,?,?,?,?)",
        PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTipoDocumento()+""); // cuando hay un enums sumamos un campo vacio
            ps.setString(3, String.valueOf(cliente.getNumeroDocumento())); //TIPO DE DATO CHAR
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getComentarios());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) cliente.setId(rs.getInt(1));
        } catch (Exception e) {e.printStackTrace();}
    }
    
    public List<Cliente>getAll(){
        List<Cliente>list=new ArrayList();
        try (ResultSet rs = conn.createStatement().executeQuery("select * from clientes")){
            while(rs.next()){
                list.add(new Cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    TipoDocumento.valueOf(rs.getString("tipoDocumento")),
                    rs.getString("numeroDocumento"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("comentarios")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
}
