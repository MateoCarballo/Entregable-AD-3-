package Repository;

import Entity.Hospital;
import Entity.Tratamiento;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RepoHospital {

    private Session session;

    public RepoHospital(Session session) {
        this.session = session;
    }

    public Hospital mostrarTratamientos(String nombreHospital) {
        Hospital hospital = null;
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            Query queryHospital = session.createQuery("FROM Hospital WHERE nombre = :nombreHospital", Hospital.class);
            queryHospital.setParameter("nombreHospital",nombreHospital);
            hospital = (Hospital) queryHospital.uniqueResult();
            hospital.getTratamientos();
            transaction.commit();
        }catch (Exception e){
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return hospital;
    }

    public List<Hospital> mostratTodosTratamientosTodosHospitales() {
        Transaction transaction = null;
        List<Hospital> listadoHospitales = null;
        try{
            transaction = session.beginTransaction();
            Query<Hospital> listaHospitales = session.createQuery("FROM Hospital" ,Hospital.class);
            listadoHospitales = listaHospitales.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        }
        return listadoHospitales;
    }

    public String anhadirNuevoTratamiento(int idTratamiento, String nombreAntiguoHospital, String nombreNuevoHospital){
        Transaction transaction = null;
        Hospital antiguoHospital;
        Hospital nuevoHospital;
        Tratamiento tratamiento;
        String resultadoOperacion = "";
        try{
            transaction = session.beginTransaction();

            //Traigo el objeto asociado al idTratamiento
            Query<Tratamiento> queryTratamiento = session.createQuery("FROM Tratamiento WHERE id = :idTratamiento", Tratamiento.class);
            queryTratamiento.setParameter("idTratamiento",idTratamiento);
            tratamiento = queryTratamiento.uniqueResult();

            if (tratamiento == null){
                transaction.commit();
                return "El tratamiento no existe, insertelo en la base de datos y repita la operación.";
            }

            //Obtengo el objeto hospital antiguo y elimino el tratamiento
            Query<Hospital> queryHospitalAntiguo = session.createQuery("FROM Hospital WHERE nombre = :nombreHospital", Hospital.class);
            queryHospitalAntiguo.setParameter("nombreHospital",nombreAntiguoHospital);
            antiguoHospital= queryHospitalAntiguo.uniqueResult();

            if (antiguoHospital == null){
                transaction.commit();
                return "El hospital actual para el tratamiento no existe, insertelo en la base de datos y repita la operación.";
            }

            //Obtengo el objeto hospital asociado al nombre que recibo
            Query<Hospital> queryHospitalNuevo = session.createQuery("FROM Hospital WHERE nombre = :nombreHospital", Hospital.class);
            queryHospitalNuevo.setParameter("nombreHospital",nombreNuevoHospital);
            nuevoHospital = queryHospitalNuevo.uniqueResult();

            if (nuevoHospital == null){
                transaction.commit();
                return "El nuevo hospital para el tratamiento no existe, insertelo en la base de datos y repita la operación.";
            }

            //Si el tratamiento ya se realiza en ese hospital no volvemos a meterle el mismo hospital
            if (tratamiento.getHospital().getId() == nuevoHospital.getId()){
                transaction.commit();
                return "Este tratamiento ya se realiza en el hospital por el que es intenta cambiar. Revise los datos.";
            }

            /*
            Por como están establecidas las relaciones bidireccionales en las clases, el añadir un tratamiento ya setea el hospital al tratamiento.
            Podriamos verlo de la otra manera, es decir, modificar el hospital de tratamiento y eliminarlo de la lista del hospital anterior.
            Resulta mas facil de esta manera.
            Planteado de esta forma me aseguro de que no quede en la lista del antiguo hospital eliminandolo a mano antes de insertar el nuevo tratamiento
            al hospital destino.
            //TODO hibernate se encarga de gestionar las relaciones a través de las listas,
                al indicarle al tratamiento que objeto hospital contiene debería eliminarlo de la lista de tratamientos de ese mismo hospital?,
                Esta linea entiendo que no es necesaria antiguoHospital.getTratamientos().remove(tratamiento);
             */
            nuevoHospital.addTratamiento(tratamiento);
            //Añado el tratamiento al hospital y dejo que hibernate haga su magia
            transaction.commit();
            resultadoOperacion = "Operacion realizada con exito";

        }catch(Exception e){
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return resultadoOperacion;
    }
/*




public List<Tratamiento> mostrarTratamientos(String nombreHospital) {
    Hospital hospital = null;
    Transaction transaction = null;
    List tratamientos = null;
    try{
        transaction = session.beginTransaction();
        Query<Tratamiento> queryTratamiento = session.createQuery("FROM Tratamiento WHERE hospital.nombre = :nombreHospital", Tratamiento.class);
        queryTratamiento.setParameter("nombreHospital",nombreHospital);
        tratamientos = queryTratamiento.getResultList();

    }catch (Exception e){
        if (transaction != null) transaction.rollback();
        e.printStackTrace();
    }
    return tratamientos;
    }
 */
}
