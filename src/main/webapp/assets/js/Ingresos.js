/**
 * @fileoverview Cálculo de métricas financieras (KPIs) y filtrado dinámico de ingresos en LibriFlow.
 *
 * Procesa en el cliente las filas de transacciones para calcular ganancias y conteos
 * desglosados por periodos (Hoy, Esta Semana, Este Mes, Total Histórico), actualizando
 * las tarjetas de indicadores principales y permitiendo la filtración por pestañas.
 *
 * @author Francisco Emmanuel Fuentes Pérez
 * @version 1.0
 * @since 25/08/2026
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Obtención de referencias a las filas de transacciones registradas
    const rows = Array.from(document.querySelectorAll('.lf-ingreso-row'));
    if (rows.length === 0) return;

    // 2. Cálculo de marcas de tiempo de referencia (Hoy, Semana actual, Mes actual)
    const now = new Date();

    // Inicio del día actual (00:00:00 hrs)
    const startOfToday = new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime();

    // Inicio de la semana actual (Lunes a las 00:00:00 hrs)
    const currentDayOfWeek = now.getDay() === 0 ? 6 : now.getDay() - 1; // Ajuste para considerar Lunes = 0
    const startOfWeek = new Date(now.getFullYear(), now.getMonth(), now.getDate() - currentDayOfWeek).getTime();

    // Inicio del mes actual (Día 1 a las 00:00:00 hrs)
    const startOfMonth = new Date(now.getFullYear(), now.getMonth(), 1).getTime();

    // Estructura contenedora para totales de ganancias y conteo por período
    let totals = {
        hoy: { ganancia: 0, count: 0 },
        semana: { ganancia: 0, count: 0 },
        mes: { ganancia: 0, count: 0 },
        total: { ganancia: 0, count: 0 }
    };

    // 3. Iteración sobre las filas para procesar y acumular montos de ganancias
    rows.forEach(row => {
        const timeMs = parseInt(row.getAttribute('data-time'), 10);
        const ganancia = parseFloat(row.getAttribute('data-ganancia')) || 0;

        // Acumulado Total Histórico
        totals.total.ganancia += ganancia;
        totals.total.count++;

        // Acumulado del Mes
        if (timeMs >= startOfMonth) {
            totals.mes.ganancia += ganancia;
            totals.mes.count++;
        }

        // Acumulado de la Semana
        if (timeMs >= startOfWeek) {
            totals.semana.ganancia += ganancia;
            totals.semana.count++;
        }

        // Acumulado de Hoy
        if (timeMs >= startOfToday) {
            totals.hoy.ganancia += ganancia;
            totals.hoy.count++;
        }
    });

    /**
     * Formatea valores numéricos a formato de moneda local en Pesos Mexicanos (MXN).
     * @param {number} val Monto numérico a transformar.
     * @returns {string} Cadena formateada como moneda (ej. "$1,250.00").
     */
    const fmt = (val) => '$' + val.toLocaleString('es-MX', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

    // 4. Actualización visual de los paneles KPI superiores
    document.getElementById('kpi-hoy').textContent = fmt(totals.hoy.ganancia);
    document.getElementById('count-hoy').textContent = `${totals.hoy.count} transacciones`;

    document.getElementById('kpi-semana').textContent = fmt(totals.semana.ganancia);
    document.getElementById('count-semana').textContent = `${totals.semana.count} transacciones`;

    document.getElementById('kpi-mes').textContent = fmt(totals.mes.ganancia);
    document.getElementById('count-mes').textContent = `${totals.mes.count} transacciones`;

    document.getElementById('kpi-total').textContent = fmt(totals.total.ganancia);
    document.getElementById('count-total').textContent = `${totals.total.count} transacciones`;

    // 5. Configuración del filtrado dinámico por pestañas de período
    const tabs = document.querySelectorAll('.lf-tab-btn');
    const emptyFilterRow = document.getElementById('row-empty-filter');
    const tabSumDisplay = document.getElementById('tab-sum-display');

    /**
     * Filtra la visibilidad de las filas en la tabla según el período seleccionado.
     * @param {string} period Período a evaluar ('hoy', 'semana', 'mes', 'total').
     */
    function filterPeriod(period) {
        let visibleCount = 0;
        let sumPeriod = 0;

        rows.forEach(row => {
            const timeMs = parseInt(row.getAttribute('data-time'), 10);
            const ganancia = parseFloat(row.getAttribute('data-ganancia')) || 0;
            let show = false;

            if (period === 'total') show = true;
            else if (period === 'mes' && timeMs >= startOfMonth) show = true;
            else if (period === 'semana' && timeMs >= startOfWeek) show = true;
            else if (period === 'hoy' && timeMs >= startOfToday) show = true;

            if (show) {
                row.style.display = '';
                visibleCount++;
                sumPeriod += ganancia;
            } else {
                row.style.display = 'none';
            }
        });

        // Muestra mensaje alternativo si no se encuentran registros en el rango seleccinado
        if (emptyFilterRow) {
            emptyFilterRow.style.display = visibleCount === 0 ? '' : 'none';
        }

        tabSumDisplay.textContent = fmt(sumPeriod);
    }

    // Asignación de manejadores de eventos a los botones de pestañas
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            filterPeriod(tab.getAttribute('data-period'));
        });
    });

    // Filtro inicial por defecto (muestra todo el historial)
    filterPeriod('total');
});