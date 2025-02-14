package Repository;

import Entity.Doctor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RepoDoctor {

   private final String MENSAJE_ERROR = "No se ha completado la operacion revisar datos";
   private Session session;

   public RepoDoctor(Session session) {
      this.session = session;
   }

   public String crearDoctor(Doctor nuevoDoctor){
      Transaction  transaction = null;
      //TODO Crear una clase para los mensajes comunes y no tener que modificarlos en todos los p sitios
      String retorno = MENSAJE_ERROR;
      try {
         transaction = session.beginTransaction();
         session.save(nuevoDoctor);
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
   public String modificarDoctor(Doctor doctor){
      Transaction  transaction = null;
      String retorno = MENSAJE_ERROR;
      try {
         if (doctor != null){
            transaction = session.beginTransaction();
            session.update(doctor);
            transaction.commit();
            retorno = "Operacion completada";
         }
      } catch (Exception e) {
         if (transaction != null){
            transaction.rollback();
         }
         e.printStackTrace();
      }
      return retorno;
   }

   public String borrarPorId(int id){
      Transaction  transaction = null;
      int referenciasEliminadas = 0;
      int doctoresEliminados = 0;
      String retorno = MENSAJE_ERROR;

      try {
         //Para borrar el doctor necesitamos eliminar las referencias que existen en otras tablas
         transaction = session.beginTransaction();

         Query queryEliminarReferenciaEnCita = session.createQuery("DELETE FROM Cita Where doctor.id = :idParaBorrar");
         queryEliminarReferenciaEnCita.setParameter("idParaBorrar",id);
         referenciasEliminadas = queryEliminarReferenciaEnCita.executeUpdate();

         Query queryEliminarDoctor = session.createQuery("DELETE FROM Doctor Where id = :id");
         queryEliminarDoctor.setParameter("id",id);
         doctoresEliminados = queryEliminarDoctor.executeUpdate();

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

   //Busca un doctor por nombre y devuelve el objeto asociado
   public Doctor buscarDoctor(String nombreDoctor){
      //Buscamos si existe algun doctor con ese nombre
      Query<Doctor> doctorQuery = session.createQuery("FROM Doctor WHERE nombre = :nombre", Doctor.class);
      doctorQuery.setParameter("nombre",nombreDoctor);
      Doctor doctor = doctorQuery.uniqueResult();

      //devolvemos el doctor
      return doctor;
   }

   public Boolean existeDoctor(int id){
      Boolean existe = false;
      //Buscamos si existe algun doctor con ese nombre
      Query<Doctor> doctorQuery = session.createQuery("FROM Doctor WHERE id = :id", Doctor.class);
      doctorQuery.setParameter("id",id);

      if (doctorQuery.uniqueResult() != null) existe = true;

      return existe;
   }

   public int obtenerPrimerIdDisponible() {
      Transaction transaction = null;
      int nuevoId = 1;

      try {
         transaction = session.beginTransaction();

         Query<Integer> query = session.createQuery("SELECT id FROM Doctor ORDER BY id ASC", Integer.class);
         List<Integer> ids = query.getResultList();

         for (int id : ids) {
            if (id == nuevoId) {
               nuevoId++;
            } else {
               break;
            }
         }
         transaction.commit();
      } catch (Exception e) {
         if (transaction != null) transaction.rollback();
         e.printStackTrace();
      }

      return nuevoId;
   }


   public List<Doctor> obtenerDoctores(){
      Transaction transaction = null;
      List<Doctor> listaDoctores = null;
      try{
         listaDoctores =  session.createQuery("FROM Doctor", Doctor.class)
                 .getResultList();
      }catch (Exception e){
         if (transaction != null) transaction.rollback();
      }

       return listaDoctores;
   }
}
