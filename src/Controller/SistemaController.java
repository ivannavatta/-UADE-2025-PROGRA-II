package Controller;

import Exceptions.*;
import Modulo.*;
import Implements.Dynamic.*;

public class SistemaController {

    private Terminal[] terminales;
    private Micro[] micros;
    private Ruta[] rutas;
    private Viaje[] viajes;

    private DynamicLinkedListADT listaIdTerminales;
    private DynamicLinkedListADT listaIdMicros;
    private DynamicLinkedListADT listaIdRutas;
    private DynamicLinkedListADT listaIdViajes;

    private int contadorMicros;
    private int contadorTerminales;
    private int contadorRutas;
    private int contadorViajes;

    private DynamicGraphADT mapaRutas;
    private DynamicSimpleDictionaryADT asignacionesPorMicro;
    private DynamicMultipleDictionaryADT viajesPorMicro;
    private DynamicPriorityQueueADT colaViajes;

    public SistemaController() {
        this.terminales = new Terminal[10];
        this.micros = new Micro[15];
        this.rutas = new Ruta[200];
        this.viajes = new Viaje[20];

        this.listaIdTerminales = new DynamicLinkedListADT();
        this.listaIdMicros = new DynamicLinkedListADT();
        this.listaIdRutas = new DynamicLinkedListADT();
        this.listaIdViajes = new DynamicLinkedListADT();

        this.contadorMicros = 0;
        this.contadorTerminales = 0;
        this.contadorRutas = 0;
        this.contadorViajes = 0;

        this.mapaRutas = new DynamicGraphADT();
        this.asignacionesPorMicro = new DynamicSimpleDictionaryADT();
        this.viajesPorMicro = new DynamicMultipleDictionaryADT();
        this.colaViajes = new DynamicPriorityQueueADT();
    }


