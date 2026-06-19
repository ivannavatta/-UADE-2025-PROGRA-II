package Controller;

import Exceptions.*;
import Interfaces.LinkedListADT;
import Modulo.*;
import Implements.Dynamic.*;

public class SistemaController {
    private LinkedListADT<Terminal> terminales;
    private LinkedListADT<Micro>    micros;
    private LinkedListADT<Ruta> rutas;
    private LinkedListADT<Viaje>    viajes;

    private int contadorMicros;
    private int contadorTerminales;
    private int contadorRutas;
    private int contadorViajes;

    private DynamicGraphADT            mapaRutas;
    private DynamicSimpleDictionaryADT asignacionesPorMicro;
    private DynamicMultipleDictionaryADT viajesPorMicro;
    private DynamicPriorityQueueADT    colaViajes;

    public SistemaController() {
        this.terminales = new DynamicLinkedListADT<>();
        this.micros     = new DynamicLinkedListADT<>();
        this.rutas      = new DynamicLinkedListADT<>();
        this.viajes     = new DynamicLinkedListADT<>();

        this.contadorMicros      = 0;
        this.contadorTerminales  = 0;
        this.contadorRutas       = 0;
        this.contadorViajes      = 0;

        this.mapaRutas             = new DynamicGraphADT();
        this.asignacionesPorMicro  = new DynamicSimpleDictionaryADT();
        this.viajesPorMicro        = new DynamicMultipleDictionaryADT();
        this.colaViajes            = new DynamicPriorityQueueADT();
    }

    public int buscarIdPorCodigo(String codigo) {
        if (codigo == null) return -1;
        for (int i = 0; i < terminales.size(); i++) {
            Terminal t = terminales.get(i);
            if (t.getCodigo().equalsIgnoreCase(codigo.trim())) return i;
        }
        return -1;
    }

    public Micro buscarMicroPorPatente(String patente) {
        if (patente == null) return null;
        for (int i = 0; i < micros.size(); i++) {
            Micro m = micros.get(i);
            if (m.getPatente().equalsIgnoreCase(patente.trim())) return m;
        }
        return null;
    }

    private int buscarIdMicroPorPatente(String patente) {
        if (patente == null) return -1;
        for (int i = 0; i < micros.size(); i++) {
            if (micros.get(i).getPatente().equalsIgnoreCase(patente.trim())) return i;
        }
        return -1;
    }

    private Ruta buscarRutaDirecta(String codigoOrigen, String codigoDestino) {
        int idOrigen  = buscarIdPorCodigo(codigoOrigen);
        int idDestino = buscarIdPorCodigo(codigoDestino);
        if (idOrigen == -1 || idDestino == -1) return null;
        for (int i = 0; i < rutas.size(); i++) {
            Ruta r = rutas.get(i);
            if (r.esIgualA(idOrigen, idDestino)) return r;
        }
        return null;
    }

    private void imprimirRuta(Ruta r) {
        String origen  = terminales.get(r.getIdOrigen()).getCodigo();
        String destino = terminales.get(r.getIdDestino()).getCodigo();
        System.out.println("    -> " + origen + " -> " + destino);
    }

    // ==========================================
    //       MÓDULO 1: PLANIFICACIÓN DE RUTAS
    // ==========================================

    public void registrarTerminal(String codigo, String descripcion) {
        if (buscarIdPorCodigo(codigo) != -1)
            throw new InvalidTerminalException("Terminal repetida: " + codigo);

        int nuevoId = contadorTerminales;
        terminales.add(new Terminal(nuevoId, codigo, descripcion));
        contadorTerminales++;

        mapaRutas.addVertex(nuevoId);
    }

    public void conectarTerminales(String codigoOrigen, String codigoDestino, int pesoCamino) {
        int idOrigen  = buscarIdPorCodigo(codigoOrigen);
        int idDestino = buscarIdPorCodigo(codigoDestino);

        if (idOrigen == -1 || idDestino == -1)
            throw new InvalidTerminalException("Una o ambas terminales no existen.");
        if (buscarRutaDirecta(codigoOrigen, codigoDestino) != null)
            throw new InvalidRuteException("Ya existe ruta directa de " + codigoOrigen + " a " + codigoDestino);

        int nuevoIdRuta = contadorRutas;
        rutas.add(new Ruta(nuevoIdRuta, idOrigen, idDestino));
        contadorRutas++;

        mapaRutas.addEdge(idOrigen, idDestino, pesoCamino);
    }

