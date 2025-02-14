package Repository;

import Entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class RepoTratamiento   {

    private Session session;

    public RepoTratamiento(Session s){
        this.session = s;
    }

    public String asignarFechaTratamiento(int idTratamiento, String nombrePaciente, LocalDate fechaInicio, LocalDate fechaFin){
        Paciente paciente;
        Tratamiento tratamiento;
        RecibePK claveCompuesta = null;
        Transaction transaction = null;
        String resultadoOperacion = "";

        try {

            transaction = session.beginTransaction();
            // 1. Obtener id paciente
            Query<Paciente> queryIdPaciente = session.createQuery("FROM Paciente WHERE nombre = :nombre", Paciente.class);
            queryIdPaciente.setParameter("nombre",nombrePaciente);
            paciente = queryIdPaciente.uniqueResult();

            if (paciente == null){
                transaction.commit();
                return "El paciente no existe en la base de datos. Cree el paciente y repita la operación";
            }


            // 2. Obtener id tratamiento
            Query<Tratamiento> queryIdTratamiento = session.createQuery(
                    "FROM Tratamiento WHERE id = :id", Tratamiento.class);
            queryIdTratamiento.setParameter("id",idTratamiento);
            tratamiento = queryIdTratamiento.uniqueResult();

            if (tratamiento == null){
                transaction.commit();
                return "El tratamiento no existe en la base de datos. Cree el tratamiento y repita la operación";
            }

            // 3. Crear clave compuesta y comprobar que no exista en la DB
            claveCompuesta = new RecibePK(paciente.getId(),tratamiento.getId(),fechaInicio);
            Query<Recibe> queryComprobarDuplicados = session.createQuery(
                        "FROM Recibe where idRecibe = :recibePk",Recibe.class);
            queryComprobarDuplicados.setParameter("recibePk",claveCompuesta);
            Recibe recibeComprobarDuplicados = queryComprobarDuplicados.uniqueResult();

            if(recibeComprobarDuplicados == null){
                // 4. Crear el objeto recibe y guardarlo
                Recibe recibe = new Recibe(claveCompuesta,paciente,tratamiento,fechaFin);

                // 5. Escribir cambios y hacer commit
                session.save(recibe);
                transaction.commit();
                resultadoOperacion = "Operacion realizada con exito";
            }else{
                resultadoOperacion = "Este tratamiento ya ha sido asignado anteriormente al paciente en las fechas indicadas";
                transaction.rollback();
            }

        }catch (Exception e){
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            resultadoOperacion = "Ha ocurrido una excepcion no controlada noob";
        }
        return resultadoOperacion;
    }

    public List<Tratamiento> obtenerTratamientos() {
        Transaction transaction = null;
        List<Tratamiento> listaTratamientos = null;
        try{
            listaTratamientos =  session.createQuery("FROM Tratamiento", Tratamiento.class)
                    .getResultList();
        }catch (Exception e){
            if (transaction != null) transaction.rollback();
        }

        return listaTratamientos;
    }
}
