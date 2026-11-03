import Controller.SistemaController;
import Modulo.Micro;
import Modulo.Viaje;
import java.util.Scanner;

public class Main {
    private static SistemaController sistema = new SistemaController();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        sistema.cargarDatosDePrueba();

        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n==================================================");
            System.out.println("          SISTEMA DE GESTIÓN DE TRANSPORTE        ");
            System.out.println("==================================================");
            System.out.println("1. Gestión de MICROS");
            System.out.println("2. Gestión de RUTAS");
            System.out.println("3. Gestión de VIAJES");
            System.out.println("4. Gestión de TERMINALES");
            System.out.println("0. Salir del Sistema");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1: menuMicros(); break;
                    case 2: menuRutas(); break;
                    case 3: menuViajes(); break;
                    case 4: menuTerminales(); break;
                    case 0: System.out.println("Saliendo..."); break;
                    default: System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número válido.");
            }
        }
    }

    // ==========================================
    //            SUBMENÚ: MICROS
    // ==========================================
    private static void menuMicros() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MODULO: GESTIÓN DE MICROS ---");
            System.out.println("1. Crear / Registrar Micro");
            System.out.println("2. Ver micros con MÁS asignaciones");
            System.out.println("3. Ver micros OPERATIVOS (Con viajes asignados)");
            System.out.println("4. Ver reporte de porcentaje de utilización de flota");
            System.out.println("5. [Simulación] Cambiar estado del micro (Reprogramar Viaje)");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese patente (ej. AA-123-BB): ");
                        String patente = scanner.nextLine();
                        System.out.print("Ingrese tipo (Cama / Semi-Cama / Ejecutivo): ");
                        String tipo = scanner.nextLine();
                        sistema.registrarMicro(patente, tipo);
                        System.out.println("¡Micro registrado con éxito!");
                        break;
                    case 2:
                        Micro[] topMicros = sistema.obtenerMicrosMasAsignados();
                        System.out.println("\n--- Micros con mayor cantidad de asignaciones ---");
                        for (Micro m : topMicros) {
                            System.out.println("-> Patente: [" + m.getPatente() + "] | Tipo: " + m.getTipo() + " | Asignaciones: " + sistema.obtenerAsignacionesDeMicro(m.getPatente()));
                        }
                        break;
                    case 3:
                        Micro[] operativos = sistema.obtenerMicrosAsignados();
                        System.out.println("\n--- Micros Operativos actualmente ---");
                        for (Micro m : operativos) {
                            System.out.println("-> Patente: [" + m.getPatente() + "] | Tipo: " + m.getTipo());
                        }
                        break;
                    case 4:
                        sistema.generarReporteUtilizacionMicros();
                        break;
                    case 5:
                        System.out.print("Ingrese el ID del viaje a modificar: ");
                        int idV = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingrese nueva fecha (AAAA-MM-DD): ");
                        String f = scanner.nextLine();
                        System.out.print("Ingrese patente del nuevo micro asignado: ");
                        String p = scanner.nextLine();
                        sistema.reprogramarViaje(idV, f, p);
                        System.out.println("¡Viaje reprogramado y estado de flota actualizado!");
                        break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error en Módulo Micros: " + e.getMessage());
            }
        }
    }

    // ==========================================
    //            SUBMENÚ: RUTAS
    // ==========================================
    private static void menuRutas() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MÓDULO: GESTIÓN DE RUTAS ---");
            System.out.println("1. Crear nueva Ruta directa");
            System.out.println("2. Reporte de Rutas Extremas (Más y Menos recorridas)");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.print("Código Terminal Origen (ej. BUE): ");
                        String orig = scanner.nextLine();
                        System.out.print("Código Terminal Destino (ej. ROS): ");
                        String dest = scanner.nextLine();
                        System.out.print("Ingrese peso/distancia del camino: ");
                        int peso = Integer.parseInt(scanner.nextLine());
                        sistema.conectarTerminales(orig, dest, peso);
                        System.out.println("¡Ruta conectada y añadida al Grafo con éxito!");
                        break;
                    case 2:
                        sistema.generarReporteRutasExtremas();
                        break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error en Módulo Rutas: " + e.getMessage());
            }
        }
    }

    // ==========================================
    //            SUBMENÚ: VIAJES
    // ==========================================
    private static void menuViajes() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MÓDULO: GESTIÓN DE VIAJES ---");
            System.out.println("1. Planificar un nuevo Viaje");
            System.out.println("2. Cambiar prioridad de un Viaje");
            System.out.println("3. Listar todos los Viajes ordenados por PRIORIDAD");
            System.out.println("4. Buscar caminos posibles estableciendo MÁXIMO DE PARADAS (DFS)");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.print("Código Terminal Origen: ");
                        String o = scanner.nextLine();
                        System.out.print("Código Terminal Destino: ");
                        String d = scanner.nextLine();
                        System.out.print("Patente del Micro: ");
                        String p = scanner.nextLine();
                        System.out.print("Fecha del Viaje (AAAA-MM-DD): ");
                        String f = scanner.nextLine();
                        sistema.planificarViaje(o, d, p, f);
                        System.out.println("¡Viaje planificado y encolado en prioridad!");
                        break;
                    case 2:
                        System.out.print("Ingrese ID del viaje: ");
                        int idV = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingrese nueva Prioridad (1-Alta, 2-Media, 3-Baja): ");
                        String pri = scanner.nextLine();
                        sistema.cambiarPrioridadViaje(idV, Integer.parseInt(pri));
                        System.out.println("¡Prioridad modificada en la Cola!");
                        break;
                    case 3:
                        Viaje[] ordenados = sistema.obtenerViajesOrdenadosPorPrioridad();
                        System.out.println("\n--- Viajes Desencolados por Prioridad ---");
                        for (Viaje v : ordenados) {
                            if (v != null) {
                                System.out.println("-> ID Viaje: " + v.getIdViaje() + " | Prioridad: " + v.getPrioridad() + " | Fecha: " + v.getFecha() + " | Micro: " + v.getMicroAsignado().getPatente());
                            }
                        }
                        break;
                    case 4:
                        System.out.print("Código Terminal Origen: ");
                        String origDfs = scanner.nextLine();
                        System.out.print("Código Terminal Destino: ");
                        String destDfs = scanner.nextLine();
                        System.out.print("Establezca el máximo de paradas intermedias permitidas: ");
                        int maxParadas = Integer.parseInt(scanner.nextLine());
                        sistema.buscarYMostrarRutas(origDfs, destDfs, maxParadas);
                        break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error en Módulo Viajes: " + e.getMessage());
            }
        }
    }

    // ==========================================
    //          SUBMENÚ: TERMINALES
    // ==========================================
    private static void menuTerminales() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MÓDULO: GESTIÓN DE TERMINALES ---");
            System.out.println("1. Crear / Registrar Terminal");
            System.out.println("2. Conectar Terminales (Crear Ruta)");
            System.out.println("3. Listar Terminales Operadas");
            System.out.println("4. Reporte: Terminales con mayor movimiento (Salidas / Llegadas)");
            System.out.println("5. Reporte: Terminales con más conexiones directas");
            System.out.println("0. Volver al menú principal");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese código de Terminal (3 letras - ej. AEP): ");
                        String cod = scanner.nextLine();
                        System.out.print("Ingrese descripción/nombre de la Terminal: ");
                        String desc = scanner.nextLine();
                        sistema.registrarTerminal(cod, desc);
                        System.out.println("¡Terminal añadida correctamente!");
                        break;
                    case 2:
                        System.out.print("Código Terminal Origen: ");
                        String o = scanner.nextLine();
                        System.out.print("Código Terminal Destino: ");
                        String d = scanner.nextLine();
                        System.out.print("Distancia/Peso en kms: ");
                        int peso = Integer.parseInt(scanner.nextLine());
                        sistema.conectarTerminales(o, d, peso);
                        System.out.println("¡Conexión establecida!");
                        break;
                    case 3:
                        sistema.listarTerminalesOperadas();
                        break;
                    case 4:
                        sistema.reportarTerminalesMayorMovimiento();
                        break;
                    case 5:
                        sistema.reportarTerminalesMasConectadas();
                        break;
                    case 0: break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error en Módulo Terminales: " + e.getMessage());
            }
        }
    }
}