    public void planificarViaje(String codigoOrigen, String codigoDestino, String patente, String fecha) {
        Ruta ruta = buscarRutaDirecta(codigoOrigen, codigoDestino);
        if (ruta == null)
            throw new InvalidRuteException("No existe ruta directa de " + codigoOrigen + " a " + codigoDestino);

        Micro m = buscarMicroPorPatente(patente);
        if (m == null)
            throw new InvalidMicroException("Micro no registrado: " + patente);

        int idViaje = contadorViajes;
        viajes.add(new Viaje(idViaje, ruta, m, fecha));
        contadorViajes++;

        int idMicro = buscarIdMicroPorPatente(patente);
        asignacionesPorMicro.add(idMicro, asignacionesPorMicro.get(idMicro) + 1);

        viajesPorMicro.add(idMicro, idViaje);

        colaViajes.add(idViaje, 2);
    }

    public void buscarYMostrarRutas(String codigoOrigen, String codigoDestino, int maxParadas) {
        int idOrigen  = buscarIdPorCodigo(codigoOrigen);
        int idDestino = buscarIdPorCodigo(codigoDestino);

        if (idOrigen == -1 || idDestino == -1) {
            System.out.println("  [DFS] Origen o destino no válidos.");
            return;
        }

        System.out.println("  Rutas de " + codigoOrigen + " a " + codigoDestino
                + " (máx " + maxParadas + " paradas):");

        boolean[] visitados   = new boolean[contadorTerminales];
        int[]     caminoActual = new int[contadorTerminales];

        dfsCaminos(idOrigen, idDestino, maxParadas, 0, visitados, caminoActual, 0);
    }

    private void dfsCaminos(int actual, int destino, int maxParadas, int paradas,
                            boolean[] visitados, int[] camino, int paso) {
        camino[paso] = actual;
        paso++;

        if (actual == destino) {
            System.out.print("    -> ");
            for (int i = 0; i < paso; i++) {
                System.out.print(terminales.get(camino[i]).getCodigo());
                if (i < paso - 1) System.out.print(" -> ");
            }
            System.out.println(" [Paradas: " + (paso - 2) + "]");
            return;
        }

        if (paradas >= maxParadas) return;

        visitados[actual] = true;
        for (int i = 0; i < rutas.size(); i++) {
            Ruta r = rutas.get(i);
            if (r.getIdOrigen() == actual && !visitados[r.getIdDestino()]) {
                dfsCaminos(r.getIdDestino(), destino, maxParadas, paradas + 1,
                        visitados, camino, paso);
            }
        }
        visitados[actual] = false;
    }

    public void identificarTerminalesDesconectadas() {
        System.out.println("  Terminales sin conexiones:");
        boolean hayAisladas = false;

        for (int i = 0; i < terminales.size(); i++) {
            boolean tieneConexion = false;
            for (int j = 0; j < terminales.size(); j++) {
                if (mapaRutas.existEdge(i, j) || mapaRutas.existEdge(j, i)) {
                    tieneConexion = true;
                    break;
                }
            }
            if (!tieneConexion) {
                System.out.println("    -> " + terminales.get(i).getCodigo()
                        + " - " + terminales.get(i).getDescripcion());
                hayAisladas = true;
            }
        }

        if (!hayAisladas) System.out.println("    Todas las terminales tienen al menos una conexión.");
    }

    // ==========================================
    //       MÓDULO 2: GESTIÓN DE FLOTA
    // ==========================================

    public void registrarMicro(String patente, String tipo) {
        if (buscarMicroPorPatente(patente) != null)
            throw new InvalidMicroException("Micro repetido: " + patente);

        int idMicro = contadorMicros;
        micros.add(new Micro(patente, tipo));
        contadorMicros++;

        asignacionesPorMicro.add(idMicro, 0);
    }

    public void cambiarDisponibilidadMicro(String patente, boolean disponible) {
        Micro m = buscarMicroPorPatente(patente);
        if (m == null)
            throw new InvalidMicroException("Micro no encontrado: " + patente);
        m.setDisponible(disponible);
    }

