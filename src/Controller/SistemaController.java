package Controller;

import Implements.Dynamic.*;
import Exceptions.*;
import Modulo.Micro;
import Modulo.Ruta;
import Modulo.Terminal;
import Modulo.Viaje;

public class SistemaController {

    private Micro[] flotaMicros;
    private Terminal[] terminales;
    private Ruta[] rutas;
    private Viaje[] viajes;
    private int contadorMicros;
    private int contadorTerminales;
    private int contadorRutas;
    private int contadorViajes;


    private DynamicGraphADT mapaRutas;

    private DynamicLinkedListADT listaIdTerminales;
    private DynamicLinkedListADT listaIdRutas;

    private DynamicSimpleDictionaryADT asignacionesPorMicro;

    private DynamicMultipleDictionaryADT viajesPorMicro;

    private DynamicPriorityQueueADT colaViajes;


    public SistemaController() {
        this.flotaMicros = new Micro[30];
        this.terminales = new Terminal[20];
        this.rutas = new Ruta[50];
        this.viajes = new Viaje[50];
        this.contadorMicros = 0;
        this.contadorTerminales = 0;
        this.contadorRutas = 0;
        this.contadorViajes = 0;

        this.mapaRutas = new DynamicGraphADT();
        this.listaIdTerminales = new DynamicLinkedListADT();
        this.listaIdRutas = new DynamicLinkedListADT();
        this.asignacionesPorMicro = new DynamicSimpleDictionaryADT();
        this.viajesPorMicro = new DynamicMultipleDictionaryADT();
        this.colaViajes = new DynamicPriorityQueueADT();
    }


    public void cargarDatosDePrueba() {
        registrarTerminal("BUE", "Terminal de Retiro (Buenos Aires)");
        registrarTerminal("COR", "Terminal de Córdoba Capital");
        registrarTerminal("ROS", "Terminal Mariano Moreno (Rosario)");
        registrarTerminal("MDZ", "Terminal del Sol (Mendoza)");
        registrarTerminal("SLA", "Terminal de Salta Capital");
        registrarTerminal("TUC", "Terminal de San Miguel de Tucumán");
        registrarTerminal("SFE", "Terminal de Santa Fe Capital");
        registrarTerminal("NQN", "Terminal de Neuquén");
        registrarTerminal("BRC", "Terminal de San Carlos de Bariloche");
        registrarTerminal("POS", "Terminal de Posadas (Misiones)");

        conectarTerminales("BUE", "ROS", 300);
        conectarTerminales("ROS", "COR", 400);
        conectarTerminales("COR", "SLA", 850);
        conectarTerminales("SLA", "TUC", 310);
        conectarTerminales("BUE", "MDZ", 1050);
        conectarTerminales("MDZ", "NQN", 800);
        conectarTerminales("NQN", "BRC", 430);
        conectarTerminales("BUE", "SFE", 470);
        conectarTerminales("SFE", "POS", 700);
        conectarTerminales("COR", "MDZ", 600);

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

        planificarViaje("BUE", "ROS", "AA-001-AA", "2026-07-01");
        planificarViaje("ROS", "COR", "AA-002-AA", "2026-07-02");
        planificarViaje("COR", "SLA", "AA-003-AA", "2026-07-03");
        planificarViaje("SLA", "TUC", "BB-004-BB", "2026-07-04");
        planificarViaje("BUE", "MDZ", "BB-005-BB", "2026-07-05");
        planificarViaje("MDZ", "NQN", "BB-006-BB", "2026-07-06");
        planificarViaje("NQN", "BRC", "CC-007-CC", "2026-07-07");
        planificarViaje("BUE", "SFE", "CC-008-CC", "2026-07-08");
        planificarViaje("SFE", "POS", "CC-009-CC", "2026-07-09");
        planificarViaje("COR", "MDZ", "DD-010-DD", "2026-07-10");
        planificarViaje("BUE", "ROS", "DD-011-DD", "2026-07-11");
        planificarViaje("ROS", "COR", "DD-012-DD", "2026-07-12");
        planificarViaje("COR", "SLA", "EE-013-EE", "2026-07-13");
        planificarViaje("BUE", "MDZ", "EE-014-EE", "2026-07-14");
        planificarViaje("MDZ", "NQN", "EE-015-EE", "2026-07-15");
        planificarViaje("NQN", "BRC", "AA-001-AA", "2026-07-16");
        planificarViaje("BUE", "SFE", "AA-002-AA", "2026-07-17");
        planificarViaje("SFE", "POS", "AA-003-AA", "2026-07-18");
        planificarViaje("COR", "MDZ", "BB-004-BB", "2026-07-19");
        planificarViaje("BUE", "ROS", "BB-005-BB", "2026-07-20");
    }


