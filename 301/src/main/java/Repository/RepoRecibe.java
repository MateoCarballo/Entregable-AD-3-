package Repository;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;

public class RepoRecibe  {
    private Session session;

    public RepoRecibe(Session session) {
        this.session = session;
    }

    private int obtenerIdPaciente(String nombrePaciente) {
        Transaction transaction = null;
        int idPaciente = -1;
        try{
             transaction = session.beginTransaction();
            Query<Integer> queryPaciente = session.createQuery("SELECT id FROM Paciente WHERE nombre = :nombrePaciente ", Integer.class);
            idPaciente = queryPaciente.executeUpdate();

        }catch (Exception e){
            if (transaction != null){
                transaction.rollback();
            }
        }
        return idPaciente;
    }


    private int obtenerIdTratamiento(String tipoTratamiento) {
        Transaction transaction = null;
        int idTratamiento = -1;
    try{
        transaction = session.beginTransaction();
        Query<Integer> queryTratamiento = session.createQuery("SELECT id FROM Tratamiento WHERE tipo = :tipoTratamiento",Integer.class);
        queryTratamiento.setParameter("tipoTratamiento",tipoTratamiento);
        idTratamiento = queryTratamiento.executeUpdate();
    } catch (Exception e) {
        if (transaction != null){
            transaction.rollback();
            e.printStackTrace();
        }
    }
    return idTratamiento;
    }
}