    public Micro[] obtenerMicrosAsignados() {
        int cant = 0;
        for (int i = 0; i < micros.size(); i++) {
            if (asignacionesPorMicro.get(i) > 0) cant++;
        }
        Micro[] resultado = new Micro[cant];
        int idx = 0;
        for (int i = 0; i < micros.size(); i++) {
            if (asignacionesPorMicro.get(i) > 0) resultado[idx++] = micros.get(i);
        }
        return resultado;
    }

    public Micro[] obtenerMicrosMasAsignados() {
        int maxVal = -1;
        for (int i = 0; i < micros.size(); i++) {
            int asig = asignacionesPorMicro.get(i);
            if (asig > maxVal) maxVal = asig;
        }
        if (maxVal <= 0) return new Micro[0];

        int cant = 0;
        for (int i = 0; i < micros.size(); i++) {
            if (asignacionesPorMicro.get(i) == maxVal) cant++;
        }
        Micro[] resultado = new Micro[cant];
        int idx = 0;
        for (int i = 0; i < micros.size(); i++) {
            if (asignacionesPorMicro.get(i) == maxVal) resultado[idx++] = micros.get(i);
        }
        return resultado;
    }

    public void mostrarViajesDeMicro(String patente) {
        int idMicro = buscarIdMicroPorPatente(patente);
        if (idMicro == -1)
            throw new InvalidMicroException("Micro no encontrado: " + patente);

        int[] idsViajes = viajesPorMicro.get(idMicro);
        System.out.println("  Viajes del micro [" + patente + "]:");
        for (int idViaje : idsViajes) {
            System.out.println("    -> " + viajes.get(idViaje));
        }
    }

    // ==========================================
    //     MÓDULO 3: PRIORIDAD EN VIAJES
    // ==========================================

    public void cambiarPrioridadViaje(int idViaje, int nuevaPrioridad) {
        if (idViaje < 0 || idViaje >= contadorViajes)
            throw new InvalididTravelException("ID de viaje inexistente: " + idViaje);

        viajes.get(idViaje).setPrioridad(nuevaPrioridad);

        DynamicPriorityQueueADT colaTemp = new DynamicPriorityQueueADT();
        while (!colaViajes.isEmpty()) {
            int vid = colaViajes.getElement();
            int pri = colaViajes.getPriority();
            colaViajes.remove();
            colaTemp.add(vid, (vid == idViaje) ? nuevaPrioridad : pri);
        }
        colaViajes = colaTemp;
    }

    public Viaje[] obtenerViajesOrdenadosPorPrioridad() {
        Viaje[] resultado = new Viaje[contadorViajes];
        DynamicPriorityQueueADT colaTemp = new DynamicPriorityQueueADT();
        int i = 0;
        while (!colaViajes.isEmpty()) {
            int idViaje = colaViajes.getElement();
            int pri     = colaViajes.getPriority();
            colaViajes.remove();
            resultado[i++] = viajes.get(idViaje);
            colaTemp.add(idViaje, pri);
        }
        colaViajes = colaTemp;
        return resultado;
    }

    // ==========================================
    //    MÓDULO 4: ANÁLISIS DE CONEXIONES
    // ==========================================

    public void listarTerminalesOperadas() {
        System.out.println("  Terminales operadas:");
        for (int i = 0; i < terminales.size(); i++) {
            Terminal t = terminales.get(i);
            System.out.println("    • [" + t.getCodigo() + "] " + t.getDescripcion());
        }
    }

    public void reportarTerminalesMayorMovimiento() {
        int[] salidas  = new int[contadorTerminales];
        int[] llegadas = new int[contadorTerminales];

        for (int i = 0; i < viajes.size(); i++) {
            Ruta r = viajes.get(i).getRutaAsignada();
            salidas[r.getIdOrigen()]++;
            llegadas[r.getIdDestino()]++;
        }

        int maxS = -1, maxL = -1;
        for (int i = 0; i < terminales.size(); i++) {
            if (salidas[i]  > maxS) maxS = salidas[i];
            if (llegadas[i] > maxL) maxL = llegadas[i];
        }

        System.out.println("  Mayor número de SALIDAS (" + maxS + "):");
        for (int i = 0; i < terminales.size(); i++) {
            if (salidas[i] == maxS && maxS > 0)
                System.out.println("    -> " + terminales.get(i).getCodigo());
        }

        System.out.println("  Mayor número de LLEGADAS (" + maxL + "):");
        for (int i = 0; i < terminales.size(); i++) {
            if (llegadas[i] == maxL && maxL > 0)
                System.out.println("    -> " + terminales.get(i).getCodigo());
        }
    }

