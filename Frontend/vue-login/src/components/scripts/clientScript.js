import { ref, onMounted } from "vue";
import { AgGridVue } from "ag-grid-vue3";
import api from "../../services/api.js";
import { alertDelete, alertInfo } from "../../models/Alerts.js";
import FormClient from "../FormClient.vue";
import FormClientModify from "../FormClientModify.vue";
import * as XLSX from "xlsx";

export function UseClientScript() {
  return {
    components: {
      //Componente de la tabla
      AgGridVue,
      //Componentes de los formularios.
      FormClient,
      FormClientModify,
    },
    setup() {
      //Al montar la pagina, se obtiene la lista de clientes.
      onMounted(() => {
        getClients();
        getDataInvoices();
      });
      //Referencias para la creacion de la tabla.
      const gridApi = ref(null);
      const rowData = ref([]);
      //Referencias para mostrar los formularios
      const showForm = ref(false);
      const showFormMod = ref(false);
      //Referencia para la seleccion del cliente
      const selectedClient = ref(null);
      //Referencias para mostrarlos en el cuadro
      const numInvoices = ref(0);
      const invoiceTotal = ref(0);
      const numClients = ref(0);
      //Datos de las listas top
      const topClients = ref([]);

      //Funcion que sirve para abrir el formulario de modificacion de un cliente
      const handleModify = (data) => {
        selectedClient.value = data;
        formModOpenOrClose();
      };

      //Funcion que sirve para eliminar una cliente por fila.
      const handleDelete = async (data) => {
        try {
          //Primero se pedira confirmacion de la eliminacion.
          const result = await alertDelete(
            "¿Quieres borrar este cliente?",
            "Cliente: " + data.nameClient + "(" + data.idClient + ")"
          );
          //Si se confirma, con el identificador del cliente, se hace un DELETE, que se borrara en la base de datos.
          if (result) {
            const response = await api.delete("/client/delete/" + data.id);
            await getClients();
            await getDataInvoices();
          }
        } catch (err) {
          console.error("Eliminacion de cliente:", err);
        }
      };

      //Funcion que creara los botones de Modificar y Borrar.
      const actionCellRenderer = (params) => {
        //Los datos de las celdas se convierten en JSON, y remplaza las comillas dobles para evitar fallos.
        const data = JSON.stringify(params.data).replace(/"/g, "&quot;");
        //Devuelve los botones para mostrar en la columna Acciones.
        return `
      <div>
        <button style="font-size:1.1vh; font-weight:bold; background-color: #4d79ff; color: white; padding: 0.55vh 1vh; border: 0.11vh solid black; border-radius: 0.44vh; cursor: pointer;" onclick='handleModify(${data})'>Modificar</button>
        <button style="margin: 0;font-size:1.1vh; font-weight:bold; background-color: #ff4d4d; color: white; padding: 0.55vh 1vh; border: 0.11vh solid black; border-radius: 0.44vh; cursor: pointer;" onclick='handleDelete(${data})'>Borrar</button>
      </div>
    `;
      };

      // Habilitamos las funciones para que puedan ser invocadas desde el html.
      window.handleModify = handleModify;
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
        //Configuracion de la columna Nombre Del Cliente"
        {
          field: "nameClient",
          headerName: "Nombre Del Cliente",
          width: 110,
          filter: "agTextColumnFilter",
          filterParams: {
            debounceMs: 200,
            buttons: ["reset"],
          },
        },
        //Configuracion de la columna Identificador del cliente"
        {
          field: "idClient",
          headerName: "Identificador",
          width: 90,
          filter: "agTextColumnFilter",
          filterParams: {
            debounceMs: 200,
            buttons: ["reset"],
          },
        },
        //Configuracion de la columna Direccion"
        {
          field: "addressClient",
          headerName: "Direccion",
          filter: "agTextColumnFilter",
          width: 100,
          filterParams: {
            debounceMs: 200,
            buttons: ["reset"],
          },
        },

        //Configuracion de la columna Correo"
        {
          field: "emailClient",
          headerName: "Correo",
          width: 120,
          filter: "agTextColumnFilter",
          filterParams: {
            debounceMs: 200,
            buttons: ["reset"],
          },
        },

        //Configuracion de la columna Acciones, y asignar los botones de accion creados anteriormente"
        {
          headerName: "Acciones",
          width: 120,
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
       * Funcion que se encarga de obtener datos de clientes que luego se mostraran en la tabla.
       */
      const getClients = async () => {
        try {
          //GET que obtiene la lista de clientes de la api.
          const businessData = JSON.parse(sessionStorage.getItem("business"));
          const response = await api.get(
            "/client/list-clients/" + businessData.idBusiness
          );

          //Se guarda la lista de clientes en la  referencia rowData.
          rowData.value = response.data.map((client) => {
            return {
              //Para mostrar en la tabla
              id: client.clientId,
              nameClient: client.nameClient,
              idClient: client.cifClient,
              addressClient: client.address,
              emailClient: client.email,
            };
          });
        } catch (err) {
          console.error("Clientes error:", err);
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
      //Funcion que sirve para cerrar el formuralio con emit saved.
      const formOpenOrCloseSaved = () => {
        if (!showForm.value) {
          showForm.value = true;
        } else {
          showForm.value = false;
          getClients();
          getDataInvoices();
        }
      };
      //Funcion que sirve para cerrar el formuralio que edita. Se vuelve a obtener los clientes actulizadas
      const formModOpenOrClose = () => {
        if (!showFormMod.value) {
          showFormMod.value = true;
        } else {
          showFormMod.value = false;
          getClients();
          getDataInvoices();
        }
      };
      //Funcion que actualiza la tabla, y los datos de los cuadros
      const updateTable = () => {
        getClients();
        getDataInvoices();
      };

      //Funcion que sirve para exportar los clientes seleccionados en excel.
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
                // Si es la columna Acciones, no lo guarda
                if (colDef.headerName !== "Acciones") {
                  rowData[colDef.headerName] = row[colDef.field];
                }
              });
              return rowData;
            });

            // Creacion del excel con los datos obtenidos.
            const ws = XLSX.utils.json_to_sheet(exportData);
            const wb = XLSX.utils.book_new();
            XLSX.utils.book_append_sheet(wb, ws, "Clientes");

            // Exportar el archivo excel
            XLSX.writeFile(wb, "Clientes.xlsx");
          } else {
            alertInfo("Selecciona al menos una fila para exportar.");
          }
        }
      };
      //Funcion que obtiene los datos para mostrarlos en los cuadros.
      const getDataInvoices = async () => {
        try {
          const businessData = JSON.parse(sessionStorage.getItem("business"));
          //GET que obtiene la lista de precios totales.
          const responseInvoiceTotal = await api.get(
            "/invoice/list-total-prices-business/" + businessData.idBusiness
          );

          //GET que obtiene el top de clientes
          const responseTopClients = await api.get(
            "/invoice/list-total-prices-clients/" + businessData.idBusiness
          );
          topClients.value = responseTopClients.data;

          responseInvoiceTotal.data.forEach((invoice) => {
            numClients.value = invoice.numClients;
            numInvoices.value = invoice.numInvoices;
            invoiceTotal.value = invoice.totalPrices;
          });
        } catch (err) {
          console.log("Error Datos De Clientes: ", err);
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
        selectedClient,
        topClients,
        numClients,
      };
    },
  };
}
