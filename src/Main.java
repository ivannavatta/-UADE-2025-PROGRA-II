import Controller.SistemaController;
import Modulo.Micro;
import Modulo.Viaje;

public class Main {
    public static void main(String[] args) {
        SistemaController sistema = new SistemaController();

        sistema.cargarDatosDePrueba();

        System.out.println("==================================================");
        System.out.println("      INICIO DE PRUEBAS INTEGRALES DE ENTREGA      ");
        System.out.println("==================================================");

        System.out.println("\n--- TEST Módulo 1: Búsqueda de Rutas (DFS) ---");
        sistema.buscarYMostrarRutas("BUE", "BRC", 1);

        System.out.println("\n--- TEST Módulo 2: Gestión de Flota (Micros) ---");

        Micro[] microsAsignados = sistema.obtenerMicrosAsignados();
        System.out.println("Micros operativos (con viajes asignados): " + microsAsignados.length);

        Micro[] topMicros = sistema.obtenerMicrosMasAsignados();
        System.out.println("\n Micros con la MAYOR cantidad de asignaciones:");
        for (int i = 0; i < topMicros.length; i++) {
            System.out.println("    -> Patente [" + topMicros[i].getPatente() + "] - Tipo: " + topMicros[i].getTipo()
                    + " | Viajes asignados: " + topMicros[i].getContadorAsignaciones());
        }

        System.out.println("\n--- TEST Módulo 3: Ordenamiento por Prioridad ---");
        sistema.cambiarPrioridadViaje(0, 1);
        Viaje[] ordenados = sistema.obtenerViajesOrdenadosPorPrioridad();
        for(int i = 0; i < ordenados.length; i++) {
            System.out.println("Viaje ID: " + ordenados[i].getIdViaje() + " | Prioridad: " + ordenados[i].getPrioridad());
        }

        System.out.println("\n--- TEST Módulo 4: Reporte de Conectividad ---");
        sistema.listarTerminalesOperadas();
        sistema.reportarTerminalesMayorMovimiento();
        sistema.reportarTerminalesMasConectadas();

        System.out.println("\n--- TEST Módulo 5: Simulación y Reportes Finales ---");
        sistema.reprogramarViaje(1, "2026-08-15", "AA-001-AA");

        sistema.generarReporteUtilizacionMicros();
        sistema.generarReporteRutasExtremas();

        System.out.println("\n==================================================");
        System.out.println("          FIN DE LAS PRUEBAS INTEGRALES           ");
        System.out.println("==================================================");
    }
}