    public void reportarTerminalesMasConectadas() {
        int[] conexiones = new int[contadorTerminales];
        for (int i = 0; i < rutas.size(); i++) {
            conexiones[rutas.get(i).getIdOrigen()]++;
        }

        int maxCon = -1;
        for (int i = 0; i < terminales.size(); i++) {
            if (conexiones[i] > maxCon) maxCon = conexiones[i];
        }

        System.out.println("  Terminales con más conexiones directas (" + maxCon + "):");
        for (int i = 0; i < terminales.size(); i++) {
            if (conexiones[i] == maxCon && maxCon > 0) {
                System.out.println("    -> " + terminales.get(i).getCodigo()
                        + " (" + terminales.get(i).getDescripcion() + ")");
            }
        }
    }

    // ==========================================
    //     MÓDULO 5: SIMULACIÓN Y REPORTES
    // ==========================================

    public void reprogramarViaje(int idViaje, String nuevaFecha, String nuevaPatente) {
        if (idViaje < 0 || idViaje >= contadorViajes)
            throw new InvalididTravelException("ID de viaje inválido: " + idViaje);

        Viaje v = viajes.get(idViaje);
        v.setFecha(nuevaFecha);

        if (nuevaPatente != null && !nuevaPatente.isEmpty()) {
            Micro nuevoMicro = buscarMicroPorPatente(nuevaPatente);
            if (nuevoMicro == null)
                throw new InvalidMicroException("Micro no encontrado: " + nuevaPatente);

            int idMicroViejo = buscarIdMicroPorPatente(v.getMicroAsignado().getPatente());
            asignacionesPorMicro.add(idMicroViejo,
                    asignacionesPorMicro.get(idMicroViejo) - 1);

            int idMicroNuevo = buscarIdMicroPorPatente(nuevaPatente);
            asignacionesPorMicro.add(idMicroNuevo,
                    asignacionesPorMicro.get(idMicroNuevo) + 1);

            viajesPorMicro.add(idMicroNuevo, idViaje);

            v.setMicroAsignado(nuevoMicro);
        }
    }

    public void generarReporteUtilizacionMicros() {
        int total = 0;
        for (int i = 0; i < micros.size(); i++) total += asignacionesPorMicro.get(i);

        System.out.println("  Utilización de flota:");
        if (total == 0) {
            System.out.println("    Ningún micro tiene viajes asignados.");
            return;
        }

        double promedio = (double) total / micros.size();
        System.out.println("    Promedio global: " + String.format("%.2f", promedio) + " viajes/micro");
        System.out.println();

        for (int i = 0; i < micros.size(); i++) {
            int asig  = asignacionesPorMicro.get(i);
            double pct = (asig * 100.0) / total;
            System.out.println("    -> [" + micros.get(i).getPatente() + "]: "
                    + asig + " viajes (" + String.format("%.1f", pct) + "%)");
        }
    }

    public void generarReporteRutasExtremas() {
        if (rutas.isEmpty()) {
            System.out.println("  No hay rutas registradas.");
            return;
        }

        int[] usos = new int[contadorRutas];
        for (int i = 0; i < viajes.size(); i++) {
            usos[viajes.get(i).getRutaAsignada().getIdRuta()]++;
        }

        int max = -1, min = Integer.MAX_VALUE;
        for (int i = 0; i < rutas.size(); i++) {
            int u = usos[i];
            if (u > max) max = u;
            if (u < min) min = u;
        }

        System.out.println("  Rutas más recorridas (" + max + " viajes):");
        for (int i = 0; i < rutas.size(); i++) {
            if (usos[i] == max) imprimirRuta(rutas.get(i));
        }

        System.out.println("  Rutas menos recorridas (" + min + " viajes):");
        for (int i = 0; i < rutas.size(); i++) {
            if (usos[i] == min) imprimirRuta(rutas.get(i));
        }
    }

