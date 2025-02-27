import { reactive, computed, ref, onMounted, watch } from "vue";
import api from "../../services/api.js";
import { alertInfo, alertSuccess } from "../../models/Alerts.js";
import Invoice from "../Invoice.vue";

export function UseFormInvoiceMod() {
  return {
    //Datos que pasara el componente padre
    props: {
      //Datos de la factura que se modificara
      invoiceData: {
        type: Object,
        required: true,
      },
      //Estado del formulario para que sea mostrado
      visible: {
        type: Boolean,
        required: true,
      },
    },
    //Comunica el evento close al componente padre.
    emits: ["close"],
    //El setup necesita recibir los props y el emit
    setup(props, { emit }) {
      //Array reactivo para el uso en la lista de items
      const clientes = reactive([]);
      //Referencia para la los datos del cliente seleccionado
      const selectedClient = ref(null);
      //Referencias para los datos del cliente y factura.
      const clientId = ref("");
      const clientCIF = ref("");
      const clientAddress = ref("");
      const newNumInvoice = ref("");
      const referenceInvoice = ref("");
      const dateInvoice = ref("");
      const iva = reactive({ porcentaje: 0 });
      const checkPaid = ref(false);

      //Al abrir la pagina, se obtiene la lista de clientes y se ingresa los datos de la factura seleccionada al formulario.
      onMounted(() => {
        getListClients();
        pushDataInvoice();
      });

      //Funcion que sirve para mostrar los datos de la factura que se va a editar. Los datos se consiguen del props.
      const pushDataInvoice = () => {
        clientCIF.value = props.invoiceData.clients.cifClient;
        clientId.value = props.invoiceData.clients.clientId;
        clientAddress.value = props.invoiceData.clients.address;
        newNumInvoice.value = props.invoiceData.num;
        dateInvoice.value = props.invoiceData.date;
        referenceInvoice.value = props.invoiceData.address;
        iva.porcentaje = props.invoiceData.iva;
        checkPaid.value = props.invoiceData.paid;
        selectedClient.value = props.invoiceData.clients.clientId;
      };
      //Funcion que mite el evento close
      const closeForm1 = () => {
        emit("close");
      };

      // Lista reactiva para las filas de la tabla
      // Se asignan los items de la factura seleccionada.
      const rows = reactive(
        props.invoiceData.invoicesItems.map((item) => ({
          serviceName: item.serviceName,
          quantity: item.quantity,
          price: item.price,
          total: item.total,
        }))
      );

      // Metodo para cerrar el formulario
      const closeForm = () => {
        emit("close");
      };

      // Funcion para agregar una nueva fila a la tabla
      const addRow = () => {
        rows.push({ serviceName: "", quantity: "", price: "", total: "" });
      };

      // Funcion que sirve para eliminar una fila de la tabla
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
            "Faltan datos para la factura",
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
          row.total = row.quantity * row.price;
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

      //Funcion que sirve para guarda la factura editada.
      const saveNewInvoice = async () => {
        try {
          let businessData = JSON.parse(sessionStorage.getItem("business"));
          //Antes de hacer el put, se calcula el total de los items de la factura.
          calculateTotals();
          //Se hace el PUT, pasandole los datos a la API.
          let response = await api.put("/invoice/update", {
            invoiceId: props.invoiceData.id,
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
          alertSuccess("Factura Editada");
          closeForm1();
        } catch (err) {
          console.log("Error Editar Factura: ", err);
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