    public int buscarIdPorCodigo(String codigo) {
        if (codigo == null) return -1;
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int idTerminal = listaIdTerminales.get(i);
            Terminal t = terminales[idTerminal];
            if (t != null && t.getCodigo().equalsIgnoreCase(codigo.trim())) return idTerminal;
        }
        return -1;
    }

    public Micro buscarMicroPorPatente(String patente) {
        if (patente == null) return null;
        for (int i = 0; i < listaIdMicros.size(); i++) {
            int idMicro = listaIdMicros.get(i);
            Micro m = micros[idMicro];
            if (m != null && m.getPatente().equalsIgnoreCase(patente.trim())) return m;
        }
        return null;
    }

    private int buscarIdMicroPorPatente(String patente) {
        if (patente == null) return -1;
        for (int i = 0; i < listaIdMicros.size(); i++) {
            int idMicro = listaIdMicros.get(i);
            Micro m = micros[idMicro];
            if (m != null && m.getPatente().equalsIgnoreCase(patente.trim())) return idMicro;
        }
        return -1;
    }

    private Ruta buscarRutaDirecta(String codigoOrigen, String codigoDestino) {
        int idOrigen = buscarIdPorCodigo(codigoOrigen);
        int idDestino = buscarIdPorCodigo(codigoDestino);
        if (idOrigen == -1 || idDestino == -1) return null;

        for (int i = 0; i < listaIdRutas.size(); i++) {
            int idRuta = listaIdRutas.get(i);
            Ruta r = rutas[idRuta];
            if (r != null && r.esIgualA(idOrigen, idDestino)) return r;
        }
        return null;
    }

    // ==========================================
    //       MÓDULO 1: PLANIFICACIÓN DE RUTAS
    // ==========================================

    public void registrarTerminal(String codigo, String descripcion) {
        if (buscarIdPorCodigo(codigo) != -1) throw new InvalidTerminalException("Terminal repetida: " + codigo);
        int nuevoId = contadorTerminales;
        terminales[nuevoId] = new Terminal(nuevoId, codigo, descripcion);
        listaIdTerminales.add(nuevoId);
        contadorTerminales++;
        mapaRutas.addVertex(nuevoId);
    }

    public void conectarTerminales(String codigoOrigen, String codigoDestino, int pesoCamino) {
        int idOrigen = buscarIdPorCodigo(codigoOrigen);
        int idDestino = buscarIdPorCodigo(codigoDestino);
        if (idOrigen == -1 || idDestino == -1) throw new InvalidTerminalException("Terminales no existen.");
        if (buscarRutaDirecta(codigoOrigen, codigoDestino) != null) throw new InvalidRuteException("Ruta ya existente.");

        int nuevoIdRuta = contadorRutas;
        rutas[nuevoIdRuta] = new Ruta(nuevoIdRuta, idOrigen, idDestino);
        listaIdRutas.add(nuevoIdRuta);
        contadorRutas++;
        mapaRutas.addEdge(idOrigen, idDestino, pesoCamino);
    }

    public void planificarViaje(String codigoOrigen, String codigoDestino, String patente, String fecha) {
        Ruta ruta = buscarRutaDirecta(codigoOrigen, codigoDestino);
        if (ruta == null) throw new InvalidRuteException("No existe ruta directa.");
        Micro m = buscarMicroPorPatente(patente);
        if (m == null) throw new InvalidMicroException("Micro no registrado: " + patente);

        int idViaje = contadorViajes;
        viajes[idViaje] = new Viaje(idViaje, ruta, m, fecha);
        listaIdViajes.add(idViaje);
        contadorViajes++;

        int idMicro = buscarIdMicroPorPatente(patente);
        int nuevasAsignaciones = asignacionesPorMicro.get(idMicro) + 1;
        asignacionesPorMicro.add(idMicro, nuevasAsignaciones);
        m.incrementarAsignaciones();

        viajesPorMicro.add(idMicro, idViaje);
        colaViajes.add(idViaje, 2);
    }

    public void buscarYMostrarRutas(String codigoOrigen, String codigoDestino, int maxParadas) {
        int idOrigen = buscarIdPorCodigo(codigoOrigen);
        int idDestino = buscarIdPorCodigo(codigoDestino);
        if (idOrigen == -1 || idDestino == -1) {
            System.out.println("  [DFS] Origen o Destino no válidos.");
            return;
        }
        System.out.println("  Rutas encontradas desde " + codigoOrigen + " hasta " + codigoDestino + " (Máx " + maxParadas + " paradas):");
        boolean[] visitados = new boolean[contadorTerminales];
        int[] caminoActual = new int[contadorTerminales];
        dfsCaminos(idOrigen, idDestino, maxParadas, 0, visitados, caminoActual, 0);
    }

    private void dfsCaminos(int actual, int destino, int maxParadas, int paradas, boolean[] visitados, int[] camino, int paso) {
        camino[paso] = actual;
        paso++;

        if (actual == destino) {
            System.out.print("    -> ");
            for (int i = 0; i < paso; i++) {
                System.out.print(terminales[camino[i]].getCodigo() + (i == paso - 1 ? "" : " -> "));
            }
            System.out.println(" [Paradas: " + (paso - 2) + "]");
            return;
        }

        if (paradas >= maxParadas) return;

        visitados[actual] = true;
        for (int i = 0; i < listaIdRutas.size(); i++) {
            Ruta r = rutas[listaIdRutas.get(i)];
            if (r.getIdOrigen() == actual && !visitados[r.getIdDestino()]) {
                dfsCaminos(r.getIdDestino(), destino, maxParadas, paradas + 1, visitados, camino, paso);
            }
        }
        visitados[actual] = false;
    }

    // ==========================================
    //       MÓDULO 2: GESTIÓN DE FLOTA
    // ==========================================

    public void registrarMicro(String patente, String tipo) {
        if (buscarMicroPorPatente(patente) != null) throw new InvalidMicroException("Micro repetido: " + patente);
        int idMicro = contadorMicros;
        micros[idMicro] = new Micro(patente, tipo);
        listaIdMicros.add(idMicro);
        contadorMicros++;
        asignacionesPorMicro.add(idMicro, 0);
    }



    public Micro[] obtenerMicrosAsignados() {
        int cant = 0;
        for (int i = 0; i < listaIdMicros.size(); i++) {
            if (asignacionesPorMicro.get(listaIdMicros.get(i)) > 0) cant++;
        }
        Micro[] resultado = new Micro[cant];
        int idx = 0;
        for (int i = 0; i < listaIdMicros.size(); i++) {
            int id = listaIdMicros.get(i);
            if (asignacionesPorMicro.get(id) > 0) {
                resultado[idx++] = micros[id];
            }
        }
        return resultado;
    }

    public Micro[] obtenerMicrosMasAsignados() {
        int maxVal = -1;
        for (int i = 0; i < listaIdMicros.size(); i++) {
            int asig = asignacionesPorMicro.get(listaIdMicros.get(i));
            if (asig > maxVal) maxVal = asig;
        }
        int cant = 0;
        for (int i = 0; i < listaIdMicros.size(); i++) {
            if (asignacionesPorMicro.get(listaIdMicros.get(i)) == maxVal && maxVal > 0) cant++;
        }
        Micro[] resultado = new Micro[cant];
        int idx = 0;
        for (int i = 0; i < listaIdMicros.size(); i++) {
            int id = listaIdMicros.get(i);
            if (asignacionesPorMicro.get(id) == maxVal && maxVal > 0) {
                resultado[idx++] =micros[id];
            }
        }
        return resultado;
    }

    // ==========================================
    //     MÓDULO 3: PRIORIDAD EN VIAJES
    // ==========================================

    public void cambiarPrioridadViaje(int idViaje, int nuevaPrioridad) {
        if (idViaje < 0 || idViaje >= contadorViajes) throw new InvalididTravelException("ID inexistente.");
        viajes[idViaje].setPrioridad(nuevaPrioridad);

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
            int pri = colaViajes.getPriority();
            colaViajes.remove();
            resultado[i++] = viajes[idViaje];
            colaTemp.add(idViaje, pri);
        }
        colaViajes = colaTemp;
        return resultado;
    }

    // ==========================================
    //    MÓDULO 4: ANÁLISIS DE CONEXIONES
    // ==========================================

    public void listarTerminalesOperadas() {
        System.out.println("  Terminales Operadas:");
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            Terminal t = terminales[listaIdTerminales.get(i)];
            System.out.println("    • [" + t.getCodigo() + "] " + t.getDescripcion());
        }
    }

    public void reportarTerminalesMayorMovimiento() {
        int[] salidas = new int[contadorTerminales];
        int[] llegadas = new int[contadorTerminales];
        for (int i = 0; i < listaIdViajes.size(); i++) {
            Ruta r = viajes[listaIdViajes.get(i)].getRutaAsignada();
            salidas[r.getIdOrigen()]++;
            llegadas[r.getIdDestino()]++;
        }
        int maxS = -1, maxL = -1;
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int id = listaIdTerminales.get(i);
            if (salidas[id] > maxS) maxS = salidas[id];
            if (llegadas[id] > maxL) maxL = llegadas[id];
        }
        System.out.println("  Mayor número de SALIDAS (" + maxS + "):");
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int id = listaIdTerminales.get(i);
            if (salidas[id] == maxS && maxS > 0) System.out.println("    -> " + terminales[id].getCodigo());
        }
        System.out.println("  Mayor número de LLEGADAS (" + maxL + "):");
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int id = listaIdTerminales.get(i);
            if (llegadas[id] == maxL && maxL > 0) System.out.println("    -> " + terminales[id].getCodigo());
        }
    }

    public void reportarTerminalesMasConectadas() {
        int[] conexiones = new int[contadorTerminales];
        for (int i = 0; i < listaIdRutas.size(); i++) {
            Ruta r = rutas[listaIdRutas.get(i)];
            conexiones[r.getIdOrigen()]++;
        }
        int maxCon = -1;
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            if (conexiones[listaIdTerminales.get(i)] > maxCon) maxCon = conexiones[listaIdTerminales.get(i)];
        }
        System.out.println("  Terminales con más conexiones directas (" + maxCon + "):");
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int id = listaIdTerminales.get(i);
            if (conexiones[id] == maxCon && maxCon > 0) {
                System.out.println("    -> " + terminales[id].getCodigo() + " (" + terminales[id].getDescripcion() + ")");
            }
        }
    }

    // ==========================================
    //     MÓDULO 5: SIMULACIÓN Y REPROGRAMACIÓN
    // ==========================================

    public void reprogramarViaje(int idViaje, String nuevaFecha, String nuevaPatente) {
        if (idViaje < 0 || idViaje >= contadorViajes) throw new InvalididTravelException("ID de viaje inválido.");
        Viaje v = viajes[idViaje];
        v.setFecha(nuevaFecha);

        Micro nuevoMicro = buscarMicroPorPatente(nuevaPatente);
        if (nuevoMicro != null) {
            int idMicroViejo = buscarIdMicroPorPatente(v.getMicroAsignado().getPatente());
            int idMicroNuevo = buscarIdMicroPorPatente(nuevaPatente);

            int asigViejo = asignacionesPorMicro.get(idMicroViejo) - 1;
            asignacionesPorMicro.add(idMicroViejo, asigViejo);

            int asigNuevo = asignacionesPorMicro.get(idMicroNuevo) + 1;
            asignacionesPorMicro.add(idMicroNuevo, asigNuevo);

            v.setMicroAsignado(nuevoMicro);
            viajesPorMicro.add(idMicroNuevo, idViaje);
        }
    }

    public void generarReporteUtilizacionMicros() {
        int total = 0;
        for (int i = 0; i < listaIdMicros.size(); i++) total += asignacionesPorMicro.get(listaIdMicros.get(i));
        System.out.println("  Utilización de Flota:");
        if (total == 0) return;
        for (int i = 0; i < listaIdMicros.size(); i++) {
            int id = listaIdMicros.get(i);
            double porc = (asignacionesPorMicro.get(id) * 100.0) / total;
            System.out.println("    -> [" + micros[id].getPatente() + "]: " + porc + "%");
        }
    }

    public void generarReporteRutasExtremas() {
        int[] usos = new int[contadorRutas];
        for (int i = 0; i < listaIdViajes.size(); i++) {
            usos[viajes[listaIdViajes.get(i)].getRutaAsignada().getIdRuta()]++;
        }
        int max = -1, min = 99999;
        for (int i = 0; i < listaIdRutas.size(); i++) {
            int u = usos[listaIdRutas.get(i)];
            if (u > max) max = u;
            if (u < min) min = u;
        }
        System.out.println("  Rutas Máximas Recorridas (" + max + " viajes):");
        for (int i = 0; i < listaIdRutas.size(); i++) {
            int id = listaIdRutas.get(i);
            if (usos[id] == max) {
                Ruta r = rutas[id];
                String origen = terminales[r.getIdOrigen()].getCodigo();
                String destino = terminales[r.getIdDestino()].getCodigo();
                System.out.println("    -> " + origen + " -> " + destino);
            }
        }
        System.out.println("  Rutas Mínimas Recorridas (" + min + " viajes):");
        for (int i = 0; i < listaIdRutas.size(); i++) {
            int id = listaIdRutas.get(i);
            if (usos[id] == min) {
                Ruta r = rutas[id];
                String origen = terminales[r.getIdOrigen()].getCodigo();
                String destino = terminales[r.getIdDestino()].getCodigo();
                System.out.println("    -> " + origen + " -> " + destino);
            }
        }
    }

    // ==========================================
    //    ESCENARIO: CARGA DE DATOS OBLIGATORIOS
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

        planificarViaje("BUE", "ROS", "AA-001-AA", "2026-06-20");
        planificarViaje("ROS", "COR", "AA-002-AA", "2026-06-21");
        planificarViaje("COR", "BRC", "AA-003-AA", "2026-06-22");
        planificarViaje("BUE", "NQN", "BB-004-BB", "2026-06-20");
        planificarViaje("NQN", "BRC", "BB-005-BB", "2026-06-21");
        planificarViaje("BUE", "MDZ", "AA-001-AA", "2026-06-23");
        planificarViaje("MDZ", "BRC", "AA-002-AA", "2026-06-24");
        planificarViaje("ROS", "SFE", "BB-006-BB", "2026-06-20");
        planificarViaje("SLA", "TUC", "CC-007-CC", "2026-06-25");

        planificarViaje("BUE", "ROS", "AA-001-AA", "2026-07-01");
        planificarViaje("ROS", "COR", "CC-008-CC", "2026-07-02");
        planificarViaje("COR", "BRC", "CC-009-CC", "2026-07-03");
        planificarViaje("BUE", "NQN", "DD-010-DD", "2026-07-01");
        planificarViaje("NQN", "BRC", "DD-011-DD", "2026-07-02");
        planificarViaje("BUE", "MDZ", "DD-012-DD", "2026-07-05");
        planificarViaje("ROS", "SFE", "EE-013-EE", "2026-07-02");
        planificarViaje("SLA", "TUC", "EE-014-EE", "2026-07-06");
        planificarViaje("BUE", "ROS", "EE-015-EE", "2026-07-10");
        planificarViaje("ROS", "COR", "AA-002-AA", "2026-07-11");
        planificarViaje("BUE", "NQN", "BB-004-BB", "2026-07-12");
    }
}
