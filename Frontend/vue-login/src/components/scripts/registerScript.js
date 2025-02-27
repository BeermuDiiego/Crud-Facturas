import api from "../../services/api.js";
import { alertSuccess } from "../../models/Alerts.js";

export function useRegister() {
  return {
    data() {
      return {
        //Datos para el formulario
        name: "",
        surname: "",
        email: "",
        password: "",
        //Para la carga de la pantalla
        loader: false,
        //Mensaje de error
        error: "",
      };
    },
    //Al iniciar la pagina, borra en el sessionStorage el item del token. Por si es necesario.
    mounted() {
      sessionStorage.removeItem("token");
      sessionStorage.removeItem("numInvoice");
      sessionStorage.removeItem("business");
    },
    methods: {
      //Metodo sincronizado para el registro del nuevo usuario.
      async registerUser() {
        this.loader = true;
        try {
          //POST para el registro del nuevo usuario, los datos  se envian en formato JSON.
          const response = await api.post("/auth/register", {
            name: this.name,
            surname: this.surname,
            email: this.email,
            password: this.password,
          });
          //Si todo va bien, alerta al usuario del registro y lo devuelve al inicio.
          if (response.status === 202) {
            alertSuccess(response.data);
            this.$router.push("/");
          } else {
            this.error = "Fallo en el registro";
          }
          //Si sale mal, se advierte al usuario que el usuario no fue creado o hubo un error de conexion con el servidor.
        } catch (err) {
          if (err.status === 409) {
            this.error = "Usuario No Creado (Ese correo ya esta registrado)";
          } else {
            this.error = "No hay conexion con el servidor";
          }
        } finally {
          this.loader = false;
        }
      },
      //Meotod que sirve para volver al inicio
      backLogin() {
        this.$router.push("/");
      },
    },
  };
}
