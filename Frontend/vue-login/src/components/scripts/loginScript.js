import api from "../../services/api.js";

export function useLogin() {
  return {
    //Datos necesarios para el logeo y cambiar mensaje de error
    data() {
      return {
        email: "",
        password: "",
        error: "",
        cargando: false,
      };
    },
    //Al abrir la pagina, elimina los posibles tokens y empresas guardadas en el sessioStorage
    mounted() {
      sessionStorage.removeItem("token");
      sessionStorage.removeItem("business");
      sessionStorage.removeItem("numInvoice");
    },
    //Metodos
    methods: {
      //Metodo sincronizado que hace el post a la API para iniciar sesion.
      async handleLogin() {
        this.cargando = true;
        try {
          //Hace el POST para el inicio de sesion.
          const response = await api.post("/auth/login", {
            email: this.email,
            password: this.password,
          });
          //Si recibe el token, primero lo guarda en sessionStorage, y luego accede a la pagina de seleccion de negocios
          if (response.data && response.data.token) {
            const token = response.data.token;
            sessionStorage.setItem("token", token);
            this.$router.push("/selectBusiness");
          } else {
            this.error = "Credencial Invalida";
          }
        } catch (err) {
          //Control de errores de logeo.
          console.error("Login error:", err);
          if (err.code == "ERR_NETWORK") {
            this.error = "No hay conexión con el servidor";
          } else {
            this.error = "Credencial Invalida";
          }
        } finally {
          this.cargando = false;
        }
      },
      //Metodo que sirve para acceder a la parte de regristro de un nuevo usuario.
      goRegister() {
        this.$router.push("/register");
      },
    },
  };
}