    public int buscarIdPorCodigo(String codigo) {
        if (codigo == null) return -1;
        for (int i = 0; i < contadorTerminales; i++) {
            if (terminales[i].getCodigo().equalsIgnoreCase(codigo.trim())) {
                return terminales[i].getId();
            }
        }
        return -1;
    }

    private Ruta buscarRutaDirecta(String codigoOrigen, String codigoDestino) {
        int idOrigen = buscarIdPorCodigo(codigoOrigen);
        int idDestino = buscarIdPorCodigo(codigoDestino);
        if (idOrigen == -1 || idDestino == -1) return null;
        for (int i = 0; i < contadorRutas; i++) {
            if (rutas[i].esIgualA(idOrigen, idDestino)) return rutas[i];
        }
        return null;
    }

    public Micro buscarMicroPorPatente(String patente) {
        if (patente == null) return null;
        for (int i = 0; i < contadorMicros; i++) {
            if (flotaMicros[i].getPatente().equalsIgnoreCase(patente.trim())) {
                return flotaMicros[i];
            }
        }
        return null;
    }

    // Devuelve el índice del micro en flotaMicros (necesario como clave en los TDAs)
    private int buscarIndiceMicro(String patente) {
        if (patente == null) return -1;
        for (int i = 0; i < contadorMicros; i++) {
            if (flotaMicros[i].getPatente().equalsIgnoreCase(patente.trim())) {
                return i;
            }
        }
        return -1;
    }

    private void imprimirDatosRuta(Ruta r) {
        String codOrigen = "N/D";
        String codDestino = "N/D";
        for (int i = 0; i < contadorTerminales; i++) {
            if (terminales[i].getId() == r.getIdOrigen()) codOrigen = terminales[i].getCodigo();
            if (terminales[i].getId() == r.getIdDestino()) codDestino = terminales[i].getCodigo();
        }
        System.out.println("    -> Ruta ID [" + r.getIdRuta() + "]: " + codOrigen + " -> " + codDestino);
    }

    public void registrarTerminal(String codigo, String descripcion) {
        if (buscarIdPorCodigo(codigo) != -1) {
            throw new InvalidTerminalException("Ya existe una terminal registrada con el código: " + codigo);
        }

        int nuevoId = contadorTerminales;
        terminales[contadorTerminales] = new Terminal(nuevoId, codigo, descripcion);
        contadorTerminales++;

        mapaRutas.addVertex(nuevoId);
        listaIdTerminales.add(nuevoId);
    }

    public void conectarTerminales(String codigoOrigen, String codigoDestino, int pesoCamino) {
        int idOrigen = buscarIdPorCodigo(codigoOrigen);
        int idDestino = buscarIdPorCodigo(codigoDestino);

        if (idOrigen == -1) throw new InvalidTerminalException("La terminal de origen '" + codigoOrigen + "' no existe.");
        if (idDestino == -1) throw new InvalidTerminalException("La terminal de destino '" + codigoDestino + "' no existe.");

        for (int i = 0; i < contadorRutas; i++) {
            if (rutas[i].esIgualA(idOrigen, idDestino)) {
                throw new InvalidRuteException("Ya existe una ruta directa de " + codigoOrigen + " hacia " + codigoDestino);
            }
        }


        int nuevoIdRuta = contadorRutas;
        rutas[contadorRutas] = new Ruta(nuevoIdRuta, idOrigen, idDestino);
        contadorRutas++;

        mapaRutas.addEdge(idOrigen, idDestino, pesoCamino);
        listaIdRutas.add(nuevoIdRuta);
    }

