import api from "../../services/api.js";
import { alertSuccess } from "../../models/Alerts.js";
import { ref } from "vue";

export function useEditBusiness() {
  return {
    props: {
      //Variable que le pasa el componente padre. Obtiene los datos de la factura seleccionada
      invoiceData: {
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
      //Referencias para los datos de la factura. Asignados del invoiceData del props
      const idBusiness = ref(props.invoiceData.businessId);
      const nameBusiness = ref(props.invoiceData.nameBusiness);
      const cifBusiness = ref(props.invoiceData.cifBusiness);
      const emailBusiness = ref(props.invoiceData.email);
      const addressBusiness = ref(props.invoiceData.address);
      const phoneBusiness = ref(props.invoiceData.phone);
      const accountNumberBusiness = ref(props.invoiceData.accountNumber);
      const error = ref("");

      //Funcion que sirve para guardar la factura editada.
      const saveEditBusiness = async () => {
        try {
          //PUT que guarda la factura editada
          const response = await api.put("business/update", {
            businessId: idBusiness.value,
            nameBusiness: nameBusiness.value,
            cifBusiness: cifBusiness.value,
            email: emailBusiness.value,
            address: addressBusiness.value,
            phone: phoneBusiness.value,
            accountNumber: accountNumberBusiness.value,
          });

          //Si recibe un status 202, informa del exito, y cierra el formulario
          if (response.status === 202) {
            alertSuccess("Negocio Editado");
            closeForm();
          } else {
            error.value = response.data;
          }
        } catch (err) {
          if (err.status === 409) {
            error.value = err.response.data;
          } else {
            console.log("Error Actualizacion Negocio: ", err);
          }
        }
      };

      //Funcion que sirve para validar el identificador del negocio.Sirve para CIF,DNiI o NIF.
      const validCif = () => {
        const regexp = /^[A-Za-z]\d{7}[0-9A-Za-z]$|^[0-9]{8}[A-Za-z]$/;
        //Se comprueba con .test si el cif pasa la validacion
        if (!regexp.test(cifBusiness.value)) {
          error.value =
            "El identificador debe ser un CIF (letra seguida de 8 dígitos) o un DNI/NIF (8 dígitos y una letra).";
        } else {
          //Invoca el metodo que guarda la factura
          saveEditBusiness();
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
        nameBusiness,
        cifBusiness,
        emailBusiness,
        addressBusiness,
        phoneBusiness,
        accountNumberBusiness,
        error,
        saveEditBusiness,
        validCif,
        closeForm,
        closeForm1,
      };
    },
  };
}
