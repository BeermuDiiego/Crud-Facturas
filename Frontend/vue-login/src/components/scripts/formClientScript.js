import api from "../../services/api.js";
import { alertSuccess } from "../../models/Alerts.js";
import { ref } from "vue";

export function UseCreateClient() {
  return {
    props: {
      //Variable para la visibilidad del formulario
      visible: {
        type: Boolean,
        required: true,
      },
    },
    //Eventos que informara al componente padre
    emits: ["close", "saved"],
    setup(_, { emit }) {
      //Referencias para los datos del cliente
      const nameClient = ref("");
      const cifClient = ref("");
      const emailClient = ref("");
      const addressClient = ref("");
      const error = ref("");

      //Funcion que sirve para guardar el cliente
      const saveClient = async () => {
        try {
          //POST que guarda el nuevo cliente
          const businessData = JSON.parse(sessionStorage.getItem("business"));
          const response = await api.post("client/create", {
            businessId: businessData.idBusiness,
            nameClient: nameClient.value,
            cifClient: cifClient.value,
            address: addressClient.value,
            email: emailClient.value,
          });

          //Si recibe un status 202, informa del exito, y cierra el formulario
          if (response.status === 202) {
            alertSuccess("Cliente Creado");
            closeForm();
          } else {
            error.value = response.data;
          }
        } catch (err) {
          if (err.status === 409) {
            error.value = err.response.data;
          } else {
            console.log("Error Crear Cliente: ", err);
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
          saveClient();
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
        saveClient,
        validCif,
        closeForm,
        closeForm1,
      };
    },
  };
}
