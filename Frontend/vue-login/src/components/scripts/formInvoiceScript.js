import { reactive, computed, ref, onMounted, watch } from "vue";
import api from "../../services/api.js";
import { alertInfo, alertSuccess } from "../../models/Alerts.js";

export function UseFormInvoice() {
  return {
    //Datos que pasara el componente padre
    props: {
      //Estado del formulario para que sea mostrado
      visible: {
        type: Boolean,
        required: true,
      },
    },
    //Comunica el evento close o saved al componente padre.
    emits: ["close", "saved"],
    //El setup necesita recibir  el emit
    setup(_, { emit }) {
      //Array reactivo para el uso en la lista de items
      const clientes = reactive([]);
      //Referencia para la los datos del cliente seleccionado
      const selectedClient = ref(null);
      //Referencias para los datos del cliente y factura.
      const clientId = ref("");
      const clientCIF = ref("");
      const clientAddress = ref("");
      const newNumInvoice = ref("");
      const checkPaid = ref(false);
      const referenceInvoice = ref("");
      const dateInvoice = ref("");
      const iva = reactive({ porcentaje: 0 });

      //Al montar la pagina, se invoca 3 funciones.
      onMounted(() => {
        getListClients();
        getNewNumInvoice();
        //Se asigna a la referencia el nuevo numero de factura que tendra la factura.
        newNumInvoice.value = getNewNumInvoice();
      });

      // Funcion que emite al padre el evento saved
      const closeForm = () => {
        emit("saved");
      };
      // Funcion que emite al padre el evento close
      const closeForm1 = () => {
        emit("close");
      };

      // Lista reactiva para las filas de la tabla
      const rows = reactive([
        { serviceName: "", quantity: "", price: "", total: "" },
      ]);

      // Funcion para agregar una nueva fila
      const addRow = () => {
        rows.push({ serviceName: "", quantity: "", price: "", total: "" });
      };

      // Funcion para eliminar una fila
      const removeRow = (index) => {
        rows.splice(index, 1);
      };

      // Funcion que comprueba los datos de la factura editada antes de guardarlos.
      const saveData = () => {
        //Se comprueba que ningun campo este vacio.
        const allFieldsFilled =
          rows.every(
            (row) =>
              row.serviceName.trim() !== "" &&
              row.quantity !== "" &&
              row.price !== ""
          ) &&
          referenceInvoice.value.trim() !== "" &&
          dateInvoice.value.trim() !== "" &&
          selectedClient.value !== null;

        if (!allFieldsFilled) {
          alertInfo(
            "Faltan datos para la nueva factura",
            "Por favor, rellena todos los campos antes de guardar."
          );
          return;
        }
        //Invoca el metodo que guardara los datos.
        saveNewInvoice();
      };

      // Funcion que calcula el subtotal de las filas automaticamente.
      const subtotal = computed(() => {
        return rows.reduce((total, row) => total + row.quantity * row.price, 0);
      });

      // Funcion para calcular el total de cada item
      const calculateTotals = () => {
        rows.forEach((row) => {
          row.total = row.quantity * row.price; // Calcular el total
        });
      };

      // Funcion que calcula el total de la factura con el iva automaticamente
      const total = computed(() => {
        const ivaAmount = subtotal.value * (iva.porcentaje / 100);
        return subtotal.value + ivaAmount;
      });

      //Funcion que sirve para obtener la lista de clientes
      const getListClients = async () => {
        try {
          //GET recibe los clientes de la empresa por id de la empresa.
          const businessData = JSON.parse(sessionStorage.getItem("business"));
          const response = await api.get(
            "client/list-clients/" + businessData.idBusiness
          );
          // Asignar los clientes obtenidos a la lista reactiva.
          clientes.splice(0, clientes.length, ...response.data);
        } catch (err) {
          console.log("Error Lista Clientes: " + err);
        }
      };

      //Funcion que obtiene el nuevo numero de la factura que se creara.
      const getNewNumInvoice = () => {
        //Se obtendra a partir del ultimo numero de la factura + 1 y el año de la fecha
        let year = new Date().getFullYear().toString().slice(-2);
        let numInvoice = sessionStorage.getItem("numInvoice");
        //Si no existe ninguna factura, o no existe una en ese año, se crea apartir de 1.
        if (year != numInvoice.split("-")[1]) {
          return "1-" + year;
        } else {
          let num = 1 + parseInt(numInvoice.split("-")[0]);
          return num + "-" + numInvoice.split("-")[1];
        }
      };

      //Watch que sirve para actualizar el cif y la direccion automaticamente cuando se selecciona un cliente.
      watch(selectedClient, (newClientId) => {
        //Busca el cliente seleccionado en la lista reactiva. Si lo encuentra, obtiene sus valores y los asigna.
        const selected = clientes.find(
          (client) => client.clientId === newClientId
        );
        if (selected) {
          clientId.value = selected.clientId;
          clientCIF.value = selected.cifClient;
          clientAddress.value = selected.address;
        }
      });
      //Funcion que sirve para guarda la factura creada.
      const saveNewInvoice = async () => {
        try {
          let businessData = JSON.parse(sessionStorage.getItem("business"));
          //Antes de hacer el put, se calcula el total de los items de la factura.
          calculateTotals();
          //Se hace el POST, pasandole los datos a la API.
          let response = await api.post("/invoice/create", {
            businessId: businessData.idBusiness,
            clientId: clientId.value,
            numInvoice: newNumInvoice.value,
            dateInvoice: dateInvoice.value,
            addressWork: referenceInvoice.value,
            subtotal: subtotal.value,
            iva: iva.porcentaje,
            total: total.value,
            paid: checkPaid.value,
            invoicesItems: rows,
          });
          alertSuccess("Nueva Factura Creada");
          closeForm();
        } catch (err) {
          console.log("Error Guardar Factura: ", err);
        }
      };
      //Variables y funciones que se usaran en el template
      return {
        closeForm1,
        closeForm,
        rows,
        addRow,
        removeRow,
        saveData,
        subtotal,
        total,
        iva,
        referenceInvoice,
        dateInvoice,
        clientes,
        selectedClient,
        clientCIF,
        clientAddress,
        newNumInvoice,
        saveNewInvoice,
        checkPaid,
      };
    },
  };
}
