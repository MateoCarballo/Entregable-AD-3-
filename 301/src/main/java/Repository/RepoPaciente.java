package Repository;

import Entity.Cita;
import Entity.Doctor;
import Entity.Paciente;
import Entity.Recibe;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;


public class RepoPaciente {
    private Session session;

    private final String MENSAJE_ERROR = "No se ha completado la operacion revisar datos";
    private ArrayList<Paciente> listaPacientes;

    public RepoPaciente (Session session) {
        this.session = session;
        this.listaPacientes = new ArrayList();
    }

    public String crearPaciente(Paciente paciente){
        String retorno = MENSAJE_ERROR;
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.persist(paciente);
            transaction.commit();
            retorno = "Operacion completada";
        } catch (Exception e) {
            if (transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return retorno;
    }

    public String borrarPaciente(String nombrePaciente){
        Transaction transaction = null;
        Paciente pacienteUnico;
        String retorno = MENSAJE_ERROR;
        try{
            transaction = session.beginTransaction();

            Query<Paciente> queryPaciente = session.createQuery("FROM Paciente WHERE nombre = :nombrePaciente", Paciente.class);
            queryPaciente.setParameter("nombrePaciente",nombrePaciente);

            //Traemos la lista desde la DB mediante la consulta
            pacienteUnico= queryPaciente.uniqueResult();

            //Borramos el paciente (suponiendo que solo existe uno)
            //La configuracion de la entidad con cascade = CascadeType.REMOVE asegura que borre las referencias en otras tablas
            if (pacienteUnico != null) {
                session.remove(pacienteUnico);
                retorno = "Operacion completada";
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return retorno;
    }

    public String modificarPaciente(Paciente paciente){
        Transaction transaction = null;
        String retorno = MENSAJE_ERROR;
        try{
            transaction = session.beginTransaction();
            if(paciente!=null){
                Query <Integer> pacienteQuery = session.createQuery(
                        "SELECT id FROM Paciente WHERE nombre = : nombre",Integer.class);
                pacienteQuery.setParameter("nombre",paciente.getNombre());
                Integer idPaciente = pacienteQuery.uniqueResult();
                if (idPaciente != null){
                    paciente.setId(idPaciente);
                    session.update(paciente);
                    retorno = "Operacion compeltada con exito";
                }
            }
            transaction.commit();
        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return retorno;
    }

    private void borrarRecibeAsociado(Paciente p) {
        Transaction transaction = null;
        try{
            // TODO cambiado a lista para ver si arregalba pero nada
            Query<Recibe> buscarRecibe = session.createQuery("FROM Recibe WHERE paciente.id = :idPaciente", Recibe.class);
            buscarRecibe.setParameter("idPaciente", p.getId());
            ArrayList <Recibe> recibes= (ArrayList<Recibe>) buscarRecibe.getResultList();
            for (Recibe r:recibes){
                session.remove(r);

            }
        }catch(Exception e){
            if (transaction != null) transaction.rollback();

        }

    }

    private void borrarCitaAsociada(Paciente paciente) {
        Transaction transaction = null;
        try{
            Query<Cita> buscarCita = session.createQuery("FROM Cita WHERE paciente.id = :idPaciente", Cita.class);
            buscarCita.setParameter("idPaciente", paciente.getId());
            ArrayList <Cita> citas = (ArrayList<Cita>) buscarCita.getResultList();
            for (Cita c:citas){
                session.remove(c);
            }
        }catch(Exception e){
            if (transaction != null) transaction.rollback();
        }


    }

    /*
    public int buscarPaciente(String nombrePaciente){
        //Buscamos si existe algun doctor con ese nombre
        Query<Integer> pacienteQuery = session.createQuery("SELECT id FROM Paciente WHERE nombre = :nombre");
        pacienteQuery.setParameter("nombre",nombrePaciente);
        int intPaciente = pacienteQuery.getSingleResult();

        //devolvemos el doctor
        return intPaciente;
    }
     */

    public Paciente buscarPaciente(String nombrePaciente){
        Transaction transaction = null;
        Paciente paciente = null;
        try{
            Query<Paciente> pacienteQuery = session.createQuery("FROM Paciente WHERE nombre = :nombre", Paciente.class);
            pacienteQuery.setParameter("nombre",nombrePaciente);
            paciente= pacienteQuery.uniqueResult();

        }catch (Exception e){

        }
        return paciente;
    }

    public Paciente mostrarTodosDatos(String nombrePaciente) {
        Paciente paciente = null;
        try{
            Query<Paciente> queryPaciente = session.createQuery(
                    "FROM Paciente WHERE nombre = :nombrePaciente", Paciente.class);
            queryPaciente.setParameter("nombrePaciente", nombrePaciente);

            // Usamos getResultList() para evitar excepciones si no se encuentra
            List<Paciente> resultados = queryPaciente.getResultList();

            // Si hay al menos un paciente, devolvemos el primero; si no, retornamos null
            return resultados.isEmpty() ? null : resultados.get(0);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int obtenerPrimerIdDisponible() {
        Transaction transaction = null;
        int nuevoId = 1; // Empezamos desde el ID mínimo permitido

        try {
            transaction = session.beginTransaction();

            // Consulta para obtener todos los IDs en orden ascendente
            Query<Integer> query = session.createQuery("SELECT id FROM Paciente ORDER BY id ASC", Integer.class);
            List<Integer> ids = query.getResultList();

            // Buscar la primera brecha en los IDs
            for (int id : ids) {
                if (id == nuevoId) {
                    nuevoId++; // Si el ID actual ya existe, avanzar al siguiente
                } else {
                    break; // Encontramos un hueco
                }
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

        return nuevoId;
    }

    public List<Paciente> obtenerPacientes() {
        Transaction transaction = null;
        List<Paciente> listaPacientes = null;
        try{
            listaPacientes =  session.createQuery("FROM Paciente", Paciente.class)
                    .getResultList();
        }catch (Exception e){
            if (transaction != null) transaction.rollback();
        }

        return listaPacientes;
    }
}
