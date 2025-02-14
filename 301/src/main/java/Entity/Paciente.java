package Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder // Permite usar el metodo factoria y es mas visible y comodo para contruirlo

public class Paciente {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull //Esto lanza errores de nulos en Java
    @Column(nullable = false, length = 100) // Esto lanza errores de nulos en la DB
    private String nombre;
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    private String direccion;

    //TODO preguntar a Jose
    // Por qué no usar Lazy para que solo las cargue cuando las necesita
    // Es mas eficiente, por qué no?
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.REMOVE)
    private List <Cita> citas;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.REMOVE) // Cuando remueve el objeto remueve todas las referencias a esa tupla (Objeto)
    private List<Recibe> listaRecibes;

    //Constructor requerido
    public Paciente(int id, @NonNull String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Paciente(int id, String nombre, List<Cita> citas, List<Recibe> listaRecibes) {
        this.id = id;
        this.nombre = nombre;
        this.citas = new ArrayList<Cita>();
        this.listaRecibes = new ArrayList<Recibe>();
    }

    public void addCita(Cita c){
        //Añado la cita a la lista de citas del paciente
        citas.add(c);
        //Le digo a la cita que el paciente soy yo mismo
        c.setPaciente(this);
    }

    public void addRecibe(Recibe recibe){
        listaRecibes.add(recibe);
        recibe.setPaciente(this);
    }

    @Override
    public String toString() {
        StringBuilder toStringMejorado = new StringBuilder();
        toStringMejorado
                .append("---------------------------------------\n")
                .append("--- Paciente " + this.nombre + " ---\n")
                .append("Id -> " + this.id + "\n")
                .append("Fecha de Nacimiento -> " + this.fechaNacimiento + "\n")
                .append("Direccion -> " + this.direccion + "\n")
                .append("---------------------------------------\n")
                .append("Citas:\n ");
                for (Cita c :citas){
                    toStringMejorado.append(c.toStringParaPaciente());
                }
                toStringMejorado.append("---------------------------------------\n")
                                .append("Recibe:\n ");
                for (Recibe r :listaRecibes){
                    toStringMejorado.append(r.toStringParaPaciente());
                }
                toStringMejorado.append("---------------------------------------\n");
        return toStringMejorado.toString();
    }


}