    public void planificarViaje(String codigoOrigen, String codigoDestino, String patente, String fecha) {
        Ruta ruta = buscarRutaDirecta(codigoOrigen, codigoDestino);
        if (ruta == null) {
            throw new InvalidRuteException("No existe una ruta directa entre " + codigoOrigen + " y " + codigoDestino + ".");
        }

        Micro microReal = buscarMicroPorPatente(patente);
        if (microReal == null) {
            throw new InvalidMicroException("El micro con patente " + patente + " no está registrado en la flota.");
        }

        int idViaje = contadorViajes;
        viajes[contadorViajes] = new Viaje(idViaje, ruta, microReal, fecha);
        contadorViajes++;

        int idMicro = buscarIndiceMicro(patente);

        int asignacionesActuales = asignacionesPorMicro.get(idMicro);
        asignacionesPorMicro.add(idMicro, asignacionesActuales + 1);

        viajesPorMicro.add(idMicro, idViaje);

        colaViajes.add(idViaje, 2);
    }

    public String[] buscarRutasPosibles(String codigoOrigen, String codigoDestino, int maxParadas) {
        int idOrigen = buscarIdPorCodigo(codigoOrigen);
        int idDestino = buscarIdPorCodigo(codigoDestino);

        if (idOrigen == -1 || idDestino == -1) {
            throw new InvalidTerminalException("Terminal de origen o destino no encontradas.");
        }

        String[] caminosEncontrados = new String[250];
        int[] contadorCaminos = new int[1];
        boolean[] visitados = new boolean[contadorTerminales];
        int[] caminoActual = new int[contadorTerminales];

        caminoActual[0] = idOrigen;
        visitados[idOrigen] = true;

        ejecutarDFS(idOrigen, idDestino, 0, maxParadas, visitados, caminoActual, 1, caminosEncontrados, contadorCaminos);

        String[] resultadoFinal = new String[contadorCaminos[0]];
        for (int i = 0; i < contadorCaminos[0]; i++) {
            resultadoFinal[i] = caminosEncontrados[i];
        }
        return resultadoFinal;
    }

    private void ejecutarDFS(int nodoActual, int nodoDestino, int paradasActuales, int maxParadas,
                             boolean[] visitados, int[] caminoActual, int largoCamino,
                             String[] resultados, int[] contadorResultados) {
        if (nodoActual == nodoDestino) {
            String strCamino = "";
            for (int i = 0; i < largoCamino; i++) {
                strCamino += terminales[caminoActual[i]].getCodigo();
                if (i < largoCamino - 1) strCamino += " -> ";
            }
            if (contadorResultados[0] < resultados.length) {
                resultados[contadorResultados[0]] = strCamino;
                contadorResultados[0]++;
            }
            return;
        }

        if (paradasActuales >= maxParadas) return;

        for (int v = 0; v < contadorTerminales; v++) {
            if (!visitados[v] && mapaRutas.existEdge(nodoActual, v)) {
                visitados[v] = true;
                caminoActual[largoCamino] = v;
                ejecutarDFS(v, nodoDestino, paradasActuales + 1, maxParadas, visitados, caminoActual, largoCamino + 1, resultados, contadorResultados);
                visitados[v] = false; // backtracking
            }
        }
    }

    public Terminal[] identificarTerminalesDesconectadas() {
        Terminal[] auxDesconectadas = new Terminal[contadorTerminales];
        int cantidadAisladas = 0;

        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int idTerminal = listaIdTerminales.get(i);
            boolean tieneConexiones = false;

            for (int j = 0; j < listaIdTerminales.size(); j++) {
                int idOtra = listaIdTerminales.get(j);
                if (mapaRutas.existEdge(idTerminal, idOtra) || mapaRutas.existEdge(idOtra, idTerminal)) {
                    tieneConexiones = true;
                    break;
                }
            }

            if (!tieneConexiones) {
                auxDesconectadas[cantidadAisladas] = terminales[idTerminal];
                cantidadAisladas++;
            }
        }

