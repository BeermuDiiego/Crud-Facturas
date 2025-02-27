import { ref, onMounted } from "vue";
import { AgGridVue } from "ag-grid-vue3";
import api from "../../services/api.js";
import { alertDelete, alertInfo } from "../../models/Alerts.js";
import FormInvoice from "../FormInvoice.vue";
import FormInvoiceModify from "../FormInvoiceModify.vue";
import * as XLSX from "xlsx";
import { createPdf } from "../../models/Pdf.js";

export function UseInvoice() {
  return {
    components: {
      //Componente de la tabla
      AgGridVue,
      //Componentes de los formularios.
      FormInvoice,
      FormInvoiceModify,
    },
    setup() {
      //Al montar la pagina, se obtiene la lista de facturas y sus totales.
      onMounted(() => {
        getInvoices();
        getDataInvoices();
      });
      //Referencias para la creacion de la tabla.
      const gridApi = ref(null);
      const rowData = ref([]);
      //Referencias para mostrar los formularios
      const showForm = ref(false);
      const showFormMod = ref(false);
      //Referencia para la seleccion de la factura
      const selectedInvoice = ref(null);
      //Referencias para mostrarlos en el cuadro
      const numInvoices = ref(0);
      const invoiceTotal = ref(0);
      const invoiceTotalPaid = ref(0);
      const invoiceTotalNoPaid = ref(0);

      //Funcion que sirve para abrir el formulario de modificacion de una factura
      const handleModify = (data) => {
        selectedInvoice.value = data;
        formModOpenOrClose();
      };

      //Funcion que sirve para ver la factura en pdf.
      const handleView = (data) => {
        createPdf(data);
      };

      //Funcion que sirve para eliminar una factura por fila.
      const handleDelete = async (data) => {
        try {
          //Primero se pedira confirmacion de la eliminacion.
          const result = await alertDelete(
            "¿Quieres borrar esta factura?",
            "Factura: " + data.num
          );
          //Si se confirma, con el id de la factura, se hace un DELETE, que se borrara en la base de datos. Incluyendo sus items.
          if (result) {
            const response = await api.delete("/invoice/delete/" + data.id);
            await getInvoices();
            await getDataInvoices();
          }
        } catch (err) {
          console.error("Eliminacion de factura:", err);
        }
      };

      //Funcion que creara los botones de Modificar, PDF y Borrar.
      const actionCellRenderer = (params) => {
        //Los datos de las celdas se convierten en JSON, y remplaza las comillas dobles para evitar fallos.
        const data = JSON.stringify(params.data).replace(/"/g, "&quot;");
        //Devuelve los botones para mostrar en la columna Acciones.
        return `
      <div>
        <button style="font-size:1.1vh; font-weight:bold; background-color: #4d79ff; color: white; padding: 0.55vh 1vh; border: 0.11vh solid black; border-radius: 0.44vh; cursor: pointer;" onclick='handleModify(${data})'>Modificar</button>
        <button style="font-size:1.1vh; font-weight:bold; background-color: white; padding: 0.55vh 1vh; border: 1px solid black; border-radius: 0.44vh; cursor: pointer;" onclick='handleView(${data})'>PDF</button>
        <button style="font-size:1.1vh; font-weight:bold; background-color: #ff4d4d; color: white; padding: 0.55vh 1vh; border: 0.11vh solid black; border-radius: 0.44vh; cursor: pointer;" onclick='handleDelete(${data})'>Borrar</button>
      </div>
    `;
      };

      // Habilitamos las funciones para que puedan ser invocadas desde el html.
      window.handleModify = handleModify;
      window.handleView = handleView;
      window.handleDelete = handleDelete;

      // Función para guardar la refencia de la api de la tabla  que se usara luego, y tambien ajuta el tamanyo de las columnas automaticamente.
      const onGridReady = (params) => {
        gridApi.value = params.api;
        params.api.sizeColumnsToFit();
      };

      //Referencia que sirve para configurar los textos a espanyol de la tabla.
      const localeText = ref({
        filterOoo: "Filtrar...",
        equals: "Igual",
        notEqual: "No igual",
        contains: "Contiene",
        notContains: "No contiene",
        startsWith: "Comienza con",
        endsWith: "Termina con",
        before: "Antes de",
        after: "Despues de",
        inRange: "Entre",
        lessThan: "Menor que",
        greaterThan: "Mayor que",
        lessThanOrEqual: "Menor o igual que",
        greaterThanOrEqual: "Mayor o igual que",
        applyFilter: "Aplicar filtro...",
        resetFilter: "Restablecer filtro",
        clearFilter: "Limpiar filtro",
        loadingOoo: "Cargando...",
        noRowsToShow: "La tabla no tiene contenido",
        searchOoo: "Buscar...",
        blank: "Vacios",
        notBlank: "No Vacios",
        andCondition: "Y",
        orCondition: "O",
      });

      //Referencia para asignar las columnas y configurar las caracteristicas de la tabla.
      const columnDefs = ref([
        //Configuracion de la columna Nº Factura"
        {
          field: "num",
          headerName: "Nº Factura",
          width: 100,
          filter: "agTextColumnFilter",
          filterParams: {
            debounceMs: 200,
            buttons: ["reset"],
          }, // Comparador personalizado para el numero de factura
          comparator: (a, b) => {
            let [a1, a2] = a.split("-").map((num) => parseInt(num, 10));
            let [b1, b2] = b.split("-").map((num) => parseInt(num, 10));

            // Comparar primero la parte anterior al guion (a1 vs b1)
            if (a1 !== b1) {
              return a1 - b1; // Si son diferentes, se ordena por la primera parte
            } else {
              // Si son iguales, comparar la parte después del guion (a2 vs b2)
              return a2 - b2;
            }
          },
        },
        //Configuracion de la columna Fecha de creación"
        {
          field: "date",
          headerName: "Fecha de creación",
          width: 110,
          filter: "agDateColumnFilter",
          filterParams: {
            debounceMs: 200,
            buttons: ["reset"],
          },
        },
        //Configuracion de la columna Referencia"
        {
          field: "address",
          headerName: "Referencia",
          filter: "agTextColumnFilter",
          filterParams: {
            debounceMs: 200,
            buttons: ["reset"],
          },
        },
        //Configuracion de la columna Cliente"
        {
          field: "client",
          headerName: "Cliente",
          filter: "agTextColumnFilter",
          filterParams: {
            debounceMs: 200,
            buttons: ["reset"],
          },
        },
        //Configuracion de la columna Estado de pago"
        { field: "paid", headerName: "Estado de pago", width: 90 },
        //Configuracion de la columna IVA"
        {
          field: "iva",
          headerName: "IVA",
          width: 60,
          filter: "agNumberColumnFilter",
          filterParams: {
            debounceMs: 200,
            buttons: ["reset"],
          },
          //Formateo para que cada numero tenga el porcentaje.
          valueFormatter: (params) => {
            return params.value + "%";
          },
        },
        //Configuracion de la columna Total"
        {
          field: "total",
          headerName: "Total",
          width: 70,
          filter: "agNumberColumnFilter",
          filterParams: {
            debounceMs: 200,
            buttons: ["reset"],
          },
          //Formateo para que cada numero tenga el simbolo del euro.
          valueFormatter: (params) => {
            return params.value + "€";
          },
        },
        //Configuracion de la columna Acciones, y asignar los botones de accion creados anteriormente"
        {
          headerName: "Acciones",
          width: 170,
          cellRenderer: actionCellRenderer,
        },
      ]);

      // Configuracion de checkbox multiple y por fila para la tabla.
      const rowSelection = {
        mode: "multiRow",
        enableClickSelection: false,
        headerCheckboxSelection: true,
        checkboxSelection: true,
        width: 10,
        minWidth: 10,
        suppressSizeToFit: false,
      };
      //Referencia con una configuracion para que la tabla tenga columnas estaticas
      const gridOptions = ref({
        suppressMovableColumns: true,
      });

      /**
       * Funcion que se encarga de obtener la lista de facturas que luego se mostraran en la tabla.
       * A su vez, tambien obtiene el ultimo numero de factura para tener una referencia a la hora de crear una nueva factura.
       *
       */
      const getInvoices = async () => {
        try {
          //GET que obtiene la lista de facturas de la api.
          const businessData = JSON.parse(sessionStorage.getItem("business"));
          const response = await api.get(
            "/invoice/list-invoices/" + businessData.idBusiness
          );

          //Variables para usar en la logica de la obtencion del ultimo numero de factura.
          let lastNum = 0;
          let lastNumYear = 0;
          let latestInvoice = 0;

          //Se guarda la lista de facturas en la  referencia rowData, que a su vez, se busca el ultimo numero de factura.
          rowData.value = response.data.map((invoice) => {
            let invoiceNum = parseInt(invoice.numInvoice.split("-")[0]);
            let invoiceNumYear = parseInt(invoice.numInvoice.split("-")[1]);

            // Comprobamos si es el mayor número de factura
            if (
              invoiceNumYear > lastNumYear ||
              (invoiceNumYear === lastNumYear && invoiceNum > lastNum)
            ) {
              lastNumYear = invoiceNumYear;
              lastNum = invoiceNum;
              // Guardamos la última factura
              latestInvoice = invoice.numInvoice;
            }

            return {
              //Para mostrar en la tabla y pdf
              id: invoice.invoiceId,
              num: invoice.numInvoice,
              date: invoice.dateInvoice,
              address: invoice.addressWork,
              client:
                invoice.client.nameClient +
                " (" +
                invoice.client.cifClient +
                ")",
              paid: invoice.paid,
              iva: invoice.iva,
              total: invoice.total,
              //Para mostrar en el pdf
              subtotalInvoice: invoice.subtotal,
              invoicesItems: invoice.invoicesItems,
              clients: invoice.client,
            };
          });
          //Ser guarda en sessionStorage el ultimo numero de factura para un futuro uso.
          sessionStorage.setItem("numInvoice", latestInvoice);
        } catch (err) {
          console.error("Facturas error:", err);
        }
      };

      //Funcion que sirve para cerrar el formuralio con emit closed
      const formOpenOrClose = () => {
        if (!showForm.value) {
          showForm.value = true;
        } else {
          showForm.value = false;
        }
      };
      //Funcion que sirve para cerrar el formuralio con emit saved. Se vuelve a obtener las facturas actulizadas
      const formOpenOrCloseSaved = () => {
        if (!showForm.value) {
          showForm.value = true;
        } else {
          showForm.value = false;
          getInvoices();
          getDataInvoices();
        }
      };
      //Funcion que sirve para cerrar el formuralio que edita. Se vuelve a obtener las facturas actulizadas
      const formModOpenOrClose = () => {
        if (!showFormMod.value) {
          showFormMod.value = true;
        } else {
          showFormMod.value = false;
          getInvoices();
          getDataInvoices();
        }
      };
      //Funcion que actualiza la tabla, y los datos de los cuadros
      const updateTable = () => {
        getInvoices();
        getDataInvoices();
      };

      //Funcion que sirve para exportar las facturas seleccionadas en excel.
      const exportExcel = () => {
        if (gridApi.value) {
          // Obtener las filas seleccionadas
          const selectedRows = gridApi.value.getSelectedRows();

          // Comprobar si hay filas seleccionadas
          if (selectedRows.length > 0) {
            // Crear los datos que se exportarán
            const exportData = selectedRows.map((row) => {
              const rowData = {};
              columnDefs.value.forEach((colDef) => {
                // Quitar la columna de Acciones
                if (colDef.headerName !== "Acciones") {
                  // Si es la columna paid, se cambia True por Pagado y False por No Pagado
                  if (colDef.field === "paid") {
                    rowData[colDef.headerName] = row[colDef.field]
                      ? "Pagado"
                      : "No Pagado";
                  } else {
                    rowData[colDef.headerName] = row[colDef.field];
                  }
                }
              });
              return rowData;
            });

            // Creacion del excel con los datos obtenidos.
            const ws = XLSX.utils.json_to_sheet(exportData);
            const wb = XLSX.utils.book_new();
            XLSX.utils.book_append_sheet(wb, ws, "Facturas");

            // Exportar el archivo excel
            XLSX.writeFile(wb, "Facturas.xlsx");
          } else {
            alertInfo("Selecciona al menos una fila para exportar.");
          }
        }
      };
      //Funcion que obtiene los  totales para mostrarlos en los cuadros.
      const getDataInvoices = async () => {
        try {
          const businessData = JSON.parse(sessionStorage.getItem("business"));
          const responseInvoiceTotal = await api.get(
            "/invoice/list-total-prices-business/" + businessData.idBusiness
          );

          responseInvoiceTotal.data.forEach((invoice) => {
            numInvoices.value = invoice.numInvoices;
            invoiceTotal.value = invoice.totalPrices;
            invoiceTotalNoPaid.value = invoice.totalPricesNoPaid;
            invoiceTotalPaid.value = invoice.totalPricesPaid;
          });
        } catch (err) {
          console.log("Error Datos De Facturas: ", err);
        }
      };

      //Variables y funciones que se retornan para usarlo en el template.
      return {
        rowData,
        columnDefs,
        localeText,
        rowSelection,
        gridOptions,
        onGridReady,
        showForm,
        showFormMod,
        formOpenOrClose,
        formOpenOrCloseSaved,
        formModOpenOrClose,
        updateTable,
        exportExcel,
        numInvoices,
        invoiceTotal,
        invoiceTotalPaid,
        invoiceTotalNoPaid,
        selectedInvoice,
      };
    },
  };
}
