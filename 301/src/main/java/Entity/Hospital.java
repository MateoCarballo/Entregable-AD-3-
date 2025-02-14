package Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private String nombre;
    private String ubicacion;

    //Todo preguntar a Jose
    // Donde tengo que inicilizar la lista o el arrayList
    @OneToMany(mappedBy = "hospital")
    private List<Tratamiento> tratamientos ;

    public Hospital(int id, @NonNull String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.tratamientos = new ArrayList();
    }

    public void addTratamiento(Tratamiento t){
        tratamientos.add(t);
        t.setHospital(this);
    }

    public String escribirHospitalCompleto(){
        StringBuilder resultadoCompleto = new StringBuilder();
        resultadoCompleto.append(this)
                .append("Tratamientos -> " + tratamientos.size() + "\n")
                .append("---------------------------------------\n");
        /*
        for(Tratamiento t : tratamientos){
            resultadoCompleto.append(t.toString());
        }
         */
        return resultadoCompleto.toString();
    }

    public void borrarTratamiento(int idTratamiento){
        for (int i = 0; i < tratamientos.size(); i++) {
            if (tratamientos.get(i).getId() == idTratamiento){
                tratamientos.remove(i);
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder toStringMejorado = new StringBuilder();
        toStringMejorado
                .append("---------------------------------------\n")
                .append("--- Hospital " + this.nombre + " ---\n")
                .append("Id -> " + this.id + "\n")
                .append("Ubicacion -> " + this.ubicacion + "\n")
                .append("---------------------------------------\n");


        return toStringMejorado.toString();
    }
}
