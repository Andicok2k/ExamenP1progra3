
package ec.edu.taller;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;

public class GestorIncidentes {

    private Queue<IncidenteSeguridad> colaIncidentes;
    private int capacidadMaxima;

    public GestorIncidentes(int capacidadMaxima) {

        this.capacidadMaxima = capacidadMaxima;

        this.colaIncidentes = new PriorityQueue<>(
                Comparator.comparingInt(IncidenteSeguridad::getPrioridad)
        );

    }

    public boolean registrarIncidente(IncidenteSeguridad incidente) {

        if (incidente == null)
            return false;

        if (incidente.getCodigo() == null ||
                incidente.getCodigo().trim().isEmpty())
            return false;

        if (existeIncidente(incidente.getCodigo()))
            return false;

        if (colaIncidentes.size() >= capacidadMaxima)
            return false;

        return colaIncidentes.offer(incidente);
    }

    public boolean existeIncidente(String codigo) {

        if (codigo == null || codigo.trim().isEmpty())
            return false;

        for (IncidenteSeguridad i : colaIncidentes) {
            if (i.getCodigo().equals(codigo))
                return true;
        }

        return false;
    }

    public IncidenteSeguridad consultarSiguienteIncidente() {

        return colaIncidentes.peek();
    }

    public IncidenteSeguridad atenderSiguienteIncidente() {

        if (colaIncidentes.isEmpty())
            return null;

        IncidenteSeguridad incidente = colaIncidentes.poll();

        incidente.setEstado("ATENDIDO");

        return incidente;
    }

    public int contarIncidentesPendientes() {

        return colaIncidentes.size();
    }

    public int consultarEspaciosDisponibles() {

        return capacidadMaxima - colaIncidentes.size();
    }

    public List<IncidenteSeguridad> listarIncidentes() {

        PriorityQueue<IncidenteSeguridad> copia =
                new PriorityQueue<>(
                        Comparator.comparingInt(IncidenteSeguridad::getPrioridad)
                );

        copia.addAll(colaIncidentes);

        List<IncidenteSeguridad> lista = new ArrayList<>();

        while (!copia.isEmpty()) {
            lista.add(copia.poll());
        }

        return lista;
    }

    public void predefinir() {

        colaIncidentes.offer(new IncidenteSeguridad(
                "INC001", "MALWARE", "Virus detectado", 3));

        colaIncidentes.offer(new IncidenteSeguridad(
                "INC002", "PHISHING", "Correo sospechoso", 1));

        colaIncidentes.offer(new IncidenteSeguridad(
                "INC003", "ACCESO_NO_AUTORIZADO", "Ingreso desconocido", 2));

        colaIncidentes.offer(new IncidenteSeguridad(
                "INC004", "FUGA_DATOS", "Robo de informacion", 1));

        colaIncidentes.offer(new IncidenteSeguridad(
                "INC005", "OTRO", "Actividad sospechosa", 4));

        colaIncidentes.offer(new IncidenteSeguridad(
                "INC006", "MALWARE", "Archivo infectado", 2));

        colaIncidentes.offer(new IncidenteSeguridad(
                "INC007", "PHISHING", "Enlace fraudulento", 5));

        colaIncidentes.offer(new IncidenteSeguridad(
                "INC008", "FUGA_DATOS", "Filtracion de contrasenas", 1));
    }

}

