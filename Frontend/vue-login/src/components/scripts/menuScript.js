export function useMenu() {
  return {
    data() {
      return {
        //Datos del negocio para mostrar en el menu inferior
        idBusiness: "",
        nameBusiness: "",
        cifBusiness: "",
        //Variables para la logica del submenu despegable e icono.
        isSubmenuOpen: false,
        iconSubmenu: "▶",
      };
    },
    //Se revisa si tiene el token, o si el usuario a seleccionado alguna empresa.
    mounted() {
      const token = sessionStorage.getItem("token");
      const business = sessionStorage.getItem("business");

      if (!token) {
        alert("No tienes las acceso a esta pagina");
        this.$router.push("/");
      } else if (token && !business) {
        alert("No seleccionaste ninguna empresa");
        this.backSelectBusinesses();
      } else {
        this.getBusinessData();
      }
    },
    methods: {
      //Metodo que hace volver al login principal
      closeSession() {
        this.$router.push("/");
      },
      //Metodo que regresa a la seleccion de negocios
      backSelectBusinesses() {
        this.$router.push("/selectBusiness");
      },

      //Parsea el JSON  del negocio guardado en sessionStorage, y lo guarda en las variables.
      getBusinessData() {
        const business = JSON.parse(sessionStorage.getItem("business"));
        this.idBusiness = business.idBusiness;
        this.nameBusiness = business.nameBusiness;
        this.cifBusiness = business.cifBusiness;
      },
      //Metodo que sirve para dirigirse al dashboard
      goDashboard() {
        this.$router.push("/menu/dashboard");
      },
      //Metodo que sirve para dirigirse a la seccion de facturas
      goInvoice() {
        this.$router.push("/menu/invoice");
      },
      goClient() {
        this.$router.push("/menu/client");
      },

      //Metodo que sirve para la logica del submenu
      useSubmenu() {
        const submenu1 = document.getElementById("submenu1");
        const submenu2 = document.getElementById("submenu2");

        if (!this.isSubmenuOpen) {
          this.iconSubmenu = "▼";
          this.isSubmenuOpen = true;
          submenu1.style.height = "auto";
          submenu1.style.opacity = "1";
          submenu2.style.height = "auto";
          submenu2.style.opacity = "1";
        } else {
          this.iconSubmenu = "▶";
          this.isSubmenuOpen = false;
          submenu1.style.height = "0";
          submenu1.style.opacity = "0";
          submenu2.style.height = "0";
          submenu2.style.opacity = "0";
        }
      },
    },
  };
}
