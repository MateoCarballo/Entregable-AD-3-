package org.example;

import Entity.Doctor;
import Entity.Hospital;
import Entity.Paciente;
import Entity.Tratamiento;
import Repository.*;
import org.hibernate.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class App {
    static Session session;
    static RepoDoctor repoDoctor;
    static RepoPaciente repoPaciente;
    static RepoCita repoCita;
    static RepoRecibe repoRecibe;
    static RepoTratamiento repoTratamiento;
    static RepoHospital repoHospital;
    static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static String entradaTeclado;

    public static void main( String[] args ) {
        boolean continuar = true;
        final String MENSAJE_CANCELAR_OPERACION = "Operacion cancelada por el usuario";
        session = HibernateUtil.get().openSession();
        repoDoctor = new RepoDoctor(session);
        repoPaciente = new RepoPaciente(session);
        repoCita = new RepoCita(session);
        repoRecibe = new RepoRecibe(session);
        repoTratamiento = new RepoTratamiento(session);
        repoHospital = new RepoHospital(session);

        while (continuar) {

            // Imprimimos el menú en consola
            System.out.print(generarMenu());

            try {
                // TODO No me deja usar el switch mejorado
                switch (seleccionarOpcionMenu()) {
                    case 0:
                        continuar = false;
                        break;
                    case 1:
                        switch (menuOpcionesCrearBorrarModificar()){
                            case 0:
                                System.out.println(MENSAJE_CANCELAR_OPERACION);
                                break;
                            case 1:
                                crearDoctor();
                                break;
                            case 2:
                                modificarDoctor();
                                break;
                            case 3:
                                borrarDoctor();
                                break;
                        }
                        break;
                    case 2:
                        switch (menuOpcionesCrearBorrarModificar()){
                            case 0:
                                System.out.println(MENSAJE_CANCELAR_OPERACION);
                                break;
                            case 1:
                                crearPaciente();
                                break;
                            case 2:
                                modificarPaciente();
                                break;
                            case 3:
                                borrarPaciente();
                                break;
                        }
                        break;
                    case 3:
                        asignarDoctorPaciente();
                        break;
                    case 4:
                        indicarFechaFinTratamiento();
                        break;
                    case 5:
                        cambiarHospitalParaTratamiento();
                        break;
                    case 6:
                        mostrarDatosPacientePorNombre();
                        break;
                    case 7:
                        mostrarTodosTratamientosHospital();
                        break;
                    case 8:
                        mostratTodosHospitalesYNumeroTratamientos();
                        break;
                    default:
                        System.out.println("Opción no válida, por favor seleccione una opción del 0 al 8.");
                        break;
                }

            } catch (NumberFormatException e) {
                // En caso de error (entrada no válida o error de I/O)
                System.out.println("Error en la entrada. Por favor, ingrese un número válido.");
            }
        }
        mostrarBarraDeCarga(1);
        session.close();
        System.out.println("Finalizando la conexion a MySQL");
    }

    //OPCION 1.1

    private static void crearDoctor(){
        /**
         * Según que ocurra durante la operación recibiremos un mensaje u otro.
         * 1    =>  "No se ha completado la operación revisar datos": Indica que la operación no se ha llevado a cabo
         * 2    =>  "Operación completada" : La modificación se ha realizado con éxito.
         */
        String mensajeResultadoOperacion = repoDoctor.crearDoctor(pedirDatosNuevoDoctor());
        System.out.println(mensajeResultadoOperacion);
    }

    private static Doctor pedirDatosNuevoDoctor() {
        /**
         * Recopilo todos los datos para crear un nuevo
         * objeto doctor que guardaremos en la base de datos
         */

        int id = 0 ;
        String nombreDoctor = "";
        String especialidadDoctor = "";
        String telefonoDoctor ="";

        try {
            id = repoDoctor.obtenerPrimerIdDisponible();
            do{
                System.out.println("Introduce el nombre del doctor");
                nombreDoctor= br.readLine();
                if (!PatronesRegex.NOMBRE.matches(nombreDoctor)) System.out.println("El nombre introducido no es valido -> " + nombreDoctor);
            }while(!PatronesRegex.NOMBRE.matches(nombreDoctor));

            do{
                System.out.println("Introduce la especialidad del doctor");
                especialidadDoctor = br.readLine();
                if (!PatronesRegex.SOLO_LETRAS.matches(especialidadDoctor)) System.out.println("La especialidad introducida no es valida -> " + especialidadDoctor);;
            }while(!PatronesRegex.SOLO_LETRAS.matches(especialidadDoctor));

            do{
                System.out.println("Introduce el telefono del doctor");
                telefonoDoctor = br.readLine();
                if (!PatronesRegex.TELEFONO.matches(telefonoDoctor)) System.out.println("El numero de telefono no es valido -> " + telefonoDoctor);;
            }while(!PatronesRegex.TELEFONO.matches(telefonoDoctor));

        } catch (IOException e) {
            System.out.println("Error en lectura datos");
        }

        return Doctor.builder()
                .id(id)
                .nombre(nombreDoctor)
                .especialidad(especialidadDoctor)
                .telefono(telefonoDoctor)
                .build();
    }

    // OPCION 1.2

    private static void modificarDoctor(){
        /**
         * Según que ocurra durante la operación recibiremos un mensaje u otro.
         * 1 => "No se ha completado la operación revisar datos": Indica que la operación no se ha llevado a cabo
         * 2 => "Operación completada" : La modificación se ha realizado con éxito.
         */
        String mensajeResultado = repoDoctor.modificarDoctor(pedirDatosModificarDoctor());
        System.out.println(mensajeResultado);
    }

    private static Doctor pedirDatosModificarDoctor() {

        Doctor doctor = null;
        String nombreDoctor;
        String especialidad;
        String telefonoDoctor;

        try {
            System.out.println("Que doctor deseas modificar ? (Escribe su nombre)");

            do{
                System.out.println("Introduce el nombre del doctor");
                nombreDoctor= br.readLine();
                if (!PatronesRegex.NOMBRE.matches(nombreDoctor)) System.out.println("El nombre introducido no es valido -> " + nombreDoctor);
            }while(!PatronesRegex.NOMBRE.matches(nombreDoctor));

            doctor = repoDoctor.buscarDoctor(nombreDoctor);
            if ( doctor != null){
                /**
                 * Si el doctor no es nulo significa que existe en la DB
                 * Entonces preguntamos que campos queremos modificar
                 * y seteamos esos campos en el nuevo objeto para poder actualizarlo
                 */
                do{
                    System.out.println("Deseas modificar el nombre ? (si no desea modificarlo solo pulsa enter)");
                    nombreDoctor= br.readLine();
                    if (nombreDoctor.trim().isEmpty()) break;
                    if (!PatronesRegex.NOMBRE.matches(nombreDoctor)) System.out.println("El nombre introducido no es valido -> " + nombreDoctor);
                }while(!PatronesRegex.NOMBRE.matches(nombreDoctor));

                do{
                    System.out.println("Deseas modificar la especialidad ? (si no desea modificarlo solo pulsa enter)");
                    especialidad = br.readLine();
                    if (especialidad.trim().isEmpty()) break;
                    if (!PatronesRegex.SOLO_LETRAS.matches(especialidad)) System.out.println("La especialidad introducida no es valida -> " + especialidad);
                }while(!PatronesRegex.SOLO_LETRAS.matches(especialidad));

                do{
                    System.out.println("Deseas modificar el telefono ? (si no desea modificarlo solo pulsa enter)");
                    telefonoDoctor = br.readLine();
                    if (telefonoDoctor.trim().isEmpty()) break;
                    if (!PatronesRegex.TELEFONO.matches(telefonoDoctor)) System.out.println("El telefono introducida no es valido -> " + telefonoDoctor);;
                }while(!PatronesRegex.TELEFONO.matches(telefonoDoctor));

                if (!nombreDoctor.trim().isEmpty()){
                    doctor.setNombre(nombreDoctor);
                }

                if (!especialidad.trim().isEmpty()){
                    doctor.setEspecialidad(especialidad);
                }

                if (!telefonoDoctor.trim().isEmpty()){
                    doctor.setTelefono(telefonoDoctor);
                }
            }
        } catch (IOException e) {
            System.out.println("Error en lectura datos");
        }
        return doctor;
    }

    // OPCION 1.3

    private static void borrarDoctor(){
        int idDoctor = pedirDatosBorrarPorId();
        if (repoDoctor.existeDoctor(idDoctor)) System.out.println(repoDoctor.borrarPorId(idDoctor));
    }

    private static int pedirDatosBorrarPorId() {
        printearDoctores();
        String idMedico = "";
        System.out.println("Introduce el Id del medico que quieres eliminar\n");
        try {
            do{
                idMedico = br.readLine();
            }while(!PatronesRegex.SOLO_NUMEROS_POSITIVOS.matches(idMedico));
        } catch (IOException e) {
            System.out.println();
        }
        return Integer.parseInt(idMedico);
    }

    //OPCION 2.1

    private static void crearPaciente(){
        String resultadoOperacion = repoPaciente.crearPaciente(pedirDatosNuevoPaciente());
        System.out.println(resultadoOperacion);
    }

    private static Paciente pedirDatosNuevoPaciente() {
        int id = 0 ;
        String nombrePaciente = "";
        LocalDate fechaNacimiento = LocalDate.of(1900,01,01);
        String direccion = "";

        try {
            id = repoPaciente.obtenerPrimerIdDisponible();
            // El paciente se puede llamar como quiera como si se llama %^&@

            do{
                System.out.println("Introduce el nombre del paciente");
                nombrePaciente= br.readLine();
                if (!PatronesRegex.NOMBRE.matches(nombrePaciente)) System.out.println("El nombre introducido no es valido -> " + nombrePaciente);
            }while(!PatronesRegex.NOMBRE.matches(nombrePaciente));

            System.out.println("Fecha de nacimiento (Formato AAAA-MM-DD)");

            boolean continuar;
            do{
                continuar = true;
                try {
                    fechaNacimiento = LocalDate.parse(br.readLine(),formatoFecha);
                } catch (DateTimeParseException e) {
                    System.out.println("Fecha inválida. Asegúrate de que sea una fecha real.");
                    continuar = false;
                }
            }while (!continuar);

            do{
                System.out.println("Introduce la dirección");
                direccion= br.readLine();
                if (direccion.isEmpty()) System.out.println("Es necesa -> " + nombrePaciente);
            }while(!PatronesRegex.NOMBRE.matches(nombrePaciente));

        } catch (IOException e) {
            System.out.println("Error en lectura datos");
        }

        return Paciente.builder()
                .id(id)
                .nombre(nombrePaciente)
                .fechaNacimiento(fechaNacimiento)
                .direccion(direccion)
                .build();
    }

    //OPCION 2.2

    private static void modificarPaciente(){
        String resultadoOperacion = repoPaciente.modificarPaciente(pedirDatosModificarPaciente());
        System.out.println(resultadoOperacion);
    }

    private static Paciente pedirDatosModificarPaciente() {
        Paciente paciente = new Paciente();
        String nombrePaciente = "";
        String fechaNacimientoString = "";
        LocalDate fechaNacimiento = null;
        String direccion ="";

        try {
            System.out.println("Que paciente deseas modificar ? (Escribe su nombre)");
            nombrePaciente = br.readLine();
            paciente = repoPaciente.buscarPaciente(nombrePaciente);
            if ( paciente != null){
                System.out.println("Deseas modificar el nombre ? (si no deseas modificarlo solo pulsa enter)");
                nombrePaciente = br.readLine();

                boolean continuar;
                do{
                    System.out.println("Deseas modificar la fecha de nacimiento ? (si no deseas modificarla solo pulsa enter)");
                    continuar = true;
                    try {
                        fechaNacimientoString = br.readLine();
                        fechaNacimiento = LocalDate.parse(fechaNacimientoString,formatoFecha);
                    } catch (DateTimeParseException e) {
                        if (!fechaNacimientoString.isEmpty()){
                            System.out.println("Fecha inválida. Asegúrate de que sea una fecha real. Con formaro AAAA-MM-DD");
                            continuar = false;
                        }
                    }
                }while (!continuar);

                /**
                 * System.out.println("Deseas modificar la fecha de nacimiento (AAAA-MM-DD) ? (si no desea modificarlo solo pulsa enter)");
                 * //TODO comprobar fecha antes de parsearla a localdate
                 * fechaNacimiento = br.readLine();
                 */
                System.out.println("Deseas modificar la direccion ? (si no desea modificarlo solo pulsa enter)");
                direccion = br.readLine();

                if (!nombrePaciente.trim().isEmpty()){
                    paciente.setNombre(nombrePaciente);
                }
                if (!fechaNacimientoString.trim().isEmpty()){
                    paciente.setFechaNacimiento(fechaNacimiento);
                }
                if (!direccion.trim().isEmpty()){
                    paciente.setDireccion(direccion);
                }
            }
        } catch (IOException e) {
            System.out.println("Error en lectura datos");
        }
        return paciente;
    }

    //OPCION 2.3

    public static void borrarPaciente(){
        printearPacientes();
        String nombrePacienteEliminar = pedirDatosBorrarPaciente();
        String mensajeResultado = repoPaciente.borrarPaciente(nombrePacienteEliminar);
        System.out.println(mensajeResultado);

    }

    private static String pedirDatosBorrarPaciente() {
        String nombrePaciente = "";
        try{
            do{
                System.out.println("Introduce el nombre del paciente a eliminar");
                nombrePaciente= br.readLine();
            }while(!PatronesRegex.NOMBRE.matches(nombrePaciente));
        }catch (IOException e){
            System.out.println("Error al leer los datos");
        }
        return nombrePaciente;
    }

    // OPCION 3

    private static void asignarDoctorPaciente() {
        String doctorNombre = "";
        String pacienteNombre = "";
        String retorno = "Alguno de los campos está vacío";
        try{
            System.out.println("Escribe el nombre del doctor");
            doctorNombre = br.readLine();
            System.out.println("Escribe el nombre del paciente");
            pacienteNombre = br.readLine();
        }catch(IOException e){
            System.out.println("Error en la lectura de datos por teclado");
        }
        // TODO Java 8 no tiene el metodo "isBlank()",
        //  Entiendo que debo comprobar que no metan espacios en blanco,
        //  Pero no puedo con esta version de Java
        if (!doctorNombre.isEmpty() && !pacienteNombre.isEmpty()){
            Doctor doctor = repoDoctor.buscarDoctor(doctorNombre);
            Paciente paciente = repoPaciente.buscarPaciente(pacienteNombre);
            if (doctor != null && paciente != null) {
                retorno = repoCita.crearCita(paciente, doctor);
            }
        }
        System.out.println(retorno);;
    }

    // OPCION 4

    private static void indicarFechaFinTratamiento(){
        LocalDate fechaComienzo = null;
        LocalDate fechaFin = null;
        String idTratamiento = "";
        String nombrePaciente = "Paciente";

        try {
            do{
                do{
                    System.out.println("Introduce la fecha de comienzo del tratamiento (AAAA-MM-DD)");
                    fechaComienzo = comprobarFechaIntroducida(br.readLine());
                }while(fechaComienzo == null);

                do{
                    System.out.println("Introduce la fecha fin del tratamiento (AAAA-MM-DD)");
                    fechaFin = LocalDate.parse(br.readLine());
                }while(fechaFin == null);
            }while((fechaFin.isBefore(fechaComienzo)));

            do{
                System.out.println("Introduce el tipo de tratamiento por id");
                printearTratamientos();
                idTratamiento = br.readLine();
            }while(!PatronesRegex.SOLO_NUMEROS_POSITIVOS.matches(idTratamiento));

            do{
                System.out.println("Introduce el nombre del paciente");
                nombrePaciente = br.readLine();
            }while(!PatronesRegex.NOMBRE.matches(nombrePaciente));

        } catch (IOException e) {
            System.out.println("Error al registrar las fechas en 'indicarFechaFinTratamiento'");;
        }

        // Compruebo en el propio metodo del repositorio que existan en la base de datos los tratamientos y los pacientes
        System.out.println(repoTratamiento.asignarFechaTratamiento(
                Integer.parseInt(idTratamiento),
                nombrePaciente,
                fechaComienzo,
                fechaFin));
    }

    private static LocalDate comprobarFechaIntroducida(String fecha){
        LocalDate posibleFecha = null;

        try{
            posibleFecha = LocalDate.parse(fecha);
        }catch(DateTimeException e){
            System.out.println("Comprueba que la fecha sea correcta");
        }
        return posibleFecha;
    }

    // OPCION 5

    public static void cambiarHospitalParaTratamiento(){
        /**
        Posicion 0 => id Tratamiento
        Posicion 1 => nombre hospital actual
        Posicion 2 => nombre nuevo hospital
        */
        String[] datos = pedirDatosModificarHospital();
        System.out.println(repoHospital.anhadirNuevoTratamiento(
                Integer.parseInt(datos[0]),
                datos[1],
                datos[2]));
    }

    private static String [] pedirDatosModificarHospital() {
        String[] datosConsulta = new String[3];
        try{
            System.out.println("Introduce el id del tratamiento que quieres cambiar de hospital");
            printearTratamientos();
            datosConsulta[0] = br.readLine();
            System.out.println("Introduce el nombre del hospital actual en el que se realiza el tratamiento");
            datosConsulta[1] = br.readLine();
            System.out.println("Introduce el nombre del nuevo hospital");
            datosConsulta[2] = br.readLine();
        }catch(IOException e ){
            System.out.println("Error en la lectura de datos");
        }
        return datosConsulta;
    }

    //OPCION 6

    private static void mostrarDatosPacientePorNombre(){
        String nombrePaciente = pedirDatosMostrarPaciente();
        if (!nombrePaciente.isEmpty()){
            Paciente p = repoPaciente.mostrarTodosDatos(nombrePaciente);
            if (p != null){
                System.out.println(p);
            }else{
                System.out.println("El paciente introducido no existe");
            }
        }else{
            System.out.println("Introduce un nombre para mostrar los datos del paciente");
        }
    }

    private static String pedirDatosMostrarPaciente() {
        printearPacientes();
        String entradaTeclado = "";
        try{
            System.out.println("Introduce el nombre del paciente que quieres visualizar");
            entradaTeclado = br.readLine();
        } catch (IOException e) {
            System.out.println("Error de lectura");
        }
        return entradaTeclado;
    }

    //OPCION 7

    public static void mostrarTodosTratamientosHospital(){
        String nombreHospital ="";
        try{
            System.out.println("Introduce el nombre del hospital y mostrare todos los tratamientos que da");
            nombreHospital = br.readLine();
        }catch(IOException e){
            System.out.println("Error al leer los datos");
        }
        Hospital hospital = (repoHospital.mostrarTratamientos(nombreHospital));
        if (hospital != null){
            System.out.println(hospital.escribirHospitalCompleto());
        }else{
            System.out.println("El nombre del hospital no se encuentra en la db");
        }
    }

    // OPCION 8

    public static void mostratTodosHospitalesYNumeroTratamientos(){
        List<Hospital> listadoImprimir = repoHospital.mostratTodosTratamientosTodosHospitales();
        for (Hospital h : listadoImprimir){
            System.out.println(h.escribirHospitalCompleto());;
        }
    }

    // MENUS

    private static int seleccionarOpcionMenu() {
        String eleccion="";
        do {
            try{
                eleccion = br.readLine();
            }catch(IOException ioe){
                System.out.println("Error al leer los datos por teclado");
            }
        }while(!PatronesRegex.DIGITOS_0_9.matches(eleccion));
        return Integer.parseInt(eleccion);
    }

    private static String generarMenu() {
        StringBuilder menu = new StringBuilder();
        menu.append("-------------------------------------------------\n")
                .append("1. OPERACIONES SOBRE DOCTOR\n")
                .append("2. OPERACIONES SOBRE PACIENTE\n")
                .append("3. ASIGNAR A UN DOCTOR UN PACIENTE (DAR CITA)\n")
                .append("4. INDICAR LA FECHA FIN DEL TRATAMIENTO DE UN PACIENTE\n")
                .append("5. CAMBIAR EL HOSPITAL DE UN TRATAMIENTO\n")
                .append("6. MOSTRAR DATOS PACIENTE (Introduciendo nombre)\n")
                .append("7. MOSTRAR LOS TRATAMIENTOS Y LOS DATOS DE LOS HOSPITALES EN LOS QUE SE REALIZAN \n")
                .append("8.  MOSTRAR EL NUMERO TOTAL DE TRATAMIENTOS QUE TIENE CADA HOSPITAL (ENTRADA NOMBRE HOSPITAL)\n")
                .append("0. Cerrar aplicación\n")
                .append("-------------------------------------------------\n")
                .append("Seleccione una opción: ");
        return menu.toString();
    }

    private static int menuOpcionesCrearBorrarModificar() {
        String eleccion = "";
        StringBuilder menu = new StringBuilder();
        menu.append("-------------------------------------------------\n")
                .append("1. Crear\n")
                .append("2. Modifcar\n")
                .append("3. Borrar\n")
                .append("0. Anular operación\n")
                .append("Seleccione una opción: ");
        System.out.print(menu);
        do {
            try{
                eleccion = br.readLine();
            }catch(IOException ioe){
                System.out.println("Error al leer los datos por teclado");
            }
        }while(!PatronesRegex.DIGITOS_0_3.matches(eleccion));
        return Integer.parseInt(eleccion);
    }

    // PRINTS DE LISTA DE OBJETOS

    private static void printearDoctores(){
        List<Doctor> doctores = repoDoctor.obtenerDoctores();
        if (!doctores.isEmpty()){
            for (Doctor d :doctores){
                System.out.println(d.toString());
            }
        }
    }

    public static void printearPacientes(){
        List<Paciente> pacientes = repoPaciente.obtenerPacientes();
        if (!pacientes.isEmpty()){
            for (Paciente p :pacientes){
                System.out.println(p);
            }
        }
    }

    public static void printearTratamientos(){
        List<Tratamiento> tratamientos = repoTratamiento.obtenerTratamientos();
        if (!tratamientos.isEmpty()){
            for (Tratamiento p :tratamientos){
                System.out.println(p);
            }
        }
    }

    // Método que simula la barra de carga de x segundos
    private static void mostrarBarraDeCarga(int segundos) {

        System.out.println("Cerrando aplicación...");
        int totalTiempo = segundos * 1000; // Convertir a milisegundos
        int tiempoRestante = totalTiempo;
        int intervalo = 100; // Intervalo de actualización de la barra

        System.out.println("Iniciando la barra de carga...");

        // Realizamos el bucle de la barra de carga
        for (int i = 0; i <= totalTiempo; i += intervalo) {
            tiempoRestante = totalTiempo - i;
            int porcentaje = (int) ((i / (float) totalTiempo) * 100);

            // Limpiamos la línea anterior para que la barra se actualice
            System.out.print("\r[");
            int cantidad = porcentaje / 2; // Calculamos el número de caracteres para la barra
            for (int j = 0; j < cantidad; j++) {
                System.out.print("#");
            }
            for (int j = cantidad; j < 50; j++) {
                System.out.print(" ");
            }
            System.out.print("] " + porcentaje + "% - " + tiempoRestante / 1000 + " segundos restantes");

            try {
                Thread.sleep(intervalo); // Esperamos 100ms entre actualizaciones
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\nCierre completado.");
    }

}