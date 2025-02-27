import api from "../../services/api.js";
import { alertSuccess } from "../../models/Alerts.js";

export function useRegisterBusiness() {
  return {
    data() {
      return {
        //Variables de datos del nuevo negocio
        nameBusiness: "",
        cifBusiness: "",
        emailBusiness: "",
        addressBusiness: "",
        phoneBusiness: "",
        accountNumberBusiness: "",
        //Variable de la pantalla de carga
        loader: false,
        //Mensaje de error
        error: "",
      };
    },
    //Se comprueba si el usuario tiene el token para acceder a la pagina.
    mounted() {
      const token = sessionStorage.getItem("token");
      if (!token) {
        alert("No tienes las acceso a esta pagina");
        this.$router.push("/");
      }
    },
    methods: {
      //Metodo que sirve para validar el identificador del negocio.Sirve para CIF,DNiI o NIF.
      validCif() {
        const regexp = /^[A-Za-z]\d{7}[0-9A-Za-z]$|^[0-9]{8}[A-Za-z]$/;
        //Se comprueba con .test si el cif pasa la validacion
        if (!regexp.test(this.cifBusiness)) {
          this.error =
            "El identificador debe ser un CIF (letra seguida de 8 dígitos) o un DNI/NIF (8 dígitos y una letra).";
        } else {
          //Invoca el metodo que registra el nuevo negocio en la base de datos.
          this.registerBusiness();
        }
      },

      //Metodo para registrar el nuevo negocio
      async registerBusiness() {
        this.error = "";
        try {
          this.loader = true;
          //POST del registro de nuevo negocio, con los datos obtenidos del formulario.
          const response = await api.post("/business/create", {
            nameBusiness: this.nameBusiness,
            cifBusiness: this.cifBusiness,
            email: this.emailBusiness,
            address: this.addressBusiness,
            phone: this.phoneBusiness,
            accountNumber: this.accountNumberBusiness,
          });
          //Si se registra, informa al usuario, y vuelve a la seleccion de negocios
          if (response.status === 202) {
            alertSuccess(response.data);
            this.backSelectBusiness();
          } else {
            this.error = "Fallo en el registro";
          }
        } catch (err) {
          //Si hay algun fallo, lo muestra en el mensaje de error.
          if (err.status === 409) {
            this.error = err.response.data;
          } else {
            this.error = "No hay conexion con el servidor";
          }
        } finally {
          this.loader = false;
        }
      },
      //Meotod que sirve para volver a la selecciones de negocios
      backSelectBusiness() {
        this.$router.push("/selectBusiness");
      },
    },
  };
}
