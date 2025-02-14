package Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Recibe {
    @EmbeddedId
    private RecibePK idRecibe;

    @ManyToOne
    @MapsId("idPaciente")
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @ManyToOne
    @MapsId("idTratamiento")
    @JoinColumn(name = "tratamiento_id")
    private Tratamiento tratamiento;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Override
    public String toString() {
        StringBuilder toStringMejorado = new StringBuilder();
        toStringMejorado
                .append("---------------------------------------\n")
                .append("--- Recibe ID " + this.idRecibe + " ---\n")
                .append("Paciente -> " + (paciente != null ? paciente.getNombre() : "No asignado") + "\n")
                .append("Tratamiento -> " + (tratamiento != null ? tratamiento.getTipo() : "No asignado") + "\n")
                .append("Fecha Fin -> " + this.fechaFin + "\n")
                .append("---------------------------------------\n");
        return toStringMejorado.toString();
    }


    public String toStringParaPaciente() {
        StringBuilder toStringMejorado = new StringBuilder();
        toStringMejorado
                .append("---------------------------------------\n")
                .append("--- Recibe ID " + this.idRecibe + " ---\n")
                .append("Tratamiento -> " + (tratamiento != null ? tratamiento.getTipo() : "No asignado") + "\n")
                .append("Fecha Inicio -> " + this.idRecibe.getFechaInicio() + "\n")
                .append("Fecha Fin -> " + this.fechaFin + "\n")
                .append("---------------------------------------\n");
        return toStringMejorado.toString();
    }

}


