package Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder

public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private LocalDate fecha;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    //Este joinColumn te dice en que columna
    //tiene la clave foranea que apunta al objeto
    //orphanRemoval = true para evitar referencias huérfanas.

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_doctor")
    private Doctor doctor;

    public Cita(int id, @NonNull LocalDate fecha, Paciente paciente) {
        this.id = id;
        this.fecha = fecha;
        this.paciente = paciente;
    }

    public void setDoctor(Doctor d){
        doctor= d;
        d.setCita(this);
    }

    @Override
    public String toString() {
        StringBuilder toStringMejorado = new StringBuilder();
        toStringMejorado
                .append("---------------------------------------\n")
                .append("--- Cita ID " + this.id + " ---\n")
                .append("Fecha -> " + this.fecha + "\n")
                .append("Estado -> " + this.estado + "\n")
                .append("Paciente -> " + (paciente != null ? paciente.getNombre() : "No asignado") + "\n")
                .append("Doctor -> " + (doctor != null ? doctor.getNombre() : "No asignado") + "\n")
                .append("---------------------------------------\n");
        return toStringMejorado.toString();
    }

    public String toStringParaPaciente() {
        StringBuilder toStringMejorado = new StringBuilder();
        toStringMejorado
                .append("---------------------------------------\n")
                .append("--- Cita ID " + this.id + " ---\n")
                .append("Fecha -> " + this.fecha + "\n")
                .append("Estado -> " + this.estado + "\n")
                .append("Paciente -> " + (paciente != null ? paciente.getNombre() : "No asignado") + "\n")
                .append("Doctor -> " + (doctor != null ? doctor.getNombre() : "No asignado") + "\n")
                .append("---------------------------------------\n");
        return toStringMejorado.toString();
    }
}