        Terminal[] resultado = new Terminal[cantidadAisladas];
        for (int k = 0; k < cantidadAisladas; k++) resultado[k] = auxDesconectadas[k];
        return resultado;
    }

    public void buscarYMostrarRutas(String codigoOrigen, String codigoDestino, int maxParadas) {
        String[] rutas = buscarRutasPosibles(codigoOrigen, codigoDestino, maxParadas);
        System.out.println("  Buscando rutas de " + codigoOrigen + " a " + codigoDestino + ":");
        if (rutas.length == 0) {
            System.out.println("    No se encontraron rutas con el límite de paradas establecido.");
        } else {
            for (int i = 0; i < rutas.length; i++) {
                System.out.println("    Ruta #" + (i + 1) + ": " + rutas[i]);
            }
        }
    }

    // ==========================================
    //       MÓDULO 2: GESTIÓN DE FLOTA
    // ==========================================

    public void registrarMicro(String patente, String tipo) {
        if (buscarMicroPorPatente(patente) != null) {
            throw new InvalidMicroException("Ya existe un micro registrado con la patente: " + patente);
        }

        int idMicro = contadorMicros;
        flotaMicros[contadorMicros] = new Micro(patente, tipo);
        contadorMicros++;

        asignacionesPorMicro.add(idMicro, 0);
    }

    public void asignarMicroAViaje(int idViaje, String patente) {
        if (idViaje < 0 || idViaje >= contadorViajes) {
            throw new InvalididTravelException("El ID de viaje suministrado no existe en el sistema.");
        }
        Micro micro = buscarMicroPorPatente(patente);
        if (micro == null) {
            throw new InvalidMicroException("El micro con patente " + patente + " no está registrado.");
        }

        viajes[idViaje].setMicroAsignado(micro);

        int idMicro = buscarIndiceMicro(patente);
        int asignacionesActuales = asignacionesPorMicro.get(idMicro);
        asignacionesPorMicro.add(idMicro, asignacionesActuales + 1);
        viajesPorMicro.add(idMicro, idViaje);
    }

    public void cambiarDisponibilidadMicro(String patente, boolean nuevaDisponibilidad) {
        Micro micro = buscarMicroPorPatente(patente);
        if (micro == null) {
            throw new InvalidMicroException("El micro con patente " + patente + " no existe.");
        }
        micro.setDisponible(nuevaDisponibilidad);
    }

    public Micro[] obtenerMicrosAsignados() {
        Micro[] aux = new Micro[contadorMicros];
        int count = 0;
        for (int i = 0; i < contadorMicros; i++) {
            if (asignacionesPorMicro.get(i) > 0) {
                aux[count] = flotaMicros[i];
                count++;
            }
        }
        Micro[] resultado = new Micro[count];
        for (int k = 0; k < count; k++) resultado[k] = aux[k];
        return resultado;
    }

    public Micro[] obtenerMicrosMasAsignados() {
        if (contadorMicros == 0) return new Micro[0];

        int maxAsignaciones = 0;
        for (int i = 0; i < contadorMicros; i++) {
            int a = asignacionesPorMicro.get(i);
            if (a > maxAsignaciones) maxAsignaciones = a;
        }

        if (maxAsignaciones == 0) return new Micro[0];

        Micro[] aux = new Micro[contadorMicros];
        int count = 0;
        for (int i = 0; i < contadorMicros; i++) {
            if (asignacionesPorMicro.get(i) == maxAsignaciones) {
                aux[count] = flotaMicros[i];
                count++;
            }
        }

        Micro[] resultado = new Micro[count];
        for (int k = 0; k < count; k++) resultado[k] = aux[k];
        return resultado;
    }

    public int[] obtenerViajesDeMicro(String patente) {
        int idMicro = buscarIndiceMicro(patente);
        if (idMicro == -1) {
            throw new InvalidMicroException("El micro con patente " + patente + " no existe.");
        }
        return viajesPorMicro.get(idMicro);
    }

    // ==========================================
    //     MÓDULO 3: PRIORIDAD EN VIAJES
    // ==========================================

    public void cambiarPrioridadViaje(int idViaje, int nuevaPrioridad) {
        if (idViaje < 0 || idViaje >= contadorViajes) {
            throw new InvalididTravelException("El ID de viaje suministrado no existe en el sistema.");
        }
        if (nuevaPrioridad < 1 || nuevaPrioridad > 3) {
            throw new IllegalArgumentException("Prioridad inválida. Debe ser: 1 (Alta), 2 (Media) o 3 (Baja).");
        }

        viajes[idViaje].setPrioridad(nuevaPrioridad);

        DynamicPriorityQueueADT colaTemp = new DynamicPriorityQueueADT();
        while (!colaViajes.isEmpty()) {
            int vid = colaViajes.getElement();
            int pri = colaViajes.getPriority();
            colaViajes.remove();
            if (vid == idViaje) {
                colaTemp.add(vid, nuevaPrioridad);
            } else {
                colaTemp.add(vid, pri);
            }
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
            resultado[i] = viajes[idViaje];
            i++;
            colaTemp.add(idViaje, pri);
        }

        colaViajes = colaTemp;
        return resultado;
    }

    // ==========================================
    //    MÓDULO 4: ANÁLISIS DE CONEXIONES
    // ==========================================

    public void listarTerminalesOperadas() {
        if (listaIdTerminales.isEmpty()) {
            System.out.println("  No hay terminales registradas en el sistema.");
            return;
        }
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int id = listaIdTerminales.get(i);
            System.out.println("  • [" + terminales[id].getCodigo() + "] " + terminales[id].getDescripcion());
        }
    }

    public void reportarTerminalesMayorMovimiento() {
        if (contadorViajes == 0) {
            System.out.println("  No hay viajes planificados para calcular movimientos.");
            return;
        }

        int[] conteoSalidas = new int[contadorTerminales];
        int[] conteoLlegadas = new int[contadorTerminales];

        for (int i = 0; i < contadorViajes; i++) {
            Ruta r = viajes[i].getRutaAsignada();
            conteoSalidas[r.getIdOrigen()]++;
            conteoLlegadas[r.getIdDestino()]++;
        }

        int maxSalidas = -1;
        int maxLlegadas = -1;
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int id = listaIdTerminales.get(i);
            if (conteoSalidas[id] > maxSalidas) maxSalidas = conteoSalidas[id];
            if (conteoLlegadas[id] > maxLlegadas) maxLlegadas = conteoLlegadas[id];
        }

        System.out.println("\n  Terminales con mayor número de SALIDAS (" + maxSalidas + " viajes):");
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int id = listaIdTerminales.get(i);
            if (conteoSalidas[id] == maxSalidas && maxSalidas > 0) {
                System.out.println("    -> " + terminales[id].getCodigo() + " - " + terminales[id].getDescripcion());
            }
        }

        System.out.println("\n  Terminales con mayor número de LLEGADAS (" + maxLlegadas + " viajes):");
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int id = listaIdTerminales.get(i);
            if (conteoLlegadas[id] == maxLlegadas && maxLlegadas > 0) {
                System.out.println("    -> " + terminales[id].getCodigo() + " - " + terminales[id].getDescripcion());
            }
        }
    }

    public void reportarTerminalesMasConectadas() {
        if (listaIdTerminales.isEmpty()) {
            System.out.println("  No hay terminales para evaluar conexiones.");
            return;
        }

        int[] conexionesDirectas = new int[contadorTerminales];

        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int idOrigen = listaIdTerminales.get(i);
            for (int j = 0; j < listaIdTerminales.size(); j++) {
                int idDestino = listaIdTerminales.get(j);
                if (mapaRutas.existEdge(idOrigen, idDestino)) {
                    conexionesDirectas[idOrigen]++;
                }
            }
        }

        int maxConexiones = -1;
        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int id = listaIdTerminales.get(i);
            if (conexionesDirectas[id] > maxConexiones) maxConexiones = conexionesDirectas[id];
        }

        System.out.println("\n  Terminales con más conexiones directas (" + maxConexiones + " rutas):");
        if (maxConexiones <= 0) {
            System.out.println("    No hay rutas de conexión directa registradas.");
            return;
        }

        for (int i = 0; i < listaIdTerminales.size(); i++) {
            int id = listaIdTerminales.get(i);
            if (conexionesDirectas[id] == maxConexiones) {
                System.out.println("    -> " + terminales[id].getCodigo() + " - " + terminales[id].getDescripcion());
            }
        }
    }

    // ==========================================
    //     MÓDULO 5: SIMULACIÓN Y REPORTES
    // ==========================================

    public void reprogramarViaje(int idViaje, String nuevaFecha, String nuevaPatente) {
        if (idViaje < 0 || idViaje >= contadorViajes) {
            throw new InvalididTravelException("El ID de viaje suministrado no existe en el sistema.");
        }

        viajes[idViaje].setFecha(nuevaFecha);

        if (nuevaPatente != null && !nuevaPatente.equals("")) {
            Micro nuevoMicro = buscarMicroPorPatente(nuevaPatente);
            if (nuevoMicro == null) {
                throw new InvalidMicroException("El micro con patente " + nuevaPatente + " no está registrado.");
            }
            viajes[idViaje].setMicroAsignado(nuevoMicro);

            int idMicro = buscarIndiceMicro(nuevaPatente);
            int asignacionesActuales = asignacionesPorMicro.get(idMicro);
            asignacionesPorMicro.add(idMicro, asignacionesActuales + 1);
            viajesPorMicro.add(idMicro, idViaje);
        }
    }

    public void generarReporteUtilizacionMicros() {
        if (contadorMicros == 0) {
            System.out.println("  No hay micros registrados en la flota.");
            return;
        }

        int totalAsignaciones = 0;
        for (int i = 0; i < contadorMicros; i++) {
            totalAsignaciones += asignacionesPorMicro.get(i);
        }

        double promedioGlobal = (double) totalAsignaciones / contadorMicros;
        System.out.println("  Utilización Promedio Global: " + promedioGlobal + " viajes por unidad.");
        System.out.println("\n  Distribución porcentual sobre el total de operaciones:");

        if (totalAsignaciones == 0) {
            System.out.println("    Ningún micro registra viajes asignados todavía.");
            return;
        }

        for (int i = 0; i < contadorMicros; i++) {
            int asignaciones = asignacionesPorMicro.get(i);
            double porcentaje = (asignaciones * 100.0) / totalAsignaciones;
            System.out.println("    -> Micro [" + flotaMicros[i].getPatente() + "]: "
                    + asignaciones + " viajes (" + porcentaje + "%)");
        }
    }

    public void generarReporteRutasExtremas() {
        if (listaIdRutas.isEmpty()) {
            System.out.println("  No hay rutas conectadas en el sistema.");
            return;
        }
        if (contadorViajes == 0) {
            System.out.println("  No hay viajes planificados para analizar frecuencia.");
            return;
        }

        int[] frecuenciaRutas = new int[contadorRutas];
        for (int i = 0; i < contadorViajes; i++) {
            int idRuta = viajes[i].getRutaAsignada().getIdRuta();
            frecuenciaRutas[idRuta]++;
        }

        int maxUso = -1;
        int minUso = frecuenciaRutas[listaIdRutas.get(0)];

        for (int i = 0; i < listaIdRutas.size(); i++) {
            int idRuta = listaIdRutas.get(i);
            if (frecuenciaRutas[idRuta] > maxUso) maxUso = frecuenciaRutas[idRuta];
            if (frecuenciaRutas[idRuta] < minUso) minUso = frecuenciaRutas[idRuta];
        }

        System.out.println("\n  Rutas MÁS UTILIZADAS (" + maxUso + " recorridos):");
        for (int i = 0; i < listaIdRutas.size(); i++) {
            int idRuta = listaIdRutas.get(i);
            if (frecuenciaRutas[idRuta] == maxUso) imprimirDatosRuta(rutas[idRuta]);
        }

        System.out.println("\n  Rutas MENOS RECORRIDAS (" + minUso + " recorridos):");
        for (int i = 0; i < listaIdRutas.size(); i++) {
            int idRuta = listaIdRutas.get(i);
            if (frecuenciaRutas[idRuta] == minUso) imprimirDatosRuta(rutas[idRuta]);
        }
    }
}