    // ==========================================
    //    CARGA DE DATOS INICIALES
    // ==========================================

    public void cargarDatosDePrueba() {
        registrarTerminal("BUE", "Retiro Buenos Aires");
        registrarTerminal("COR", "Córdoba Capital");
        registrarTerminal("ROS", "Rosario Santa Fe");
        registrarTerminal("MDZ", "Mendoza Capital");
        registrarTerminal("SLA", "Salta Capital");
        registrarTerminal("TUC", "Tucumán");
        registrarTerminal("SFE", "Santa Fe Capital");
        registrarTerminal("NQN", "Neuquén");
        registrarTerminal("BRC", "Bariloche");
        registrarTerminal("POS", "Posadas Misiones");

        registrarMicro("AA-001-AA", "Cama");
        registrarMicro("AA-002-AA", "Semi-Cama");
        registrarMicro("AA-003-AA", "Ejecutivo");
        registrarMicro("BB-004-BB", "Cama");
        registrarMicro("BB-005-BB", "Semi-Cama");
        registrarMicro("BB-006-BB", "Ejecutivo");
        registrarMicro("CC-007-CC", "Cama");
        registrarMicro("CC-008-CC", "Semi-Cama");
        registrarMicro("CC-009-CC", "Ejecutivo");
        registrarMicro("DD-010-DD", "Cama");
        registrarMicro("DD-011-DD", "Semi-Cama");
        registrarMicro("DD-012-DD", "Ejecutivo");
        registrarMicro("EE-013-EE", "Cama");
        registrarMicro("EE-014-EE", "Semi-Cama");
        registrarMicro("EE-015-EE", "Ejecutivo");

        conectarTerminales("BUE", "ROS", 300);
        conectarTerminales("ROS", "COR", 400);
        conectarTerminales("COR", "BRC", 1200);
        conectarTerminales("BUE", "NQN", 1000);
        conectarTerminales("NQN", "BRC", 450);
        conectarTerminales("BUE", "MDZ", 1050);
        conectarTerminales("MDZ", "BRC", 1200);
        conectarTerminales("ROS", "SFE", 170);
        conectarTerminales("SLA", "TUC", 310);

        planificarViaje("BUE", "ROS",  "AA-001-AA", "2026-06-20");
        planificarViaje("ROS", "COR",  "AA-002-AA", "2026-06-21");
        planificarViaje("COR", "BRC",  "AA-003-AA", "2026-06-22");
        planificarViaje("BUE", "NQN",  "BB-004-BB", "2026-06-20");
        planificarViaje("NQN", "BRC",  "BB-005-BB", "2026-06-21");
        planificarViaje("BUE", "MDZ",  "AA-001-AA", "2026-06-23");
        planificarViaje("MDZ", "BRC",  "AA-002-AA", "2026-06-24");
        planificarViaje("ROS", "SFE",  "BB-006-BB", "2026-06-20");
        planificarViaje("SLA", "TUC",  "CC-007-CC", "2026-06-25");
        planificarViaje("BUE", "ROS",  "AA-001-AA", "2026-07-01");
        planificarViaje("ROS", "COR",  "CC-008-CC", "2026-07-02");
        planificarViaje("COR", "BRC",  "CC-009-CC", "2026-07-03");
        planificarViaje("BUE", "NQN",  "DD-010-DD", "2026-07-01");
        planificarViaje("NQN", "BRC",  "DD-011-DD", "2026-07-02");
        planificarViaje("BUE", "MDZ",  "DD-012-DD", "2026-07-05");
        planificarViaje("ROS", "SFE",  "EE-013-EE", "2026-07-02");
        planificarViaje("SLA", "TUC",  "EE-014-EE", "2026-07-06");
        planificarViaje("BUE", "ROS",  "EE-015-EE", "2026-07-10");
        planificarViaje("ROS", "COR",  "AA-002-AA", "2026-07-11");
        planificarViaje("BUE", "NQN",  "BB-004-BB", "2026-07-12");
    }
}
