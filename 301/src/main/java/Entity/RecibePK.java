package Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecibePK implements Serializable {
    @Column(name = "id_paciente")
    private int idPaciente;

    @Column(name = "tratamiento_id")
    private int idTratamiento;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

}
