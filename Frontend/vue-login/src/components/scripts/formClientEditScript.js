import api from "../../services/api.js";
import { alertSuccess } from "../../models/Alerts.js";
import { ref } from "vue";

export function UseEditClient() {
  return {
    props: {
      //Variable que le pasa el componente padre. Obtiene los datos del cliente seleccionado
      clientData: {
        type: Object,
        required: true,
      },
      //Variable para la visibilidad del formulario
      visible: {
        type: Boolean,
        required: true,
      },
    },
    //Eventos que informara al componente padre
    emits: ["close", "saved"],
    setup(props, { emit }) {
      //Referencias para los datos del cliente, obtenidos del padre
      const nameClient = ref(props.clientData.nameClient);
      const cifClient = ref(props.clientData.idClient);
      const emailClient = ref(props.clientData.emailClient);
      const addressClient = ref(props.clientData.addressClient);
      const error = ref("");

      //Funcion que sirve para guardar el cliente editado
      const saveEditClient = async () => {
        try {
          //PUT que guarda el cliente
          const businessData = JSON.parse(sessionStorage.getItem("business"));
          const response = await api.put("client/update", {
            clientId: props.clientData.id,
            businessId: businessData.idBusiness,
            nameClient: nameClient.value,
            cifClient: cifClient.value,
            address: addressClient.value,
            email: emailClient.value,
          });

          //Si recibe un status 202, informa del exito, y cierra el formulario
          if (response.status === 202) {
            alertSuccess("Cliente Modificado");
            closeForm();
          } else {
            error.value = response.data;
          }
        } catch (err) {
          if (err.status === 409) {
            error.value = err.response.data;
          } else {
            console.log("Error Modificar Cliente: ", err);
          }
        }
      };

      //Funcion que sirve para validar el identificador del cliente.Sirve para CIF,DNiI o NIF.
      const validCif = () => {
        const regexp = /^[A-Za-z]\d{7}[0-9A-Za-z]$|^[0-9]{8}[A-Za-z]$/;
        //Se comprueba con .test si el cif pasa la validacion
        if (!regexp.test(cifClient.value)) {
          error.value =
            "El identificador debe ser un CIF (letra seguida de 8 dígitos) o un DNI/NIF (8 dígitos y una letra).";
        } else {
          //Invoca el metodo que guarda el cliente
          saveEditClient();
        }
      };
      //Emite el evento saved
      const closeForm = () => {
        emit("saved");
      };
      //Emite el evento close
      const closeForm1 = () => {
        emit("close");
      };

      //Variables y funciones que se usaran en el template
      return {
        nameClient,
        cifClient,
        emailClient,
        addressClient,
        error,
        saveEditClient,
        validCif,
        closeForm,
        closeForm1,
      };
    },
  };
}
