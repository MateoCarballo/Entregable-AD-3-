package Entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Doctor {
    @Id
    //Esto solo cuando quiero que la generacion de ID la lleve hibernate es decir sea autoincremental en la base de datos
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String nombre;
    private String especialidad;
    private String telefono;
    //Mapea contra un objeto en la otra tabla (entidad)
    @OneToOne(mappedBy = "doctor")
    private Cita cita;

    public Doctor(int id, @NonNull String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Doctor(int id, String nombre, String especialidad, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.telefono = telefono;
    }

    @Override
    public String toString(){
        StringBuilder toStringMejorado = new StringBuilder();
        toStringMejorado
                .append("---------------------------------------\n")
                .append("--- Doctor " + nombre + " ---\n")
                .append("Id -> " + id + "\n")
                .append("Especialidad -> " + especialidad + "\n")
                .append("Telefono -> " + telefono + "\n")
                .append("---------------------------------------\n");
        return toStringMejorado.toString();
    }

}
